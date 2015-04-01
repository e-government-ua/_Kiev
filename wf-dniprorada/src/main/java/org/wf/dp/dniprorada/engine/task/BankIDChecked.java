package org.wf.dp.dniprorada.engine.task;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Check client data throw bankID system.</br>
 * 
 * Request: https://bankid.privatbank.ua/DataAccessService
 * /checked/fio?access_token=095be9eb
 * -01e7-4045-b60b-9d71581fb4d9&client_id=d9c8b4d6-6a18-4492-a50a-2b3b4ea6285b
 * 
 * Response: <?xml version="1.0" encoding="UTF-8" standalone="yes"?><message
 * state="ok"><fio><firstName>имя</firstName><lastName>фамилия</lastName><
 * middleName>отчество</middleName></fio></message>
 * 
 * 
 * @author Tereshchenko
 * 
 */
@Component("bankIDChecked")
public class BankIDChecked implements JavaDelegate {

	@Value("${checkBankIDUrl}")
	private String checkBankIDUrl;
	@Value("${systemClient}")
	private String systemClient;

	@Value("${firstNameElementName}")
	private static final String FIRST_NAME = "firstName";
	@Value("${lastNameElementName}")
	private static final String LAST_NAME = "lastName";
	@Value("${middleNameElementName}")
	private static final String MIDDLE_NAME = "middleName";
	@Value("${messageElementName}")
	private static final String MESSAGE = "message";
	@Value("${stateElementName}")
	private static final String STATE = "state";
	@Value("${access_tokenPathParam}")
	private static final String ACCESS_TOKEN = "access_token";
	@Value("${client_idPathParam}")
	private static final String CLIENT_ID = "client_id";

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String token = execution.getVariable(ACCESS_TOKEN, String.class);

		HttpsURLConnection con;
		try {

			con = getConnection(String.format(checkBankIDUrl, token,
					systemClient));

			// dump all the content
			getData(con, execution);

		} catch (MalformedURLException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}

	}

	private static HttpsURLConnection getConnection(String httpsUrl)
			throws MalformedURLException, IOException,
			NoSuchAlgorithmException, KeyManagementException {
		URL url;
		HttpsURLConnection con;
		url = new URL(httpsUrl);
		// Create a trust manager that does not validate certificate chains
		final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {

			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {

			}
		} };

		// Install the all-trusting trust manager
		final SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
		// Create an ssl socket factory with our all-trusting manager
		final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
		HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);

		con = (HttpsURLConnection) url.openConnection();
		// Tell the url connection object to use our socket factory which
		// bypasses security checks
		((HttpsURLConnection) con).setSSLSocketFactory(sslSocketFactory);

		return con;
	}

	private static void getData(HttpsURLConnection con,
			DelegateExecution execution) throws XMLStreamException, IOException {
		if (con != null) {
			try {
				readFio(con.getInputStream(), execution);
			} catch (Exception e) {
				execution.setVariable("error_message", "invalid token");
				// throw new BpmnError("invalid token");
				throw new IllegalArgumentException("invalid token", e);
			}

		} else {
			// throw new BpmnError("connection unavailable");
			throw new IllegalArgumentException("connection unavailable");
		}
	}

	private static void readFio(InputStream is, DelegateExecution execution)
			throws XMLStreamException {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEventReader eventReader = inputFactory.createXMLEventReader(is);

		boolean isEmptyFio = true;
		while (eventReader.hasNext()) {
			XMLEvent event = eventReader.nextEvent();

			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();

				if (MESSAGE.equals(startElement.getName().getLocalPart())) {

					@SuppressWarnings("unchecked")
					Iterator<Attribute> attributes = startElement
							.getAttributes();
					boolean stateAttrFound = false;
					while (attributes.hasNext()) {
						Attribute attribute = attributes.next();
						if (STATE.equals(attribute.getName().toString())) {
							if (!"ok".equals(attribute.getValue())) {

								throw new BpmnError("wrong response state : "
										+ attribute.getValue());
							}
							stateAttrFound = true;
						}

					}
					if (!stateAttrFound) {
						throw new BpmnError(
								"message did not contains [state] attribute  ");
					}

				}

				if (event.isStartElement()) {
					if (FIRST_NAME.equals(event.asStartElement().getName()
							.getLocalPart())) {
						event = eventReader.nextEvent();
						execution.setVariable(FIRST_NAME, event.toString());
						isEmptyFio = false;
						continue;
					}
				}
				if (LAST_NAME.equals(event.asStartElement().getName()
						.getLocalPart())) {
					event = eventReader.nextEvent();
					execution.setVariable(LAST_NAME, event.toString());
					isEmptyFio = false;
					continue;
				}

				if (MIDDLE_NAME.equals(event.asStartElement().getName()
						.getLocalPart())) {
					event = eventReader.nextEvent();
					execution.setVariable(MIDDLE_NAME, event.toString());
					isEmptyFio = false;
					continue;
				}

			}

		}
		if (isEmptyFio) {
			// throw new BpmnError("fio not found");
			throw new IllegalArgumentException("fio not found");

		}
	}

}
