package project.Ecommerce.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import project.Ecommerce.dto.Upload;
import project.Ecommerce.security.JwtTokenProvider;
import project.Ecommerce.service.ProductService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

  private final ProductService productService;
  private final JwtTokenProvider jwtTokenProvider;

  @PostMapping("/upload")
  public ResponseEntity<Upload.Response> upload(
      @RequestPart("file") List<MultipartFile> files,
      @RequestPart("upload") @Valid Upload.Request request,
      @RequestHeader("Authorization") String token) {

    String userEmail = jwtTokenProvider.getUserEmail(token);

    return ResponseEntity.ok(productService.upload(files, request, userEmail));
  }
}
