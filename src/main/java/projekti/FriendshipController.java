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
public class FriendshipController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;
    
    @Autowired
    private PhotoObjectRepository photoRepository;


    @GetMapping("/friendships")
    public String list(Model model) {

        model.addAttribute("friendships", friendshipRepository.findAll());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = accountRepository.findByUsername(username);
        Account account = accountRepository.getOne(user.getId());
        model.addAttribute("account", account);

        return "friendships";
        // return "redirect:/accounts";
    }

    @GetMapping("/friendships/{id}")
    public String getOne(Model model, @PathVariable Long id) {
//        Account account = accountRepository.getOne(id);
//        List<Friend> friends = friendRepository.findAll();
// 
//        model.addAttribute("account", account);
//        model.addAttribute("accounts", accountRepository.findAll());
//        model.addAttribute("friends", friends);
// 
        return "friendship";
    }
    
    @PostMapping("/friendships")
    public String createProfile() { //(@RequestParam String profileName)//frienship

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = accountRepository.findByUsername(username);
        Account account = accountRepository.getOne(user.getId());
        String profilename=account.getProfilename();
        
        //tässä laitetaan nimeksi profiilin tunnus
        List<Friendship> friendships = account.getFriendships();     
        
        //jos profiili jo olemassa ei luoda toista
        if (friendships.size() > 0) {
            for (Friendship p : friendships) {
                if (p.getAccounts().get(0).getUsername().equalsIgnoreCase(username)) {
                    System.out.println("olet jo luonut profiilin");  
              break;
            }
            }
        }
        else{ 
          if (friendshipRepository.findByprofileName(profilename) == null &&(friendships.size() < 1)) {
                        System.out.println("ei oo profiilia");
                        Friendship friendship = new Friendship();;
                        //friendship.setAccepted(false);
                        friendshipRepository.save(friendship);
                        friendship.setProfileName(profilename);
                        friendship.getAccounts().add(account);
                        account.getFriendships().add(friendship);
                        friendshipRepository.save(friendship); 
                        accountRepository.save(account);
       
          }   

        }    return "redirect:/account/profiles";

    }
    

    @PostMapping("/friendships/account/{id}")
    public String createFriendShip(@PathVariable Long id) { //(@RequestParam String profileName)//frienship

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = accountRepository.findByUsername(username);
        Account account = accountRepository.getOne(user.getId());
        String profilename=account.getProfilename();
        
        //tässä laitetaan nimeksi profiilin tunnus
        List<Friendship> friendships = account.getFriendships();     
        
    //jos profiili jo olemassa ei luoda toista
        if (friendships.size() > 0) {
            for (Friendship p : friendships) {
                if (p.getAccounts().get(0).getUsername().equalsIgnoreCase(username)) {
                    System.out.println("olet jo luonut profiilin");  
              break;
            }
            }
        }
        else{ 
          if (friendshipRepository.findByprofileName(profilename) == null &&(friendships.size() < 1)) {
                        System.out.println("ei oo profiilia");
                        Friendship friendship = new Friendship();;
                        //friendship.setAccepted(false);
                        friendshipRepository.save(friendship);
                        friendship.setProfileName(profilename);
                        friendship.getAccounts().add(account);
                        account.getFriendships().add(friendship);
                        friendshipRepository.save(friendship); 
                        accountRepository.save(account);
       
          }   

        }    return "redirect:/account/profiles";

    }
}
