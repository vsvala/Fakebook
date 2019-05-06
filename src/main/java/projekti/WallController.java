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
public class WallController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MessageRepository msgRepository;

    @Autowired
    private ProfilePictureRepository pictureRepository;
    
    @Autowired
    private PhotoObjectRepository photoRepository;

   //NOT IN USE yet
// @GetMapping("/wall")
//    public String redirect(Model model) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//        Account user = accountRepository.findByUsername(username);
//        if(user.getPicture()!=null){
//         System.out.println("iddddphoto"+ user.getPicture().getPictureId());
//          return "redirect:/messages/photos/"+user.getPicture().getPictureId();
//        }
//       return "redirect:/wall/photos";
//    }
//
//
//    //TODO Sekä käyttäjä että käyttäjän kaverit voivat lähettää seinälle tekstimuotoisia viestejä. 
//    //Jokaisesta viestistä näytetään viestin lähettäjän nimi, viestin lähetysaika, sekä viestin tekstimuotoinen sisältö. 
//    @PostMapping("/wall")
//    public String create(@RequestParam String message) {
//
//        //tähän tarkastus onko user ko.profiilin kaveri eli saako lähettää seinälle..
//        Message msg = new Message();
//        msg.setMessage(message);
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//        msg.setUser(accountRepository.findByUsername(username));
//        PhotoObject photo = new PhotoObject();
//        // photo=photoRepository.getOne(id).getContent();
//        msgRepository.save(msg);
//
//        return "redirect:/wall";
//    }
//
//
//    @GetMapping(value = "/wall/photos/{id}",  produces = "image/*")//)//yksittäisen kuvan näyttö
//    public String viewOne(Model model, @PathVariable Long id) {
//
// 
//         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//        Pageable pageable = PageRequest.of(0, 25, Sort.by("messageDate").descending());
//        model.addAttribute("messages", msgRepository.findAll(pageable));
//        model.addAttribute("username", username);
//
//           model.addAttribute("current", id);
//   
//        return "wall";
//    }
//    
//     @GetMapping(path = "/wall/photos/{id}/content", produces = "image/*")
//    @ResponseBody
//    public byte[] getContent(@PathVariable Long id) {
//        return photoRepository.getOne(id).getContent();
//    }
//    
//
//
//    @GetMapping(value = "/wall/photos")//)//yksittäisen kuvan näyttö
//    public String getwall(Model model) {
//
//         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//        Pageable pageable = PageRequest.of(0, 25, Sort.by("messageDate").descending());
//        model.addAttribute("messages", msgRepository.findAll(pageable));
//        model.addAttribute("username", username);
//        return "wall";
//    }

}