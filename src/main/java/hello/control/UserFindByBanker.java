package hello.control;

import hello.model.CapitalAccountUser;

public class UserFindByBanker {
    private int status;
    private CapitalAccountUser user;

    public UserFindByBanker(int status, CapitalAccountUser user) {
        this.status = status;
        this.user = user;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public CapitalAccountUser getUser() {
        return user;
    }

    public void setUser(CapitalAccountUser user) {
        this.user = user;
    }
}
