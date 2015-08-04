package org.wf.dp.dniprorada.util;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;

public final class Util {

    private final static Logger log = LoggerFactory.getLogger(Util.class);
    public static final String FILE_PATH_BEGIN = "../webapps/wf-region/WEB-INF/classes/pattern/";
    public static final String DEFAULT_CONTENT_TYPE = "text/plain";

    private Util() {
	}

	public static String sData(byte[] a) {
		// Charset.forName("UTF-8")
		// byte[] b = {(byte) 99, (byte)97, (byte)116};
		String s = "Not convertable!";
		log.info("[sData]:a.length=" + a.length + ",Arrays.toString(a)="
				+ Arrays.toString(a));
		try {
			s = new String(a, "UTF-8");
		} catch (Exception oException) {
			log.error("[sData]", oException);
		}
		log.info("[sData]:s=" + s);
		return s;
	}

	public static byte[] aData(String s) {
		log.info("[aData]:s=" + s);
		byte[] a = s.getBytes(Charset.forName("UTF-8"));
		log.info("[aData]:a.length=" + a.length + ",Arrays.toString(a)="
				+ Arrays.toString(a));
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
        
        
        
        
        
        

	//public static void replacePatterns(DelegateExecution execution, Expression osBody, Logger oLog) {
	public static void replacePatterns(DelegateExecution execution, DelegateTask task, Logger oLog) {
                try{
                    /*String sBody=(String)execution.getVariable("sBody");
                    if(sBody==null){
                        return;
                    }*/
                    //String sExpression = getStringFromFieldExpression(osBody, execution);
                	
                    
                    oLog.info("[replacePatterns]:task.getId()="+task.getId());
                    oLog.info("[replacePatterns]:execution.getId()="+execution.getId());
                    oLog.info("[replacePatterns]:task.getVariable(\"sBody\")="+task.getVariable("sBody"));
                    oLog.info("[replacePatterns]:execution.getVariable(\"sBody\")="+execution.getVariable("sBody"));
                            
                	TaskFormData oTaskFormData = execution.getEngineServices()
                		    .getFormService()
                		    .getTaskFormData(task.getId());
                		    //.getTaskFormData(execution.getId());
                	
                	oLog.info("[replacePatterns]:Found taskformData=" + oTaskFormData);
                	String sExpression = null;
                	if (oTaskFormData != null){
	                	for (FormProperty property : oTaskFormData.getFormProperties()) {
	                		//if (property.getId().equals("sBody") ){
                                        String sFieldID=property.getId();
                                        
                                        oLog.info("[replacePatterns]:sFieldID=" + sFieldID);
	                		if (sFieldID!=null && sFieldID.startsWith("sBody") ){
	                			oLog.info("[replacePatterns]:Found necessary property.getValue()=" + property.getValue());
	                			//sExpression = property.getValue();
	                			oLog.info("[replacePatterns]:Found necessary property.getName()=" + property.getName());
                                                //if( sExpression==null || "".equals(sExpression.trim()) || "${sBody}".equals(sExpression.trim())){
                                                    sExpression = property.getName();
                                                //}
                                                    
                                                    oLog.info("[replacePatterns]:sExpression="+sExpression);
                                                    if(sExpression!=null){
                                                        String[] asPatterns = {
                                                                "pattern/print/subsidy.html"
                                                                ,"pattern/print/subsidy_zayava.html"
                                                                ,"pattern/print/subsidy_declaration.html"
                                                                ,"pattern/print/1.html"
                                                                ,"pattern/print/2.html"
                                                                ,"pattern/print/3.html"
                                                                ,"pattern/print/4.html"
                                                                ,"pattern/print/5.html"
                                                        };
                                                        for(String sName:asPatterns){
                                                            if(sExpression.contains("["+sName+"]")){
                                                                oLog.info("[replacePatterns]:sName="+sName);
                                                                //String sFullPath = sName.replaceAll("\\.", "/").replaceLast(sName, sName);
                                                                File oFile = new File(sName);//"pattern/print/subsidy.html"
                                                                oLog.info("[replacePatterns]:oFile.exists()="+oFile.exists());
                                                                if(!oFile.exists()){
                                                                    //oFile = new File("class/"+sName);
                                                                    // /sybase/tomcat_wf-region/bin/class/pattern/print/subsidy.html                                    
                                                                    oFile = new File("../webapps/wf-region/WEB-INF/classes/"+sName);
                                                                }
                                                                oLog.info("[replacePatterns]:oFile.exists()="+oFile.exists());
                                                                if(oFile.exists()){
                                                                    //String sData = getFromFile(oFile, "Cp1251");
                                                                    String sData = getFromFile(oFile, null);
                                                                    oLog.info("[replacePatterns]:sData="+sData);
                                                                    if(sData!=null){
                                                                        sExpression=sExpression.replaceAll("\\Q["+sName+"]\\E", sData);
                                                                        oLog.info("[replacePatterns]:sExpression="+sExpression);
                                                                        //setStringFromFieldExpression(osBody, execution, sExpression);
                                //                                        execution.setVariable("sBody", sExpression);
                                //                                        execution.setVariable("sBody0", sExpression);
                                //                                        task.setVariable("sBody", sExpression);
                                //                                        task.setVariable("sBody0", sExpression);
                                                                        oLog.info("[replacePatterns](sFieldID="+sFieldID+"):1-Ok!");
                                                                        
                                                                        
                                                                        execution.getEngineServices().getRuntimeService()
                                                                                .setVariable(task.getProcessInstanceId(), sFieldID, sExpression);
                                                                                //.setVariable(task.getProcessInstanceId(), "sBody", sExpression);
                                                                        //task.getName();
                                                                        oLog.info("[replacePatterns](sFieldID="+sFieldID+"):2-Ok:"+execution.getEngineServices().getRuntimeService()
                                                                                .getVariable(task.getProcessInstanceId(), sFieldID));
                                                                                //.getVariable(task.getProcessInstanceId(), "sBody"));
                                                                        //task.getId()

                                                                        oLog.info("[replacePatterns](sFieldID="+sFieldID+"):3-Ok!");
                                                                    }
                                                                }else{
                                                                    oLog.info("[replacePatterns]:oFile.getAbsolutePath()="+oFile.getAbsolutePath());
                                                                    oLog.info("[replacePatterns]:oFile.getCanonicalPath()="+oFile.getCanonicalPath());
                                                                    oLog.info("[replacePatterns]:oFile.getPath()="+oFile.getPath());
                                                                    oLog.info("[replacePatterns]:oFile.getName()="+oFile.getName());
                                                                }
                                                            }
                                                        }
                                                    }                                                    
                                                    
	                		} else {
	                			oLog.info("[replacePatterns]:Property =" + property.getId());
	                		}
	                	}
                	}
                	
                    //String sExpression = (String)execution.getVariable("sBody");
                    oLog.info("[replacePatterns]:sExpression="+sExpression);
                    /*if(sExpression!=null){
                        sExpression = (String)execution.get getVariable("sBody");
                        oLog.info("[replacePatterns]:sExpression="+sExpression);
                    }*/
                    
                    

                }catch(Exception oException){
                    oLog.error("[replacePatterns]",oException);
                }            
        }
        
        public static String getFromFile(File file, String sCodepage) throws IOException {
            byte[] aByte = getBytesFromFile(file);
            //return Util.sData(aByte);
            //java.lang.
            if(aByte==null){
                return null;
            }
            return new String(aByte, sCodepage == null ? "UTF-8" : sCodepage);
            //Charset.forName("UTF-8")
            //Cp1251
            
        }
        public static byte[] getBytesFromFile(File file) throws IOException {

            InputStream is = new FileInputStream(file);

            // �������� ������ �����
            long length = file.length();

            // ������� ������ ��� �������� ������
            byte[] bytes = new byte[(int)length];

            // ���������
            int offset = 0;

            int numRead = 0;

            while (offset < bytes.length
                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {

                offset += numRead;
            }

            // ���������, ��� �� ���������
            if (offset < bytes.length) {
                throw new IOException("Could not completely read file "+file.getName());
            }

            // ��������� � ����������
            is.close();

            return bytes;
        }        
        
	public static String setStringFromFieldExpression(Expression expression,
			DelegateExecution execution, Object value) {
		if (expression != null && value!=null) {
			expression.setValue(value, execution);
		}
		return null;
	}        

    /**1) Сервис назвать getPatternFile
Параметры:
sSingleFolder - строковое название папки
sFullName - строковое название файла
sContentType - строковой тип контента
Возвращать контент указанного файла.
(у этого cthdbcf должна быть и простая возможность вызова как обычного метода из явы)
2) Папки смотреть начиная с корня классов (т.е. "\i\wf-region\src\main\resources\patterns",
и убедиться, что в продеплоеном виде берется именно этот каталог в качестве исходного
3) создать в проекте соответствующую папку "patterns" по пути "\i\wf-region\src\main\resources\patterns"
4) Не допускать, чтоб в параметре "sSingleFolder" и sFullName встречались слеши или обратные слеши, которые позволят сослаться на папку, уровнем выше.
5) задавая в хеадере респонса тот контенттайп, что указан в "sContentType" - отдавать контент файла.
5) описать в доке с АПИ*/

    public static String getPatternFile(String sPathFile, String sContentType)
            throws IOException {
//        System.out.println("begin test");//src/main/resources/pattern/print/subsidy_zayava.html
     ///temp
//   if (sPathFile .contains("..")/* || sPathFile.charAt(0) == '/' || sPathFile.charAt(0) == '\\'*/){
//            throw new IllegalArgumentException("incorrect sPathFile!");
//        }
//        //get File
        String fullFileName = //FILE_PATH_BEGIN "../webapps/wf-region/WEB-INF/classes/pattern/" +
                sPathFile;
        File file = new File(fullFileName);
//        //FileInputStream fis = new FileInputStream(file);
//
//        //System.out.println("Total file size to read (in bytes) : "+ fis.available());

//        int content;
//        while ((content = fis.read()) != -1) {
//            // convert to char and display it
//            System.out.print((char) content);
//        }
        String content = Files.toString(file, Charsets.UTF_8);
        return content;

    }

    public static void main(String[] args) {
        try {
            String s = getPatternFile("report","report1.html");//"/";
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
