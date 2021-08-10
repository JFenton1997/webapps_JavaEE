package webapps.jsf;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import webapps.ejb.UserTransactionAndMoneyService;
import webapps.entity.MoneyTransaction;
import webapps.entity.SystemUser;

@Named("UserMoneyBean")
@RequestScoped
public class UserMoneyBean implements Serializable {

    @EJB
    UserTransactionAndMoneyService UTMS;

    private String recieverName;
    private float money;

    public String getRecieverName() {
        return recieverName;
    }

    public void setRecieverName(String recieverName) {
        this.recieverName = recieverName;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getCurrentUserName() {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getUserPrincipal().getName();
    }

    public List<SystemUser> getCurrentUser() {
        try {
            return UTMS.getUserInfo(getCurrentUserName());

        } catch (NumberFormatException e) {
            return null;
        }
    }

    public float getCurrentUserMoney() {
        try {
            return showCorrectCurrencyFromDB(UTMS.getSingleUser(getCurrentUserName()).getMoney());

        } catch (Exception e) {
            System.err.println(e);
        }
        return -99999;

    }

    public List<MoneyTransaction> getFinalisedTransactions() {
        try {
            return UTMS.getCompleteTransactionsInvolvingUser(this.getCurrentUserName());
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    public List<MoneyTransaction> getPaymentRequestsOfUser() {
        try {
            return UTMS.getPaymentRequestsAimedAtUser(this.getCurrentUserName());
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    public String submitPayment() {
        if (UTMS.getSingleUser(recieverName) != null) {
            
            UTMS.doPayment(this.getCurrentUserName(), recieverName, this.getCorrectCurrencyForDB(money));
            return "user";
        } else {
            return "error_1";
        }

    }

    public String acceptRequest(MoneyTransaction t) {
        System.out.println("webapps.jsf.UserMoneyBean.acceptRequest()");
        System.out.println(t.getId());
        UTMS.acceptRequest(t.getId());
        return "viewRequests";
    }

    public String refuseRequest(MoneyTransaction t) {
        UTMS.refuseRequest(t.getId());
        return "viewRequests";
    }

    public String submitRequest() {
        if (UTMS.getSingleUser(recieverName) != null) {
            UTMS.requestPayment(recieverName, this.getCurrentUserName(), this.getCorrectCurrencyForDB(money));
            return "user";
        } else {
            return "error_1";
        }

    }
    
    public String getUserCurrencySymbol(){
        String usersCurrency =  UTMS.getSingleUser(getCurrentUserName()).getCurrency();
        switch(usersCurrency){
            case "P":
                return "£";
            case "D":
                return "$";
            case "E":
                return "€";
            default:
                return usersCurrency;
        }
    }
    
    public float showCorrectCurrencyFromDB(float money){
        return this.moneyConvertFromPound(money, UTMS.getSingleUser(getCurrentUserName()).getCurrency());
    }
    
    public float getCorrectCurrencyForDB(float money){
        return this.moneyConvertToPound(money, UTMS.getSingleUser(getCurrentUserName()).getCurrency());
    }
    
    
    
    private final DecimalFormat df = new DecimalFormat("0.00");
    final float PtE = (float) 1.16;
    final float PtD = (float) 1.39;
    public float moneyConvertToPound(float money, String fromCurrency) {
        float displayMoney;
        switch (fromCurrency) {
            case "D":
                displayMoney=  money / PtD;
                break;
            case "E":
                displayMoney= money / PtE;
                break;
            default:
                displayMoney=  money;
                break;
        }
        return Float.parseFloat(df.format(displayMoney));
    }

    public float moneyConvertFromPound(float money, String toCurrency) {
         float displayMoney;
        switch (toCurrency) {
            case "D":
                displayMoney=  money * PtD;
                break;
            case "E":
                displayMoney= money * PtE;
                break;
            default:
                displayMoney=  money;
                break;
        }
        return Float.parseFloat(df.format(displayMoney));
    }

}
