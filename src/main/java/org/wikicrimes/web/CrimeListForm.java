package org.wikicrimes.web;

import java.util.ArrayList;
import java.util.List;

import org.wikicrimes.model.Usuario;
import org.wikicrimes.service.CrimeService;

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

