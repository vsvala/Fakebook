package projekti;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AccountController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ProfileRepository profileRepository;
    
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
        List<Profile> profiles = profileRepository.findAll();

        model.addAttribute("account", account);
        model.addAttribute("accounts", accountRepository.findAll());
        model.addAttribute("profiles", profiles);

        return "account";
    }

    //  get all user's  friends 
    @GetMapping("/account/profiles")
    public String getfriends(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = accountRepository.findByUsername(username);
        Account account = accountRepository.getOne(user.getId());
        List<Profile> friends = account.getProfiles();

        model.addAttribute("account", account);
        model.addAttribute("accounts", accountRepository.findAll());
        model.addAttribute("friends", friends);
        model.addAttribute("profiles", profileRepository.findAll());

        
        if(account.getProfiles().get(0).getProfilePicture() != null) {
        PhotoObject p = account.getProfiles().get(0).getProfilePicture();
        model.addAttribute("profilePicture", p.getDescription());
        }
        
        return "account";
    }

  
    
    //creates account
    @PostMapping("/accounts")
    public String createAccount(@RequestParam String username, @RequestParam String password, @RequestParam String name, @RequestParam String identifier) {
        if (accountRepository.findByUsername(username) != null) {
            return "redirect:/accounts";
        }
        Account a = new Account();
        //Account a = new Account(name, username, passwordEncoder.encode(password), identifier);
        a.setName(name);
        a.setUsername(username);
        a.setPassword(passwordEncoder.encode(password));
        a.setIdentifier(identifier);
        accountRepository.save(a);
        return "redirect:/profiles";
    }

    
     //  Add friends to account
    @Transactional
    @PostMapping("/account/{accountId}/profiles/{profileId}")
    public String addFriend(@PathVariable Long accountId, @PathVariable Long profileId) {

        //samaa kaveria ei lisätä useasti
        Account account = accountRepository.getOne(accountId);
        Profile profile = profileRepository.getOne(profileId);
//        List<Profile> profiles = account.getProfiles();  
//        
//    //jos on jo kaveri, ei lisätä
//        if (profiles.size() > 0) {
//            for (Profile p : profiles) {
//                 if (p.getProfileName().equalsIgnoreCase(profile.getProfileName())) {
//                    System.out.println("on jo kaverisi");  
//              break;
//            } 
//            }

        //jos ei ole kaveri, lisätään kaveriksi   
        if (!account.getProfiles().contains(profile)) {
            System.out.println("lisätään kaveriksi");
            profileRepository
                    .getOne(profileId)
                    .getAccounts().add(accountRepository.getOne(profileId));
            accountRepository
                    .getOne(accountId).getProfiles().add(profileRepository.getOne(profileId));
        } else {
            System.out.println("on jo kaverisi");
        }
        //return "redirect:/account/"+ accountId;
        return "redirect:/account/profiles";
    }

          @GetMapping("/account/profile/{id}/profilepicture")//yksittäisen kuvan näyttö
         public String  getProfilePicture(Model model, @PathVariable Long id) {
         System.out.println("iiiiiiiiiidddddddddddddd"+id);
         //PhotoObject pp=photoRepository.getOne(id);
    // pp.setProfile(profile);
          model.addAttribute("photos", photoRepository.findAll());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
       // List<PhotoObject> photos = accountRepository.findByUsername(username).getPhotos();
      
       // model.addAttribute("profilePicture",pp.getDescription());
      
        Account user = accountRepository.findByUsername(username);
        Account account = accountRepository.getOne(user.getId());
        List<Profile> friends = account.getProfiles();
        
        PhotoObject p = account.getProfiles().get(0).getProfilePicture();
        model.addAttribute("profilePicture",p.getDescription());
       
        
        model.addAttribute("account", account);
        model.addAttribute("accounts", accountRepository.findAll());
        model.addAttribute("friends", friends);
        model.addAttribute("profiles", profileRepository.findAll());
        
        
     
        return "account";
    }



}

    
    
    
//       @GetMapping("account/profiles/{Profiled}/photos")
//    public String getProfilepicture(Model model, @PathVariable Long id) {
//    
//        //model.addAttribute("profile", profileRepository.getOne(id));
//        model.addAttribute("profilePicture", profileRepository.getOne(id).getProfilePicture() );
//  
//        model.addAttribute("profiles", profileRepository.findAll());
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
//          for (Profile p : profiles) {
//                if (p.getAccounts().equals(account)) {
//                    System.out.println("tämä on userin profile");  
//                   pPicture= p.getProfilePicture();
//                    System.out.println(" p.getProfilePicture();");
//                }
//          }
//        return "account";
//    }
//      
//}
