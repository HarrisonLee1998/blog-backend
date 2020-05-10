package com.color.pink.dao;

import com.color.pink.pojo.Archive;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArchiveMapper {
    List<Archive> selectAllForAdmin();
    List<Archive> selectAllForClient();
    boolean addArchive(Archive archive);
    Archive getArchiveByTitle(@Param("isAdmin") Boolean isAdmin,
                              @Param("archiveTitle") String archiveTitle);
    List<Archive>selectAllTitle();
}