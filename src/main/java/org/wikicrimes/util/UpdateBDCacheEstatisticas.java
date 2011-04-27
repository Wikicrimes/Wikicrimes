package org.wikicrimes.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UpdateBDCacheEstatisticas {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");  
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/wikicrimes?user=" + args[0] + "&password=" + args[1]);  
		
		Statement stm = conn.createStatement();
		Statement stm2 = conn.createStatement();
		ResultSet rs = stm.executeQuery("SELECT cri_idcrime, tcr_idtipo_crime, tvi_idtipo_vitima FROM tb_cri_crime ORDER BY cri_idcrime");
		while (rs.next()) { 
		    int crime = rs.getInt("cri_idcrime");  
		    int tipo = rs.getInt("tcr_idtipo_crime");
		    int vitima = rs.getInt("tvi_idtipo_vitima");
		    String ids = tipo + "|" + vitima + "|" ;
		    
			ResultSet rs2 = stm2.executeQuery("select crz_idrazao from tb_crz_crime_razao where crz_idcrime = " + crime + " order by crz_idrazao");
			while (rs2.next()) {  
			    int razao = rs2.getInt("crz_idrazao");
			    ids += razao + ",";
			}
			ids = ids.substring(0, ids.length()-1);
			
		    stm2.executeUpdate("UPDATE tb_cri_crime SET cri_cache_estatisticas='" + ids + "' WHERE cri_idcrime=" + crime );
		    
		}
	}
}
