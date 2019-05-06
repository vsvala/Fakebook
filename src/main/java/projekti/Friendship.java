package projekti;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Friendship extends AbstractPersistable<Long> {
      
   
   // private boolean accepted;
    private LocalDateTime date= LocalDateTime.now();
    private String profileName;
    //private Boolean accepted=false;
    @ManyToMany(mappedBy = "friendships")//tulisko tää sittenki toisinpäin..
    private List<Account>accounts = new ArrayList<>();
    //private Account user;    
   // private String identifier;
 // @ManyToMany
//    private List<Friend> friends; //= new ArrayList<>();
//    @OneToOne 
//    private PhotoObject profilePicture;
//    

}
//import javax.persistence.ManyToOne;
//
///**
// *
// * @author svsv
// */
//public class Friendship {
//
//    // @ManyToOne
//    //private Account user;    
//    private String identifier;
//    private String name;
//    @ManyToMany
//    private List<Person> persons = new ArrayList<>();
//

//    
//}
