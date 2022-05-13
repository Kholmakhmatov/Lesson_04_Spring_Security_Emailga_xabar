package uz.kholmakhmatov.lesson_04_spring_security_emailga_xabar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.kholmakhmatov.lesson_04_spring_security_emailga_xabar.entity.Product;

import java.util.UUID;
@RepositoryRestResource(path = "product")
public interface ProductRepository extends JpaRepository<Product, UUID> {
}
