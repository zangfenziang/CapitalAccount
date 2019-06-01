package hello.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CapitalAccountPersonalUserRepository extends CrudRepository<CapitalAccountPersonalUser, Integer> {
    @Query(value = "select p from CapitalAccountPersonalUser p where p.account_id = ?1")
    List<CapitalAccountPersonalUser> findByAccountId(String account_id);
}
