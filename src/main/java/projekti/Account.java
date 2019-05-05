package projekti;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account extends AbstractPersistable<Long> {
  
   // private boolean accepted;
    private String name;
    private String profilename;
    @ManyToMany//(mappedBy = "accounts")//tää vois olla vaan yks
    private List<Friendship>friendships= new ArrayList<>();
    private String username;
    private String password;
  
    @ManyToMany
    private List<Friend> friends;;
    
    @OneToMany// @Size( min=0,max=10)
    private List<PhotoObject>photos= new ArrayList<>();
 
    @OneToOne 
    private ProfilePicture picture;
//    
//    @OneToOne
//    private Wall wall;
//    
    @OneToMany
    private List<Message> messages;
   
}
