/* <![CDATA[ */
var urlWikiCrimes = "http://localhost:8081/wikicrimes/"
	
document.write("<script type='text/javascript' src='"+urlWikiCrimes+"wikicrimesapi/js/constantesWikiCrimesApi.js'></script>");
document.write("<script type='text/javascript' src='"+urlWikiCrimes+"wikicrimesapi/js/language.js'></script>");
document.write("<script type='text/javascript' src='"+urlWikiCrimes+"wikicrimesapi/js/utilWikiCrimesApi.js'></script>");
document.write("<script type='text/javascript' src='"+urlWikiCrimes+"wikicrimesapi/js/googleClustererWikiCrimesApi.js'></script>");
document.write("<script type='text/javascript' src='"+urlWikiCrimes+"wikicrimesapi/js/controlesWikiCrimesApi.js'></script>");
document.write("<script type='text/javascript' src='"+urlWikiCrimes+"wikicrimesapi/js/kernelMapWikiCrimesApi.js'></script>");
document.write("<link rel='stylesheet' href='"+urlWikiCrimes+"wikicrimesapi/css/wikicrimesapi.css' type='text/css'/>");
var mapWikiCrimes;
var usuarioSessaoWikiCrimes;
var GClusterer;
var GVetorMarcadores;
var crimesAtuais = {};
var podeCarregarCrimes = true;
var mensagens = {};
var comandoFiltroWikiCrimes;
var comandoRegistroCrimeWikiCrimes;
var variavelGlobalJQuery = '';
//Filtros
var dataInicialWikicrimes = '';
var dataFinalWikicrimes = '';
var tipoCrimeWikiCrimes = '0';
var tipoVitimaWikiCrimes = '0';
var getMeusCrimesWikiCrimes = false;
//Filtros

function WikiCrimes(mapClient,variavelJQuery, idioma, filtroWikiCrimes, userWikiCrimes){
	variavelGlobalJQuery = variavelJQuery;
	usuarioSessaoWikiCrimes = userWikiCrimes;
	if(userWikiCrimes != 'undefined' && userWikiCrimes != null)
		usuarioSessaoWikiCrimes.locale = idioma;
	carregarIdioma(idioma);
	mapWikiCrimes = mapClient;
	GClusterer = new MarkerClusterer(mapWikiCrimes, []);
	GVetorMarcadores = [];
	mapWikiCrimes.addControl(new LegendaWikicrimes());
	mapWikiCrimes.addControl(new ComandosWikicrimes());
	comandoFiltroWikiCrimes = new ComandoFiltroWikicrimes();
	comandoRegistroCrimeWikiCrimes = new ComandoRegistroCrimeWikicrimes();
	WikiCrimes.prototype.enableViewPortWikiCrimes = function() {
		podeCarregarCrimes=true;
		GEvent.addListener(mapWikiCrimes, "moveend", function() {
			if(podeCarregarCrimes){
				var b = mapWikiCrimes.getBounds();
				var norte = b.getNorthEast().lat();
				var sul = b.getSouthWest().lat();
				var leste = b.getNorthEast().lng();
				var oeste = b.getSouthWest().lng();
				limpaCrimesForaViewPort(b);
				var emailUsuario = '';
				if(getMeusCrimesWikiCrimes)
					emailUsuario = usuarioSessaoWikiCrimes.email;
				var url = urlWikiCrimes + 'ServletWikiCrimesApi?acao=listaCrimes&n='+norte+'&s='+sul+'&e='+leste+'&w='+oeste+'&di='+dataInicialWikicrimes+'&df='+dataFinalWikicrimes+'&tc='+tipoCrimeWikiCrimes+'&tv='+tipoVitimaWikiCrimes+'&emailUsuario='+emailUsuario+'&jsoncallback=?'
				variavelGlobalJQuery.getJSON(url, false, function(data){
					trataRespostaListaCrimesWikiCrimes(data);
				});
				
			}	
		});
		GEvent.addListener(mapWikiCrimes,"infowindowbeforeclose", function(overlay,latlng) {
			podeCarregarCrimes=true;
			if(crimeWikiCrimes != null){
				mapWikiCrimes.removeOverlay(crimeWikiCrimes);
			}
			document.getElementById('botaoRegistroCrimesWikiCrimes').innerHTML = "<div id='botaoRegistroCrimesWikiCrimes'><div onclick='mostraEscondeRegistroCrimeWikiCrimes();' class='botao'> "+mensagens['registrarCrime']+" </div></div>";
			statusRegistroCrimesWikiCrimes='invisivel';
		});		
	}
	
	WikiCrimes.prototype.disableViewPortWikiCrimes = function() {
		crimesAtuais={};
		GVetorMarcadores = [];
		GClusterer.clearMarkers();
		podeCarregarCrimes=false;
	}
	
	WikiCrimes.prototype.setFilter = function(filtroWikiCrimes) {
		if( filtroWikiCrimes != undefined && filtroWikiCrimes != null ){
			dataInicialWikicrimes = filtroWikiCrimes.dtInicial;
			dataFinalWikicrimes = filtroWikiCrimes.dtFinal;
			tipoCrime = filtroWikiCrimes.tipoCrime;
			tipoVitima = filtroWikiCrimes.subTipoCrime;
		}		
	}
	
	WikiCrimes.prototype.refreshMap = function() {
		GEvent.trigger(mapWikiCrimes,'moveend');
	}
	
	this.enableViewPortWikiCrimes();
	this.setFilter(filtroWikiCrimes);
	this.refreshMap();
}

function WikiCrimesFilter(){
	this.dtInicial = '';
	this.dtFinal = '';
	this.tipoCrime = '';
	this.subTipoCrime = '';
}

function WikiCrimesUser(){
	this.firstName = '';
	this.lastName = '';
	this.email = '';
	this.locale = '';
}

function trataRespostaListaCrimesWikiCrimes(data){
	var array = eval(data);
	for (var i = 0; i < array.length ; i++){
		var atributosCrime = array[i].split("|");
		var marker = createMarkerWikiCrimes(atributosCrime);
		if (crimesAtuais[atributosCrime[0]] == undefined){
        	GVetorMarcadores.push(marker);
        	crimesAtuais[atributosCrime[0]] = marker;
        }	
	}
	GClusterer.clearMarkers();
	GClusterer = new MarkerClusterer(mapWikiCrimes, GVetorMarcadores);
}

function setaParametrosFiltro(){
	crimesAtuais={};
	GVetorMarcadores = [];
	GClusterer.clearMarkers();	
	dataInicialWikicrimes = document.getElementById('dataInicialWikiCrimesApi').value;
	dataFinalWikicrimes = document.getElementById('dataFinalWikiCrimesApi').value;
	tipoCrimeWikiCrimes = document.getElementById('tipo_crime').value;
	tipoVitimaWikiCrimes = document.getElementById('tipo_vitima').value;
	getMeusCrimesWikiCrimes = document.getElementById('emailUsuarioWikiCrimes').checked;
	GEvent.trigger(mapWikiCrimes,'moveend');
}

function limpaCrimesForaViewPort(mapBounds){
	GVetorMarcadores = [];
	for (k in crimesAtuais) {
		if (!mapBounds.contains(crimesAtuais[k].getPoint())){
			delete crimesAtuais[k];
		}
	}
	for (k in crimesAtuais) {
		GVetorMarcadores.push(crimesAtuais[k]);
	}
}

function createMarkerWikiCrimesTemp(tipoCrimeSel){
	var baseIcon = new GIcon(G_DEFAULT_ICON);
	baseIcon.shadow = "";
	baseIcon.shadow = "";
	baseIcon.iconSize = new GSize(14, 24);
	baseIcon.iconAnchor = new GPoint(2, 24);
	baseIcon.infoWindowAnchor = new GPoint(9, 2);
	markerOptions = { icon:baseIcon };
	var marker = new CrimeMarker(mapWikiCrimes.getCenter(), markerOptions);//new GMarker(new GLatLng(atributosCrime[1],atributosCrime[2]), markerOptions);
    marker.idTipoCrime = parseInt(tipoCrimeSel);
    if( tipoCrimeSel == "1" || tipoCrimeSel == "4" ){
		baseIcon.image = urlWikiCrimes + "images/baloes/marcadorTempVermelho.png";
	}
	if( tipoCrimeSel == "2" || tipoCrimeSel == "3" ){
		baseIcon.image = urlWikiCrimes+  "images/baloes/marcadorTempAzul.png";
	}
	if( tipoCrimeSel == "5" ){
		baseIcon.image = urlWikiCrimes+ "images/baloes/marcadorTempLaranja.png";
	}
	return marker;
}

function createMarkerWikiCrimes(atributosCrime){
	var baseIcon = new GIcon(G_DEFAULT_ICON);
	baseIcon.shadow = "";
	baseIcon.shadow = "";
	baseIcon.iconSize = new GSize(14, 24);
	baseIcon.iconAnchor = new GPoint(2, 24);
	baseIcon.infoWindowAnchor = new GPoint(9, 2);
	markerOptions = { icon:baseIcon };
    var marker = new CrimeMarker(new GLatLng(atributosCrime[1],atributosCrime[2]), markerOptions);//new GMarker(new GLatLng(atributosCrime[1],atributosCrime[2]), markerOptions);
    marker.idCrime = atributosCrime[0];
    marker.idTipoCrime = atributosCrime[3];
	if( atributosCrime[3] == "1" || atributosCrime[3] == "4" ){
		baseIcon.image = urlWikiCrimes + "images/baloes/marcadorTempVermelho.png";
	}
	if( atributosCrime[3] == "2" || atributosCrime[3] == "3" ){
		baseIcon.image = urlWikiCrimes+  "images/baloes/marcadorTempAzul.png";
	}
	if( atributosCrime[3] == "5" ){
		baseIcon.image = urlWikiCrimes+ "images/baloes/marcadorTempLaranja.png";
	}
	GEvent.addListener(marker, "click", function() {
		podeCarregarCrimes = false;
		marker.openInfoWindowHtml("<iframe src='"+urlWikiCrimes+"mostrarDadosOpenSocial.html?idCrime=" + atributosCrime[0] + "' width='380' height='150' frameborder='0'></iframe>");
	});
	return marker;
}

var widthMapClienteWikiCrimes;
var heightMapClienteWikiCrimes;
function getTamMapClienteWikiCrimes(){
	var b = mapWikiCrimes.getBounds();
	var north = b.getNorthEast().lat();
	var south = b.getSouthWest().lat();
	var east = b.getNorthEast().lng();
	var west = b.getSouthWest().lng();	

	var northPixel = mapWikiCrimes.getCurrentMapType().getProjection().fromLatLngToPixel(b.getNorthEast(), mapWikiCrimes.getZoom()).y;
	var southPixel = mapWikiCrimes.getCurrentMapType().getProjection().fromLatLngToPixel(b.getSouthWest(), mapWikiCrimes.getZoom()).y;
	var eastPixel = mapWikiCrimes.getCurrentMapType().getProjection().fromLatLngToPixel(b.getNorthEast(), mapWikiCrimes.getZoom()).x;
	var westPixel = mapWikiCrimes.getCurrentMapType().getProjection().fromLatLngToPixel(b.getSouthWest(), mapWikiCrimes.getZoom()).x;
	widthMapClienteWikiCrimes = (eastPixel - westPixel);
	heightMapClienteWikiCrimes = (southPixel - northPixel);
}

function CrimeMarker(latlng, opts) {
	this.latlng = latlng;
	this.idCrime = 0;
	this.idTipoCrime = 0;
	GMarker.apply(this, arguments);
}
/**
*
*/
CrimeMarker.prototype = new GMarker(new GLatLng(0, 0));
/* ]]> */