package com.qrcafe.service;

import com.qrcafe.entity.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CommentService {
    List<Comment> getCommentsByProductId(Long productId);

    Comment save(Comment comment);

    Comment getCommentById(Long id);

    void delete(Comment comment);
}
