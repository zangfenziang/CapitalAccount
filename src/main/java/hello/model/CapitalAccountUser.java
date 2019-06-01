package hello.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CapitalAccountUser {
    @Id
    private String account_id;
    private String account_type;

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }
}
