package hello.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CapitalAccountBankerRepository extends CrudRepository<CapitalAccountBanker, Integer> {
    @Query(value = "select p from CapitalAccountBanker p where p.id = ?1 and p.password = ?2")
    List<CapitalAccountBanker> getBankerLogin(String id, String password);
    @Query(value = "select p from CapitalAccountBanker p where p.id = ?1")
    List<CapitalAccountBanker> getBankerLoginById(String id);
}
