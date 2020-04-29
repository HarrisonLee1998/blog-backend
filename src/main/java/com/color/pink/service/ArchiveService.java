package com.color.pink.service;

import com.color.pink.dao.ArchiveMapper;
import com.color.pink.pojo.Archive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
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

    public List<Archive> selectAllForClient(){
        var list = archiveMapper.selectAllForClient();
        list = list.stream().filter(x -> x.getArticles().size() > 0).collect(Collectors.toList());
        list.sort(Comparator.comparingInt(Archive::getOrder));
        Objects.requireNonNull(list);
        return list;
    }


    public List<Archive> selectAllForAdmin(){
        var list = archiveMapper.selectAllForAdmin();
        list.sort(Comparator.comparingInt(Archive::getOrder));
        Objects.requireNonNull(list);
        return list;
    }

    public Archive getArchiveByTitle(Boolean isAdmin, String archiveTitle) {
        return archiveMapper.getArchiveByTitle(isAdmin, archiveTitle);
    }
}
