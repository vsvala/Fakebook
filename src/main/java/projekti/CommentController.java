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
public class CommentController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MessageRepository msgRepository;

    @Autowired
    private CommentRepository commentRepository;
    
//    @Autowired
//    private PhotoObjectRepository photoRepository; //luodaan gif=PhotoObjectRepository  prepository olio
//
//    //Viestit näytetään seinällä niiden saapumisjärjestyksessä siten, että seinällä näkyy aina korkeintaan 25 uusinta viestiä.
//   
// @GetMapping("/comments")
//    public String redirect(Model model) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//        Account user = accountRepository.findByUsername(username);
//        if(user.getPicture()!=null){
//         System.out.println("iddddphoto"+ user.getPicture().getPictureId());
//          return "redirect:/messages/photos/"+user.getPicture().getPictureId();
//        }
//       return "redirect:/messages/photos";
//    }

    @PostMapping("/comments/messages/{id}")
    public String createComment(@RequestParam String content,@PathVariable Long id) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        Comment comment= new Comment();
        comment.setComment(content);

        comment.setUser(accountRepository.findByUsername(username));
        commentRepository.save(comment);
        
        Message message=msgRepository.getOne(id);
        message.getComments().add(comment);
        msgRepository.save(message);

        return "redirect:/messages/{id}/comments";
    }


}
