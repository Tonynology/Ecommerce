package project.Ecommerce.service;

import static project.Ecommerce.type.ErrorCode.LESS_MONEY_WALLET;
import static project.Ecommerce.type.ErrorCode.PRODUCT_NOT_FOUND;
import static project.Ecommerce.type.ErrorCode.PRODUCT_NOT_ONSAIL;
import static project.Ecommerce.type.ErrorCode.USER_NOT_FOUND;
import static project.Ecommerce.type.ErrorCode.WALLET_ALREADY_EXISTS;
import static project.Ecommerce.type.ErrorCode.WALLET_NOT_FOUND;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.Ecommerce.dto.Charge;
import project.Ecommerce.dto.CreateWallet;
import project.Ecommerce.dto.Pay;
import project.Ecommerce.dto.Remain;
import project.Ecommerce.entity.Product;
import project.Ecommerce.entity.User;
import project.Ecommerce.entity.Wallet;
import project.Ecommerce.exception.PaymentException;
import project.Ecommerce.exception.ProductException;
import project.Ecommerce.exception.UserException;
import project.Ecommerce.exception.WalletException;
import project.Ecommerce.repository.ProductRepository;
import project.Ecommerce.repository.UserRepository;
import project.Ecommerce.repository.WalletRepository;
import project.Ecommerce.type.ProductStatusType;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService{

  private final UserRepository userRepository;
  private final WalletRepository walletRepository;
  private final ProductRepository productRepository;

  @Override
  public CreateWallet.Response createWallet(String userEmail) {
    User user = userRepository.findUserByEmail(userEmail)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));

    Wallet existingWallet = walletRepository.findByUser(user);
    if (existingWallet != null) {
      throw new WalletException(WALLET_ALREADY_EXISTS);
    }

    walletRepository.save(Wallet.builder()
        .user(user)
        .totalMoney(0)
        .build());

    return CreateWallet.Response.toResponse(user.getName());
  }

  @Override
  @Transactional
  public Charge.Response charge(Charge.Request request, String userEmail) {

    User user = userRepository.findUserByEmail(userEmail)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));

    Wallet wallet = walletRepository.findByUser(user);
    if (wallet == null) {
      throw new WalletException(WALLET_NOT_FOUND);
    }

    double totalMoney = request.getDepositMoney() + wallet.getTotalMoney();

    wallet.update(totalMoney);
    walletRepository.save(wallet);

    return Charge.Response.toResponse(
        request.getDepositMoney(), totalMoney, user.getName());

  }

  @Override
  @Transactional
  public Pay.Response pay(Long productId, String userEmail) {

    User user = userRepository.findUserByEmail(userEmail)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));

    Wallet wallet = walletRepository.findByUser(user);
    if (wallet == null) {
      throw new WalletException(WALLET_NOT_FOUND);
    }

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

    if (!(product.getProductStatus().equals(ProductStatusType.SAIL))) {
      throw new PaymentException(PRODUCT_NOT_ONSAIL);
    }

    double remain = wallet.getTotalMoney() - product.getPrice();

    if (remain < 0) {
      throw new PaymentException(LESS_MONEY_WALLET);
    }

    wallet.update(remain);
    walletRepository.save(wallet);

    product.update();
    productRepository.save(product);

    return Pay.Response.toResponse(
        user.getName(), remain);
  }

  @Override
  public Remain.Response getRemain(String userEmail) {

    User user = userRepository.findUserByEmail(userEmail)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));

    Wallet wallet = walletRepository.findByUser(user);
    if (wallet == null) {
      throw new WalletException(WALLET_NOT_FOUND);
    }

    return Remain.Response.toResponse(user.getName(), wallet.getTotalMoney());
  }
}
