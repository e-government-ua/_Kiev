package ua.org.egov.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.org.egov.service.model.PublicCitizenInfo;

@Controller
@RequestMapping(value = "/public",
        produces = {MediaType.APPLICATION_JSON_VALUE})
public class PublicResource {

    @RequestMapping(value = "/citizen/stub", method = RequestMethod.GET)
    @ResponseBody
    public PublicCitizenInfo getPublicCitizenInfo() {
        PublicCitizenInfo info = new PublicCitizenInfo();
        info.setName("name");
        info.setLastName("lastName");
        return info;
    }
}
