package com.color.pink;

import com.color.pink.service.QiniuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author HarrisonLee
 * @date 2020/4/14 17:23
 */
@SpringBootTest
public class QiniuTest {

    @Autowired
    private QiniuService qiniuService;

    @Test
    void getDomainNameTraffic(){
        qiniuService.getDomainNameTraffic();
    }

    @Test
    void getFilesList(){
        qiniuService.getFilesList();
    }

    @Test
    void getDomainNameBandWidth(){
        qiniuService.getDomainNameBandWidth();
    }

    @Test
    void getQiniuToken(){
        System.out.println(qiniuService.getQiniuToken());
    }
}
