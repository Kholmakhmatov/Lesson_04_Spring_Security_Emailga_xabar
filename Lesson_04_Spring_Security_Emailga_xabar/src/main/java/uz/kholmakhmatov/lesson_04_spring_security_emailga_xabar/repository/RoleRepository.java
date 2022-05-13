package uz.kholmakhmatov.lesson_04_spring_security_emailga_xabar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.kholmakhmatov.lesson_04_spring_security_emailga_xabar.entity.Role;
import uz.kholmakhmatov.lesson_04_spring_security_emailga_xabar.entity.enums.RoleName;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleName(RoleName roleName);
}
