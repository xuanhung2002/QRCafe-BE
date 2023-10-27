package com.qrcafe.controller;

import com.qrcafe.converter.Converter;
import com.qrcafe.dto.ComboProductRequestDTO;
import com.qrcafe.dto.ComboRequestDTO;
import com.qrcafe.entity.Combo;
import com.qrcafe.entity.ComboProductDetails;
import com.qrcafe.entity.Product;
import com.qrcafe.service.ComboProductDetailsService;
import com.qrcafe.service.ComboService;
import com.qrcafe.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/combo")
public class ComboController {

    @Autowired
    ComboService comboService;
    @Autowired
    ProductService productService;
    @Autowired
    ComboProductDetailsService comboProductDetailsService;
    @Autowired
    Converter converter;

    @GetMapping("/")
    public ResponseEntity<?> getAllCombo(){
        List<Combo> combos = comboService.getAllCombos();

        if(combos.isEmpty()){
           return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Dont have any combo");
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(combos.stream().map(t -> converter.toComboDTO(t)).collect(Collectors.toList()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCombo(@RequestBody ComboRequestDTO comboRequestDTO){
        if(comboRequestDTO.getName() == null || comboRequestDTO.getPrice() == null || comboRequestDTO.getDetailsProducts().isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("name and price and details combo must not be null");
        }
        if(comboService.existedByName(comboRequestDTO.getName())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This combo name has already existed!!!");
        }
        Combo combo = Combo.builder()
                .name(comboRequestDTO.getName())
                .price(comboRequestDTO.getPrice())
                .description(comboRequestDTO.getDescription()).build();
        Combo savedCombo = comboService.save(combo);

        for (ComboProductRequestDTO comboProductRequestDTO: comboRequestDTO.getDetailsProducts()
             ) {
            Optional<Product> productOpt = productService.getProductById(comboProductRequestDTO.getProductId());
            if(productOpt.isPresent()){
                ComboProductDetails comboProductDetails = new ComboProductDetails();
                comboProductDetails.setProduct(productOpt.get());
                comboProductDetails.setCombo(savedCombo);
                comboProductDetails.setQuantity(comboProductRequestDTO.getQuantity());
                comboProductDetailsService.save(comboProductDetails);
            }
            else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("One of items not existed!!");
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("Add success!!!!!!!!!");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCombo(@PathVariable Long id, @RequestBody ComboRequestDTO comboRequestDTO){

        Combo combo = comboService.getComboById(id);
        if(combo == null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This combo is not existed!!");
        }
        else {
            if(comboRequestDTO.getName() == null || comboRequestDTO.getPrice() == null || comboRequestDTO.getDetailsProducts().isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("name and price and details combo must not be null");
            }
            if(comboService.existedByName(comboRequestDTO.getName())){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("This combo name has already existed!!!");
            }

            combo.setName(comboRequestDTO.getName());
            combo.setPrice(comboRequestDTO.getPrice());
            combo.setDescription(comboRequestDTO.getDescription());
            Combo savedCombo = comboService.save(combo);

            for (ComboProductDetails comboProductDetails: combo.getComboProductDetails()
            ) {
                comboProductDetailsService.delete(comboProductDetails);
            }

            for (ComboProductRequestDTO comboProductRequestDTO: comboRequestDTO.getDetailsProducts()
            ) {
                Optional<Product> productOpt = productService.getProductById(comboProductRequestDTO.getProductId());
                if(productOpt.isPresent()){
                    ComboProductDetails comboProductDetails = new ComboProductDetails();
                    comboProductDetails.setProduct(productOpt.get());
                    comboProductDetails.setCombo(savedCombo);
                    comboProductDetails.setQuantity(comboProductRequestDTO.getQuantity());
                    comboProductDetailsService.save(comboProductDetails);
                }
                else {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("One of items not existed!!");
                }
            }

            return ResponseEntity.status(HttpStatus.OK).body("Update success!!!!!!!!!");
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCombo(@PathVariable Long id){
        Combo combo = comboService.getComboById(id);
        if(combo == null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This combo is not existed!!");
        }
        else {
            comboService.delete(combo);
            return ResponseEntity.status(HttpStatus.OK).body("Delete successfully!!");
        }
    }

}
