package com.qrcafe.service;

import java.util.List;
import java.util.UUID;

import com.qrcafe.entity.Table;

public interface TableService {
    Table getTableById(UUID id);

    Table save(Table table);

    Table updateAccessKey(Table table);

    List<Table> getAllTable();


}
