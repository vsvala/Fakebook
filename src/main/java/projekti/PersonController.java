package projekti;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PersonController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/persons")
    public String list(Model model) {
        model.addAttribute("persons", personRepository.findAll());
        return "persons";
    }
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            String username = auth.getName();
//            msg.setUser(accountRepository.findByUsername(username));
//            messageRepository.save(msg);
//  

    @PostMapping("/persons")
    public String addPerson(@RequestParam String profileName, @RequestParam String identifier) {

        Person person = new Person();
        person.setProfileName(profileName);
        person.setIdentifier(identifier);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        person.setUser(accountRepository.findByUsername(username));
        personRepository.save(person);

        return "redirect:/persons";
    }

}
