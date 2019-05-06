package projekti;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
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
    @ManyToMany(mappedBy = "friendships")
    private List<Account>accounts = new ArrayList<>();
    //private Account user;    
    // @ManyToMany
//    private List<Friend> friends; //= new ArrayList<>();

}
