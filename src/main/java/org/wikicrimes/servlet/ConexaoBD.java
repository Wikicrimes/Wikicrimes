package org.wikicrimes.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;



// Classe de conex�o - uso do padr�o Singleton
public class ConexaoBD {

	// Conex�o
	private static ConexaoBD conexaoBD;
	private static Connection conexao;
	
	

	private ConexaoBD() throws SQLException, ClassNotFoundException {
		Properties props=null;
		/*Leitura do arquivo properties*/  
		try{
			 URL urlArquivo = getClass().getClassLoader().getResource("/");    
		         
			FileInputStream fis = new FileInputStream(urlArquivo.getPath()+"jdbc.properties.wikimapps");  
			props = new Properties();
		    props.load(fis);  
		    fis.close();  
		}catch(IOException e){  
		    e.printStackTrace();  
		}  
		
		/*
		 * ODBC Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); conexao =
		 * DriverManager.getConnection("jdbc:odbc:" + aAliase, "root","");
		 */

		try {
			
			// Carregando o JDBC Driver
			String driverName = props.getProperty("jdbc.driverClassName");//"org.gjt.mm.mysql.Driver"; // MySQL MM JDBC
			
			// driver
			Class.forName(driverName);
			 

			// Criando a conex�o com o Banco de Dados
			/*String serverName = rb.getString("serverName");
			String mydatabase = "wikimapps";*/
			String url = props.getProperty("jdbc.url");//"jdbc:mysql://" + serverName + "/" + mydatabase; // a
			// JDBC
			// url
			String username = props.getProperty("jdbc.username");			
			String password = props.getProperty("jdbc.password");
			
			conexao = DriverManager.getConnection(url, username, password);

		} catch (ClassNotFoundException e) {
			// Driver n�o encontrado
			System.out.println("O driver expecificado n�o foi encontrado.");
			e.printStackTrace();
		} catch (SQLException e) {
			// N�o est� conseguindo se conectar ao banco
			System.err.println("N�o foi poss�vel conectar ao Banco de Dados");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public static ConexaoBD getConexaoBD() {
		if (conexao == null)
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

	public static void fechaConexao() {
		try {
			conexao.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conexao = null;
	}

	

}