package org.wikicrimes.tarefa;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimerTask;

import org.wikicrimes.dao.InterfaceDAO;
import org.wikicrimes.dao.Menssagem;
import org.wikicrimes.model.Usuario;
import org.wikicrimes.service.EmailAlertaService;
import org.wikicrimes.service.EmailErrorService;
import org.wikicrimes.service.HTMLLogService;

public class Mailer extends TimerTask
{
	public static final long MENSAL = 1l, DIARIO = 2l, SEMANAL = 3l;
	
	private GregorianCalendar calendar;
	private Date date;
	private int weekDay;
	private int monthDay;
	private InterfaceDAO dao;
	private ArrayList<Usuario> usuarios;
	private Menssagem menssagem;
	/**
	 * Construtor recebe o DAO a ser usado.
	 */
	public Mailer(InterfaceDAO dao)
	{
		this.dao = dao;
	}
	
	/**
	 * Corpo do programa da tarefa
	 */
	public void run()
	{
		try
		{
			System.out.println(new Date() + " Inicio de tarefa..");
			
			// Caso DAO nao tenha sido inicializado.
			if (dao == null)
				throw new Exception("DAO nao inicializado");
			menssagem = new Menssagem();
			
			dao.abrirConexao();

			HTMLLogService.addLinha("<h2>-- Inicio do processamento diario --</h2>");
			
			//Trata possíveis pendências
			if(!dao.getControle(MENSAL))
			{
				HTMLLogService.addLinha("<h3>- enviando e-mails pendentes mensais...</h3>");
				usuarios = dao.getUsuariosExcep(MENSAL);
				
				enviarEmails(usuarios, MENSAL);
			}
			if(!dao.getControle(SEMANAL))
			{
				HTMLLogService.addLinha("<h3>- enviando e-mails pendentes semanais...</h3>");
				usuarios = dao.getUsuariosExcep(SEMANAL);
				
				enviarEmails(usuarios, SEMANAL);
			}
			if(!dao.getControle(DIARIO))
			{
				HTMLLogService.addLinha("<h3>- enviando e-mails pendentes diarios...</h3>");
				usuarios = dao.getUsuariosExcep(DIARIO);
				
				enviarEmails(usuarios, DIARIO);
			}
			
			
			// Instancia um objeto data com a data atual.
			date = new Date();
			calendar = new GregorianCalendar();
			calendar.setTime(date);

			// Armazena o dia da semana e do mês.
			weekDay = calendar.get(GregorianCalendar.DAY_OF_WEEK);
			monthDay = calendar.get(GregorianCalendar.DAY_OF_MONTH);
			
			// Envia os e-mails mensais.
			if (monthDay == 1) {
				HTMLLogService.addLinha("<h3>- enviando e-mails mensais...</h3>");
				usuarios = dao.getUsuarios(MENSAL);
				
				enviarEmails(usuarios, MENSAL);
			}
			
			// Envia os e-mails semanais.
			if (weekDay == GregorianCalendar.FRIDAY) {
				HTMLLogService.addLinha("<h3>- enviando e-mails semanais...</h3>");

				usuarios = dao.getUsuarios(SEMANAL);
				
				enviarEmails(usuarios, SEMANAL);
			}
			
			// Envia os e-mails diários
			{
				HTMLLogService.addLinha("<h3>- enviando e-mails diarios...</h3>");

				usuarios = dao.getUsuarios(DIARIO);
				
				enviarEmails(usuarios, DIARIO);
			}
			
			HTMLLogService.addLinha("<h3>-- Fim do processamento diario --</h3>");
			HTMLLogService.gravarHTML();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			EmailErrorService.enviarEmail(e);
		}
		finally
		{
			try 
			{
				menssagem = null;
				System.out.println(new Date() +" Termino de tarefa");
				
				if(dao!=null)
					dao.fecharConexao();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Envia emails para os usuarios do ArrayList registrado no banco as ações ocorridas
	 * 
	 * @param usuarios Usuarios que receberam os e-mails
	 */
	public void enviarEmails(ArrayList<Usuario> usuarios, long periodo)
	{
		try
		{
			if(usuarios!=null)
			{
				//Registra os envios no banco como inacabados
				dao.alterarControle(periodo, false);
				
				//Envia o email para todos os usuários
				
				for(Usuario usuario : usuarios)
				{	
					if(menssagem!=null)
					{
						EmailAlertaService.enviarEmail(usuario, menssagem);
						dao.cadastrarEmailEnviado(usuario, periodo);
					}
					else
					{
						System.out.println(" ****** Erro: menssagem = null");
					}
				}

				HTMLLogService.addLinha("* terminado.");
			}
			else
			{
				HTMLLogService.addLinha("** Sem usuarios para enviar");
			}
			//Se terminou, registra no banco
			dao.alterarControle(periodo, true);
			dao.removerEmailEnviado(periodo);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			EmailErrorService.enviarEmail(e);
		}
	}
	
	/*
	 * Retorna o DAO usado.
	 */
	public InterfaceDAO getDao()
	{
		return dao;
	}

	/*
	 * Recebe o DAO a ser usado.
	 */
	public void setDao(InterfaceDAO dao)
	{
		this.dao = dao;
	}
}