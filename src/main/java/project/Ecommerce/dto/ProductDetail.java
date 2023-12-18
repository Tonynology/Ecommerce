package project.Ecommerce.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.Ecommerce.entity.Product;
import project.Ecommerce.entity.User;
import project.Ecommerce.type.CategoryType;
import project.Ecommerce.type.ProductQualityType;
import project.Ecommerce.type.ProductStatusType;

public class ProductDetail {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Response {

    private String productName;
    private double price;
    private String description;
    private ProductStatusType productStatus;
    private ProductQualityType product_quality;
    private CategoryType category;
    private String sellerName;
    private List<String> imagePath;

    public static Response toResponse(Product product, List<String> filePaths) {
      return Response.builder()
          .productName(product.getTitle())
          .price(product.getPrice())
          .description(product.getDescription())
          .productStatus(product.getProductStatus())
          .product_quality(product.getProduct_quality())
          .category(product.getCategory())
          .sellerName(product.getSeller().getName())
          .imagePath(filePaths)
          .build();
    }
  }
}
