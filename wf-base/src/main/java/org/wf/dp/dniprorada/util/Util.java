package org.wf.dp.dniprorada.util;

import com.google.common.io.Files;
import org.activiti.engine.EngineServices;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

public final class Util {

    public static final String PATTERN_FILE_PATH_BEGIN = "../webapps/wf/WEB-INF/classes/pattern/";
    public static final String MARKERS_MOTION_FILE_PATH_BEGIN = "../webapps/wf/WEB-INF/classes/bpmn/markers/motion/";
    public static final String PATTERN_DEFAULT_CONTENT_TYPE = "text/plain";
    private final static Logger log = LoggerFactory.getLogger(Util.class);

    private static final String DEFAULT_ENCODING = "UTF-8";

    private Util() {
    }

    /**
     * Resolves file content based on specified smart file path string and base file path.
     * Examples of the smart paths: "[/custom.html]", "[*]"
     *
     * @param smartPath       A possible smart path string starting from [
     * @param basePath        Base path to be prepended
     * @param defaultFilePath If the string equals to "[*]" than this value will be used
     * @return File content. If a passed string was not a smart file path
     * (e.g. it does not start and end with "[" and "]"), then "null" is returned
     */
    public static String getSmartPathFileContent(String smartPath, String basePath, String defaultFilePath) {
        if (smartPath == null || smartPath.isEmpty() || !smartPath.startsWith("[") || !smartPath.endsWith("]")) {
            return null;
        }

        smartPath = new StringBuilder(smartPath)
                .deleteCharAt(smartPath.length() - 1)
                .deleteCharAt(0)
                .toString();

        Path path = smartPath.equals("*")
                ? Paths.get(basePath, defaultFilePath)
                : Paths.get(basePath, smartPath);

        String pathString = path.toString();
        URL resource = Util.class.getClassLoader().getResource(pathString);
        if (resource == null) {
            log.error("[getSmartPathFileContent] Cannot find the file " + path);
            return null;
        }

        try {
            path = Paths.get(resource.toURI());
            return new String(Files.toByteArray(path.toFile()), DEFAULT_ENCODING);
        } catch (URISyntaxException | IOException e) {
            log.error("[getSmartPathFileContent] Cannot read the file " + path, e);
            return null;
        }
    }

    public static byte[] getPatternFile(String sPathFile) throws IOException {
        return getResourcesFile(PATTERN_FILE_PATH_BEGIN, sPathFile);
    }
    
    public static byte[] getMarkersMotionJson(String sPathFile) throws IOException {
        return getResourcesFile(MARKERS_MOTION_FILE_PATH_BEGIN, sPathFile);
    }
    
    private static byte[] getResourcesFile(String sRootFolder, String sPathFile) throws IOException {
        if (sPathFile.contains("..")) {
            throw new IllegalArgumentException("incorrect sPathFile!");
        }
        String fullFileName = sRootFolder + sPathFile;
        File file = new File(fullFileName);
        log.info("Loading pattern file:" + fullFileName);
        return Files.toByteArray(file);
    }

    public static String sData(byte[] a) {
        String s = "Not convertable!";
        try {
            s = new String(a, DEFAULT_ENCODING);
        } catch (Exception oException) {
            log.error("[sData]", oException);
        }
        return s;
    }

    public static byte[] aData(String s) {
        //log.info("[aData]:s=" + s);
        byte[] a = s.getBytes(Charset.forName(DEFAULT_ENCODING));
        //log.info("[aData]:a.length=" + a.length + ",Arrays.toString(a)=" + Arrays.toString(a));
        return a;
    }

    public static String contentByteToString(byte[] contentByte) {
        BASE64Encoder encoder = new BASE64Encoder();
        String contentString = encoder.encode(contentByte);
        return contentString;
    }

    public static byte[] contentStringToByte(String contentString) {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] contentByte;
        try {
            contentByte = decoder.decodeBuffer(contentString);
        } catch (Exception ex) {
            contentByte = new byte[1];
        }

        return contentByte;
    }

    public static boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    public static String httpAnswer(String urlName, String sData)
            throws Exception {

        URL url = new URL(urlName);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("content-type", "application/json;charset=UTF-8");
        con.setDoOutput(true);
        DataOutputStream dos = new DataOutputStream(con.getOutputStream());
        dos.writeBytes(sData);
        dos.flush();
        dos.close();
        BufferedReader br = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String inputLine;
        while ((inputLine = br.readLine()) != null) {
            sb.append(inputLine);
        }
        br.close();
        return sb.toString();
    }

    private static Collection<File> getPatternFiles() {
        File directory = new File("../webapps/wf/WEB-INF/classes/pattern/print");
        return FileUtils.listFiles(directory, null, true);
    }

    public static void replacePatterns(DelegateExecution execution, DelegateTask task, Logger oLog) {
        try {
            oLog.info("[replacePatterns]:task.getId()=" + task.getId());
            //oLog.info("[replacePatterns]:execution.getId()=" + execution.getId());
            //oLog.info("[replacePatterns]:task.getVariable(\"sBody\")=" + task.getVariable("sBody"));
            //oLog.info("[replacePatterns]:execution.getVariable(\"sBody\")=" + execution.getVariable("sBody"));

            EngineServices oEngineServices = execution.getEngineServices();
            RuntimeService oRuntimeService = oEngineServices.getRuntimeService();
            TaskFormData oTaskFormData = oEngineServices
                    .getFormService()
                    .getTaskFormData(task.getId());

            oLog.info("[replacePatterns]:Found taskformData=" + oTaskFormData);
            if (oTaskFormData == null) {
                return;
            }

            Collection<File> asPatterns = getPatternFiles();
            for (FormProperty oFormProperty : oTaskFormData.getFormProperties()) {
                String sFieldID = oFormProperty.getId();
                String sExpression = oFormProperty.getName();

                oLog.info("[replacePatterns]:sFieldID=" + sFieldID);
                //oLog.info("[replacePatterns]:sExpression=" + sExpression);
                oLog.info("[replacePatterns]:sExpression.length()=" + (sExpression != null ?
                        sExpression.length() + "" :
                        ""));

                if (sExpression == null || sFieldID == null || !sFieldID.startsWith("sBody")) {
                    continue;
                }

                for (File oFile : asPatterns) {
                    String sName = "pattern/print/" + oFile.getName();
                    //oLog.info("[replacePatterns]:sName=" + sName);

                    if (sExpression.contains("[" + sName + "]")) {
                        oLog.info("[replacePatterns]:sName=" + sName);

                        String sData = getFromFile(oFile, null);
                        //oLog.info("[replacePatterns]:sData=" + sData);
                        oLog.info("[replacePatterns]:sData.length()=" + (sData != null ? sData.length() + "" : "null"));
                        if (sData == null) {
                            continue;
                        }

                        sExpression = sExpression.replaceAll("\\Q[" + sName + "]\\E", sData);
                        //                        oLog.info("[replacePatterns]:sExpression=" + sExpression);

                        oLog.info("[replacePatterns](sFieldID=" + sFieldID + "):1-Ok!");
                        oRuntimeService.setVariable(task.getProcessInstanceId(), sFieldID, sExpression);
/*                        oLog.info("[replacePatterns](sFieldID=" + sFieldID + "):2-Ok:" + oRuntimeService
                                .getVariable(task.getProcessInstanceId(), sFieldID));*/
                        oLog.info("[replacePatterns](sFieldID=" + sFieldID + "):3-Ok!");
                    }
                    oLog.info("[replacePatterns](sName=" + sName + "):Ok!");
                }
                oLog.info("[replacePatterns](sFieldID=" + sFieldID + "):Ok!");
            }
        } catch (Exception oException) {
            oLog.error("[replacePatterns]", oException);
        }
    }

    public static String getFromFile(File file, String sCodepage) throws IOException {
        byte[] aByte = getBytesFromFile(file);
        //return Util.sData(aByte);
        //java.lang.
        if (aByte == null) {
            return null;
        }
        return new String(aByte, sCodepage == null ? DEFAULT_ENCODING : sCodepage);
        //Charset.forName(DEFAULT_ENCODING)
        //Cp1251
    }

    public static byte[] getBytesFromFile(File file) throws IOException {

        InputStream is = new FileInputStream(file);
        long length = file.length();

        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {

            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();

        return bytes;
    }

    public static String setStringFromFieldExpression(Expression expression,
            DelegateExecution execution, Object value) {
        if (expression != null && value != null) {
            expression.setValue(value, execution);
        }
        return null;
    }

    public static String deleteContextFromURL(String URL) {
        String temp = URL.substring(URL.indexOf("//") + 2);
        return temp.substring(temp.indexOf("/"));
    }

}
