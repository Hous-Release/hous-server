package hous.core.domain.user.mysql;

import hous.core.domain.user.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingRepository extends JpaRepository<Setting, Long>, SettingRepositoryCustom {
}
