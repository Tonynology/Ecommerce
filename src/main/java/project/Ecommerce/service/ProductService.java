package project.Ecommerce.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import project.Ecommerce.dto.Upload;

public interface ProductService {
  Upload.Response upload(List<MultipartFile> files,
      Upload.Request request, String userEmail);
}
