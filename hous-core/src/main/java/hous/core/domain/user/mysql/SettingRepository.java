package hous.core.domain.user.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import hous.core.domain.user.Setting;

public interface SettingRepository extends JpaRepository<Setting, Long>, SettingRepositoryCustom {
}
