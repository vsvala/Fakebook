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
    
     
//    @GetMapping("account/profiles/{id}/photos")
//    public String getProfilepicture(Model model, @PathVariable Long id) {
//    
//        model.addAttribute("friendship", friendshipRepository.getOne(id));
//        model.addAttribute("profilePicture", friendshipRepository.getOne(id).getProfilePicture() );
// 
//        
//        
//        
//        model.addAttribute("friendships", friendshipRepository.findAll());
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//        Account user = accountRepository.findByUsername(username);
//        Account account = accountRepository.getOne(user.getId());
//        model.addAttribute("account", account);
//
//        
//        
//     
//   //  Authentication auth = SecurityContextHolder.getContext().getAuthentication();   
////        String username = auth.getName();
////        Account user = accountRepository.findByUsername(username);
////        Account account = accountRepository.getOne(user.getId());
////          List<Profile>friendships= account.getProfiles();
////   
////          for (Friendship p : friendships) {
////                if (p.getAccounts().equals(account)) {
////                    System.out.println("tämä on userin friendship");  
////                   pPicture= p.getProfilePicture();
////                    System.out.println(" p.getProfilePicture();");
////                }
////          }
//        return "account";
//    }
    
    
//    @PostMapping("/profiles/photos/{id}")
//    public String chooseProfilePicture(@PathVariable Long id) {
//         PhotoObject profilePicture = photoRepository.getOne(id);
//         Long profileId=9L;
//         
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//        Account user = accountRepository.findByUsername(username);
//        Account account = accountRepository.getOne(user.getId());
//          List<Profile>friendships= account.getProfiles();
//   
//          for (Friendship p : friendships) {
//                if (p.getAccounts().equals(account)) {
//                    System.out.println("tämä on userin account, aseta kuva");  
//                    p.setProfilePicture(profilePicture);
//                   id=p.getId();
//              break;
//            }
//               }
//        // id=  profileId;
//        return "redirect:/accounts/photos/{id}";
//      //  return "friendship";
//    }

    //
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
    //
//////        @PostMapping("/profiles/{profileId}/accounts")
//////        public String assignAccount
//////        (
//////            @PathVariable
//////        Long profileId // @RequestParam Long accountId
//////        
//////            ) {
//////        Friendship f = friendshipRepository.getOne(profileId);
//////
//////            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//////            String username = auth.getName();
//////            Account a = accountRepository.findByUsername(username);
//////
////////        friendRepository.set);
//////// 
////////        aircraftRepository.save(ac);
//////// 
//////            return "redirect:/profiles";
//////        }

