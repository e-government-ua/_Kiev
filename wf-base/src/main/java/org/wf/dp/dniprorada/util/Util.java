package org.wf.dp.dniprorada.util;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;

import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.wf.dp.dniprorada.base.model.AbstractModelTask.getStringFromFieldExpression;

public final class Util {

	private final static Logger log = LoggerFactory.getLogger(Util.class);

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
                            
                	TaskFormData taskformData = execution.getEngineServices()
                		    .getFormService()
                		    .getTaskFormData(task.getId());
                		    //.getTaskFormData(execution.getId());
                	
                	oLog.info("[replacePatterns]:Found taskformData=" + taskformData);
                	String sExpression = null;
                	if (taskformData != null){
	                	for (FormProperty property : taskformData.getFormProperties()) {
	                		if (property.getId().equals("sBody") ){
	                			oLog.info("[replacePatterns]:Found necessary property.getValue()=" + property.getValue());
	                			//sExpression = property.getValue();
	                			oLog.info("[replacePatterns]:Found necessary property.getName()=" + property.getName());
                                                //if( sExpression==null || "".equals(sExpression.trim()) || "${sBody}".equals(sExpression.trim())){
                                                    sExpression = property.getName();
                                                //}
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
                    
                    
                    if(sExpression!=null){
                        String[] asPatterns = {
                                "pattern/print/subsidy.html"
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
                                        oLog.info("[replacePatterns]:1-Ok!");
                                        
                                        execution.getEngineServices().getRuntimeService()
                                                .setVariable(task.getProcessInstanceId(), "sBody", sExpression);
                                        //task.getName();
                                        oLog.info("[replacePatterns]:2-Ok:"+execution.getEngineServices().getRuntimeService()
                                                .getVariable(task.getProcessInstanceId(), "sBody"));
                                        //task.getId()
                                        
                                        oLog.info("[replacePatterns]:3-Ok!");
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
        
        
}
