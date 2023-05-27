package com.example.sharingplatform.repository;

import com.example.sharingplatform.entity.comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface commentRepository extends JpaRepository<comment,Long> {
    List<comment> findByWorkIDOrderByCommentTimeDesc(long workID);
    comment findByCommentID(long commentID);
}
