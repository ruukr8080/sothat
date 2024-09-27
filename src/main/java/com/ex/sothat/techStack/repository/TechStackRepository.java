package com.ex.sothat.techStack.repository;

import com.ex.sothat.techStack.entity.TechStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TechStackRepository extends JpaRepository<TechStack, String>{
    public List<TechStack> findByTechStackStartingWith(String techStack);
    List<TechStack> findByTechStackIn(List<String> techStacks);
}
