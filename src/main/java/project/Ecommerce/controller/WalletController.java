package project.Ecommerce.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.Ecommerce.dto.Charge;
import project.Ecommerce.dto.CreateWallet;
import project.Ecommerce.dto.Pay;
import project.Ecommerce.dto.Remain;
import project.Ecommerce.dto.UserDetail;
import project.Ecommerce.service.WalletService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/wallet")
public class WalletController {

  private final WalletService walletService;

  @PostMapping("/create")
  public ResponseEntity<CreateWallet.Response> createWallet() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userEmail = ((UserDetail) authentication.getPrincipal()).getUsername();
    return ResponseEntity.ok(walletService.createWallet(userEmail));
  }

  @PostMapping("/charge")
  public ResponseEntity<Charge.Response> charge(@RequestBody @Valid Charge.Request request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userEmail = ((UserDetail) authentication.getPrincipal()).getUsername();
    return ResponseEntity.ok(walletService.charge(request, userEmail));
  }

  @PostMapping("/payment/{productId}")
  public ResponseEntity<Pay.Response> pay(@PathVariable Long productId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userEmail = ((UserDetail) authentication.getPrincipal()).getUsername();
    return ResponseEntity.ok(walletService.pay(productId, userEmail));
  }

  @GetMapping("/remain")
  public ResponseEntity<Remain.Response> getRemain() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userEmail = ((UserDetail) authentication.getPrincipal()).getUsername();
    return ResponseEntity.ok(walletService.getRemain(userEmail));
  }

}
