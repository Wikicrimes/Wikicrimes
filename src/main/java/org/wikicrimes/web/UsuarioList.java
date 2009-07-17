package org.wikicrimes.web;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.wikicrimes.service.UsuarioService;


public class UsuarioList implements Serializable {
    private UsuarioService service;
    private String sortColumn = "id";
    private boolean ascending = true;

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

    public void setUsuarioService(UsuarioService service) {
        this.service = service;
    }
    
    public List getUsuarios() {
        List usuarios = service.getAll();

        Comparator comparator = null;

        if (sortColumn.equalsIgnoreCase("birthday")) {
            comparator = new BeanDateComparator(sortColumn);
        } else {
            comparator = new BeanComparator(sortColumn);
        }

        if (!ascending) {
            comparator = new ReverseComparator(comparator);
        }
        
        Collections.sort(usuarios, comparator);

        return usuarios;
    }
}
