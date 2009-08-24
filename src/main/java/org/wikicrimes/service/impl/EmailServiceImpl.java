package org.wikicrimes.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.wikicrimes.model.Comentario;
import org.wikicrimes.model.Confirmacao;
import org.wikicrimes.model.ConfirmacaoRelato;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.Perfil;
import org.wikicrimes.model.Relato;
import org.wikicrimes.model.Usuario;
import org.wikicrimes.service.EmailService;

/**
 * @author Marcos de Oliveira
 *
 */
public class EmailServiceImpl extends GenericCrudServiceImpl implements
		EmailService {
	
	private JavaMailSender mailSender;
	private VelocityEngine velocityEngine;
	
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
	/* (non-Javadoc)
	 * @see org.wikicrimes.service.EmailService#enviarEmail(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void enviarEmail(final String nome, final String email, final String subject,
			final String texto) {

        final MimeMessagePreparator preparator = new MimeMessagePreparator() {
	         public void prepare(MimeMessage mimeMessage) throws Exception {
	            MimeMessageHelper message = new MimeMessageHelper(mimeMessage,true);
	            message.setTo("faleconosco@wikicrimes.org");
	            message.setFrom(email); 
	           	message.setSubject(subject);
	            message.setText("<html><body>Nome: " + nome + 
               		"<br></br><br></br>Email: " + email + "<br></br><br></br>" +
               		texto + "</body></html>", true);
	         }
	      };
		
	      try {
				Thread t = new Thread() {
					public void run() {
						mailSender.send(preparator);
						System.out.println("[" + new Date() + "] Email Fale Conosco enviado por " + email);
					}
				};
				t.start();
				System.out.println("Enviando Fale Conosco email...");
			} catch (MailException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
	  public void sendMailConfirmation(final Usuario usuario, final String locale){
	        final MimeMessagePreparator preparator = new MimeMessagePreparator() {
		         public void prepare(MimeMessage mimeMessage) throws Exception {
		            MimeMessageHelper message = new MimeMessageHelper(mimeMessage,true);
		            message.setTo(usuario.getEmail());
		            
		            message.setFrom("admin@wikicrimes.org"); 
		            String idiomaEmail;
					if (usuario.getIdiomaPreferencial()!=null)
						 idiomaEmail=usuario.getIdiomaPreferencial();
					else
						idiomaEmail=locale;
		           	  String text=null;
		           	    Map model= new HashMap();
						model.put("usuario",usuario);
						if (idiomaEmail.equals("pt_BR") || idiomaEmail.equals("pt")){
							message.setSubject("Confirmação de Cadastro - WikiCrimes.org");
						 text = VelocityEngineUtils.mergeTemplateIntoString(
					               velocityEngine, "org/wikicrimes/template-cadastro-confirmacao.vm", model);
						}
						else if (idiomaEmail.equals("it")){
							message.setSubject("Conferma della Registrazione - WikiCrimes.org");
						 text = VelocityEngineUtils.mergeTemplateIntoString(
					               velocityEngine, "org/wikicrimes/template-cadastro-confirmacao_it.vm", model);
						}
						else {
							message.setSubject("Confirmation of Registration - WikiCrimes.org");
							
							 text = VelocityEngineUtils.mergeTemplateIntoString(
						               velocityEngine, "org/wikicrimes/template-cadastro-confirmacao_en.vm", model);
						}
						message.setText(text,true);
		           /* message.setText("<html><body><img src='cid:identifier1234'>Caro " + usuario.getNome() + 
	                		", Obrigado por se cadastrar no WikiCrimes.org Clique <a href=http://www.wikicrimes.org/confirma.html?id="+usuario.getEmail()+"&key="+usuario.getChaveConfirmacao()+">aqui</a> para confirmar seu cadastro</body></html>", true);
		            FileSystemResource res = new FileSystemResource(new File("/opt/apache-tomcat-6.0.14/webapps/wikicrimes/images/logo.jpg"));
		           // message.addInline("identifier1234", res);
	*/	         }
		      };
			
		      try {
					Thread t = new Thread() {
						public void run() {
							mailSender.send(preparator);
							System.out.println("[" + new Date() + "] Email de Confirmacao de Cadastro enviado para " + usuario.getEmail());
						}
					};
					t.start();
					System.out.print("Enviando email...");
				} catch (MailException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
		/*
		 * MimeMessagePreparator preparator = new MimeMessagePreparator() {
		 * 
		 * public void prepare(MimeMessage mimeMessage) throws Exception {
		 * 
		 * mimeMessage.setRecipient(Message.RecipientType.TO, new
		 * InternetAddress(usuario.getEmail())); mimeMessage.setFrom(new
		 * InternetAddress("admin@wikicrimes.org")); mimeMessage.setText("<html><body>Caro " +
		 * usuario.getNome() + ", Obrigado por se cadastrar no WikiCrimes.org Clique
		 * <a href=aqui para confirmar seu cadastro</body></html>"); } }; try {
		 * this.mailSender.send(preparator); } catch (MailException ex) { // simply
		 * log it and go on... System.err.println(ex.getMessage()); }
		 */
	    }

	  public void sendMailChanges(final Crime crime, final String locale){
	        final MimeMessagePreparator preparator = new MimeMessagePreparator() {
		         public void prepare(MimeMessage mimeMessage) throws Exception {
		            MimeMessageHelper message = new MimeMessageHelper(mimeMessage,true);
		            message.setTo(crime.getUsuario().getEmail());
		            message.setFrom("admin@wikicrimes.org"); 
		           	  String text=null;
		           	    Map model= new HashMap();
						model.put("usuario",crime.getUsuario());
						if (locale.equals("pt_BR") || locale.equals("pt")){
							message.setSubject("Alterações no registro de seu crime - WikiCrimes.org");
						 text = VelocityEngineUtils.mergeTemplateIntoString(
					               velocityEngine, "org/wikicrimes/template-mudancas-crime.vm", model);
						}
						else {
							message.setSubject("Changes in your Crime Register - WikiCrimes.org");
							
							 text = VelocityEngineUtils.mergeTemplateIntoString(
						               velocityEngine, "org/wikicrimes/template-mudancas-crime.vm", model);
						}
						message.setText(text,true);
		          }
		      };
			
		      try {
					Thread t = new Thread() {
						public void run() {
							mailSender.send(preparator);
							System.out.println("[" + new Date() + "] Email de mudancas de crime enviado para " + crime.getUsuario().getEmail());
						}
					};
					t.start();
					System.out.println("Enviando email...");
				} catch (MailException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
	  }			
	  
	
	public void sendMailConfirmation(final Crime crime, final String locale) {
		
		
		 
		
		
		for (final Confirmacao confirmacao : crime.getConfirmacoes()) {
		
			Usuario usuarioTemp;
			if(confirmacao.getUsuarioIndica()!=null){
				usuarioTemp = confirmacao.getUsuarioIndica();
			}else{
				usuarioTemp = crime.getUsuario();
			}
			final Usuario usuario = usuarioTemp;
			
			final MimeMessagePreparator preparator = new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) throws Exception {
					MimeMessageHelper message = new MimeMessageHelper(
							mimeMessage, true);
		
					ApplicationFactory factory = (ApplicationFactory) FactoryFinder
					.getFactory(FactoryFinder.APPLICATION_FACTORY);
					String bundleName = factory.getApplication().getMessageBundle();
					ResourceBundle bundle;
					String idiomaEmail;
					if (confirmacao.getUsuario().getIdiomaPreferencial()!=null){
						bundle = ResourceBundle.getBundle(bundleName,new Locale(confirmacao.getUsuario().getIdiomaPreferencial().toString()));
						idiomaEmail=confirmacao.getUsuario().getIdiomaPreferencial().toString();
					}
					else {
						bundle = ResourceBundle.getBundle(bundleName,new Locale(locale));
						idiomaEmail=locale;
					}
					
					String descricaoTipoCrime=bundle.getString(crime.getTipoCrime().getNome());
					 String tipoVitima=bundle.getString(crime.getTipoLocal().getTipoVitima().getNome());
					 String descricaoTipoVitima =bundle.getString(crime.getTipoVitima().getNome());
					message.setTo(confirmacao.getUsuario().getEmail());

					message.setFrom("admin@wikicrimes.org");
					
					Map model= new HashMap();
					model.put("usuario",usuario);
					model.put("crime",crime);
					
					if(!crime.getTipoCrime().getIdTipoCrime().equals(new Long(5)))
						model.put("tipoCrime", descricaoTipoCrime);
					else
						model.put("tipoCrime", descricaoTipoVitima);
					model.put("tipoVitima", tipoVitima);
					model.put("confirmacao",confirmacao);					
					String text=null;
					
					if (idiomaEmail.equals("pt_BR") || idiomaEmail.equals("pt")){
						message.setSubject("Confirmação de relato de crime - WikiCrimes.org");
						//verifica se usuario ja e cadastrado
					if(confirmacao.getUsuario().getPerfil().getIdPerfil().equals(new Long(Perfil.CONVIDADO))){
						text = VelocityEngineUtils.mergeTemplateIntoString(
					               velocityEngine, "org/wikicrimes/template-convidado-confirmacao.vm", model);
					}
					else {
						text = VelocityEngineUtils.mergeTemplateIntoString(
					               velocityEngine, "org/wikicrimes/template-usuario-confirmacao.vm", model);
											
					}
					} else if (idiomaEmail.equals("it")){
						message.setSubject("Conferma dell'invio del reato  - WikiCrimes.org");
						if(confirmacao.getUsuario().getPerfil().getIdPerfil()== Perfil.CONVIDADO){
							text = VelocityEngineUtils.mergeTemplateIntoString(
						               velocityEngine, "org/wikicrimes/template-convidado-confirmacao_it.vm", model);
						}
						else {
							text = VelocityEngineUtils.mergeTemplateIntoString(
						               velocityEngine, "org/wikicrimes/template-usuario-confirmacao_it.vm", model);
						}
					}
					else {
						message.setSubject("Crime report confirmation - WikiCrimes.org");
						if(confirmacao.getUsuario().getPerfil().getIdPerfil()== Perfil.CONVIDADO){
							text = VelocityEngineUtils.mergeTemplateIntoString(
						               velocityEngine, "org/wikicrimes/template-convidado-confirmacao_en.vm", model);
						}
						else {
							text = VelocityEngineUtils.mergeTemplateIntoString(
						               velocityEngine, "org/wikicrimes/template-usuario-confirmacao_en.vm", model);
												
						}
					}
					message.setText(text,true);
				}
			};

			try {
				Thread t = new Thread() {
					public void run() {
						mailSender.send(preparator);
						System.out.println("[" + new Date() + "] Email de Confirmacao de Crime registrado por " + usuario.getEmail() + " para " + confirmacao.getUsuario().getEmail());
					}
				};
				t.start();
				System.out.print("Enviando email...");
			} catch (MailException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}

		}
	}


	public void enviarEmailRecuperaSenha(final Usuario usuario, final String locale) {
		          final MimeMessagePreparator preparator = new MimeMessagePreparator() {
			         public void prepare(MimeMessage mimeMessage) throws Exception {
			        	 String text;
			            MimeMessageHelper message = new MimeMessageHelper(mimeMessage,true);
			            message.setTo(usuario.getEmail());
			            
			            message.setFrom("admin@wikicrimes.org"); 
			           	           
			           	    Map model= new HashMap();
							model.put("usuario",usuario);
							String idiomaEmail;
							if (usuario.getIdiomaPreferencial()!=null)
								 idiomaEmail=usuario.getIdiomaPreferencial();
							else
								idiomaEmail=locale;
							if (idiomaEmail.equals("pt_BR") || idiomaEmail.equals("pt")){
							message.setSubject("Recuperação de Senha - WikiCrimes.org");
							text = VelocityEngineUtils.mergeTemplateIntoString(
						               velocityEngine, "org/wikicrimes/template-recupera-senha.vm", model);
							} else if (idiomaEmail.equals("it")){
								message.setSubject("Recupero password - WikiCrimes.org");
								text = VelocityEngineUtils.mergeTemplateIntoString(
							               velocityEngine, "org/wikicrimes/template-recupera-senha_it.vm", model);
							}
							else {
								message.setSubject("Password Recovery - WikiCrimes.org");
								text = VelocityEngineUtils.mergeTemplateIntoString(
							               velocityEngine, "org/wikicrimes/template-recupera-senha_en.vm", model);
							}
							message.setText(text,true);
			           
			         	}
			      };
				
			      try {
						Thread t = new Thread() {
							public void run() {
								mailSender.send(preparator);
								System.out.println(" Email enviado!");
							}
						};
						t.start();
						System.out.print("[" + new Date() + "] Enviando email de recuperacao de senha... " + usuario.getEmail());
					} catch (MailException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		
	}

	public void sendMailConfirmation(final Relato relato, final String locale) {
		Usuario usuarioTemp = null;
		if(relato.getUsuario()!=null){
			usuarioTemp = relato.getUsuario();
		}	
		else{
			for (Iterator iterator = relato.getConfirmacoes().iterator(); iterator.hasNext();) {
				ConfirmacaoRelato conf = (ConfirmacaoRelato) iterator.next();
				usuarioTemp = conf.getUsuarioIndicado();
			}
		}
		
		
		final Usuario usuario = usuarioTemp;
		for (final ConfirmacaoRelato confirmacao : relato.getConfirmacoes()) {
			
			final MimeMessagePreparator preparator = new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) throws Exception {
					MimeMessageHelper message = new MimeMessageHelper(
							mimeMessage, true);
					message.setTo(confirmacao.getUsuario().getEmail());

					message.setFrom("admin@wikicrimes.org");
					
					Map model= new HashMap();
					model.put("usuario",usuario);
					model.put("relato",relato);
					model.put("confirmacao",confirmacao);
					String text=null;
					
					if (locale.equals("pt_BR") || locale.equals("pt")){
						message.setSubject("Confirmação de Denúncia - WikiCrimes.org");
						//verifica se usuario ja e cadastrado
						if(relato.getSubTipoRelato().equalsIgnoreCase("1"))
							model.put("subTipoRelato", "Áreas Perigosas");
						if(relato.getSubTipoRelato().equalsIgnoreCase("2"))
							model.put("subTipoRelato", "Áreas com uso/tráfico de drogas");
						if(relato.getSubTipoRelato().equalsIgnoreCase("3"))
							model.put("subTipoRelato", "Bares Ilegais");
						if(relato.getSubTipoRelato().equalsIgnoreCase("4"))
							model.put("subTipoRelato", "Uso Excessivo de Álcool");
						if(relato.getSubTipoRelato().equalsIgnoreCase("5"))
							model.put("subTipoRelato", "Violência contra Mulher");
						if(relato.getSubTipoRelato().equalsIgnoreCase("6"))
							model.put("subTipoRelato", "Violência contra Criança");
					if(confirmacao.getUsuario().getPerfil().getIdPerfil()== Perfil.CONVIDADO){
						text = VelocityEngineUtils.mergeTemplateIntoString(
					               velocityEngine, "org/wikicrimes/template-convidado-relato-confirmacao.vm", model);
					}
					else {
						text = VelocityEngineUtils.mergeTemplateIntoString(
					               velocityEngine, "org/wikicrimes/template-usuario-relato-confirmacao.vm", model);
											
					}
					}
					else {
						if(relato.getSubTipoRelato().equalsIgnoreCase("1"))
							model.put("subTipoRelato", "Danger Area");
						if(relato.getSubTipoRelato().equalsIgnoreCase("2"))
							model.put("subTipoRelato", "Drug Abuse");
						if(relato.getSubTipoRelato().equalsIgnoreCase("3"))
							model.put("subTipoRelato", "Shebeen Location");
						if(relato.getSubTipoRelato().equalsIgnoreCase("4"))
							model.put("subTipoRelato", "Alcohol Abuse");
						if(relato.getSubTipoRelato().equalsIgnoreCase("5"))
							model.put("subTipoRelato", "Woman Abuse");
						if(relato.getSubTipoRelato().equalsIgnoreCase("6"))
							model.put("subTipoRelato", "Child Abuse");
						message.setSubject("Denounce Confirmation - WikiCrimes.org");
						if(confirmacao.getUsuario().getPerfil().getIdPerfil()== Perfil.CONVIDADO){
							text = VelocityEngineUtils.mergeTemplateIntoString(
						               velocityEngine, "org/wikicrimes/template-convidado-relato-confirmacao_en.vm", model);
						}
						else {
							text = VelocityEngineUtils.mergeTemplateIntoString(
						               velocityEngine, "org/wikicrimes/template-usuario-relato-confirmacao_en.vm", model);
												
						}
					}
					message.setText(text,true);
				}
			};

			try {
				Thread t = new Thread() {
					public void run() {
						mailSender.send(preparator);
						System.out.println("[" + new Date() + "] Email de Confirmacao de Denúncia registrado por " + usuario.getEmail() + " para " + confirmacao.getUsuario().getEmail());
					}
				};
				t.start();
				System.out.print("Enviando email...");
			} catch (MailException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}

		}
	}

	//enviar notificacao ao autor do registro de crime que seu crime recebeu uma confirmacao
	public void enviarEmailNotificacao(final Confirmacao confirmacao, final String locale) {
		final Usuario usuario = confirmacao.getCrime().getUsuario();
		final Crime crime = confirmacao.getCrime();
				
			final MimeMessagePreparator preparator = new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) throws Exception {
					
					String idiomaEmail;
					if (usuario.getIdiomaPreferencial()!=null)
						 idiomaEmail=usuario.getIdiomaPreferencial();
					else
						idiomaEmail=locale;
					ApplicationFactory factory = (ApplicationFactory) FactoryFinder
					.getFactory(FactoryFinder.APPLICATION_FACTORY);
					String bundleName = factory.getApplication().getMessageBundle();
					ResourceBundle bundle = ResourceBundle.getBundle(bundleName,new Locale(idiomaEmail));
					final String descricaoTipoConfirmacao = bundle.getString(confirmacao.getTipoConfirmacao().getDescricao());
					String descTipoCrime =crime.getTipoCrime().getNome();
					final String descricaoTipoCrime=bundle.getString(descTipoCrime);
					 final String descricaoTipoVitima =bundle.getString(crime.getTipoVitima().getNome());
					 final String usuarioTexto = bundle.getString("usuario.texto");
					final boolean positiva = confirmacao.getConfirma();
					MimeMessageHelper message = new MimeMessageHelper(
							mimeMessage, true);
					message.setTo(usuario.getEmail());

					message.setFrom("wikicrimes@wikicrimes.org");
					
					Map model= new HashMap();
					model.put("usuario",usuario);
					model.put("crime",crime);
					model.put("confirmacao",confirmacao);
					model.put("descricaoTipoConfirmacao",descricaoTipoConfirmacao);
					
					if(confirmacao.getIndicacaoEmail()!= null && confirmacao.getIndicacaoEmail()==true)
						model.put("usuarioEmailouNome","(" + confirmacao.getUsuario().getEmail() +")");
					else
						model.put("usuarioEmailouNome",usuarioTexto);
					if (!positiva)
						model.put("nao", " não ");
					else
						model.put("nao", " ");
					if(crime.getTipoCrime().getIdTipoCrime() != 5)
						model.put("descricaoTipoCrime", descricaoTipoCrime);
					else
						model.put("descricaoTipoCrime", descricaoTipoVitima);
					String text=null;
					
					if (idiomaEmail.equals("pt_BR") || idiomaEmail.equals("pt")){
						message.setSubject("Notificação sobre seu relato de crime - WikiCrimes.org");
							text = VelocityEngineUtils.mergeTemplateIntoString(
					               velocityEngine, "org/wikicrimes/template-notificacao-confirmacao.vm", model);
					}
					else if (idiomaEmail.equals("it")){
						message.setSubject("Notifica circa la tua segnalazione di reato - WikiCrimes.org");
							text = VelocityEngineUtils.mergeTemplateIntoString(
					               velocityEngine, "org/wikicrimes/template-notificacao-confirmacao_it.vm", model);
					}
					else {
						message.setSubject("Notification about your crime report - WikiCrimes.org");
						text = VelocityEngineUtils.mergeTemplateIntoString(
						               velocityEngine, "org/wikicrimes/template-notificacao-confirmacao_en.vm", model);
										
					}
					message.setText(text,true);
				}
			};

			try {
				Thread t = new Thread() {
					public void run() {
						mailSender.send(preparator);
						System.out.println("[" + new Date() + "] Email de Notificação de confirmacao de Crime por " + confirmacao.getUsuario().getEmail() + " para " + usuario.getEmail());
					}
				};
				t.start();
				System.out.print("Enviando email...");
			} catch (MailException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}

		}
	//enviar notificacao ao autor do registro de crime que seu relato recebeu uma confirmacao
	public void enviarEmailNotificacao(final ConfirmacaoRelato confirmacao, final String locale) {
		final Usuario usuario = confirmacao.getRelato().getUsuario();
		final Relato relato = confirmacao.getRelato();
				
			final MimeMessagePreparator preparator = new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) throws Exception {
					
					String idiomaEmail;
					if (usuario.getIdiomaPreferencial()!=null)
						 idiomaEmail=usuario.getIdiomaPreferencial();
					else
						idiomaEmail=locale;
					ApplicationFactory factory = (ApplicationFactory) FactoryFinder
					.getFactory(FactoryFinder.APPLICATION_FACTORY);
					String bundleName = factory.getApplication().getMessageBundle();
					ResourceBundle bundle = ResourceBundle.getBundle(bundleName,new Locale(idiomaEmail));
					final boolean positiva = confirmacao.getConfirma();
					final String usuarioTexto = bundle.getString("usuario.texto");
					MimeMessageHelper message = new MimeMessageHelper(
							mimeMessage, true);
					message.setTo(usuario.getEmail());

					message.setFrom("wikicrimes@wikicrimes.org");
					
					Map model= new HashMap();
					model.put("usuario",usuario);
					model.put("confirmacao",confirmacao);
					model.put("relato",relato);
					if(confirmacao.getIndicacaoEmail())
						model.put("usuarioEmailouNome","(" + confirmacao.getUsuario().getEmail() +")");
					else
						model.put("usuarioEmailouNome",usuarioTexto);
					if (!positiva)
						model.put("nao", " não ");
					else
						model.put("nao", " ");
					String text=null;
					
					if (idiomaEmail.equals("pt_BR") || idiomaEmail.equals("pt")){
						message.setSubject("Notificação sobre sua denúncia - WikiCrimes.org");
							text = VelocityEngineUtils.mergeTemplateIntoString(
					               velocityEngine, "org/wikicrimes/template-notificacao-relato.vm", model);
					}
					else {
						message.setSubject("Notification about your report - WikiCrimes.org");
						text = VelocityEngineUtils.mergeTemplateIntoString(
						               velocityEngine, "org/wikicrimes/template-notificacao-relato_en.vm", model);
										
					}
					message.setText(text,true);
				}
			};

			try {
				Thread t = new Thread() {
					public void run() {
						mailSender.send(preparator);
						System.out.println("[" + new Date() + "] Email de Notificação de confirmacao de Relato por " + confirmacao.getUsuario().getEmail() + " para " + usuario.getEmail());
					}
				};
				t.start();
				System.out.print("Enviando email...");
			} catch (MailException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}

		}

	/**
	 * envia notificacao ao autor do registro de crime que seu crime recebeu um comentario
	 */
		
	public void enviarEmailNotificacao(final Comentario comentario, final String locale) {
		final Usuario usuario = comentario.getCrime().getUsuario();
		final Crime crime = comentario.getCrime();
		ApplicationFactory factory = (ApplicationFactory) FactoryFinder
		.getFactory(FactoryFinder.APPLICATION_FACTORY);
		
			final MimeMessagePreparator preparator = new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) throws Exception {
					String idiomaEmail;
					if (usuario.getIdiomaPreferencial()!=null)
						 idiomaEmail=usuario.getIdiomaPreferencial();
					else
						idiomaEmail=locale;
					ApplicationFactory factory = (ApplicationFactory) FactoryFinder
					.getFactory(FactoryFinder.APPLICATION_FACTORY);
					String bundleName = factory.getApplication().getMessageBundle();
					ResourceBundle bundle = ResourceBundle.getBundle(bundleName,new Locale(idiomaEmail));
					String descTipoCrime =crime.getTipoCrime().getNome();
					final String descricaoTipoCrime=bundle.getString(descTipoCrime);
					final String usuarioTexto = bundle.getString("usuario.texto");
					String tipoVitima;
					if (crime.getTipoVitima().getNome()!=null){
						tipoVitima =bundle.getString(crime.getTipoVitima().getNome());	
					}
					else {
						tipoVitima =null;
					}
					 final String descricaoTipoVitima=tipoVitima;
					MimeMessageHelper message = new MimeMessageHelper(
							mimeMessage, true);
					message.setTo(usuario.getEmail());

					message.setFrom("wikicrimes@wikicrimes.org");
					
					Map model= new HashMap();
					model.put("usuario",usuario);
					model.put("crime",crime);
					model.put("comentario",comentario);
					if(comentario.getUsuario().getPerfil().getIdPerfil()==2)
						model.put("usuarioEmailouNome","(" + comentario.getUsuario().getEmail() +")");
					else
						model.put("usuarioEmailouNome",usuarioTexto);
					if(crime.getTipoCrime().getIdTipoCrime() != 5)
						model.put("descricaoTipoCrime", descricaoTipoCrime);
					else
						model.put("descricaoTipoCrime", descricaoTipoVitima);
					String text=null;
					
					if (idiomaEmail.equals("pt_BR") || idiomaEmail.equals("pt")){
						message.setSubject("Comentários sobre seu relato de crime - WikiCrimes.org");
							text = VelocityEngineUtils.mergeTemplateIntoString(
					               velocityEngine, "org/wikicrimes/template-notificacao-comentario.vm", model);
					}
					else if (idiomaEmail.equals("es")){
						message.setSubject("Comentarios sobre relato de crimen - WikiCrimes.org");
						text = VelocityEngineUtils.mergeTemplateIntoString(
				               velocityEngine, "org/wikicrimes/template-notificacao-comentario_es.vm", model);
					}
					else if (idiomaEmail.equals("it")){
						message.setSubject("Commenti sulla tua segnalazione di reato - WikiCrimes.org");
						text = VelocityEngineUtils.mergeTemplateIntoString(
				               velocityEngine, "org/wikicrimes/template-notificacao-comentario_it.vm", model);
					}
					else {
						message.setSubject("Comments about your crime report - WikiCrimes.org");
						text = VelocityEngineUtils.mergeTemplateIntoString(
						               velocityEngine, "org/wikicrimes/template-notificacao-comentario_en.vm", model);
										
					}
					message.setText(text,true);
				}
			};

			try {
				Thread t = new Thread() {
					public void run() {
						mailSender.send(preparator);
						System.out.println("[" + new Date() + "] Email de Notificação de comentário de " + comentario.getUsuario().getEmail() + " enviado para " + usuario.getEmail());
					}
				};
				t.start();
				System.out.print("Enviando email...");
			} catch (MailException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}

		}
		
	

}
