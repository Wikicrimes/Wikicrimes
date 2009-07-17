package org.wikicrimes.service.impl;

import java.util.List;

import org.wikicrimes.dao.ComentarioDao;
import org.wikicrimes.model.Comentario;
import org.wikicrimes.service.ComentarioService;

public class ComentarioServiceImpl extends GenericCrudServiceImpl implements
		ComentarioService {

	private ComentarioDao comentarioDao;
	
	public ComentarioDao getComentarioDao() {
		return comentarioDao;
	}

	public void setComentarioDao(ComentarioDao comentarioDao) {
		this.comentarioDao = comentarioDao;
	}
	
	public List<Comentario> getComentariosByCrime(Long idCrime) {
		return this.comentarioDao.getComentariosByCrime(idCrime);
		
	}

	public void salvaComentario(Comentario comentario) {
		this.comentarioDao.salvaComentario(comentario);
		
	}

}
