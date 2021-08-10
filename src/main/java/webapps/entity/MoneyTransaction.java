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
    @NamedQuery(name="getFinalisedUserTransactions", query="SELECT t FROM MoneyTransaction t WHERE (t.receivingUser = :name OR t.sendingUser = :name ) AND t.finalised = TRUE"),
    @NamedQuery(name="getPaymentRequests", query="SELECT t FROM MoneyTransaction t WHERE t.sendingUser = :name  AND t.finalised = FALSE"),
    @NamedQuery(name="getAllTransactions", query="SELECT t FROM MoneyTransaction t"),

})
@Entity
public class MoneyTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    
    @NotNull
    String sendingUser;

    @NotNull
    String receivingUser;

    @NotNull
    float money;
    
    @NotNull
    Boolean finalised;
            

    public MoneyTransaction() {
    }

    public MoneyTransaction(String sendingUser, String receivingUser, float money, Boolean finalised) {
        this.sendingUser = sendingUser;
        this.receivingUser = receivingUser;
        this.money = money;
        this.finalised = finalised;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSendingUser() {
        return sendingUser;
    }

    public void setSendingUser(String sendingUser) {
        this.sendingUser = sendingUser;
    }

    public String getReceivingUser() {
        return receivingUser;
    }

    public void setReceivingUser(String receivingUser) {
        this.receivingUser = receivingUser;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public Boolean getFinalised() {
        return finalised;
    }

    public void setFinalised(Boolean finalised) {
        this.finalised = finalised;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.sendingUser);
        hash = 97 * hash + Objects.hashCode(this.receivingUser);
        hash = 97 * hash + Float.floatToIntBits(this.money);
        hash = 97 * hash + Objects.hashCode(this.finalised);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MoneyTransaction other = (MoneyTransaction) obj;
        if (Float.floatToIntBits(this.money) != Float.floatToIntBits(other.money)) {
            return false;
        }
        if (!Objects.equals(this.sendingUser, other.sendingUser)) {
            return false;
        }
        if (!Objects.equals(this.receivingUser, other.receivingUser)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.finalised, other.finalised)) {
            return false;
        }
        return true;
    }




}
