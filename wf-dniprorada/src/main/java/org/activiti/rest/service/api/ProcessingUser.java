package org.activiti.rest.service.api;

import org.activiti.engine.ProcessEngines;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ProcessingUser {
	private static final Logger log = Logger.getLogger(ProcessingUser.class);

	public boolean validateUser(String user, String password) {
		try {
			if (checkParameters(user, password)) {
				if (ProcessEngines.getDefaultProcessEngine()
						.getIdentityService().checkPassword(user, password)) {
					return true;
				} else {
					log.info(user+":"+" введены неверные данные");
					return false;
				}
			} else {
				log.info(user+":"+" логин или пароль не введены");
				return false;
			}
		} catch (Exception ex) {
			log.error(ex.getMessage());
			return false;
		}
	}

	private boolean checkParameters(String user, String password) {
		if (user == null || password == null) {
			return false;
		} else if (user.isEmpty() || password.isEmpty()) {
			return false;
		}
		return true;
	}
}
