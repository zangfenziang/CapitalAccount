package hello.model;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CapitalAccountPersonalUserRepository extends CrudRepository<CapitalAccountPersonalUser, Integer> {
    @Transactional
    @Modifying
    @Query("delete from CapitalAccountPersonalUser p where p.account_id = ?1")
    void deleteByAccountID(String account_id);

    @Query(value = "select p from CapitalAccountPersonalUser p where p.account_id = ?1")
    List<CapitalAccountPersonalUser> findByAccountId(String account_id);
}
