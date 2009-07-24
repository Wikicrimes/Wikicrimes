/* <![CDATA[ */

//Legenda
var statusLegendaWikiCrimes='visivel';

function mostraEscondeLegandaWikiCrimes(){
	if(statusLegendaWikiCrimes=='visivel'){
		statusLegendaWikiCrimes = 'invisivel';
		wikicrimesDiv.innerHTML = htmlLegendaEscondidaWikiCrimes();
	}
	else{
		statusLegendaWikiCrimes='visivel';
		wikicrimesDiv.innerHTML = htmlLegendaWikiCrimes();
	}							
}

function htmlLegendaEscondidaWikiCrimes(){
	var html = "<table cellpading='0' cellspacing='0' style='width:100px; border:1px solid #333333; opacity: .84; -moz-opacity:0.84; filter: alpha(opacity=84); background-color:#F0F8FF;font-family:Arial, sans-serif;  font-size: 11px;'>";
	html+="     	<tr>";
	html+="     		<td> <div style='width:4px;'></div> </td><td align='left' colspan='1'> <b >"+ mensagens['legenda'] +"</b> </td> <td align='right' colspan='1'> <img width='12px' height='12px' src='"+urlWikiCrimes+"images/maximizar.png' onclick='mostraEscondeLegandaWikiCrimes();' style='font-size: 12px;cursor: pointer;'/> </td> <td> <div style='width:4px;'></div> </td>";
	html+="         </tr>"
	html += "	</table>";
	return html;
}

function htmlLegendaWikiCrimes(){
	var html = "<table cellpading='0' cellspacing='0' style='width:100px; border:1px solid #333333; opacity: .84; -moz-opacity:0.84; filter: alpha(opacity=84); background-color:#F0F8FF;font-family:Arial, sans-serif;  font-size: 11px;'>";
	html+="     	<tr>";
	html+="     		<td> <div style='width:4px;'></div> </td><td align='left' colspan='2'> <b >"+ mensagens['legenda'] +"</b> </td> <td align='right' colspan='1'> <img width='12px' height='12px' src='"+urlWikiCrimes+"images/maximizar.png' onclick='mostraEscondeLegandaWikiCrimes();' style='font-size: 12px;cursor: pointer;'/> </td> <td> <div style='width:4px;'></div> </td>";
	html+="         </tr>"
	html+="     	<tr>";
	html+="     		<td colspan='4'> <div style='width:100%; height: 4px; border-top:1px solid #333333;'></div> </td>";
	html+="         </tr>"
	html+="     	<tr>";
	html+="     		<td align='right'> <div style='width:4px;'></div> <img width='19px' height='33px' src='"+urlWikiCrimes+"images/baloes/clusterIcon_Legend.png' />  </td> <td align='left' colspan='2'> "+ mensagens['agrupador'] +"  </td><td> <div style='width:4px;'></div> </td>";
	html+="         </tr>";
	html+="     	<tr>";
	html+="     		<td align='right'> <div style='width:4px;'></div> <img width='14px' height='24px' src='"+urlWikiCrimes+"images/baloes/novoMarcadorVermelha.png' /> </td> <td align='left' colspan='2'> "+ mensagens['roubo'] +"  </td><td> <div style='width:4px;'></div> </td>";
	html+="         </tr>";
	html+="     	<tr>";
	html+="     		<td align='right'> <div style='width:4px;'></div> <img width='14px' height='24px' src='"+urlWikiCrimes+"images/baloes/novoMarcadorAzul.png' />  </td> <td align='left' colspan='2'> "+ mensagens['furto'] +"  </td><td> <div style='width:4px;'></div> </td>";
	html+="         </tr>";
	html+="     	<tr>";
	html+="     		<td align='right'> <div style='width:4px;'></div> <img width='14px' height='24px' src='"+urlWikiCrimes+"images/baloes/novoMarcadorLaranja.png' />  </td> <td align='left' colspan='2'> "+ mensagens['outros'] +"  </td><td> <div style='width:4px;'></div> </td>";
	html+="         </tr>";
	html+="     	<tr>";
	html+="     		<td colspan='4'> <div style='width:100%; height: 4px'></div> </td>";
	html+="         </tr>"
	html += "	</table>";
	return html;
}

var wikicrimesDiv;

function LegendaWikicrimes() {
}
LegendaWikicrimes.prototype = new GControl();

LegendaWikicrimes.prototype.initialize = function(mapWikiCrimes) {
  var container = document.createElement("div");

  wikicrimesDiv = document.createElement("div");
  this.setButtonStyle_(wikicrimesDiv);
  container.appendChild(wikicrimesDiv);
  wikicrimesDiv.innerHTML = htmlLegendaWikiCrimes();
 
  mapWikiCrimes.getContainer().appendChild(container);
  return container;
}


LegendaWikicrimes.prototype.getDefaultPosition = function() {
  return new GControlPosition(G_ANCHOR_BOTTOM_RIGHT, new GSize(10,16));
}


LegendaWikicrimes.prototype.setButtonStyle_ = function(button) {				
  //button.style.height = "3.8em";
  //button.style.cursor = "pointer";
  //button.style.backgroundImage = "url('./images/logoWikicrimesEmbedded.PNG')";
}


//Comandos Mapa
var statusComandosWikiCrimes='visivel';

function mostraEscondeComandosWikiCrimes(){
	if(statusComandosWikiCrimes=='visivel'){
		statusComandosWikiCrimes = 'invisivel';
		comandosWikicrimesDiv.innerHTML = htmlComandosEscondidosWikiCrimes();
	}
	else{
		statusComandosWikiCrimes='visivel';
		comandosWikicrimesDiv.innerHTML = htmlComandosWikiCrimes();
	}							
}


var habilitaListagemCrimesWikiCrimes = true;
function mostraLimpaCrimesWikiCrimes(){
	var div = document.getElementById('botaoLimparCrimesWikiCrimes');
	if(habilitaListagemCrimesWikiCrimes){		
		habilitaListagemCrimesWikiCrimes = false;
		GVetorMarcadores = [];
		GClusterer.clearMarkers();
		podeCarregarCrimes = false;
		div.innerHTML = "<div onclick='mostraLimpaCrimesWikiCrimes();' class='botaoAtivado'> "+mensagens['limpar.crimes']+" </div>";
	}else{
		div.innerHTML = "<div onclick='mostraLimpaCrimesWikiCrimes();' class='botao'> "+mensagens['limpar.crimes']+" </div>";
		podeCarregarCrimes = true;
		habilitaListagemCrimesWikiCrimes = true;
		GEvent.trigger(mapWikiCrimes,'moveend');
	}
}

function htmlComandosEscondidosWikiCrimes(){
	var html = "<table cellpading='0' cellspacing='0' style='width:218px; padding:0px ;border:1px solid #333333; opacity: .84; -moz-opacity:0.84; filter: alpha(opacity=84); background-color:#F0F8FF;font-family:Arial, sans-serif;  font-size: 11px;'>";
	html+="     	<tr>";
	html+="     		<td> <div style='width:0.5px;'></div> </td> <td align='left' colspan='1'> <b >"+ "WikiCrimes" +"</b> </td> <td align='right' colspan='1'><img  width='12px' height='12px' src='"+urlWikiCrimes+"images/maximizar.png' onclick='mostraEscondeComandosWikiCrimes();' style='font-size: 12px;cursor: pointer;'/></td><td> <div style='width:0.5px;'></div> </td>";
	html+="         </tr>"				
	html += "	</table>";
	return html;
}

function htmlComandosWikiCrimes(){
	var html = "<table cellpading='0' cellspacing='0' style='width:218px; padding:0px ;border:1px solid #333333; opacity: .84; -moz-opacity:0.84; filter: alpha(opacity=84); background-color:#F0F8FF;font-family:Arial, sans-serif;  font-size: 11px;'>";
	html+="     	<tr>";
	html+="     		<td> <div style='width:4px;'></div> </td><td align='left' colspan='1'> <b >"+ "WikiCrimes" +"</b> </td> <td align='right' colspan='1'> <img width='12px' height='12px' src='"+urlWikiCrimes+"images/maximizar.png' onclick='mostraEscondeComandosWikiCrimes();' style='font-size: 12px;cursor: pointer;'/> </td> <td> <div style='width:4px;'></div> </td>";
	html+="         </tr>"
	html+="     	<tr>";
	html+="     		<td align='center' colspan='4'> <div style='width:100%; height: 4px; border-top:1px solid #333333;'></div> </td>";
	html+="         </tr>"	
	html+="     	<tr>";
	html+="     		<td> <div style='width:4px;'></div> </td><td align='center'><div id='botaoFiltroWikiCrimes'><div onclick='mostraEscondeFiltroWikiCrimes();' class='botao'> "+mensagens['filtrarCrimes']+" </div></div></td> <td> <div id='botaoRegistroCrimesWikiCrimes'><div onclick='mostraEscondeRegistroCrimeWikiCrimes();' class='botao'> "+mensagens['registrarCrime']+" </div></div></td><td> <div style='width:4px;'></div> </td>";
	html+="         </tr>"
	html+="     	<tr>";
	html+="     		<td> <div style='width:4px;'></div> </td><td align='center'><div id='botaoMapaKernel'><div onclick='habilitaDesabilitaMapaDeKernelWikiCrimes();' class='botao'> "+mensagens['titulo.kernel.map']+" </div></div></td> <td> <div id='botaoLimparCrimesWikiCrimes'><div onclick='mostraLimpaCrimesWikiCrimes();' class='botao'> "+mensagens['limpar.crimes']+" </div></div> </td><td> <div style='width:4px;'></div> </td>";
	html+="         </tr>"	
	html+="     	<tr>";
	html+="     		<td align='center' colspan='4'> <div style='width:100%; height: 4px;'></div> </td>";
	html+="         </tr>"
	html += "	</table>";
	return html;
}

var comandosWikicrimesDiv;

function ComandosWikicrimes() {
}
ComandosWikicrimes.prototype = new GControl();

ComandosWikicrimes.prototype.initialize = function(mapWikiCrimes) {
  var container = document.createElement("div");

  comandosWikicrimesDiv = document.createElement("div");
  this.setButtonStyle_(comandosWikicrimesDiv);
  container.appendChild(comandosWikicrimesDiv);
  comandosWikicrimesDiv.innerHTML = htmlComandosWikiCrimes();
 
  mapWikiCrimes.getContainer().appendChild(container);
  return container;
}


ComandosWikicrimes.prototype.getDefaultPosition = function() {
  return new GControlPosition(G_ANCHOR_TOP_RIGHT, new GSize(10,36));
}


ComandosWikicrimes.prototype.setButtonStyle_ = function(button) {				
  //button.style.height = "3.8em";
  //button.style.cursor = "pointer";
  //button.style.backgroundImage = "url('./images/logoWikicrimesEmbedded.PNG')";
}


//Filtros Mapa
var statusFiltroWikiCrimes='invisivel';

function mostraEscondeFiltroWikiCrimes(){
	var div = document.getElementById('botaoFiltroWikiCrimes');	
	if(statusFiltroWikiCrimes=='visivel'){
		statusFiltroWikiCrimes = 'invisivel';
		mapWikiCrimes.removeControl(comandoFiltroWikiCrimes);

		div.innerHTML = "<div id='botaoFiltroWikiCrimes'><div onclick='mostraEscondeFiltroWikiCrimes();' class='botao'> "+mensagens['filtrarCrimes']+" </div></div>";
	}
	else{
		document.getElementById('botaoRegistroCrimesWikiCrimes').innerHTML = "<div id='botaoRegistroCrimesWikiCrimes'><div onclick='mostraEscondeRegistroCrimeWikiCrimes();' class='botao'> "+mensagens['registrarCrime']+" </div></div>";
		statusFiltroWikiCrimes='visivel';
		mapWikiCrimes.removeControl(comandoRegistroCrimeWikiCrimes);
		statusRegistroCrimesWikiCrimes='invisivel';
		mapWikiCrimes.addControl(comandoFiltroWikiCrimes);
		document.getElementById('dataInicialWikiCrimesApi').value = dataInicialWikicrimes;
		document.getElementById('dataFinalWikiCrimesApi').value = dataFinalWikicrimes;
		document.getElementById('tipo_crime').value = tipoCrimeWikiCrimes ;
		alteraTipoVitima(tipoCrimeWikiCrimes);
		document.getElementById('tipo_vitima').value = tipoVitimaWikiCrimes;
		div.innerHTML = "<div id='botaoFiltroWikiCrimes'><div onclick='mostraEscondeFiltroWikiCrimes();' class='botaoAtivado'> "+mensagens['filtrarCrimes']+" </div></div>";
	}							
}

function alteraTipoVitima(valor){
	if( valor == '1' || valor == '2' || valor == '3' || valor == '4'){
		document.getElementById("escolha_tipo_vitima").innerHTML = "<select style='font-size:10px;' name='tipo_vitima' id='tipo_vitima'> <option value='0'> "+mensagens['todos.tipos']+" </option> <option value='1'> "+mensagens['tipo.vitima.pessoa']+" </option> <option value='2'> "+mensagens['tipo.vitima.propriedade']+" </option> </select>";
	}else if(valor == '5'){
		document.getElementById("escolha_tipo_vitima").innerHTML = "<select style='font-size:10px;' name='tipo_vitima' id='tipo_vitima'> <option value='0'> "+mensagens['todos.tipos']+" </option> <option value='3'> "+mensagens['tipo.vitima.rixas.ou.brigas']+" </option> <option value='4'> "+mensagens['tipo.vitima.violencia.domestica']+" </option> <option value='5'> "+mensagens['tipo.vitima.abuso.de.autoridade']+" </option> <option value='6'> "+mensagens['tipo.vitima.homicidio']+" </option> <option value='7'> "+mensagens['tipo.vitima.tentativa.homicidio']+" </option> <option value='8'> "+mensagens['tipo.vitima.latrocinio']+" </option> </select>";
	}else if(valor == '0'){
		document.getElementById("escolha_tipo_vitima").innerHTML = "<select style='font-size:10px;' name='tipo_vitima' id='tipo_vitima'> <option value='0'> "+mensagens['todos.tipos']+" </option> </select>";
	}
}

function htmlFiltroWikiCrimes(){
	var html = "<table cellpading='0' cellspacing='0' style='width:226px; padding:0px ;border:1px solid #333333; opacity: .84; -moz-opacity:0.84; filter: alpha(opacity=84); background-color:#F0F8FF;font-family:Arial, sans-serif;  font-size: 11px;'>";
	html+="     	<tr>";
	html+="     		<td> <div style='width:4px;'></div> </td><td align='left' colspan='3'> <b >"+ mensagens['filtros'] +"</b> </td> <td align='right' colspan='1'> <b onclick='mostraEscondeFiltroWikiCrimes();' style='font-size: 10px;cursor: pointer;'> X </b> </td> <td> <div style='width:4px;'></div> </td>";
	html+="         </tr>";
	html+="     	<tr>";
	html+="     		<td align='center' colspan='6'> <div style='width:100%; height: 4px; border-top:1px solid #333333;'></div> </td>";
	html+="         </tr>";
	html+="     	<tr>";
	html+="     		<td colspan='1'> <div style='width:4px;'></div> </td> <td align='left' colspan='5'> <select style='font-size:10px;' name='tipo_crime' id='tipo_crime' onchange='alteraTipoVitima(this.value);'> <option value='0'> "+mensagens['todos.crimes']+" </option> <option value='4'> "+mensagens['tipo.crime.roubo'] +" </option> <option value='3'> "+mensagens['tipo.crime.furto'] +" </option> <option value='1'> "+mensagens['tipo.crime.tentativa.roubo'] +" </option> <option value='2'> "+mensagens['tipo.crime.tentativa.furto'] +" </option> <option value='5'> "+mensagens['tipo.crime.outros'] +" </option> </select> </td>";
	html+="         </tr>";
	html+="     	<tr>";
	html+="     		<td colspan='1'> <div style='width:4px;'></div> </td> <td align='left' colspan='5'><div id='escolha_tipo_vitima'> <select style='font-size:10px;' name='tipo_vitima' id='tipo_vitima'> <option value='0'> "+mensagens['todos.tipos']+" </option> </select> </div> </td>";
	html+="         </tr>";
	html+="     	<tr>";
	html+="     		<td colspan='1'> <div style='width:4px;'></div> </td> <td align='left' colspan='5'> "+mensagens['periodo']+": </td>";
	html+="         </tr>";
	html+="     	<tr>";
	html+="     		<td> <div style='width:4px;'></div> </td> <td> "+mensagens['de']+" </td> <td> <input type='text' style='font-size:10px;' size='10' maxlength='10' onkeyup='formataData(this,event)' id='dataInicialWikiCrimesApi'/> </td> <td> "+mensagens['ate']+" </td> <td> <input type='text' size='10' maxlength='10' onkeyup='formataData(this,event)' style='font-size:10px;' id='dataFinalWikiCrimesApi'/> </td><td> <div style='width:4px;'></div> </td>";
	html+="         </tr>";
	html+="     	<tr>";
	html+="     		<td> <div style='width:4px;'></div> </td> <td> <input type='checkbox' name='emailUsuarioWikiCrimes' id='emailUsuarioWikiCrimes'/> </td> <td colspan='3'> "+mensagens['meus.registros']+" </td> <td> <div style='width:4px;'></div> </td>";
	html+="         </tr>";
	html+="     	<tr>";
	html+="     		<td align='center' colspan='6'> <div style='width:100%; height: 4px;'></div> </td>";
	html+="         </tr>";
	html+="     	<tr>";
	html+="     		<td colspan='1'> <div style='width:4px;'></div> </td><td align='left' colspan='4'> <input class='button' type='button' value='"+mensagens['filtrar']+"' onclick='setaParametrosFiltro();'/> <input class='button' type='button' value='"+mensagens['cancelar']+"' onclick='mostraEscondeFiltroWikiCrimes();'/> </td><td> <div style='width:4px;'></div> </td>";
	html+="         </tr>";
	html+="     	<tr>";
	html+="     		<td align='center' colspan='6'> <div style='width:100%; height: 4px;'></div> </td>";
	html+="         </tr>";
	html += "	</table>";
	return html;
}

var filtroWikicrimesDiv;

function ComandoFiltroWikicrimes() {
}

ComandoFiltroWikicrimes.prototype = new GControl();

ComandoFiltroWikicrimes.prototype.initialize = function(mapWikiCrimes) {
  var container = document.createElement("div");

  filtroWikicrimesDiv = document.createElement("div");
  this.setButtonStyle_(filtroWikicrimesDiv);
  container.appendChild(filtroWikicrimesDiv);
  filtroWikicrimesDiv.innerHTML = htmlFiltroWikiCrimes();
 
  mapWikiCrimes.getContainer().appendChild(container);
  return container;
}


ComandoFiltroWikicrimes.prototype.getDefaultPosition = function() {
  return new GControlPosition(G_ANCHOR_TOP_RIGHT, new GSize(10,124));
}


ComandoFiltroWikicrimes.prototype.setButtonStyle_ = function(button) {				
  //button.style.height = "3.8em";
  //button.style.cursor = "pointer";
  //button.style.backgroundImage = "url('./images/logoWikicrimesEmbedded.PNG')";
}

//Filtros Mapa
var statusRegistroCrimesWikiCrimes='invisivel';

function mostraEscondeRegistroCrimeWikiCrimes(){
	tipoCrimeRegistro = "0";
	tipoVitimaRegistro = "0";
	var div = document.getElementById('botaoRegistroCrimesWikiCrimes');	
	if(statusRegistroCrimesWikiCrimes=='visivel'){
		statusRegistroCrimesWikiCrimes = 'invisivel';
		mapWikiCrimes.removeControl(comandoRegistroCrimeWikiCrimes);
		div.innerHTML = "<div id='botaoRegistroCrimesWikiCrimes'><div onclick='mostraEscondeRegistroCrimeWikiCrimes();' class='botao'> "+mensagens['registrarCrime']+" </div></div>";
	}
	else{		
		statusRegistroCrimesWikiCrimes='visivel';
		mapWikiCrimes.removeControl(comandoFiltroWikiCrimes);
		statusFiltroWikiCrimes = 'invisivel';
		mapWikiCrimes.addControl(comandoRegistroCrimeWikiCrimes);
		document.getElementById('botaoFiltroWikiCrimes').innerHTML = "<div id='botaoFiltroWikiCrimes'><div onclick='mostraEscondeFiltroWikiCrimes();' class='botao'> "+mensagens['filtrarCrimes']+" </div></div>"
		div.innerHTML = "<div id='botaoRegistroCrimesWikiCrimes'><div onclick='mostraEscondeRegistroCrimeWikiCrimes();' class='botaoAtivado'> "+mensagens['registrarCrime']+" </div></div>";
	}							
}

var tipoCrimeRegistro = "0";
var tipoVitimaRegistro = "0";

function setaTipoVitimaTipoCrime(tipoCrimeSel, tipoVitimaSel){
	tipoCrimeRegistro = tipoCrimeSel;
	tipoVitimaRegistro = tipoVitimaSel;
}

var crimeWikiCrimes = null;
var eventoMouseMoveWikiCrimes = null;
var eventoClickMapWikiCrimes = null;
var eventoInfoWindowCloseWikiCrimes = null;
var eventoRigthClickMapWikiCrimes = null;
var infoTT = null;
function prepararPlotarEventoWikiCrimes(){	
	if( tipoCrimeRegistro == "0" || tipoVitimaRegistro == "0" ){
		document.getElementById("div_erro_tipo_crime_req").innerHTML = mensagens['campo.requerido'];
		return;
	}
	mapWikiCrimes.removeControl(comandoRegistroCrimeWikiCrimes);
	crimeWikiCrimes = createMarkerWikiCrimesTemp(tipoCrimeRegistro);
	if(infoTT == null)	
		infoTT = document.createElement("div");
	mapWikiCrimes.addOverlay(crimeWikiCrimes);
	eventoMouseMoveWikiCrimes = GEvent.addListener(mapWikiCrimes, "mousemove", function (latLng) {
		crimeWikiCrimes.setLatLng(latLng);
		
		var point = mapWikiCrimes.getCurrentMapType().getProjection().fromLatLngToPixel(mapWikiCrimes.fromDivPixelToLatLng(new GPoint(0,0),true),mapWikiCrimes.getZoom());
		var offset = mapWikiCrimes.getCurrentMapType().getProjection().fromLatLngToPixel(crimeWikiCrimes.getPoint(),mapWikiCrimes.getZoom());
		var anchor = crimeWikiCrimes.getIcon().iconAnchor;
		var width = crimeWikiCrimes.getIcon().iconSize.width;
		var height = infoTT.clientHeight;
		var pos = new GControlPosition(G_ANCHOR_TOP_LEFT, new GSize(offset.x - point.x - anchor.x + width, offset.y - point.y -anchor.y -height));

  	  	infoTT.innerHTML = "<div class='infoMarcadorTemporario'>" + mensagens['move.icone.local.crime'] + "</div>";

		// Plota toolTip no mapa
  	  	pos.apply(infoTT);
  	    mapWikiCrimes.getPane(G_MAP_FLOAT_PANE).appendChild(infoTT);
  	  					      	  	
	  	infoTT.style.visibility="visible";
	  	
	});
	
	eventoClickMapWikiCrimes = GEvent.addListener(mapWikiCrimes, "click", function (overlay, latLng) {
		GEvent.removeListener(eventoMouseMoveWikiCrimes);
		GEvent.removeListener(eventoClickMapWikiCrimes);
		crimeWikiCrimes.openInfoWindowHtml(htmlTelaRegistroCrimesWikiCrimes());
		infoTT.style.visibility="hidden";
	});
	
	eventoRigthClickMapWikiCrimes = GEvent.addListener(mapWikiCrimes,"singlerightclick",function(pixel,tile) {
		document.getElementById('botaoRegistroCrimesWikiCrimes').innerHTML = "<div id='botaoRegistroCrimesWikiCrimes'><div onclick='mostraEscondeRegistroCrimeWikiCrimes();' class='botao'> "+mensagens['registrarCrime']+" </div></div>";
		statusRegistroCrimesWikiCrimes='invisivel';
		GEvent.removeListener(eventoMouseMoveWikiCrimes);
		GEvent.removeListener(eventoClickMapWikiCrimes);
		GEvent.removeListener(eventoRigthClickMapWikiCrimes);
		if(crimeWikiCrimes != null){
			mapWikiCrimes.removeOverlay(crimeWikiCrimes);
		}
		infoTT.style.visibility="hidden";
	});
}

function htmlTelaRegistroCrimesWikiCrimes(){
	var html="<div style='width:440px; height:160px ;overflow: auto;'><div id='topoAlerta'> <b> "+mensagens['titulo.registrar.crime']+" </b> </div> <table style='font-family:Arial, sans-serif;  font-size: 11px;'>";
	html+="<tr> <td colspan = '4' > <b>"+ mensagens['tipo.local']+":(*) </b> </td>  </tr>";
	html+="<tr> <td colspan = '4' > <div style='color:red;' id = 'erroTipoLocal'> </div> </td>  </tr>";
	html+="<tr> <td colspan = '4' > <select style='font-size: 12px; border: 1px solid #2763a5;' name='tipoLocal' id='tipoLocal'> <option value=''>"+mensagens['selecione']+"</option> <option value='1'> "+mensagens['via.publica']+" </option> <option value='2'> "+mensagens['transporte.coletivo']+" </option> <option value='3'> "+mensagens['estabelecimento.comercial']+" </option> <option value='4'> "+mensagens['residencia']+" </option> <option value='5'> "+mensagens['escolas']+" </option> <option value='8'> "+mensagens['banco']+" </option> <option value='9'> "+mensagens['farmacia']+" </option> <option value='10'> "+mensagens['posto.gasolina']+" </option> <option value='11'> " + mensagens['loterica'] +" </option> <option value='12'> " + mensagens['veiculo'] + " </option> <option value='13'> " + mensagens['shopping'] + " </option> <option value='14'> " + mensagens['praca.publica'] + " </option> <option value='6'> "+mensagens['outros']+" </option>  </select> </td>  </tr>";
	html+="<tr> <td colspan = '2' > <b> "+ mensagens['data']+":(*) </b> </td> <td colspan = '2' > <b> "+ mensagens['horario']+":(*) </b> </td>  </tr>";
	html+="<tr> <td colspan = '2' > <div style='color:red;' id = 'erroData'> </div> </td>  <td colspan = '2' > <div style='color:red;' id = 'erroHorario'> </div> </td>  </tr>";
	html+="<tr> <td colspan = '2' > <input type='text' style='font-size: 12px; border: 1px solid #2763a5;' id='data' name='data' size='8' maxlength='10' onkeyup='formataData(this,event);'/>(dd/MM/aaaa)</td> <td colspan = '2' > <select style='font-size: 12px; border: 1px solid #2763a5;' name='horario' id='horario'> <option value=''>"+mensagens['selecione']+"</option> <option value='0'> 00:00 </option> <option value='1'> 01:00 </option> <option value='2'> 02:00 </option> <option value='3'> 03:00 </option> <option value='4'> 04:00 </option> <option value='5'> 05:00 </option> <option value='6'> 06:00 </option> <option value='6'> 07:00 </option> <option value='8'> 08:00 </option> <option value='9'> 09:00 </option> <option value='10'> 10:00 </option> <option value='11'> 11:00 </option> <option value='12'> 12:00 </option> <option value='13'> 13:00 </option> <option value='14'> 14:00 </option> <option value='15'> 15:00 </option> <option value='16'> 16:00 </option> <option value='17'> 17:00 </option> <option value='18'> 18:00 </option> <option value='19'> 19:00 </option> <option value='20'> 20:00 </option> <option value='21'> 21:00 </option> <option value='22'> 22:00 </option> <option value='23'> 23:00 </option> </select> </td>  </tr>";
	html+="<tr> <td colspan = '2' > <b> "+ mensagens['qtd.vitimas']+":(*) </b> </td> <td colspan = '2' > <b> "+ mensagens['qtd.criminosos']+":(*) </b> </td>  </tr>";
	html+="<tr> <td colspan = '2' > <div style='color:red;' id = 'erroQtdV'> </div> </td>  <td colspan = '2' > <div style='color:red;' id = 'erroQtdC'> </div> </td>  </tr>";
	html+="<tr> <td colspan = '2' > <input style='font-size: 12px; border: 1px solid #2763a5;' type='text' id='qtdV' name='qtdV' size='2' maxlength='2'/></td> <td colspan = '2' > <input style='font-size: 12px; border: 1px solid #2763a5;' type='text' id='qtdC' name='qtdC' size='2' maxlength='2'/> </td>  </tr>";
	html+="<tr> <td colspan = '4' > <b> "+ mensagens['arma.usada']+":(*) </b> </td></tr>";
	html+="<tr> <td colspan = '4' > <div style='color:red;' id = 'erroArmaUsada'> </div> </td> </tr>";
	html+="<tr> <td colspan = '4' > <select style='font-size: 12px; border: 1px solid #2763a5;' name='armaUsada' id='armaUsada'> <option value=''>"+mensagens['selecione']+"</option> <option value='1'>"+mensagens['nao']+"</option> <option value='2'>"+mensagens['fogo']+"</option> <option value='3'>"+mensagens['branca']+"</option> <option value='4'>"+mensagens['nao.sei']+"</option> </select> </td> </tr>"
	html+="<tr> <td colspan = '4' > <b> "+ mensagens['relacao.crime']+"(*) </b> </td></tr>";
	html+="<tr> <td colspan='4'> <input type='radio' id='rcrime' onclick='setRadioRCrime(this.value)' name='rcrime' value='1' checked> "+mensagens['vitima']+" <input type='radio' onclick='setRadioRCrime(this.value)' name='rcrime' value='2' > "+mensagens['testemunha']+" <input type='radio' onclick='setRadioRCrime(this.value)' name='rcrime' value='3'> "+mensagens['tive.conhecimento']+" </td> </tr>"
	html+="<tr> <td colspan = '4' > <b>  "+ mensagens['pol.info']+"(*) </b> </td></tr>";
	html+="<tr> <td colspan='4'> <input type='radio' onclick='setRadioPolInfo(this.value)' id='polinfo' name='polinfo' value='1' checked> "+mensagens['sim.190']+" <input type='radio' id='polinfo' onclick='setRadioPolInfo(this.value)' name='polinfo' value='2' > "+mensagens['sim.delegacia']+" <input type='radio' id='polinfo' name='polinfo' onclick='setRadioPolInfo(this.value)' value='3'> "+mensagens['nao']+" <input type='radio' id='polinfo' onclick='setRadioPolInfo(this.value)' name='polinfo' value='4'> "+mensagens['nao.sei']+ " </td> </tr>"	
	html+="<tr> <td colspan = '4' > <b> "+mensagens['descricao']+":(*) </b> </td>  </tr>";
	html+="<tr> <td colspan = '4' > <div style='color:red;' id = 'erroDesc'> </div> </td>  </tr>";
	html+="<tr><td colspan = '4'> <TEXTAREA NAME='desc_alerta' id='desc_alerta' style='font-size: 12px; border: 1px solid #2763a5;' COLS='48' ROWS='6'></TEXTAREA> </td></tr>";
	html+="<tr> <td colspan = '4'><b> "+mensagens['causa.motivo.ocorrencia1']+" "+ mensagens['causa.motivo.ocorrencia2'] +":(*) </b> </td>  </tr>";
	html+="<tr> <td colspan = '4' > <div style='color:red;' id = 'erroRazoes'> </div> </td>  </tr>";
	html+="<tr> <td>	<input style='font-size: 12px; border: 1px solid #2763a5;' type='checkbox' id='razao1' name='cIluP'/> </td> <td > "+mensagens['ma.iluminacao.publica']+" </td> <td> <input type='checkbox' id='razao2' name='cFalLazJovens'/> </td> <td > "+mensagens['ausencia.lazer.jovens']+" </td> </tr>";
	html+="<tr> <td> <input type='checkbox' id='razao3' name='cDesReg'/> </td> <td > "+mensagens['desemprego.regiao']+" </td> <td> <input type='checkbox' id='razao4' name='cFacilAcesFuga'/> </td> <td > "+mensagens['facil.acesso.fulga']+" </td> </tr>";
	html+="<tr> <td> <input type='checkbox' id='razao5'	name='cDisGan'/> </td> <td > "+mensagens['disputa.gangues']+" </td> <td> <input type='checkbox' id='razao6' name='cUsoTrafDrog'/> </td> <td > "+mensagens['trafico.drogas']+" </td> </tr>";
	html+="<tr> <td> <input type='checkbox' id='razao7' name='cUsoAcl'/> </td> <td > "+mensagens['uso.alcool']+" </td> <td> <input type='checkbox' id='razao8' name='cCriRuas'/> </td> <td > "+mensagens['criancas.nas.ruas']+" </td> </tr>";
	html+="<tr> <td> <input type='checkbox' id='razao9'	name='cAltConPes'/> </td> <td > "+mensagens['alta.concentracao.pessoas']+" </td> <td> <input type='checkbox' id='razao10' name='cFalPol'/> </td> <td > "+mensagens['falta.policiamento']+" </td> </tr>";
	html+="<tr> <td> <input type='checkbox' id='razao11' name='cOmisTest'/> </td> <td > "+mensagens['omissao.testemunhas']+" </td> <td> <input type='checkbox' id='razao12' name='cProxRegPerig'/> </td> <td > "+mensagens['prox.regioes.perig']+" </td> </tr>";
	html+="<tr> <td> <input type='checkbox' id='razao13' name='cImpPen'/> </td> <td > "+mensagens['impunidade.penal']+" </td> <td> <input type='checkbox' id='razao14' name='cPistolagem'/> </td> <td > "+mensagens['pistolagem']+" </td> </tr>";
	html+="<tr> <td> <input type='checkbox' id='razao15' name='cVlcPol'/> </td> <td > "+mensagens['violencia.policial']+" </td> <td> <input type='checkbox' id='razao16' name='cFalMor'/> </td> <td > "+mensagens['falta.moradia']+" </td> </tr>";
	html+="<tr> <td> <input type='checkbox' id='razao17' name='cCriOrg'/> </td> <td > "+mensagens['crime.organizado']+" </td> <td> <input type='checkbox' id='razao18' name='cCriPas'/> </td> <td > "+mensagens['crime.passional']+" </td> </tr>";
	html+="<tr> <td> <input type='checkbox' id='razao19' name='cOutros'/> </td> <td > "+mensagens['outros']+" </td> <td> <input type='checkbox' id='razao20' name='cNaoSei'/> </td> <td > "+mensagens['nao.sei']+" </td> </tr>";					
	html+="<tr><td colspan = '4'> E-mails de pessoas que podem reafirmar seu relato. Digite até 2(dois) e-mails: </td></tr>";
	html+="<tr><td colspan = '4'> 1° E-mail <input style='font-size: 12px; border: 1px solid #2763a5;' id='emailConfirmacaoWikiCrimes1' type='text' maxlength='117' size='40' /> </td></tr>";
	html+="<tr><td colspan = '4'> 2° E-mail <input style='font-size: 12px; border: 1px solid #2763a5;' id='emailConfirmacaoWikiCrimes2' type='text' maxlength='117' size='40' /> </td></tr>";
	html+="     	<tr>";
	html+="     		<td align='center' colspan='4'> <div style='width:100%; height: 6px;'></div> </td>";
	html+="         </tr>";
	html+="<tr><td colspan = '4'> <input type='hidden' id='tipoCrime' value='"+tipoCrimeRegistro+"'> <input type='hidden' id='tipoVitima' value='"+tipoVitimaRegistro+"'> <input style='font-family: Arial, Helvetica, sans-serif; font-size: 10px;	font-weight: bold; margin: 1; padding: 1; background-color: #6da6e2; color: white;	border: 1px solid #2763a5;' onclick='validaFormRegistroCrimeWikiCrime();' type='button' value='"+mensagens['registrar']+"'/> </td></tr>";				
	html+="</table></div>";
	return html;
}

function validaFormRegistroCrimeWikiCrime(){
	var passouNaValidacao = true;	

	if(document.getElementById("tipoLocal").value == ""){		
		document.getElementById("erroTipoLocal").innerHTML = mensagens['campo.requerido'];
		passouNaValidacao = false;
		document.getElementById('tipoLocal').focus();
	}
	else{
		document.getElementById("erroTipoLocal").innerHTML = "";
	}
	if(document.getElementById("data").value == ""){		
		document.getElementById("erroData").innerHTML = mensagens['campo.requerido'];
		passouNaValidacao = false;
		document.getElementById('data').focus();
	}
	else{
		document.getElementById("erroData").innerHTML = "";
	}
	
	if(!validarData(document.getElementById("data"))){
		document.getElementById("erroData").innerHTML = mensagens['data.invalida'];
		passouNaValidacao = false;
	}else{
		document.getElementById("erroData").innerHTML = '';
	}
		
	if(document.getElementById("horario").value == ""){		
		document.getElementById("erroHorario").innerHTML = mensagens['campo.requerido'];
		passouNaValidacao = false;
		document.getElementById('horario').focus();
	}
	else{
		document.getElementById("erroHorario").innerHTML = "";
	}	
	
	
	if(document.getElementById("qtdC").value == ""){		
		document.getElementById("erroQtdC").innerHTML = mensagens['campo.requerido'];
		passouNaValidacao = false;
		document.getElementById('qtdC').focus();
	}
	else{
		if(isNaN(document.getElementById("qtdC").value))
			document.getElementById("erroQtdC").innerHTML = mensagens['campo.requerido'];
		else
			document.getElementById("erroQtdC").innerHTML = "";
	}
	
	if(document.getElementById("qtdV").value == ""){		
		document.getElementById("erroQtdV").innerHTML = mensagens['campo.requerido'];
		passouNaValidacao = false;
		document.getElementById('qtdV').focus();
	}
	else{
		if(isNaN(document.getElementById("qtdV").value))
			document.getElementById("erroQtdV").innerHTML = mensagens['campo.requerido'];
		else
			document.getElementById("erroQtdV").innerHTML = "";
	}
	
	if(document.getElementById("armaUsada").value == ""){		
		document.getElementById("erroArmaUsada").innerHTML = mensagens['campo.requerido'];
		passouNaValidacao = false;
		document.getElementById('armaUsada').focus();
	}
	else{
		document.getElementById("erroArmaUsada").innerHTML = "";
	}
	
	if(document.getElementById("desc_alerta").value == ""){
		document.getElementById("erroDesc").innerHTML = mensagens['campo.requerido'];
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
		document.getElementById("erroRazoes").innerHTML = mensagens['campo.requerido.razao'];
		passouNaValidacao = false;
		document.getElementById('razao1').focus();
	}else{
		document.getElementById("erroRazoes").innerHTML ="";
	}
		
	
	if(passouNaValidacao)		
		executaRequisicaoRegistrarCrime(document.getElementById('desc_alerta').value,crimeWikiCrimes.getLatLng().lat(),crimeWikiCrimes.getLatLng().lng(),razoes);
}

function executaRequisicaoRegistrarCrime(descricao, lat, lng, razoes){
	
    var tipoCrime = document.getElementById('tipoCrime').value;
    var tipoVitima = document.getElementById('tipoVitima').value;
    var tipoLocal = document.getElementById('tipoLocal').value;
    var data = document.getElementById('data').value;
    var horario = document.getElementById('horario').value;
    var qtdVitimas = document.getElementById('qtdV').value;
    var qtdCriminosos = document.getElementById('qtdC').value;
    var tipoArmaUsada = document.getElementById('armaUsada').value;
    var emailConf1 = document.getElementById('emailConfirmacaoWikiCrimes1').value;
    var emailConf2 = document.getElementById('emailConfirmacaoWikiCrimes2').value;
    var url = urlWikiCrimes + 'ServletWikiCrimesApi?acao=registrarCrime&lat='+lat+'&lng='+lng+'&descricao='+descricao+'&razoes='+razoes+'&rcrime='+rcrime+'&polInfo='+polInfo+'&tipoVitima='+tipoVitima+'&tipoLocal='+tipoLocal+'&data='+data+'&hora='+horario+'&qtdV='+qtdVitimas+'&qtdC='+qtdCriminosos+'&tpa='+tipoArmaUsada+'&tipoCrime='+tipoCrime+'&emailUsuario='+usuarioSessaoWikiCrimes.email+'&emailConf1='+emailConf1+'&emailConf2='+emailConf2+'&jsoncallback=?'; 
    variavelGlobalJQuery.getJSON(url, false, function(data){		
    	document.getElementById('botaoRegistroCrimesWikiCrimes').innerHTML = "<div id='botaoRegistroCrimesWikiCrimes'><div onclick='mostraEscondeRegistroCrimeWikiCrimes();' class='botao'> "+mensagens['registrarCrime']+" </div></div>";
    	statusRegistroCrimesWikiCrimes='invisivel';
    	if(data.resposta == 'ok'){			
			crimeWikiCrimes.openInfoWindowHtml(mensagens['crime.registrado.sucesso']);
			mapWikiCrimes.addOverlay(crimeWikiCrimes);
			GEvent.trigger(mapWikiCrimes,'moveend');			
		}else
			alert(data.resposta);   	
	});				
}

var polInfo="1";
function setRadioPolInfo(valor){
	polInfo = valor;
}

var rcrime="1";
function setRadioRCrime(valor){
	rcrime = valor;
}

function mostraTipoVitima(valor){
	tipoCrimeRegistro = "0";
	tipoVitimaRegistro = "0";
	document.getElementById("div_erro_tipo_crime_req").innerHTML = "";
	if(valor == '4'){
		document.getElementById("mostra_tipo_vitima").innerHTML = "<table cellpading='0' cellspacing='0'> <tr> <td> <input type='radio' name='group1' onclick='setaTipoVitimaTipoCrime(4,1);'>" + mensagens['tipo.crime.roubo'] +" "+ mensagens['tipo.vitima.pessoa'] + "</td> </tr> <tr> <td> <input type='radio' name='group1' onclick='setaTipoVitimaTipoCrime(4,2);'>" + mensagens['tipo.crime.roubo'] +" "+ mensagens['tipo.vitima.propriedade'] + "</td> </tr> <tr> <td> <input type='radio' name='group1' onclick='setaTipoVitimaTipoCrime(1,1);'>" + mensagens['tipo.crime.tentativa.roubo'] +" "+ mensagens['tipo.vitima.pessoa'] + "</td> </tr> <tr> <td> <input type='radio' name='group1' onclick='setaTipoVitimaTipoCrime(1,2);'>" + mensagens['tipo.crime.tentativa.roubo'] +" "+ mensagens['tipo.vitima.propriedade'] + "</td> </tr> </table>";
	}else if (valor == '3'){
		document.getElementById("mostra_tipo_vitima").innerHTML = "<table cellpading='0' cellspacing='0'> <tr> <td> <input type='radio' name='group1' onclick='setaTipoVitimaTipoCrime(3,1);'>" + mensagens['tipo.crime.furto'] +" "+ mensagens['tipo.vitima.pessoa'] + "</td> </tr> <tr> <td> <input type='radio' name='group1' onclick='setaTipoVitimaTipoCrime(3,2);'>" +mensagens['tipo.crime.furto'] +" "+ mensagens['tipo.vitima.propriedade'] + "</td> </tr> <tr> <td> <input type='radio' name='group1' onclick='setaTipoVitimaTipoCrime(2,1);'>" + mensagens['tipo.crime.tentativa.furto'] +" "+ mensagens['tipo.vitima.pessoa'] + "</td> </tr> <tr> <td> <input type='radio' name='group1' onclick='setaTipoVitimaTipoCrime(2,2);'>" +mensagens['tipo.crime.tentativa.furto'] +" "+ mensagens['tipo.vitima.propriedade'] + "</td> </tr> </table>";
	}else if(valor == '5'){
		document.getElementById("mostra_tipo_vitima").innerHTML = "<table cellpading='0' cellspacing='0'> <tr> <td> <input type='radio' name='group1' onclick='setaTipoVitimaTipoCrime(5,3);'>" + mensagens['tipo.vitima.rixas.ou.brigas'] + "</td> </tr> <tr> <td> <input type='radio' name='group1' onclick='setaTipoVitimaTipoCrime(5,4);'>" + mensagens['tipo.vitima.violencia.domestica'] + "</td> </tr> <tr> <td> <input type='radio' name='group1' onclick='setaTipoVitimaTipoCrime(5,5);'>" + mensagens['tipo.vitima.abuso.de.autoridade'] + "</td> </tr> <tr> <td> <input type='radio' name='group1' onclick='setaTipoVitimaTipoCrime(5,6);'>" + mensagens['tipo.vitima.homicidio'] + "</td> </tr> <tr> <td> <input type='radio' name='group1' onclick='setaTipoVitimaTipoCrime(5,7);'>" + mensagens['tipo.vitima.tentativa.homicidio'] + "</td> </tr> <tr> <td> <input type='radio' name='group1' onclick='setaTipoVitimaTipoCrime(5,8);'>" + mensagens['tipo.vitima.latrocinio'] + "</td> </tr> </table>";
	}else{
		document.getElementById("mostra_tipo_vitima").innerHTML = "";
	}
}

function htmlRegistroCrimeWikiCrimes(){
	var html = "<table cellpading='0' cellspacing='0' style='width:226px; padding:0px ;border:1px solid #333333; opacity: .84; -moz-opacity:0.84; filter: alpha(opacity=84); background-color:#F0F8FF;font-family:Arial, sans-serif;  font-size: 11px;'>";
	html+="     	<tr>";
	html+="     		<td> <div style='width:4px;'></div> </td><td align='left' colspan='3'> <b >"+ mensagens['registro.de.crimes'] +"</b> </td> <td align='right' colspan='1'> <b onclick='mostraEscondeRegistroCrimeWikiCrimes();' style='font-size: 10px;cursor: pointer;'> X </b> </td> <td> <div style='width:4px;'></div> </td>";
	html+="         </tr>";
	html+="     	<tr>";
	html+="     		<td align='center' colspan='6'> <div style='width:100%; height: 4px; border-top:1px solid #333333;'></div> </td>";
	html+="         </tr>";
	html+="     	<tr>";
	html+="     		<td colspan='1'> <div style='width:4px;'></div> </td> <td align='left' colspan='5'> " + mensagens['registro.crimes.pergunta.tipo.crime'] + " </td>";
	html+="         </tr>";
	html+="     	<tr>";
	html+="     		<td colspan='1'> <div style='width:4px;'></div> </td> <td align='left' colspan='5'> <select style='font-size:10px;' name='tipo_crime' id='tipo_crime' onchange='mostraTipoVitima(this.value);'> <option value='0'> "+mensagens['selecione']+" </option> <option value='4'> "+mensagens['tipo.crime.roubo'] +" </option> <option value='3'> "+mensagens['tipo.crime.furto'] +" </option> <option value='5'> "+mensagens['tipo.crime.outros'] +" </option> </select> </td>";
	html+="         </tr>";
	html+="     	<tr>";
	html+="     		<td colspan='1'> <div style='width:4px;'></div> </td> <td align='left' colspan='5'> <div style='color:red;' id='div_erro_tipo_crime_req'> </div> </td>";
	html+="         </tr>";
	html+="     	<tr>";
	html+="     		<td colspan='1'> <div style='width:4px;'></div> </td> <td align='left' colspan='5'> <div id='mostra_tipo_vitima'> </div> </td>";
	html+="         </tr>";
	html+="     	<tr>";
	html+="     		<td colspan='1'> <div style='width:4px;'></div> </td><td align='left' colspan='4'> <input class='button' type='button' value='"+mensagens['marcar.no.mapa']+"' onclick='prepararPlotarEventoWikiCrimes();'/> <input class='button' type='button' value='"+mensagens['cancelar']+"' onclick='mostraEscondeRegistroCrimeWikiCrimes();'/> </td><td> <div style='width:4px;'></div> </td>";
	html+="         </tr>";
	html+="     	<tr>";
	html+="     		<td align='center' colspan='6'> <div style='width:100%; height: 4px;'></div> </td>";
	html+="         </tr>";
	html += "	</table>";
	return html;
}

var registroCrimeWikicrimesDiv;

function ComandoRegistroCrimeWikicrimes() {
}
ComandoRegistroCrimeWikicrimes.prototype = new GControl();

ComandoRegistroCrimeWikicrimes.prototype.initialize = function(mapWikiCrimes) {
  var container = document.createElement("div");

  registroCrimeWikicrimesDiv = document.createElement("div");
  this.setButtonStyle_(registroCrimeWikicrimesDiv);
  container.appendChild(registroCrimeWikicrimesDiv);
  registroCrimeWikicrimesDiv.innerHTML = htmlRegistroCrimeWikiCrimes();
 
  mapWikiCrimes.getContainer().appendChild(container);
  return container;
}


ComandoRegistroCrimeWikicrimes.prototype.getDefaultPosition = function() {
  return new GControlPosition(G_ANCHOR_TOP_RIGHT, new GSize(10,124));
}


ComandoRegistroCrimeWikicrimes.prototype.setButtonStyle_ = function(button) {				
  //button.style.height = "3.8em";
  //button.style.cursor = "pointer";
  //button.style.backgroundImage = "url('./images/logoWikicrimesEmbedded.PNG')";
}

/* ]]> */