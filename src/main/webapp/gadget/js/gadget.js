//Inicializa o mapa
function initialize() {
	//Este método se encontra o arquivo requisicoes.js
	getPessoas();
	//Este método se encontra o arquivo requisicoes.js	   				   					 			
}

function getViewName() {	
    return gadgets.views.getCurrentView().getName();
}

function montaTelaApp(latitude, longitude, zoom){
	 if (getViewName() == "canvas") {
		if(opensocial.getEnvironment().getDomain() == 'orkut.com') 
			document.getElementById("bodyContent").innerHTML = conteudoCanvasOrkut();
		else
			document.getElementById("bodyContent").innerHTML = conteudoCanvasNing();		
	 }				
	 if (getViewName() == "profile") {
		 tutorEstaAtivado = false; 
	   	document.getElementById("bodyContent").innerHTML = conteudoPerfil();
	   	qtdMoveMap++;					   	
	 }
	 		   	 	   	 
   	 document.getElementById("botao_pesquisar").value = prefs.getMsg("pesquisar.no.mapa");
   	 document.getElementById("pesquisa").title = prefs.getMsg("pesquise.um.endereco.no.mapa");
//   if (GBrowserIsCompatible()) {						
   		var myOptions = {
   		    zoom: 8,
   		    center: new google.maps.LatLng(-8.16100515602485, -34.9125123023987),
   		    mapTypeId: google.maps.MapTypeId.ROADMAP
   		};
   		map = new google.maps.Map(document.getElementById("map"), myOptions);
   		infowindow = new google.maps.InfoWindow(
   				{ 
   					content: ''
   				}); 
   		 //map = new GMap2(document.getElementById('map'));
   		if(opensocial.getEnvironment().getDomain() == 'orkut.com'){
	   		if (getViewName() == "canvas") {	
				if(navigator.appName=='Netscape' || navigator.appName=='Opera')
					mostraDivLegendaMapa('319px','380px',conteudoLegenda());
				else
					mostraDivLegendaMapa('308px','366px',conteudoLegenda());
	   		}
   		}else{
   			if (getViewName() == "canvas") {	
				if(navigator.appName=='Netscape' || navigator.appName=='Opera')
					mostraDivLegendaMapa('319px','288px',conteudoLegenda());
				else
					mostraDivLegendaMapa('308px','274px',conteudoLegenda());
	   		}
   		}	
		if (latitude==''){ 
			map.setCenter(new google.maps.LatLng(-8.16100515602485, -34.9125123023987));
			map.setZoom(16);
		}	
		else{
			map.setCenter(new google.maps.LatLng(latitude, longitude));
			map.setZoom(zoom);
		}	
		
		google.maps.event.addListener(map, "tilesloaded", function() {
			if (podeCarregarCrimes) {
        	    limpaTela=false;
			 	atualizaMapa();
			 	situacao['qtd_move_map']++;
			 	alterarEstadoSituacao(situacao);
		  	}
		});
		
		google.maps.event.addListener(map, "zoom_changed", function() {
			situacao['zoom'] = map.getZoom();
		});
		setTimeout('executaRequisicaoListagem()',500);
}							

function montaArrayCrimes(responseText){
	var crimes_texto = responseText.split('|');				
	var crime = null;
	var eDeAmigo = false;
	for(var i=0; i<crimes_texto.length;i++){
		if(crimes_texto[i]!='' && crimes_texto[i]!=null){
			crime = crimes_texto[i].split('#');
			if(crime[0]!=null && crime[0]!= undefined ){
				if(crimes[crime[1]] == undefined){	
					crimes[crime[1]] = new Crime();
					crimes[crime[1]].chave = crime[1];
					crimes[crime[1]].latitude = crime[3];
					crimes[crime[1]].longitude = crime[4];
					crimes[crime[1]].tipo_crime = crime[5];
					crimes[crime[1]].id_usuario_rede_social = crime[2];
					crimes[crime[1]].id = crime[6];
					crimes[crime[1]].estaNoMapa = true;
//					for(var j = 0 ; j <= amigos.length ; j++ ){
//																					
//						if(amigos[j]!=null){
//								
//							if( amigos[j].id == crimes[crime[1]].id_usuario_rede_social ){
//								crimes[crime[1]].marcador = criaMarcador(crimes[crime[1]], constroiHtml(amigos[j]));
//								crimes[crime[1]].marcador.setMap(map);
//								eDeAmigo = true;
//								break;
//							}
//						}								
//					}
					if(!eDeAmigo){
						crimes[crime[1]].marcador = criaMarcador(crimes[crime[1]], null);
						crimes[crime[1]].marcador.setMap(map);
					}	
				}
				else
					crimes[crime[1]].estaNoMapa = true;		
				
				eDeAmigo = false;
						
				crime = null;
				
			}
		}
	}
	for (k in crimes){
		
		if(crimes[k]!=undefined){	
			if(!crimes[k].estaNoMapa){
				crimes[k].marcador.setMap(null);
				crimes[k]=undefined;								
			}
			else
				crimes[k].estaNoMapa = false;
			
			
			if(k == crimeInfoWindow){								
				google.maps.event.trigger(crimes[k].marcador,'click');
				map.setZoom(16);
				crimeInfoWindow = null;
			}		
		}	
	}
	if(qtdMoveMap==0){
		executaRequisicaoRelatosMaisRecentes();
	}
	qtdMoveMap++;
}

function criaMarcador(crime, htmlUser) {
	
	var icone = null;//new GIcon(G_DEFAULT_ICON);
	var sizeMarker = null;
	var anchorMarker = null;
	if(htmlUser==null){
		sizeMarker = new google.maps.Size(14, 24);
		anchorMarker = new google.maps.Point(2, 24);
	}	
	else{
		sizeMarker = new google.maps.Size(27,24);
		anchorMarker = new google.maps.Point(2,22);
	}	
	if(crime.tipo_crime == 'tipocrime.roubo' || crime.tipo_crime == 'tipocrime.tentativaderoubo' || crime.tipo_crime == '1' || crime.tipo_crime == '4' ){
		if(htmlUser==null){
			icone = new google.maps.MarkerImage(linkAplication+'images/widget/vermelho.png',
					sizeMarker,					
					new google.maps.Point(0,0),
					anchorMarker); 
		}	
		else{
			icone = new google.maps.MarkerImage(linkAplication+'images/widget/vermelho_amigo.png',
					sizeMarker,					
					new google.maps.Point(0,0),
					anchorMarker); 
		}	
	}
	if(crime.tipo_crime == 'tipocrime.furto' || crime.tipo_crime == 'tipocrime.tentativadefurto' || crime.tipo_crime == '2' || crime.tipo_crime == '3'){
		if(htmlUser==null){			
			icone = new google.maps.MarkerImage(linkAplication+'images/widget/azul.png',
					sizeMarker,					
					new google.maps.Point(0,0),
					anchorMarker); 
		}	
		else{
			icone = new google.maps.MarkerImage(linkAplication+'images/widget/azul_amigo.png',
					sizeMarker,					
					new google.maps.Point(0,0),
					anchorMarker); 
		}
	}
	if(crime.tipo_crime == 'tipocrime.violencia'|| crime.tipo_crime == '5'){
		if(htmlUser==null){
			icone = new google.maps.MarkerImage(linkAplication+'images/widget/laranja.png',
					sizeMarker,					
					new google.maps.Point(0,0),
					anchorMarker); 
		}	
		else{
			
			icone = new google.maps.MarkerImage(linkAplication+'images/widget/laranja_amigo.png',
					sizeMarker,					
					new google.maps.Point(0,0),
					anchorMarker); 
		}	
	}
	if(crime.tipo_crime == '6'){					
		sizeMarker = new google.maps.Size(24,32);
		anchorMarker = new google.maps.Point(2,22);
		if(htmlUser==null){
			
			icone = new google.maps.MarkerImage(linkAplication+'images/baloes/novoMarcadorVerde.png',
					sizeMarker,					
					new google.maps.Point(0,0),
					anchorMarker); 
		}	
		else{
			icone = new google.maps.MarkerImage(linkAplication+'images/baloes/novoMarcadorVerde.png',
					sizeMarker,					
					new google.maps.Point(0,0),
					anchorMarker); 
		}	
			
		
			
	}	
	var marcador = //new GMarker(new GLatLng(crime.latitude, crime.longitude), markerOptions);
		new google.maps.Marker({
			position: new google.maps.LatLng(crime.latitude, crime.longitude),
			map: null,
			icon: icone
		 }); 
	google.maps.event.addListener(marcador, "click", function() {
		desabilitaTutor(4000);
		if(htmlUser==null){
			if(crime.tipo_crime == '6'){
				infowindow.setContent("<iframe src='"+linkAplication+"mostrarDadosRelatoOpensocial.html?idRelato=" + crime.id + "' height='150' frameborder='0'></iframe>");
				
			}
			else
				infowindow.setContent("<iframe src='"+linkAplication+"mostrarDadosOpenSocial.html?idCrime=" + crime.id + "' height='150' frameborder='0'></iframe>");
		}
		else{
			if(crime.tipo_crime == '6'){
				infowindow.setContent([new GInfoWindowTab(prefs.getMsg("ocorrencia"),"<iframe src='"+linkAplication+"mostrarDadosRelatoOpensocial.html?idRelato=" + crime.id + "'height='150' frameborder='0'></iframe>"),new GInfoWindowTab(prefs.getMsg("amigo"),htmlUser)]);
			}
			else
				infowindow.setContent([new GInfoWindowTab(prefs.getMsg("ocorrencia"),"<iframe src='"+linkAplication+"mostrarDadosOpenSocial.html?idCrime=" + crime.id + "' height='150' frameborder='0'></iframe>"),new GInfoWindowTab('Amigo',htmlUser)]);
		}
		infowindow.open(map,marcador);
	});
	return marcador;
}

function criaMarcadorTemp(latlng) {
	
	var sizeMarker = new google.maps.Size(24,32);
	var anchorMarker = new google.maps.Point(2,22);
	var icone = new google.maps.MarkerImage(linkAplication+'images/baloes/novoMarcadorVerde.png',
			sizeMarker,					
			new google.maps.Point(0,0),
			anchorMarker
	);
	var marcador = //new GMarker(new google.maps.LatLng(latlng.lat(), latlng.lng()), markerOptions);
		new google.maps.Marker({
			position: new google.maps.LatLng(latlng.lat(), latlng.lng()),
			map: null,
			icon: icone
		 });
	marcador.setDraggable(true);	
					
	return marcador;
}

function criaMarcadorTempCrime(latlng, tipoCrime) {
	var icone = null;
	
	var sizeMarker = new google.maps.Size(14, 24);
	var anchorMarker = new google.maps.Point(2, 24);
	if(tipoCrime=='1' || tipoCrime=='4'){
		icone = new google.maps.MarkerImage(linkAplication+'images/baloes/vermelho.png',
				sizeMarker,					
				new google.maps.Point(0,0),
				anchorMarker
		);
	}	
	if(tipoCrime=='2' || tipoCrime=='3'){
		icone = new google.maps.MarkerImage(linkAplication+'images/baloes/azul.png',
				sizeMarker,					
				new google.maps.Point(0,0),
				anchorMarker
		);
	}	
	if(tipoCrime=='5'){		
		icone = new google.maps.MarkerImage(linkAplication+'images/baloes/laranja.png',
				sizeMarker,					
				new google.maps.Point(0,0),
				anchorMarker
		);
	}
	
	var marcador = //new GMarker(new google.maps.LatLng(latlng.lat(), latlng.lng()), markerOptions);
		new google.maps.Marker({
			position: new google.maps.LatLng(latlng.lat(), latlng.lng()),
			map: null,
			icon: icone
		 });
	marcador.setDraggable(true);
	
	return marcador;
}

var estaRegistrando = false;

function cancelarRegistroAlerta(){
	infowindowtemp.close();
	if(!auxRegistrouAlerta)
		marcadorAlertaAmigos.setMap(null);				
	var myOptions = {
		draggable:true
	};
	//marcadorAlertaAmigos = null;
	map.setOptions(myOptions);
	auxRegistrouAlerta = false;
	estaRegistrando = false;
	podeCarregarCrimes = true;
	infowindowtemp = null;
}

function mostraJanelaRegistroAlerta(){
	infowindowtemp.close();
	infowindowtemp = null;
	var novoCrime = new Crime();
	novoCrime.tipo_crime = '6';
	novoCrime.latitude = marcadorAlertaAmigos.getPosition().lat();
	novoCrime.longitude = marcadorAlertaAmigos.getPosition().lng();
	
	marcadorAlertaAmigos.setMap(null);
	marcadorAlertaAmigos=criaMarcador(novoCrime, null);
	desabilitaTutor(2000);
	marcadorAlertaAmigos.setMap(map);   	
	infowindow.setContent(constroiHtmlAlerta());
	infowindow.open(map,marcadorAlertaAmigos);
	google.maps.event.addListener(infowindow, "closeclick", function (latLng) {
		if(!auxRegistrouAlerta)
			marcadorAlertaAmigos.setMap(null);
		
		map.setCenter(new google.maps.LatLng(marcadorAlertaAmigos.getPosition().lat(),marcadorAlertaAmigos.getPosition().lng()+0.0000001),map.getZoom());	
		auxRegistrouAlerta = false;
		estaRegistrando = false;
		podeCarregarCrimes = true;
	});
}
var infowindowtemp = null;
function registrarAlerta(){	
	escondeDivSelecaoAlerta();
	if(!estaRegistrando){	
		if(gadgets.util.getUrlParameters()["gadgetOwner"]!=gadgets.util.getUrlParameters()["gadgetViewer"]){
			mostrarMensagem('warning', prefs.getMsg("sem.permissao"),true);
		}else{
			podeCarregarCrimes = false;
			estaRegistrando = true;
			for (k in crimes){		
				if(crimes[k]!=undefined){
					crimes[k].marcador.setVisible(false);
					crimes[k]=undefined;
				}
			}
			infowindow.close();
			
			infowindowtemp = new google.maps.InfoWindow({ 
	   			content: '<b>'+prefs.getMsg("ms.registro1")+'</b> '+prefs.getMsg("ms.registro2")+' <a href="#" onclick="mostraJanelaRegistroAlerta();" >'+prefs.getMsg("aqui")+'</a> '+prefs.getMsg("ms.registro3")+'.<br/> '+prefs.getMsg("ms.registro4")+' <b>'+prefs.getMsg("ms.registro5")+'</b>, '+prefs.getMsg("ms.registro6")+' "'+prefs.getMsg("pesquisar.no.mapa")+'"' 
	   		}); 	
			marcadorAlertaAmigos = criaMarcadorTemp(map.getCenter());
			marcadorAlertaAmigos.setMap(map);
			marcadorAlertaAmigos.setDraggable(true);
			infowindowtemp.open(map,marcadorAlertaAmigos);
			google.maps.event.addListener(marcadorAlertaAmigos, "click", function() {
				infowindowtemp.open(map,marcadorAlertaAmigos);		        	  
			});
			google.maps.event.addListener(infowindowtemp, "closeclick", function (latLng) {
				cancelarRegistroAlerta();
			});		

		}
	}	
}

function mostraJanelaRegistroCrime(tipoCrime){
	infowindowtemp.close();
	infowindowtemp = null;
	var novoCrime = new Crime();
	novoCrime.tipo_crime = tipoCrime;
	novoCrime.latitude = marcadorAlertaAmigos.getPosition().lat();
	novoCrime.longitude = marcadorAlertaAmigos.getPosition().lng();
	
	marcadorAlertaAmigos.setMap(null);
	marcadorAlertaAmigos=criaMarcador(novoCrime, null);
	desabilitaTutor(2000);
	marcadorAlertaAmigos.setMap(map);   	
	infowindow.setContent(constroiHtmlAlertaCrime(tipoCrime));
	infowindow.open(map,marcadorAlertaAmigos);
	google.maps.event.addListener(infowindow, "closeclick", function (latLng) {
		if(!auxRegistrouAlerta)
			marcadorAlertaAmigos.setMap(null);
		
		map.setCenter(new google.maps.LatLng(marcadorAlertaAmigos.getPosition().lat(),marcadorAlertaAmigos.getPosition().lng()+0.0000001),map.getZoom());	
		auxRegistrouAlerta = false;
		estaRegistrando = false;
		podeCarregarCrimes = true;
	});
}

function registrarCrime(tipoCrime){
	
	escondeDivSelecaoAlerta();
	if(!estaRegistrando){	
		if(gadgets.util.getUrlParameters()["gadgetOwner"]!=gadgets.util.getUrlParameters()["gadgetViewer"]){
			mostrarMensagem('warning', prefs.getMsg("sem.permissao"),true);
		}else{
			podeCarregarCrimes = false;
			estaRegistrando = true;
			for (k in crimes){		
				if(crimes[k]!=undefined){
					crimes[k].marcador.setVisible(false);
					crimes[k]=undefined;
				}
			}
			infowindow.close();
			infowindowtemp = new google.maps.InfoWindow({ 
	   			content: '<b>'+prefs.getMsg("ms.registro1")+'</b> '+prefs.getMsg("ms.registro2")+' <a href="#" onclick="mostraJanelaRegistroCrime(\''+tipoCrime+'\');" >'+prefs.getMsg("aqui")+'</a> '+prefs.getMsg("ms.registro3")+'.<br/> '+prefs.getMsg("ms.registro4")+' <b>'+prefs.getMsg("ms.registro5")+'</b>, '+prefs.getMsg("ms.registro6")+' "'+prefs.getMsg("pesquisar.no.mapa")+'"' 
	   		});  	
			marcadorAlertaAmigos = criaMarcadorTempCrime(map.getCenter(),tipoCrime);
			marcadorAlertaAmigos.setMap(map);
			marcadorAlertaAmigos.setDraggable(true);
			infowindowtemp.open(map,marcadorAlertaAmigos);
//			google.maps.event.addListener(marcadorAlertaAmigos, "dragend", function() {
//				infowindowtemp.open(map,marcadorAlertaAmigos);
//			});
			google.maps.event.addListener(marcadorAlertaAmigos, "click", function() {
				infowindowtemp.open(map,marcadorAlertaAmigos);		        	  
			});
			google.maps.event.addListener(infowindowtemp, "closeclick", function (latLng) {
				cancelarRegistroAlerta();
			});		
		}
	}	
}

function validaFormRegistroAlerta(){
	var passouNaValidacao = true;
	if(document.getElementById("desc_alerta").value == ""){
		document.getElementById("erroDesc").innerHTML = prefs.getMsg("relato.descricao.requerida");
		passouNaValidacao = false;
		document.getElementById('desc_alerta').focus();
	}
	else{
		document.getElementById("erroDesc").innerHTML = "";
	}
	var selecionouUmTurno = false;
	if(document.getElementById('cMadrugada').checked)
		 selecionouUmTurno = true;
		
	if(document.getElementById('cManha').checked)
		selecionouUmTurno = true;
	
	if(document.getElementById('cTarde').checked)
		selecionouUmTurno = true;
		
	if(document.getElementById('cNoite').checked)
		selecionouUmTurno = true;
		
	if(!selecionouUmTurno){
		document.getElementById("erroTurno").innerHTML = prefs.getMsg("relato.turno.requerida");
		passouNaValidacao = false;
		document.getElementById('cMadrugada').focus();
	}
	else{
		document.getElementById("erroTurno").innerHTML = "";
	}
	var razoes = "";
	var contRazoes = 0;
	for(var i = 1 ;i<=20;i++){
		if(document.getElementById('razao'+i).checked){
			contRazoes++;
			if(razoes==""){
				razoes+=i;
			}else{							
				razoes+=";"+i;
			}	
		}	
	}
	
	if(contRazoes < 1 || contRazoes > 4){
		document.getElementById("erroRazoes").innerHTML = prefs.getMsg("relato.razao.requerida");
		passouNaValidacao = false;
		document.getElementById('razao1').focus();
	}else{
		document.getElementById("erroRazoes").innerHTML ="";
	}
		
	
	if(passouNaValidacao){
		
		executaRequisicaoRegistrarAlerta(document.getElementById('desc_alerta').value,marcadorAlertaAmigos.getPosition().lat(),marcadorAlertaAmigos.getPosition().lng(),razoes);
		estaRegistrando = false;
		podeCarregarCrimes = true;
		marcadorAlertaAmigos.setMap(null);
	}	
}

function validaFormRegistroAlertaCrime(){
	var passouNaValidacao = true;
	
	if(document.getElementById("tipoVitima").value == ""){		
		document.getElementById("erroTipoVitima").innerHTML = prefs.getMsg("alerta.campo.requerido");
		passouNaValidacao = false;
		document.getElementById('tipoVitima').focus();
	}
	else{
		document.getElementById("erroTipoVitima").innerHTML = "";
	}
	if(document.getElementById("tipoLocal").value == ""){		
		document.getElementById("erroTipoLocal").innerHTML = prefs.getMsg("alerta.campo.requerido");
		passouNaValidacao = false;
		document.getElementById('tipoLocal').focus();
	}
	else{
		document.getElementById("erroTipoLocal").innerHTML = "";
	}
	if(document.getElementById("data").value == ""){		
		document.getElementById("erroData").innerHTML = prefs.getMsg("alerta.campo.requerido");
		passouNaValidacao = false;
		document.getElementById('data').focus();
	}
	else{
		document.getElementById("erroData").innerHTML = "";
	}
	
	if(!ValidateForm()){
		passouNaValidacao = false;
	}	
	
	if(document.getElementById("horario").value == ""){		
		document.getElementById("erroHorario").innerHTML = prefs.getMsg("alerta.campo.requerido");
		passouNaValidacao = false;
		document.getElementById('horario').focus();
	}
	else{
		document.getElementById("erroHorario").innerHTML = "";
	}	
	
	
	if(document.getElementById("qtdC").value == ""){		
		document.getElementById("erroQtdC").innerHTML = prefs.getMsg("alerta.campo.requerido");
		passouNaValidacao = false;
		document.getElementById('qtdC').focus();
	}
	else{
		if(isNaN(document.getElementById("qtdC").value))
			document.getElementById("erroQtdC").innerHTML = prefs.getMsg("alerta.campo.numero");
		else
			document.getElementById("erroQtdC").innerHTML = "";
	}
	
	if(document.getElementById("qtdV").value == ""){		
		document.getElementById("erroQtdV").innerHTML = prefs.getMsg("alerta.campo.requerido");
		passouNaValidacao = false;
		document.getElementById('qtdV').focus();
	}
	else{
		if(isNaN(document.getElementById("qtdV").value))
			document.getElementById("erroQtdV").innerHTML = prefs.getMsg("alerta.campo.numero");
		else
			document.getElementById("erroQtdV").innerHTML = "";
	}
	
	if(document.getElementById("armaUsada").value == ""){		
		document.getElementById("erroArmaUsada").innerHTML = prefs.getMsg("alerta.campo.requerido");
		passouNaValidacao = false;
		document.getElementById('armaUsada').focus();
	}
	else{
		document.getElementById("erroArmaUsada").innerHTML = "";
	}
	
	if(document.getElementById("desc_alerta").value == ""){
		document.getElementById("erroDesc").innerHTML = prefs.getMsg("relato.descricao.requerida");
		passouNaValidacao = false;
		document.getElementById('desc_alerta').focus();
	}
	else{
		document.getElementById("erroDesc").innerHTML = "";
	}
	
	var razoes = "";
	var contRazoes = 0;
	for(var i = 1 ;i<=20;i++){
		if(document.getElementById('razao'+i).checked){
			contRazoes++;
			if(razoes==""){
				razoes+=i;
			}else{							
				razoes+=";"+i;
			}	
		}	
	}
	
	if(contRazoes < 1 || contRazoes > 4){
		document.getElementById("erroRazoes").innerHTML = prefs.getMsg("relato.razao.requerida");
		passouNaValidacao = false;
		document.getElementById('razao1').focus();
	}else{
		document.getElementById("erroRazoes").innerHTML ="";
	}		
	
	if(passouNaValidacao)		
		executaRequisicaoRegistrarAlertaCrime(document.getElementById('desc_alerta').value,marcadorAlertaAmigos.getPosition().lat(),marcadorAlertaAmigos.getPosition().lng(),razoes);
}


function replaceAll(string, token, newtoken) {
	while (string.indexOf(token) != -1) {
 		string = string.replace(token, newtoken);
	}
	return string;
}

function codificaCaracteres(descricao){
	descricao = replaceAll(descricao,"á","aagudo");
	descricao = replaceAll(descricao,"é","eagudo");
	descricao = replaceAll(descricao,"í","iagudo");
	descricao = replaceAll(descricao,"ó","oagudo");
	descricao = replaceAll(descricao,"ú","uagudo");
	descricao = replaceAll(descricao,"â","acircu");
	descricao = replaceAll(descricao,"ê","ecircu");
	descricao = replaceAll(descricao,"î","icircu");
	descricao = replaceAll(descricao,"ô","ocircu");
	descricao = replaceAll(descricao,"û","ucircu");
	descricao = replaceAll(descricao,"ç","cagudo");
	descricao = replaceAll(descricao,"ã","atil");
	descricao = replaceAll(descricao,"õ","otil");
	descricao = replaceAll(descricao,"à","acrase");
	return descricao;
}


function showLocal() {
	var endereco = document.getElementById("pesquisa").value;
	if (!geocoder) {						
		geocoder = new google.maps.Geocoder();
	}
//	geocoder.getLocations(endereco, plotaEnderecoMap);
//	geocoder = null;
	geocoder.geocode( { 'address': endereco}, function(results, status) {
		 if (status == google.maps.GeocoderStatus.OK) {
			 desabilitaTutor(2000);			 
			 map.fitBounds(results[0].geometry.bounds);
			 map.setCenter(results[0].geometry.location);
			 if(marcadorAlertaAmigos){
				 marcadorAlertaAmigos.setPosition(results[0].geometry.location);
			 }	 
		} else {
			alert(prefs.getMsg("erro.pesquisa"));
		}
	}); 
}


function submitEnter(e){

	var unicode=e.keyCode? e.keyCode : e.charCode
	if (unicode == 13) {
			showLocal();					
			return false;
	}
	else return true;	
}

function convideSeusAmigos() {

	var params = {};
	params[opensocial.Message.Field.TITLE] = "Alerte seus amigos sobre regiões perigosas com WikiCrimes Social.";
	var body = usuarioRedeSocial.nome+" lhe indicou para conhecer o aplicativo do Wikicrimes no orkut. Acesse http://www.orkut.com.br/Main#AppInfo.aspx?appUrl=http%3A%2F%2Fwww.wikicrimes.org%2Fgadget%2Fwikicrimes.xml&objs=&sn=&ref=SR e adicione o aplicativo"; 
	var message = opensocial.newMessage(body, params);
	opensocial.requestSendMessage(opensocial.DataRequest.Group.VIEWER_FRIENDS, message); 
}

var vCrimeSelecionado = "1";

function selecionouSim(){
	vCrimeSelecionado = "1";
}

function selecionouNao(){
	vCrimeSelecionado = "0";
}			

function showEstado(value, reset) {
	if (value == 'BR') {
		if (reset) document.getElementById('comboEstado').value = "0";
		document.getElementById('inputEstado').value = "";

		document.getElementById('comboEstado').style.display = 'inline';
		document.getElementById('inputEstado').style.display = 'none';
	}
	else {
		document.getElementById('comboEstado').value = "0";
		if (reset) document.getElementById('inputEstado').value = "";					

		document.getElementById('comboEstado').style.display = 'none';
		document.getElementById('inputEstado').style.display = 'inline';					
	}
}				


function mostrarMensagem(tipo, descricao, mostrarOcultar){
	var ocultarTexto = "";
	if(mostrarOcultar)
		ocultarTexto = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a style='cursor: pointer' onclick='limparMensagem()'> (ocultar) </a>";
	if(tipo == "info"){
		document.getElementById("mens_info").innerHTML = "<div class='info'>"+descricao+" "+ocultarTexto+"</div>";	
	}
	if(tipo == "success"){
		document.getElementById("mens_info").innerHTML = "<div class='success' >"+ descricao+" "+ocultarTexto+"</div>";	
	}
	if(tipo == "warning"){
		document.getElementById("mens_info").innerHTML = "<div class='warning' >"+ descricao+" "+ocultarTexto+"</div>";	
	}
	if(tipo == "error"){
		document.getElementById("mens_info").innerHTML = "<div class='error' >"+ descricao+" "+ocultarTexto+" </div>";	
	}
	
}

function limparMensagem(){
	document.getElementById("mens_info").innerHTML = "";
}	

function selecionarNotificacao(id, nomeUsuario, acao, tipoNotficacao){
	infowindow.close();
	estaRegistrando = false;
	podeCarregarCrimes = true;
	desabilitaTutor(5000);
	mostrarMensagem('info', prefs.getMsg("carregando"),false);
	acaoAposSelecaoNotificacao = acao;
	if(usuarioSelecionadoAnterior!=null && usuarioSelecionadoAnterior!=undefined && usuarioSelecionadoAnterior!=''){
		document.getElementById(usuarioSelecionadoAnterior.id).innerHTML = usuarioSelecionadoAnterior.nome;
	}
	var arrayDados = id.split("|");
	idNotificacaoSel = arrayDados[0];
	usuarioSelecionadoAnterior = new Amigo();
	usuarioSelecionadoAnterior.nome = nomeUsuario;
	usuarioSelecionadoAnterior.id = id;								
	document.getElementById(id).innerHTML ="<b>"+ nomeUsuario +"</b>";
	map.setCenter(new google.maps.LatLng(arrayDados[1],arrayDados[2]));
	map.setZoom(16);
	crimeInfoWindow = arrayDados[0];
	
	if(tipoNotficacao==1)
		executaRequisicaoRecuperaComentarios(arrayDados[0]);
	else
		executaRequisicaoRecuperaComentariosCrime(arrayDados[0]);
}

function listarComentarios(texto, tipoNotificacao){
	var arrayComentarios = texto.split("#");
	var htmlComentarios = '<br>';
	var atributosComentarios;
	var achouAmigo = false;					
	for( i in arrayComentarios){
		atributosComentario = arrayComentarios[i].split("|");
		for(j in amigos){
			if(atributosComentario[3]==amigos[j].id){
				htmlComentarios += '<div><img width=22px height=20px src='+ linkAplication+'images/widget/comentar.PNG' +' /><b>'+amigos[j].nome+' '+prefs.getMsg("comenta")+':</b></div> <br/>'+atributosComentario[1]+'<br> <hr/>';
				achouAmigo=true;
				break;
			}
			if(atributosComentario[3]==usuarioRedeSocial.idUsuario){
				htmlComentarios += '<div><img width=22px height=20px src='+ linkAplication+'images/widget/comentar.PNG' +' /><b>'+usuarioRedeSocial.nome+' '+prefs.getMsg("comenta")+':</b></div> <br/>'+atributosComentario[1]+'<br> <hr/>';
				achouAmigo=true;
				break;
			}
		}
		
		if(!achouAmigo)
			if(atributosComentario[1]!=undefined)
				htmlComentarios += '<div><img width=22px height=20px src='+ linkAplication+'images/widget/comentar.PNG' +' /> <b>'+prefs.getMsg("usuario.anonimo")+' '+prefs.getMsg("comenta")+':</b></div> <br/>'+atributosComentario[1]+'<br> <hr/>';
		achouAmigo=false;			
	}
	if(htmlComentarios == '<br>')
		htmlComentarios = '<br>'+prefs.getMsg("nenhum.comentario");
	document.getElementById('comentarios').innerHTML = htmlComentarios;
	if (acaoAposSelecaoNotificacao=='comentar'){		
		prepararComentarNotificacao(idNotificacaoSel,tipoNotificacao);
	}	
		
}

function listarRelatosMaisRecentes(texto){
	
	var arrayRelatos = texto.split("#");
	if(texto==""){
		situacao['tem_notificacoes'] = false;
		alterarEstadoSituacao(situacao);
	}
	else{
		situacao['tem_notificacoes'] = true;
		alterarEstadoSituacao(situacao);
	}
	var htmlNotificacoes = '<br/>';
	var atributosRelato;
	var idRelatoInicial = null;
	var htmlNotSel = null;
	var htmlNotificacao = "";
	var cont = 0 ;
	var tipoNotificacao = null;
	for( i in arrayRelatos){
		atributosRelato = arrayRelatos[i].split("|");
		if(atributosRelato[6]=='1'){		
			if(cont==0){
				idRelatoInicial = arrayRelatos[i];
				tipoNotificacao = 1;
			}	
		
			for(j in amigos){
				if(atributosRelato[3]==usuarioRedeSocial.idUsuario){
					htmlNotificacao = ' '+ mostrarImagemTipoAlerta(atributosRelato[5])+' '+ usuarioRedeSocial.nome +' <br/> '+prefs.getMsg("alertou.em")+' ('+atributosRelato[4]+')';
					if(cont == 0){
						htmlNotSel = htmlNotificacao;
					}
					htmlNotificacoes += '<img align="right" title="'+prefs.getMsg("hint.confirmar.positivamente")+'" style="cursor: pointer" onclick="executaRequisicaoConfirmarNotificacao(\''+atributosRelato[0]+'\', \'1\');" src="'+linkAplication+'images/widget/confirmarPositivamente.PNG" /> <img align="right" style="cursor: pointer" onclick="executaRequisicaoConfirmarNotificacao(\''+atributosRelato[0]+'\', \'0\');" src="'+linkAplication+'images/widget/confirmarNegativamente.PNG" title="'+prefs.getMsg("hint.confirmar.negativamente")+'" /><img  style="cursor: pointer" onclick="selecionarNotificacao(\''+arrayRelatos[i]+'\', \''+htmlNotificacao+'\',\'comentar\',1);" align="right" src="'+linkAplication+'images/widget/comentar.PNG" title="'+prefs.getMsg("hint.comentar.alerta")+'" /><img title="'+prefs.getMsg("hint.repasse.alerta")+'" style="cursor: pointer" onclick = "mostrarPerguntaConfRepasse(\''+atributosRelato[0]+'\',1);" align="right" src="'+linkAplication+'images/widget/setaPlotagem.png" />  <a align="left" id="'+arrayRelatos[i]+'" style="cursor: pointer" onclick="selecionarNotificacao(this.id, \''+htmlNotificacao+'\',null,1);"> '+htmlNotificacao+' </a><hr>';
					break;
				}
				if(atributosRelato[3]==amigos[j].id){
					htmlNotificacao = ' '+ mostrarImagemTipoAlerta(atributosRelato[5])+' '+ amigos[j].nome +' <br/> '+prefs.getMsg("alertou.em")+' ('+atributosRelato[4]+')';
					if(cont == 0)
						htmlNotSel = htmlNotificacao;
					htmlNotificacoes += '<img align="right" title="'+prefs.getMsg("hint.confirmar.positivamente")+'" style="cursor: pointer" onclick="executaRequisicaoConfirmarNotificacao(\''+atributosRelato[0]+'\', \'1\');" src="'+linkAplication+'images/widget/confirmarPositivamente.PNG" /> <img align="right" style="cursor: pointer" onclick="executaRequisicaoConfirmarNotificacao(\''+atributosRelato[0]+'\', \'0\');" src="'+linkAplication+'images/widget/confirmarNegativamente.PNG" title="'+prefs.getMsg("hint.confirmar.negativamente")+'" /><img  style="cursor: pointer" onclick="selecionarNotificacao(\''+arrayRelatos[i]+'\', \''+htmlNotificacao+')'+'\',\'comentar\',1);" align="right" src="'+linkAplication+'images/widget/comentar.PNG" title="'+prefs.getMsg("hint.comentar.alerta")+'" /><img title="'+prefs.getMsg("hint.repasse.alerta")+'" style="cursor: pointer" onclick = "mostrarPerguntaConfRepasse(\''+atributosRelato[0]+'\',1);" align="right" src="'+linkAplication+'images/widget/setaPlotagem.png" />  <a align="left" id="'+arrayRelatos[i]+'" style="cursor: pointer" onclick="selecionarNotificacao(this.id, \''+htmlNotificacao+'\',null,1);"> '+htmlNotificacao+' </a><hr>';
					break;
				}
				
			}
			cont++;
		}else{
			if(cont==0){
				idRelatoInicial = arrayRelatos[i];
				tipoNotificacao = 0;
			}			
		
			for(j in amigos){
				if(atributosRelato[3]==usuarioRedeSocial.idUsuario){
					htmlNotificacao = ' '+ mostrarImagemTipoAlerta(atributosRelato[5])+' '+ usuarioRedeSocial.nome +' <br/> '+prefs.getMsg("alertou.em")+' ('+atributosRelato[4]+')';
					if(cont == 0)
						htmlNotSel = htmlNotificacao; 	
					htmlNotificacoes += '<img align="right" title="'+prefs.getMsg("hint.confirmar.positivamente")+'" style="cursor: pointer" onclick="executaRequisicaoConfirmarNotificacaoCrime(\''+atributosRelato[0]+'\', \'1\');" src="'+linkAplication+'images/widget/confirmarPositivamente.PNG" /> <img align="right" style="cursor: pointer" onclick="executaRequisicaoConfirmarNotificacaoCrime(\''+atributosRelato[0]+'\', \'0\');" src="'+linkAplication+'images/widget/confirmarNegativamente.PNG" title="'+prefs.getMsg("hint.confirmar.negativamente")+'" /><img  style="cursor: pointer" onclick="selecionarNotificacao(\''+arrayRelatos[i]+'\', \''+htmlNotificacao+'\',\'comentar\',0);" align="right" src="'+linkAplication+'images/widget/comentar.PNG" title="'+prefs.getMsg("hint.comentar.alerta")+'" /><img title="'+prefs.getMsg("hint.repasse.alerta")+'" style="cursor: pointer" onclick = "mostrarPerguntaConfRepasse(\''+atributosRelato[0]+'\',0);" align="right" src="'+linkAplication+'images/widget/setaPlotagem.png" />  <a align="left" id="'+arrayRelatos[i]+'" style="cursor: pointer" onclick="selecionarNotificacao(this.id, \''+htmlNotificacao+'\',null,0);"> '+htmlNotificacao+' </a><hr>';
					break;
				}
				if(atributosRelato[3]==amigos[j].id){
					htmlNotificacao = ' '+ mostrarImagemTipoAlerta(atributosRelato[5])+' '+ amigos[j].nome +' <br/> '+prefs.getMsg("alertou.em")+' ('+atributosRelato[4]+')';
					if(cont == 0)
						htmlNotSel = htmlNotificacao;	
					htmlNotificacoes += '<img align="right" title="'+prefs.getMsg("hint.confirmar.positivamente")+'" style="cursor: pointer" onclick="executaRequisicaoConfirmarNotificacaoCrime(\''+atributosRelato[0]+'\', \'1\');" src="'+linkAplication+'images/widget/confirmarPositivamente.PNG" /> <img align="right" style="cursor: pointer" onclick="executaRequisicaoConfirmarNotificacaoCrime(\''+atributosRelato[0]+'\', \'0\');" src="'+linkAplication+'images/widget/confirmarNegativamente.PNG" title="'+prefs.getMsg("hint.confirmar.negativamente")+'" /><img  style="cursor: pointer" onclick="selecionarNotificacao(\''+arrayRelatos[i]+'\', \''+htmlNotificacao+'\',\'comentar\',0);" align="right" src="'+linkAplication+'images/widget/comentar.PNG" title="'+prefs.getMsg("hint.comentar.alerta")+'" /><img title="'+prefs.getMsg("hint.repasse.alerta")+'" style="cursor: pointer" onclick = "mostrarPerguntaConfRepasse(\''+atributosRelato[0]+'\',0);" align="right" src="'+linkAplication+'images/widget/setaPlotagem.png" />  <a align="left" id="'+arrayRelatos[i]+'" style="cursor: pointer" onclick="selecionarNotificacao(this.id, \''+htmlNotificacao+'\',null,0);"> '+htmlNotificacao+' </a><hr>';
					break;
				}
				
			}
			cont++;
		}	
								
	}
	if(htmlNotificacoes == '<br/>')
		htmlNotificacoes = '<br>'+prefs.getMsg("nenhum.amigo.registrou.alerta");
	
	document.getElementById('notificacoes').innerHTML = htmlNotificacoes;
	//alert(idRelatoInicial);
	//alert(htmlNotSel);
	if(htmlNotSel!=null)
		selecionarNotificacao(idRelatoInicial,htmlNotSel,null,tipoNotificacao);
	
}

function mostrarPerguntaConfRepasse(idNotificacao,tipoRepasse){
	constroiModoal(conteudoConfirmacaoIndicacao(idNotificacao,tipoRepasse), 252, 100,prefs.getMsg('mensagem.sistema'),35,35);
}

function mostrarModalFaleConosco(){
	constroiModoal(conteudoFaleConosco(),372,240,prefs.getMsg('fale.conosco'),25,25);	
}

function mostrarModalConfiguracoes(){
	constroiModoal(conteudoConfiguracoes(),320,120,prefs.getMsg("canvas.configuracoes"),35,35);	
}

function mostrarModalAvisoDesativarTutor(){
	constroiModoal(conteudoAvisoDesativarTutor(),320,120,prefs.getMsg("mensagem.sistema"),35,35);	
}

function atualizaMapa() {
	if(map){
		if (limpaTela) {
			map.clearOverlays();
			crimesAtuais = {};			
		}
		executaRequisicaoListagem();
	}
}
function getCityLatLng2(){
	if (!geocoder) {						
		geocoder = new google.maps.Geocoder();
	}
	geocoder.geocode( { 'address': document.getElementById('cidade').value + " , " +  document.getElementById('pais').value}, function(results, status) {
		 if (status == google.maps.GeocoderStatus.OK) {
			 usuarioRedeSocial.latitude=results[0].geometry.location.lat();
			 usuarioRedeSocial.longitude=results[0].geometry.location.lat().lng();
			 executaRequisicaoRegistraUsuarioOrkut();
			
		} else {
			alert(" usuario.cidade.nao.encontrada(" + document.getElementById('cidade').value + " , " +  document.getElementById('pais').value + ")");
			 //alert(prefs.getMsg('usuario.cidade.nao.encontrada') +" (" + document.getElementById('cidade').value + " , " +  document.getElementById('pais').value + ")");
			document.getElementById('cidade').focus();
		}
	}); 
	
}

function mostraDivSelecaoAlerta(left,top,html){
	jaMostrouAlerta = true;
	document.getElementById("divSelectTipoAlerta").style.top = top;
	document.getElementById("divSelectTipoAlerta").style.left= left;
	document.getElementById("divSelectTipoAlerta").style.visibility = "visible";
	document.getElementById("divSelectTipoAlerta").innerHTML = html;
}

function mostraDivLegendaMapa(left,top,html){	
	document.getElementById("divLegendaMapa").style.top = top;
	document.getElementById("divLegendaMapa").style.left= left;
	document.getElementById("divLegendaMapa").style.visibility = "visible";
	document.getElementById("divLegendaMapa").innerHTML = html;
}

function escondeDivSelecaoAlerta(){
	document.getElementById("divSelectTipoAlerta").style.visibility = "hidden";
}

function escondeDivLegendaMapa(){
	document.getElementById("divLegendaMapa").style.visibility = "hidden";
}

function getCityLatLng(){
	if (!geocoder) {						
		geocoder = new google.maps.Geocoder();
	}
	geocoder.geocode( { 'address': document.getElementById('cidade').value + " , " +  document.getElementById('pais').value}, function(results, status) {
		 if (status == google.maps.GeocoderStatus.OK) {
			 usuarioRedeSocial.latitude=results[0].geometry.location.lat();
			 usuarioRedeSocial.longitude=results[0].geometry.location.lng();
			 executaRequisicaoRegistraUsuarioOrkut();
			
		} else {
			 alert(" usuario.cidade.nao.encontrada(" + document.getElementById('cidade').value + " , " +  document.getElementById('pais').value + ")");
			 //alert(prefs.getMsg('usuario.cidade.nao.encontrada') +" (" + document.getElementById('cidade').value + " , " +  document.getElementById('pais').value + ")");
			 document.getElementById('cidade').focus();
			 place=null;
			 return false;
		}
	});
 }

   
function prepararComentarNotificacao(idNotificacao, tipoNotificacao){
   	if(gadgets.util.getUrlParameters()["gadgetOwner"]!=gadgets.util.getUrlParameters()["gadgetViewer"]){
		mostrarMensagem('warning', prefs.getMsg("sem.permissao"),true);
	}else{			    
    	htmlComentariosDescartar = document.getElementById('comentarios').innerHTML;
    	var htmlComentarios = "<br><div><img width=22px height=20px src="+ linkAplication+"images/widget/comentar.PNG" +" /> <b>"+usuarioRedeSocial.nome+" comenta:</b> </div><br/>";		
   		htmlComentarios +="<TEXTAREA NAME='desc_comentario' id='desc_comentario' style='font-size: 12px; border: 1px solid #2763a5;' COLS='36' ROWS='4'></TEXTAREA> ";
   		if(tipoNotificacao==1)
   			htmlComentarios += "<br> <input style='font-family: Arial, Helvetica, sans-serif; font-size: 10px;	font-weight: bold; margin: 1; padding: 1; background-color: #6da6e2; color: white;	border: 1px solid #2763a5;' type='button' value ='"+prefs.getMsg("salvar")+"' onclick='executaRequisicaoComentarNotificacao(\""+idNotificacao+"\");' /> <input style='font-family: Arial, Helvetica, sans-serif; font-size: 10px;	font-weight: bold; margin: 1; padding: 1; background-color: #6da6e2; color: white;	border: 1px solid #2763a5;' type='button' value ='"+prefs.getMsg("cancelar")+"' onclick='descartarComentario();' /> <hr/>";
   		else
   			htmlComentarios += "<br> <input style='font-family: Arial, Helvetica, sans-serif; font-size: 10px;	font-weight: bold; margin: 1; padding: 1; background-color: #6da6e2; color: white;	border: 1px solid #2763a5;' type='button' value ='"+prefs.getMsg("salvar")+"' onclick='executaRequisicaoComentarNotificacaoCrime(\""+idNotificacao+"\");' /> <input style='font-family: Arial, Helvetica, sans-serif; font-size: 10px;	font-weight: bold; margin: 1; padding: 1; background-color: #6da6e2; color: white;	border: 1px solid #2763a5;' type='button' value ='"+prefs.getMsg("cancelar")+"' onclick='descartarComentario();' /> <hr/>";
   		if(document.getElementById('comentarios').innerHTML == '<br>Nenhum comentário sobre esse alerta')
    		document.getElementById('comentarios').innerHTML = '';	
    	document.getElementById('comentarios').innerHTML = htmlComentarios + document.getElementById('comentarios').innerHTML;
    	document.getElementById('desc_comentario').focus();
    }	
}
   
function descartarComentario(){
	document.getElementById('comentarios').innerHTML=htmlComentariosDescartar;
}
var polInfo="1";
function setRadioPolInfo(valor){
	polInfo = valor;
}

var rcrime="1";
function setRadioRCrime(valor){
	rcrime = valor;
}
				
        