package SmartBankAI.repository;

import SmartBankAI.model.customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface customerRepository extends JpaRepository<customer, Long> {
    Optional<customer> findByCustomerId(Long customerId);
}