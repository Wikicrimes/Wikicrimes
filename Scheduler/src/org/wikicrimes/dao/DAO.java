package org.wikicrimes.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import org.wikicrimes.model.Area;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.Ponto;
import org.wikicrimes.model.Usuario;
import org.wikicrimes.service.AreaService;
import org.wikicrimes.service.EmailErrorService;
import org.wikicrimes.tarefa.Mailer;

public class DAO implements InterfaceDAO{

	private Statement stm;
	private ResultSet rs;
	private Connection con;	
	
	private String url = "jdbc:mysql://localhost/wikicrimes";
	private String usuario = "root";
	private String senha = "root";
	
	public DAO()
	{
		try 
		{
			//Carrega um jdbc-odbc driver
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			EmailErrorService.enviarEmail(e);
		}
	}
	
	@Override
	public void abrirConexao() throws SQLException
	{
		con = DriverManager.getConnection(url, usuario, senha);
	}
	
	@Override
	public void fecharConexao() throws SQLException 
	{
		con.close();
	}

	@Override
	public ArrayList<Usuario> getUsuarios(Long periodo) throws SQLException 
	{
		String sql ="SELECT * FROM TB_ARO_AREA_OBSERVACAO ARO, TB_PTA_PONTOS_AREA PTA, TB_USU_USUARIO USU " +
            		"WHERE ARO.ARO_IDAREA_OBSERVACAO = PTA.ARO_IDAREA_OBSERVACAO " +
            		"AND ARO.USU_IDUSUARIO = USU.USU_IDUSUARIO "+
            		"AND ARO.ARO_IDPERIODO_INFORMACAO = " + periodo +
            		" ORDER BY USU.USU_IDUSUARIO, PTA.ARO_IDAREA_OBSERVACAO, PTA_ORDEM_CRIACAO";
            
        return getUsuarios(periodo, sql, false);
	}

	public ArrayList<Usuario> getUsuariosExcep(Long periodo) throws SQLException 
	{
		String sql ="SELECT * FROM TB_ARO_AREA_OBSERVACAO ARO, TB_PTA_PONTOS_AREA PTA, TB_USU_USUARIO USU " +
            		"WHERE ARO.ARO_IDAREA_OBSERVACAO = PTA.ARO_IDAREA_OBSERVACAO " +
            		"AND ARO.USU_IDUSUARIO = USU.USU_IDUSUARIO "+
            		"AND ARO.ARO_IDPERIODO_INFORMACAO = " + periodo + " "+
            		"AND ARO.USU_IDUSUARIO NOT IN " +
            		"(SELECT usu_idusuario FROM tb_eme_email_enviado WHERE cts_idcontrole_schedule = "+periodo+")";

		return getUsuarios(periodo, sql, true);
	}
	
	private ArrayList<Usuario> getUsuarios(Long periodo, String sql, boolean pendencia) throws SQLException 
	{
		//Instanciação das variáveis
		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
		Usuario usuario = new Usuario();
		Area area = new Area();
		Ponto ponto;
		
		area.setId(-1l);
		usuario.setEmail("valor qualquer inicial");
		
		try
		{
			stm = con.createStatement();

            rs = stm.executeQuery(sql);

            //Preenche o ArrayList usuarios.
            while(rs.next())
            {
            	//Verifica se é ou não um novo usuário.
            	if(rs.getLong("USU_IDUSUARIO") != usuario.getIdUsuario())
            	{
            		usuario = new Usuario();
            		
            		usuarios.add(usuario);
            		usuario.setIdUsuario(rs.getLong("USU_IDUSUARIO"));
            		usuario.setEmail(rs.getString("USU_EMAIL"));
            		usuario.setPrimeiroNome(rs.getString("USU_PRIMEIRO_NOME"));
            		usuario.setUltimoNome(rs.getString("USU_ULTIMO_NOME"));
            		usuario.setIdioma(rs.getString("USU_IDIOMA"));
            	}
            	
            	//Verifica se é ou não uma nova área
            	if(rs.getLong("ARO_IDAREA_OBSERVACAO") != area.getId())
            	{
            		area = new Area();
            		
            		usuario.getAreas().add(area);
            		
            		area.setId(rs.getLong("ARO_IDAREA_OBSERVACAO"));
            		area.setNome(rs.getString("ARO_NOME"));
            		area.setPeriodoInformacao(periodo);
            	}
            	
            	//Cria e adiciona pontos às áreas.
            	ponto = new Ponto();
            	
            	ponto.setId(rs.getLong("ARO_IDPONTOS_AREA"));
            	ponto.setLat(rs.getDouble("PTA_LATITUDE"));
            	ponto.setLng(rs.getDouble("PTA_LONGITUDE"));
            	
            	area.getPontos().add(ponto);
            }

            
            ArrayList<Crime> crimes = getCrimes(periodo, pendencia);
            for(Usuario usuarioAux : usuarios)
            {
            	for(Area areaAux : usuarioAux.getAreas())
            	{	
            		AreaService.setArea(areaAux);
		            for(Crime crime : crimes)
		            {
		            	Ponto pontoAux = new Ponto();
		            	pontoAux.setLat(crime.getLatitude());
		            	pontoAux.setLng(crime.getLongitude());
		            	
		            	if(AreaService.pontoPertenceArea( pontoAux ))
		            	{
		            		areaAux.getCrimes().add(crime);
		            	}
		            }
            	}
            }
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			EmailErrorService.enviarEmail(e);
		}
		finally
		{
			if(stm!=null)
				stm.close();
		}
		
		//Retorna todos os usuarios do periodo ou null se a lista estiver vazia
		if(usuarios.size()==0)
			return null;
		else
			return usuarios;
	}
	
	@Override
	public boolean getControle(long periodo) throws SQLException
	{
		boolean retorno = false;
		
		try
		{
			stm = con.createStatement();
			rs = stm.executeQuery("SELECT * FROM tb_cts_controle_schedule WHERE pri_idperiodo_informacao = "+periodo);
			
			rs.next();
		
			if(rs.getInt("enviado")==1)
				retorno = true;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			EmailErrorService.enviarEmail(e);
		}
		finally
		{
			if(stm != null)
				stm.close();
		}
		
		return retorno;
	}

	@Override
	public void alterarControle(long periodo, boolean bool) throws SQLException
	{
		try
		{
			int valor;
			
			if(bool)
				valor = 1;
			else
				valor = 0;
			
			stm = con.createStatement();
			stm.executeUpdate("UPDATE tb_cts_controle_schedule SET enviado = "+valor+" WHERE pri_idperiodo_informacao = "+periodo);	
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			EmailErrorService.enviarEmail(e);
		}
		finally
		{
			stm.close();
		}
	}

	public ArrayList<Crime> getCrimes(Long periodo, boolean pendencia)
	{
		String sql = "", dataInicio, dataAtual;
		
		GregorianCalendar gc = new GregorianCalendar();
		GregorianCalendar gcAux = new GregorianCalendar();
		
		gcAux.setTime(new Date());
		gc.setTime(new Date());
		
		if(periodo == Mailer.DIARIO)
		{
			gc.add(GregorianCalendar.DAY_OF_MONTH, -1);
		}
		else if(periodo == Mailer.SEMANAL)
		{
			gc.add(GregorianCalendar.DAY_OF_MONTH, -7);
		}
		else if(periodo == Mailer.MENSAL)
		{
			gc.add(GregorianCalendar.MONTH, -1);
		}
		else if(periodo == -1)
		{
			gc.add(GregorianCalendar.MONTH, -12);
		}
		if(pendencia)
		{
			gc.add(GregorianCalendar.DAY_OF_MONTH, -1);
			gcAux.add(GregorianCalendar.DAY_OF_MONTH, -1);
		}
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		dataInicio = format.format(gc.getTime());
		dataAtual = format.format(gcAux.getTime());
		
		ArrayList<Crime> crimes = new ArrayList<Crime>();
		Crime crime = new Crime();
		
		try
		{
			stm = con.createStatement();
			
			sql ="SELECT * FROM TB_CRI_CRIME " +
					"INNER JOIN tb_tcr_tipo_crime ON tb_tcr_tipo_crime.TCR_IDTIPO_CRIME = TB_CRI_CRIME.TCR_IDTIPO_CRIME " +
					"INNER JOIN tb_tvi_tipo_vitima ON tb_tvi_tipo_vitima.TVI_IDTIPO_VITIMA = TB_CRI_CRIME.TVI_IDTIPO_VITIMA WHERE CRI_DATA_HORA_REGISTRO >= '"+ dataInicio + "' AND CRI_DATA_HORA_REGISTRO < '"+dataAtual+"' ORDER BY CRI_IDCRIME";
			
			rs = stm.executeQuery(sql);
	        
	        while(rs.next())
	        {
            	crime.setId(rs.getLong("CRI_IDCRIME"));
            	crime.setChave(rs.getString("CRI_CHAVE"));
            	crime.setData(rs.getDate("CRI_DATA"));
        		crime.setTipo(rs.getString("TCR_NOME"));
        		crime.setVitima(rs.getString("TVI_NOME"));
            	crime.setHorario(""+rs.getInt("CRI_HORARIO"));
            	crime.setLatitude(rs.getDouble("CRI_LATITUDE"));
            	crime.setLongitude(rs.getDouble("CRI_LONGITUDE"));
            	crime.setDescricao(rs.getString("CRI_DESCRICAO"));
            	crimes.add(crime);
            	crime = new Crime();            	
            }
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			EmailErrorService.enviarEmail(e);
		}
		return crimes;
	}

	@Override
	public void cadastrarEmailEnviado(Usuario usuario, long periodo) throws SQLException 
	{
		try
		{
			stm = con.createStatement();
			
			stm.executeUpdate("INSERT INTO tb_eme_email_enviado(cts_idcontrole_schedule, usu_idusuario) VALUES("+periodo+","+usuario.getIdUsuario()+")");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			EmailErrorService.enviarEmail(e);
		}
		finally
		{
			stm.close();
		}
	}

	@Override
	public void removerEmailEnviado(long periodo) throws SQLException
	{
		try
		{	
			stm = con.createStatement();
			
			stm.executeUpdate("DELETE FROM tb_eme_email_enviado WHERE cts_idcontrole_schedule = "+periodo);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			EmailErrorService.enviarEmail(e);
		}
		finally
		{
			stm.close();
		}
	}	
}
