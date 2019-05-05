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
import org.springframework.web.bind.annotation.DeleteMapping;

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
        Account user = accountRepository.getOne(id);

        List<Friendship> friendships = friendshipRepository.findAll();

        model.addAttribute("user", user);
        model.addAttribute("accounts", accountRepository.findAll());
        model.addAttribute("friendships", friendships);

        return "account";
    }

    //  get all user's  friends 
    @GetMapping("/account/friendships/{id}")
    public String getFriendsSearch(Model model, @PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = accountRepository.findByUsername(username);
        Account account = accountRepository.findByUsername(username);
      
        List<Account> frequests =new ArrayList();

     if(user.getFriendships().size()>=0){
        Friendship frienship = user.getFriendships().get(0);//eka on aina userin koska luodaan tehdessä

        if (frienship.getAccounts() != null) {
            frequests = frienship.getAccounts();//kaikki tilit jotkaovat kyseisen profiilin ysäväksihakeneet
            if (frequests.contains(user)) {
                frequests.remove(user);
            }
        }    
            List<Account> accounts = accountRepository.findAll();
            if (accounts.contains(user)) {
                accounts.remove(user);
            }

            List<Friend> userfriends = account.getFriends();//Userinkvetit

            List<Account> friendusers = new ArrayList();
            if (friendRepository.findByusername(username) != null) {
                friendusers = friendRepository.findByusername(username).getAccounts();
            }

            Account searched = accountRepository.getOne(id);

            model.addAttribute("searched", searched);
            model.addAttribute("friendusers", friendusers);
            model.addAttribute("userfriends", userfriends);
 
            model.addAttribute("user", user);
            model.addAttribute("accounts", accounts);
    
            model.addAttribute("frequests", frequests);
            model.addAttribute("friendships", friendshipRepository.findAll());
            model.addAttribute("allfriends", friendRepository.findAll());
        }
        List<Account> accounts = accountRepository.findAll();
        if (accounts.contains(user)) {
            accounts.remove(user);
        }
        
        model.addAttribute("user", user);
        model.addAttribute("accounts", accounts);
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
          List<Account> frequests =new ArrayList();
        
       // if(user.getFriendships().size()>0){
        Friendship frienship = user.getFriendships().get(0);//eka on aina userin koska luodaan tehdessa
        if (frienship.getAccounts() != null) {
        frequests = frienship.getAccounts();//kaikki tilit jotkaovat kyseisen profiilin ysäväksihakeneet
            if (frequests.contains(user)) {
                frequests.remove(user);
            }
            //  }
            List<Account> accounts = accountRepository.findAll();
            if (accounts.contains(user)) {
                accounts.remove(user);
            }
      
            List<Friend> userfriends = account.getFriends();//Userinkvetit

            List<Account> friendusers = new ArrayList();
            if (friendRepository.findByusername(username) != null) {
                friendusers = friendRepository.findByusername(username).getAccounts();
            }

            Account searched = new Account();

            model.addAttribute("searched", searched); //search
            model.addAttribute("friendusers", friendusers); // shows accepted friends fo request sender
            model.addAttribute("userfriends", userfriends); //shows accepted friends fo receiver
        
            model.addAttribute("user", user); 
            model.addAttribute("accounts", accounts);
            
            model.addAttribute("frequests", frequests); //friend acceptance requests friends
            model.addAttribute("friendships", friendshipRepository.findAll());//all friendships
            model.addAttribute("allfriends", friendRepository.findAll());
   
        }
        List<Account> accounts = accountRepository.findAll();
        if (accounts.contains(user)) {
            accounts.remove(user);
        }

        model.addAttribute("user", user);
        model.addAttribute("accounts", accounts);
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
        //creating account
        Account account = new Account();
        account.setName(name);
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(password));
        account.setProfilename(profilename);
        accountRepository.save(account);

        //creating friendship
        Friendship friendship = new Friendship();;
        friendship.setProfileName(profilename);
        friendship.getAccounts().add(account);//set as your friend 0 in the list
        account.getFriendships().add(friendship);
        friendshipRepository.save(friendship);
        accountRepository.save(account);

        return "redirect:/account/friendships";
    }

   //  Add friend request to account
    @Transactional
    @PostMapping("/accounts/{id}/add")
    public String addFriendShipRequest(@PathVariable Long id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = accountRepository.findByUsername(username);
        System.out.println("adding friendrequestuuuuuuuuuuu" + user.getProfilename() + user.getId());
        //String profilename=account.getProfilename();

        //liittään account ja friendship
        Account friendAccount = accountRepository.getOne(id);
        System.out.println("täääääläineen ooooooooooooooooooooonppppppppppppp" + friendAccount.getProfilename() + friendAccount.getId());
        
        Friendship friendship = friendshipRepository.findByprofileName(friendAccount.getProfilename());
// 

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

        Friend f = friendRepository.findByusername(friendAccount.getUsername());

        if (!userAccount.getFriends().contains(f)) {//jos ei oo  userin lisassa kaverina lisätään sinne
            System.out.println("lisätään uusi kaveri nimikaverilistalle");
            userAccount.getFriends().add(f);//lisätään userin listalle kaveri     
            accountRepository.save(userAccount);
            f.getAccounts().add(userAccount);
            friendRepository.save(f);

       

        //remooves accepted frienship
            System.out.println("useraccountiddddddd" + userAccount.getId());
            System.out.println("friendccountiddddddd" + friendAccount.getId());
            Friendship fs = friendshipRepository.findByprofileName(friendAccount.getProfilename()); //kaveri haetaaan 2
            Friendship fs2 = friendshipRepository.findByprofileName(userAccount.getProfilename()); //kaveri haetaaan 2

            System.out.println("poistoonfs" + fs.getId()); 
            System.out.println("fstoinenpoistoon" + fs2.getId()); 
           //List<Friendship> friendshipList = userAccount.getFriendships();
            //userAccount.getFriendships().remove(fs);
           // accountRepository.save(userAccount);
            
           // friendAccount.getFriendships().remove(fs);
            friendAccount.getFriendships().remove(fs2);
            accountRepository.save(friendAccount);
            
            //fs.getAccounts().remove(userAccount);   
           // fs.getAccounts().remove(friendAccount);
            //friendshipRepository.save(fs);
            System.out.println("nollas"+userAccount.getFriendships().get(0).getProfileName());
          
            Friendship friendship = userAccount.getFriendships().get(0);//eka on aina userin koska luodaan tehdessä
             System.out.println("nollas"+ friendship.getProfileName());
//            friendship.getAccounts().remove(userAccount);   
//            friendship.getAccounts().remove(friendAccount);
//             friendshipRepository.save(friendship);
             //friendshipRepository.delete(friendship);
            

        } else {
            System.out.println("kaveri on jo"); 
            
        }

        return "redirect:/account/friendships";
    }

    @PostMapping("/accounts/search")
    public String searchUser(@RequestParam String searched) {

        if (accountRepository.findByUsername(searched) == null) {
            return "redirect:/account/friendships";
        }
        Account user = accountRepository.findByUsername(searched);
        return "redirect:/account/friendships/" + user.getId();

    }

    @DeleteMapping(path = "/account/{id}/reject")
    public String deleteAccount(@PathVariable Long id) {
        
        Account account = accountRepository.getOne(id);

        accountRepository.delete(account);

        return "redirect:/logout";

    }
       @DeleteMapping(path = "/account/{id}/delete")
    public String rejecttFriend(@PathVariable Long id) {
       
//       Friendship fs = friendshipRepository.getOne(id);
//       friendAccount.getFriendships().remove(fs2);
//       accountRepository.save(friendAccount);

        return "redirect:/logout";

    }
    
    
}
                  //remooves accepted frienship
//            System.out.println("useraccountiddddddd" + userAccount.getId());
//            System.out.println("friendccountiddddddd" + friendAccount.getId());
//            Friendship fs = friendshipRepository.findByprofileName(friendAccount.getProfilename()); //kaveri haetaaan 2
//            System.out.println("fs" + fs.getId()); 
//           
//           //List<Friendship> friendshipList = userAccount.getFriendships();
//            userAccount.getFriendships().remove(fs);
//            accountRepository.save(userAccount);
//            
//            friendAccount.getFriendships().remove(fs);
//            accountRepository.save(friendAccount);
//            
//            fs.getAccounts().remove(userAccount);   
//            fs.getAccounts().remove(friendAccount);
//            friendshipRepository.save(fs);
//            System.out.println("nollas"+userAccount.getFriendships().get(0).getProfileName());
//             Friendship friendship = userAccount.getFriendships().get(0);//eka on aina userin koska luodaan tehdessä
//            friendship.getAccounts().remove(userAccount);   
//            friendship.getAccounts().remove(friendAccount);
//             friendshipRepository.save(friendship);
//             //friendshipRepository.delete(friendship);
  
           