package com.color.pink.dao;

import com.color.pink.pojo.Archive;

import java.util.List;

public interface ArchiveMapper {
    List<Archive> selectAll();
    List<Archive> selectAll2();
    boolean addArchive(Archive archive);
}