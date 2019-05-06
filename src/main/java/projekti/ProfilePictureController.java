package projekti;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class ProfilePictureController {

    @Autowired
    private AccountRepository accountRepository;

     @Autowired
    private ProfilePictureRepository ppictureRepository;  
    
    @Autowired
    private PhotoObjectRepository photoRepository; //luodaan gif=PhotoObjectRepository  prepository olio

    //creates profilepicture
    @PostMapping("profilepicture/photos/{id}")
    public String setProfilePicture(Model model, @PathVariable Long id) {
        
        System.out.println("pictureiiiiiiiiiidddddddddddddd"+id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user=accountRepository.findByUsername(username);
     
//        PhotoObject pp = photoRepository.getOne(id);
         ProfilePicture ppicture= new  ProfilePicture(); 
         ppicture.setPictureId(id);
         ppictureRepository.save(ppicture);
         
         if( user.getPicture()!=null){//jPoistetaan jos on edellinen profiilipicture
        // pictureRepository.delete(user.getPicture());
          ProfilePicture picture= user.getPicture();
          picture.setPictureId(id);   
          ppictureRepository.save(picture);
          
          user.getPicture().setPictureId(id); 
          accountRepository.save(user);
         }
         else{
         user.setPicture(ppicture);
        accountRepository.save(user);
         }
        return "redirect:/messages/photos/{id}";
    }
}