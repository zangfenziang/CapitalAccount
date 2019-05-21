package hello.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class CapitalAccountPersonalUser {
    @Id
    private String account_id;
    private Date date;
    private String name;
    private String gender;
    private String id_num;
    private String address;
    private String job;
    private String degree;
    private String organization;
    private String phone_num;
    private boolean agency;
    private String agent_id_num;

    public CapitalAccountPersonalUser(){}

    public CapitalAccountPersonalUser(String account_id
            , Date date
            , String name
            , String gender
            , String id_num
            , String address
            , String job
            , String degree
            , String organization
            , String phone_num
            , boolean agency
            , String agent_id_num){
        this.account_id = account_id;
        this.date = date;
        this.name = name;
        this.gender = gender;
        this.id_num = id_num;
        this.address = address;
        this.job = job;
        this.degree = degree;
        this.organization = organization;
        this.phone_num = phone_num;
        this.agency = agency;
        this.agent_id_num = agent_id_num;
    }

    public String getAccount_id() {
        return account_id;
    }

    public Date getDate() {
        return date;
    }

    public boolean getAgency() {
        return agency;
    }

    public String getGender() {
        return gender;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getAgent_id_num() {
        return agent_id_num;
    }

    public String getDegree() {
        return degree;
    }

    public String getId_num() {
        return id_num;
    }

    public String getJob() {
        return job;
    }

    public String getOrganization() {
        return organization;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAgency(boolean agency) {
        this.agency = agency;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAgent_id_num(String agent_id_num) {
        this.agent_id_num = agent_id_num;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public void setId_num(String id_num) {
        this.id_num = id_num;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }
}
