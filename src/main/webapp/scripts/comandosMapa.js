var estaRegistrandoArea = false;
//Comando dentro do mapa para mover
function ComandoMao() {
}
ComandoMao.prototype = new GControl();

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
		//document.getElementById("divExplicaMarcarArea").style.visibility = "hidden"; 
  });
  
  GEvent.addDomListener(maoDiv, "mouseover", function() {
  		mostraHintComandosMapa("botoes.title.mover", "138px", "72px");
  });
  
  GEvent.addDomListener(maoDiv, "mouseout", function() {  		
  		removeHintComandosMapa();
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
  button.style.width = "2.52em";
  button.style.height = "2.52em";
  button.style.cursor = "pointer";
  button.style.backgroundImage = "url('./images/comandoMao.png')";
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
  	//if(!estaRegistrandoArea){
  		
	  	//selecionarComando(map, 'area');	   
	    registrarArea1();
	//}    
    //var label = new ELabel(polyListArea.getVertex(0), '<div style="background-color:#ffffff;border:1px solid blue;color:blue;width:120px;height:14px"> Marque os pontos de sua area no mapa </div>', null, new GSize(6,-10), 75);
  });
  
  GEvent.addDomListener(marcarAreaDiv, "mouseover", function() {
  		mostraHintComandosMapa("webapp.area.erro.info.hint.area", "138px", "100px");
  });
  
  GEvent.addDomListener(marcarAreaDiv, "mouseout", function() {  		
  		removeHintComandosMapa();
  });    

  map.getContainer().appendChild(container);
  return container;
}


ComandoMarcadorDeArea.prototype.getDefaultPosition = function() {
  return new GControlPosition(G_ANCHOR_TOP_LEFT, new GSize(112,7));
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
  button.style.width = "2.52em";
  button.style.height = "2.52em";
  button.style.cursor = "pointer";
  button.style.backgroundImage = "url('./images/comandoArea.png')";
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
  		mostraHintComandosMapa("botoes.title.mover", "138px", "72px");
  });
  
  GEvent.addDomListener(maoDiv, "mouseout", function() {  		
  		removeHintComandosMapa();
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
  button.style.width = "2.52em";
  button.style.height = "2.52em";
  button.style.cursor = "pointer";
  button.style.backgroundImage = "url('./images/comandoMaoSel.png')";
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
  		mostraHintComandosMapa("webapp.area.erro.info.hint.area", "138px", "100px");
  });
  
  GEvent.addDomListener(marcarAreaDiv, "mouseout", function() {  		
  		removeHintComandosMapa();
  });    

  map.getContainer().appendChild(container);
  return container;
}


ComandoMarcadorDeAreaSelecionado.prototype.getDefaultPosition = function() {
  return new GControlPosition(G_ANCHOR_TOP_LEFT, new GSize(112,7));
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
  button.style.width = "2.52em";
  button.style.height = "2.52em";
  button.style.cursor = "pointer";
  button.style.backgroundImage = "url('./images/comandoAreaSel.png')";
}

//Comando dentro do mapa para gerarEmbed
function ComandoEmbedSelecionado() {
}
ComandoEmbedSelecionado.prototype = new GControl();

ComandoEmbedSelecionado.prototype.initialize = function(map) {
  var container = document.createElement("div");

  var embedDiv = document.createElement("div");
  this.setButtonStyle_(embedDiv);
  container.appendChild(embedDiv);
  embedDiv.appendChild(document.createTextNode(""));
  GEvent.addDomListener(embedDiv, "click", function() {
	  removeHintComandosMapa();
	  gerarEmbedded();
    selecionarComando(map, 'mao');
  });
  GEvent.addDomListener(embedDiv, "mouseover", function() {
  		mostraHintComandosMapa("embedded.hint.comando.mapa", "138px", "134px");
  });
  
  GEvent.addDomListener(embedDiv, "mouseout", function() {  		
  		removeHintComandosMapa();
  });    

  map.getContainer().appendChild(container);
  return container;
}


ComandoEmbedSelecionado.prototype.getDefaultPosition = function() {
  return new GControlPosition(G_ANCHOR_TOP_LEFT, new GSize(144,7));
}


ComandoEmbedSelecionado.prototype.setButtonStyle_ = function(button) {
  //button.style.textDecoration = "underline";
 // button.style.color = "#0000cc";
  //button.style.backgroundColor = "white";
  //button.style.font = "small Arial";
  //button.style.border = "1px solid black";
  //button.style.padding = "2px";
  //button.style.marginBottom = "3px";
  //button.style.textAlign = "center";
  button.style.width = "2.52em";
  button.style.height = "2.52em";
  button.style.cursor = "pointer";
  button.style.backgroundImage = "url('./images/comandoEmbedSel.png')";
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
  embedDiv.appendChild(document.createTextNode(""));
  GEvent.addDomListener(embedDiv, "click", function() {
	 removeHintComandosMapa();
	 selecionarComando(map, 'embed');
     gerarEmbedded(); 
     selecionarComando(map, 'mao');
  });
  GEvent.addDomListener(embedDiv, "mouseover", function() {
  		mostraHintComandosMapa("embedded.hint.comando.mapa", "138px", "134px");
  });
  
  GEvent.addDomListener(embedDiv, "mouseout", function() {  		
  		removeHintComandosMapa();
  });    

  map.getContainer().appendChild(container);
  return container;
}


ComandoEmbed.prototype.getDefaultPosition = function() {
  return new GControlPosition(G_ANCHOR_TOP_LEFT, new GSize(144,7));
}


ComandoEmbed.prototype.setButtonStyle_ = function(button) {
  //button.style.textDecoration = "underline";
 // button.style.color = "#0000cc";
  //button.style.backgroundColor = "white";
  //button.style.font = "small Arial";
  //button.style.border = "1px solid black";
  //button.style.padding = "2px";
  //button.style.marginBottom = "3px";
  //button.style.textAlign = "center";
  button.style.width = "2.52em";
  button.style.height = "2.52em";
  button.style.cursor = "pointer";
  button.style.backgroundImage = "url('./images/comandoEmbed.png')";
}




