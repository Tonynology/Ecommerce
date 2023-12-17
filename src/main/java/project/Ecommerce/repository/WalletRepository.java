package project.Ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.Ecommerce.entity.User;
import project.Ecommerce.entity.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

  Wallet findByUser(User user);

}
