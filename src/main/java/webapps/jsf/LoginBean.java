package webapps.jsf;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class LoginBean implements Serializable {

    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
       
            request.logout();
//            context.addMessage(null, new FacesMessage("User is logged out"));
            return "index";
        } catch (ServletException e) {
            context.addMessage(null, new FacesMessage("Logout failed."));
            return "index";
        }
    }
    
}
