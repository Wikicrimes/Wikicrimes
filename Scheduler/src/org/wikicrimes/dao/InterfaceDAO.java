package org.wikicrimes.dao;

import java.sql.SQLException;
import java.util.ArrayList;

import org.wikicrimes.model.Usuario;

public interface InterfaceDAO 
{	
	public void abrirConexao()  throws SQLException;
	
	public void fecharConexao()  throws SQLException;
	
	public ArrayList<Usuario> getUsuarios(Long periodoArea)  throws SQLException;
	
	public void alterarControle(long periodo, boolean bool)  throws SQLException;
	
	public boolean getControle(long periodo) throws SQLException;
	
	public void cadastrarEmailEnviado(Usuario usuario, long periodo) throws SQLException;
	
	public void removerEmailEnviado(long periodo) throws SQLException;
	
	public ArrayList<Usuario> getUsuariosExcep(Long periodo) throws SQLException;
}
