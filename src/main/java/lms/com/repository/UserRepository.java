package lms.com.repository;

import lms.com.dtos.UserDTO;
import lms.com.entity.User;
import lms.com.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUserRole(Role role);
}
