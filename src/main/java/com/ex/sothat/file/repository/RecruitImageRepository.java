//package com.ex.sothat.file.repository;
//
//import com.ex.sothat.file.entity.RecruitImage;
//import jakarta.transaction.Transactional;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface RecruitImageRepository extends JpaRepository<RecruitImage, Long> {
//
//    List<RecruitImage> findByRecruitId(Long recruitId);
//
//    @Transactional
//    @Modifying
//    void deleteByRecruitId(Long recruitId);
//}
