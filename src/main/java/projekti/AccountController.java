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
            List<Friend> userfriends = account.getFriends();//Userinkvetit

            List<Account> friendusers = new ArrayList();
            if (friendRepository.findByusername(username) != null) {
                friendusers = friendRepository.findByusername(username).getAccounts();
            }

            Account searched = accountRepository.getOne(id);

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
            List<Friend> userfriends = account.getFriends();//Userinkvetit

            List<Account> friendusers = new ArrayList();
            if (friendRepository.findByusername(username) != null) {
                friendusers = friendRepository.findByusername(username).getAccounts();
            }

            Account searched = new Account();

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
        return "redirect:/accounts/friendships/" + user.getId();

    }

    @DeleteMapping(path = "/account/{id}/delete")
    public String deleteAccount(@PathVariable Long id) {
        Account account = accountRepository.getOne(id);

        accountRepository.delete(account);

        return "redirect:/logout";

    }
}
