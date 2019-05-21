package hello.control;

import hello.model.CapitalAccountUser;

public class UserFindByBanker {
    private int status;
    private Iterable<CapitalAccountUser> iterable;

    public UserFindByBanker(int status, Iterable<CapitalAccountUser> iterable){
        this.status = status;
        this.iterable = iterable;
    }

    public int getStatus() {
        return status;
    }

    public Iterable<CapitalAccountUser> getIterable() {
        return iterable;
    }
}
