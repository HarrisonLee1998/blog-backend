package com.color.pink.service;

import com.color.pink.dao.ArchiveMapper;
import com.color.pink.pojo.Archive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * @author HarrisonLee
 * @date 2020/4/16 15:34
 */
@Service
public class ArchiveService {

    @Autowired
    private ArchiveMapper archiveMapper;

    public boolean addArchive(Archive archive){
        archive.setCreateDate(LocalDateTime.now());
        archive.setViewTimes(0);
        return archiveMapper.addArchive(archive);
    }

    public List<Archive> selectAll(boolean admin){
        var list = admin? archiveMapper.selectAll(): archiveMapper.selectAll2();
        list.sort(Comparator.comparingInt(Archive::getOrder));
        return list;
    }
}
