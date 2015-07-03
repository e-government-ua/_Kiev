package org.activiti.rest.controller.adapter;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class MultiReaderHttpServletResponse extends HttpServletResponseWrapper {

	protected CharArrayWriter charWriter;

	protected PrintWriter writer;

	protected boolean getOutputStreamCalled;

	protected boolean getWriterCalled;

	public MultiReaderHttpServletResponse(HttpServletResponse response) {
		super(response);
		charWriter = new CharArrayWriter();
	}

	public ServletOutputStream getOutputStream() throws IOException {
		if (getWriterCalled) {
			throw new IllegalStateException("getWriter already called");
		}

		getOutputStreamCalled = true;
		return super.getOutputStream();
	}

	public PrintWriter getWriter() throws IOException {
		if (writer != null) {
			return writer;
		}
		if (getOutputStreamCalled) {
			throw new IllegalStateException("getOutputStream already called");
		}
		getWriterCalled = true;
		writer = new PrintWriter(charWriter);
		return writer;
	}

	@Override
	public String toString() {
		String result = null;
        System.out.println("DDDDDDDDDDDDDDDDD!!!!!!!!!!!!!!!!!!!!!!!!!!!!toString");
		if (writer != null) {
			result = charWriter.toString();
		}
		return result;
	}
}