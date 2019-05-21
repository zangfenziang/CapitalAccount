package hello.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CapitalAccountUserRepository extends CrudRepository<CapitalAccountUser, Integer> {
    @Query(value = "select p from CapitalAccountUser p where p.account_id = ?1")
    List<CapitalAccountUser> getBankerUserById(String id);
}
