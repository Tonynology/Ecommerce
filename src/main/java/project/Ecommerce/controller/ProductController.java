package project.Ecommerce.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import project.Ecommerce.dto.AddWatchList;
import project.Ecommerce.dto.Delete;
import project.Ecommerce.dto.DeleteWatchList;
import project.Ecommerce.dto.ProductDetail;
import project.Ecommerce.dto.SearchProduct;
import project.Ecommerce.dto.SearchProduct.Request;
import project.Ecommerce.dto.Update;
import project.Ecommerce.dto.Upload;
import project.Ecommerce.dto.UserDetail;
import project.Ecommerce.entity.document.ProductDocument;
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

  @GetMapping("/search")
  public ResponseEntity<Page<ProductDocument>> search(
      @RequestBody Request request,
      @PageableDefault Pageable pageable) {
    return ResponseEntity.ok(productService.searchProduct(request, pageable));
  }

  @PostMapping("/{productId}/watchlist")
  public ResponseEntity<AddWatchList.Response> addWatchList(
      @PathVariable Long productId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userEmail = ((UserDetail) authentication.getPrincipal()).getUsername();
    return ResponseEntity.ok(productService.addWatchList(productId, userEmail));
  }

  @DeleteMapping("/{productId}/watchlist")
  public ResponseEntity<DeleteWatchList.Response> deleteWatchList(
      @PathVariable Long productId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userEmail = ((UserDetail) authentication.getPrincipal()).getUsername();
    return ResponseEntity.ok(productService.deleteWatchList(productId, userEmail));
  }

  @GetMapping("/list")
  public ResponseEntity<Page<SearchProduct.Response>> getProductList() {
    return ResponseEntity.ok(productService.getProductList());
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<Update.Response> updateProduct(@PathVariable Long id,
      @RequestPart("file") List<MultipartFile> files,
      @RequestPart("updateProduct") @Valid Update.Request request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userEmail = ((UserDetail) authentication.getPrincipal()).getUsername();
    return ResponseEntity.ok(productService.updateProduct(id, userEmail, files, request));
  }

  @DeleteMapping("/delete/{productId}")
  public ResponseEntity<Delete.Response> deleteProduct(@PathVariable Long productId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userEmail = ((UserDetail) authentication.getPrincipal()).getUsername();
    return ResponseEntity.ok(productService.deleteProduct(productId, userEmail));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductDetail.Response> getProduct(@PathVariable Long id) {
    return ResponseEntity.ok(productService.getProduct(id));
  }
}
