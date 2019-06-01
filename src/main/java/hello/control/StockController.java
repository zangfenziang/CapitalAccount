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

@CrossOrigin(origins = "http://localhost:3000", allowCredentials="true")
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
    @Autowired
    private CapitalAccountRepository capitalAccountRepository;

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
                return new SimpleStatus(0, id);
            }
        }
        return new SimpleStatus(3, "please login again");
    }

}
