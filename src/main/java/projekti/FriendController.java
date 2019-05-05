package projekti;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FriendController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FriendRepository friendRepository;
    
//    @Autowired
//    private PhotoObjectRepository photoRepository;
//

    @GetMapping("/friends")
    public String list(Model model) {

        model.addAttribute("friend", friendRepository.findAll());

        
        return "account";
        // return "redirect:/accounts";
    }

//    @GetMapping("/friendships/{id}")
//    public String getOne(Model model, @PathVariable Long id) {
////        Account account = accountRepository.getOne(id);
////        List<Friend> friends = friendRepository.findAll();
//// 
////        model.addAttribute("account", account);
////        model.addAttribute("accounts", accountRepository.findAll());
////        model.addAttribute("friends", friends);
//// 
//        return "friendship";
//    }
//    
}

