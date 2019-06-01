package hello.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CapitalAccountRepository extends CrudRepository<CapitalAccount, Integer> {
    @Query(value = "select p from CapitalAccount p where p.user_id = ?1 and p.login_pwd = ?2")
    List<CapitalAccount> getAccountLogin(String user_id, String login_pwd);
    @Query(value = "select p from CapitalAccount p where p.user_id = ?1")
    List<CapitalAccount> getAccount(String user_id);
}
