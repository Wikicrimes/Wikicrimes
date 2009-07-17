package org.wikicrimes.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;

import org.wikicrimes.dao.DAO;
import org.wikicrimes.tarefa.*;

public class Main 
{	
	public static void main(String[] args) 
	{
		try 
		{
	        BufferedReader in = new BufferedReader(new FileReader("./properties/inicio.properties"));
	        ArrayList<Integer> props = new ArrayList<Integer>();
	        String aux;
	        
	        while(in.ready())
	        {
	        	aux = in.readLine();
	        	props.add( Integer.parseInt( aux.substring(aux.indexOf("=")+1) ) );
	        }
	        in.close();
	        
			//Cria um calendário com a data e hora desejada.
			Date data = new Date();
			GregorianCalendar gCalendar = new GregorianCalendar();
			gCalendar.setTime(data);
			gCalendar.set(GregorianCalendar.YEAR, props.get(0));
			gCalendar.set(GregorianCalendar.MONTH, props.get(1));
			gCalendar.set(GregorianCalendar.DAY_OF_MONTH, props.get(2));
			gCalendar.set(GregorianCalendar.HOUR_OF_DAY, props.get(3));
			gCalendar.set(GregorianCalendar.MINUTE, props.get(4));
			gCalendar.set(GregorianCalendar.SECOND, 0);
			data = gCalendar.getTime();
			
			if (gCalendar.getTime().after(new Date())) 
				System.out.println("Inicio de schedule agendado para "+props.get(2)+"/"+props.get(1)+"/"+props.get(0)+" "+props.get(3)+"h "+props.get(4)+"m...");
			else
				throw new Exception("Data de inicio invalida.");	
			
			//Cria a tarefa a ser executada.
			Mailer tarefa = new Mailer(new DAO());
			
			//Cria o objeto que administra o tempo.
			Timer timer = new Timer();
			
			//Agenda a tarefa para data 'd' e para ser repetida a cada '86400000' milisegundos(24h).
			timer.scheduleAtFixedRate(tarefa, data, 86400000);
	    }
		catch (Exception e) 
	    {
	    	e.printStackTrace();
	    }
	}
}