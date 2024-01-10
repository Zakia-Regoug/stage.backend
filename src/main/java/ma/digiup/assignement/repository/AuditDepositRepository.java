package ma.digiup.assignement.repository;

import ma.digiup.assignement.domain.AuditDeposit;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AuditDepositRepository extends JpaRepository<AuditDeposit, Long> {
}
