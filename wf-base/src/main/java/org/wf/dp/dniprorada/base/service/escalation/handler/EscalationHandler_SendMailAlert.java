package org.wf.dp.dniprorada.base.service.escalation.handler;

import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.wf.dp.dniprorada.util.Mail;
import org.wf.dp.dniprorada.util.Util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EscalationHandler_SendMailAlert
        implements EscalationHandler {

    private static final Logger log = Logger.getLogger(EscalationHandler_SendMailAlert.class);

    @Autowired
    Mail oMail;

    @Override
    public void execute(Map<String, Object> mParam, String[] asRecipientMail, String sPatternFile) {
//        //check input data
//        if (params.length != 3){
//            throw new IllegalArgumentException("wrong input data!");
//        }
//        Map<String, Object> mParam = (Map<String, Object>) params[0];
//        String[] asRecipientMail = (String[]) params[1];
//        String sPatternFile = (String) params[2];

        //create email body
        String sBody = null;
        try {
            sBody = Util.getPatternFile(sPatternFile).toString();
        } catch (IOException e) {//??
            log.error("error during finding the pattern file! path=" + sPatternFile, e);
            throw new IllegalArgumentException("wrong pattern path! path=" + sPatternFile, e);
        }
        if (sBody == null) {
            throw new IllegalArgumentException("wrong pattern data! path=" + sPatternFile);
        }
        //??
        String sHead = "Ескалація задачі";
        //
        for (String key : mParam.keySet()) {
            sBody = sBody.replaceAll(String.format("[%s]", key), mParam.get(key).toString());
        }

        for (String recipient : asRecipientMail) {
            try {
                sendEmail(sHead, sBody, recipient);
            } catch (EmailException e) {
                log.error("error sending email!", e);
            }
        }

    }

    private void sendEmail(String sHead, String sBody, String recipient) throws EmailException {
        oMail.reset();
        oMail
                //._From(mailAddressNoreply)
                ._To(recipient)
                ._Head(sHead)
                ._Body(sBody)
        //._AuthUser(mailServerUsername)
        //._AuthPassword(mailServerPassword)
        //._Host(mailServerHost)
        //._Port(Integer.valueOf(mailServerPort))
        //._SSL(true)
        //._TLS(true)
        ;
        oMail.send();
    }

    public static void main(String[] args) {
        Map<String, Object> param = new HashMap<>();
        //[Surname],[Name],[Middlename]
        param.put("[Surname]", "Petrenko");
        param.put("[Name]", "Petro");
        param.put("[Middlename]", "Petrovych");

        String[] recipients = new String[2];
        recipients[0] = "olga2012olga@gmail.com";
        recipients[1] = "olga.prylypko@gmail.com";

        String file = "print/kiev_dms_print1.html";

        new EscalationHandler_SendMailAlert().execute(param, recipients, file);

    }
}
