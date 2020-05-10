package com.color.pink.service;

import com.color.pink.dao.ArchiveMapper;
import com.color.pink.pojo.Archive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author HarrisonLee
 * @date 2020/4/16 15:34
 */
@Service
public class ArchiveService {

    @Autowired
    private ArchiveMapper archiveMapper;

    public boolean addArchive(Archive archive){
        return archiveMapper.addArchive(archive);
    }

    public List<Archive> selectAll(Boolean isAdmin) {
        List<Archive> list = null;
        if(!isAdmin) {
            list = archiveMapper.selectAllForClient();
            list = list.stream().filter(x -> x.getArticleNums() > 0).collect(Collectors.toList());
        } else {
            list = archiveMapper.selectAllForAdmin();
        }
        list.sort((x, y) -> y.getArticleNums() - x.getArticleNums());
        Objects.requireNonNull(list);
        return list;
    }


    public List<Archive> selectAllTitle() {
        return archiveMapper.selectAllTitle();
    }

    public Archive getArchiveByTitle(Boolean isAdmin, String archiveTitle) {
        return archiveMapper.getArchiveByTitle(isAdmin, archiveTitle);
    }
}
