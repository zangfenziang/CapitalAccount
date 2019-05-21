package hello.control;

import hello.model.CapitalAccountBanker;
import hello.model.CapitalAccountBankerRepository;
import hello.model.CapitalAccountUser;
import hello.model.CapitalAccountUserRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
public class StockController {

    private final static int maxAge = 60 * 60 * 24 * 90;
    private final static String BankerCookieName = "BankerStockCookie";
    private final static String BankerCookieIdName = "BankerStockCookieId";
    private final static String LegalUserType = "Legal";
    private final static String PersonalUserType = "Personal";
    private final static String LogoutType = "Logout";
    private final static String FrozenStatus = "Frozen";
    private final static String NormalStatus = "Normal";

    @Autowired
    private CapitalAccountBankerRepository capitalAccountBankerRepository;
    @Autowired
    private CapitalAccountUserRepository capitalAccountUserRepository;

    private String getBankStockCookie(String id, String password){
        return DigestUtils.md5DigestAsHex((id + DigestUtils.md5DigestAsHex(password.getBytes())).getBytes());
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
        else{
            return new SimpleStatus(1, "login failed please try again");
        }
    }

    @RequestMapping("/banker_login_status")
    public SimpleStatus getBankerLoginStatus(@CookieValue(value = BankerCookieIdName, defaultValue = "") String id, @CookieValue(value = BankerCookieName, defaultValue = "") String cookie){
        List<CapitalAccountBanker> list = capitalAccountBankerRepository.getBankerLoginById(id);
        for (CapitalAccountBanker banker: list) {
            if (cookie.equals(getBankStockCookie(banker.getId(), banker.getPassword()))) {
                return new SimpleStatus(0, "success");
            }
        }
        return new SimpleStatus(3, "please login again");
    }

    @RequestMapping("/user_find_by_banker")
    public @ResponseBody UserFindByBanker getUserByBanker(@CookieValue(value = BankerCookieIdName, defaultValue = "") String id
            , @CookieValue(value = BankerCookieName, defaultValue = "") String cookie
            , @RequestParam String account_id) {
        SimpleStatus status = getBankerLoginStatus(id, cookie);
        List<CapitalAccountUser> list;
        if (status.getStatus() == 0){
            list = capitalAccountUserRepository.getBankerUserById(account_id);
            for (CapitalAccountUser user: list){
                user.setPassword("");
            }
            return new UserFindByBanker(0, list);
        }
        else{
            list = new ArrayList<>();
            return new UserFindByBanker(1, list);
        }
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
        List<CapitalAccountUser> list = capitalAccountUserRepository.getBankerUserById(account_id);
        if (list.size() != 0){
            return new SimpleStatus(2, "user id exists");
        }
        CapitalAccountUser user = new CapitalAccountUser();
        user.setAccount_id(account_id);
        user.setPassword(password);
        user.setAccount_type(account_type);
        capitalAccountUserRepository.save(user);
        return new SimpleStatus(0, "success");
    }

//    @RequestMapping("/personaluser_add_by_banker")
//    public SimpleStatus addPersonalUserByBanker()

}
