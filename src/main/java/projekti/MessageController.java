package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MessageController {
     
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MessageRepository msgRepository;

    @Autowired
    private PhotoObjectRepository photoRepository; //luodaan gif=PhotoObjectRepository  prepository olio

    
    //Viestit näytetään seinällä niiden saapumisjärjestyksessä siten, että seinällä näkyy aina korkeintaan 25 uusinta viestiä.
    @GetMapping("/messages")
    public String list(Model model) {
        Pageable pageable = PageRequest.of(0, 25, Sort.by("messageDate").descending());
        model.addAttribute("messages", msgRepository.findAll(pageable));
        return "messages";
    }

     //TODO Sekä käyttäjä että käyttäjän kaverit voivat lähettää seinälle tekstimuotoisia viestejä. 
     //Jokaisesta viestistä näytetään viestin lähettäjän nimi, viestin lähetysaika, sekä viestin tekstimuotoinen sisältö. 
    @PostMapping("/messages")
    public String create(@RequestParam String message) {
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
}
