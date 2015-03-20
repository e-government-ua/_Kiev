package org.wf.dp.dniprorada.engine.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Check client data throw bankID system.</br>
 * https://bankid.privatbank.ua/DataAccessService
 * /checked/fio?access_token=caef896e-3d84-4c16-8a2b-f4f2264db6b1
 * 
 * @author Tereshchenko
 *
 */
@Component("bankIDChecked")
public class BankIDChecked implements JavaDelegate {

	@Autowired
	private ObjectMapper objectMapper;
	// TODO pull to variables (can change dynamicaly)
	private final String bankIdUrl = "https://bankid.privatbank.ua/DataAccessService/checked/fio?access_token=";

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String token = execution.getVariable("access_token", String.class);
		HttpsURLConnection con;
		try {

			con = getConnection(bankIdUrl + token);

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
			throws IOException {
		if (con != null) {

			try {

				BufferedReader br = new BufferedReader(new InputStreamReader(
						con.getInputStream()));

				JsonNode jNode = objectMapper.readTree(br);
				// TODO pull variables names to constants
				// TODO get description for variables
				execution.setVariable("firstName", jNode.findValue("firstName")
						.asText());
				execution.setVariable("lastName", jNode.findValue("lastName")
						.asText());
				execution.setVariable("secondName",
						jNode.findValue("secondName").asText());
				execution.setVariable("phone", jNode.findValue("phone")
						.asText());
				execution.setVariable("adress", jNode.findValue("adress")
						.asText());

			} catch (IOException e) {
				throw e;
			} finally {

				br.close();
			}

		}
	}
}
