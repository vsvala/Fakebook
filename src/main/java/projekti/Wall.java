/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.util.List;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author svsv
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wall extends AbstractPersistable<Long> { 
    
          @OneToOne
          private Account user;
          @OneToMany
          private List<Message> messages;
    
}

  