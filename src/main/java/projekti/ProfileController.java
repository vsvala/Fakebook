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
public class ProfileController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProfileRepository profileRepository;
    
    @Autowired
    private PhotoObjectRepository photoRepository;


    @GetMapping("/profiles")
    public String list(Model model) {

        model.addAttribute("profiles", profileRepository.findAll());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = accountRepository.findByUsername(username);
        Account account = accountRepository.getOne(user.getId());
        model.addAttribute("account", account);

        return "profiles";
        // return "redirect:/accounts";
    }

    @GetMapping("/profile/{id}")
    public String getOne(Model model, @PathVariable Long id) {
//        Account account = accountRepository.getOne(id);
//        List<Friend> friends = friendRepository.findAll();
// 
//        model.addAttribute("account", account);
//        model.addAttribute("accounts", accountRepository.findAll());
//        model.addAttribute("friends", friends);
// 
        return "profile";
    }
    
     
//    @GetMapping("account/profiles/{id}/photos")
//    public String getProfilepicture(Model model, @PathVariable Long id) {
//    
//        model.addAttribute("profile", profileRepository.getOne(id));
//        model.addAttribute("profilePicture", profileRepository.getOne(id).getProfilePicture() );
// 
//        
//        
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
//     
//   //  Authentication auth = SecurityContextHolder.getContext().getAuthentication();   
////        String username = auth.getName();
////        Account user = accountRepository.findByUsername(username);
////        Account account = accountRepository.getOne(user.getId());
////          List<Profile>profiles= account.getProfiles();
////   
////          for (Profile p : profiles) {
////                if (p.getAccounts().equals(account)) {
////                    System.out.println("tämä on userin profile");  
////                   pPicture= p.getProfilePicture();
////                    System.out.println(" p.getProfilePicture();");
////                }
////          }
//        return "account";
//    }
    
    
    @PostMapping("/profiles/photos/{id}")
    public String chooseProfilePicture(@PathVariable Long id) {
         PhotoObject profilePicture = photoRepository.getOne(id);
         Long profileId=9L;
         
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = accountRepository.findByUsername(username);
        Account account = accountRepository.getOne(user.getId());
          List<Profile>profiles= account.getProfiles();
   
          for (Profile p : profiles) {
                if (p.getAccounts().equals(account)) {
                    System.out.println("tämä on userin account, aseta kuva");  
                    p.setProfilePicture(profilePicture);
                   id=p.getId();
              break;
            }
               }
        // id=  profileId;
        return "redirect:/accounts/photos/{id}";
      //  return "profile";
    }

    @PostMapping("/profiles")
    public String createProfile(@RequestParam String profileName) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = accountRepository.findByUsername(username);
        Account account = accountRepository.getOne(user.getId());

        List<Profile> profiles = account.getProfiles();     
        
    //jos profiili jo olemassa ei luoda toista
        if (profiles.size() > 0) {
            for (Profile p : profiles) {
                if (p.getAccounts().get(0).getUsername().equalsIgnoreCase(username)) {
                    System.out.println("olet jo luonut profiilin");  
              break;
            }
            }
        }
        else{ 
          if (profileRepository.findByprofileName(profileName) == null &&(profiles.size() < 1)) {
                        System.out.println("ei oo profiilia");
                        Profile profile = new Profile();
                        profileRepository.save(profile);
                        profile.setProfileName(profileName);
                        profile.getAccounts().add(account);
                        account.getProfiles().add(profile);
                        profileRepository.save(profile); 
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
//////        Profile f = profileRepository.getOne(profileId);
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

