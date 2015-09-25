package org.wf.dp.dniprorada.engine.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wf.dp.dniprorada.base.dao.AccessDataDao;
import org.wf.dp.dniprorada.util.GeneralConfig;

@Component
public class CancelTaskUtil {

    @Autowired
    private AccessDataDao accessDataDao;

    @Autowired
    private GeneralConfig generalConfig;
    
    private static final Logger log = LoggerFactory.getLogger(CancelTaskUtil.class);
    private static final String sURL_CancelTask =  "/wf/service/rest/tasks/cancelTask";



    private static final String TAG_action = "[sURL_CancelTask]";
    private static String TAG_nID_Protected = "[nID_Protected]";
    private static final String cancelButtonHTML = new StringBuilder()
            .append("<form method=\"POST\" action=\"")
            .append(TAG_action)
            .append("\" ")
            .append("accept-charset=\"utf-8\">")
            .append("Ви можете скасувати свою заявку, вказавши причину в цьому полі: <br/>\n")
            .append("<input type=\"text\" name=\"sInfo\"/><br/>\n")
            //.append("<input type=\"hidden\" name=\"nID_Protected\" value=\"")
            //.append(TAG_nID_Protected + "\"/><br/>\n")
            .append("<input type=\"submit\" name=\"submit\" ")
            .append("value=\"Скасувати заявку!\"/>")
            .append("</form>")
                .toString();

    public String getCancelFormHTML(Long nID_Protected) throws Exception {


        String sURL_ForAccessKey = new StringBuilder(sURL_CancelTask)
                .append("?nID_Protected=").append(nID_Protected)
                .toString();
        String sAccessKey = accessDataDao.setAccessData(sURL_ForAccessKey);
        String sURL_CancelTaskAction = new StringBuilder(generalConfig.sHost())
                .append(sURL_ForAccessKey)
                .append("&sAccessContract=Request")
                .append("&sAccessKey=").append(sAccessKey)
                .toString();
        log.info("total URL for action =" + sURL_CancelTaskAction);

        String cancelBtn = cancelButtonHTML.replace(TAG_action, sURL_CancelTaskAction)
                .replace(TAG_nID_Protected, "" + nID_Protected);
        return cancelBtn;
    }
}
