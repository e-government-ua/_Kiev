/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.activiti.rest.controller.adapter;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * @author olya
 */
public class MultiReadHttpServletRequest extends HttpServletRequestWrapper {

    private static final Logger logger = LoggerFactory
            .getLogger(MultiReadHttpServletRequest.class);
    private static volatile String errorText_NotSupported = "mark/reset/isFinished/isReady/setReadListener not supported";
    private byte[] body;

    public MultiReadHttpServletRequest(HttpServletRequest httpServletRequest) throws IOException {
        super(httpServletRequest);
        // Read the request body and save it as a byte array
        InputStream is = super.getInputStream();
        body = IOUtils.toByteArray(is);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStreamImpl(new ByteArrayInputStream(body));
    }

    @Override
    public BufferedReader getReader() throws IOException {
        String enc = getCharacterEncoding();
        if (enc == null) {
            enc = "UTF-8";
        }
        return new BufferedReader(new InputStreamReader(getInputStream(), enc));
    }

    private class ServletInputStreamImpl extends ServletInputStream {

        private InputStream is;

        public ServletInputStreamImpl(InputStream is) {
            this.is = is;
        }

        public int read() throws IOException {
            return is.read();
        }

        public boolean markSupported() {
            return false;
        }

        public synchronized void mark(int i) {
            throw new UnsupportedOperationException(new IOException(errorText_NotSupported));
        }

        public synchronized void reset() throws IOException {
            throw new UnsupportedOperationException(errorText_NotSupported);
        }

        @Override
        public boolean isFinished() {
            throw new UnsupportedOperationException(errorText_NotSupported);
        }

        @Override
        public boolean isReady() {
            throw new UnsupportedOperationException(errorText_NotSupported);
        }

        @Override
        public void setReadListener(ReadListener rl) {
            throw new UnsupportedOperationException(errorText_NotSupported);
        }
    }
}
