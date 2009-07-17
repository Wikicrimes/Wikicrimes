package org.wikicrimes.model;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

/**
 * Define uma cidade de umestado da federacao.
 *
 * @author Fábio Barros
 */
@MappedSuperclass
public class BaseObject implements Serializable {
    private static final long serialVersionUID = 3256446889040622647L;

   /* public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.MULTI_LINE_STYLE);
    }*/
}
