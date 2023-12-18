package project.Ecommerce.service;

import static project.Ecommerce.type.ErrorCode.ALREADY_ADD_WATCHLIST;
import static project.Ecommerce.type.ErrorCode.DELETE_IMAGE_FAILED;
import static project.Ecommerce.type.ErrorCode.FAIL_UPLOAD_IMAGE;
import static project.Ecommerce.type.ErrorCode.NOT_FOUND_WATCHLIST;
import static project.Ecommerce.type.ErrorCode.PRODUCT_NOT_FOUND;
import static project.Ecommerce.type.ErrorCode.SELLER_DIFFERENT;
import static project.Ecommerce.type.ErrorCode.USER_NOT_FOUND;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.Ecommerce.dto.AddWatchList;
import project.Ecommerce.dto.Delete;
import project.Ecommerce.dto.DeleteWatchList;
import project.Ecommerce.dto.SearchProduct;
import project.Ecommerce.dto.SearchProduct.Request;
import project.Ecommerce.dto.Update;
import project.Ecommerce.dto.Upload;
import project.Ecommerce.entity.Product;
import project.Ecommerce.entity.User;
import project.Ecommerce.entity.WatchList;
import project.Ecommerce.entity.document.ProductDocument;
import project.Ecommerce.exception.ProductException;
import project.Ecommerce.exception.UserException;
import project.Ecommerce.exception.WatchListException;
import project.Ecommerce.repository.ProductRepository;
import project.Ecommerce.repository.ProductSearchNativeQueryRepository;
import project.Ecommerce.repository.ProductSearchRepository;
import project.Ecommerce.repository.UserRepository;
import project.Ecommerce.repository.WatchListRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

  @Value("${cloud.aws.s3.bucketName}")
  private String bucketName; //버킷 이름

  private final ProductRepository productRepository;
  private final UserRepository userRepository;
  private final AmazonS3Client amazonS3;
  private final ObjectMetadata objectMetadata;
  private final ProductSearchRepository productSearchRepository;
  private final ProductSearchNativeQueryRepository productSearchNativeQueryRepository;
  private final WatchListRepository watchListRepository;



  @Override
  @Transactional
  public Upload.Response upload(List<MultipartFile> images,
      Upload.Request request, String userEmail) {

    User user = userRepository.findUserByEmail(userEmail)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));

    List<String> imagePaths = uploadImages(images);

    Product product = productRepository.save(request.toEntity(user, imagePaths));
    log.info("document 저장");

    productSearchRepository.save(ProductDocument.from(product));

    return Upload.Response.toResponse(user.getName(), imagePaths);
  }

  @Override
  @Transactional
  public Update.Response updateProduct(
      Long id, String userEmail, List<MultipartFile> files, Update.Request request) {

    User user = userRepository.findUserByEmail(userEmail)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));

    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

    if (!(product.getSeller().equals(user))) {
      throw new ProductException(SELLER_DIFFERENT);
    }

    deleteImages(product);
    List<String> imagePaths = uploadImages(files);
    product.update(request, imagePaths);

    productRepository.save(product);
    productSearchRepository.delete(ProductDocument.from(product));
    productSearchRepository.save(ProductDocument.from(product));

    return Update.Response.toResponse(user.getName(), imagePaths);
  }

  /**
   * 최신 목록, sail 순으로 상품을 불러온다.
   * @return
   */
  @Override
  public Page<SearchProduct.Response> getProductList() {

    List<Product> products = productRepository.findByOrderByProductStatusAscUpdatedAtDesc();

    return new PageImpl<>(
        products.stream()
            .map(Product::toDto)
            .collect(Collectors.toList()));
  }

  /**
   * 상품 삭제
   * @param productId
   * @param userEmail
   * @return
   */
  @Override
  public Delete.Response deleteProduct(Long productId, String userEmail) {

    User user = userRepository.findUserByEmail(userEmail)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

    if (!(product.getSeller().equals(user))) {
      throw new ProductException(SELLER_DIFFERENT);
    }

    deleteImages(product);
    productRepository.delete(product);
    productSearchRepository.delete(ProductDocument.from(product));

    return Delete.Response.toResponse(user.getName(), product.getTitle());
  }

  /**
   * s3 이미지 삭제
   * @param product
   */
  private void deleteImages(Product product) {
    for (String imagePath : product.getImagePath()) {
      try {
        imagePath = imagePath.substring(imagePath.lastIndexOf("/") + 1);

        String fileName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
        String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
        String key = decodedFileName;

        amazonS3.deleteObject(bucketName, key);
        log.info("S3 object deleted successfully.");
      } catch (Exception e) {
        log.error("Error deleting S3 object", e);
        throw new ProductException(DELETE_IMAGE_FAILED);
      }
    }
  }

  /**
   * 상품 검색 기능
   * @param request
   * @param pageable
   * @return Page<productDocument>
   */
  @Override
  public Page<ProductDocument> searchProduct(Request request, Pageable pageable) {
    return productSearchNativeQueryRepository.findByProductName(request, pageable);
  }

  /**
   * 상품을 관심 목록에 추가
   * @param id
   * @param userEmail
   * @return
   */
  @Override
  @Transactional
  public AddWatchList.Response addWatchList(Long id, String userEmail) {
    log.info("addWatchList 시작");
    User user = userRepository.findUserByEmail(userEmail)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));

    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

    if (watchListRepository.existsWatchListByUserAndProduct(user, product)) {
      throw new WatchListException(ALREADY_ADD_WATCHLIST);
    }

    watchListRepository.save(WatchList.builder()
            .user(user)
            .product(product)
            .build());

    return AddWatchList.Response.toResponse(user.getEmail(), product.getTitle());
  }

  /**
   * 상품을 관심 목록에서 삭제
   * @param productId
   * @param userEmail
   * @return
   */
  @Override
  public DeleteWatchList.Response deleteWatchList(Long productId, String userEmail) {
    User user = userRepository.findUserByEmail(userEmail)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));


    WatchList watchList = watchListRepository.findWatchListByUserAndProduct(user, product)
            .orElseThrow(() -> new WatchListException(NOT_FOUND_WATCHLIST));

    watchListRepository.delete(watchList);

    return DeleteWatchList.Response.toResponse(user.getEmail(), product.getTitle());
  }

  /**
   * 이미지 업로드
   * @param images
   * @return imagePaths
   */
  private List<String> uploadImages (List<MultipartFile> images) {
    List<String> imagePaths = new ArrayList<>();

    images.forEach(image -> {
      if (!image.isEmpty()) {
        String originalImageName = image.getOriginalFilename();
        String newImageName = createImageName(originalImageName);

        try {
          amazonS3.putObject(bucketName, newImageName, image.getInputStream(), getMetadata(image));
        } catch (IOException e) {
          throw new ProductException(FAIL_UPLOAD_IMAGE);
        }
        String imagePath = amazonS3.getUrl(bucketName, newImageName).toString();
        imagePaths.add(imagePath);
      }
    });
    return imagePaths;
  }

  /**
   * 이미지 중복 방지를 위해 이미지 이름 랜덤으로 생성
   * @param originName
   * @return randomName + fileName
   */
  private String createImageName(String originName) {
    String randomName = UUID.randomUUID().toString();
    return randomName + originName;
  }

  /**
   * S3에 업로드를 위한 메타데이터
   * @param file
   * @return objectMetadata
   */
  private ObjectMetadata getMetadata(MultipartFile file) {
    objectMetadata.setContentLength(file.getSize());
    objectMetadata.setContentType(file.getContentType());
    return objectMetadata;
  }
}
