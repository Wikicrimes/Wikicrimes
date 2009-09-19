var estaRegistrandoArea = false;
//Comando dentro do mapa para mover
function ComandoMao() {
}
ComandoMao.prototype = new GControl();
var kernelEnable = false;
ComandoMao.prototype.initialize = function(map) {
  var container = document.createElement("div");

  var maoDiv = document.createElement("div");
  this.setButtonStyle_(maoDiv);
  container.appendChild(maoDiv);
  maoDiv.appendChild(document.createTextNode(""));
  GEvent.addDomListener(maoDiv, "click", function() {
  	if(!estaDesenhandoArea){
  		selecionarComando(map, 'mao');
		setPodeRegistrar(false, 0);
		removerPoligono();
	}
  	document.getElementById("divTelaFiltro").style.visibility = "hidden";
  	if(!aindaCarregando){
  		if(kernelEnable){	
  			mostraCrimesAgrupador();
			selecionarComando(map, 'mao');
			kernelEnable = false;
			apagaMapaKernel();
			mostraMarcadores();
			podeCarregarCrimes=true;
  		}	
		
	}  
		//document.getElementById("divExplicaMarcarArea").style.visibility = "hidden"; 
  });
  
  GEvent.addDomListener(maoDiv, "mouseover", function() {
  		
  });
  
  GEvent.addDomListener(maoDiv, "mouseout", function() {  		
  		
  });
     
  map.getContainer().appendChild(container);
  return container;
}


ComandoMao.prototype.getDefaultPosition = function() {
  return new GControlPosition(G_ANCHOR_TOP_LEFT, new GSize(80,7));
}


ComandoMao.prototype.setButtonStyle_ = function(button) {
  //button.style.textDecoration = "underline";
 // button.style.color = "#0000cc";
  //button.style.backgroundColor = "white";
  //button.style.font = "small Arial";
  //button.style.border = "1px solid black";
  //button.style.padding = "2px";
  //button.style.marginBottom = "3px";
  //button.style.textAlign = "center";
  button.style.width = "7.42em";
  button.style.height = "2.68em";
  button.style.cursor = "pointer";
  button.style.backgroundImage = "url('./images/comandoMao_"+localeWikiCrimes+".png')";
}

function ComandoFiltro() {
}
ComandoFiltro.prototype = new GControl();

ComandoFiltro.prototype.initialize = function(map) {
  var container = document.createElement("div");

  var filtroDiv = document.createElement("div");
  this.setButtonStyle_(filtroDiv);
  container.appendChild(filtroDiv);
  filtroDiv.appendChild(document.createTextNode(""));
  GEvent.addDomListener(filtroDiv, "click", function() {
	  selecionarComando(map, 'filtro');
	  document.getElementById("divTelaFiltro").style.visibility = "visible";
  });
  
  GEvent.addDomListener(filtroDiv, "mouseover", function() {
  		mostraHintComandosMapa("tutor.texto.filtro", "178px", "134px");
  });
  
  GEvent.addDomListener(filtroDiv, "mouseout", function() {  		
  		removeHintComandosMapa();
  });
     
  map.getContainer().appendChild(container);
  return container;
}


ComandoFiltro.prototype.getDefaultPosition = function() {
  return new GControlPosition(G_ANCHOR_TOP_LEFT, new GSize(80,40));
}


ComandoFiltro.prototype.setButtonStyle_ = function(button) {
  //button.style.textDecoration = "underline";
 // button.style.color = "#0000cc";
  //button.style.backgroundColor = "white";
  //button.style.font = "small Arial";
  //button.style.border = "1px solid black";
  //button.style.padding = "2px";
  //button.style.marginBottom = "3px";
  //button.style.textAlign = "center";
  button.style.width = "7.42em";
  button.style.height = "2.68em";
  button.style.cursor = "pointer";
  button.style.backgroundImage = "url('./images/comandoFiltro_"+localeWikiCrimes+".png')";
}

function ComandoFiltroSelecionado() {
}
ComandoFiltroSelecionado.prototype = new GControl();

ComandoFiltroSelecionado.prototype.initialize = function(map) {
  var container = document.createElement("div");

  var filtroDiv = document.createElement("div");
  this.setButtonStyle_(filtroDiv);
  container.appendChild(filtroDiv);
  filtroDiv.appendChild(document.createTextNode(""));
  GEvent.addDomListener(filtroDiv, "click", function() {  		
		selecionarComando(map, 'mao');
		document.getElementById("divTelaFiltro").style.visibility = "hidden";
  });
  GEvent.addDomListener(filtroDiv, "mouseover", function() {
	  mostraHintComandosMapa("tutor.texto.filtro", "178px", "134px");
  });
  
  GEvent.addDomListener(filtroDiv, "mouseout", function() {  		
  		removeHintComandosMapa();
  });    
  map.getContainer().appendChild(container);
  return container;
}


ComandoFiltroSelecionado.prototype.getDefaultPosition = function() {
  return new GControlPosition(G_ANCHOR_TOP_LEFT, new GSize(80,40));
}

ComandoFiltroSelecionado.prototype.setButtonStyle_ = function(button) {
	  //button.style.textDecoration = "underline";
	 // button.style.color = "#0000cc";
	  //button.style.backgroundColor = "white";
	  //button.style.font = "small Arial";
	  //button.style.border = "1px solid black";
	  //button.style.padding = "2px";
	  //button.style.marginBottom = "3px";
	  //button.style.textAlign = "center";
	  button.style.width = "7.42em";
	  button.style.height = "2.68em";
	  button.style.cursor = "pointer";
	  button.style.backgroundImage = "url('./images/comandoFiltroSel_"+localeWikiCrimes+".png')";
	}

//Comando dentro do mapa para marcar uma area
function ComandoMarcadorDeArea() {
}
ComandoMarcadorDeArea.prototype = new GControl();

ComandoMarcadorDeArea.prototype.initialize = function(map) {
  var container = document.createElement("div");

  var marcarAreaDiv = document.createElement("div");
  this.setButtonStyle_(marcarAreaDiv);
  container.appendChild(marcarAreaDiv);
  marcarAreaDiv.appendChild(document.createTextNode(""));
  GEvent.addDomListener(marcarAreaDiv, "click", function() {
  	if(!estaRegistrandoArea){
  		
	  	//selecionarComando(map, 'area');	   
	    registrarArea1();
	}    
    //var label = new ELabel(polyListArea.getVertex(0), '<div style="background-color:#ffffff;border:1px solid blue;color:blue;width:120px;height:14px"> Marque os pontos de sua area no mapa </div>', null, new GSize(6,-10), 75);
  });
  
  GEvent.addDomListener(marcarAreaDiv, "mouseover", function() {
  		mostraHintComandosMapa("webapp.area.erro.info.hint.area", "138px", "268px");
  });
  
  GEvent.addDomListener(marcarAreaDiv, "mouseout", function() {  		
  		removeHintComandosMapa();
  });    

  map.getContainer().appendChild(container);
  return container;
}


ComandoMarcadorDeArea.prototype.getDefaultPosition = function() {
  return new GControlPosition(G_ANCHOR_TOP_LEFT, new GSize(170,7));
}


ComandoMarcadorDeArea.prototype.setButtonStyle_ = function(button) {
  //button.style.textDecoration = "underline";
 // button.style.color = "#0000cc";
  //button.style.backgroundColor = "white";
  //button.style.font = "small Arial";
  //button.style.border = "1px solid black";
  //button.style.padding = "2px";
  //button.style.marginBottom = "3px";
  //button.style.textAlign = "center";
  button.style.width = "7.42em";
  button.style.height = "2.68em";
  button.style.cursor = "pointer";
  button.style.backgroundImage = "url('./images/comandoArea_"+localeWikiCrimes+".png')";
}

//Comando dentro do mapa para mover
function ComandoMaoSelecionado() {
}
ComandoMaoSelecionado.prototype = new GControl();

ComandoMaoSelecionado.prototype.initialize = function(map) {
  var container = document.createElement("div");

  var maoDiv = document.createElement("div");
  this.setButtonStyle_(maoDiv);
  container.appendChild(maoDiv);
  maoDiv.appendChild(document.createTextNode(""));
  GEvent.addDomListener(maoDiv, "click", function() {  		
		selecionarComando(map, 'mao');
		setPodeRegistrar(false, 0);
		removerPoligono();		 
  });
  GEvent.addDomListener(maoDiv, "mouseover", function() {
  		
  });
  
  GEvent.addDomListener(maoDiv, "mouseout", function() {  		
  		
  });    
  map.getContainer().appendChild(container);
  return container;
}


ComandoMaoSelecionado.prototype.getDefaultPosition = function() {
  return new GControlPosition(G_ANCHOR_TOP_LEFT, new GSize(80,7));
}


ComandoMaoSelecionado.prototype.setButtonStyle_ = function(button) {
  //button.style.textDecoration = "underline";
 // button.style.color = "#0000cc";
  //button.style.backgroundColor = "white";
  //button.style.font = "small Arial";
  //button.style.border = "1px solid black";
  //button.style.padding = "2px";
  //button.style.marginBottom = "3px";
  //button.style.textAlign = "center";
  button.style.width = "7.42em";
  button.style.height = "2.68em";
  button.style.cursor = "pointer";
  button.style.backgroundImage = "url('./images/comandoMaoSel_"+localeWikiCrimes+".png')";
}


//Comando dentro do mapa para marcar uma area
function ComandoMarcadorDeAreaSelecionado() {
}
ComandoMarcadorDeAreaSelecionado.prototype = new GControl();

ComandoMarcadorDeAreaSelecionado.prototype.initialize = function(map) {
  var container = document.createElement("div");

  var marcarAreaDiv = document.createElement("div");
  this.setButtonStyle_(marcarAreaDiv);
  container.appendChild(marcarAreaDiv);
  marcarAreaDiv.appendChild(document.createTextNode(""));
  GEvent.addDomListener(marcarAreaDiv, "click", function() {
    //startShape();
  });
  GEvent.addDomListener(marcarAreaDiv, "mouseover", function() {
  		mostraHintComandosMapa("webapp.area.erro.info.hint.area", "138px", "268px");
  });
  
  GEvent.addDomListener(marcarAreaDiv, "mouseout", function() {  		
  		removeHintComandosMapa();
  });    

  map.getContainer().appendChild(container);
  return container;
}


ComandoMarcadorDeAreaSelecionado.prototype.getDefaultPosition = function() {
  return new GControlPosition(G_ANCHOR_TOP_LEFT, new GSize(170,7));
}


ComandoMarcadorDeAreaSelecionado.prototype.setButtonStyle_ = function(button) {
  //button.style.textDecoration = "underline";
 // button.style.color = "#0000cc";
  //button.style.backgroundColor = "white";
  //button.style.font = "small Arial";
  //button.style.border = "1px solid black";
  //button.style.padding = "2px";
  //button.style.marginBottom = "3px";
  //button.style.textAlign = "center";
  button.style.width = "7.42em";
  button.style.height = "2.68em";
  button.style.cursor = "pointer";
  button.style.backgroundImage = "url('./images/comandoAreaSel_"+localeWikiCrimes+".png')";
}


//Comando dentro do mapa para gerarEmbed
function ComandoEmbed() {
}
ComandoEmbed.prototype = new GControl();

ComandoEmbed.prototype.initialize = function(map) {
  var container = document.createElement("div");

  var embedDiv = document.createElement("div");
  this.setButtonStyle_(embedDiv);
  container.appendChild(embedDiv);
  embedDiv.appendChild(document.createTextNode(textoEmbedMapaWikiCrimes));
  GEvent.addDomListener(embedDiv, "click", function() {
	 gerarEmbedded(); 
     selecionarComando(map, 'mao');
  });
  GEvent.addDomListener(embedDiv, "mouseover", function() {
  
  });
  
  GEvent.addDomListener(embedDiv, "mouseout", function() {  		
  
  });    

  map.getContainer().appendChild(container);
  return container;
}


ComandoEmbed.prototype.getDefaultPosition = function() {
  return new GControlPosition(G_ANCHOR_TOP_RIGHT, new GSize(32,28));
}


ComandoEmbed.prototype.setButtonStyle_ = function(button) {
  button.style.textDecoration = "underline";
  //button.style.color = "#0000cc";
  button.style.backgroundColor = "white";
  button.style.border = "1px solid black";
  button.style.padding = "2px";
  button.style.marginBottom = "3px";
  button.style.textAlign = "center";
 
  button.style.cursor = "pointer";
  //button.style.backgroundImage = "url('./images/comandoEmbed.png')";
}

function ComandoKernel() {
}
ComandoKernel.prototype = new GControl();

ComandoKernel.prototype.initialize = function(map) {
  var container = document.createElement("div");

  var kernelDiv = document.createElement("div");
  this.setButtonStyle_(kernelDiv);
  container.appendChild(kernelDiv);
  kernelDiv.appendChild(document.createTextNode(""));
  GEvent.addDomListener(kernelDiv, "click", function() {	 
	  if(!aindaCarregando){
		  	selecionarComando(map, 'kernel');
		  	limpaCrimesAgrupador();
		  	document.getElementById("divTelaFiltro").style.visibility = "hidden";
		  	kernelEnable = true;
		  	document.getElementById("loadingKernelMap").style.visibility='visible';		
		  	aindaCarregando = true;
		  	podeCarregarCrimes=false;
		  	window.setTimeout(constroiMapaKernel,1);  	
	  }
  });
  
  GEvent.addDomListener(kernelDiv, "mouseover", function() {
	  mostraHintComandosMapa("tutor.ajuda.hots.spots", "138px", "268px");
  });
  
  GEvent.addDomListener(kernelDiv, "mouseout", function() {  		
	  removeHintComandosMapa();
  });
     
  map.getContainer().appendChild(container);
  return container;
}


ComandoKernel.prototype.getDefaultPosition = function() {
  return new GControlPosition(G_ANCHOR_TOP_LEFT, new GSize(170,40));
}


ComandoKernel.prototype.setButtonStyle_ = function(button) {
  //button.style.textDecoration = "underline";
 // button.style.color = "#0000cc";
  //button.style.backgroundColor = "white";
  //button.style.font = "small Arial";
  //button.style.border = "1px solid black";
  //button.style.padding = "2px";
  //button.style.marginBottom = "3px";
  //button.style.textAlign = "center";
  button.style.width = "7.42em";
  button.style.height = "2.68em";
  button.style.cursor = "pointer";
  button.style.backgroundImage = "url('./images/comandoKernel_"+localeWikiCrimes+".png')";
}

function ComandoKernelSelecionado() {
}
ComandoKernelSelecionado.prototype = new GControl();

ComandoKernelSelecionado.prototype.initialize = function(map) {
  var container = document.createElement("div");

  var kernelDiv = document.createElement("div");
  this.setButtonStyle_(kernelDiv);
  container.appendChild(kernelDiv);
  kernelDiv.appendChild(document.createTextNode(""));
  GEvent.addDomListener(kernelDiv, "click", function() {  		
		
		if(!aindaCarregando){
			mostraCrimesAgrupador();
			selecionarComando(map, 'mao');
			kernelEnable = false;
			apagaMapaKernel();
			mostraMarcadores();
			podeCarregarCrimes=true;
			//alert('disable');
		}  
  });
  GEvent.addDomListener(kernelDiv, "mouseover", function() {
	  mostraHintComandosMapa("tutor.ajuda.hots.spots", "138px", "268px");
  });
  
  GEvent.addDomListener(kernelDiv, "mouseout", function() {  		
	  removeHintComandosMapa();
  });    
  map.getContainer().appendChild(container);
  return container;
}


ComandoKernelSelecionado.prototype.getDefaultPosition = function() {
  return new GControlPosition(G_ANCHOR_TOP_LEFT, new GSize(170,40));
}

ComandoKernelSelecionado.prototype.setButtonStyle_ = function(button) {
	  //button.style.textDecoration = "underline";
	 // button.style.color = "#0000cc";
	  //button.style.backgroundColor = "white";
	  //button.style.font = "small Arial";
	  //button.style.border = "1px solid black";
	  //button.style.padding = "2px";
	  //button.style.marginBottom = "3px";
	  //button.style.textAlign = "center";
	  button.style.width = "7.42em";
	  button.style.height = "2.68em";
	  button.style.cursor = "pointer";
	  button.style.backgroundImage = "url('./images/comandoKernelSel_"+localeWikiCrimes+".png')";
}




