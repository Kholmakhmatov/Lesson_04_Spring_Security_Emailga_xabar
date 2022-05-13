package uz.kholmakhmatov.lesson_04_spring_security_emailga_xabar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.kholmakhmatov.lesson_04_spring_security_emailga_xabar.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    Optional<User> findByEmailAndEmailCode(String email, String emailCode);

    Optional<User> findByEmail(String email);
}
