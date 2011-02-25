package org.wikicrimes.service;

import java.util.List;

import org.wikicrimes.model.Comentario;


public interface ComentarioService extends GenericCrudService {

	
	public List<Comentario> getComentariosByCrime(String idCrime);
	public void salvaComentario(Comentario comentario);

}
