package hous.core.domain.user.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import hous.core.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
}
