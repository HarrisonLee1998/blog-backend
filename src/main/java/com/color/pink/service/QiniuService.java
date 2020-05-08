package com.color.pink.service;

import com.color.pink.util.Qiniu;
import com.qiniu.cdn.CdnManager;
import com.qiniu.cdn.CdnResult;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * @author HarrisonLee
 * @date 2020/4/14 15:42
 */
@Service
public class QiniuService {

    private static Logger logger = LoggerFactory.getLogger(QiniuService.class);

    private Qiniu qiniu;

    private Auth auth;

    private Configuration cfg;

    private  BucketManager bucketManager;

    private LocalDateTime lastGetTokenDateTime = null;

    private String tokenCache = null;

    @Autowired
    public QiniuService(Qiniu Qiniu){
        this.qiniu = Qiniu;
        auth = Auth.create(qiniu.getAccessKey(), qiniu.getSecretKey());
        cfg = new Configuration(Region.region0());
        bucketManager = new BucketManager(auth, cfg);
    }

    /**
     * 获取上传凭证
     * 默认的有效期的1个小时
     * @return
     */
    public String getQiniuToken(){
        String qiniu_token;
        if(Objects.nonNull(lastGetTokenDateTime)
                && Duration.between(LocalDateTime.now(), lastGetTokenDateTime).toMinutes() < 40) {
            logger.info("返回七牛token缓存");
            qiniu_token = tokenCache;
        }else {
            logger.info("向七牛请求token");
            qiniu_token=auth.uploadToken(qiniu.getBucket());
            tokenCache = qiniu_token;
            lastGetTokenDateTime = LocalDateTime.now();
        }
        return qiniu_token;
    }

    /**
     * 从空间中批量删除文件
     * @param params 包含批量删除的文件的key
     * @return
     * @throws IOException
     */
    public Boolean delFromQiniu(Map<String,Object>params) throws IOException {
        try {
            String[] keyList = (String[]) params.get("keyList");
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(qiniu.getBucket(), keyList);
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < keyList.length; i++) {
                BatchStatus status = batchStatusList[i];
                String key = keyList[i];
                System.out.print(key + "\t");
                if (status.code == 200) {
                    System.out.println("delete success");
                } else {
                    System.out.println(status.data.error);
                    return false;
                }
            }
            return true;
        } catch (QiniuException e) {
            throw e;
        }
    }

    /**
     * 获取域名空间流量
     */
    public void getDomainNameTraffic(){
        CdnManager c = new CdnManager(auth);
        //域名列表
        String[] domains = new String[]{
                "cdn.harrisonlee.net",
        };
        //开始和结束日期
        String fromDate = "2020-04-13";
        String toDate = "2020-04-14";
        //数据粒度，支持的取值为 5min ／ hour ／day
        String granularity = "day";
        try {
            CdnResult.FluxResult fluxResult = c.getFluxData(domains, fromDate, toDate, granularity);
            //处理得到的结果数据
            //...
            var map = fluxResult.data;
            System.out.println(map.size());
            for(var entry: map.entrySet()){
                System.out.println(entry.getKey()+" : "+entry.getValue());
            }
            System.out.println(fluxResult.code);
            System.out.println(fluxResult.error);
            System.out.println(Arrays.toString(fluxResult.time));
        } catch (QiniuException e) {
            System.err.println(e.response.toString());
        }
    }

    public void getDomainNameBandWidth(){
        CdnManager c = new CdnManager(auth);

        //域名列表
        String[] domains = new String[]{
                qiniu.getBucket()
        };
        //开始和结束日期
        String fromDate = "2020-04-11";
        String toDate = "2020-04-14";
        //数据粒度，支持的取值为 5min ／ hour ／day
        String granularity = "day";
        try {
            CdnResult.BandwidthResult bandwidthResult = c.getBandwidthData(domains, fromDate, toDate, granularity);
            //处理得到的结果数据
            //...
            System.out.println(bandwidthResult.code);
            System.out.println(bandwidthResult.error);
            System.out.println(Arrays.toString(bandwidthResult.time));
            bandwidthResult.data.forEach((key, value) -> {
                System.out.println(key+":"+value);
            });
        } catch (QiniuException e) {
            System.err.println(e.response.toString());
        }
    }

    /**
     * 获取空间文件列表
     */
    public void getFilesList(){
        //文件名前缀
        String prefix = "";
        //每次迭代的长度限制，最大1000，推荐值 1000
        int limit = 1000;
        //指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
        String delimiter = "";
        //列举空间文件列表
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(qiniu.getBucket(), prefix, limit, delimiter);
        while (fileListIterator.hasNext()) {
            //处理获取的file list结果
            FileInfo[] items = fileListIterator.next();
            for (FileInfo item : items) {
                System.out.println(item.key);
                System.out.println(item.hash);
                System.out.println(item.fsize);
                System.out.println(item.mimeType);
                System.out.println(item.putTime);
                System.out.println(item.endUser);
            }
        }
    }
}
