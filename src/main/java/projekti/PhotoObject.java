/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class PhotoObject extends AbstractPersistable<Long> {
    
    
    
 
  //  @Basic(fetch = FetchType.LAZY)   //poistoon herokun takia myös lob
   // @Lob 
    private byte[] content;
    private String description;
    @ManyToOne
    private Account user; //id  


}