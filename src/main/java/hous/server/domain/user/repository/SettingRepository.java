package hous.server.domain.user.repository;

import hous.server.domain.user.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingRepository extends JpaRepository<Setting, Long>, SettingRepositoryCustom {
}
