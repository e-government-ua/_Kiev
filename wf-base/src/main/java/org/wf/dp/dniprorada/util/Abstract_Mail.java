package org.wf.dp.dniprorada.util;

import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import org.activiti.engine.TaskService;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Belyavtsev Vladimir Vladimirovich (BW)
 */
public abstract class Abstract_Mail {
    
    
    @Value("${mailServerHost}")
    public String mailServerHost;

    @Value("${mailServerPort}")
    public String mailServerPort;

    @Value("${mailServerDefaultFrom}")
    public String mailServerDefaultFrom;

    @Value("${mailServerUsername}")
    public String mailServerUsername;

    @Value("${mailServerPassword}")
    public String mailServerPassword;

    @Value("${mailAddressNoreply}")
    public String mailAddressNoreplay;    
    
    
    //private final static Logger log = LoggerFactory.getLogger(Abstract_Mail.class);

    
    public Abstract_Mail(){}
    
    private String sFrom = mailServerDefaultFrom; //"noreplay@gmail.com";
    private String saTo = mailAddressNoreplay; //"noreplay@gmail.com";
    private String sHead = "Subject";
    private String sBody = "Text";
    private String sAuthUser = mailServerUsername; //"user";
    private String sAuthPassword = mailServerPassword; //"password";
    private String sHost = mailServerHost; //"gmail.com";
    private Integer nPort = Integer.valueOf(mailServerPort); //465;
    private boolean bSSL = true;
    private boolean bTLS = true;
    
    
    

    public String getFrom() {
        return sFrom;
    }

    public Abstract_Mail _From(String sFrom) {
        this.sFrom = sFrom;
        return this;
    }

    public String getTo() {
        return saTo;
    }

    public Abstract_Mail _To(String saTo) {
        this.saTo = saTo;
        return this;
    }

    public String getHead() {
        return sHead;
    }

    public Abstract_Mail _Head(String sHead) {
        this.sHead = sHead;
        return this;
    }

    public String getBody() {
        return sBody;
    }

    public Abstract_Mail _Body(String sBody) {
        this.sBody = sBody;
        return this;
    }

    public String getAuthUser() {
        return sAuthUser;
    }

    public Abstract_Mail _AuthUser(String sAuthUser) {
        this.sAuthUser = sAuthUser;
        return this;
    }

    public String getAuthPassword() {
        return sAuthPassword;
    }

    public Abstract_Mail _AuthPassword(String sAuthPassword) {
        this.sAuthPassword = sAuthPassword;
        return this;
    }

    public String getHost() {
        return sHost;
    }

    public Abstract_Mail _Host(String sHost) {
        this.sHost = sHost;
        return this;
    }

    public Integer getPort() {
        return nPort;
    }

    public Abstract_Mail _Port(Integer nPort) {
        this.nPort = nPort;
        return this;
    }

    public boolean isSSL() {
        return bSSL;
    }

    public Abstract_Mail _SSL(boolean bSSL) {
        this.bSSL = bSSL;
        return this;
    }

    public boolean isTLS() {
        return bTLS;
    }

    public Abstract_Mail _TLS(boolean bTLS) {
        this.bTLS = bTLS;
        return this;
    }
    

            
    abstract public void init() throws Exception;
    
    abstract public void send() throws Exception;
    
}
