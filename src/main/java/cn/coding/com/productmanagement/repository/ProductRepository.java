package cn.coding.com.productmanagement.repository;

import cn.coding.com.productmanagement.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
