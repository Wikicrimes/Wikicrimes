function ComandoAlerta() {
}
//ComandoAlerta.prototype = new GControl();

ComandoAlerta.prototype.initialize = function(map) {
  var container = document.createElement("div");

  var alertaDiv = document.createElement("div");
  this.setButtonStyle_(alertaDiv);
  container.appendChild(alertaDiv);
  alertaDiv.appendChild(document.createTextNode(""));
  GEvent.addDomListener(alertaDiv, "click", function() {
  	
  });
  
  GEvent.addDomListener(alertaDiv, "mouseover", function() {
  		
  });
  
  GEvent.addDomListener(alertaDiv, "mouseout", function() {  		
  		
  });
     
  map.getContainer().appendChild(container);
  return container;
}


ComandoAlerta.prototype.getDefaultPosition = function() {
  return new GControlPosition(G_ANCHOR_TOP_LEFT, new GSize(80,7));
}


ComandoAlerta.prototype.setButtonStyle_ = function(button) {
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
  button.style.backgroundImage = "url('./images/btns/pt/btnAlerta.PNG')";
}
