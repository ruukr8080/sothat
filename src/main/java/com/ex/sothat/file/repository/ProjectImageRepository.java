package com.ex.sothat.file.repository;

import com.ex.sothat.file.entity.ProjectImage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectImageRepository extends JpaRepository<ProjectImage, Long> {

    List<ProjectImage> findByProjectId(Long projectId);

    @Transactional
    @Modifying
    void deleteByProjectId(Long projectId);
}
