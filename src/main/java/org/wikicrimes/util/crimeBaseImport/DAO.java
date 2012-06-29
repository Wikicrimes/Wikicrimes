package org.wikicrimes.util.crimeBaseImport;

import static org.wikicrimes.util.crimeBaseImport.IgnoredCrimeException.Reason.PREVIOUSLY_IMPORTED;
import static org.wikicrimes.util.crimeBaseImport.Model.FIXED_COMENTARIOS;
import static org.wikicrimes.util.crimeBaseImport.Model.FIXED_CONF_NEGATIVAS;
import static org.wikicrimes.util.crimeBaseImport.Model.FIXED_CONF_POSITIVAS;
import static org.wikicrimes.util.crimeBaseImport.Model.FIXED_CREDIBILIDADE;
import static org.wikicrimes.util.crimeBaseImport.Model.FIXED_IP;
import static org.wikicrimes.util.crimeBaseImport.Model.FIXED_MSG_CONFIRMACAO;
import static org.wikicrimes.util.crimeBaseImport.Model.FIXED_RAZAO;
import static org.wikicrimes.util.crimeBaseImport.Model.FIXED_STATUS;
import static org.wikicrimes.util.crimeBaseImport.Model.FIXED_USUARIO;
import static org.wikicrimes.util.crimeBaseImport.Model.FIXED_VIEW;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.Properties;

import org.wikicrimes.util.kernelmap.PropertiesLoader;

public class DAO {

	private String jdbcDriver, dbUrl, dbUser, dbPass;
	private Connection conn;
	private PreparedStatement stmtCrime, stmtChave, stmtConfi, stmtRazao, stmtCredi;

	public DAO() throws ClassNotFoundException, SQLException, IOException {
		loadJdbcConfig();
		this.conn = openConnection();
		prepareStatements();
	}
	
	private void loadJdbcConfig() throws IOException{
		Properties prop = new Properties();
		prop.load(PropertiesLoader.class.getClassLoader().getResourceAsStream("jdbc.properties"));
		jdbcDriver = prop.getProperty("jdbc.driverClassName");
		dbUrl = prop.getProperty("jdbc.url");
		dbUser = prop.getProperty("jdbc.username");			
		dbPass = prop.getProperty("jdbc.password");
	}
	
	private Connection openConnection() throws ClassNotFoundException, SQLException{
		Class.forName(jdbcDriver);
		Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
		conn.setAutoCommit(false);
		return conn;
	}
	
	private void prepareStatements() throws SQLException{
		
		//insert principal 
		stmtCrime = conn.prepareStatement("INSERT INTO tb_cri_crime " +
						"(TLO_IDTIPO_LOCAL, TCR_IDTIPO_CRIME, TPA_IDTIPO_PAPEL, TTR_IDTIPO_TRANSPORTE,TAU_IDTIPO_ARMA_USADA, TRE_IDTIPO_REGISTRO, USU_IDUSUARIO, CRI_DATA, " +
						"CRI_QUANTIDADE, CRI_FAIXA_ETARIA, CRI_QTD_MASCULINO, CRI_QTD_FEMININO, CRI_SEXO,CRI_DESCRICAO, CRI_LATITUDE, CRI_LONGITUDE, CRI_STATUS, CRI_HORARIO, " +
						"CRI_CONFIRMACOES_POSITIVAS, CRI_CONFIRMACOES_NEGATIVAS, CRI_IP, CRI_DATA_HORA_REGISTRO, TVI_IDTIPO_VITIMA, CRI_ENDERECO, CRI_CIDADE, CRI_ESTADO, " +
						"CRI_PAIS, CRI_VIEW, CRI_CEP, URS_IDUSUARIO_REDE_SOCIAL, CRI_EMBED_NOTICIA, CRI_LINK_NOTICIA, CRI_QTD_COMENTARIOS, CRI_REGISTRADO_PELA_API, " +
						"CRE_CREDIBILIDADE, CRI_CACHE_ESTATISTICAS, CRI_ID_ORIGINAL, CRI_ID_BASE_ORIGEM) " +
						"VALUES (?, ?, ?, ?, ?, ?, " + FIXED_USUARIO + ", ?, ?, ?, ?, ?, ?, ?, ?, ?, " + FIXED_STATUS + ", ?, " + FIXED_CONF_POSITIVAS + ", " + FIXED_CONF_NEGATIVAS + 
						", '" + FIXED_IP + "', ?, ?, ?, ?, ?, ?, " + FIXED_VIEW + ", ?, ?, ?, ?, " + FIXED_COMENTARIOS + ", ?, " + FIXED_CREDIBILIDADE + ", ?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS);
		
		//chave
		stmtChave = conn.prepareStatement("UPDATE tb_cri_crime set CRI_CHAVE = ? where CRI_IDCRIME = ?");
		
		//confirmacao automática
		stmtConfi = conn.prepareStatement("INSERT INTO tb_con_confirmacao (CRI_IDCRIME, CON_DATA_CONFIRMACAO, USU_IDUSUARIO, CON_CONFIRMA, CON_IDICACAO_VALIDA, CON_INDICACAO_EMAIL, " +
				"TIPCON_IDTIPOCONFIRMACAO, CON_IP, CON_MENSAGEM) values (?, ?, " + FIXED_USUARIO + ", 1, 1, 1, 1, '" + FIXED_IP + "', '" + FIXED_MSG_CONFIRMACAO + "')");
		
		//razao
		stmtRazao = conn.prepareStatement("INSERT INTO tb_crz_crime_razao (CRZ_IDCRIME, CRZ_IDRAZAO) values (?, " + FIXED_RAZAO + ")");
		
		//credibilidade
		stmtCredi = conn.prepareStatement("INSERT INTO tb_cre_credibilidade (CRE_CREDIBILIDADE, CRE_PERIODO, CRI_IDCRIME) values (" + FIXED_CREDIBILIDADE + ", ?, ?)");
	}
	
	public void importCrimes(Parser parser) throws SQLException, ParseException, IOException{
		setupOriginalBaseReference(parser);
		/*LOG*/Report report = new Report(parser);
		int importCount = 0;
		String data = parser.next();
		while(data != null){
			try {
				Model crime = parser.convert(data);
				executeUpdates(crime);
				/*LOG*/report.countSuccess(crime);
				importCount++;
				if(importCount%100 == 0){
					conn.commit();
				}
			} catch (IgnoredCrimeException e) {
				/*LOG*/report.countException(e);
			}
			data = parser.next();
		}
		/*LOG*/report.printSummary();
		updateLastImportTime(parser);
	}
	
	public void setupOriginalBaseReference(Parser parser) throws SQLException{
		String name = parser.getBaseName();
		ResultSet rs = conn.createStatement().executeQuery("SELECT bci_idbase FROM tb_bci_base_crimes_importacao WHERE bci_nome='" + name + "'");
		Integer id = null;
		if(rs.next()){
			id = rs.getInt("bci_idbase");
		}else{
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO tb_bci_base_crimes_importacao (BCI_NOME, BCI_URL, BCI_DATA_HORA_REGISTRO) VALUES (?,?,?)");
			stmt.setString(1, parser.getBaseName());
			stmt.setString(2, parser.getBaseUrl());
			stmt.setTimestamp(3, new Timestamp(new Date().getTime()));
			stmt.executeUpdate();
			id = lastId(stmt);
			conn.commit();
		}
		parser.setBaseId(id);
	}
	
	public void updateLastImportTime(Parser parser) throws SQLException{
		PreparedStatement stmt = conn.prepareStatement("UPDATE tb_bci_base_crimes_importacao SET bci_data_ultima_importacao=? WHERE bci_idbase=?");
		stmt.setTimestamp(1, new Timestamp(new Date().getTime()));
		stmt.setLong(2, parser.getBaseId());
		stmt.executeUpdate();
		conn.commit();
	}

	public void executeUpdates(Model crime) throws SQLException, IgnoredCrimeException{
		
		if(existsInDB(crime))
			throw new IgnoredCrimeException(PREVIOUSLY_IMPORTED); 
		
		//insert principal
		stmtCrime.setLong(1, crime.TLO_IDTIPO_LOCAL);
		stmtCrime.setLong(2, crime.TCR_IDTIPO_CRIME);
		stmtCrime.setLong(3, crime.TPA_IDTIPO_PAPEL);
		stmtCrime.setLong(4, crime.TTR_IDTIPO_TRANSPORTE);
		stmtCrime.setInt(5, crime.TAU_IDTIPO_ARMA_USADA);
		stmtCrime.setLong(6, crime.TRE_IDTIPO_REGISTRO);
		stmtCrime.setTimestamp(7, crime.CRI_DATA);
		stmtCrime.setInt(8, crime.CRI_QUANTIDADE);
		stmtCrime.setInt(9, crime.CRI_FAIXA_ETARIA);
		stmtCrime.setInt(10, crime.CRI_QTD_MASCULINO);
		stmtCrime.setInt(11, crime.CRI_QTD_FEMININO);
		stmtCrime.setBoolean(12, crime.CRI_SEXO);
		stmtCrime.setString(13, crime.CRI_DESCRICAO);
		stmtCrime.setDouble(14, crime.CRI_LATITUDE);
		stmtCrime.setDouble(15, crime.CRI_LONGITUDE);
		stmtCrime.setLong(16, crime.CRI_HORARIO);
		stmtCrime.setTimestamp(17, crime.CRI_DATA_HORA_REGISTRO);
		stmtCrime.setLong(18, crime.TVI_IDTIPO_VITIMA);
		stmtCrime.setString(19, crime.CRI_ENDERECO);
		stmtCrime.setString(20, crime.CRI_CIDADE);
		stmtCrime.setString(21, crime.CRI_ESTADO);
		stmtCrime.setString(22, crime.CRI_PAIS);
		stmtCrime.setString(23, crime.CRI_CEP);
		stmtCrime.setLong(24, crime.USRS_IDUSUARIO_REDE_SOCIAL);
		stmtCrime.setString(25, crime.CRI_EMBED_NOTICIA);
		stmtCrime.setString(26, crime.CRI_LINK_NOTICIA);
		stmtCrime.setString(27, crime.CRI_REGISTRADO_PELA_API);
		stmtCrime.setString(28, crime.CRI_CACHE_ESTATISTICAS);
		stmtCrime.setString(29, crime.CRI_ID_ORIGINAL);
		stmtCrime.setLong(30, crime.CRI_ID_BASE_ORIGEM);
		stmtCrime.executeUpdate();
		int id = lastId(stmtCrime);
		
		//chave
		stmtChave.setString(1, crime.generateCRI_CHAVE(id));
		stmtChave.setInt(2, id);
		stmtChave.executeUpdate();
		
		//confirmacao automática
		stmtConfi.setInt(1, id);
		stmtConfi.setTimestamp(2, crime.CRI_DATA_HORA_REGISTRO);
		stmtConfi.executeUpdate();
		
		//razao
		stmtRazao.setInt(1, id);
		stmtRazao.executeUpdate();
		
		//credibilidade
		stmtCredi.setTimestamp(1, crime.CRI_DATA_HORA_REGISTRO);
		stmtCredi.setInt(2, id);
		stmtCredi.executeUpdate();
	}
	
	private boolean existsInDB(Model crime) throws SQLException{
		PreparedStatement stmt = conn.prepareStatement("SELECT cri_idcrime FROM tb_cri_crime WHERE cri_id_base_origem=? and cri_id_original=?");
		stmt.setLong(1, crime.CRI_ID_BASE_ORIGEM);
		stmt.setString(2, crime.CRI_ID_ORIGINAL);
		ResultSet rs = stmt.executeQuery();
		return rs.next();
	}
	
	private static int lastId(Statement stmt) throws SQLException{
		int lastId = 0;
		ResultSet rs = stmt.getGeneratedKeys();
		if (rs.next()) {
			lastId = rs.getInt(1);
		} else {
			throw new RuntimeException();
		}
		return lastId;
	}
	
}
