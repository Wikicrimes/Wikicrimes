package org.wikicrimes.web;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;

import org.wikicrimes.model.Usuario;
import org.wikicrimes.model.Crime;
import org.wikicrimes.service.CrimeService;
import org.wikicrimes.web.BeanDateComparator;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class CrimeListForm extends GenericForm {
    private CrimeService crimeService;
    private String sortColumn = "data";
    private boolean ascending = true;
    private List crimes = new ArrayList();
    
    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public void setCrimeService(CrimeService crimeService) {
        this.crimeService = crimeService;
    }
    
    public List getCrimes() {
        //List crimes = crimeService.getAll();// userManager.getUsers();
    	Usuario usuario = (Usuario) this.getSessionScope().get("usuario");
        
    	
        if(usuario !=null) {
        	crimes = crimeService.getByUser(usuario.getIdUsuario());
        } 
        
        
        
        return crimes;
    }
}

