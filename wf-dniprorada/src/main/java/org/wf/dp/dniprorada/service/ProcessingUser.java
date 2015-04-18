package org.wf.dp.dniprorada.service;

import org.activiti.engine.ProcessEngines;

public class ProcessingUser {
	public boolean validateUser(String user, String password){ 
		
        if(ProcessEngines.getDefaultProcessEngine().getIdentityService().checkPassword(user, password)){
            return true;
        } else {
            return false;
        }
    }
}
