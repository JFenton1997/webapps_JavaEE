package webapps.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;


@NamedQueries({
    @NamedQuery(name="findByUsername", query="SELECT u FROM SystemUser u WHERE u.username = :name"),
    @NamedQuery(name="getAllUserData", query="SELECT u FROM SystemUser u")

})
@Entity
public class SystemUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // here on could use Bean Validation annotations to enforce specific rules - this could be alternatively implemented when validating the form in the web tier
    // for now we check only for Null values
    @NotNull
    String username;

    // here on could use Bean Validation annotations to enforce specific rules - this could be alternatively implemented when validating the form in the web tier
    // for now we check only for Null values
    @NotNull
    String userpassword;

    @NotNull
    String name;

    @NotNull
    String surname;
    
    @NotNull
    float money;
            
    @NotNull
    String currency;

    public SystemUser() {
    }

    public SystemUser(String username, String userpassword, String name, String surname, float money, String currency) {
        this.username = username;
        this.userpassword = userpassword;
        this.name = name;
        this.surname = surname;
        this.money = money;
        this.currency = currency;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }




    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.username);
        hash = 97 * hash + Objects.hashCode(this.userpassword);
        hash = 97 * hash + Objects.hashCode(this.name);
        hash = 97 * hash + Objects.hashCode(this.surname);
        hash = 97 * hash + Float.floatToIntBits(this.money);
        hash = 97 * hash + Objects.hashCode(this.currency);
        return hash;
    }

}
