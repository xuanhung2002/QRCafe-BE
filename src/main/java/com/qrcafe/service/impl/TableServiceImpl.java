package com.qrcafe.service.impl;

import com.qrcafe.entity.Table;
import com.qrcafe.repository.TableRepository;
import com.qrcafe.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TableServiceImpl implements TableService {
    @Autowired
    TableRepository tableRepository;
    @Override
    public Table getTableById(UUID id) {
        return tableRepository.findById(id).orElse(null);
    }

    @Override
    public Table save(Table table) {
        if(tableRepository.existsByName(table.getName())){
            throw new IllegalArgumentException("this table name is existed");
        }
        return tableRepository.save(table);
    }

    @Override
    public List<Table> getAllTable() {
        return tableRepository.findAll();
    }
}
