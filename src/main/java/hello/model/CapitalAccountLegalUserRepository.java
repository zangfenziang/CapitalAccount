package hello.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CapitalAccountLegalUserRepository extends CrudRepository<CapitalAccountLegalUser, Integer> {
    @Query(value = "select p from CapitalAccountLegalUser p where p.account_id = ?1")
    List<CapitalAccountLegalUser> findByAccountId(String account_id);
}
