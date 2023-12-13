package project.Ecommerce.repository;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;
import project.Ecommerce.dto.SearchProduct.Request;
import project.Ecommerce.entity.document.ProductDocument;

@Repository
@RequiredArgsConstructor
public class ProductSearchNativeQueryRepository {

  private final ElasticsearchOperations elasticsearchOperations;

  public Page<ProductDocument> findByProductName(Request request, Pageable pageable) {
    String productName = request.getProductName();

    // boolQuery 필터로 검색 조건을 구성
    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
    if (productName != null && !productName.isEmpty()) {
      boolQueryBuilder.must(QueryBuilders.matchQuery("title", productName));
    }

    // 네이티브 검색 쿼리 생성
    NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
        .withQuery(boolQueryBuilder)
        .withPageable(pageable)
        .build();

    // es 쿼리 실행
    SearchHits<ProductDocument> searchHits =
        elasticsearchOperations.search(searchQuery, ProductDocument.class);

    // page 객체로 반환
    List<ProductDocument> productDocumentList = searchHits.getSearchHits()
        .stream()
        .map(SearchHit::getContent)
        .collect(Collectors.toList());

    return new PageImpl<>(productDocumentList, pageable, searchHits.getTotalHits());
  }
}
