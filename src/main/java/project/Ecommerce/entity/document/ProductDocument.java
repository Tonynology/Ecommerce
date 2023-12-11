package project.Ecommerce.entity.document;

import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;
import project.Ecommerce.entity.Category;
import project.Ecommerce.entity.Product;
import project.Ecommerce.type.ProductQualityType;
import project.Ecommerce.type.ProductStatusType;

/**
 * Elastic Search를 위한 document
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "product")
@Mapping(mappingPath = "elastic/product-mapping.json")
@Setting(settingPath = "elastic/elastic-setting.json")
public class ProductDocument {

  @Id
  private long id;

  private String title;

  private Category category;

  private ProductQualityType productQuality;

  private ProductStatusType productStatus;

  public static ProductDocument from(Product product) {
    return ProductDocument.builder()
        .id(product.getId())
        .title(product.getTitle())
        .category(product.getCategory())
        .productQuality(product.getProduct_quality())
        .productStatus(product.getProduct_status())
        .build();

  }
}
