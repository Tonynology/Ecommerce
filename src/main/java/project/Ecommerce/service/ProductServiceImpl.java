package project.Ecommerce.service;

import static project.Ecommerce.type.ErrorCode.FAIL_UPLOAD_IMAGE;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.Ecommerce.dto.SearchProduct.Request;
import project.Ecommerce.dto.Upload;
import project.Ecommerce.entity.Product;
import project.Ecommerce.entity.User;
import project.Ecommerce.entity.document.ProductDocument;
import project.Ecommerce.exception.ProductException;
import project.Ecommerce.exception.UserException;
import project.Ecommerce.repository.ProductRepository;
import project.Ecommerce.repository.ProductSearchNativeQueryRepository;
import project.Ecommerce.repository.ProductSearchRepository;
import project.Ecommerce.repository.UserRepository;
import project.Ecommerce.type.ErrorCode;

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



  @Override
  @Transactional
  public Upload.Response upload(List<MultipartFile> images,
      Upload.Request request, String userEmail) {

    User user = userRepository.findUserByEmail(userEmail)
        .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

    List<String> imagePaths = uploadImages(images);

    Product product = productRepository.save(request.toEntity(user, imagePaths));
    log.info("document 저장");

    productSearchRepository.save(ProductDocument.from(product));

    return Upload.Response.toResponse(user.getName(), imagePaths);
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
          amazonS3.putObject(bucketName, originalImageName, image.getInputStream(), getMetadata(image));
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
