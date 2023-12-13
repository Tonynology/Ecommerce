package project.Ecommerce.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.Ecommerce.entity.Product;
import project.Ecommerce.entity.User;
import project.Ecommerce.entity.WatchList;

@Repository
public interface WatchListRepository extends JpaRepository<WatchList, Long> {
  boolean existsWatchListByUserAndProduct(User user, Product product);
  Optional<WatchList> findWatchListByUserAndProduct(User user, Product product);
}
