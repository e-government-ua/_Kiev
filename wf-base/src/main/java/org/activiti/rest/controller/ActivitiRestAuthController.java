package org.activiti.rest.controller;

import org.activiti.engine.ProcessEngines;
import org.activiti.rest.controller.entity.LoginResponse;
import org.activiti.rest.controller.entity.LoginResponseI;
import org.activiti.rest.controller.entity.LogoutResponse;
import org.activiti.rest.controller.entity.LogoutResponseI;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/auth")
public class ActivitiRestAuthController {

    @RequestMapping(value = { "/login", "/login-v2" }, method = RequestMethod.POST)
    public
    @ResponseBody
    LoginResponseI login(@RequestParam(value = "sLogin") String login,
            @RequestParam(value = "sPassword") String password, HttpServletRequest request)
            throws ActivitiAuthException {
        if (ProcessEngines.getDefaultProcessEngine().getIdentityService().checkPassword(login, password)) {
            request.getSession(true);
            return new LoginResponse(Boolean.TRUE.toString());
        } else {
            throw new ActivitiAuthException(ActivitiAuthException.Error.LOGIN_ERROR, "Login or password invalid");
        }
    }

    @RequestMapping(value = "/logout", method = { RequestMethod.DELETE, RequestMethod.POST })
    public
    @ResponseBody
    LogoutResponseI logout(HttpServletRequest request) throws ActivitiAuthException {
        HttpSession session = request.getSession();
        if (session.isNew()) {
            throw new ActivitiAuthException(ActivitiAuthException.Error.LOGOUT_ERROR,
                    "Client doesn't have a valid server session");
        } else {
            session.invalidate();
            return new LogoutResponse(session.getId());
        }
    }
}
