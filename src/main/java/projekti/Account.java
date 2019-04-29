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
  
    
    private String name;
    @ManyToMany//(mappedBy = "accounts")
    private List<Profile>profiles= new ArrayList<>();
    private String username;
    private String password;
    private String identifier;
  
    @OneToMany// @Size( min=0,max=10)
    private List<PhotoObject>photos= new ArrayList<>();;
   
}
