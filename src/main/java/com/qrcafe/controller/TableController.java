package com.qrcafe.controller;

import java.util.UUID;

import com.qrcafe.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/addTableAccessKey/{tableId}/{tableAccessKey}")
    public ResponseEntity<?> addTableAccessKey(@PathVariable("tableId") UUID tableId,
                                                @PathVariable("tableAccessKey") UUID tableAccessKey){
        try {
            Object tableObject = tableService.getTableById(tableId);
            if (tableObject == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No item have this ID");
            } else {
                Table table = (Table)tableObject;
                table.setTableAccessKey(tableAccessKey);
                tableService.updateAccessKey(table);
                return ResponseEntity.status(HttpStatus.OK).body(tableAccessKey);
            }
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Can't set table access key");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTable(@PathVariable UUID id){
        Table table = tableService.getTableById(id);
        if (table == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This table is not existed");
        }
        else {
            tableService.deleteTable(table);
            return ResponseEntity.status(HttpStatus.OK).body("Delete success");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTable(@PathVariable UUID id, @RequestParam(value = "name",required = true) String name){

        try {
            Table table = tableService.getTableById(id);
            if(table != null){
                table.setName(name);
                Table savedTable = tableService.save(table);
                return ResponseEntity.status(HttpStatus.OK).body(savedTable);
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This table is not existed");
            }
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
