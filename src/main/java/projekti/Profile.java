package projekti;

import java.time.LocalDate;
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
public class Profile extends AbstractPersistable<Long> {
    
  
    @OneToOne 
    private PhotoObject profilePicture;
    
    private String profileName;
    @ManyToMany(mappedBy = "profiles")
    private List<Account>accounts = new ArrayList<>();
    //private Account user;    
   // private String identifier;
 // @ManyToMany
//    private List<Friend> friends; //= new ArrayList<>();


}
//import javax.persistence.ManyToOne;
//
///**
// *
// * @author svsv
// */
//public class Profile {
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
