package com.qrcafe.service;

import com.qrcafe.entity.Table;

import java.util.List;

public interface TableService {
    Table getTableById(Long id);

    Table save(Table table);

    List<Table> getAllTable();


}
