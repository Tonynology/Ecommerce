package project.Ecommerce.entity;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import project.Ecommerce.dto.SearchProduct;
import project.Ecommerce.dto.Update;
import project.Ecommerce.type.CategoryType;
import project.Ecommerce.type.ProductQualityType;
import project.Ecommerce.type.ProductStatusType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  private double price;

  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "product_status")
  private ProductStatusType productStatus;

  @Enumerated(EnumType.STRING)
  private ProductQualityType product_quality;

  @Enumerated(EnumType.STRING)
  private CategoryType category;

  @Type(type = "json")
  @Column(columnDefinition = "longtext")
  private List<String> imagePath;

  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seller_id")
  private User seller;

  public SearchProduct.Response toDto() {
    return SearchProduct.Response.builder()
        .productName(this.title)
        .price(this.price)
        .description(this.description)
        .product_quality(this.product_quality)
        .productStatus(this.productStatus)
        .category(this.category)
        .build();
  }

  public void update(Update.Request request, List<String> imagePath) {
    this.title = request.getTitle();
    this.price = request.getPrice();
    this.description = request.getDescription();
    this.product_quality = request.getProduct_quality();
    this.productStatus = request.getProductStatus();
    this.category = request.getCategory();
    this.imagePath = imagePath;
  }

  public void update() {
    this.productStatus = ProductStatusType.SOLD;
  }
}
