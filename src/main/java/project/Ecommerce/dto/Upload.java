package project.Ecommerce.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.Ecommerce.entity.Category;
import project.Ecommerce.entity.Product;
import project.Ecommerce.entity.User;
import project.Ecommerce.type.ProductQualityType;
import project.Ecommerce.type.ProductStatusType;

public class Upload {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Request {
    private String title;
    private double price;
    private String description;
    private ProductStatusType product_status;
    private ProductQualityType product_quality;
    private Category category;

    public Product toEntity(User user, List<String> imagePaths) {
      return Product.builder()
          .title(this.title)
          .price(this.price)
          .description(this.description)
          .product_quality(this.product_quality)
          .product_status(this.product_status)
          .category(this.category)
          .seller_id(user)
          .imagePath(imagePaths)
          .build();
    }
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Response {
    private String userName;
    List<String> imagePaths;
    private String message;
    public static Response toResponse(String userName, List<String> imagePaths) {
      return Response.builder()
          .userName(userName)
          .imagePaths(imagePaths)
          .message("상품이 등록되었습니다.")
          .build();
    }
  }
}
