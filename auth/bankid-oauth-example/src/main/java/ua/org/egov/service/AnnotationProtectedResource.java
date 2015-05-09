package ua.org.egov.service;

import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.org.bankid.oauth.OAuthRoles;
import ua.org.egov.service.model.ProtectedCitizenInfo;
import ua.org.egov.service.model.PublicCitizenInfo;

@Controller
@RequestMapping(value = "/not_protected",
        produces = {MediaType.APPLICATION_JSON_VALUE})
public class AnnotationProtectedResource {

    @RequestMapping(value = "/citizen/stub", method = RequestMethod.GET)
    @ResponseBody
    public PublicCitizenInfo getPublicCitizenInfo() {
        PublicCitizenInfo info = new PublicCitizenInfo();
        info.setName("name");
        info.setLastName("lastName");
        return info;
    }

//    @PreAuthorize("hasRole('" + OAuthRoles.SERVICE_CONSUMER + "')")
    @Secured(OAuthRoles.SERVICE_CONSUMER)
    @RequestMapping(value = "/citizen/protected_stub", method = RequestMethod.GET)
    @ResponseBody
    public ProtectedCitizenInfo getProtectedCitizenInfo() {
        ProtectedCitizenInfo info = new ProtectedCitizenInfo();
        info.setName("name");
        info.setLastName("lastName");
        info.setInn("1111111111");
        info.setPhone("+380-11-111-1111");
        return info;
    }
}
