package com.qrcafe.service;

import com.qrcafe.entity.Table;

import java.util.List;
import java.util.UUID;

public interface TableService {
    Table getTableById(UUID id);

    Table save(Table table);

    List<Table> getAllTable();


}
