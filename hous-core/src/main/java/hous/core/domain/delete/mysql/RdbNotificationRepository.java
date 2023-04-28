package hous.core.domain.delete.mysql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hous.core.domain.delete.RdbNotification;

// TODO migration - 삭제 예정

@Repository
public interface RdbNotificationRepository extends JpaRepository<RdbNotification, Long> {
	List<RdbNotification> findAllByOrderByIdAsc();
}
