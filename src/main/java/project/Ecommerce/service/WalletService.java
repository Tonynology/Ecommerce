package project.Ecommerce.service;

import project.Ecommerce.dto.Charge;
import project.Ecommerce.dto.CreateWallet;
import project.Ecommerce.dto.Pay;
import project.Ecommerce.dto.Remain;
import project.Ecommerce.entity.Wallet;

public interface WalletService {

  CreateWallet.Response createWallet(String userEmail);
  Charge.Response charge(Charge.Request request, String userEmail);
  Pay.Response pay(Long productId, String userEmail);

  Remain.Response getRemain(String userEmail);

}
