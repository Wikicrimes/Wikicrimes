package org.wikicrimes.view.validators;

import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class RazaoCrimesValidator implements Validator {
	
	//falta fazer a validacao do conjunto de checkbox!!!
	
	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object arg2)
			throws ValidatorException {
		
		List<String> listaSelecionados = (List<String>)arg2;
		
		if(listaSelecionados.size()>4){
			FacesContext fc = FacesContext.getCurrentInstance();
	        ResourceBundle rb = fc.getApplication().getResourceBundle(fc, "msg");

	        FacesMessage msn = new FacesMessage(rb.getString("crime.limite.razao"));
	        throw new ValidatorException(msn);
		}

	}

}
