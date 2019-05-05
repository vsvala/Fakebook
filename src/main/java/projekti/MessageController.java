package projekti;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MessageController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MessageRepository msgRepository;

    @Autowired
    private ProfilePictureRepository pictureRepository;
    
    @Autowired
    private PhotoObjectRepository photoRepository; //luodaan gif=PhotoObjectRepository  prepository olio

    //Viestit näytetään seinällä niiden saapumisjärjestyksessä siten, että seinällä näkyy aina korkeintaan 25 uusinta viestiä.
   
 @GetMapping("/messages")
    public String redirect(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = accountRepository.findByUsername(username);
        if(user.getPicture()!=null){
         System.out.println("iddddphoto"+ user.getPicture().getPictureId());
          return "redirect:/messages/photos/"+user.getPicture().getPictureId();
        }
       return "redirect:/messages/photos";
    }


    //TODO Sekä käyttäjä että käyttäjän kaverit voivat lähettää seinälle tekstimuotoisia viestejä. 
    //Jokaisesta viestistä näytetään viestin lähettäjän nimi, viestin lähetysaika, sekä viestin tekstimuotoinen sisältö. 
    @PostMapping("/messages")
    public String create(@RequestParam String message) {

        //tähän tarkastus onko user ko.profiilin kaveri eli saako lähettää seinälle..
        Message msg = new Message();
        msg.setMessage(message);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        msg.setUser(accountRepository.findByUsername(username));
        PhotoObject photo = new PhotoObject();
        // photo=photoRepository.getOne(id).getContent();
        msgRepository.save(msg);

        return "redirect:/messages";
    }


    @GetMapping(value = "/messages/photos/{id}",  produces = "image/*")//)//yksittäisen kuvan näyttö
    public String viewOne(Model model, @PathVariable Long id) {

 
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Pageable pageable = PageRequest.of(0, 25, Sort.by("messageDate").descending());
        model.addAttribute("messages", msgRepository.findAll(pageable));
        model.addAttribute("username", username);

//        Account user = accountRepository.findByUsername(username);
//
//        if (photoRepository.findByFriendship(user.getFriendships().get(0)) != null) {
//            PhotoObject photo = photoRepository.findByFriendship(user.getFriendships().get(0));
//            model.addAttribute("profilePicture", photo.getDescription());
//            
           model.addAttribute("current", id);
       // }
        return "wall";
    }
    
     @GetMapping(path = "/messages/photos/{id}/content", produces = "image/*")
    @ResponseBody
    public byte[] getContent(@PathVariable Long id) {
        return photoRepository.getOne(id).getContent();
    }
    


    @GetMapping(value = "/messages/photos")//)//yksittäisen kuvan näyttö
    public String getwall(Model model) {

         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Pageable pageable = PageRequest.of(0, 25, Sort.by("messageDate").descending());
        model.addAttribute("messages", msgRepository.findAll(pageable));
        model.addAttribute("username", username);
        return "wall";
    }

}












//    @GetMapping("/messages/photos/{id}/profilepicture")//yksittäisen kuvan näyttö
//    public String getProfilePictur(Model model, @PathVariable Long id) {
//        System.out.println("iiiiiiiiiidddddddddddddd" + id);
//// PhotoObject pp=photoRepository.getOne(id);
////         System.out.println("iiiiiiiiiiddddddddddddddphotoobj"+pp);
//        // pp.setProfile(profile);
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//        Account user = accountRepository.findByUsername(username);
//        // Account account = accountRepository.getOne(user.getId())
//        //PhotoObject photo=photoRepository.getOne(id);
//        PhotoObject photo = photoRepository.findByFriendship(user.getFriendships().get(0));
//
////////       // List<PhotoObject> photos = accountRepository.findByUsername(username).getPhotos();
////////      
////////       // model.addAttribute("profilePicture",pp.getDescription());
////////      
////////        Account user = accountRepository.findByUsername(username);
////////      ;
////////        List<Profile> friends = account.getProfiles();
////////        
//        ////PhotoObject p = account.getProfiles().get(0).getProfilePicture();
//        // model.addAttribute("profilePicture",photo.getContent());//photo.getProfilePictureId());//photoRepository.getOne(id).getDescription());
//        model.addAttribute("profilePicture", photo.getDescription());//photo.getProfilePictureId());//photoRepository.getOne(id).getDescription());
//
//        Pageable pageable = PageRequest.of(0, 25, Sort.by("messageDate").descending());
//        model.addAttribute("messages", msgRepository.findAll(pageable));
//        model.addAttribute("username", username);
//
//        return "wall";
//    }

       // return "redirect:/messages/photos/{id}";
    
    
//    @GetMapping("/messages")
//    public String list(Model model) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//        Pageable pageable = PageRequest.of(0, 25, Sort.by("messageDate").descending());
//        model.addAttribute("messages", msgRepository.findAll(pageable));
//        model.addAttribute("username", username);
//
//        Account user = accountRepository.findByUsername(username);
//
//        if (photoRepository.findByFriendship(user.getFriendships().get(0)) != null) {
//            PhotoObject photo = photoRepository.findByFriendship(user.getFriendships().get(0));
//            model.addAttribute("profilePicture", photo.getDescription());
//        }
//
//        return "wall";
//    }