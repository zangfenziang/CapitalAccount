package hello.control;

import hello.model.*;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials="true")
@RestController
public class StockController {

    private final static int maxAge = 60 * 60 * 24 * 90;
    private final static String BankerCookieName = "BankerStockCookie";
    private final static String BankerCookieIdName = "BankerStockCookieId";
    private final static String AccountCookieName = "UserStockCookie";
    private final static String AccountCookieIdName = "UserStockCookieId";
    private final static String LockStatus = "Lock";
    private final static String NormalStatus = "Normal";
    private final static double eps = 1e-6;

    @Autowired
    private CapitalAccountBankerRepository capitalAccountBankerRepository;
    @Autowired
    private CapitalAccountUserRepository capitalAccountUserRepository;
    @Autowired
    private CapitalAccountPersonalUserRepository capitalAccountPersonalUserRepository;
    @Autowired
    private CapitalAccountLegalUserRepository capitalAccountLegalUserRepository;
    @Autowired
    private CapitalAccountRepository capitalAccountRepository;

    private String getBankStockCookie(String id, String password){
        return DigestUtils.md5DigestAsHex((id + DigestUtils.md5DigestAsHex(password.getBytes())).getBytes());
    }
    private String getAccountStockCookie(String id, String password){
        return DigestUtils.md5DigestAsHex((id + DigestUtils.md5DigestAsHex(password.getBytes())).getBytes());
    }

    private SimpleStatus setCookie(HttpServletResponse response, int remember_status, Cookie cookie, Cookie cookieId){
        switch (remember_status){
            case 0:
                response.addCookie(cookie);
                response.addCookie(cookieId);
                break;
            case 1:
                cookie.setMaxAge(maxAge);
                cookieId.setMaxAge(maxAge);
                response.addCookie(cookie);
                response.addCookie(cookieId);
                break;
            default:
                return new SimpleStatus(2, "status illegal");
        }
        return new SimpleStatus(0, "success");
    }

    @RequestMapping("/banker_login")
    public SimpleStatus bankLogin(@RequestParam String id
            , @RequestParam String password
            , @RequestParam(defaultValue = "0") int remember_status
            , HttpServletResponse response) {
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        List<CapitalAccountBanker> list = capitalAccountBankerRepository.getBankerLogin(id, password);
        if (list.size() > 0){
            String stockCookie = getBankStockCookie(id, password);
            Cookie cookie = new Cookie(BankerCookieName, stockCookie);
            Cookie cookieId = new Cookie(BankerCookieIdName, id);
            return setCookie(response, remember_status, cookie, cookieId);
        }
        else{
            return new SimpleStatus(1, "login failed please try again");
        }
    }

    @RequestMapping("/banker_login_status")
    public SimpleStatus getBankerLoginStatus(@CookieValue(value = BankerCookieIdName, defaultValue = "") String id, @CookieValue(value = BankerCookieName, defaultValue = "") String cookie){
        List<CapitalAccountBanker> list = capitalAccountBankerRepository.getBankerById(id);
        for (CapitalAccountBanker banker: list) {
            if (cookie.equals(getBankStockCookie(banker.getId(), banker.getPassword()))) {
                return new SimpleStatus(0, id);
            }
        }
        return new SimpleStatus(3, "please login again");
    }

    @RequestMapping("/account_login")
    public SimpleStatus accountLogin(@RequestParam String user_id
            , @RequestParam String login_pwd
            , @RequestParam(defaultValue = "0") int remember_status
            , HttpServletResponse response){
        login_pwd = DigestUtils.md5DigestAsHex(login_pwd.getBytes());
        List<CapitalAccount> list = capitalAccountRepository.getAccountLogin(user_id, login_pwd);
        if (list.size() == 0){
            return new SimpleStatus(1, "login failed please try again");
        }
        String accountCookie = getAccountStockCookie(user_id, login_pwd);
        Cookie cookie = new Cookie(AccountCookieName, accountCookie);
        Cookie cookieId = new Cookie(AccountCookieIdName, user_id);
        return setCookie(response, remember_status, cookie, cookieId);
    }

    @RequestMapping("/account_login_status")
    public SimpleStatus getAccountLoginStatus(@CookieValue(value = AccountCookieIdName, defaultValue = "") String id
            , @CookieValue(value = AccountCookieName, defaultValue = "") String cookie){
        List<CapitalAccount> list = capitalAccountRepository.getAccount(id);
        for (CapitalAccount account: list) {
            if (cookie.equals(getBankStockCookie(account.getUser_id(), account.getLogin_pwd()))) {
                return new SimpleStatus(0, id);
            }
        }
        return new SimpleStatus(3, "please login again");
    }

    private CapitalAccount getAccount(String id, String cookie) throws Exception{
        List<CapitalAccount> list = capitalAccountRepository.getAccount(id);
        if (list.size() == 0){
            throw new Exception("please login again");
        }
        CapitalAccount account = list.get(0);
        if (!cookie.equals(getBankStockCookie(account.getUser_id(), account.getLogin_pwd()))) {
            throw new Exception("please login again");
        }
        return account;
    }

    @RequestMapping("/account_fund")
    public SimpleStatus getAccountFund(@CookieValue(value = AccountCookieIdName, defaultValue = "") String id
            , @CookieValue(value = AccountCookieName, defaultValue = "") String cookie){
        try{
            CapitalAccount account = getAccount(id, cookie);
            return new SimpleStatus(0, account.getFund().toString());
        }
        catch (Exception e){
            return new SimpleStatus(3, e.getMessage());
        }
    }

    @Transactional
    @RequestMapping("/account_fund_change")
    public SimpleStatus setAccountFund(@CookieValue(value = AccountCookieIdName, defaultValue = "") String id
            , @CookieValue(value = AccountCookieName, defaultValue = "") String cookie
            , @RequestParam double number){
        try{
            CapitalAccount account = getAccount(id, cookie);
            BigDecimal fund = account.getFund();
            if (fund.doubleValue() + number < 0){
                return new SimpleStatus(4, "fund less than zero");
            }
            BigDecimal delta = new BigDecimal(number);
            BigDecimal result = fund.add(delta);
            account.setFund(result);
            return new SimpleStatus(0, "success");
        }
        catch (Exception e){
            return new SimpleStatus(3, e.getMessage());
        }
    }

    @RequestMapping("/user_find_by_banker")
    public UserFindByBanker getUserByBankerPublic(@CookieValue(value = BankerCookieIdName, defaultValue = "") String id
            , @CookieValue(value = BankerCookieName, defaultValue = "") String cookie
            , @RequestParam String account_id){
        SimpleStatus status = getBankerLoginStatus(id, cookie);
        List<CapitalAccountUser> list;
        if (status.getStatus() == 0){
            list = capitalAccountUserRepository.getUserById(account_id);
            if (list.size() > 0) {
                CapitalAccountUser user = list.get(0);
                return new UserFindByBanker(0, user);
            }
        }
        return new UserFindByBanker(1, new CapitalAccountUser());
    }

    @RequestMapping("/personal_user_find_by_banker")
    public CapitalAccountPersonalUser getPersonalUserByBanker(@CookieValue(value = BankerCookieIdName, defaultValue = "") String id
            , @CookieValue(value = BankerCookieName, defaultValue = "") String cookie
            , @RequestParam String account_id){
        SimpleStatus status = getBankerLoginStatus(id, cookie);
        if (status.getStatus() != 0){
            return new CapitalAccountPersonalUser();
        }
        List<CapitalAccountPersonalUser> list = capitalAccountPersonalUserRepository.findByAccountId(account_id);
        if (list.size() == 0){
            return new CapitalAccountPersonalUser();
        }
        return list.get(0);
    }

    @RequestMapping("/legal_user_find_by_banker")
    public CapitalAccountLegalUser getLegalUserByBanker(@CookieValue(value = BankerCookieIdName, defaultValue = "") String id
            , @CookieValue(value = BankerCookieName, defaultValue = "") String cookie
            , @RequestParam String account_id){
        if (getBankerLoginStatus(id, cookie).getStatus() != 0) {
            return new CapitalAccountLegalUser();
        }
        List<CapitalAccountLegalUser> list = capitalAccountLegalUserRepository.findByAccountId(account_id);
        if (list.size() == 0){
            return new CapitalAccountLegalUser();
        }
        return list.get(0);
    }

    @RequestMapping("/account_add_by_banker")
    public SimpleStatus addAccount(@CookieValue(value = BankerCookieIdName, defaultValue = "") String cookie_id
            , @CookieValue(value = BankerCookieName, defaultValue = "") String cookie
            , @RequestParam String user_id
            , @RequestParam String id
            , @RequestParam String login_pwd
            , @RequestParam String securities_id){
        SimpleStatus status = getBankerLoginStatus(cookie_id, cookie);
        if (status.getStatus() != 0){
            return status;
        }
        if (capitalAccountRepository.getAccount(user_id).size() != 0){
            return new SimpleStatus(1, "account exists");
        }
        if (capitalAccountUserRepository.getUserById(securities_id).size() == 0){
            return new SimpleStatus(2, "securities_id not exists");
        }
        login_pwd = DigestUtils.md5DigestAsHex(login_pwd.getBytes());
        CapitalAccount account = new CapitalAccount();
        account.setUser_id(user_id);
        account.setID(id);
        account.setFund(new BigDecimal(0));
        account.setLogin_pwd(login_pwd);
        account.setSecurities_id(securities_id);
        account.setStatus(NormalStatus);
        capitalAccountRepository.save(account);
        return new SimpleStatus(0, "success");
    }

    private CapitalAccount getAccountByBanker(String id, String cookie, String user_id) throws Exception {
        SimpleStatus status = getBankerLoginStatus(id, cookie);
        if (status.getStatus() != 0){
            throw new Exception(status.getMessage());
        }
        List<CapitalAccount> list = capitalAccountRepository.getAccount(user_id);
        if (list.size() == 0){
            throw new Exception("account not found");
        }
        return list.get(0);
    }

    @RequestMapping("/account_logout_by_banker")
    public SimpleStatus deleteAccount(@CookieValue(value = BankerCookieIdName, defaultValue = "") String id
            , @CookieValue(value = BankerCookieName, defaultValue = "") String cookie
            , @RequestParam String user_id){
        try{
            CapitalAccount account = getAccountByBanker(id, cookie, user_id);
            if (account.getFund().abs().doubleValue() > eps){
                return new SimpleStatus(2, "fund not equal zero");
            }
            capitalAccountRepository.delete(account);
            return new SimpleStatus(0, "success");
        }
        catch (Exception e){
            return new SimpleStatus(1, e.getMessage());
        }
    }

    private SimpleStatus setAccountStatus(CapitalAccount account, String fromStatus, String toStatus){
        if (!account.getStatus().equals(fromStatus)){
            return new SimpleStatus(2, "account status is " + account.getStatus());
        }
        account.setStatus(toStatus);
        return new SimpleStatus(0, "success");
    }

    @Transactional
    @RequestMapping("/account_lock_by_banker")
    public SimpleStatus lockAccount(@CookieValue(value = BankerCookieIdName, defaultValue = "") String id
            , @CookieValue(value = BankerCookieName, defaultValue = "") String cookie
            , @RequestParam String user_id){
        try{
            CapitalAccount account = getAccountByBanker(id, cookie, user_id);
            return setAccountStatus(account, NormalStatus, LockStatus);
        }
        catch (Exception e){
            return new SimpleStatus(1, e.getMessage());
        }
    }

    @Transactional
    @RequestMapping("/account_unlock_by_banker")
    public SimpleStatus unlockAccount(@CookieValue(value = BankerCookieIdName, defaultValue = "") String id
            , @CookieValue(value = BankerCookieName, defaultValue = "") String cookie
            , @RequestParam String user_id){
        try{
            CapitalAccount account = getAccountByBanker(id, cookie, user_id);
            return setAccountStatus(account, LockStatus, NormalStatus);
        }
        catch (Exception e){
            return new SimpleStatus(1, e.getMessage());
        }
    }

}
