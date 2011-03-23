/**
*
*/
function CrimeMarker(latlng, opts) {
    this.latlng = latlng;
    this.idCrime = 0;
	this.registrado = 0;
	this.evento = 0;
    GMarker.apply(this, arguments);
}

/**
*
*/
CrimeMarker.prototype = Object.extend(new GMarker(new GLatLng(0, 0)), {

    initialize: function(map) {
        GMarker.prototype.initialize.call(this, map);
    },

    setIdCrime: function(i) {
        this.idCrime = i;
    },
     setEvento: function(i) {
        this.evento = i;
    },
    getEvento: function(i) {
        return this.evento;
    },
    
    setIdTipoCrime: function(i) {
        this.idTipoCrime = i;
    },
     setRegistrado: function(i) {
        this.registrado = i;
    },
     getRegistrado: function(i) {
        return this.registrado;
    },
    
    getIdCrime: function() {
        return this.idCrime;
    },
	getIdTipoCrime: function() {
        return this.idTipoCrime;
    },
    registrarCrime: function(idTipoCrime,idSubTipoCrime) {
        var latitude = this.getLatLng().lat();
        var longitude = this.getLatLng().lng();
        var myHtml = "<iframe src='/wikicrimes/registrarCrime.html?tipoCrime="+idTipoCrime+"&tipoVitima="+idSubTipoCrime+ "&latitude="+latitude+"&longitude="+longitude+"' width='600' height='250' frameborder='0'></iframe>";
        this.openInfoWindowHtml(myHtml, 600);
    },
    
    confirmaCrime: function(idConfirmacao) {
        var latitude = this.getLatLng().lat();
        var longitude = this.getLatLng().lng();
        var myHtml = "<iframe src='/wikicrimes/confirmaCrime.html?idConfirmacao="+idConfirmacao+"' width='400' height='250' frameborder='0'></iframe>";
        this.openInfoWindowHtml(myHtml, 400);
    },
    
    mostrarDados: function(idCrime) {
        var myHtml = "<iframe src='/wikicrimes/mostrarDados.html?idCrime="+idCrime+"' width='400' height='150' frameborder='0'></iframe>";
        var myHtml2 = "<iframe src='/wikicrimes/mostrarInformacoes.html?idCrime="+idCrime+"' width='400' height='150' frameborder='0'></iframe>";
        this.openInfoWindowTabsHtml([new GInfoWindowTab('Dados',myHtml),new GInfoWindowTab('Coment�rios',myHtml2)]);
    }

});

/**
*
*/
function createMarker(point, tipoCrime, zoom)  {
	 var icone = new GIcon(G_DEFAULT_ICON);
	 var marker;
	 icone.shadow="";
	  if (tipoCrime) {
	  		if (tipoCrime == 1)
               icone.image = "/wikicrimes/images/baloes/vermelha.png";
  	  		if (tipoCrime == 2)
               icone.image = "/wikicrimes/images/baloes/azul.png";
   	  		if (tipoCrime == 3)
               icone.image = "/wikicrimes/images/baloes/azul.png";
   	  		if (tipoCrime == 4)
   	  		   icone.image = "/wikicrimes/images/baloes/vermelha.png";
			if (tipoCrime == 5)
   	  		   icone.image = "/wikicrimes/images/baloes/laranja.png";               
               
       }
      
	    marker = new CrimeMarker(point, zoom,{draggable: true, icon: icone});
	    marker.idTipoCrime=tipoCrime;
	    
    return marker;
}
function createMarkerId(point, tipoCrime, idCrime)  {
		var marker=createMarker(point,tipoCrime);	
	    marker.disableDragging();
	    
		GEvent.addListener( marker, "click", function() {
								    	
								    	marker.mostrarDados(idCrime);
									});
		gmarkers.put(idCrime,marker);	    
    return marker;
}
function createMarkerTemp(point, tipoCrime)  {
	 var icone = new GIcon(G_DEFAULT_ICON);
	 var marker;
	 icone.shadow="/wikicrimes/images/baloes/shadow50.png";


	  if (tipoCrime) {
	  		if (tipoCrime == 1)
               icone.image = "/wikicrimes/images/baloes/vermelha_hint.png";
  	  		if (tipoCrime == 2)
               icone.image = "/wikicrimes/images/baloes/azul_hint.png";
   	  		if (tipoCrime == 3)
               icone.image = "/wikicrimes/images/baloes/azul_hint.png";
   	  		if (tipoCrime == 4)
   	  		   icone.image = "/wikicrimes/images/baloes/vermelha_hint.png";
			if (tipoCrime == 5)
   	  		   icone.image = "/wikicrimes/images/baloes/laranja_hint.png";               
               
       }
	  	 icone.iconSize= new GSize(122,34);
       	 
      
	    marker = new CrimeMarker(point, {draggable: true, icon: icone});
	    marker.idTipoCrime=tipoCrime;
    return marker;
}

