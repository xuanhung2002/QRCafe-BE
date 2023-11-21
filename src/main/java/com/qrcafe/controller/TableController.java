package com.qrcafe.controller;

import com.qrcafe.entity.Table;
import com.qrcafe.enums.TableStatus;
import com.qrcafe.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        Object table = tableService.getTableById(id);
        if (table == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No item have this ID");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(table);
        }
    }

}
