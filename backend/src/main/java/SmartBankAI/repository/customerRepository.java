package SmartBankAI.repository;

import SmartBankAI.model.customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface customerRepository extends JpaRepository<customer, Integer> {
}
