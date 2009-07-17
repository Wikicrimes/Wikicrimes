package org.wikicrimes.servlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Classe de conex�o - uso do padr�o Singleton
public class ConexaoBD {

	// Conex�o
	private static ConexaoBD conexaoBD;
	private Connection conexao;

	private ConexaoBD() throws SQLException, ClassNotFoundException {

		/*
		 * ODBC Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); conexao =
		 * DriverManager.getConnection("jdbc:odbc:" + aAliase, "root","");
		 */

		try {
			// Carregando o JDBC Driver
			String driverName = "org.gjt.mm.mysql.Driver"; // MySQL MM JDBC
			// driver
			Class.forName(driverName);

			// Criando a conex�o com o Banco de Dados
			// String serverName = "192.168.0.5";
			String serverName = "192.168.0.254";
			String mydatabase = "wikicrimes";
			String url = "jdbc:mysql://" + serverName + "/" + mydatabase; // a
			// JDBC
			// url
			String username = "root";
			String password = "root";
			conexao = DriverManager.getConnection(url, username, password);

		} catch (ClassNotFoundException e) {
			// Driver n�o encontrado
			System.out.println("O driver expecificado n�o foi encontrado.");
		} catch (SQLException e) {
			// N�o est� conseguindo se conectar ao banco
			System.out.println("N�o foi poss�vel conectar ao Banco de Dados");
		}
	}

	public static ConexaoBD getConexaoBD() {
		if (conexaoBD == null)
			try {
				conexaoBD = new ConexaoBD();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return conexaoBD;
	}
	

	public ResultSet enviarConsulta(String aComandoSQL) throws SQLException {
		return conexao.createStatement().executeQuery(aComandoSQL);
	}

	public int enviarComando(String aComandoSQL) throws SQLException {
		return conexao.createStatement().executeUpdate(aComandoSQL);
	}

	public int[] enviarComandos(String[] aComandosSQL) throws SQLException {
		Statement st = conexao.createStatement();
		st.clearBatch();
		for (int i = 0; i < aComandosSQL.length; i++) {
			st.addBatch(aComandosSQL[i]);
		}
		return st.executeBatch();
	}

	public void fechaConexao() {
		try {
			conexao.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

}