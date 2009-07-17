package org.wikicrimes.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.mail.EmailException;
import org.wikicrimes.dao.Menssagem;
import org.wikicrimes.model.Area;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.Usuario;
import org.wikicrimes.util.Email;

public class EmailAlertaService
{
	//Variáveis gerais
	private static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	private static boolean controle;
	private static String log;
	private static String templatePT, templateEN, templateIT;
	private static String subjectPT = "Alerta WikiCrimes!", subjectEN="WikiCrimes Alert!",subjectIT="Avviso - WikiCrimes.org!"; 
	static
	{
		try 
		{
	        templatePT = lerArquivo("./templates/template_pt.html");
	        templateEN = lerArquivo("./templates/template_en.html");
	        templateIT = lerArquivo("./templates/template_it.html");
	    }
		catch (Exception e) 
	    {
	    	e.printStackTrace();
	    }
	}

	/**
	 * Enviar um e-mail
	 * 
	 * @param usuario Usuario que receberá o e-mail
	 */
	public static void enviarEmail(Usuario usuario, Menssagem menssagem) throws EmailException
	{
		controle = false;
		
		for(Area area : usuario.getAreas())
			if(area.getCrimes().size()>0)
				controle = true;
		
		if(controle)
		{
			String template = "", conteudoDinamicoHTML = "", tipoCrime, preposicao;
			String subject = "";
			
			
			
			if(usuario.getIdioma()!=null)
			{
				if(usuario.getIdioma().equalsIgnoreCase("es"))
				{
					template = templateEN;
					subject = subjectEN;
					preposicao = " to a ";
				}
				else if(usuario.getIdioma().equalsIgnoreCase("pt")||usuario.getIdioma().equalsIgnoreCase("pt_BR"))
				{
					template = templatePT;
					subject = subjectPT;
					preposicao = " a ";
					
				}else if (usuario.getIdioma().equalsIgnoreCase("it"))
					{
					template = templateIT;
					subject = subjectIT;
					preposicao = " a ";
					}
				else
				{
					template = templateEN;
					subject = subjectEN;
					preposicao = " to a ";
					
				}
			}
			else
			{
				template = templatePT;
				subject = subjectPT;
				preposicao = " a ";
			}
			
			log = "Enviando email para usuario: id " + usuario.getIdUsuario() +", email "+usuario.getEmail()+"<br/>";
			
			
			
			template = replaceSubstring(template, "%nome", usuario.getPrimeiroNome() + " " + usuario.getUltimoNome());
			template = replaceSubstring(template, "%email", usuario.getEmail());
			
			log += "-AREAS<br/>";
			
			for(Area area : usuario.getAreas())
			{
				log += "id " +area.getId()+ ", nome " + area.getNome() + "<br/>";
				if(area.getCrimes().size()>0)
				{
					conteudoDinamicoHTML += 
										" <p align='center'> "+menssagem.consulta("scheduler.titulo.area", usuario.getIdioma())+" : <strong>"+area.getNome()+"</strong></p> "+
										"<table width='550' align='center' style='border-collapse: collapse;'>" +
										"	<tbody>" +
										
										"			<tr align='center' style='font-size: 12px; text-decoration: underline; border-width: 1px; border-style: solid; border-color: gray; background-color: #FFFF55;'>" +
										"				<td width='24%'>" +
															 menssagem.consulta("scheduler.titulo.tipoCrime", usuario.getIdioma()) +
										"				</td>" +
										"				<td width='16%'>" +
															 menssagem.consulta("scheduler.titulo.data", usuario.getIdioma()) +
										"				</td>" +
										"				<td width='12%'>" +
															 menssagem.consulta("scheduler.titulo.horario", usuario.getIdioma()) +
										"				</td>" +
										"				<td width='38%'>" +
															 menssagem.consulta("scheduler.titulo.descricao", usuario.getIdioma()) +
										"				</td>" +
										"				<td width='5%'>" +
															 menssagem.consulta("scheduler.titulo.ver", usuario.getIdioma()) +
										"				</td>" +
										"			</tr>";
					
					for( Crime crime : area.getCrimes() )
					{
						//Trata a string de Tipo Crime e Tipo Vitima
						if(crime.getTipo().equalsIgnoreCase("tipocrime.violencia"))
							tipoCrime = menssagem.consulta(crime.getVitima(),usuario.getIdioma());
						else
							tipoCrime = menssagem.consulta(crime.getTipo(),usuario.getIdioma()) + preposicao +
										menssagem.consulta(crime.getVitima(),usuario.getIdioma());
						
						conteudoDinamicoHTML += "<tr style='border-width: 1px; border-style: solid; border-color: gray; background-color: #EEEEEE; font-size: 12px;' >" +
										"				<td width='24%'>" +
										"					<p align='center' >" +
																tipoCrime+
										"					</p>" +														
										"				</td>" +
										"				<td width='16%'>" +
										"					<p align='center' >" +
																format.format(crime.getData())+
										"					</p>" +
										"				</td>" +
										"				<td width='12%'>" +
										"					<p align='center'>" +
																crime.getHorario()+":00"+
										"					</p>" +
										"				</td>" +
										"				<td width='38%'>" +
										"					<p align='justify' style='text-indent: 15pt'><i>" +
																crime.getDescricao()+
										"					</i></p>" +
										"				</td>" +
										"				<td  width='5%'>" +
										"					<p align='center'><a href='http://www.wikicrimes.org/main.html?idcrime="+crime.getChave()+"'> <img src='http://www.wikicrimes.org/images/lupa.gif' border='0' width='16' height='17' title='Clique para observar no mapa'  alt='+'/> </a></p>" +
										"				</td>" +
										"			<tr>";
						
						log += "Crime id :<a href='http://www.wikicrimes.org/main.html?idcrime="+crime.getChave()+"'>" + crime.getId()+"</a><br/>";
					}
					log += "<br/>";
					conteudoDinamicoHTML += "</tbody> </table> <br/>";
					
				}
			}
			
			//Reply necessário?
			
			template = replaceSubstring(template, "%conteudo", conteudoDinamicoHTML);
			
			Email.enviarEmail(usuario.getEmail(), usuario.getPrimeiroNome() +" "+ usuario.getUltimoNome(), 
					subject, template);
			
			HTMLLogService.addLinha("----------------<br/> "+ new Date() +"<br/>"+ log + "----------------");
			
		}
	}
	
	public static String replaceSubstring(String texto, String seraSubstituido, String substituto) {
		
		int i = -1, tamanhoSeraSubstituido = seraSubstituido.length();
		
		do{
			i = texto.indexOf(seraSubstituido,i);
			if(i>=0)
				texto = texto.substring(0, i) + substituto + texto.substring(i+tamanhoSeraSubstituido);
		}while(i >= 0);
		
		return texto;
	}
	
	public static String lerArquivo(String url) throws Exception
	{
		BufferedReader in = new BufferedReader(new FileReader(url));
        String texto = "";
        
        while(in.ready())
        {
        	texto += in.readLine();
        }
        in.close();
        
        return texto;
	}
}

