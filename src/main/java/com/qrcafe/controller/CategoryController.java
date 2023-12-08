package com.qrcafe.controller;

import com.qrcafe.entity.Category;
import com.qrcafe.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<?> getAllCategory(){
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getAllCategory());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody Category category){

        Category c = categoryService.save(category);
        if(c == null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This category name has existed");
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(c);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id){
        Category category = categoryService.getCategoryById(id);
        if (category == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This category is not existed");
        }
        else {
            categoryService.deleteCategory(category);
            return ResponseEntity.status(HttpStatus.OK).body("Delete success");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestParam(value = "name",required = true) String name){

        Category category = categoryService.getCategoryById(id);
        if(category != null){
            category.setName(name);
            Category savedCategory = categoryService.save(category);
            return ResponseEntity.status(HttpStatus.OK).body(savedCategory);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This category is not existed");
        }

    }

}
