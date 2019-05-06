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
    private CommentRepository commentRepository;

    @Autowired
    private ProfilePictureRepository pictureRepository;

    @Autowired
    private PhotoObjectRepository photoRepository; //luodaan gif=PhotoObjectRepository  prepository olio

    @GetMapping("/messages")
    public String redirect(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = accountRepository.findByUsername(username);
        if (user.getPicture() != null) {
            System.out.println("iddddphoto" + user.getPicture().getPictureId());
            return "redirect:/messages/photos/" + user.getPicture().getPictureId();
        }
        return "redirect:/messages/photos";
    }

    //TODO Sekä käyttäjä että käyttäjän kaverit voivat lähettää seinälle tekstimuotoisia viestejä. autorisointi
    @PostMapping("/messages")
    public String create(@RequestParam String message) {

        //tähän esim tarkastus onko user ko.profiilin kaveri eli saako lähettää seinälle..
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

    @GetMapping(value = "/messages/photos/{id}", produces = "image/*")//)//yksittäisen kuvan näyttö
    public String viewOne(Model model, @PathVariable Long id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = accountRepository.findByUsername(username);

        Pageable pageable = PageRequest.of(0, 25, Sort.by("messageDate").descending());
        model.addAttribute("messages", msgRepository.findAll(pageable));
        model.addAttribute("username", username);

        ProfilePicture picture = user.getPicture();
        model.addAttribute("current", picture.getPictureId());
        return "wall";
    }

    @GetMapping(path = "/messages/photos/{id}/content", produces = "image/*")
    @ResponseBody
    public byte[] getContent(@PathVariable Long id) {
        return photoRepository.getOne(id).getContent();
    }

    @GetMapping(value = "/messages/photos")
    public String getwall(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Pageable pageable = PageRequest.of(0, 25, Sort.by("messageDate").descending());
        model.addAttribute("messages", msgRepository.findAll(pageable));
        model.addAttribute("username", username);
        return "wall";
    }

    @GetMapping(value = "/messages/{id}/comments")
    public String getwallWithComments(Model model, @PathVariable Long id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = accountRepository.findByUsername(username);
        Pageable pageable = PageRequest.of(0, 25, Sort.by("messageDate").descending());

        Message message = msgRepository.getOne(id);
        List<Comment> comments = message.getComments();

        ProfilePicture picture = user.getPicture();

        model.addAttribute("comments", comments);
        model.addAttribute("messages", msgRepository.findAll(pageable));
        model.addAttribute("username", username);
        model.addAttribute("current", picture.getPictureId());
        return "wall";
    }

}
