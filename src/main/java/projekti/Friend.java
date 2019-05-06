/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
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
public class Friend extends AbstractPersistable<Long>{
//    
    @ManyToMany(mappedBy = "friends")
    private List<Account>accounts = new ArrayList<>();
    @Column(unique=true)
    private String username;
    private String friendname;
       
}

 