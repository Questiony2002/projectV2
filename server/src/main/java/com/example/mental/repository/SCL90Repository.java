package com.example.mental.repository;

import com.example.mental.entity.SCL90Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SCL90Repository extends JpaRepository<SCL90Result, Long> {
    Optional<SCL90Result> findByUserId(Long userId);
    void deleteByUserId(Long userId);
} 