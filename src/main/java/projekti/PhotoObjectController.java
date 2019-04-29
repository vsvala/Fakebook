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
    private ProfileRepository profileRepository;

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
          
        if(account.getProfiles().get(0).getProfilePicture() != null) {
        PhotoObject p = account.getProfiles().get(0).getProfilePicture();
        model.addAttribute("profilePicture", p.getDescription());

    }        return "photos";
    
    }

    @GetMapping(path = "/photos2/{id}", produces = "image/*")//yksittäisenkuvan näyttö
    public String getImage(Model model, @PathVariable Long id) {

        model.addAttribute("count", photoRepository.count());

//         if (photoRepository.count() > 1) {
//                  model.addAttribute("next", (id + 1));
//         }
//  
        model.addAttribute("next", (id + 1));
        model.addAttribute("previous", (id - 1));
        model.addAttribute("current", (id));

        return "photos";
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
    @GetMapping(path = "/photos/{id}", produces = "image/*")//yksittäisen kuvan näyttö
    @ResponseBody
    public byte[] get(@PathVariable Long id) {
        return photoRepository.getOne(id).getContent();

    }
    
////            // Get pyyntö kuvan tuottamiselle
////    @GetMapping(path = "/photos/{photoId}", produces = "image/jpeg")
////    @ResponseBody
////    public byte[] getKuva(@PathVariable Long photoId) throws SQLException {
////        return this.photoRepository.getOne(photoId).getContent();
////    }

    

    @PostMapping("/photos/{id}/profilepicture")//yksittäisen kuvan näyttö
    public String setProfilePicture(Model model, @PathVariable Long id) {
        System.out.println("pictureiiiiiiiiiidddddddddddddd" + id);
        PhotoObject pp = photoRepository.getOne(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Profile profile=accountRepository.findByUsername(username).getProfiles().get(0);
        profile.setProfilePicture(pp);
        profileRepository.save(profile);
        
       
        
        return "redirect:/account/profile/{id}/profilepicture";
    }

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
    //poistaminen pitää kattoo kytkökset..
    @DeleteMapping(path = "/photos/{id}")
    public String delete(@PathVariable Long id) {
        // PhotoObject f = foRepository.findOne(id);
        PhotoObject f = photoRepository.getOne(id);
        photoRepository.delete(f);

        return "redirect:/photos";

    }
}
