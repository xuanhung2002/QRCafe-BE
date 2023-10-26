package com.qrcafe.controller;

import com.qrcafe.converter.Converter;
import com.qrcafe.dto.CommentRequestDTO;
import com.qrcafe.entity.Comment;
import com.qrcafe.entity.Product;
import com.qrcafe.service.CommentService;
import com.qrcafe.service.ProductService;
import com.qrcafe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;
    @Autowired
    Converter converter;

    @GetMapping("/")
    public ResponseEntity<?> getCommentOfProduct(@RequestParam("productId") Long productId) {
        List<Comment> comments = commentService.getCommentsByProductId(productId);
        if (comments != null) {
            return ResponseEntity.status(HttpStatus.OK).body(comments.stream().map(c -> converter.toCommentResponseDTO(c)).collect(Collectors.toList()));
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Dont have any comment of this product");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCommentForProduct(Authentication authentication, @RequestBody CommentRequestDTO commentRequestDTO) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Let's login!!");
        }

        Optional<Product> productOpt = productService.getProductById(commentRequestDTO.getProductId());
        if (productOpt.isPresent()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            Comment comment = Comment.builder()
                    .description(commentRequestDTO.getDescription())
                    .user(userService.getUserByUsername(username))
                    .product(productOpt.get()).build();
            Comment savedComment = commentService.save(comment);

            return ResponseEntity.status(HttpStatus.OK).body(converter.toCommentResponseDTO(savedComment));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This product ID is not exist");
        }

    }

    @DeleteMapping("/userDeleteComment/{id}")
    public ResponseEntity<?> userDeleteComment(Authentication authentication, @PathVariable Long id) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Let's login!!");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        Comment comment = commentService.getCommentById(id);
        if (comment != null) {
            if (comment.getUser().getUsername().equals(username)) {
                commentService.delete(comment);
                return ResponseEntity.status(HttpStatus.OK).body("Delete successfully!!");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You dont have permission!!");
            }

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This comment is not existed!!");
        }
    }

    @PutMapping("/updateComment/{id}")
    public ResponseEntity<?> userUpdateComment(Authentication authentication, @PathVariable Long id,
                                               @RequestParam(value = "newDescription") String newDescription){
        Comment comment = commentService.getCommentById(id);
        if(comment != null){
            comment.setDescription(newDescription);
            Comment savedComment = commentService.save(comment);
            return ResponseEntity.status(HttpStatus.OK).body(converter.toCommentResponseDTO(savedComment));
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This comment is not existed!!");
        }
    }


    //authorize this api for admin
    @DeleteMapping("/adminDeleteComment/{id}")
    public ResponseEntity<?> adminDeleteComment(@PathVariable Long id) {
        Comment comment = commentService.getCommentById(id);
        if (comment != null) {
                commentService.delete(comment);
                return ResponseEntity.status(HttpStatus.OK).body("Delete successfully!!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This comment is not existed!!");
        }
    }

}
