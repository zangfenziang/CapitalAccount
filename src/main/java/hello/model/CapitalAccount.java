package hello.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class CapitalAccount {
    @Id
    private String user_id;
    private String securities_id;
    private String login_pwd;
    private String ID;
    private String user_right;
    private String status;
    @Column(columnDefinition = "decimal(10,2)")
    private BigDecimal fund;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSecurities_id() {
        return securities_id;
    }

    public void setSecurities_id(String securities_id) {
        this.securities_id = securities_id;
    }

    public String getLogin_pwd() {
        return login_pwd;
    }

    public void setLogin_pwd(String login_pwd) {
        this.login_pwd = login_pwd;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUser_right() {
        return user_right;
    }

    public void setUser_right(String user_right) {
        this.user_right = user_right;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getFund() {
        return fund;
    }

    public void setFund(BigDecimal fund) {
        this.fund = fund;
    }
}
