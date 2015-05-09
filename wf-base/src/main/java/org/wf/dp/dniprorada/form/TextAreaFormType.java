package org.wf.dp.dniprorada.form;

import org.activiti.engine.form.AbstractFormType;

public class TextAreaFormType  extends AbstractFormType {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String FORM_NAME="textArea";
	
	 @Override
	public String getName() {
		return FORM_NAME;
	}
	
	@Override
	public Object convertFormValueToModelValue(String propertyValue) {		
			return propertyValue;		
	}
	
	
	 @Override
	  public String convertModelValueToFormValue(Object modelValue) {	
	    return (String) modelValue;
	    
	  }
	 
}
