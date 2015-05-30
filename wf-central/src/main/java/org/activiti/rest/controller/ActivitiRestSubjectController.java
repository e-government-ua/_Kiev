package org.activiti.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wf.dp.dniprorada.dao.SubjectDao;
import org.wf.dp.dniprorada.dao.SubjectExtractor;
import org.wf.dp.dniprorada.model.Subject;

@Controller
@RequestMapping(value = "/subject")
public class ActivitiRestSubjectController {

        //private final Logger log = LoggerFactory.getLogger(ActivitiRestSubjectController.class);
        private final Logger log = LoggerFactory.getLogger(SubjectExtractor.class);
  
	@Autowired
	@Qualifier(value = "subjectDao")
	private SubjectDao subjectDao;

	@RequestMapping(value = "/syncSubject", method = RequestMethod.GET, headers = {"Accept=application/json"})
	public @ResponseBody
	Subject getSubject(@RequestParam(value = "sINN") String inn) {
		Subject subj = null;
		try {
                        //log.error("inn(0):"+inn);
			subj = subjectDao.getSubject(inn);
                        //log.error("inn(2):"+inn);
                        /*if(subj==null){
                            throw new Exception("Subject not found: inn="+inn);
                        }*/
		} catch (Exception ex) {
                        //System.err.println(ex.getMessage());
                        log.error("syncSubject_sID_Error(1):"+ex.toString());
                        log.error("syncSubject_sID_Error(2):"+ex.getMessage());
                        if(subj==null){
                            try {
                                    subj = subjectDao.insertSubject(inn);
                            } catch (Exception e) {
                                    throw e;
                            }
                        }
		}
		return subj;
	}
	
	@RequestMapping(value="/setSubject", method = RequestMethod.POST, headers = {"Accept=application/json"})
	public @ResponseBody Subject setSubject(@RequestBody Subject subject){
		return subjectDao.updateSubject(subject);
	}
}