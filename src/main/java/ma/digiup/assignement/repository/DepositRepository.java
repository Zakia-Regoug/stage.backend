package ma.digiup.assignement.repository;

import ma.digiup.assignement.domain.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositRepository extends JpaRepository<Deposit, Long> {
}
