package hello.control;

import hello.model.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@CrossOrigin
@RestController
public class StockController {

    private final static int maxAge = 60 * 60 * 24 * 90;
    private final static String BankerCookieName = "BankerStockCookie";
    private final static String BankerCookieIdName = "BankerStockCookieId";
    private final static String UserCookieName = "UserStockCookie";
    private final static String UserCookieIdName = "UserStockCookieId";
    private final static String LegalUserType = "Legal";
    private final static String PersonalUserType = "Personal";
    private final static String LogoutType = "Logout";
    private final static String FrozenStatus = "Frozen";
    private final static String NormalStatus = "Normal";

    @Autowired
    private CapitalAccountBankerRepository capitalAccountBankerRepository;
    @Autowired
    private CapitalAccountUserRepository capitalAccountUserRepository;
    @Autowired
    private CapitalAccountPersonalUserRepository capitalAccountPersonalUserRepository;
    @Autowired
    private CapitalAccountLegalUserRepository capitalAccountLegalUserRepository;

    private String getBankStockCookie(String id, String password){
        return DigestUtils.md5DigestAsHex((id + DigestUtils.md5DigestAsHex(password.getBytes())).getBytes());
    }
    private String getUserStockCookie(String id, String password){
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
    public SimpleStatus bankLogin(@RequestParam String user_id
            , @RequestParam String user_password
            , @RequestParam(defaultValue = "0") int remember_status
            , HttpServletResponse response) {
        user_password = DigestUtils.md5DigestAsHex(user_password.getBytes());
        List<CapitalAccountBanker> list = capitalAccountBankerRepository.getBankerLogin(user_id, user_password);
        if (list.size() > 0){
            String stockCookie = getBankStockCookie(user_id, user_password);
            Cookie cookie = new Cookie(BankerCookieName, stockCookie);
            Cookie cookieId = new Cookie(BankerCookieIdName, user_id);
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
                return new SimpleStatus(0, "success");
            }
        }
        return new SimpleStatus(3, "please login again");
    }

    private UserFindByBanker getUserByBanker(String id, String cookie, String account_id) {
        SimpleStatus status = getBankerLoginStatus(id, cookie);
        List<CapitalAccountUser> list;
        if (status.getStatus() == 0){
            list = capitalAccountUserRepository.getUserById(account_id);
            if (list.size() > 0) {
                CapitalAccountUser user = list.get(0);
                return new UserFindByBanker(0, user);
            }
            else{
                return new UserFindByBanker(1, null);
            }
        }
        else{
            return new UserFindByBanker(1, null);
        }
    }

    @RequestMapping("/user_find_by_banker")
    public UserFindByBanker getUserByBankerPublic(@CookieValue(value = BankerCookieIdName, defaultValue = "") String id
            , @CookieValue(value = BankerCookieName, defaultValue = "") String cookie
            , @RequestParam String account_id){
        UserFindByBanker result = getUserByBanker(id, cookie, account_id);
        if (result.getStatus() != 0){
            result.setUser(new CapitalAccountUser());
        }
        else{
            CapitalAccountUser user = new CapitalAccountUser();
            CapitalAccountUser oldUser = result.getUser();
            user.setAccount_id(oldUser.getAccount_id());
            user.setStatus(oldUser.getStatus());
            user.setPassword("");
            user.setAccount_type(oldUser.getAccount_type());
            result.setUser(user);
        }
        return result;
    }

    @RequestMapping("/user_add_by_banker")
    public @ResponseBody SimpleStatus addUserByBanker(@CookieValue(value = BankerCookieIdName, defaultValue = "") String id
            , @CookieValue(value = BankerCookieName, defaultValue = "") String cookie
            , @RequestParam String account_id
            , @RequestParam String password
            , @RequestParam String account_type){
        SimpleStatus status = getBankerLoginStatus(id, cookie);
        if (status.getStatus() != 0){
            return status;
        }
        List<CapitalAccountUser> list = capitalAccountUserRepository.getUserById(account_id);
        if (list.size() != 0){
            return new SimpleStatus(2, "user id exists");
        }
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        CapitalAccountUser user = new CapitalAccountUser();
        user.setAccount_id(account_id);
        user.setPassword(password);
        user.setAccount_type(account_type);
        user.setStatus(NormalStatus);
        capitalAccountUserRepository.save(user);
        return new SimpleStatus(0, "success");
    }

    private SimpleStatus setUserStatus(String id, String cookie, String account_id, String fromStatus, String toStatus){
        UserFindByBanker result = getUserByBanker(id, cookie, account_id);
        if (result.getStatus() != 0){
            return new SimpleStatus(result.getStatus(), "select failed");
        }
        CapitalAccountUser user = result.getUser();
        if (user == null){
            return new SimpleStatus(1, "user not exists");
        }
        if (!user.getStatus().equals(fromStatus)){
            return new SimpleStatus(1, "user is " + user.getStatus());
        }
        user.setStatus(toStatus);
        return new SimpleStatus(0, "success");
    }

    @Transactional
    @RequestMapping("/user_freeze_by_banker")
    public SimpleStatus freezeUserByBanker(@CookieValue(value = BankerCookieIdName, defaultValue = "") String id
            , @CookieValue(value = BankerCookieName, defaultValue = "") String cookie
            , @RequestParam String account_id){
        return setUserStatus(id, cookie, account_id, NormalStatus, FrozenStatus);
    }

    @Transactional
    @RequestMapping("/user_unfreeze_by_banker")
    public SimpleStatus unfreezeUserByBanker(@CookieValue(value = BankerCookieIdName, defaultValue = "") String id
            , @CookieValue(value = BankerCookieName, defaultValue = "") String cookie
            , @RequestParam String account_id){
        return setUserStatus(id, cookie, account_id, FrozenStatus, NormalStatus);
    }

    private int getUserStock(String account_id){
        return 0;
    }

    @RequestMapping("/user_delete_by_banker")
    public SimpleStatus deleteUserByBanker(@CookieValue(value = BankerCookieIdName, defaultValue = "") String id
            , @CookieValue(value = BankerCookieName, defaultValue = "") String cookie
            , @RequestParam String account_id){
        UserFindByBanker result = getUserByBanker(id, cookie, account_id);
        CapitalAccountUser user = result.getUser();
        if (result.getStatus() != 0 || user == null){
            return new SimpleStatus(1, "user not exists");
        }
        if (getUserStock(account_id) != 0){
            return new SimpleStatus(2, "user holds stock");
        }
        switch (user.getAccount_type()){
            case LegalUserType:
                capitalAccountLegalUserRepository.deleteByAccountID(user.getAccount_id());
                break;
            case PersonalUserType:
                capitalAccountPersonalUserRepository.deleteByAccountID(user.getAccount_id());
                break;
            default:
                break;
        }
        capitalAccountUserRepository.delete(user);
        return new SimpleStatus(0, "success");
    }

    @RequestMapping("/personal_user_add_by_banker")
    public SimpleStatus addPersonalUserByBanker(@CookieValue(value = BankerCookieIdName, defaultValue = "") String id
            , @CookieValue(value = BankerCookieName, defaultValue = "") String cookie
            , @RequestParam String account_id
            , @RequestParam String password
            , @RequestParam String name
            , @RequestParam String gender
            , @RequestParam String id_num
            , @RequestParam String address
            , @RequestParam String job
            , @RequestParam String degree
            , @RequestParam String organization
            , @RequestParam String phone_num
            , @RequestParam boolean agency
            , @RequestParam(defaultValue = "") String agent_id_num
    ){
        SimpleStatus status = addUserByBanker(id, cookie, account_id, password, PersonalUserType);
        if (status.getStatus() != 0){
            return status;
        }
        CapitalAccountPersonalUser user = new CapitalAccountPersonalUser(account_id
                , new Date()
                , name
                , gender
                , id_num
                , address
                , job
                , degree
                , organization
                , phone_num
                , agency
                , agent_id_num);
        capitalAccountPersonalUserRepository.save(user);
        return new SimpleStatus(0, "success");
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

    @RequestMapping("/legal_user_add_by_banker")
    public SimpleStatus addLegalUserByBanker(@CookieValue(value = BankerCookieIdName, defaultValue = "") String id
            , @CookieValue(value = BankerCookieName, defaultValue = "") String cookie
            , @RequestParam String account_id
            , @RequestParam String password
            , @RequestParam String legal_num
            , @RequestParam String license_num
            , @RequestParam String legal_name
            , @RequestParam String legal_id_num
            , @RequestParam String legal_address
            , @RequestParam String legal_phone
            , @RequestParam String authorize_name
            , @RequestParam String authorize_id_num
            , @RequestParam String authorize_address
            , @RequestParam String authorize_phone){
        SimpleStatus status = addUserByBanker(id, cookie, account_id, password, LegalUserType);
        if (status.getStatus() != 0){
            return status;
        }
        CapitalAccountLegalUser user = new CapitalAccountLegalUser(account_id
                , legal_num
                , license_num
                , legal_name
                , legal_id_num
                , legal_address
                , legal_phone
                , authorize_name
                , authorize_id_num
                , authorize_address
                , authorize_phone);
        capitalAccountLegalUserRepository.save(user);
        return new SimpleStatus(0, "success");
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

    @RequestMapping("/user_login")
    public SimpleStatus getUserLogin(@RequestParam String account_id
            , @RequestParam String password
            , @RequestParam(defaultValue = "0") int remember_status
            , HttpServletResponse response){
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        List<CapitalAccountUser> list = capitalAccountUserRepository.getUserLogin(account_id, password);
        if (list.size() != 1){
            return new SimpleStatus(1, "login failed please try again");
        }
        CapitalAccountUser user = list.get(0);
        if (!user.getStatus().equals(NormalStatus)){
            return new SimpleStatus(2, "user is " + user.getStatus());
        }
        Cookie cookie = new Cookie(UserCookieName, getUserStockCookie(account_id, password));
        Cookie cookieId = new Cookie(UserCookieIdName, account_id);
        return setCookie(response, remember_status, cookie, cookieId);
    }

    @RequestMapping("/user_login_status")
    public SimpleStatus getUserLoginStatus(@CookieValue(value = UserCookieIdName, defaultValue = "") String id, @CookieValue(value = UserCookieName, defaultValue = "") String cookie){
        List<CapitalAccountUser> list = capitalAccountUserRepository.getUserById(id);
        for (CapitalAccountUser banker: list) {
            if (banker.getStatus().equals(NormalStatus) && cookie.equals(getBankStockCookie(banker.getAccount_id(), banker.getPassword()))) {
                return new SimpleStatus(0, "success");
            }
        }
        return new SimpleStatus(3, "please login again");
    }

}
