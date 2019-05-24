package hello.model;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CapitalAccountLegalUserRepository extends CrudRepository<CapitalAccountLegalUser, Integer> {
    @Transactional
    @Modifying
    @Query("delete from CapitalAccountLegalUser p where p.account_id = ?1")
    void deleteByAccountID(String account_id);

    @Query(value = "select p from CapitalAccountLegalUser p where p.account_id = ?1")
    List<CapitalAccountLegalUser> findByAccountId(String account_id);
}
