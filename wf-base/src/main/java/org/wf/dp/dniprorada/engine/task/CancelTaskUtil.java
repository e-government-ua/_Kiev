package org.wf.dp.dniprorada.engine.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wf.dp.dniprorada.base.dao.AccessDataDao;
import org.wf.dp.dniprorada.util.GeneralConfig;
import org.wf.dp.dniprorada.util.luna.AlgorithmLuna;

@Component
public class CancelTaskUtil {

    @Autowired
    private AccessDataDao accessDataDao;

    @Autowired
    private GeneralConfig generalConfig;
    
    private static final Logger log = LoggerFactory.getLogger(CancelTaskUtil.class);
    private static final String sURL_CancelTask =  "/wf-region/service/cancelTask"; //????


    private static final String TAG_action = "[sURL_CancelTask]";
    private static final String canselButtonHTML = new StringBuilder()
            .append("<form method=\"POST\" action=\"")
            .append(TAG_action)
            .append("\" ")
            .append("accept-charset=\"utf-8\">")
            .append("Ви можете скасувати свою заявку, вказавши причину в цьому полі: <br/>\n")
            .append("<input type=\"text\" name=\"sInfo\"/><br/>\n")
            .append("<input type=\"button\" name=\"submit\" ")
            .append("value=\"Скасувати заявку!\"/>")
        .append("</form>").toString();

    public String getCancelFormHTML(Long nID_Task) throws Exception {


        String sAccessKey = accessDataDao.setAccessData("" + nID_Task);
        String sURL_CancelTaskService = generalConfig.sHostCentral() + sURL_CancelTask;
        String sURL_CancelTaskAction = new StringBuilder(sURL_CancelTaskService)
                .append("?")
                .append("sAccessKey=").append(sAccessKey)
                .append("&nID_Protected=").append(AlgorithmLuna.getProtectedNumber(nID_Task))
                .append("&sAccessConract=Request")
                .toString();

        log.info("total URL for action =" + sURL_CancelTaskAction);

        String buttonStr = canselButtonHTML.replace(TAG_action, sURL_CancelTaskAction);
        return buttonStr;
    }

}
