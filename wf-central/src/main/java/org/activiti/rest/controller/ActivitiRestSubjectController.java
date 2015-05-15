package org.activiti.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wf.dp.dniprorada.dao.SubjectDao;
import org.wf.dp.dniprorada.model.Subject;

@Controller
@RequestMapping(value = "/subject")
public class ActivitiRestSubjectController {

	@Autowired
	@Qualifier(value = "subjectDao")
	private SubjectDao subjectDao;

	@RequestMapping(value = "/getSubject", method = RequestMethod.GET)
	public @ResponseBody
	Subject getSubject(@RequestParam(value = "sINN") String inn) {
		Subject subj = null;
		try {
			subj = subjectDao.getSubject(inn);
		} catch (Exception ex) {
			try {
				subj = subjectDao.insertSubject(inn);
			} catch (Exception e) {
				throw e;
			}
		}
		return subj;
	}
}