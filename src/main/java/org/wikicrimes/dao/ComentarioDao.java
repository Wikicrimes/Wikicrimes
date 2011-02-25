package org.wikicrimes.dao;

import java.util.List;

import org.wikicrimes.model.Comentario;

public interface ComentarioDao extends GenericCrudDao {

	public List<Comentario> getComentariosByCrime(String idCrime);
	public void salvaComentario(Comentario comentario);
}
