package com.qrcafe.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qrcafe.entity.Table;
import com.qrcafe.enums.TableStatus;
import com.qrcafe.service.TableService;

@RestController
@RequestMapping("/api/table")
public class TableController {
    @Autowired
    TableService tableService;

    @GetMapping("/")
    public ResponseEntity<?> getAllTable(){
        return ResponseEntity.status(HttpStatus.OK).body(tableService.getAllTable());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addTable(@RequestParam("tableName") String tableName){
        try {
            Table table = Table.builder().name(tableName).status(TableStatus.EMPTY).build();
            Table savedTable = tableService.save(table);
            return ResponseEntity.status(HttpStatus.OK).body(savedTable);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTableById(@PathVariable UUID id) {
        Object table = tableService.getTableById(id);
        if (table == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No item have this ID");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(table);
        }
    }

}
