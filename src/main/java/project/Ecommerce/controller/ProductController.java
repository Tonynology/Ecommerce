package project.Ecommerce.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import project.Ecommerce.dto.Upload;
import project.Ecommerce.dto.UserDetail;
import project.Ecommerce.service.ProductService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

  private final ProductService productService;

  @PostMapping("/upload")
  public ResponseEntity<Upload.Response> upload(
      @RequestPart("file") List<MultipartFile> files,
      @RequestPart("upload") @Valid Upload.Request request) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userEmail = ((UserDetail) authentication.getPrincipal()).getUsername();
    log.info("userEmail : {}", userEmail);

    return ResponseEntity.ok(productService.upload(files, request, userEmail));
  }
}
