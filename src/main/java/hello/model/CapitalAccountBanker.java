package hello.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CapitalAccountBanker{
    @Id
    private String id;
    private String password;
    private String status;
    public String getId(){
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}