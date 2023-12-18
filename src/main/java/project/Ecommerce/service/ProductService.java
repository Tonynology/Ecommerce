package project.Ecommerce.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import project.Ecommerce.dto.AddWatchList;
import project.Ecommerce.dto.Delete;
import project.Ecommerce.dto.DeleteWatchList;
import project.Ecommerce.dto.ProductDetail;
import project.Ecommerce.dto.SearchProduct;
import project.Ecommerce.dto.SearchProduct.Request;
import project.Ecommerce.dto.Update;
import project.Ecommerce.dto.Upload;
import project.Ecommerce.entity.document.ProductDocument;

public interface ProductService {
  Upload.Response upload(List<MultipartFile> files,
      Upload.Request request, String userEmail);

  Page<ProductDocument> searchProduct(Request request, Pageable pageable);

  AddWatchList.Response addWatchList(Long id, String userEmail);

  DeleteWatchList.Response deleteWatchList(Long id, String userEmail);

  Page<SearchProduct.Response> getProductList();

  Update.Response updateProduct(
      Long id, String userEmail, List<MultipartFile> files, Update.Request request);

  Delete.Response deleteProduct(Long productId, String userEmail);

  ProductDetail.Response getProduct(Long id);
}
