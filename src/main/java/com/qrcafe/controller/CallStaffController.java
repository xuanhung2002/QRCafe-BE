package com.qrcafe.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qrcafe.dto.WsMessageDTO;
import com.qrcafe.service.TableService;

@RestController
@RequestMapping("/api/call-staff")
public class CallStaffController {
    @Autowired
    TableService tableService;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @GetMapping("/{tableId}")
    public ResponseEntity<?> callStaff(@PathVariable UUID tableId) {
        Object table = tableService.getTableById(tableId);
        if (table == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No item have this ID");
        }
        try {
            WsMessageDTO messageDTO = WsMessageDTO.builder()
                    .message("CALL_STAFF")
                    .data(table)
                    .build();
            messagingTemplate.convertAndSend("/topic/notify", messageDTO );
                        return ResponseEntity.status(HttpStatus.OK).body("Staff is coming");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Can't call staff now");
        }
    }
    
}
