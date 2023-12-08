package com.qrcafe.controller;

import com.qrcafe.converter.Converter;
import com.qrcafe.entity.Image;
import com.qrcafe.entity.Product;
import com.qrcafe.service.CategoryService;
import com.qrcafe.service.ImageService;
import com.qrcafe.service.ProductService;
import com.qrcafe.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ImageService imageService;

    @Autowired
    FileUtils fileUtils;

    @Autowired
    Converter converter;

    @GetMapping("")
    public ResponseEntity<?> getAllProduct(@RequestParam(name = "page", defaultValue = "0") Integer pageNo,
                                           @RequestParam(name = "size", defaultValue = "2147483647") Integer pageSize,
                                           @RequestParam(name = "sortBy", defaultValue = "name-asc") String sortBy) {
        List<Product> products = productService.getAll(pageNo, pageSize, sortBy);
        return new ResponseEntity<>(products.stream().map(p -> converter.toProductDTO(p)).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        if (productService.getProductById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No item have this ID");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(converter.toProductDTO(productService.getProductById(id).get()));
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getProductByCategoryName(@RequestParam(value = "categoryName", required = true) String categoryName,
                                                      @RequestParam(name = "page", defaultValue = "0") Integer pageNo,
                                                      @RequestParam(name = "size", defaultValue = "2147483647") Integer pageSize,
                                                      @RequestParam(name = "sortBy", defaultValue = "name-asc") String sortBy) {

        if (categoryService.getCategoryByCategoryName(categoryName) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid category name");
        } else {
            List<Product> products = productService
                    .getProductByCategoryName(categoryService.getCategoryByCategoryName(categoryName), pageNo, pageSize, sortBy);
            return ResponseEntity.status(HttpStatus.OK).body(products.stream().map(t -> converter.toProductDTO(t)).collect(Collectors.toList()));

        }
    }


    @GetMapping("/search")
    public ResponseEntity<?> searchProduct(@RequestParam("searchKey") String searchKey){
        if(productService.searchProduct(searchKey) != null){
            return ResponseEntity.status(HttpStatus.OK).body(productService.searchProduct(searchKey).stream().map(p -> converter.toProductDTO(p)).collect(Collectors.toList()));
        }
        else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No have product with this search key");
        }
    }



    @PostMapping ("/add")
    ResponseEntity<?> addNewProduct(@RequestParam(value = "name", required = true) String name,
                                    @RequestParam(value = "price", required = true) Double price,
                                    @RequestParam(value = "description", required = true) String description,
                                    @RequestParam(value = "amount", required = true) Integer amount,
                                    @RequestParam(value = "images", required = true) List<MultipartFile> images,
                                    @RequestParam(value = "categoryName", required = true) String categoryName
    )
    {
        if(categoryService.getCategoryByCategoryName(categoryName) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category is not existed");
        }
        Product product = Product.builder()
                .name(name)
                .price(price)
                .description(description)
                .amount(amount)
                .category(categoryService.getCategoryByCategoryName(categoryName)).build();

        Product savedProduct = productService.save(product);

        List<Image> savedImage = new ArrayList<>();

        images.forEach(t -> {
            try {
                savedImage.add(new Image(fileUtils.uploadImage(t), savedProduct));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        //add to show in response
        if(savedProduct.getImages() == null) {
            savedProduct.setImages(savedImage);
        }
        else {
            savedProduct.getImages().addAll(savedImage);
        }

        savedImage.forEach(i -> imageService.save(i));


        return new ResponseEntity<>(converter.toProductDTO(savedProduct), HttpStatus.OK);
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,
                                           @RequestParam(value = "name", required = true) String name,
                                           @RequestParam(value = "price", required = true) Double price,
                                           @RequestParam(value = "description", required = true) String description,
                                           @RequestParam(value = "amount", required = true) Integer amount,
                                           @RequestParam(value = "images", required = true) List<MultipartFile> images,
                                           @RequestParam(value = "categoryName", required = true) String categoryName){

        Optional<Product> productOpt = productService.getProductById(id);
        if(productOpt.isPresent()){
            Product product = productOpt.get();
            product.setName(name);
            product.setPrice(price);
            product.setDescription(description);
            product.setAmount(amount);
            product.setCategory(categoryService.getCategoryByCategoryName(categoryName));

            List<Image> oldImage = product.getImages();
            if(oldImage != null){
                oldImage.forEach(t -> {
                    try {
                        fileUtils.deleteImageInCloudinary(t.getImageUrl());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            List<Image> newImages = new ArrayList<>();
            images.forEach(t -> {
                try {
                    newImages.add(new Image(fileUtils.uploadImage(t), product));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            product.setImages(newImages);

            Product savedProduct = productService.save(product);

            return ResponseEntity.status(HttpStatus.OK).body(savedProduct != null ? "Update success" : "Failed update");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("product is not existed");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
        Optional<Product> productOpt = productService.getProductById(id);

        if(productOpt.isPresent()){
            Product product = productOpt.get();
            productService.delete(product);
            return ResponseEntity.status(HttpStatus.OK).body("Delete success");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("product hasn't existed");
        }
    }

}
