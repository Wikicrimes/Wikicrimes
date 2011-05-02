//fun��o que constroi o html de um amigo			
function constroiHtml(amigo){
	var html_amigo="<div style='width:368;'><div id='topoAlerta'> <b> "+prefs.getMsg("dados.do.seu.amigo")+" </b> </div> <br/> <br/> <table style='font-family:Arial, sans-serif;  font-size: 11px;'>";
	html_amigo+="<tr> <td rowspan='1'> <a target='_blank' href='" + amigo.linkPerfil + "'> <img width='46' height='56' src='" + amigo.foto + "' /> </a> </td> <td> <b> "+prefs.getMsg("nome")+" : </b> </td> <td align='left'> " + amigo.nome + " </td>  </tr>";
					
	html_amigo+="</table>";
	return html_amigo;
}			
//fun��o que constroi o html de um amigo

//fun��o que constroi o html da tela de registro de alerta			
function constroiHtmlAlerta(){
	var html_alerta="<div style='width:100%; height:150px ;overflow: auto;'><div id='topoAlerta'> <b> "+prefs.getMsg("titulo.registrar.alerta")+" </b> </div> <br/> <br/> <table style='font-family:Arial, sans-serif;  font-size: 11px;'>";
	html_alerta+="<tr> <td colspan = '4' > <b> "+prefs.getMsg("desc.alerta")+":(*) </b> </td>  </tr>";
	html_alerta+="<tr> <td colspan = '4' > <div style='color:red;' id = 'erroDesc'> </div> </td>  </tr>";
	html_alerta+="<tr><td colspan = '4'> <TEXTAREA NAME='desc_alerta' id='desc_alerta' style='font-size: 12px; border: 1px solid #2763a5;' COLS='40' ROWS='6'></TEXTAREA> </td></tr>";
	html_alerta+="<tr> <td colspan = '4'> <b>"+prefs.getMsg("em.que.turno.ocorre")+":(*)</b> </td>  </tr>";
	html_alerta+="<tr> <td colspan = '4' > <div style='color:red;' id = 'erroTurno'> </div> </td>  </tr>";
	html_alerta+="<tr> <td> <input type='checkbox' id='cMadrugada' name='cMadrugada'/> </td> <td> "+prefs.getMsg("madrugada")+" </td> <td> <input type='checkbox' id='cManha' name='cManha'/> </td> <td> "+prefs.getMsg("manha")+"</td> </tr>";
	html_alerta+="<tr> <td> <input type='checkbox' id='cTarde' name='cTarde'/> </td> <td> "+prefs.getMsg("tarde")+"</td> <td> <input type='checkbox' id='cNoite' name='cNoite'/> </td> <td> "+prefs.getMsg("noite")+"</td> </tr>";
	html_alerta+="<tr> <td colspan = '4'><b> "+prefs.getMsg("causa.motivo.ocorrencia1")+" <br/> "+prefs.getMsg("causa.motivo.ocorrencia2")+":(*) </b> </td>  </tr>";
	html_alerta+="<tr> <td colspan = '4' > <div style='color:red;' id = 'erroRazoes'> </div> </td>  </tr>";
	html_alerta+="<tr> <td>	<input type='checkbox' id='razao1' name='cIluP'/> </td> <td > "+prefs.getMsg("ma.iluminacao.publica")+" </td> <td> <input type='checkbox' id='razao2' name='cFalLazJovens'/> </td> <td > "+prefs.getMsg("ausencia.lazer.jovens")+" </td> </tr>";
	html_alerta+="<tr> <td> <input type='checkbox' id='razao3' name='cDesReg'/> </td> <td > "+prefs.getMsg("desemprego.regiao")+" </td> <td> <input type='checkbox' id='razao4' name='cFacilAcesFuga'/> </td> <td > "+prefs.getMsg("facil.acesso.fuga")+" </td> </tr>";
	html_alerta+="<tr> <td> <input type='checkbox' id='razao5'	name='cDisGan'/> </td> <td > "+prefs.getMsg("disputa.gangues")+" </td> <td> <input type='checkbox' id='razao6' name='cUsoTrafDrog'/> </td> <td > "+prefs.getMsg("trafico.drogas")+" </td> </tr>";
	html_alerta+="<tr> <td> <input type='checkbox' id='razao7' name='cUsoAcl'/> </td> <td > "+prefs.getMsg("uso.alcool")+" </td> <td> <input type='checkbox' id='razao8' name='cCriRuas'/> </td> <td > "+prefs.getMsg("criancas.ruas")+" </td> </tr>";
	html_alerta+="<tr> <td> <input type='checkbox' id='razao9'	name='cAltConPes'/> </td> <td > "+prefs.getMsg("alta.concentracao.pessoas")+" </td> <td> <input type='checkbox' id='razao10' name='cFalPol'/> </td> <td > "+prefs.getMsg("falta.policiamento")+" </td> </tr>";
	html_alerta+="<tr> <td> <input type='checkbox' id='razao11' name='cOmisTest'/> </td> <td > "+prefs.getMsg("omissao.testemunhas")+" </td> <td> <input type='checkbox' id='razao12' name='cProxRegPerig'/> </td> <td > "+prefs.getMsg("proximidade.regioes.perigosas")+" </td> </tr>";
	html_alerta+="<tr> <td> <input type='checkbox' id='razao13' name='cImpPen'/> </td> <td > "+prefs.getMsg("impunidade.penal")+" </td> <td> <input type='checkbox' id='razao14' name='cPistolagem'/> </td> <td > "+prefs.getMsg("pistolagem")+" </td> </tr>";
	html_alerta+="<tr> <td> <input type='checkbox' id='razao15' name='cVlcPol'/> </td> <td > "+prefs.getMsg("violencia.policial")+" </td> <td> <input type='checkbox' id='razao16' name='cFalMor'/> </td> <td > "+prefs.getMsg("falta.moradia")+" </td> </tr>";
	html_alerta+="<tr> <td> <input type='checkbox' id='razao17' name='cCriOrg'/> </td> <td > "+prefs.getMsg("crime.organizado")+" </td> <td> <input type='checkbox' id='razao18' name='cCriPas'/> </td> <td > "+prefs.getMsg("crime.passional")+" </td> </tr>";
	html_alerta+="<tr> <td> <input type='checkbox' id='razao19' name='cOutros'/> </td> <td > "+prefs.getMsg("outros")+" </td> <td> <input type='checkbox' id='razao20' name='cNaoSei'/> </td> <td > "+prefs.getMsg("nao.sei")+" </td> </tr>";					
	html_alerta+="<tr><td colspan = '4'> <input style='font-family: Arial, Helvetica, sans-serif; font-size: 10px;	font-weight: bold; margin: 1; padding: 1; background-color: #6da6e2; color: white;	border: 1px solid #2763a5;' onclick='validaFormRegistroAlerta();' type='button' value='"+prefs.getMsg("enviar")+"'/> </td></tr>";				
	html_alerta+="</table></div>";
	return html_alerta;
}				
//fun��o que constroi o html da tela de registro de alerta

//fun��o que constroi o html da tela de registro de alerta de Crime		
function constroiHtmlAlertaCrime(tipoCrime){
	var htmTextoVitima = '';
	var htmlTipoVitima = '';
	var htmlTipoLocal = "<option value='1'> Via P&uacute;blica </option> <option value='2'> Tr&acirc;nsporte Coletivo </option> <option value='3'> Estabelecimento Comercial </option> <option value='4'> Resid&ecirc;ncia </option> <option value='5'> Escolas </option> <option value='8'> Banco </option> <option value='9'> Farm&aacute;cia </option> <option value='10'> Posto de Gasolina </option> <option value='11'> Lot&eacute;rica </option> <option value='12'> Ve&iacute;culo </option> <option value='13'> Shopping </option> <option value='14'> Pra&ccedil;a P&uacute;blica </option> <option value='6'> Outros </option> ";
	if(tipoCrime=='1' || tipoCrime=='4'){
		htmlTipoVitima = "<option value='1'> &Agrave; Pessoa </option> <option value='2'> &Agrave; Propriedade </option>";
		htmTextoVitima = prefs.getMsg("alerta.tipo.roubo");
	}	
	if(tipoCrime=='2' || tipoCrime=='3'){
		htmlTipoVitima = "<option value='1'> &Agrave; Pessoa </option> <option value='2'> &Agrave; Propriedade </option>";
		htmTextoVitima = prefs.getMsg("alerta.tipo.furto");
	}
	if(tipoCrime=='5'){
		htmlTipoVitima = "<option value='3'> Rixas ou Brigas </option> <option value='4'>Viol&ecirc;ncia Dom&eacute;stica </option> <option value='5'> Abuso de Autoridade </option> <option value='6'> Homic&iacute;dio</option> <option value='7'> Tentativa de Homic&iacute;dio </option> <option value='8'> Latroc&iacute;nio </option>";
		htmTextoVitima = prefs.getMsg("alerta.tipo.crime");
	}
		
	var html_alerta = "<table style='font-family:Arial, sans-serif;  font-size: 11px;'>";
	html_alerta+="<tr> <td colspan = '2' > <b> "+ htmTextoVitima +":(*) </b> </td> <td colspan = '2' > <b>"+ prefs.getMsg("alerta.tipo.local")+":(*) </b> </td>  </tr>";
	html_alerta+="<tr> <td colspan = '2' > <div style='color:red;' id = 'erroTipoVitima'> </div> </td>  <td colspan = '2' > <div style='color:red;' id = 'erroTipoLocal'> </div> </td>  </tr>";
	html_alerta+="<tr> <td colspan = '2' > <select style='font-size: 12px; border: 1px solid #2763a5;' name='tipoVitima' id='tipoVitima'> <option value=''>"+prefs.getMsg("selecione")+"</option> "+ htmlTipoVitima +" </select> </td> <td colspan = '2' > <select style='font-size: 12px; border: 1px solid #2763a5;' name='tipoLocal' id='tipoLocal'> <option value=''>"+prefs.getMsg("selecione")+"</option>"+ htmlTipoLocal +"</select> </td>  </tr>";
	html_alerta+="<tr> <td colspan = '2' > <b> "+ prefs.getMsg("alerta.tipo.data")+":(*) </b> </td> <td colspan = '2' > <b> "+ prefs.getMsg("alerta.tipo.horario")+":(*) </b> </td>  </tr>";
	html_alerta+="<tr> <td colspan = '2' > <div style='color:red;' id = 'erroData'> </div> </td>  <td colspan = '2' > <div style='color:red;' id = 'erroHorario'> </div> </td>  </tr>";
	html_alerta+="<tr> <td colspan = '2' > <input type='text' style='font-size: 12px; border: 1px solid #2763a5;' id='data' name='data' size='8'/>(dd/MM/aaaa)</td> <td colspan = '2' > <select style='font-size: 12px; border: 1px solid #2763a5;' name='horario' id='horario'> <option value=''>"+prefs.getMsg("selecione")+"</option> <option value='0'> 00:00 </option> <option value='1'> 01:00 </option> <option value='2'> 02:00 </option> <option value='3'> 03:00 </option> <option value='4'> 04:00 </option> <option value='5'> 05:00 </option> <option value='6'> 06:00 </option> <option value='6'> 07:00 </option> <option value='8'> 08:00 </option> <option value='9'> 09:00 </option> <option value='10'> 10:00 </option> <option value='11'> 11:00 </option> <option value='12'> 12:00 </option> <option value='13'> 13:00 </option> <option value='14'> 14:00 </option> <option value='15'> 15:00 </option> <option value='16'> 16:00 </option> <option value='17'> 17:00 </option> <option value='18'> 18:00 </option> <option value='19'> 19:00 </option> <option value='20'> 20:00 </option> <option value='21'> 21:00 </option> <option value='22'> 22:00 </option> <option value='23'> 23:00 </option> </select> </td>  </tr>";
	html_alerta+="<tr> <td colspan = '2' > <b> "+ prefs.getMsg("alerta.qtd.vitimas")+":(*) </b> </td> <td colspan = '2' > <b> "+ prefs.getMsg("alerta.qtd.criminosos")+":(*) </b> </td>  </tr>";
	html_alerta+="<tr> <td colspan = '2' > <div style='color:red;' id = 'erroQtdV'> </div> </td>  <td colspan = '2' > <div style='color:red;' id = 'erroQtdC'> </div> </td>  </tr>";
	html_alerta+="<tr> <td colspan = '2' > <input style='font-size: 12px; border: 1px solid #2763a5;' type='text' id='qtdV' name='qtdV' size='2' maxlength='2'/></td> <td colspan = '2' > <input style='font-size: 12px; border: 1px solid #2763a5;' type='text' id='qtdC' name='qtdC' size='2' maxlength='2'/> </td>  </tr>";
	html_alerta+="<tr> <td colspan = '4' > <b> "+ prefs.getMsg("alerta.tipo.arma.usada")+":(*) </b> </td></tr>";
	html_alerta+="<tr> <td colspan = '4' > <div style='color:red;' id = 'erroArmaUsada'> </div> </td> </tr>";
	html_alerta+="<tr> <td colspan = '4' > <select style='font-size: 12px; border: 1px solid #2763a5;' name='armaUsada' id='armaUsada'> <option value=''>"+prefs.getMsg("selecione")+"</option> <option value='1'>N&atilde;o</option> <option value='2'>Fogo</option> <option value='3'>Branca</option> <option value='4'>N&atilde;o Sei</option> </select> </td> </tr>"
	html_alerta+="<tr> <td colspan = '4' > <b> "+ prefs.getMsg("alerta.tipo.relacao.crime")+"(*) </b> </td></tr>";
	html_alerta+="<tr> <td colspan='4'> <input type='radio' id='rcrime' onclick='setRadioRCrime(this.value)' name='rcrime' value='1' checked> V&iacute;tima <input type='radio' onclick='setRadioRCrime(this.value)' name='rcrime' value='2' > Testemunha <input type='radio' onclick='setRadioRCrime(this.value)' name='rcrime' value='3'> Tive Conhecimento </td> </tr>"
	html_alerta+="<tr> <td colspan = '4' > <b>  "+ prefs.getMsg("alerta.tipo.pol.info")+"(*) </b> </td></tr>";
	html_alerta+="<tr> <td colspan='4'> <input type='radio' onclick='setRadioPolInfo(this.value)' id='polinfo' name='polinfo' value='1' checked> Sim (190) <input type='radio' id='polinfo' onclick='setRadioPolInfo(this.value)' name='polinfo' value='2' > Sim (Delegacia) <input type='radio' id='polinfo' name='polinfo' onclick='setRadioPolInfo(this.value)' value='3'> N&atilde;o <input type='radio' id='polinfo' onclick='setRadioPolInfo(this.value)' name='polinfo' value='4'> N&atilde;o Sei</td> </tr>"	
	html_alerta+="<tr> <td colspan = '4' > <b> "+prefs.getMsg("desc.alerta")+":(*) </b> </td>  </tr>";
	html_alerta+="<tr> <td colspan = '4' > <div style='color:red;' id = 'erroDesc'> </div> </td>  </tr>";
	html_alerta+="<tr><td colspan = '4'> <TEXTAREA NAME='desc_alerta' id='desc_alerta' style='font-size: 12px; border: 1px solid #2763a5;' COLS='38' ROWS='6'></TEXTAREA> </td></tr>";
	html_alerta+="<tr> <td colspan = '4'><b> "+prefs.getMsg("causa.motivo.ocorrencia1")+" <br/> "+prefs.getMsg("causa.motivo.ocorrencia2")+":(*) </b> </td>  </tr>";
	html_alerta+="<tr> <td colspan = '4' > <div style='color:red;' id = 'erroRazoes'> </div> </td>  </tr>";
	html_alerta+="<tr> <td>	<input style='font-size: 12px; border: 1px solid #2763a5;' type='checkbox' id='razao1' name='cIluP'/> </td> <td > "+prefs.getMsg("ma.iluminacao.publica")+" </td> <td> <input type='checkbox' id='razao2' name='cFalLazJovens'/> </td> <td > "+prefs.getMsg("ausencia.lazer.jovens")+" </td> </tr>";
	html_alerta+="<tr> <td> <input type='checkbox' id='razao3' name='cDesReg'/> </td> <td > "+prefs.getMsg("desemprego.regiao")+" </td> <td> <input type='checkbox' id='razao4' name='cFacilAcesFuga'/> </td> <td > "+prefs.getMsg("facil.acesso.fuga")+" </td> </tr>";
	html_alerta+="<tr> <td> <input type='checkbox' id='razao5'	name='cDisGan'/> </td> <td > "+prefs.getMsg("disputa.gangues")+" </td> <td> <input type='checkbox' id='razao6' name='cUsoTrafDrog'/> </td> <td > "+prefs.getMsg("trafico.drogas")+" </td> </tr>";
	html_alerta+="<tr> <td> <input type='checkbox' id='razao7' name='cUsoAcl'/> </td> <td > "+prefs.getMsg("uso.alcool")+" </td> <td> <input type='checkbox' id='razao8' name='cCriRuas'/> </td> <td > "+prefs.getMsg("criancas.ruas")+" </td> </tr>";
	html_alerta+="<tr> <td> <input type='checkbox' id='razao9'	name='cAltConPes'/> </td> <td > "+prefs.getMsg("alta.concentracao.pessoas")+" </td> <td> <input type='checkbox' id='razao10' name='cFalPol'/> </td> <td > "+prefs.getMsg("falta.policiamento")+" </td> </tr>";
	html_alerta+="<tr> <td> <input type='checkbox' id='razao11' name='cOmisTest'/> </td> <td > "+prefs.getMsg("omissao.testemunhas")+" </td> <td> <input type='checkbox' id='razao12' name='cProxRegPerig'/> </td> <td > "+prefs.getMsg("proximidade.regioes.perigosas")+" </td> </tr>";
	html_alerta+="<tr> <td> <input type='checkbox' id='razao13' name='cImpPen'/> </td> <td > "+prefs.getMsg("impunidade.penal")+" </td> <td> <input type='checkbox' id='razao14' name='cPistolagem'/> </td> <td > "+prefs.getMsg("pistolagem")+" </td> </tr>";
	html_alerta+="<tr> <td> <input type='checkbox' id='razao15' name='cVlcPol'/> </td> <td > "+prefs.getMsg("violencia.policial")+" </td> <td> <input type='checkbox' id='razao16' name='cFalMor'/> </td> <td > "+prefs.getMsg("falta.moradia")+" </td> </tr>";
	html_alerta+="<tr> <td> <input type='checkbox' id='razao17' name='cCriOrg'/> </td> <td > "+prefs.getMsg("crime.organizado")+" </td> <td> <input type='checkbox' id='razao18' name='cCriPas'/> </td> <td > "+prefs.getMsg("crime.passional")+" </td> </tr>";
	html_alerta+="<tr> <td> <input type='checkbox' id='razao19' name='cOutros'/> </td> <td > "+prefs.getMsg("outros")+" </td> <td> <input type='checkbox' id='razao20' name='cNaoSei'/> </td> <td > "+prefs.getMsg("nao.sei")+" </td> </tr>";					
	html_alerta+="<tr><td colspan = '4'> <input type='hidden' id='tipoCrime' value='"+tipoCrime+"'> <input style='font-family: Arial, Helvetica, sans-serif; font-size: 10px;	font-weight: bold; margin: 1; padding: 1; background-color: #6da6e2; color: white;	border: 1px solid #2763a5;' onclick='validaFormRegistroAlertaCrime();' type='button' value='"+prefs.getMsg("enviar")+"'/> </td></tr>";				
	html_alerta+="</table>";
	return html_alerta;
}				
//fun��o que constroi o html da tela de registro de alerta de Crime


function chamarTelaLogin(){
	var conteudo ='';
	conteudo +='<div align="center" style="border: 1px solid #2763a5;width: 260px">';
	conteudo +='	<table style="font-family:Arial, sans-serif;  font-size: 11px;" cellpadding="1" cellspacing="1"  align="center"';
	conteudo +='		<tr>';
	conteudo +='			<td align="center" colspan="2">';
	conteudo +='				'+prefs.getMsg("informe.login.senha");
	conteudo +='			</td>';
	conteudo +='		</tr>';					
	conteudo +='		<tr>';
	conteudo +='			<td align="center" colspan="2">';
	conteudo +='				<div style="color:red;" id="content_erro"> </div>';
	conteudo +='			</td>';
	conteudo +='		</tr>';
	conteudo +='		<tr>';
	conteudo +='			<td align="center">';
	conteudo +='				E-mail:';
	conteudo +='			</td>';
	conteudo +='			<td align="center">';
	conteudo +='				<input id="login" style="font-size: 12px; border: 1px solid #2763a5;" type="text" />';
	conteudo +='			</td>';
	conteudo +='		</tr>';
	conteudo +='		<tr>';
	conteudo +='			<td align="center">';
	conteudo +='				'+prefs.getMsg("senha")+':';
	conteudo +='			</td>';
	conteudo +='			<td align="center">';
	conteudo +='				<input id="senha" style="font-size: 12px; border: 1px solid #2763a5;" type="password" />';
	conteudo +='			</td>';
	conteudo +='		</tr>';
	conteudo +='		<tr>';
	conteudo +='			<td align="center" colspan="2">';
	conteudo +='				'+prefs.getMsg("deseja.amigos.ver.crimes");
	conteudo +='			</td>';					
	conteudo +='		</tr>';
	conteudo +='		<tr>';
	conteudo +='			<td align="center">';
	conteudo +='				<input type="radio" onclick="selecionouSim()" id="vCrimes" name="vCrimes" value="1" checked/> '+prefs.getMsg("sim");
	conteudo +='			</td>';
	conteudo +='			<td align="center">';
	conteudo +='				<input type="radio" onclick="selecionouNao()" id="vCrimes" name="vCrimes" value="0" /> '+prefs.getMsg("nao");
	conteudo +='			</td>';
	conteudo +='		</tr>';					
	conteudo +='		<tr>';
	conteudo +='			<td align="center">';
	conteudo +='				<input style="cursor: pointer;font-family: Arial, Helvetica, sans-serif; font-size: 10px;	font-weight: bold; margin: 1; padding: 1; background-color: #6da6e2; color: white;	border: 1px solid #2763a5;" type="button" value ="login" onclick="executaRequisicaoRealizaLogin();" />';
	conteudo +='			</td>';
	conteudo +='			<td align="center">';
	conteudo +='				<input style="cursor: pointer;font-family: Arial, Helvetica, sans-serif; font-size: 10px;	font-weight: bold; margin: 1; padding: 1; background-color: #6da6e2; color: white;	border: 1px solid #2763a5;" type="button" value="'+prefs.getMsg("voltar")+'" onclick="conteudoInstallApp();" />';
	conteudo +='			</td>';
	conteudo +='		</tr>';				
	conteudo +='	</table>';
	conteudo +='</div>';
	document.getElementById("bodyContent").innerHTML = conteudo;
}

function registrarUsuario(){
	var conteudo ='';
	conteudo +='<div align="center" style="border: 1px solid #2763a5;width: 350px">';
	conteudo +='	<table style="font-family:Arial, sans-serif;  font-size: 11px;" cellpadding="1" cellspacing="1"  align="center"';
	conteudo +='		<tr>';
	conteudo +='			<td align="center" colspan="2">';
	conteudo +='				'+prefs.getMsg("informe.cidade.pais");
	conteudo +='			</td>';
	conteudo +='		</tr>';					
	conteudo +='		<tr>';
	conteudo +='			<td align="center" colspan="2">';
	conteudo +='				<div style="color:red;" id="content_erro"> </div>';
	conteudo +='			</td>';
	conteudo +='		</tr>';
	conteudo +='		<tr>';
	conteudo +='			<td align="center">';
	conteudo +='				'+prefs.getMsg("cidade")+':';
	conteudo +='			</td>';
	conteudo +='			<td align="center">';
	conteudo +='				<input id="cidade" style="font-size: 12px; border: 1px solid #2763a5;" type="text" value="' + usuarioRedeSocial.cidade + '" />';
	conteudo +='			</td>';
	conteudo +='		</tr>';
	conteudo +='		<tr>';
	conteudo +='			<td align="center">';
	conteudo +='				'+prefs.getMsg("pais")+':';
	conteudo +='			</td>';
	conteudo +='			<td align="center">';
	conteudo +='			<select id="pais" name="pais" style="font-size: 12px; border: 1px solid #2763a5;" type="text" size="1"><option value="" selected="selected">'+prefs.getMsg("selecione")+'</option>	<option value="BR">Brasil</option> <option value="AF">Afeganist&#227;o</option>	<option value="AL">Alb&#226;nia</option>	<option value="DE">Alemanha</option>	<option value="AD">Andorra</option>	<option value="AO">Angola</option>	<option value="AI">Anguilla</option>	<option value="AN">Antilhas Holandesas</option>	<option value="AQ">Ant&#225;rtida</option>	<option value="AG">Ant&#237;gua e Barbuda</option>	<option value="AR">Argentina</option>	<option value="DZ">Arg&#233;lia</option>	<option value="AM">Arm&#234;nia</option>	<option value="AW">Aruba</option>	<option value="SA">Ar&#225;bia Saudita</option>	<option value="AU">Austr&#225;lia</option>	<option value="AZ">Azerbaij&#227;o</option>	<option value="BS">Bahamas</option>	<option value="BD">Bangladesh</option>	<option value="BB">Barbados</option>	<option value="BH">Bareine</option>	<option value="BY">Belarus</option>	<option value="BZ">Belize</option>	<option value="BJ">Benin</option>	<option value="BM">Bermudas</option>	<option value="BO">Bol&#237;via</option>	<option value="BW">Botsuana</option>		<option value="BN">Brunei</option>	<option value="BG">Bulg&#225;ria</option>	<option value="BF">Burquina Faso</option>	<option value="BI">Burundi</option>	<option value="BT">But&#227;o</option>	<option value="BE">B&#233;lgica</option>	<option value="BA">B&#243;snia-Herzeg&#243;vina</option>	<option value="CV">Cabo Verde</option>	<option value="KH">Camboja</option>	<option value="CA">Canad&#225;</option>	<option value="KZ">Casaquist&#227;o</option>	<option value="QA">Catar</option>	<option value="TD">Chade</option>	<option value="CL">Chile</option>	<option value="CN">China</option>	<option value="CY">Chipre</option>	<option value="SG">Cingapura</option>	<option value="CO">Col&#244;mbia</option>	<option value="KM">Comores</option>	<option value="CG">Congo</option>	<option value="CD">Congo, Rep. Dem. do</option>	<option value="KP">Cor&#233;ia, Norte</option>	<option value="KR">Cor&#233;ia, Sul</option>	<option value="CR">Costa Rica</option>	<option value="CI">Costa do Marfim</option>	<option value="HR">Cro&#225;cia</option>	<option value="CU">Cuba</option>	<option value="DK">Dinamarca</option>	<option value="DJ">Djibuti</option>	<option value="DM">Dominica</option>	<option value="EG">Egito</option>	<option value="SV">El Salvador</option>	<option value="AE">Emirados &#193;rabes Unidos</option>	<option value="EC">Equador</option>	<option value="ER">Eritr&#233;ia</option>	<option value="SK">Eslov&#225;quia</option>	<option value="SI">Eslov&#234;nia</option>	<option value="ES">Espanha</option>	<option value="US">Estados Unidos</option>	<option value="EE">Est&#244;nia</option>	<option value="ET">Eti&#243;pia</option>	<option value="FJ">Fiji</option>	<option value="PH">Filipinas</option>	<option value="FI">Finl&#226;ndia</option>	<option value="FR">Fran&#231;a</option>	<option value="GA">Gab&#227;o</option>	<option value="GH">Gana</option>	<option value="GE">Ge&#243;rgia</option>	<option value="GS">Ge&#243;rgia do Sul</option>	<option value="GI">Gibraltar</option>	<option value="GD">Granada</option>	<option value="GL">Gro&#234;nlandia</option>	<option value="GR">Gr&#233;cia</option>	<option value="GP">Guadalupe</option>	<option value="GU">Guam</option>	<option value="GT">Guatemala</option>	<option value="GY">Guiana</option>	<option value="GF">Guiana Francesa</option>	<option value="GN">Guin&#233;</option>	<option value="GW">Guin&#233; Bissau</option>	<option value="GQ">Guin&#233; Equatorial</option>	<option value="GM">G&#226;mbia</option>	<option value="HT">Haiti</option>	<option value="HN">Honduras</option>	<option value="HK">Hong Kong</option>	<option value="HU">Hungria</option>	<option value="BV">Ilha Bouvet</option>	<option value="HM">Ilha Heard e Ilhas McDonald</option>	<option value="NF">Ilha Norfolk</option>	<option value="KY">Ilhas Caiman</option>	<option value="CC">Ilhas Cocos (Keeling)</option>	<option value="CK">Ilhas Cook</option>	<option value="FO">Ilhas Faroe</option>	<option value="FK">Ilhas Malvinas</option>	<option value="MP">Ilhas Marianas do Norte</option>	<option value="MH">Ilhas Marshall</option>	<option value="UM">Ilhas Menores dos EUA</option>	<option value="CX">Ilhas Natal</option>	<option value="SB">Ilhas Salom&#227;o</option>	<option value="TC">Ilhas Turks e Caicos</option>	<option value="VG">Ilhas Virgens Brit&#226;nicas</option>	<option value="VI">Ilhas Virgens dos EUA</option>	<option value="ID">Indon&#233;sia</option>	<option value="IQ">Iraque</option>	<option value="IE">Irlanda</option>	<option value="IR">Ir&#227;</option>	<option value="IS">Isl&#226;ndia</option>	<option value="IL">Israel</option>	<option value="IT">It&#225;lia</option>	<option value="YE">I&#234;men</option>	<option value="JM">Jamaica</option>	<option value="JP">Jap&#227;o</option>	<option value="JO">Jord&#226;nia</option>	<option value="KW">Kuwait</option>	<option value="LS">Lesoto</option>	<option value="LV">Let&#244;nia</option>	<option value="LR">Lib&#233;ria</option>	<option value="LI">Liechtenstein</option>	<option value="LT">Litu&#226;nia</option>	<option value="LU">Luxemburgo</option>	<option value="LB">L&#237;bano</option>	<option value="LY">L&#237;bia</option>	<option value="MO">Macau</option>	<option value="MK">Maced&#244;nia, Rep&#250;blica da</option>	<option value="MG">Madagascar</option>	<option value="MW">Malawi</option>	<option value="MV">Maldivas</option>	<option value="ML">Mali</option>	<option value="MT">Malta</option>	<option value="MY">Mal&#225;sia</option>	<option value="MA">Marrocos</option>	<option value="MQ">Martinica</option>	<option value="MR">Maurit&#226;nia</option>	<option value="MU">Maur&#237;cio</option>	<option value="YT">Mayotte</option>	<option value="MM">Mianm&#225;</option>	<option value="FM">Micron&#233;sia</option>	<option value="MD">Moldova, Rep&#250;blica de</option>	<option value="MN">Mong&#243;lia</option>	<option value="ME">Montenegro</option>	<option value="MS">Montserrat</option>	<option value="MZ">Mo&#231;ambique</option>	<option value="MX">M&#233;xico</option>	<option value="MC">M&#244;naco</option>	<option value="NA">Nam&#237;bia</option>	<option value="NR">Nauru</option>	<option value="NP">Nepal</option>	<option value="NI">Nicar&#225;gua</option>	<option value="NG">Nig&#233;ria</option>	<option value="NU">Niue</option>	<option value="NO">Noruega</option>	<option value="NC">Nova Caled&#244;nia</option>	<option value="NZ">Nova Zel&#226;ndia</option>	<option value="NE">N&#237;ger</option>	<option value="OM">Om&#227;</option>	<option value="PW">Palau</option>	<option value="PA">Panam&#225;</option>	<option value="PG">Papua-Nova Guin&#233;</option>	<option value="PK">Paquist&#227;o</option>	<option value="PY">Paraguai</option>	<option value="NL">Pa&#237;ses Baixos</option>	<option value="PE">Peru</option>	<option value="PN">Pitcairn</option>	<option value="PF">Polin&#233;sia Francesa</option>	<option value="PL">Pol&#244;nia</option>	<option value="PR">Porto Rico</option>	<option value="PT">Portugal</option>	<option value="KG">Quirguist&#227;o</option>	<option value="KI">Quiribati</option>	<option value="KE">Qu&#234;nia</option>	<option value="GB">Reino Unido</option>	<option value="CF">Rep&#250;blica Centro-Africana</option>	<option value="LA">Rep&#250;blica de Lao</option>	<option value="DO">Rep&#250;blica Dominicana</option>	<option value="CZ">Rep&#250;blica Tcheca</option>	<option value="CM">Rep&#250;blica dos Camar&#245;es</option>	<option value="RE">Reuni&#227;o</option>	<option value="RO">Rom&#234;nia</option>	<option value="RW">Ruanda</option>	<option value="RU">R&#250;ssia</option>	<option value="EH">Saara Ocidental</option>	<option value="PM">Saint Pierre e Miquelon</option>	<option value="WS">Samoa</option>	<option value="AS">Samoa Americana</option>	<option value="SM">San Marino</option>	<option value="SH">Santa Helena</option>	<option value="LC">Santa L&#250;cia</option>	<option value="SN">Senegal</option>	<option value="RS">Serbia</option>	<option value="SL">Serra Leoa</option>	<option value="SC">Seychelles</option>	<option value="SO">Som&#225;lia</option>	<option value="LK">Sri Lanka</option>	<option value="SZ">Suazil&#226;ndia</option>	<option value="SD">Sud&#227;o</option>	<option value="SR">Suriname</option>	<option value="SE">Su&#233;cia</option>	<option value="CH">Su&#237;&#231;a</option>	<option value="SJ">Svalbard e Jan Mayen</option>	<option value="KN">S&#227;o Cristov&#227;o e Nevis</option>	<option value="ST">S&#227;o Tom&#233; e Pr&#237;ncipe</option>	<option value="VC">S&#227;o Vicente e Granadinas</option>	<option value="CS">S&#233;rvia e Montenegro</option>	<option value="SY">S&#237;ria</option>	<option value="TJ">Tadjiquist&#227;o</option>	<option value="TH">Tail&#226;ndia</option>	<option value="TW">Taiwan</option>	<option value="TZ">Tanz&#226;nia</option>	<option value="IO">Ter. Brit. do Oc. &#205;ndico</option>	<option value="PS">Territ&#243;rio da Palestina</option>	<option value="TF">Territ&#243;rios Franceses do Sul</option>	<option value="TL">Timor Leste</option>	<option value="TG">Togo</option>	<option value="TK">Tokelau</option>	<option value="TO">Tonga</option>	<option value="TT">Trinidad e Tobago</option>	<option value="TN">Tun&#237;sia</option>	<option value="TM">Turcomenist&#227;o</option>	<option value="TR">Turquia</option>	<option value="TV">Tuvalu</option>	<option value="UA">Ucr&#226;nia</option>	<option value="UG">Uganda</option>	<option value="UY">Uruguai</option>	<option value="UZ">Uzbequist&#227;o</option>	<option value="VU">Vanuatu</option>	<option value="VA">Vaticano</option>	<option value="VE">Venezuela</option>	<option value="VN">Vietn&#227;</option>	<option value="WF">Wallis e Futuna</option>	<option value="ZW">Zimb&#225;bwe</option>	<option value="ZM">Z&#226;mbia</option>	<option value="ZA">&#193;frica do Sul</option>	<option value="AT">&#193;ustria</option>	<option value="AX">&#197;land Islands</option>	<option value="IN">&#205;ndia</option></select>'
	conteudo +='			</td>';
	conteudo +='		</tr>';					
	conteudo +='		<tr>';
	conteudo +='			<td align="center">';
	conteudo +='				<input style="cursor: pointer;font-family: Arial, Helvetica, sans-serif; font-size: 10px;	font-weight: bold; margin: 1; padding: 1; background-color: #6da6e2; color: white;	border: 1px solid #2763a5;" type="button" value ="'+prefs.getMsg("enviar")+'" onclick="getCityLatLng();" />';
	conteudo +='			</td>';					
	conteudo +='		</tr>';				
	conteudo +='	</table>';
	conteudo +='</div>';
	document.getElementById("bodyContent").innerHTML = conteudo;
}

function registrarUsuarioWikiCrimes(){
	var conteudo ='';
	conteudo +='<div align="center" style="border: 1px solid #2763a5; width: 450px">';
	conteudo +='	<table style="font-family:Arial, sans-serif;  font-size: 11px;" cellpadding="1" cellspacing="1"  align="center"><tbody><tr><td>';
	conteudo +='		</p></td></tr>';
	conteudo +='<tr><td><form id="usuarioForm" name="usuarioForm" method="post" enctype="application/x-www-form-urlencoded"><input type="hidden" id="usuarioForm:latUsuario" name="usuarioForm:latUsuario" value="" /><input type="hidden" id="usuarioForm:lngUsuario" name="usuarioForm:lngUsuario" value="" /><span style="text-align:left"><table border="0" style="text-align:left"><tbody><tr><td><span id="usuarioForm:primeiroNomeoutput">*Nome:</span></td><td>*Sobrenome:</td></tr>';
	conteudo +='<tr><td><input id="primeiroNome" name="primeiroNome" type="text" value="" style="font-size: 12px; border: 1px solid #2763a5;"/></td><td><input id="ultimoNome" name="ultimoNome" type="text" value="" style="font-size: 12px; border: 1px solid #2763a5;"/></td></tr>';
	conteudo +='';
	conteudo +='<tr><td></td><td></td></tr>';
	conteudo +='<tr><td></td><td></td></tr>';
	conteudo +='</tbody></table></span><table border="0" style="text-align:left"><tbody><tr><td>*E-Mail:</td></tr>';
	conteudo +='<tr><td><input id="email" name="email" type="text" value="" size="40" style="font-size: 12px; border: 1px solid #2763a5;"/></td></tr>';
	conteudo +='<tr><td></td></tr>';
	conteudo +='		<tr>';
	conteudo +='			<td align="center" colspan="2">';
	conteudo +='				<div style="color:red;" id="content_erro"> </div>';
	conteudo +='			</td>';
	conteudo +='		</tr>';
	conteudo +='</tbody></table><table border="0" style="text-align:left"><tbody><tr><td>*Repita seu E-Mail:</td></tr>';
	conteudo +='<tr><td><input id="usuarioForm:confemail" name="usuarioForm:confemail" type="text" value="" size="40" style="font-size: 12px; border: 1px solid #2763a5;"/></td></tr>';
	conteudo +='<tr><td></td></tr>';
	conteudo +='</tbody></table><table border="0" style="text-align:left"><tbody><tr><td><table border="0" style="text-align:left"><tbody><tr><td>*Senha:</td></tr>';
	conteudo +='<tr><td><input type="password" id="senha" name="senha" style="font-size: 12px; border: 1px solid #2763a5;"/></td></tr>';
	conteudo +='<tr><td></td></tr>';
	conteudo +='</tbody></table></td><td><table border="0" style="text-align:left"><tbody><tr><td>*Confirma&#231;&#227;o de Senha:</td></tr>';
	conteudo +='<tr><td><input type="password" id="usuarioForm:confsenha" name="usuarioForm:confsenha" style="font-size: 12px; border: 1px solid #2763a5;"/></td></tr>';
	conteudo +='<tr><td></td></tr>';
	conteudo +='</tbody></table></td></tr>';
	conteudo +='</tbody></table><table style=""><tbody><tr><td>*Pa&#237;s:</td></tr>';
	conteudo +='<tr><td><select id="pais" name="pais" size="1" onchange="showEstado(this.value, true);">	<option value="">Selecione...</option>	<option value="AF">Afghanistan</option>	<option value="AL">Albania</option>	<option value="DZ">Algeria</option>	<option value="AS">American Samoa</option>	<option value="AD">Andorra</option>	<option value="AO">Angola</option>	<option value="AI">Anguilla</option>	<option value="AQ">Antarctica</option>	<option value="AG">Antigua and Barbuda</option>	<option value="AR">Argentina</option>	<option value="AM">Armenia</option>	<option value="AW">Aruba</option>	<option value="AU">Australia</option>	<option value="AT">Austria</option>	<option value="AZ">Azerbaijan</option>	<option value="BS">Bahamas</option>	<option value="BH">Bahrain</option>	<option value="BD">Bangladesh</option>	<option value="BB">Barbados</option>	<option value="BY">Belarus</option>	<option value="BE">Belgium</option>	<option value="BZ">Belize</option>	<option value="BJ">Benin</option>	<option value="BM">Bermuda</option>	<option value="BT">Bhutan</option>	<option value="BO">Bolivia</option>	<option value="BA">Bosnia and Herzegovina</option>	<option value="BW">Botswana</option>	<option value="BV">Bouvet Island</option>	<option value="BR" selected="selected">Brasil</option>	<option value="IO">British Indian Ocean Territory</option>	<option value="VG">British Virgin Islands</option>	<option value="BN">Brunei</option>	<option value="BG">Bulgaria</option>	<option value="BF">Burkina Faso</option>	<option value="BI">Burundi</option>	<option value="KH">Cambodia</option>	<option value="CM">Cameroon</option>	<option value="CA">Canada</option>	<option value="CV">Cape Verde</option>	<option value="KY">Cayman Islands</option>	<option value="CF">Central African Republic</option>	<option value="TD">Chad</option>	<option value="CL">Chile</option>	<option value="CN">China</option>	<option value="CX">Christmas Island</option>	<option value="CC">Cocos Islands</option>	<option value="CO">Colombia</option>	<option value="KM">Comoros</option>	<option value="CG">Congo</option>	<option value="CK">Cook Islands</option>	<option value="CR">Costa Rica</option>	<option value="HR">Croatia</option>	<option value="CU">Cuba</option>	<option value="CY">Cyprus</option>	<option value="CZ">Czech Republic</option>	<option value="CI">C&#244;te d Ivoire</option>	<option value="DK">Denmark</option>	<option value="DJ">Djibouti</option>	<option value="DM">Dominica</option>	<option value="DO">Dominican Republic</option>	<option value="EC">Ecuador</option>	<option value="EG">Egypt</option>	<option value="SV">El Salvador</option>	<option value="GQ">Equatorial Guinea</option>	<option value="ER">Eritrea</option>	<option value="EE">Estonia</option>	<option value="ET">Ethiopia</option>	<option value="FK">Falkland Islands</option>	<option value="FO">Faroe Islands</option>	<option value="FJ">Fiji</option>	<option value="FI">Finland</option>	<option value="FR">France</option>	<option value="GF">French Guiana</option>	<option value="PF">French Polynesia</option>	<option value="TF">French Southern Territories</option>	<option value="GA">Gabon</option>	<option value="GM">Gambia</option>	<option value="GE">Georgia</option>	<option value="DE">Germany</option>	<option value="GH">Ghana</option>	<option value="GI">Gibraltar</option>	<option value="GR">Greece</option>	<option value="GL">Greenland</option>	<option value="GD">Grenada</option>	<option value="GP">Guadeloupe</option>	<option value="GU">Guam</option>	<option value="GT">Guatemala</option>	<option value="GN">Guinea</option>	<option value="GW">Guinea-Bissau</option>	<option value="GY">Guyana</option>	<option value="HT">Haiti</option>	<option value="HM">Heard Island And McDonald Islands</option>	<option value="HN">Honduras</option>	<option value="HK">Hong Kong</option>	<option value="HU">Hungary</option>	<option value="IS">Iceland</option>	<option value="IN">India</option>	<option value="ID">Indonesia</option>	<option value="IR">Iran</option>	<option value="IQ">Iraq</option>	<option value="IE">Ireland</option>	<option value="IL">Israel</option>	<option value="IT">Italy</option>	<option value="JM">Jamaica</option>	<option value="JP">Japan</option>	<option value="JO">Jordan</option>	<option value="KZ">Kazakhstan</option>	<option value="KE">Kenya</option>	<option value="KI">Kiribati</option>	<option value="KW">Kuwait</option>	<option value="KG">Kyrgyzstan</option>	<option value="LA">Laos</option>	<option value="LV">Latvia</option>	<option value="LB">Lebanon</option>	<option value="LS">Lesotho</option>	<option value="LR">Liberia</option>	<option value="LY">Libya</option>	<option value="LI">Liechtenstein</option>	<option value="LT">Lithuania</option>	<option value="LU">Luxembourg</option>	<option value="MO">Macao</option>	<option value="MK">Macedonia</option>	<option value="MG">Madagascar</option>	<option value="MW">Malawi</option>	<option value="MY">Malaysia</option>	<option value="MV">Maldives</option>	<option value="ML">Mali</option>	<option value="MT">Malta</option>	<option value="MH">Marshall Islands</option>	<option value="MQ">Martinique</option>	<option value="MR">Mauritania</option>	<option value="MU">Mauritius</option>	<option value="YT">Mayotte</option>	<option value="MX">Mexico</option>	<option value="FM">Micronesia</option>	<option value="MD">Moldova</option>	<option value="MC">Monaco</option>	<option value="MN">Mongolia</option>	<option value="MS">Montserrat</option>	<option value="MA">Morocco</option>	<option value="MZ">Mozambique</option>	<option value="MM">Myanmar</option>	<option value="NA">Namibia</option>	<option value="NR">Nauru</option>	<option value="NP">Nepal</option>	<option value="NL">Netherlands</option>	<option value="AN">Netherlands Antilles</option>	<option value="NC">New Caledonia</option>	<option value="NZ">New Zealand</option>	<option value="NI">Nicaragua</option>	<option value="NE">Niger</option>	<option value="NG">Nigeria</option>	<option value="NU">Niue</option>	<option value="NF">Norfolk Island</option>	<option value="KP">North Korea</option>	<option value="MP">Northern Mariana Islands</option>	<option value="NO">Norway</option>	<option value="OM">Oman</option>	<option value="PK">Pakistan</option>	<option value="PW">Palau</option>	<option value="PS">Palestine</option>	<option value="PA">Panama</option>	<option value="PG">Papua New Guinea</option>	<option value="PY">Paraguay</option>	<option value="PE">Peru</option>	<option value="PH">Philippines</option>	<option value="PN">Pitcairn</option>	<option value="PL">Poland</option>	<option value="PT">Portugal</option>	<option value="PR">Puerto Rico</option>	<option value="QA">Qatar</option>	<option value="RE">Reunion</option>	<option value="RO">Romania</option>	<option value="RU">Russia</option>	<option value="RW">Rwanda</option>	<option value="SH">Saint Helena</option>	<option value="KN">Saint Kitts And Nevis</option>	<option value="LC">Saint Lucia</option>	<option value="PM">Saint Pierre And Miquelon</option>	<option value="VC">Saint Vincent And The Grenadines</option>	<option value="WS">Samoa</option>	<option value="SM">San Marino</option>	<option value="ST">Sao Tome And Principe</option>	<option value="SA">Saudi Arabia</option>	<option value="SN">Senegal</option>	<option value="CS">Serbia and Montenegro</option>	<option value="SC">Seychelles</option>	<option value="SL">Sierra Leone</option>	<option value="SG">Singapore</option>	<option value="SK">Slovakia</option>	<option value="SI">Slovenia</option>	<option value="SB">Solomon Islands</option>	<option value="SO">Somalia</option>	<option value="ZA">South Africa</option>	<option value="GS">South Georgia And The South Sandwich Islands</option>	<option value="KR">South Korea</option>	<option value="ES">Spain</option>	<option value="LK">Sri Lanka</option>	<option value="SD">Sudan</option>	<option value="SR">Suriname</option>	<option value="SJ">Svalbard And Jan Mayen</option>	<option value="SZ">Swaziland</option>	<option value="SE">Sweden</option>	<option value="CH">Switzerland</option>	<option value="SY">Syria</option>	<option value="TW">Taiwan</option>	<option value="TJ">Tajikistan</option>	<option value="TZ">Tanzania</option>	<option value="TH">Thailand</option>	<option value="CD">The Democratic Republic Of Congo</option>	<option value="TL">Timor-Leste</option>	<option value="TG">Togo</option>	<option value="TK">Tokelau</option>	<option value="TO">Tonga</option>	<option value="TT">Trinidad and Tobago</option>	<option value="TN">Tunisia</option>	<option value="TR">Turkey</option>	<option value="TM">Turkmenistan</option>	<option value="TC">Turks And Caicos Islands</option>	<option value="TV">Tuvalu</option>	<option value="VI">U.S. Virgin Islands</option>	<option value="UG">Uganda</option>	<option value="UA">Ukraine</option>	<option value="AE">United Arab Emirates</option>	<option value="GB">United Kingdom</option>	<option value="US">United States</option>	<option value="UM">United States Minor Outlying Islands</option>	<option value="UY">Uruguay</option>	<option value="UZ">Uzbekistan</option>	<option value="VU">Vanuatu</option>	<option value="VA">Vatican</option>	<option value="VE">Venezuela</option>	<option value="VN">Vietnam</option>	<option value="WF">Wallis And Futuna</option>	<option value="EH">Western Sahara</option>	<option value="YE">Yemen</option>	<option value="ZM">Zambia</option>	<option value="ZW">Zimbabwe</option>	<option value="AX">&#197;land Islands</option></select></td></tr>';
	conteudo +='';
	conteudo +='<tr><td></td></tr>';
	conteudo +='</tbody></table><table style=""><tbody><tr><td>Estado:</td></tr>';
	conteudo +='<tr><td><input id="inputEstado" name="inputEstado" type="text" value="" style="font-size: 12px; border: 1px solid #2763a5; display: none;"/></td></tr>';
	conteudo +='<tr><td><select id="comboEstado" name="comboEstado" size="1">	<option selected="selected" value="0">Selecione...</option>	<option value="AC">AC</option>	<option value="AL">AL</option>	<option value="AM">AM</option>	<option value="AP">AP</option>	<option value="BA">BA</option>	<option value="CE">CE</option>	<option value="DF">DF</option>	<option value="ES">ES</option>	<option value="GO">GO</option>	<option value="MA">MA</option>	<option value="MG">MG</option>	<option value="MS">MS</option>	<option value="MT">MT</option>	<option value="PA">PA</option>	<option value="PB">PB</option>	<option value="PE">PE</option>	<option value="PI">PI</option>	<option value="PR">PR</option>	<option value="RJ">RJ</option>	<option value="RN">RN</option>	<option value="RO">RO</option>	<option value="RR">RR</option>	<option value="RS">RS</option>	<option value="SC">SC</option>	<option value="SE">SE</option>	<option value="SP">SP</option>	<option value="TO">TO</option></select></td></tr>';
	conteudo +='<tr><td></td></tr>';
	conteudo +='</tbody></table><table style=""><tbody><tr><td>*Cidade:</td></tr>';
	conteudo +='<tr><td><input id="cidade" name="usuarioForm:cidade" type="text" value=' + usuarioRedeSocial.cidade + ' style="font-size: 12px; border: 1px solid #2763a5;"/></td></tr>';
	conteudo +='<tr><td></td></tr>';
	conteudo +='</tbody></table><table style=""><tbody><tr><td>*Deseja receber e-mails com novidades de WikiCrimes?</td></tr>';
	conteudo +='<tr><td><select id="receberNewsletter" name="receberNewsletter" size="1" style="font-size: 12px; border: 1px solid #2763a5;">	<option value="1" selected="selected">Sim</option>	<option value="0">N&#227;o</option></select></td></tr>';
	conteudo +='</tbody></table><table><tbody><tr><td>*Idioma Preferencial:</td></tr>';
	conteudo +='<tr><td><select id="idiomaPref" name="idiomaPref" size="1" style="font-size: 12px; border: 1px solid #2763a5;">	<option value="">Selecione...</option>	<option value="en">Ingl&#234;s</option>	<option value="pt" selected="selected">Portugu&#234;s</option>	<option value="es">Espa&#241;ol</option></select></td></tr>';
	conteudo +='</tbody></table><table style="font-size: 12px; border: 1px solid #2763a5;"><tbody><tr><td><textarea name="usuarioForm:termoUso" id="usuarioForm:termoUso" cols="60" readonly="readonly" rows="8" style="font-size: 12px; border: 1px solid #2763a5;">Caro USU&#193;RIO, leia, por favor, atentamente as condi&#231;&#245;es abaixo estipuladas para que voc&#234; possa usufruir dos servi&#231;os prestados pelo site e lembre-se que ao utiliz&#225;-los, voc&#234; estar&#225; declarando ter ci&#234;ncia do presente Termos e Condi&#231;&#245;es de Uso do Site e estar&#225; concordando com todas as suas cl&#225;usulas e condi&#231;&#245;es.';
	conteudo +='Caso voc&#234; n&#227;o concorde com qualquer disposi&#231;&#227;o destes Termos e Condi&#231;&#245;es de Uso, por favor, n&#227;o se registre em WikiCrimes para mapear crimes.';
	conteudo +='1. Dos Servi&#231;os';
	conteudo +='';
	conteudo +='1.1     WikiCrimes &#233; propriet&#225;rio e titular exclusivo dos';
	conteudo +='direitos do site www.wikicrimes.org e tamb&#233;m do dom&#237;nio www.wikicrimes.com site este que disponibiliza uma ferramenta para registro e de crimes ocorridos em uma certa regi&#227;o geogr&#225;fica. ';
	conteudo +='1.1.1 A qualquer momento poder&#227;o ser inclu&#237;das novas ferramentas, servi&#231;os e atividades no site bem como exclu&#237;das as j&#225; existentes, sem qualquer aviso pr&#233;vio ao USU&#193;RIO.';
	conteudo +='1.1.2 Toda e qualquer nova ferramenta ou atividade inclu&#237;da no Wikicrimes, estar&#225;, automaticamente, vinculada e subordinada a todos os termos e condi&#231;&#245;es do presente contrato.';
	conteudo +='';
	conteudo +='2. Quem pode utilizar os servi&#231;os';
	conteudo +='';
	conteudo +='.1     Os servi&#231;os est&#227;o dispon&#237;veis a qualquer pessoa f&#237;sica ou jur&#237;dica ';
	conteudo +='Nacional ou estrangeira, submetendo-se estas &#250;ltimas, desde j&#225;, &#224; legisla&#231;&#227;o brasileira.';
	conteudo +='';
	conteudo +='';
	conteudo +='3. Da Utiliza&#231;&#227;o do Site';
	conteudo +='';
	conteudo +='3.1     Wikicrimes &#233; um site de utilidade p&#250;blica e dessa forma,';
	conteudo +='seus servi&#231;os s&#227;o inteiramente gratuitos para os USU&#193;RIOS. Atrav&#233;s dele o USU&#193;RIO poder&#225; utilizar a ferramenta de pesquisa para consultar onde h&#225; crimes numa determinada regi&#227;o geogr&#225;fica. ';
	conteudo +='3.2     Para a utiliza&#231;&#227;o da ferramenta de pesquisa do site, n&#227;o &#233;';
	conteudo +='necess&#225;rio o cadastramento do USU&#193;RIO no site, por&#233;m, para as demais atividades como registrar um crime ou confirm&#225;-lo o cadastro &#233; obrigat&#243;rio.';
	conteudo +='';
	conteudo +='4. Do Cadastro do Usu&#225;rio';
	conteudo +='';
	conteudo +='4.1     Quando necess&#225;rio o cadastro no site, o USU&#193;RIO dever&#225;';
	conteudo +='manifestar o seu consentimento com o presente Termos e Condi&#231;&#245;es de Uso do Site eletronicamente, clicando no bot&#227;o &quot;eu concordo&quot;.';
	conteudo +='';
	conteudo +='4.2     Com a concord&#226;ncia expressa do USU&#193;RIO, caber&#225; a este efetivar';
	conteudo +='o seu cadastramento no site Wikicrimes. Fornecendo nos campos espec&#237;ficos, todas as informa&#231;&#245;es solicitadas para que ent&#227;o possa ser iniciada a livre utiliza&#231;&#227;o das ferramentas do Wikicrimes.';
	conteudo +='';
	conteudo +='4.3     Ao se cadastrar, o USU&#193;RIO dever&#225; escolher um nome de usu&#225;rio';
	conteudo +='(&quot;login&quot;)que devera ser seu e-mail e uma senha secreta, os quais ser&#227;o pessoais e intransfer&#237;veis e garantir&#227;o o acesso do USU&#193;RIO ao site do Wikicrimes, sendo poss&#237;vel navegar e participar das atividades em seu nome, acessar e alterar todas as suas informa&#231;&#245;es pessoais e cadastros.';
	conteudo +='';
	conteudo +='4.3.1 &#201; de inteira responsabilidade do USU&#193;RIO manter a senha em sigilo, sendo o &#250;nico e exclusivo respons&#225;vel por toda atividade realizada com a sua conta, para o que &#233; necess&#225;rio digitar sua senha secreta. O uso do nome de usu&#225;rio e senha &#233; imprescind&#237;vel para a utiliza&#231;&#227;o de alguns servi&#231;os do site.';
	conteudo +='';
	conteudo +='4.3.2 O USU&#193;RIO compromete-se a, sob nenhuma hip&#243;tese, ceder, emprestar ou revelar sua senha a terceiros.';
	conteudo +='';
	conteudo +='4.3.3 O USU&#193;RIO concorda e est&#225; obrigado a:';
	conteudo +='';
	conteudo +='(a) notificar imediatamente o Wikicrimes por escrito, sobre qualquer suspeita ou conhecimento de uso n&#227;o autorizado da sua senha, dados cadastrais, conta ou qualquer outra quebra de seguran&#231;a; e';
	conteudo +='(b) sair de sua conta de usu&#225;rio ao final de cada sess&#227;o e assegurar que esta n&#227;o seja acessada por terceiros n&#227;o autorizados. O Wikicrimes n&#227;o ser&#225; respons&#225;vel por qualquer perda ou dano decorrente do descumprimento do disposto nesta cl&#225;usula por parte do USU&#193;RIO.';
	conteudo +='';
	conteudo +='4.4     O USU&#193;RIO dever&#225; registrar-se em formul&#225;rio espec&#237;fico de';
	conteudo +='cadastro, fornecendo informa&#231;&#245;es verdadeiras, exatas e atuais sobre si mesmo, bem como dever&#225; conservar e atualizar referidas informa&#231;&#245;es imediatamente sempre que houver qualquer mudan&#231;a dos dados, a fim de mant&#234;-los verdadeiros, exatos e atuais.';
	conteudo +='';
	conteudo +='4.5     Para o cadastramento no site, &#233; expressamente proibida a';
	conteudo +='cria&#231;&#227;o de conta em nome de terceiros. O USU&#193;RIO se compromete a informar seu pr&#243;prio endere&#231;o eletr&#244;nico a ser utilizado para o recebimento de mensagens advindas de seu cadastro.';
	conteudo +='';
	conteudo +='4.6     Ser&#225; de total e &#250;nica responsabilidade do USU&#193;RIO quaisquer';
	conteudo +='preju&#237;zos decorridos pelo acesso n&#227;o desejado, por terceiros, &#224;s informa&#231;&#245;es inseridas e/ou disponibilizadas no site do Wikicrimes.';
	conteudo +='';
	conteudo +='4.7     O Wikicrimes compromete-se a n&#227;o utilizar as informa&#231;&#245;es';
	conteudo +='cadastrais fornecidas pelo USU&#193;RIO para a realiza&#231;&#227;o de quaisquer atividades il&#237;citas, mas somente para aquelas expressamente permitidas pela legisla&#231;&#227;o brasileira e/ou pelo presente instrumento.';
	conteudo +='';
	conteudo +='4.8     O USU&#193;RIO poder&#225; autorizar Wikicrimes por meio de campos';
	conteudo +='espec&#237;ficos no site, a locar o seu e-mail ou cadastro pessoal para terceiros, de determinado ramo de atividade escolhido pelo pr&#243;prio USU&#193;RIO, para o recebimento de correspond&#234;ncias f&#237;sicas e/ou eletr&#244;nicas (e-mail).';
	conteudo +='';
	conteudo +='4.9     O Wikicrimes fica desde j&#225; autorizado a usar e divulgar todo';
	conteudo +='conte&#250;do referente a ocorr&#234;ncias criminais criadas pelo USUARIO.';
	conteudo +='';
	conteudo +='4.10    Todos e quaisquer dados ou informa&#231;&#245;es fornecidas pelo USU&#193;RIO';
	conteudo +='A Wikicrimes, poder&#227;o ser imediatamente reveladas por este em cumprimento &#224; ordem judicial ou administrativa.';
	conteudo +='';
	conteudo +='5. Da Propriedade Intelectual e da Responsabilidade pelas Informa&#231;&#245;es';
	conteudo +='';
	conteudo +='5.1     Todo material e servi&#231;o encontrado nas p&#225;ginas do Wikicrimes (textos, imagens, &#225;udio, tecnologia, logotipos, slogans, marcas, express&#245;es de propaganda, dom&#237;nios, nomes comerciais, obras intelectuais), bem como os softwares que viabilizam as atividades, s&#227;o de exclusiva propriedade de Wikicrimes, que det&#233;m todos os direitos autorais, licenciamentos e direitos de propriedade, marca ou patente a eles relativos. Qualquer viola&#231;&#227;o dos direitos de propriedade pelo USU&#193;RIO resultar&#225; na sua responsabilidade, direta e pessoal, pelos atos praticados, nos termos da lei e com a aplica&#231;&#227;o das san&#231;&#245;es cab&#237;veis c&#237;veis e criminais.';
	conteudo +='';
	conteudo +='5.2     O USU&#193;RIO se compromete a n&#227;o reproduzir, duplicar, copiar ou';
	conteudo +='explorar os servi&#231;os prestados, softwares utilizados e/ou materiais disponibilizados pelo Wikicrimes para quaisquer fins n&#227;o expressamente autorizados no presente termo, sem sua autoriza&#231;&#227;o por escrito.';
	conteudo +='';
	conteudo +='5.3     O USU&#193;RIO declara-se plenamente ciente de que n&#227;o s&#227;o de';
	conteudo +='responsabilidade ou autoria do Wikicrimes, sob nenhuma hip&#243;tese:';
	conteudo +='(i) as informa&#231;&#245;es postadas no Wikicrimes juntamente com suas poss&#237;veis confirma&#231;&#245;es registradas no site.';
	conteudo +='';
	conteudo +='5.4     Nenhuma responsabilidade, de nenhuma esp&#233;cie, seja ela de';
	conteudo +='acessibilidade, veracidade, legalidade do conte&#250;do ou outra qualquer, poder&#225; ser imputada a Wikicrimes, quando o USU&#193;RIO, por meio do site, adentrar-se em sites ou links de propriedade, administra&#231;&#227;o e/ou controle de terceiros, tais como patrocinadores, parceiros, prestadores de servi&#231;os em geral, demais usu&#225;rios, etc., os quais ser&#227;o os &#250;nicos e exclusivos respons&#225;veis.';
	conteudo +='';
	conteudo +='5.5     Por sua vez, todo e qualquer material, dado ou informa&#231;&#227;o,';
	conteudo +='inclu&#237;dos pelo USU&#193;RIO em qualquer p&#225;gina do Wikicrimes, ser&#225; de plena e total responsabilidade deste, que responder&#225; exclusivamente por quaisquer preju&#237;zos que sejam causados a terceiros e/ou a Wikicrimes.';
	conteudo +='';
	conteudo +='5.6     O USU&#193;RIO n&#227;o poder&#225; incluir coment&#225;rios il&#237;citos no site do';
	conteudo +='Wikicrimes, de forma a atribuir a algu&#233;m a pr&#225;tica de crime, imputar a algu&#233;m fato ofensivo &#224; sua reputa&#231;&#227;o, e, ofender algu&#233;m atentando contra sua dignidade ou decoro.';
	conteudo +='';
	conteudo +='5.7     Ao utilizar o servi&#231;o do Wikicrimes, o USU&#193;RIO dever&#225;';
	conteudo +='pautar suas opini&#245;es e coment&#225;rios em conformidade com a lei e a moral, n&#227;o podendo usar o servi&#231;o para:';
	conteudo +='';
	conteudo +='(a) submeter, postar, ou transmitir por qualquer meio, conte&#250;do que seja difamat&#243;rio, calunioso, injurioso, abusivo, vulgar, obsceno, ou que de qualquer forma atente contra a moral e os bons costumes;';
	conteudo +='(b) submeter, postar ou transmitir por qualquer meio, conte&#250;do que infrinja ou viole direitos de terceiros, inclu&#237;dos direitos da personalidade e de propriedade intelectual;';
	conteudo +='(c) submeter, postar ou transmitir por qualquer meio, v&#237;rus, arquivos corrompidos, ou quaisquer outros programas que possam danificar, ainda que momentaneamente, a opera&#231;&#227;o de computador alheio;';
	conteudo +='(d) submeter, postar ou transmitir por qualquer meio, propaganda ou oferta de venda de produtos ou servi&#231;os com intuito comercial;';
	conteudo +='(e) submeter, postar ou transmitir por quaisquer meio, protestos, manifesta&#231;&#245;es pol&#237;tica ou religiosa, pir&#226;mides, esquemas, spams, etc.; e';
	conteudo +='(f) submeter, postar ou transmitir por qualquer meio, conte&#250;do que seja contr&#225;rio &#224; lei.';
	conteudo +='';
	conteudo +='5.8     Wikicrimes n&#227;o endossa as opini&#245;es e coment&#225;rios';
	conteudo +='ofensivos e duvidosos dos USU&#193;RIOS, mal intencionado, conseq&uuml;entemente, garantir a exatid&#227;o, a qualidade, ou a confiabilidade das opini&#245;es de pessoas que utilizam o site para denegrir a imagem de algu&#233;m,onde sempre que detectada ser&#225; retirada do Wikicrimes a qualquer momento.';
	conteudo +='';
	conteudo +='5.9    Caso chegue ao conhecimento do Wikicrimes qualquer viola&#231;&#227;o';
	conteudo +='&#224;s condi&#231;&#245;es acima, fica o USU&#193;RIO ciente de que poder&#225; ter suas opini&#245;es editadas a fim de descaracterizar qualquer das condutas acima elencadas. A referida edi&#231;&#227;o poder&#225; ocorrer a qualquer momento a partir do conhecimento do Wikicrimes e sem a pr&#233;via notifica&#231;&#227;o ao USU&#193;RIO. Fica certo que a opini&#227;o n&#227;o poder&#225; ter seu valor alterado, mas t&#227;o somente editada com o intuito de descaracterizar textos que estejam em desacordo com a lei e a moral.';
	conteudo +='';
	conteudo +='5.10   O USU&#193;RIO declara-se ciente e concorda que, ao transmitir e/ou';
	conteudo +='enviar a Wikicrimes qualquer material, informa&#231;&#227;o e/ou dados, estar&#225; automaticamente cedendo os direitos de uso e divulga&#231;&#227;o do mesmo a Wikicrimes e seus demais usu&#225;rios, que poder&#227;o acess&#225;-los livremente.';
	conteudo +='';
	conteudo +='5.11    Caso Wikicrimes venha a ser responsabilizado, demandado';
	conteudo +='judicial ou administrativamente, ou de qualquer forma inquirido pela veicula&#231;&#227;o ou uso indevido ou n&#227;o autorizado de materiais, informa&#231;&#245;es e/ou dados de propriedade e titularidade de terceiros, o USU&#193;RIO, como &#250;nico respons&#225;vel por sua remessa ao Wikicrimes, Ser&#225; chamado para responder &#224;s acusa&#231;&#245;es ou indaga&#231;&#245;es perante o reclamante, devendo, se o caso, ressarci-lo integralmente pelas perdas e danos da&#237; decorrentes.';
	conteudo +='';
	conteudo +='5.12    Wikicrimes n&#227;o se responsabiliza em nenhuma hip&#243;tese, e';
	conteudo +='o USU&#193;RIO assume de forma integral o risco, de algum outro usu&#225;rio e/ou terceiro copiar as informa&#231;&#245;es que tenham sido enviadas e/ou inseridas no site, para outros fins, mesmo que il&#237;citos, alheios ou n&#227;o &#224;s atividades de Wikicrimes.';
	conteudo +='';
	conteudo +='5.13    O USU&#193;RIO que encaminhar qualquer material para inser&#231;&#227;o no';
	conteudo +='site, ser&#225; obrigado perante Wikicrimes a responder perante terceiros por toda e qualquer responsabilidade do mesmo oriunda, de qualquer natureza, seja ela financeira, patrimonial, autoral ou moral.';
	conteudo +='';
	conteudo +='5.14    Qualquer informa&#231;&#227;o ou material encaminhado, inserido e/ou';
	conteudo +='enviado pelo USU&#193;RIO a Wikicriems n&#227;o poder&#225; jamais ter conte&#250;do obsceno, preconceituoso, discriminat&#243;rio, pornogr&#225;fico, ofensivo &#224; moral, bons costumes, &#233;tica e/ou &#224; legisla&#231;&#227;o em geral, sob pena daquele responder nos termos da lei por perdas e danos causados &#224; terceiros e/ou ao Wikicrimes, sem preju&#237;zo das san&#231;&#245;es criminais porventura cab&#237;veis.';
	conteudo +='';
	conteudo +='5.15    O USU&#193;RIO fica terminantemente proibido de:';
	conteudo +='';
	conteudo +='(a) utilizar-se das atividades do Wikicrimes para fins comerciais, mediante o envio de publicidade, correspond&#234;ncias institucionais, malas diretas, propagandas ou qualquer outro material de natureza semelhante; e';
	conteudo +='(b) enviar quaisquer esp&#233;cies de arquivos e/ou material com v&#237;rus que possam causar danos ao software/hardware Wikicrimes e/ou demais usu&#225;rios.';
	conteudo +='';
	conteudo +='6. Da Aus&#234;ncia de Responsabilidade Wikicrimes';
	conteudo +='';
	conteudo +='6.1     Wikicrimes envidar&#225; seus melhores esfor&#231;os para manter o';
	conteudo +='site acess&#237;vel de forma constante, ininterrupta e isenta de quaisquer erros, no entanto, n&#227;o ser&#225; respons&#225;vel por danos decorrentes de falha ou interrup&#231;&#227;o na presta&#231;&#227;o dos servi&#231;os. O USU&#193;RIO reconhece e aceita, que Wikicrimes tamb&#233;m n&#227;o ser&#225; respons&#225;vel:';
	conteudo +='';
	conteudo +='(a) pela impossibilidade de acesso ao site p&#243; r falha de comunica&#231;&#227;o com o mesmo decorrente de: (i) quaisquer defeitos ou inadequa&#231;&#227;o dos equipamentos utilizados pelo USU&#193;RIO para acessar o Wikicrimes, incluindo, mas n&#227;o se limitando, aos softwares, hardwares, sistemas de processamento e quaisquer conex&#245;es de rede;';
	conteudo +='(ii) inabilidade do USU&#193;RIO para opera&#231;&#227;o dos seus equipamentos; (iii) falta de compreens&#227;o das instru&#231;&#245;es contidas no Wikicrimes; (iv) falhas na rede mundial de computadores (Internet) e provedores; (v) falhas nos sistemas, softwares e/ou hardwares do Wikicrimes; (vi) interrup&#231;&#245;es propositais realizadas pelo Wikicrimes por quaisquer motivos; (vii) interrup&#231;&#227;o das atividades do site; e (viii) caso fortuito ou for&#231;a maior;';
	conteudo +='';
	conteudo +='(b) por quaisquer preju&#237;zos causados por grava&#231;&#227;o realizada pelo USU&#193;RIO (download) para os seus equipamentos pr&#243;prios, de quaisquer arquivos eletr&#244;nicos existentes ou disponibilizados no site, quer pelo Wikicrimes, quer por outros usu&#225;rios, quer por terceiros;';
	conteudo +='';
	conteudo +='(c) por quaisquer preju&#237;zos resultantes de, ou relacionados a, qualquer dos servi&#231;os ou trabalhos apresentados pelos anunciantes e/ou patrocinadores ou parceiros do Wikicrimes, tais como an&#250;ncios e promo&#231;&#245;es veiculados (inclusive, mas sem limita&#231;&#227;o, preju&#237;zos resultantes do descumprimento pelos anunciantes e/ou patrocinadores ou parceiros das disposi&#231;&#245;es aplic&#225;veis pelo C&#243;digo de Defesa do Consumidor), que ser&#227;o de &#250;nica responsabilidade dos mesmos;';
	conteudo +='';
	conteudo +='   1. por quaisquer preju&#237;zos advindos ao USU&#193;RIO pela utiliza&#231;&#227;o indevida e/ou dolosa por terceiros, dos materiais, dados pessoais e cadastrais fornecidos por aquele, que forem veiculados no Wikicriems na forma descrita e prevista no presente contrato;';
	conteudo +='';
	conteudo +='';
	conteudo +='';
	conteudo +='(e) por quaisquer preju&#237;zos decorrentes da utiliza&#231;&#227;o indevida do nome de usu&#225;rio e senha por terceiros;';
	conteudo +='';
	conteudo +='(f) por eventual inviabilidade t&#233;cnica de efetivo envio de informa&#231;&#245;es ao Wikicrime por impossibilidade de acesso ou falha de comunica&#231;&#227;o atribu&#237;vel ao site ou ainda, por falhas ordin&#225;rias ou extraordin&#225;rias, principalmente aquelas que possam resultar em perda de dados e informa&#231;&#245;es previamente obtidas e armazenadas, fluxo de informa&#231;&#245;es a serem obtidas, manuten&#231;&#227;o do cadastro do USU&#193;RIO junto a Wikicrimes, bem como por interrup&#231;&#245;es moment&#226;neas ou definitivas nos servi&#231;os;';
	conteudo +='';
	conteudo +='   1. por danos civis ou criminais decorrentes da publica&#231;&#227;o e exposi&#231;&#227;o de materiais visuais, auditivos ou textuais, inseridos no site Wikicrimes por usu&#225;rios, sendo certo que a responsabilidade &#233; integralmente do USU&#193;RIO que as enviou;';
	conteudo +='';
	conteudo +='';
	conteudo +='';
	conteudo +='   1. por atos de m&#225;-f&#233; de terceiros que promovam a invas&#227;o do programa do Wikicrimes, tais como hackers, que acessem os dados cadastrais e pessoais fornecidos pelo USU&#193;RIO e que se utilizem ilicitamente dos mesmos para quaisquer fins. Wikicriems declara ter os cuidados razo&#225;veis para evitar a invas&#227;o do sistema, mas n&#227;o se responsabiliza e n&#227;o pode se responsabilizar, pela inviolabilidade do mesmo;';
	conteudo +='   1. pela perda de dados e/ou informa&#231;&#245;es eventualmente gravados pelo USU&#193;RIO no banco de dados de Wikicrimes, seja por rescis&#227;o do contrato, seja por falha de sistema. Wikicriems n&#227;o se obriga, ainda, a manter e/ou realizar qualquer esp&#233;cie de back-up dos materiais e dados fornecidos pelo USU&#193;RIO; e';
	conteudo +='';
	conteudo +='';
	conteudo +='';
	conteudo +='   1. pela inadimpl&#234;ncia do USU&#193;RIO &#224;s normas da lei ou do presente, que gere preju&#237;zos a terceiros e/ou a outros usu&#225;rios.';
	conteudo +='';
	conteudo +='7. Do Prazo, Rescis&#227;o e Modifica&#231;&#227;o de Cl&#225;usulas';
	conteudo +='';
	conteudo +='7.1     O presente contrato vigorar&#225; por tempo indeterminado ou';
	conteudo +='durante o per&#237;odo em que Wikicrimes estiver disponibilizando os seus servi&#231;os via internet. Wikicrimes, no entanto, reserva-se o direito de, a qualquer momento, sem qualquer &#244;nus ou aviso pr&#233;vio, encerrar suas atividades ou limitar o uso de seus servi&#231;os.';
	conteudo +='';
	conteudo +='7.2     Wikicrimes reserva-se o direito de bloquear o acesso e';
	conteudo +='rescindir de imediato, sem aviso pr&#233;vio, a presta&#231;&#227;o dos servi&#231;os, toda vez que, a seu exclusivo crit&#233;rio, entender que h&#225; ind&#237;cios de utiliza&#231;&#227;o fraudulenta ou il&#237;cita do site Wikicrimes pelo USU&#193;RIO. Inclui-se na presente situa&#231;&#227;o, entre outras, quaisquer ind&#237;cios (a crit&#233;rio &#250;nico e subjetivo de Wikicrimes) de que o USU&#193;RIO fraudou ou infringiu o sistema de seguran&#231;a do site.';
	conteudo +='';
	conteudo +='7.2.1 Tamb&#233;m poder&#225; Wikicrimes rescindir de imediato a presta&#231;&#227;o dos servi&#231;os do site, impedindo inclusive o recadastramento do USU&#193;RIO que tiver de qualquer maneira agido contra a moral, &#233;tica, bons costumes, lei, disposi&#231;&#245;es deste termo ou, ainda, que tenha sido expulso do site Wikicrimes.';
	conteudo +='';
	conteudo +='7.3     Em todos os casos de cessa&#231;&#227;o da presta&#231;&#227;o dos servi&#231;os';
	conteudo +='previstos no presente instrumento, seja por decis&#227;o do USU&#193;RIO ou de Wikicrimes, seja por expuls&#227;o do USU&#193;RIO do site Wikicriems, ou pelo encerramento das atividades do Wikicrimes, as informa&#231;&#245;es, imagens, textos, arquivos e quaisquer dados referentes ao USU&#193;RIO ser&#227;o perdidos em sua totalidade, n&#227;o cabendo a este qualquer esp&#233;cie de indeniza&#231;&#227;o ou compensa&#231;&#227;o.';
	conteudo +='';
	conteudo +='7.3.1 O USU&#193;RIO declara-se ciente e concorda que, ap&#243;s o t&#233;rmino da presta&#231;&#227;o dos servi&#231;os, suas informa&#231;&#245;es, opini&#245;es, textos, dados e/ou quaisquer materiais por ele encaminhados ao site Wikicrimes';
	conteudo +='poder&#227;o permanecer no site e ser utilizados pelo Wikicrimes, sem quaisquer &#244;nus para o Wikicriems, com a perman&#234;ncia da sujei&#231;&#227;o desses materiais aos termos do presente instrumento por prazo indeterminado, ou at&#233; que o USU&#193;RIO desligado do Wikicrimes solicite expressamente a sua retirada do site.';
	conteudo +='';
	conteudo +='7.4     Wikicrimes reserva-se, ainda, o direito de, a qualquer';
	conteudo +='momento, alterar o disposto neste instrumento. Caso o USU&#193;RIO continue utilizando os servi&#231;os do Wikicrimes ap&#243;s a elabora&#231;&#227;o dos novos Termos e Condi&#231;&#245;es de Uso do Site, este ter&#225; prosseguimento normal, estando configurada a sua aceita&#231;&#227;o pelo USU&#193;RIO.';
	conteudo +='';
	conteudo +='7.4.1 Caso o USU&#193;RIO n&#227;o concorde com qualquer disposi&#231;&#227;o da nova vers&#227;o dos Termos e Condi&#231;&#245;es de Uso do Site, este dever&#225; encaminhar a notifica&#231;&#227;o a Wikicrimes, informando sobre o seu interesse em n&#227;o continuar utilizando os servi&#231;os do site, ficando vedado o seu acesso.';
	conteudo +='';
	conteudo +='9. Das Condi&#231;&#245;es Gerais';
	conteudo +='';
	conteudo +='9.1     O USU&#193;RIO, ao utilizar os servi&#231;os de Wikicriems aceita';
	conteudo +='expressamente todas as cl&#225;usulas do presente instrumento, bem como as atividades que existem e que venham a existir no site Wikicrimes, reconhecendo-as como inocentes, saud&#225;veis, de boa-f&#233; e n&#227;o-ofensivas, aceitando suas regras, instru&#231;&#245;es e condi&#231;&#245;es constantes do pr&#243;prio site, de forma absoluta e irrestrita, garantindo que n&#227;o se sentir&#225; de qualquer forma ultrajado, ofendido ou prejudicado pelas mesmas.';
	conteudo +='';
	conteudo +='9.2     Os servi&#231;os de responsabilidade de terceiros e/ou ofertados';
	conteudo +='por esses, tais como servi&#231;os de telecomunica&#231;&#245;es, provedor (Internet paga), entre outros que possam ser encontrados no site do Wikicrimes, dever&#227;o ser pagos diretamente aos terceiros prestadores de servi&#231;os, n&#227;o cabendo a Wikicrimes nenhuma responsabilidade sobre a cobran&#231;a e/ou qualidade dos mesmos.';
	conteudo +='';
	conteudo +='';
	conteudo +='9.9     Para uma maior seguran&#231;a do Wikicrimes e dos usu&#225;rios, &#233;';
	conteudo +='necess&#225;rio que o USU&#193;RIO esteja atento aos seguintes pontos:';
	conteudo +='';
	conteudo +='(a) Nunca forne&#231;a sua senha a terceiros. Ela &#233; pessoal e intransfer&#237;vel;';
	conteudo +='(b) ao criar a sua senha, n&#227;o utilize senhas &#243;bvias, tais como nome pr&#243;prio, iniciais, com parentesco, data de nascimento, etc. Procure utilizar letras e n&#250;meros a fim de atender aos padr&#245;es e requisitos m&#237;nimos de seguran&#231;a;';
	conteudo +='(c) caso tenha fornecido a sua senha a terceiros, avise imediatamente Wikicrimes;';
	conteudo +='(d) a transmiss&#227;o de v&#237;rus se d&#225; atrav&#233;s de e-mails que solicitam a digita&#231;&#227;o de senhas, ou que possuam informa&#231;&#245;es de cobran&#231;a ou cont&#234;m arquivo anexado com v&#237;rus. Esteja atento e caso desconfie da proced&#234;ncia do e-mail, n&#227;o o abra; e';
	conteudo +='(e) a transmiss&#227;o de v&#237;rus e de programas destrutivos que podem fazer com que o seu computador divulgue informa&#231;&#245;es pessoais, tamb&#233;m se d&#225; comumente atrav&#233;s do download de arquivos infectados. Portanto, se voc&#234; n&#227;o conhece quem os enviou ou caso voc&#234; n&#227;o o tenha solicitado, n&#227;o efetue o download.';
	conteudo +='';
	conteudo +='10. Da Lei Aplic&#225;vel e Foro';
	conteudo +='';
	conteudo +='10.1    As presentes condi&#231;&#245;es s&#227;o regidas &#250;nica e exclusivamente';
	conteudo +='pelas leis da Rep&#250;blica Federativa do Brasil e qualquer discuss&#227;o judicial que surja tendo por base sua interpreta&#231;&#227;o ou aplica&#231;&#227;o dever&#225; ser julgado por tribunais brasileiros, estando desde logo eleito o foro da cidade de Fortaleza, Estado do Cear&#225;, por mais privilegiado que outro seja ou possa vir a ser.';
	conteudo +='';
	conteudo +='</textarea></td></tr>';
	conteudo +='';
	conteudo +='</tbody></table><input id="btnRegistrar" name="btnRegistrar" type="button" value="Concordo com os Termos de Uso" onclick="getCityLatLng(); executaRequisicaoRegistraUsuarioWikiCrimes();"  style="font-family: Arial, Helvetica, sans-serif; font-size: 10px;	font-weight: bold; margin: 1; padding: 1; background-color: #6da6e2; color: white;	border: 1px solid #2763a5;"/>';
	conteudo +='<input id="usuarioForm:cancel" name="usuarioForm:cancel" type="submit" value="Cancelar" style="font-family: Arial, Helvetica, sans-serif; font-size: 10px;	font-weight: bold; margin: 1; padding: 1; background-color: #6da6e2; color: white;	border: 1px solid #2763a5;"/>';
	conteudo +='</form></td></tr>';
	conteudo +='</tbody></table>';
	conteudo +='</div>';
	
	document.getElementById("bodyContent").innerHTML = conteudo;
}

function conteudoInstallApp(){
	var conteudo ='';
	conteudo +='<div align="center" style="border: 1px solid #2763a5;width: 176px">';
	conteudo +='	<table style="font-family:Arial, sans-serif;  font-size: 11px;" cellpadding="1" cellspacing="1"  align="center"';
	conteudo +='		<tr>';
	conteudo +='			<td align="center" colspan="2">';
	conteudo +='				'+prefs.getMsg("voce.ja.e.usuario.wikicrimes");
	conteudo +='			</td>';
	conteudo +='		</tr>';					
	conteudo +='		<tr>';
	conteudo +='			<td align="center">';
	conteudo +='				<input style="cursor: pointer;font-family: Arial, Helvetica, sans-serif; font-size: 10px;	font-weight: bold; margin: 1; padding: 1; background-color: #6da6e2; color: white;	border: 1px solid #2763a5;" type="button" value ="'+prefs.getMsg("sim")+'" onclick="chamarTelaLogin();" />';
	conteudo +='			</td>';
	conteudo +='			<td align="center">';
	conteudo +='				<input style="cursor: pointer;font-family: Arial, Helvetica, sans-serif; font-size: 10px;	font-weight: bold; margin: 1; padding: 1; background-color: #6da6e2; color: white;	border: 1px solid #2763a5;" type="button" value ="'+prefs.getMsg("nao")+'" onclick="registrarUsuario();" />';
	conteudo +='			</td>';
	conteudo +='		</tr>';
	conteudo +='	</table>';
	conteudo +='</div>';
	document.getElementById("bodyContent").innerHTML =  conteudo;
}


function conteudoPerfil(){
	 
	var conteudo ='<table align="center" style="font-family:Arial, sans-serif;  font-size: 11px;" cellpadding="0" cellspacing="0" >';
        	
	conteudo+='			<tr>';
	conteudo+='				<td colspan="1" align="left"><img width="150px" height="50px" id="img_logo"';
	conteudo+='					src="'+linkAplication+'images/widget/wikicrimesPerfil.PNG'+'" />';						
	conteudo+='				</td>';				
	conteudo+='				<td align="right"> <input title="" type="text" id="pesquisa" name="pesquisa" style="width:220px;font-size: 12px; border: 1px solid #2763a5;" onkeypress="return submitEnter(event);"/><input type="button" id="botao_pesquisar" onclick="showLocal();" style="font-family: Arial, Helvetica, sans-serif; font-size: 10px;	font-weight: bold; margin: 1; padding: 1; background-color: #6da6e2; color: white;	border: 1px solid #2763a5;"/> </td>';
	conteudo+='			</tr>';
	conteudo+='			<tr>';
	conteudo+='				<td colspan="2" align="center">';
	conteudo+='					<div class="div_erro" id="content_erro"></div>';
	conteudo+='					<div class="div_info" id="content_info"></div>';
	conteudo+='					<div class="div_alerta" id="content_alerta"></div>';
	conteudo+='				</td>';
	conteudo+='			</tr>';
	conteudo+='			<tr>';
	conteudo+='				<td colspan="2">';
	conteudo+='				<div id="map"';
	conteudo+='				style="width: 482px; height: 340px; border: 2px solid #2763a5;"></div>';
	conteudo+='				</td>';				
	conteudo+='			</tr>';				
    conteudo+='		</table>';
    return conteudo;
}


function conteudoLegenda(){
	var conteudo = "<table cellpading='0' cellspacing='0' style='font-family:Arial, sans-serif;  font-size: 10px;'>";
	conteudo += "		<tr>";
	conteudo += "			<td align='center'> <img width='12px' height='21px'  src='"+linkAplication+"/images/baloes/vermelha.png'/> </td> <td colspan='1'> "+prefs.getMsg("roubo_text")+"</td>";
	conteudo += "		</tr>";
	conteudo += "		<tr>";
	conteudo += "			<td align='center'> <img width='12px' height='21px' src='"+linkAplication+"/images/baloes/azul.png'/> </td> <td colspan='1'> "+prefs.getMsg("furto_text")+ "</td>";
	conteudo += "		</tr>";
	conteudo += "		<tr>";
	conteudo += "			<td align='center'> <img width='12px' height='21px' src='"+linkAplication+"/images/baloes/laranja.png'/> </td> <td colspan='1'> "+prefs.getMsg("outros_text")+ "</td>";
	conteudo += "		</tr>";
	conteudo += "		<tr>";
	conteudo += "			<td align='center'> <img width='12px' height='21px' src='"+linkAplication+"/images/baloes/novoMarcadorVerde.png'/> </td> <td colspan='1'> "+prefs.getMsg("denuncia_text")+ " </td>";
	conteudo += "		</tr>";
	conteudo += "	</table>";
	return conteudo;
}

function mostrarImagemTipoAlerta(tipoAlerta){
	var html = '';
	if(tipoAlerta == "1" || tipoAlerta == "4")
		html += '<img width=12px height=21 src='+ linkAplication+'images/widget/vermelho.png' +' />';
	if(tipoAlerta == "2" || tipoAlerta == "3")
		html += '<img width=12px height=21 src='+ linkAplication+'images/widget/azul.png' +' />';
	if(tipoAlerta == "5")
		html += '<img width=12px height=21px src='+ linkAplication+'images/widget/laranja.png' +' />';
	if(tipoAlerta == "6")
		html += '<img width=12px height=21px src='+ linkAplication+'images/baloes/novoMarcadorVerde.png' +' />';
	return html;
}

function conteudoCanvasOrkut(){
    	
	var conteudo = '<div align="center" id="divModal" ></div><table style="font-family:Arial, sans-serif;  font-size: 11px;" cellpadding="1" cellspacing="1" >';		          	
	conteudo+='			<tr>';					
	conteudo+='				<td colspan="3">';					
	conteudo+='				</td>';
	conteudo+='			</tr>';
	conteudo+='			<tr>';					
	conteudo+='				<td colspan="3">';					
	conteudo+='					<div id="mens_info" align="center" ></div>';					
	conteudo+='				</td>';
	conteudo+='			</tr>';
	conteudo+='			<tr>';
	conteudo+='				<td colspan="1" align="left"> <img id="img_logo"';
	conteudo+='					src="'+linkAplication+prefs.getMsg("imagem.wikicrimes")+'" />';						
	conteudo+='				</td>';				
	conteudo+='				<td align="left" colspan="1">';					
	conteudo+='					<div align="left" id="registro_alerta" align="center" style="width: 200px; height: 36px;" >'; 
	conteudo+='						<br><img title="'+prefs.getMsg("hint.alertarAmigos")+'" onclick="mostraDivSelecaoAlerta(\'410px\',\'220px\',constroiTelaEscolhaTipoAlerta());" align="left" style="cursor: pointer;border: 1px solid #2763a5" src="'+linkAplication+prefs.getMsg("imagem.registrar.relato")+'" />';	
	conteudo+='					</div>';
	conteudo+='				</td>';
	conteudo+='				<td colspan="1">';					
	conteudo+='					<div id="pesquisa_mapa" align="center" style="width: 304px; height: 36px;" >'; 
	conteudo+='						<br><input title="" align="center" size="20" type="text" id="pesquisa" name="pesquisa" style="font-size: 12px; border: 1px solid #2763a5;" onkeypress="return submitEnter(event);"/><input type="button" id="botao_pesquisar" onclick="showLocal();" style="font-family: Arial, Helvetica, sans-serif; font-size: 10px;	font-weight: bold; margin: 1; padding: 1; background-color: #6da6e2; color: white;	border: 1px solid #2763a5;"/>';	
	conteudo+='					</div>';
	conteudo+='				</td>';
	conteudo+='			</tr>';					
	conteudo+='			<tr>';					
	conteudo+='				<td >';					
	conteudo+='					<div style="position: absolute; padding: 0.2em;  padding-left: 0.8em;  background-color: #2763a5;  border-style: solid;  border-width: 2px;  width: 178px;  height: 14px;  color: #ffffff;  font: bold;"><b> '+prefs.getMsg("alertas.que.voce.recebeu")+'</b></div><br>';
	conteudo+='					<div id="notificacoes" style="padding-left: 0.8em ; width: 302px; height: 176px;overflow: auto;border: 1px solid #2763a5" >'; 
	conteudo+='						';	
	conteudo+='					</div><br>';
	conteudo+='					<div style="position: absolute; padding: 0.2em;  padding-left: 0.8em;  background-color: #2763a5;  border-style: solid;  border-width: 2px;  width: 178px;  height: 14px;  color: #ffffff;  font: bold;"><b> '+prefs.getMsg("comentarios")+'</b></div><br>';
	conteudo+='					<div id="comentarios" style="padding-left: 0.8em ;width: 302px; height: 210px; overflow: auto;border: 1px solid #2763a5" >'; 
	conteudo+='						<br>';	
	conteudo+='					</div>';					
	conteudo+='				</td>';
	conteudo+='				<td valign="top" colspan="2">';
	conteudo+='					<div id="map"';
	conteudo+='					style="width: 500px; height: 400px;border: 1px solid #2763a5; display:block;"></div>';
	conteudo+='					<div id="div_sobre_mapa"';
	conteudo+='					style="width: 500px; height: 400px;border: 1px solid #2763a5; display:none; background-color:gray"></div>';
	conteudo+='					<div id="registro_alerta" align="center"  >'; 
	conteudo+='					<br> | <a style="color : #6da6e2; cursor: pointer;" onclick="mostrarModalFaleConosco();"> <b>'+prefs.getMsg("fale.conosco")+'</b> </a> | <a style="color : #6da6e2; cursor: pointer;" onclick="window.open (\''+linkAplication+'\', \'Wikicrimes\',\'location=1,status=1,scrollbars=1,menubar=1,resizable=1\');"> <b>'+prefs.getMsg("registrar.crimes")+'</b> </a> | <a style="color : #6da6e2; cursor: pointer;" onclick="convideSeusAmigos()"> <b> '+prefs.getMsg("convidar.amigos")+' </b> </a> | <a style="color : #6da6e2; cursor: pointer;" onclick="mostrarModalConfiguracoes();"> <b> '+prefs.getMsg("canvas.configuracoes")+' </b> </a> |';	
	conteudo+='					</div>';
	conteudo+='				</td>';				
	conteudo+='			</tr>';				
    conteudo+='		</table>';
    return conteudo;
}

function conteudoCanvasNing(){
	
	var conteudo = '<div align="center" id="divModal" ></div><table style="font-family:Arial, sans-serif;  font-size: 11px;" cellpadding="1" cellspacing="1" >';		          	
	conteudo+='			<tr>';					
	conteudo+='				<td colspan="3">';					
	conteudo+='				</td>';
	conteudo+='			</tr>';
	conteudo+='			<tr>';					
	conteudo+='				<td colspan="3">';					
	conteudo+='					<div id="mens_info" align="center" ></div>';					
	conteudo+='				</td>';
	conteudo+='			</tr>';
	conteudo+='			<tr>';
	conteudo+='				<td colspan="1" align="left"> <img id="img_logo"';
	conteudo+='					src="'+linkAplication+'images/widget/wikicrimesPerfil.PNG'+'" />';						
	conteudo+='				</td>';				
	conteudo+='				<td align="left" colspan="1">';					
	conteudo+='					<div align="left" id="registro_alerta" align="center" style="width: 100px; height: 36px;" >'; 
	conteudo+='						<img title="'+prefs.getMsg("hint.alertarAmigos")+'" onclick="mostraDivSelecaoAlerta(\'388px\',\'148px\',constroiTelaEscolhaTipoAlerta());" align="left" style="cursor: pointer;border: 1px solid #2763a5" src="'+linkAplication+prefs.getMsg("imagem.registrar.relato")+'" />';	
	conteudo+='					</div>';
	conteudo+='				</td>';
	conteudo+='				<td colspan="1">';					
	conteudo+='					<div id="pesquisa_mapa" align="center" style="width: 100%; height: 36px;" >'; 
	conteudo+='						<input title="" align="center" type="text" id="pesquisa" name="pesquisa" style="width:50%;font-size: 12px; border: 1px solid #2763a5;" onkeypress="return submitEnter(event);"/><input type="button" id="botao_pesquisar" onclick="showLocal();" style="font-family: Arial, Helvetica, sans-serif; font-size: 10px;	font-weight: bold; margin: 1; padding: 1; background-color: #6da6e2; color: white;	border: 1px solid #2763a5;"/>';	
	conteudo+='					</div>';
	conteudo+='				</td>';
	conteudo+='			</tr>';					
	conteudo+='			<tr>';					
	conteudo+='				<td >';					
	conteudo+='					<div style="position: absolute; padding: 0.2em;  padding-left: 0.8em;  background-color: #2763a5;  border-style: solid;  border-width: 2px;  width: 178px;  height: 14px;  color: #ffffff;  font: bold;"><b> '+prefs.getMsg("alertas.que.voce.recebeu")+'</b></div><br>';
	conteudo+='					<div id="notificacoes" style="padding-left: 0.8em ; width: 302px; height: 156px;overflow: auto;border: 1px solid #2763a5" >'; 
	conteudo+='						';	
	conteudo+='					</div>';
	conteudo+='					<div style="position: absolute; padding: 0.2em;  padding-left: 0.8em;  background-color: #2763a5;  border-style: solid;  border-width: 2px;  width: 178px;  height: 14px;  color: #ffffff;  font: bold;"><b> '+prefs.getMsg("comentarios")+'</b></div><br>';
	conteudo+='					<div id="comentarios" style="padding-left: 0.8em ;width: 302px; height: 156px; overflow: auto;border: 1px solid #2763a5" >'; 
	conteudo+='						<br>';	
	conteudo+='					</div>';					
	conteudo+='				</td>';
	conteudo+='				<td valign="top" colspan="2">';
	conteudo+='					<div id="map"';
	conteudo+='					style="width: 416px; height: 332px;border: 1px solid #2763a5; display:block;"></div>';
	conteudo+='					<div id="registro_alerta" align="center" style="padding-top:2px" >'; 
	conteudo+='					<a style="color : #6da6e2; cursor: pointer;" onclick="mostrarModalFaleConosco();"> <b>'+prefs.getMsg("fale.conosco")+'</b> </a> | <a style="color : #6da6e2; cursor: pointer;" onclick="window.open (\''+linkAplication+'\', \'Wikicrimes\',\'location=1,status=1,scrollbars=1,menubar=1,resizable=1\');"> <b>'+prefs.getMsg("registrar.crimes")+'</b> </a> | <a style="color : #6da6e2; cursor: pointer;" onclick="mostrarModalConfiguracoes();"> <b> '+prefs.getMsg("canvas.configuracoes")+' </b> </a>';	
	conteudo+='					</div>';
	conteudo+='				</td>';				
	conteudo+='			</tr>';				
    conteudo+='		</table>';
    return conteudo;
}

function constroiTelaEscolhaTipoAlerta(){
	var htmlTela = "<table style='font-family:Arial, sans-serif;  font-size: 11px;'>";
	htmlTela += "		<tr>";
	htmlTela += "			<td colspan='2'> "+prefs.getMsg("pergunta_escolha_tipo_alerta")+" </td>";
	htmlTela += "		</tr>";
	htmlTela += "		<tr>";
	htmlTela += "			<td> <a style='color : #6da6e2; cursor: pointer;' onclick='registrarAlerta();'><b>"+prefs.getMsg("denuncia_text")+"</b></a> </td> <td> <a style='color : #6da6e2; cursor: pointer;' onclick='escolherTipoCrime();'><b>Crime</b></a></td>";
	htmlTela += "		</tr>";
	htmlTela += "	</table><br/><table cellpading='0' cellspacing='0' style='font-family:Arial, sans-serif;  font-size: 11px;'> <tr valign='bottom'> <td align='right'> <a align='right' style='color : #6da6e2; cursor: pointer;' onclick='escondeDivSelecaoAlerta();'> "+prefs.getMsg("cancelar")+" </a> </td></tr></table>";
	return htmlTela;
}

function constroiTelaEscolhaTipoCrime(){
	var htmlTela = "<table cellpading='20' cellspacing='20' style='font-family:Arial, sans-serif;  font-size: 11px;'>";
	htmlTela += "		<tr>";
	htmlTela += "			<td colspan='6'> "+prefs.getMsg("pergunta_escolha_tipo_crime")+" </td>";
	htmlTela += "		</tr>";
	htmlTela += "		<tr>";
	htmlTela += "			<td colspan='2'> <a style='color : #6da6e2; cursor: pointer;' onclick='registrarCrime(\"4\");'><b> "+prefs.getMsg("roubo_text")+" </b></a> </td> <td colspan='2'> <a style='color : #6da6e2; cursor: pointer;' onclick='registrarCrime(\"3\");'><b>"+prefs.getMsg("furto_text")+"</b></a></td> <td colspan='2'> <a style='color : #6da6e2; cursor: pointer;' onclick='registrarCrime(\"5\");'><b>"+prefs.getMsg("outros_text")+"</b></a></td>";
	htmlTela += "		</tr>";
	htmlTela += "		<tr>";
	htmlTela += "			<td colspan='3'> <a style='color : #6da6e2; cursor: pointer;' onclick='registrarCrime(\"1\");'><b>"+prefs.getMsg("tentativa_text")+" "+prefs.getMsg("roubo_text")+"</b></a> </td> <td colspan='3'> <a style='color : #6da6e2; cursor: pointer;' onclick='registrarCrime(\"2\");'><b>"+prefs.getMsg("tentativa_text")+" "+prefs.getMsg("furto_text")+"</b></a> </td>";
	htmlTela += "		</tr>";
	htmlTela += "	</table><br/><table cellpading='0' cellspacing='0' style='font-family:Arial, sans-serif;  font-size: 11px;'> <tr valign='bottom'> <td align='right'> <a align='right' style='color : #6da6e2; cursor: pointer;' onclick='escondeDivSelecaoAlerta();'> "+prefs.getMsg("cancelar")+" </a> </td></tr></table>";
	return htmlTela;
}

function escolherTipoCrime(){
	mostraDivSelecaoAlerta('400px','150px',constroiTelaEscolhaTipoCrime());
}

function constroiModoal(conteudo, wPixel, hPixel,titulo, distanciaTopo, distanciaEsquerda,rolagem){	
	var htmlModal = '<div style="background-color: white; width: '+(rolagem?wPixel+16:wPixel)+'px; height: '+hPixel+'px;overflow: auto;border: 1px solid black" >';
	htmlModal += ' 		<div style="font-family:Arial, sans-serif;  font-size: 11px;  background-color: #2763a5;  border-style: solid;  ;border: 1px solid black;  width: '+(wPixel-2)+'px;  height: 14px;  color: #ffffff;  font: bold;"><table cellpadding="0" CELLSPACING = "0" width = "100%" style = "color: #ffffff; font-family:Arial, sans-serif;  font-size: 11px;"><tr><td align="left"><b align="left">'+titulo+'</b></td> <td align="right"><b style="cursor: pointer;" onclick="fecharModal();'+(rolagem?'cancelarRegistroCrime();':'')+'" align="right"> X</b></td></tr> </table> </div>';
	htmlModal += conteudo;
	htmlModal += '	</div>';	
	document.getElementById("divModal").innerHTML = htmlModal;	
	document.getElementById("divModal").style.top = ''+distanciaTopo+'%';
	document.getElementById("divModal").style.left = ''+distanciaEsquerda+'%';
	document.getElementById("divModal").style.visibility = "visible";	
}

function fecharModal(){
	document.getElementById("divModal").style.visibility = "hidden";
}

function conteudoAtividade(tipoAtividade){
	var html = "";
	
		html = "<table>" +
				"	<tr valign='bottom'>" +
				"		<td>" +
				"			<img width='50px' height='50px' src='"+linkAplication+"images/widget/atencaoWikiCrimesSocial_50x50.jpg' />" +
				"		</td>" +
				"		<td> " +
				"			Clique <a href='"+usuarioRedeSocial.linkPerfil+"'>"+prefs.getMsg("aqui")+"</a> "+prefs.getMsg("atividade.body")+" "+usuarioRedeSocial.nome+
				"		</td>" +
				"		<td>" +
				"			<img width='82px' height='50px' src='"+linkAplication+"images/widget/img_propaganda.jpg' />" +
				"		</td>" +
				"	</tr>" +
				
				"</table>";
		
	
	return html;	
}

function conteudoFaleConosco(){
	var html = '		<table style="font-family:Arial, sans-serif;  font-size: 11px;padding: 10px;">';
	html += '				<tr>';
	html += '					<td colspan="2">';
	html += '						'+prefs.getMsg("msg.email.fale.conosco");
	html += '					</td>';
	html += '				</tr>';
	html += '				<tr>';
	html += '					<td align="left" colspan="1" width="60px">';
	html += '						'+prefs.getMsg("msg.email.seu.email")+':';
	html += '					</td>';
	html += '					<td align="left" colspan="1">';
	html += '						<input style="font-size: 12px; border: 1px solid #2763a5;width:260px" type="text" id="email_fale_conosco"/>';
	html += '					</td>';
	html += '				</tr>';
	html += '				<tr>';
	html += '					<td align="left" colspan="2">';
	html += '						'+prefs.getMsg("msg.email.mensagem")+':';
	html += '					</td>';
	html += '				</tr>';
	html += '				<tr>';
	html += '					<td align="left" colspan="2">';
	html += '						<TEXTAREA NAME="desc_fale_conosco" id="desc_fale_conosco" style="font-size: 12px; border: 1px solid #2763a5;" COLS="48" ROWS="6"></TEXTAREA>';
	html += '					</td>';
	html += '				</tr>';
	html += '				<tr>';
	html += '					<td align="left">';
	html += '						<input type="button" value="'+prefs.getMsg("enviar")+'" onclick="executaRequisicaoFaleConosco();" style="font-family: Arial, Helvetica, sans-serif; font-size: 10px;	font-weight: bold; margin: 1; padding: 1; background-color: #6da6e2; color: white;	border: 1px solid #2763a5;"/>';
	html += '					</td>';
	html += '					<td>';
	html += '						<input type="button" value="'+prefs.getMsg("cancelar")+'" onclick="fecharModal();" style="font-family: Arial, Helvetica, sans-serif; font-size: 10px;	font-weight: bold; margin: 1; padding: 1; background-color: #6da6e2; color: white;	border: 1px solid #2763a5;"/>';
	html += '					</td>';
	html += '				</tr>';					
	html += '			</table>';
						
	return html;
}

function conteudoConfiguracoes(){
	var html = '		<table style="font-family:Arial, sans-serif;  font-size: 11px;padding: 0.2em;"><br>';
	html += '				<tr>';
	html += '					<td >';
	html += '						<div id="mensagemInformacaoConfiguracoes"></div>';
	html += '					</td>';
	html += '				</tr>';
	html += '				<tr>';	
	html += '					<td >';
	html += '						'+prefs.getMsg("config.deseja.receber.ajuda.baloes")+':';//+prefs.getMsg("msg.email.fale.conosco");
	html += '					</td>';	
	html += '					<td ';
	if(tutorEstaAtivado)
		html += '					<select style="font-size: 12px; border: 1px solid #2763a5;" id="receberAjudaTutor"> <option selected value="1">'+prefs.getMsg("sim")+'</option> <option value="0">'+prefs.getMsg("nao")+'</option> </select>';
	else
		html += '					<select style="font-size: 12px; border: 1px solid #2763a5;" id="receberAjudaTutor"> <option value="1">'+prefs.getMsg("sim")+'</option> <option selected value="0">'+prefs.getMsg("nao")+'</option> </select>';
	html += '					</td>';
	html += '				</tr>';
	html += '				<tr>';
	html += '					<td colspan="2">';
	html += '						<input type="button" value="'+prefs.getMsg("enviar")+'" onclick="executaRequisicaoSalvarConfiguracoes(document.getElementById(\'receberAjudaTutor\').value, \'1\');" style="font-family: Arial, Helvetica, sans-serif; font-size: 10px;	font-weight: bold; margin: 1; padding: 1; background-color: #6da6e2; color: white;	border: 1px solid #2763a5;"/>';
	
	html += '						<input type="button" value="'+prefs.getMsg("cancelar")+'" onclick="fecharModal();" style="font-family: Arial, Helvetica, sans-serif; font-size: 10px;	font-weight: bold; margin: 1; padding: 1; background-color: #6da6e2; color: white;	border: 1px solid #2763a5;"/>';
	html += '					</td>';
	html += '				</tr>';					
	html += '			</table>';
						
	return html;
}


function conteudoAvisoDesativarTutor(){
	var html = '		<table style="font-family:Arial, sans-serif;  font-size: 11px;padding: 0.2em;"><br>';
	html += '				<tr>';
	html += '					<td >';
	html += '						'+prefs.getMsg("canvas.mensagem.desativa.tutor");
	html += '					</td>';
	html += '				</tr>';
	
	html += '				<tr>';
	html += '					<td align="center">';
	html += '						<input type="button" value="ok" onclick="fecharModal();" style="font-family: Arial, Helvetica, sans-serif; font-size: 10px;	font-weight: bold; margin: 1; padding: 1; background-color: #6da6e2; color: white;	border: 1px solid #2763a5;"/>';
	html += '					</td>';
	html += '				</tr>';					
	html += '			</table>';
						
	return html;
}


function conteudoConfirmacaoIndicacao(idNotificacao,tipoRepasse){
	
	var html = '		<table align="center" style="font-family:Arial, sans-serif;  font-size: 11px;padding: 0.2em;"><br>';
	html += '				<tr>';
	html += '					<td colspan="2">';
	html += prefs.getMsg("mensagem.conf.repasse");
	html += '					</td>';
	html += '				</tr>';
	html += '				<tr>';
	if(tipoRepasse==1){
		html += '					<td>';
		html += '						<input type="button" value="'+prefs.getMsg("sim")+'" onclick="executaRequisicaoRepassarRelato(\''+idNotificacao+'\');" style="font-family: Arial, Helvetica, sans-serif; font-size: 10px;	font-weight: bold; margin: 1; padding: 1; background-color: #6da6e2; color: white;	border: 1px solid #2763a5;"/>';
		html += '					</td>';
	}else{
		html += '					<td>';
		html += '						<input type="button" value="'+prefs.getMsg("sim")+'" onclick="executaRequisicaoRepassarCrime(\''+idNotificacao+'\');" style="font-family: Arial, Helvetica, sans-serif; font-size: 10px;	font-weight: bold; margin: 1; padding: 1; background-color: #6da6e2; color: white;	border: 1px solid #2763a5;"/>';
		html += '					</td>';
	}	
	html += '					<td>';
	html += '						<input type="button" value="'+prefs.getMsg("nao")+'" onclick="fecharModal();" style="font-family: Arial, Helvetica, sans-serif; font-size: 10px;	font-weight: bold; margin: 1; padding: 1; background-color: #6da6e2; color: white;	border: 1px solid #2763a5;"/>';
	html += '					</td>';	
	html += '				</tr>';					
	html += '			</table>';						
	return html;
}

function conteudoNaoReceberAjudaTutor(){
	var html = '<a onclick="executaRequisicaoSalvarConfiguracoes(\'0\',\'2\');fecharBalao();">'+prefs.getMsg("balao.ajuda.nao.receber")+'!</a>';
	return html;
}

function conteudoAjudaRelacionadaRegistrarAlerta(){
	var html = '<br/><table style="cursor:pointer;padding : 0px;font-family:Arial, sans-serif;font-size: 11px;">';
		html += '	<tr>';
		html += '		<td>';
		html += '			<b>'+prefs.getMsg("balao.ajuda.relacionada")+' </b>';
		html += '		</td>';
		html += '	</tr>';
		html += '	<tr>';
		html += '		<td>';
		html += '			<a onclick="mostraBalaoAlertarAmigos();">'+prefs.getMsg("balao.ajuda.relacionada.como.registrar.alerta")+'?</a>';
		html += '		</td>';
		html += '	</tr>';
		html += '	<tr>';
		html += '		<td><hr/>';
		html += 			conteudoNaoReceberAjudaTutor();
		html += '		</td>';
		html += '	</tr>';
		html += '</table>';	
	return html;
}

function conteudoAjudaRelacionadaNotificacoes(){
	var html = '<br/><table style="cursor:pointer;padding : 0px;font-family:Arial, sans-serif;font-size: 11px;">';
		html += '	<tr>';
		html += '		<td>';
		html += '			<b>'+prefs.getMsg("balao.ajuda.relacionada")+' </b>';
		html += '		</td>';
		html += '	</tr>';
		html += '	<tr>';
		html += '		<td>';
		html += '			<a onclick="mostraBalaoNotificacoes();">'+prefs.getMsg("balao.ajuda.relacionada.cade.meus.alertas")+'?</a>';
		html += '		</td>';
		html += '	</tr>';
		html += '	<tr>';
		html += '		<td><hr/>';
		html += 			conteudoNaoReceberAjudaTutor();
		html += '		</td>';
		html += '	</tr>';
		html += '</table>';	
	return html;
}

function conteudoSemAjudaRelacionada(){
	var html = '<br/><table style="cursor:pointer;padding : 0px;font-family:Arial, sans-serif;font-size: 11px;">';
		html += '	<tr>';
		html += '		<td><hr/>';
		html += 			conteudoNaoReceberAjudaTutor();
		html += '		</td>';
		html += '	</tr>';
		html += '</table>';	
	return html;
}