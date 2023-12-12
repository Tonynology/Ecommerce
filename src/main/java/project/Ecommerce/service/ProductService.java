package project.Ecommerce.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import project.Ecommerce.dto.SearchProduct.Request;
import project.Ecommerce.dto.Upload;
import project.Ecommerce.entity.document.ProductDocument;

public interface ProductService {
  Upload.Response upload(List<MultipartFile> files,
      Upload.Request request, String userEmail);

  Page<ProductDocument> searchProduct(Request request, Pageable pageable);
}
