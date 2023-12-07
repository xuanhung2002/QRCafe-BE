package com.qrcafe.controller;

import com.qrcafe.converter.Converter;
import com.qrcafe.dto.ComboRequestDTO;
import com.qrcafe.entity.Combo;
import com.qrcafe.service.ComboProductDetailsService;
import com.qrcafe.service.ComboService;
import com.qrcafe.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<?> getAllCombo() {
        List<Combo> combos = comboService.getAllCombos();

        if (combos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Dont have any combo");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(combos.stream().map(t -> converter.toComboDTO(t)).collect(Collectors.toList()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getComboById(@PathVariable Long id) {
        if (comboService.getComboById(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No item have this ID");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(converter.toComboDTO(comboService.getComboById(id)));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCombo(@RequestBody ComboRequestDTO comboRequestDTO) {
        try {
            comboService.addCombo(comboRequestDTO);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Add success!!!!!!!!!");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCombo(@PathVariable Long id, @RequestBody ComboRequestDTO comboRequestDTO) {

        Combo combo = comboService.getComboById(id);
        if (combo == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This combo is not existed!!");
        } else {
            try {
                comboService.updateCombo(combo, comboRequestDTO);
            }
            catch (Exception e){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.OK).body("Update success!!!!!!!!!");
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCombo(@PathVariable Long id) {
        Combo combo = comboService.getComboById(id);
        if (combo == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This combo is not existed!!");
        } else {
            comboService.delete(combo);
            return ResponseEntity.status(HttpStatus.OK).body("Delete successfully!!");
        }
    }

    @DeleteMapping("/deleteCombos")
    public ResponseEntity<?> deleteCombos(@RequestBody List<Long> comboIds){
        try {
            comboService.deleteCombosByIds(comboIds);
            return ResponseEntity.status(HttpStatus.OK).body("Delete success");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

}
