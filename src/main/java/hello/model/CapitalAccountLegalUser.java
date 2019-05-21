package hello.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class CapitalAccountLegalUser {
    @Id
    private String account_id;
    private String legal_num;
    private String license_num;
    private String legal_name;
    private String legal_id_num;
    private String legal_address;
    private String legal_phone;
    private String authorize_name;
    private String authorize_id_num;
    private String authorize_address;
    private String authorize_phone;

    public CapitalAccountLegalUser(String account_id, String legal_num, String license_num, String legal_name, String legal_id_num, String legal_address, String legal_phone, String authorize_name, String authorize_id_num, String authorize_address, String authorize_phone) {
        this.account_id = account_id;
        this.legal_num = legal_num;
        this.license_num = license_num;
        this.legal_name = legal_name;
        this.legal_id_num = legal_id_num;
        this.legal_address = legal_address;
        this.legal_phone = legal_phone;
        this.authorize_name = authorize_name;
        this.authorize_id_num = authorize_id_num;
        this.authorize_address = authorize_address;
        this.authorize_phone = authorize_phone;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getLegal_num() {
        return legal_num;
    }

    public void setLegal_num(String legal_num) {
        this.legal_num = legal_num;
    }

    public String getLicense_num() {
        return license_num;
    }

    public void setLicense_num(String license_num) {
        this.license_num = license_num;
    }

    public String getLegal_name() {
        return legal_name;
    }

    public void setLegal_name(String legal_name) {
        this.legal_name = legal_name;
    }

    public String getLegal_id_num() {
        return legal_id_num;
    }

    public void setLegal_id_num(String legal_id_num) {
        this.legal_id_num = legal_id_num;
    }

    public String getLegal_address() {
        return legal_address;
    }

    public void setLegal_address(String legal_address) {
        this.legal_address = legal_address;
    }

    public String getLegal_phone() {
        return legal_phone;
    }

    public void setLegal_phone(String legal_phone) {
        this.legal_phone = legal_phone;
    }

    public String getAuthorize_name() {
        return authorize_name;
    }

    public void setAuthorize_name(String authorize_name) {
        this.authorize_name = authorize_name;
    }

    public String getAuthorize_id_num() {
        return authorize_id_num;
    }

    public void setAuthorize_id_num(String authorize_id_num) {
        this.authorize_id_num = authorize_id_num;
    }

    public String getAuthorize_address() {
        return authorize_address;
    }

    public void setAuthorize_address(String authorize_address) {
        this.authorize_address = authorize_address;
    }

    public String getAuthorize_phone() {
        return authorize_phone;
    }

    public void setAuthorize_phone(String authorize_phone) {
        this.authorize_phone = authorize_phone;
    }
}
