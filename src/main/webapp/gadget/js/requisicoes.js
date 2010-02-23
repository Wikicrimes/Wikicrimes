//Solicita da rede social os dados do usuário logado e de seus amigos
function getPessoas(){					
	//document.getElementById("content_info").innerHTML = "<img src='"+linkAplication+"/images/spinner.gif'> </img><b>Carregando dados do seus amigos...</b>";
	var req = opensocial.newDataRequest();
	var params = {};
	params[opensocial.DataRequest.PeopleRequestFields.PROFILE_DETAILS] = [opensocial.Person.Field.ADDRESSES];
	req.add(req.newFetchPersonRequest('OWNER', params),'usuario');
	var paramsFriends = { };
	paramsFriends[opensocial.DataRequest.PeopleRequestFields.PROFILE_DETAILS] = [
	opensocial.Person.Field.PROFILE_URL,
	opensocial.Person.Field.THUMBNAIL_URL,
	opensocial.Person.Field.GENDER ];
	paramsFriends[opensocial.DataRequest.PeopleRequestFields.FIRST] = 0;
	paramsFriends[opensocial.DataRequest.PeopleRequestFields.MAX] = 1000;
	req.add(req.newFetchPeopleRequest('OWNER_FRIENDS',paramsFriends), 'amigos');
	req.send(carregarAmigos);
};

//Resposta com os dados do usuário logado e de seus amigos
function carregarAmigos(dataResponse) {								
	var usuario = dataResponse.get('usuario').getData();
	var endereco =usuario.getField(opensocial.Person.Field.ADDRESSES);
	usuarioRedeSocial.idUsuario = usuario.getId();
	usuarioRedeSocial.nome = usuario.getDisplayName();
	usuarioRedeSocial.linkPerfil = usuario.getField(opensocial.Person.Field.PROFILE_URL)
	usuarioRedeSocial.idRedeSocial = opensocial.getEnvironment().getDomain();
	try{	
		usuarioRedeSocial.cidade = endereco[0].getField('locality');
		usuarioRedeSocial.pais = endereco[0].getField('country');
	}catch(e){
		usuarioRedeSocial.cidade = "Fortaleza";
		usuarioRedeSocial.pais = "Brasil";
	}
	var amigosRede = dataResponse.get('amigos').getData();
	var contAmigos = 0;
	amigosRede.each(function(pessoa) {						
		amigos[contAmigos] = new Amigo();
		amigos[contAmigos].linkPerfil = pessoa.getField(opensocial.Person.Field.PROFILE_URL);
		amigos[contAmigos].foto = pessoa.getField(opensocial.Person.Field.THUMBNAIL_URL);
		amigos[contAmigos].id = pessoa.getId() ;
		amigos[contAmigos].nome = pessoa.getDisplayName();																							
		contAmigos ++;					
	})
	carregouAmigos = true;
	
	executaRequisicaoVerificarRegistro();    
};

//Executa requisição para recuperar os crimes do banco
function executaRequisicaoListagem() {
	
	var b = map.getBounds();
	
	var north = b.getNorthEast().lat();
	var south = b.getSouthWest().lat();
	var east = b.getNorthEast().lng();
	var west = b.getSouthWest().lng();
	
	var params = {};
	params[gadgets.io.RequestParameters.AUTHORIZATION] = gadgets.io.AuthorizationType.SIGNED;
	params[gadgets.io.RequestParameters.CONTENT_TYPE] = gadgets.io.ContentType.DOM;				
	//document.getElementById("content_info").innerHTML = "<img src='"+linkAplication+"/images/spinner.gif'> </img><b>"+prefs.getMsg('carregando')+"</b>";
	var url = linkAplication+"ServletOpensocial?acao=listCrimes&dominioRedeSocial="+opensocial.getEnvironment().getDomain() + "&n=" +north+ "&s=" +south+"&e=" +east + "&w="+west+"&x=1";
	gadgets.io.makeRequest(url, respostaRequisicaoListagem, params);				
}

//Resposta requisição para recuperar os crimes do banco
function respostaRequisicaoListagem(obj){
	var responseText = obj.data.getElementsByTagName("dados").item(0).getAttribute("texto");
	
	if(responseText == "nao_permitir")
		alert("Você não tem permissão para acessar essa funcionalidade");
	else	
		montaArrayCrimes(responseText);
}


function executaRequisicaoVerificarRegistro() {
	var params = {};
	params[gadgets.io.RequestParameters.AUTHORIZATION] = gadgets.io.AuthorizationType.SIGNED;
	params[gadgets.io.RequestParameters.POST_DATA] = 'acao=verificaRegistro&idUsuarioRedeSocial='+usuarioRedeSocial.idUsuario+'&dominioRedeSocial='+opensocial.getEnvironment().getDomain();
	params[gadgets.io.RequestParameters.METHOD] = gadgets.io.MethodType.POST;				  
	params[gadgets.io.RequestParameters.CONTENT_TYPE] = gadgets.io.ContentType.DOM;
	var url = linkAplication+"ServletOpensocial";					
	gadgets.io.makeRequest(url, respostaRequisicaoVerificarRegistro, params);							
}

function respostaRequisicaoVerificarRegistro(obj){
	var responseText =obj.data.getElementsByTagName("dados").item(0).getAttribute("texto");
	
	if(responseText == "nao_permitir"){
		alert("Você não tem permissão para acessar essa funcionalidade");
		return;
	}
		
	var resposta = responseText.split(";");					
	if(resposta[0]=='registrado'){
		montaTelaApp(resposta[1],resposta[2],16);  			
    }
	if(resposta[0]=='registrado_rede_social'){
		if (!geocoder) {						
			geocoder = new google.maps.Geocoder();
		}
		geocoder.geocode( { 'address': resposta[1] + "," + resposta[2]}, function(results, status) {
			 if (status == google.maps.GeocoderStatus.OK) {				
		         usuarioRedeSocial.latitude=results[0].geometry.location.lat();
			     usuarioRedeSocial.longitude=results[0].geometry.location.lng();
			     montaTelaApp(usuarioRedeSocial.latitude,usuarioRedeSocial.longitude,16);
			} else {
				return false;
			}
		});	
    }
	if(resposta[0]=='registrado'||resposta[0]=='registrado_rede_social'){
		if(resposta[3]=="0"){
			tutorEstaAtivado = false;
		}
	}
	
    if(resposta[0]=='nao_registrado'){
    	conteudoInstallApp();
    }
    
}

function executaRequisicaoRelatosMaisRecentes() {
	if(!carregouAmigos)return;
	var params = {};
	params[gadgets.io.RequestParameters.AUTHORIZATION] = gadgets.io.AuthorizationType.SIGNED;
	params[gadgets.io.RequestParameters.POST_DATA] = 'acao=relatosMaisRecentes&idUsuarioRedeSocial='+usuarioRedeSocial.idUsuario+'&dominioRedeSocial='+opensocial.getEnvironment().getDomain();
	params[gadgets.io.RequestParameters.METHOD] = gadgets.io.MethodType.POST;				  
	params[gadgets.io.RequestParameters.CONTENT_TYPE] = gadgets.io.ContentType.DOM;
	var url = linkAplication+"ServletOpensocial";					
	gadgets.io.makeRequest(url, repostaRequisicaoRelatosMaisRecentes, params);									
}

function repostaRequisicaoRelatosMaisRecentes(obj){
	var responseText = obj.data.getElementsByTagName("dados").item(0).getAttribute("texto");
	if(responseText == "nao_permitir"){
		alert("Você não tem permissão para acessar essa funcionalidade");
		return;
	}
	listarRelatosMaisRecentes(responseText);
	
}

function addToActivityStream(tipoAtividade) {     
	
	var params = {};
	
	var titulo = "";
	if(tipoAtividade==1)
		titulo = " está alertando você!";
	if(tipoAtividade==2)
		titulo = " comentou um alerta!";
	if(tipoAtividade==3)
		titulo = " confirmou um alerta!";
	
 	params[opensocial.Activity.Field.TITLE] = titulo;
 	params[opensocial.Activity.Field.BODY] =conteudoAtividade(tipoAtividade);
 	// params[opensocial.Activity.Field.TITLE] = "<a href='http://priya.com'>priya</a> thinks this should work at " + (new Date()).toString();
  // params[opensocial.Activity.Field.BODY] = "<img src='http://www.hot-screensaver.com/wp-myimages/funny-cat.jpg' />";
  
 	var activity = opensocial.newActivity( params );
 	opensocial.requestCreateActivity(activity, opensocial.CreateActivityPriority.HIGH, onAddActivity);
 	
} 

function onAddActivity (data){
	
}


function executaRequisicaoRecuperaComentarios(chaveRelato) {
	var params = {};
	params[gadgets.io.RequestParameters.AUTHORIZATION] = gadgets.io.AuthorizationType.SIGNED;
	params[gadgets.io.RequestParameters.CONTENT_TYPE] = gadgets.io.ContentType.DOM;
	var url = linkAplication+"ServletOpensocial?acao=recuperarComentarios&chaveRelato="+chaveRelato+"";					
	gadgets.io.makeRequest(url, repostaRequisicaoRecuperaComentarios, params);									
}

function repostaRequisicaoRecuperaComentarios(obj){
	var responseText = obj.data.getElementsByTagName("dados").item(0).getAttribute("texto");
	if(responseText == "nao_permitir"){
		alert("Você não tem permissão para acessar essa funcionalidade");
		return;
	}
	listarComentarios(responseText,1);
	limparMensagem();
}

function executaRequisicaoRecuperaComentariosCrime(chaveRelato) {
	var params = {};
	params[gadgets.io.RequestParameters.AUTHORIZATION] = gadgets.io.AuthorizationType.SIGNED;
	params[gadgets.io.RequestParameters.CONTENT_TYPE] = gadgets.io.ContentType.DOM;
	var url = linkAplication+"ServletOpensocial?acao=recuperarComentariosCrime&chaveCrime="+chaveRelato+"";					
	gadgets.io.makeRequest(url, repostaRequisicaoRecuperaComentariosCrime, params);									
}

function repostaRequisicaoRecuperaComentariosCrime(obj){
	var responseText = obj.data.getElementsByTagName("dados").item(0).getAttribute("texto");
	if(responseText == "nao_permitir"){
		alert("Você não tem permissão para acessar essa funcionalidade");
		return;
	}
	listarComentarios(responseText,0);
	limparMensagem();
}

function executaRequisicaoRealizaLogin() {
	var params = {};
	var paramAmigos = '';
    var cont = 0;
    for(i in amigos){
  	  if(cont==0)
  		paramAmigos +=amigos[i].id;
  	  else	
  		paramAmigos +=";"+amigos[i].id;
  	  cont++;
    }
	params[gadgets.io.RequestParameters.AUTHORIZATION] = gadgets.io.AuthorizationType.SIGNED;
	params[gadgets.io.RequestParameters.CONTENT_TYPE] = gadgets.io.ContentType.DOM;
	var url = linkAplication+"ServletOpensocial?acao=realizaLogin&idUsuarioRedeSocial="+usuarioRedeSocial.idUsuario+"&dominioRedeSocial="+opensocial.getEnvironment().getDomain()+"&login="+document.getElementById("login").value+"&senha="+document.getElementById("senha").value+"&vizualizarCrimeOpensocial="+vCrimeSelecionado+"&amigos="+paramAmigos;					
	gadgets.io.makeRequest(url, repostaRequisicaoRealizaLogin, params);							
}

function repostaRequisicaoRealizaLogin(obj){
	var responseText = obj.data.getElementsByTagName("dados").item(0).getAttribute("texto");
	
	if(responseText == "nao_permitir"){
		alert("Você não tem permissão para acessar essa funcionalidade");
		return;
	}
	
	var resposta = responseText.split(";");	
						
	if(resposta[0]=='valido'){
   		montaTelaApp(resposta[1],resposta[2],16);			   			
   	}
   	if(resposta[0]=='invalido'){
   		document.getElementById("content_erro").innerHTML = prefs.getMsg("login.senha.invalido"); 
   	}	
}

function executaRequisicaoRegistraUsuarioOrkut() {
	var params = {};
	var paramAmigos = '';
    var cont = 0;
    for(i in amigos){
  	  if(cont==0)
  		paramAmigos +=amigos[i].id;
  	  else	
  		paramAmigos +=";"+amigos[i].id;
  	  cont++;
    }
	params[gadgets.io.RequestParameters.AUTHORIZATION] = gadgets.io.AuthorizationType.SIGNED;
	params[gadgets.io.RequestParameters.CONTENT_TYPE] = gadgets.io.ContentType.DOM;
	usuarioRedeSocial.pais=document.getElementById("pais").value;
	usuarioRedeSocial.cidade=document.getElementById("cidade").value;
	var url = linkAplication+"ServletOpensocial?acao=registrarUsuarioOrkut&id="+usuarioRedeSocial.idUsuario+"&cidade=" + usuarioRedeSocial.cidade + "&pais=" + usuarioRedeSocial.pais+  "&dominioRedeSocial="+opensocial.getEnvironment().getDomain()+"&amigos="+paramAmigos;					
	gadgets.io.makeRequest(url, respostaRequisicaoRegistraUsuarioOrkut, params);								
}

function respostaRequisicaoRegistraUsuarioOrkut(obj){
	var responseText = obj.data.getElementsByTagName("dados").item(0).getAttribute("texto");
	
	if(responseText == "nao_permitir"){
		alert("Você não tem permissão para acessar essa funcionalidade");
		return;
	}
	
	var resposta = responseText.split(";");										
	if(resposta[0]=='registrado'){
	montaTelaApp(usuarioRedeSocial.latitude,usuarioRedeSocial.longitude,16);				   			
   	}
   	if(resposta[0]=='ja_registrado'){
   		document.getElementById("content_erro").innerHTML = 'usuario ja registrado'; 
   	}
}

function executaRequisicaoRegistraUsuarioWikiCrimes() {
	var params = {};
	params[gadgets.io.RequestParameters.AUTHORIZATION] = gadgets.io.AuthorizationType.SIGNED;
	params[gadgets.io.RequestParameters.CONTENT_TYPE] = gadgets.io.ContentType.DOM;
	params[gadgets.io.RequestParameters.METHOD] = gadgets.io.MethodType.POST;
	usuarioRedeSocial.pais=document.getElementById("pais").value;
	usuarioRedeSocial.cidade=document.getElementById("cidade").value;
	usuarioRedeSocial.nome=document.getElementById("primeiroNome").value;
	usuarioRedeSocial.email=document.getElementById("email").value;
	if (document.getElementById("comboEstado").value != '')
		usuarioRedeSocial.estado=document.getElementById("comboEstado").value;
	else	
		usuarioRedeSocial.estado=document.getElementById("inputEstado").value;
	params[gadgets.io.RequestParameters.POST_DATA] = 'acao=registrarUsuarioWikiCrimes&cidade=' + usuarioRedeSocial.cidade + '&pais=' + usuarioRedeSocial.pais+  '&nome='+ usuarioRedeSocial.nome +'&unome=' + document.getElementById("ultimoNome").value + '&email=' + usuarioRedeSocial.email + '&senha=' + document.getElementById("senha").value + '&estado=' + usuarioRedeSocial.estado + '&news=' + document.getElementById("receberNewsletter").value + '&idioma=' + document.getElementById("idiomaPref").value + '&lat=' + usuarioRedeSocial.latitude + '&lng=' + usuarioRedeSocial.longitude;	
	var url = linkAplication+"ServletOpensocial";					
	gadgets.io.makeRequest(url, respostaRequisicaoRegistraUsuarioWikiCrimes, params);								
}
function respostaRequisicaoRegistraUsuarioWikiCrimes(obj){
	var responseText = obj.data.getElementsByTagName("dados").item(0).getAttribute("texto");
	
	if(responseText == "nao_permitir"){
		alert("Você não tem permissão para acessar essa funcionalidade");
		return;
	}
	
	var resposta = responseText.split(";");										
	if(resposta[0]=='registrado'){
		montaTelaApp(usuarioRedeSocial.latitude,usuarioRedeSocial.longitude,16);				   			
   	}
   	if(resposta[0]=='email_existente'){
   		document.getElementById("content_erro").innerHTML = 'E-mail ja consta em nossa base. Esqueceu sua senha?'; 
   	}
}

function executaRequisicaoRegistrarAlertaCrime(descricao, lat, lng, razoes){
	descricao = codificaCaracteres(descricao);
	var params = {};
	
	var paramAmigos = '';
    var cont = 0;
    for(i in amigos){
  	  if(cont==0)
  		paramAmigos +=amigos[i].id;
  	  else	
  		paramAmigos +=";"+amigos[i].id;
  	  cont++;
    }
    var tipoCrime = document.getElementById('tipoCrime').value;
    var tipoVitima = document.getElementById('tipoVitima').value;
    var tipoLocal = document.getElementById('tipoLocal').value;
    var data = document.getElementById('data').value;
    var horario = document.getElementById('horario').value;
    var qtdVitimas = document.getElementById('qtdV').value;
    var qtdCriminosos = document.getElementById('qtdC').value;
    var tipoArmaUsada = document.getElementById('armaUsada').value;
   
    
    //descricao = escape(descricao);   
	paramAmigos += ";"+usuarioRedeSocial.idUsuario;
	params[gadgets.io.RequestParameters.AUTHORIZATION] = gadgets.io.AuthorizationType.SIGNED;
	params[gadgets.io.RequestParameters.POST_DATA] = 'acao=registrarCrime&lat='+lat+'&lng='+lng+'&idUsuarioRedeSocial='+usuarioRedeSocial.idUsuario+'&descricao='+descricao+'&dominioRedeSocial='+opensocial.getEnvironment().getDomain()+'&razoes='+razoes+'&amigos='+paramAmigos+'&rcrime='+rcrime+'&polInfo='+polInfo+'&tipoVitima='+tipoVitima+'&tipoLocal='+tipoLocal+'&data='+data+'&hora='+horario+'&qtdV='+qtdVitimas+'&qtdC='+qtdCriminosos+'&tpa='+tipoArmaUsada+'&tipoCrime='+tipoCrime;
	params[gadgets.io.RequestParameters.METHOD] = gadgets.io.MethodType.POST;				  				  
  	params[gadgets.io.RequestParameters.CONTENT_TYPE] = gadgets.io.ContentType.DOM;										
	var url = linkAplication+"ServletOpensocial";
	gadgets.io.makeRequest(url, repostaRequisicaoRegistrarAlertaCrime, params);					
}

function repostaRequisicaoRegistrarAlertaCrime(obj){
	var responseText = obj.data.getElementsByTagName("dados").item(0).getAttribute("texto");
	polInfo="1";
	rcrime="1";	
	if(responseText == "nao_permitir"){
		alert("Você não tem permissão para acessar essa funcionalidade");
		return;
	}
	
	if(responseText=='ok'){
		auxRegistrouAlerta = true;	
		marcadorAlertaAmigos.closeInfoWindow();
		mostrarMensagem('success', prefs.getMsg("alerta.registrado"),true);
		executaRequisicaoRelatosMaisRecentes();
		addToActivityStream(1);
	}
	
}

function executaRequisicaoRegistrarAlerta(descricao, lat, lng, razoes){
	descricao = codificaCaracteres(descricao);
	var params = {};
	var turno='';
	if(document.getElementById('cMadrugada').checked)
		turno+='1;';
	else
		turno+='0;';
	if(document.getElementById('cManha').checked)
		turno+='1;';
	else
		turno+='0;';
	if(document.getElementById('cTarde').checked)
		turno+='1;';
	else
		turno+='0;';
	if(document.getElementById('cNoite').checked)
		turno+='1';
	else
		turno+='0';
		
	var paramAmigos = '';
    var cont = 0;
    for(i in amigos){
  	  if(cont==0)
  		paramAmigos +=amigos[i].id;
  	  else	
  		paramAmigos +=";"+amigos[i].id;
  	  cont++;
    }
    //descricao = escape(descricao);   
	paramAmigos += ";"+usuarioRedeSocial.idUsuario;
	params[gadgets.io.RequestParameters.AUTHORIZATION] = gadgets.io.AuthorizationType.SIGNED;
	params[gadgets.io.RequestParameters.POST_DATA] = 'acao=registrarRelato&lat='+lat+'&lng='+lng+'&idUsuarioRedeSocial='+usuarioRedeSocial.idUsuario+'&descricao='+descricao+'&dominioRedeSocial='+opensocial.getEnvironment().getDomain()+'&turno='+turno+'&razoes='+razoes+'&amigos='+paramAmigos;
	params[gadgets.io.RequestParameters.METHOD] = gadgets.io.MethodType.POST;				  				  
  	params[gadgets.io.RequestParameters.CONTENT_TYPE] = gadgets.io.ContentType.DOM;										
	var url = linkAplication+"ServletOpensocial";
	gadgets.io.makeRequest(url, repostaRequisicaoRegistrarAlerta, params);					
}

function repostaRequisicaoRegistrarAlerta(obj){
	var responseText = obj.data.getElementsByTagName("dados").item(0).getAttribute("texto");
	
	if(responseText == "nao_permitir"){
		alert("Você não tem permissão para acessar essa funcionalidade");
		return;
	}
	
	if(responseText=='ok'){
			auxRegistrouAlerta = true;
			mostrarMensagem('success', prefs.getMsg("alerta.registrado"),true);
			executaRequisicaoRelatosMaisRecentes();
			addToActivityStream(1);
			infowindow.close();
	}
	
}
var tipoRespostaSalvarConfig = "";
var statusTutorSalvarConfiguracoes = "";
function executaRequisicaoSalvarConfiguracoes(status, tp) {
	tipoRespostaSalvarConfig = tp;
	var params = {};
	statusTutorSalvarConfiguracoes = status;
	
	params[gadgets.io.RequestParameters.AUTHORIZATION] = gadgets.io.AuthorizationType.SIGNED;
	params[gadgets.io.RequestParameters.POST_DATA] = 'acao=salvarConfiguracoes&idUsuarioRedeSocial='+usuarioRedeSocial.idUsuario+'&dominioRedeSocial='+opensocial.getEnvironment().getDomain()+'&statusTutor='+statusTutorSalvarConfiguracoes;
	params[gadgets.io.RequestParameters.METHOD] = gadgets.io.MethodType.POST;				  
	params[gadgets.io.RequestParameters.CONTENT_TYPE] = gadgets.io.ContentType.DOM;
	var url = linkAplication+"ServletOpensocial";					
	gadgets.io.makeRequest(url, respostaRequisicaoSalvarConfiguracoes, params);
};

function respostaRequisicaoSalvarConfiguracoes(obj) {
   var responseText = obj.data.getElementsByTagName("dados").item(0).getAttribute("texto");
	
	if(responseText == "nao_permitir"){
		alert("Você não tem permissão para acessar essa funcionalidade");
		return;
	}
	
	if(responseText == 'ok'){
		if(tipoRespostaSalvarConfig=="1")
			document.getElementById('mensagemInformacaoConfiguracoes').innerHTML = '<font color="green">'+prefs.getMsg("config.salvas")+' </font>';
		else
			mostrarModalAvisoDesativarTutor();
		
		if(statusTutorSalvarConfiguracoes=="1")
			tutorEstaAtivado = true;
		else
			tutorEstaAtivado = false;
	}
	if(responseText == 'erro'){		
		document.getElementById('mensagemInformacaoConfiguracoes').innerHTML = '<font color="red">Ocorreu um erro ao tentar processar seu ultimo pedido! </font>';		
	}
};

function executaRequisicaoComentarNotificacao(idNotificacao) {			     	
	var params = {};
	var descricao = document.getElementById('desc_comentario').value;
	descricao = codificaCaracteres(descricao);
	//descricao = escape(descricao);
	params[gadgets.io.RequestParameters.AUTHORIZATION] = gadgets.io.AuthorizationType.SIGNED;
	params[gadgets.io.RequestParameters.POST_DATA] = 'acao=registrarComentario&idUsuarioRedeSocial='+usuarioRedeSocial.idUsuario+'&dominioRedeSocial='+opensocial.getEnvironment().getDomain()+'&idNotificacao='+idNotificacao+'&descComentario='+descricao;
	params[gadgets.io.RequestParameters.METHOD] = gadgets.io.MethodType.POST;				  
	params[gadgets.io.RequestParameters.CONTENT_TYPE] = gadgets.io.ContentType.DOM;
	var url = linkAplication+"ServletOpensocial";					
	gadgets.io.makeRequest(url, respostaRequisicaoComentarNotificacao, params);
};

function respostaRequisicaoComentarNotificacao(obj) {
  //obj.data contains a Document DOM element corresponding to the page that was requested
	var responseText = obj.data.getElementsByTagName("dados").item(0).getAttribute("texto");
	
	if(responseText == "nao_permitir"){
		alert("Você não tem permissão para acessar essa funcionalidade");
		return;
	}
	
	if(responseText == 'ok'){
		if(htmlComentariosDescartar == '<br>Nenhum comentário sobre esse alerta')
			htmlComentariosDescartar = '';
		document.getElementById('comentarios').innerHTML = '<br><div><img width=22px height=20px src='+ linkAplication+'images/widget/comentar.PNG' +' /> <b>'+usuarioRedeSocial.nome+' comenta:</b> </div><br/>'+document.getElementById('desc_comentario').value+'<br> <hr/>'+ htmlComentariosDescartar;
		mostrarMensagem('success', prefs.getMsg("comentario.registrado"),true);
		addToActivityStream(2);
	}
};

function executaRequisicaoComentarNotificacaoCrime(idNotificacao) {			     	
	var params = {};
	var descricao = document.getElementById('desc_comentario').value;
	descricao = codificaCaracteres(descricao);
	//descricao = escape(descricao);
	params[gadgets.io.RequestParameters.AUTHORIZATION] = gadgets.io.AuthorizationType.SIGNED;
	params[gadgets.io.RequestParameters.POST_DATA] = 'acao=registrarComentarioCrime&idUsuarioRedeSocial='+usuarioRedeSocial.idUsuario+'&dominioRedeSocial='+opensocial.getEnvironment().getDomain()+'&idNotificacao='+idNotificacao+'&descComentario='+descricao;
	params[gadgets.io.RequestParameters.METHOD] = gadgets.io.MethodType.POST;				  
	params[gadgets.io.RequestParameters.CONTENT_TYPE] = gadgets.io.ContentType.DOM;
	var url = linkAplication+"ServletOpensocial";					
	gadgets.io.makeRequest(url, respostaRequisicaoComentarNotificacaoCrime, params);
};

function respostaRequisicaoComentarNotificacaoCrime(obj) {
  //obj.data contains a Document DOM element corresponding to the page that was requested
	var responseText = obj.data.getElementsByTagName("dados").item(0).getAttribute("texto");
	
	if(responseText == "nao_permitir"){
		alert("Você não tem permissão para acessar essa funcionalidade");
		return;
	}
	
	if(responseText == 'ok'){
		if(htmlComentariosDescartar == '<br>Nenhum comentário sobre esse alerta')
			htmlComentariosDescartar = '';
		document.getElementById('comentarios').innerHTML = '<br><div><img width=22px height=20px src='+ linkAplication+'images/widget/comentar.PNG' +' /> <b>'+usuarioRedeSocial.nome+' comenta:</b> </div><br/>'+document.getElementById('desc_comentario').value+'<br> <hr/>'+ htmlComentariosDescartar;
		mostrarMensagem('success', prefs.getMsg("comentario.registrado"),true);
		addToActivityStream(2);
	}
};

function executaRequisicaoConfirmarNotificacao(idNotificacao, confirma) {
	if(gadgets.util.getUrlParameters()["gadgetOwner"]!=gadgets.util.getUrlParameters()["gadgetViewer"]){
		mostrarMensagem('warning', prefs.getMsg("sem.permissao"),true);
	}else{					     	
	var params = {};
		params[gadgets.io.RequestParameters.AUTHORIZATION] = gadgets.io.AuthorizationType.SIGNED;
		params[gadgets.io.RequestParameters.POST_DATA] = 'acao=confirmarNotificacao&idUsuarioRedeSocial='+usuarioRedeSocial.idUsuario+'&dominioRedeSocial='+opensocial.getEnvironment().getDomain()+'&idNotificacao='+idNotificacao+'&confirma='+confirma;		 
		params[gadgets.io.RequestParameters.METHOD] = gadgets.io.MethodType.POST;				  
		params[gadgets.io.RequestParameters.CONTENT_TYPE] = gadgets.io.ContentType.DOM;
		var url = linkAplication+"ServletOpensocial";					
		gadgets.io.makeRequest(url, respostaRequisicaoConfirmarNotificacao, params);
	}   
};

function respostaRequisicaoConfirmarNotificacao(obj) {
  //obj.data contains a Document DOM element corresponding to the page that was requested
	var responseText = obj.data.getElementsByTagName("dados").item(0).getAttribute("texto");
	
	
	if(responseText == "nao_permitir"){
		alert("Você não tem permissão para acessar essa funcionalidade");
		return;
	}
	
	if(responseText == "ok"){
		mostrarMensagem('success', prefs.getMsg("confirmacao.registrada"), true);
		addToActivityStream(3);
	}	
	else if(responseText == "ja confirmou")
		mostrarMensagem('warning', prefs.getMsg("ja.confirmou"), true);
};

function executaRequisicaoConfirmarNotificacaoCrime(idNotificacao, confirma) {
	if(gadgets.util.getUrlParameters()["gadgetOwner"]!=gadgets.util.getUrlParameters()["gadgetViewer"]){
		mostrarMensagem('warning', prefs.getMsg("sem.permissao"),true);
	}else{					     	
	var params = {};
		params[gadgets.io.RequestParameters.AUTHORIZATION] = gadgets.io.AuthorizationType.SIGNED;
		params[gadgets.io.RequestParameters.POST_DATA] = 'acao=confirmarNotificacaoCrime&idUsuarioRedeSocial='+usuarioRedeSocial.idUsuario+'&dominioRedeSocial='+opensocial.getEnvironment().getDomain()+'&idNotificacao='+idNotificacao+'&confirma='+confirma;		 
		params[gadgets.io.RequestParameters.METHOD] = gadgets.io.MethodType.POST;				  
		params[gadgets.io.RequestParameters.CONTENT_TYPE] = gadgets.io.ContentType.DOM;
		var url = linkAplication+"ServletOpensocial";					
		gadgets.io.makeRequest(url, respostaRequisicaoConfirmarNotificacaoCrime, params);
	}   
};

function respostaRequisicaoConfirmarNotificacaoCrime(obj) {
  //obj.data contains a Document DOM element corresponding to the page that was requested
	var responseText = obj.data.getElementsByTagName("dados").item(0).getAttribute("texto");				  
	
	if(responseText == "nao_permitir"){
		alert("Você não tem permissão para acessar essa funcionalidade");
		return;
	}
	
	if(responseText == "ok"){
		mostrarMensagem('success', prefs.getMsg("confirmacao.registrada"), true);
		addToActivityStream(3);
	}	
	else if(responseText == "ja confirmou")
		mostrarMensagem('warning', prefs.getMsg("ja.confirmou"), true);
};

function executaRequisicaoRepassarRelato(idNotificacao) {
 	if(gadgets.util.getUrlParameters()["gadgetOwner"]!=gadgets.util.getUrlParameters()["gadgetViewer"]){
		mostrarMensagem('warning', prefs.getMsg("sem.permissao"),true);
	}else{					     	
		var params = {};
		params[gadgets.io.RequestParameters.AUTHORIZATION] = gadgets.io.AuthorizationType.SIGNED;
		params[gadgets.io.RequestParameters.METHOD] = gadgets.io.MethodType.POST;				  
		params[gadgets.io.RequestParameters.CONTENT_TYPE] = gadgets.io.ContentType.DOM;
		var paramAmigos = '';
		var cont = 0;
		for(i in amigos){
			if(cont==0)
				paramAmigos +=amigos[i].id;
			else	
				paramAmigos +=";"+amigos[i].id;
			cont++;
  		}
	 	params[gadgets.io.RequestParameters.POST_DATA] = 'acao=repassarRelato&idUsuarioRedeSocial='+usuarioRedeSocial.idUsuario+'&dominioRedeSocial='+opensocial.getEnvironment().getDomain()+'&idNotificacao='+idNotificacao+'&amigos='+paramAmigos;
	  	var url = linkAplication+"ServletOpensocial";					
	  	gadgets.io.makeRequest(url, respostaRequisicaoRepassarRelato, params);
	 }   
};

function respostaRequisicaoRepassarRelato(obj) {
  //obj.data contains a Document DOM element corresponding to the page that was requested
	var responseText = obj.data.getElementsByTagName("dados").item(0).getAttribute("texto");				  
	
	if(responseText == "nao_permitir"){
		alert("Você não tem permissão para acessar essa funcionalidade");
		return;
	}
	
	if(responseText == "ok"){
		mostrarMensagem('success', prefs.getMsg("repasse.efetuado"), true);
		addToActivityStream(1);
	}	
	else 
		mostrarMensagem('error', prefs.getMsg("erro.requisicao"), true);
	
	fecharModal();	
};

function executaRequisicaoRepassarCrime(idNotificacao) {
 	if(gadgets.util.getUrlParameters()["gadgetOwner"]!=gadgets.util.getUrlParameters()["gadgetViewer"]){
		mostrarMensagem('warning', prefs.getMsg("sem.permissao"),true);
	}else{					     	
		var params = {};
		params[gadgets.io.RequestParameters.AUTHORIZATION] = gadgets.io.AuthorizationType.SIGNED;
		params[gadgets.io.RequestParameters.METHOD] = gadgets.io.MethodType.POST;				  
		params[gadgets.io.RequestParameters.CONTENT_TYPE] = gadgets.io.ContentType.DOM;
		var paramAmigos = '';
		var cont = 0;
		for(i in amigos){
			if(cont==0)
				paramAmigos +=amigos[i].id;
			else	
				paramAmigos +=";"+amigos[i].id;
			cont++;
  		}
	 	params[gadgets.io.RequestParameters.POST_DATA] = 'acao=repassarCrime&idUsuarioRedeSocial='+usuarioRedeSocial.idUsuario+'&dominioRedeSocial='+opensocial.getEnvironment().getDomain()+'&idNotificacao='+idNotificacao+'&amigos='+paramAmigos;
	  	var url = linkAplication+"ServletOpensocial";					
	  	gadgets.io.makeRequest(url, respostaRequisicaoRepassarCrime, params);
	 }   
};

function respostaRequisicaoRepassarCrime(obj) {
  //obj.data contains a Document DOM element corresponding to the page that was requested
	var responseText = obj.data.getElementsByTagName("dados").item(0).getAttribute("texto");				  
	
	if(responseText == "nao_permitir"){
		alert("Você não tem permissão para acessar essa funcionalidade");
		return;
	}
	
	if(responseText == "ok"){
		mostrarMensagem('success', prefs.getMsg("repasse.efetuado"), true);
		addToActivityStream(1);
	}
	else 
		mostrarMensagem('error', prefs.getMsg("erro.requisicao"), true);
	
	fecharModal();	
};

function executaRequisicaoFaleConosco() {			     	
	var params = {};
	var descricao = document.getElementById('desc_fale_conosco').value;
	descricao = codificaCaracteres(descricao);
	params[gadgets.io.RequestParameters.AUTHORIZATION] = gadgets.io.AuthorizationType.SIGNED;
	params[gadgets.io.RequestParameters.POST_DATA] = 'acao=faleConosco&idUsuarioRedeSocial='+usuarioRedeSocial.idUsuario+'&dominioRedeSocial='+opensocial.getEnvironment().getDomain()+'&descFaleConosco='+descricao;
	params[gadgets.io.RequestParameters.METHOD] = gadgets.io.MethodType.POST;				  
	params[gadgets.io.RequestParameters.CONTENT_TYPE] = gadgets.io.ContentType.DOM;
	var url = linkAplication+"ServletOpensocial";					
	gadgets.io.makeRequest(url, respostaRequisicaoFaleConosco, params);
};

function respostaRequisicaoFaleConosco(obj) {
 
	var responseText = obj.data.getElementsByTagName("dados").item(0).getAttribute("texto");
	
	if(responseText == "nao_permitir"){
		alert("Você não tem permissão para acessar essa funcionalidade");
		return;
	}
	
	if(responseText == 'ok'){
		document.getElementById('desc_fale_conosco').value = "";				    
		fecharModal();				  	
		mostrarMensagem('success', prefs.getMsg("email.fale.conosco.enviado"),true);
	}
};
