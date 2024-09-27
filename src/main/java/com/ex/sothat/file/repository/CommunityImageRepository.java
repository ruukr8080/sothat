//package com.ex.sothat.file.repository;
//
//import com.ex.sothat.file.entity.CommunityImage;
//import jakarta.transaction.Transactional;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface CommunityImageRepository extends JpaRepository<CommunityImage, Long> {
//
//    List<CommunityImage> findByCommunityId(Long communityId);
//
//    @Transactional
//    @Modifying
//    void deleteByCommunityId(Long communityId);
//}
