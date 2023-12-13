package project.Ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.Ecommerce.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long > {
}
