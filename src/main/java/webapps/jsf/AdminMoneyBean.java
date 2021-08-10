package webapps.jsf;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import webapps.ejb.AdminTransactionAndMoneyService;
import webapps.ejb.UserTransactionAndMoneyService;
import webapps.entity.MoneyTransaction;
import webapps.entity.SystemUser;

@Named("AdminMoneyBean")
@RequestScoped
public class AdminMoneyBean implements Serializable {

    @EJB
    AdminTransactionAndMoneyService ATMS;
    @EJB
    UserTransactionAndMoneyService UTMS;


    public String getCurrentUserName() {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getUserPrincipal().getName();
    }

    public List<SystemUser> getCurrentAdmin() {
        try {
            return UTMS.getUserInfo(getCurrentUserName());

        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    public List<SystemUser> getAllUsers(){
        try{
            return ATMS.getAllUsers();
            
            
        }
        catch (NumberFormatException e) {
            return null;
        }
    }
    
     public List<MoneyTransaction> getAllTransactions(){
        try{
            return ATMS.getAllUserTransactions();
            
            
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    
    public String getUserCurrencySymbol(){
        String usersCurrency =  UTMS.getSingleUser(getCurrentUserName()).getCurrency();
        return (getSymbolFromCurrency(usersCurrency));
    }
    
    public String getSymbolFromCurrency(String currency){
        switch(currency){
            case "P":
                return "£";
            case "D":
                return "$";
            case "E":
                return "€";
            default:
                return currency;
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
    
    public String getTransactionType(Boolean t){
        if(t){
            return "COMPLETED";
        }
        else{
            return "PENDING REQUEST";
        }
    }

}
