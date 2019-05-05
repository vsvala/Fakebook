package projekti;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PhotoObjectController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

     @Autowired
    private ProfilePictureRepository pictureRepository;  
    
    @Autowired
    private PhotoObjectRepository photoRepository; //luodaan gif=PhotoObjectRepository  prepository olio

    @GetMapping("/photos")
    public String redirect(Model model) {

        model.addAttribute("photos", photoRepository.findAll());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = accountRepository.findByUsername(username);
        Account account = accountRepository.getOne(user.getId());
        
        List<PhotoObject> photos = accountRepository.findByUsername(username).getPhotos();
        model.addAttribute("myphotos", photos);
        model.addAttribute("user", username);
        model.addAttribute("count", photos.size());
          
//        if(account.getFriendships().get(0).getProfilePicture() != null) {
//        PhotoObject p = account.getFriendships().get(0).getProfilePicture();
//        model.addAttribute("profilePicture", p.getDescription());

//    }      
    return "photos";
    
    }

    
     @GetMapping(value = "photos2/{id}",  produces = "image/*")//)//yksittäisen kuvan näyttö
    public String getimages(Model model, @PathVariable Long id) {
        Long imageCount = photoRepository.count();

        model.addAttribute("count", imageCount);

       // if (id >= 1L && id <= imageCount) {
            model.addAttribute("current", id);
       // }

        if (id < imageCount && id > 0L) {
            model.addAttribute("next", id + 1);
        }

        if (id > 1L) {
            model.addAttribute("previous", id - 1);
        }
        
            model.addAttribute("photos", photoRepository.findAll());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = accountRepository.findByUsername(username);
        Account account = accountRepository.getOne(user.getId());
        
        List<PhotoObject> photos = accountRepository.findByUsername(username).getPhotos();
        model.addAttribute("myphotos", photos);
        model.addAttribute("user", username);
        model.addAttribute("count", photos.size());
//          
//        if(account.getFriendships().get(0).getProfilePicture() != null) {
//        PhotoObject p = account.getFriendships().get(0).getProfilePicture();
//        model.addAttribute("profilePicture", p.getDescription());
//        }
//  
     return "photos";
    }
    
         
    @GetMapping(path = "/photos2/{id}/content", produces = "image/*")
    @ResponseBody
    public byte[] getContent(@PathVariable Long id) {
        return photoRepository.getOne(id).getContent();
    }
    
    
 

//vain kirjautunut käyttäjä voi tallentaa.jonka rooli..
    @PostMapping("/photos")
    public String savePhoto(@RequestParam("file") MultipartFile file, @RequestParam String description) throws IOException {
        PhotoObject p = new PhotoObject();
        p.setDescription(description);
        p.setContent(file.getBytes());

//       if(file.getContentType().equals("image/*")){
//        System.out.println("on image");  }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account account = accountRepository.findByUsername(username);
        p.setUser(accountRepository.findByUsername(username));
        photoRepository.save(p);
        account.getPhotos().add(p);
        accountRepository.save(account);

        return "redirect:/photos";
    }


    @GetMapping(path = "/photos/{id}", produces = "image/*")//yksittäisen kuvan näyttö
    @ResponseBody
    public byte[] get(@PathVariable Long id) {
        return photoRepository.getOne(id).getContent();

    }
    

    @PutMapping("/photos/{id}/profilepicture")//yksittäisen kuvan näyttö
    public String setProfilePicture(Model model, @PathVariable Long id) {
        
        System.out.println("pictureiiiiiiiiiidddddddddddddd"+id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user=accountRepository.findByUsername(username);
     
//        PhotoObject pp = photoRepository.getOne(id);
         ProfilePicture picture= new  ProfilePicture(); 
         picture.setPictureId(id);
         
         if( user.getPicture()!=null){//jPoistetaan jos on edellinen profiilipicture
        // pictureRepository.delete(user.getPicture());
          user.getPicture().setPictureId(id);
//         accountRepository.save(user);
           }
//         else{
         pictureRepository.save(picture);
         user.setPicture(picture);
        accountRepository.save(user);
//         }
        return "redirect:/messages/photos/{id}";
    }

    
     //poistaminen pitää kattoo kytkökset..
    @DeleteMapping(path = "/photos/{id}")
    public String delete(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account account = accountRepository.findByUsername(username);
        PhotoObject p = photoRepository.getOne(id);
        //p.getUser().getPhotos().remove(p);
      
        account.getPhotos().remove(p);
        accountRepository.save(account);
        
        
//        System.out.println("photooid"+id);
//        //PhotoObject foto = photoRepository.findById(id);
//        PhotoObject f = photoRepository.getOne(id);
//       //System.out.println("photoo"+f);
//  
        photoRepository.delete(p);

        return "redirect:/photos";

    }
}

  


//
//    @PutMapping("/photos/{id}/profilepicture")//yksittäisen kuvan näyttö
//    public String setProfilePicture(Model model, @PathVariable Long id) {
//        
//        System.out.println("pictureiiiiiiiiiidddddddddddddd" + id);
//        PhotoObject pp = photoRepository.getOne(id);
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//        Account account=accountRepository.findByUsername(username);
//        
//         //ProfilePicture picture= photoRepository.getOne(id);
//
////       if(picture != null){ //jos on jo kuva edellinen poistetaan ennen lisäystä
//         //profilePictureRepository.delete(picture); 
//        // }
//        //profilePictureRepository.save(picture);     //get all
//        
//       pp.setProfilePictureId(id);
//       pp.setFriendship(account.getFriendships().get(0));
//        photoRepository.save(pp);
////       profile.setProfilePicture(pp);
////        friendshipRepository.save(profile);
//     
//        return "redirect:/messages/photos/{id}/profilepicture";
//    }
//
//


















//    @GetMapping(path = "/photos/{id}")//yksittäisen kuvan näyttö
//    public ResponseEntity<byte[]> viewFile(@PathVariable Long id) {
//        // PhotoObject f = foRepository.findOne(id);
//        PhotoObject f = foRepository.getOne(id);
//
//        final HttpHeaders headers = new HttpHeaders();
//       headers.setContentType(MediaType.parseMediaType(f.getContentType()));
////        headers.setContentLength(f.getContentLength());
//        return new ResponseEntity<>(f.getContent(), headers, HttpStatus.CREATED);
//    }

////////    @PostMapping("/photos/{id}/profiles")
////////    public String saveProfilepicture(Model model, @PathVariable Long id) throws IOException {
////////
////////        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
////////        String username = auth.getName();
////////        Account user = accountRepository.findByUsername(username);
////////
////////        PhotoObject p = photoRepository.getOne(id);
////////        p.setUser(user);
////////        photoRepository.save(p);
//////////        p.setDescription(description);
//////////        p.setContent(file.getBytes());       
//////////        
////////////       if(file.getContentType().equals("image/*")){
////////////        System.out.println("on image");  }
//////////       
////////
//////////        p.setUser(accountRepository.findByUsername(username)); 
//////////        photoRepository.save(p);
//////////       account.getPhotos().add(p);
//////////       accountRepository.save(account);
////////        return "redirect:/photos";
////////    }