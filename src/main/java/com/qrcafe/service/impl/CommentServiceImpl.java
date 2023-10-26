package com.qrcafe.service.impl;

import com.qrcafe.entity.Comment;
import com.qrcafe.repository.CommentRepository;
import com.qrcafe.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentRepository commentRepository;

    @Override
    public List<Comment> getCommentsByProductId(Long productId) {
        List<Comment> comments = commentRepository.findCommentsByProductId(productId);
        if (comments.isEmpty()) {
            return null;
        } else {
            return comments;
        }
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment getCommentById(Long id) {
        Optional<Comment> commentOpt = commentRepository.findById(id);
        return commentOpt.orElse(null);
    }

    @Override
    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }
}
