package com.qrcafe.controller;

import com.qrcafe.service.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/QRCode")
public class QRCodeController {

  private static final String QR_CODE_IMAGE_PATH = "./src/main/resources/QRCode.png";

  @Autowired
  QRCodeService qrCodeService;


  @GetMapping(value = "/genrateAndDownloadQRCode/{codeText}/{width}/{height}")
  @PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN')")
  public void download(
          @PathVariable("codeText") String codeText,
          @PathVariable("width") Integer width,
          @PathVariable("height") Integer height)
          throws Exception {
    qrCodeService.generateQRCodeImage(codeText, width, height, QR_CODE_IMAGE_PATH);
  }

  @GetMapping(value = "/generateQRCode/{rootUrl}/{idTable}/{width}/{height}")
  @PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN')")
  public ResponseEntity<byte[]> generateQRCode(
          @PathVariable("rootUrl") String rootUrl,
          @PathVariable("idTable") String idTable,
          @PathVariable("width") Integer width,
          @PathVariable("height") Integer height)
          throws Exception {
    return ResponseEntity.status(HttpStatus.OK).body(qrCodeService.getQRCodeImage(rootUrl, idTable, width, height));
  }
}