package org.wf.dp.dniprorada.engine.task;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;
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
		String clid = execution.getVariable(CLIENT_ID, String.class);

		HttpsURLConnection con;
		try {

			con = getConnection(String.format(checkBankIDUrl, token, clid));

			// dump all the content
			getData(con, execution);

		} catch (MalformedURLException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}

	}

	private HttpsURLConnection getConnection(String httpsUrl)
			throws MalformedURLException, IOException {
		URL url;
		HttpsURLConnection con;
		url = new URL(httpsUrl);
		con = (HttpsURLConnection) url.openConnection();
		return con;
	}

	private void getData(HttpsURLConnection con, DelegateExecution execution)
			throws XMLStreamException, IOException {
		if (con != null) {

			readFio(con.getInputStream(), execution);

		} else {
			throw new BpmnError("connection unavailable");
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
					if (stateAttrFound) {
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
			throw new BpmnError("fio not found");
		}
	}
}
