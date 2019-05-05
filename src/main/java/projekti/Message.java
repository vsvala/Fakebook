/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
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
public class Message extends AbstractPersistable<Long>  {
  
    @ManyToOne
    private Account user;  
    //private Account  accountFrom;   
    
//    @ManyToOne
//    private Account accountTo;
//

    private LocalDateTime messageDate = LocalDateTime.now();
    private String message;
    
    @ManyToOne
    private PhotoObject photo;  
    
//    @ElementCollection(fetch = FetchType.LAZY)
//    private List<Account> likes = new ArrayList<>();
//    
//    @ElementCollection(fetch = FetchType.LAZY)
//    private List<Comment> comments = new ArrayList<>();
}
