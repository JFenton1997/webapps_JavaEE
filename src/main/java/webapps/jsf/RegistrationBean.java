package webapps.jsf;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import webapps.ejb.UserService;

/**
 *
 * @author parisis
 */
@Named
@RequestScoped
public class RegistrationBean {

    @EJB
    UserService usrSrv;
    
    String username;
    String userpassword;
    String name;
    String surname;
    String currency;

    public RegistrationBean() {

    }

    //call the injected EJB
    public String registerUser() {
        this.currency = currency.toUpperCase();
        System.out.println(currency);
        if(!"P".equals(currency) && !"D".equals(currency) && !"E".equals(currency)){
            System.out.println("ERROR - invalid currency");
            return "reg_error";
        }
        else{
            usrSrv.registerUser(username, userpassword, name, surname,1000,currency);
            System.out.println("webapps.jsf.RegistrationBean.register()  User Success");
            
            return "index";
        }
    }
    
    public String registerAdmin() {
        this.currency = currency.toUpperCase();
        System.out.println(currency);
        if(!"P".equals(currency) && !"D".equals(currency) && !"E".equals(currency)){
            System.out.println("ERROR - invalid currency");
            return "reg_error";
        }
        else{
            usrSrv.registerAdmin(username, userpassword, name, surname,0,currency);
            
            return "admin";
        }
    }
    
    public UserService getUsrSrv() {
        return usrSrv;
    }

    public void setUsrSrv(UserService usrSrv) {
        this.usrSrv = usrSrv;
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
    
    public void setCurrency(String currency){
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }
    

    
}
