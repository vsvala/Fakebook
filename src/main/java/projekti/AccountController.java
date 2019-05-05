package projekti;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import static org.hibernate.internal.util.collections.CollectionHelper.arrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.time.LocalDateTime;

@Controller
public class AccountController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    FriendshipRepository friendshipRepository;

    @Autowired
    FriendRepository friendRepository;

    @Autowired
    PhotoObjectRepository photoRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/accounts")
    public String list(Model model) {
        model.addAttribute("accounts", accountRepository.findAll());
        return "accounts";
    }

    //???????klickin one  account you get to see it account..friends
    @GetMapping("/account/{id}")
    public String getOne(Model model, @PathVariable Long id) {
        Account account = accountRepository.getOne(id);
        List<Friendship> friendships = friendshipRepository.findAll();

        model.addAttribute("account", account);
        model.addAttribute("accounts", accountRepository.findAll());
        model.addAttribute("friendships", friendships);

        return "account";
    }

    //  get all user's  friends 
    @GetMapping("/accounts/friendships/{id}")
    public String getFriendsSearch(Model model, @PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = accountRepository.findByUsername(username);
        // Account account = accountRepository.getOne(user.getId());
        Account account = accountRepository.findByUsername(username);
 
        List<Friendship> friends = user.getFriendships();
        List<Friendship> af = new ArrayList();

        Friendship frienship = user.getFriendships().get(0);//eka on aina userin koska luodaan tehdessä

        if (frienship.getAccounts() != null) {
            List<Account> ap = frienship.getAccounts();//kaikki tilit jotkaovat kyseisen profiilin ysäväksihakeneet
            if (ap.contains(user)) {
                ap.remove(user);
            }
//     
            List<Account> accounts = accountRepository.findAll();
            if (accounts.contains(user)) {
                accounts.remove(user);
            }

            List<Friend> afriends = user.getFriends();
             List<Friend> userfriends =account.getFriends();//Userinkvetit
            
            List<Account>friendusers =new ArrayList();
            if(friendRepository.findByusername(username)!=null){
            friendusers = friendRepository.findByusername(username).getAccounts();  
              }
            
            Account searched= accountRepository.getOne(id);
            
               model.addAttribute("searched", searched);
              model.addAttribute("friendusers", friendusers);
              model.addAttribute("userfriends", userfriends);
            model.addAttribute("af", af);
            model.addAttribute("account", account);
            model.addAttribute("accounts", accounts);
            model.addAttribute("friends", friends);
            // model.addAttribute("userfriends", userfriends);
            model.addAttribute("ap", ap);
       
            model.addAttribute("friendships", friendshipRepository.findAll());
            model.addAttribute("allfriends", friendRepository.findAll());
            model.addAttribute("afriends", afriends);
        }
        List<Account> accounts = accountRepository.findAll();
        if (accounts.contains(user)) {
            accounts.remove(user);
        }
   
        model.addAttribute("af", af);
        model.addAttribute("account", account);
        model.addAttribute("accounts", accounts);
        model.addAttribute("friends", friends);
        // model.addAttribute("ap", ap);
        model.addAttribute("friendships", friendshipRepository.findAll());
        model.addAttribute("username", username);

        return "account";
    }
    
    
    
    
    
    //  get all user's  friends 
    @GetMapping("/account/friendships")
    public String getfriends(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = accountRepository.findByUsername(username);
        Account account = accountRepository.getOne(user.getId());
    
        List<Friendship> friends = user.getFriendships();
        List<Friendship> af = new ArrayList();

        Friendship frienship = user.getFriendships().get(0);//eka on aina userin koska luodaan tehdessä

        if (frienship.getAccounts() != null) {
            List<Account> ap = frienship.getAccounts();//kaikki tilit jotkaovat kyseisen profiilin ysäväksihakeneet
            if (ap.contains(user)) {
                ap.remove(user);
            }
//     
            List<Account> accounts = accountRepository.findAll();
            if (accounts.contains(user)) {
                accounts.remove(user);
            }

            List<Friend> afriends = user.getFriends();
             List<Friend> userfriends =account.getFriends();//Userinkvetit
            
            List<Account>friendusers =new ArrayList();
            if(friendRepository.findByusername(username)!=null){
            friendusers = friendRepository.findByusername(username).getAccounts();  
              }
            
            Account searched=new Account();
            
               model.addAttribute("searched", searched);
              model.addAttribute("friendusers", friendusers);
              model.addAttribute("userfriends", userfriends);
            model.addAttribute("af", af);
            model.addAttribute("account", account);
            model.addAttribute("accounts", accounts);
            model.addAttribute("friends", friends);
            model.addAttribute("ap", ap);
      
            model.addAttribute("friendships", friendshipRepository.findAll());
            model.addAttribute("allfriends", friendRepository.findAll());
            model.addAttribute("afriends", afriends);
        }
        List<Account> accounts = accountRepository.findAll();
        if (accounts.contains(user)) {
            accounts.remove(user);
        }
 
        model.addAttribute("af", af);
        model.addAttribute("account", account);
        model.addAttribute("accounts", accounts);
        model.addAttribute("friends", friends);
        // model.addAttribute("ap", ap);
        model.addAttribute("friendships", friendshipRepository.findAll());
        model.addAttribute("username", username);

        return "account";
    }
    
     

    //creates account and frienship
    @PostMapping("/accounts")
    public String createAccount(@RequestParam String username, @RequestParam String password, @RequestParam String name, @RequestParam String profilename) {
        if (accountRepository.findByUsername(username) != null) {
            return "redirect:/accounts";
        }
        Account account = new Account();
        account.setName(name);
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(password));
        account.setProfilename(profilename);
        accountRepository.save(account);

        //creating friendshipstatus
        Friendship friendship = new Friendship();;
        // friendship.setAccepted(false);
        //account.setAccepted(false);
        // friendshipRepository.save(friendship);
        friendship.setProfileName(profilename);
        // friendship.setMessageDate(LocalDateTime.now());
        friendship.getAccounts().add(account);
        account.getFriendships().add(friendship);
        friendshipRepository.save(friendship);
        accountRepository.save(account);

        return "redirect:/account/friendships";
    }

    //  Add friend request to account
    @Transactional
    @PostMapping("/accounts/{id}/add")
    public String addFriendShipRequest(@PathVariable Long id) {

        //loggeduseraccount
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = accountRepository.findByUsername(username);
        System.out.println("uuuuuuuuuuu" + user.getProfilename() + user.getId());
        //Account account = accountRepository.getOne(user.getId());
        //String profilename=account.getProfilename();

        //etsitään liityykö useriin jo ko frindship     //samaa kaveria ei lisätä useasti
        Account friendAccount = accountRepository.getOne(id);
//       String pn=account.getProfilename();
        System.out.println("ppppppppppppp" + friendAccount.getProfilename() + friendAccount.getId());
        Friendship friendship = friendshipRepository.findByprofileName(friendAccount.getProfilename());

        List<Friendship> friendships = user.getFriendships();
        if (!friendships.contains(friendship)) {       //jos ei ole kaveri, lisätään kaveriksi   
            System.out.println("ei oo kaveriiiiiiiiiiiii");

            System.out.println("lisätään kaveriksi");
            friendship.getAccounts().add(user);

            friendships.add(friendship);
            friendshipRepository.save(friendship);
        } else {
            System.out.println("on jo kaverisii");
        }

        return "redirect:/account/friendships";
    }

    // accepting friend
    @PostMapping("/accounts/{id}/accept")
    public String acceptFriend(@PathVariable Long id) {
        System.out.println("iiddddddddd" + id);
        Account friendAccount = accountRepository.getOne(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account userAccount = accountRepository.findByUsername(username);

        if (friendRepository.findByusername(friendAccount.getUsername()) == null) {//jos ei oo kaveria repossa talletetaan    
            System.out.println("lisätään uusi kaveri nimikaverilistalle");
            Friend f = new Friend();
            f.setFriendname(userAccount.getUsername());
            f.setUsername(friendAccount.getUsername());
            friendRepository.save(f);
        }

       // if (friendRepository.findByusername(friendAccount.getUsername()) != null) {//jos on   

            Friend f = friendRepository.findByusername(friendAccount.getUsername()); //kaveri haetaaan

            if (!userAccount.getFriends().contains(f)) {//jos ei oo  userin lisassa kaverina lisätään sinne
                System.out.println("lisätään uusi kaveri nimikaverilistalle");
                userAccount.getFriends().add(f);//lisätään userin listalle kaveri     
                accountRepository.save(userAccount);
                f.getAccounts().add(userAccount);
                friendRepository.save(f);

            } else {
                System.out.println("kaveri on jo");
            }

      //  }
//            userAccount.getFriendships().remove(f);//poistetaan kaveripyyntö
//            accountRepository.save(userAccount);
//             
//       Friendship accepted=friendshipRepository.findByprofileName(friendAccount.getUsername());
//       friendAccount.getFriendships().remove(accepted);
//       accountRepository.save(friendAccount);

        return "redirect:/account/friendships";
    }

    
    
    
    @PostMapping("/accounts/search")
    public String searchUser(@RequestParam String searched) {
        
    Account user = accountRepository.findByUsername(searched);
            return "redirect:/accounts/friendships/"+user.getId();

    }

}








//        Account a = accountRepository.getOne(id);
//        String profilename=a.getProfilename();
//        Friendship friend=friendshipRepository.findByprofileName(profilename);
// account.setAccepted(true);
//delete from requests and add to friend list
//create accepted friend!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
////////    //  get all user's  friends 
////////    @GetMapping("/account/friendships")
////////    public String getfriends(Model model) {
////////        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
////////        String username = auth.getName();
////////        Account user = accountRepository.findByUsername(username);
////////        Account account = accountRepository.getOne(user.getId());
////////        
////////        
////////        
////////        List<Friendship> friends = account.getFriendships();
////////        List<Friendship> af = new ArrayList();
////////
////////        Friendship frienship = user.getFriendships().get(0);
////////        if (frienship.getAccounts() != null) {
////////            List<Account> ap = frienship.getAccounts();//kaikki tilit jotkaovat kyseisen profiilin ysäväksihakeneet
////////
////////            for (Friendship friend : friends) {
////////                if (friend.isAccepted()) {
////////                    af.add(friend);
////////                }
////////            }
////////            model.addAttribute("af", af);
////////            model.addAttribute("account", account);
////////            model.addAttribute("accounts", accountRepository.findAll());
////////            model.addAttribute("friends", friends);
////////            model.addAttribute("ap", ap);
////////            model.addAttribute("friendships", friendshipRepository.findAll());
////////
////////        }
////////        model.addAttribute("af", af);
////////        model.addAttribute("account", account);
////////        model.addAttribute("accounts", accountRepository.findAll());
////////        model.addAttribute("friends", friends);
////////        // model.addAttribute("ap", ap);
////////        model.addAttribute("friendships", friendshipRepository.findAll());
////////        model.addAttribute("username", username);
////////
//////////        if(account.getProfiles().get(0).getProfilePicture() != null) {
//////////        PhotoObject p = account.getProfiles().get(0).getProfilePicture();
//////////        model.addAttribute("profilePicture", p.getDescription());
//////////        }
////////        return "account";
////////    }
//        @GetMapping("/account/{id}/accept")
//    public String approvedFirends(Model model,  @PathVariable Long id) {
//        
//        List<Profile> af= account.getProfiles();
//
//        
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//        Account user = accountRepository.findByUsername(username);
//        Account account = accountRepository.getOne(user.getId());
//        List<Profile> friends = account.getProfiles();
//        
//        Friendship friendship=user.getProfiles().get(0);
//        List<Account> ap = friendship.getAccounts();//kaikki tilit jotkaovat kyseisen profiilin ysäväksihakeneet
//
//        model.addAttribute("account", account);
//        model.addAttribute("accounts", accountRepository.findAll());
//        model.addAttribute("friends", friends);
//        model.addAttribute("ap", ap);
//        model.addAttribute("profiles", friendshipRepository.findAll());
//        model.addAttribute("acceptedfriends", af);
//
//             return "account";
//    }
////////      @PostMapping("/account/{id}/accept")
////////    public String approve(Model model,  @PathVariable Long id) {
////////        
////////        Account account=accountRepository.getOne(id);
////////        //account.setAccepted(true);
////////        accountRepository.save(account);
//////////        Friendship prof=friendshipRepository.getOne(id);
//////////        prof.setAccepted(true);
//////////        friendshipRepository.save(prof);
////////     
////////        
//////////        if(account.getProfiles().get(0).getProfilePicture() != null) {
//////////        PhotoObject p = account.getProfiles().get(0).getProfilePicture();
//////////        model.addAttribute("profilePicture", p.getDescription());
//////////        }
////////        
////////       return "redirect:/account/friendships";
////////    }
////////
////////    //  Add friends to account
////////    @Transactional
////////    @PostMapping("/account/{accountId}/friendships/{friendshipId}")
////////    public String addFriend(@PathVariable Long accountId, @PathVariable Long friendshipId) {
////////
////////        //samaa kaveria ei lisätä useasti
////////        Account account = accountRepository.getOne(accountId);
////////        Friendship friendship = friendshipRepository.getOne(friendshipId);
//////////        List<Profile> profiles = account.getProfiles();  
//////////        
//////////    //jos on jo kaveri, ei lisätä
//////////        if (profiles.size() > 0) {
//////////            for (Friendship p : profiles) {
//////////                 if (p.getProfileName().equalsIgnoreCase(friendship.getProfileName())) {
//////////                    System.out.println("on jo kaverisi");  
//////////              break;
//////////            } 
//////////            }
////////
////////        //jos ei ole kaveri, lisätään kaveriksi   
////////        if (!account.getFriendships().contains(friendship)) {
////////            System.out.println("lisätään kaveriksi");
////////            friendshipRepository
////////                    .getOne(friendshipId)
////////                    .getAccounts().add(accountRepository.getOne(friendshipId));
////////            accountRepository
////////                    .getOne(accountId).getFriendships().add(friendshipRepository.getOne(friendshipId));
////////        } else {
////////            System.out.println("on jo kaverisi");
////////        }
////////        //return "redirect:/account/"+ accountId;
////////        return "redirect:/account/friendships";
////////    }
////////    
////////    
//          @GetMapping("/account/profile/{id}/profilepicture")//yksittäisen kuvan näyttö
//         public String  getProfilePicture(Model model, @PathVariable Long id) {
//         System.out.println("iiiiiiiiiidddddddddddddd"+id);
//         //PhotoObject pp=photoRepository.getOne(id);
//    // pp.setProfile(friendship);
//          model.addAttribute("photos", photoRepository.findAll());
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//       // List<PhotoObject> photos = accountRepository.findByUsername(username).getPhotos();
//      
//       // model.addAttribute("profilePicture",pp.getDescription());
//      
//        Account user = accountRepository.findByUsername(username);
//        Account account = accountRepository.getOne(user.getId());
//        List<Profile> friends = account.getProfiles();
//        
//        PhotoObject p = account.getProfiles().get(0).getProfilePicture();
//        model.addAttribute("profilePicture",p.getDescription());
//       
//        
//        model.addAttribute("account", account);
//        model.addAttribute("accounts", accountRepository.findAll());
//        model.addAttribute("friends", friends);
//        model.addAttribute("profiles", friendshipRepository.findAll());
//        
//        
//     
//        return "account";
//    }
//}
//       @GetMapping("account/profiles/{Profiled}/photos")
//    public String getProfilepicture(Model model, @PathVariable Long id) {
//    
//        //model.addAttribute("friendship", friendshipRepository.getOne(id));
//        model.addAttribute("profilePicture", friendshipRepository.getOne(id).getProfilePicture() );
//  
//        model.addAttribute("profiles", friendshipRepository.findAll());
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//        Account user = accountRepository.findByUsername(username);
//        Account account = accountRepository.getOne(user.getId());
//        model.addAttribute("account", account);
//        
//
//        
//  Authentication auth = SecurityContextHolder.getContext().getAuthentication();   
//        String username = auth.getName();
//        Account user = accountRepository.findByUsername(username);
//        Account account = accountRepository.getOne(user.getId());
//          List<Profile>profiles= account.getProfiles();
//   
//          for (Friendship p : profiles) {
//                if (p.getAccounts().equals(account)) {
//                    System.out.println("tämä on userin friendship");  
//                   pPicture= p.getProfilePicture();
//                    System.out.println(" p.getProfilePicture();");
//                }
//          }
//        return "account";
//    }
//      
//}
