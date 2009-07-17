/*
* PanoMapTypeControl Class 
*  Copyright (c) 2007, Google 
*  Author: Pamela Fox, others
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*       http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
* This class lets you add a control to the map which mimics GMapTypeControl
*  and allows for the addition of a traffic button/traffic key.
*/

/*
 * Constructor for PanoMapTypeControl
 */
function KernelMapTypeControl(opt_opts) {
  this.options = opt_opts || {};
}


KernelMapTypeControl.prototype = new GControl();

/**
 * Is called by GMap2's addOverlay method. Creates the button 
 *  and appends to the map div.
 * @param {GMap2} map The map that has had this PanoMapTypeControl added to it.
 * @return {DOM Object} Div that holds the control
 */ 
KernelMapTypeControl.prototype.initialize = function(map) {
  var container = document.createElement("div");
  me = this;

  kernelDiv = me.createButton_(kernelLabel);
  kernelDiv.style.marginRight = "8px";
  GEvent.addDomListener(kernelDiv, "click", function() {
	  
    if (me.kernelLayer) {
      	
      if (me.kernelLayer.getEnabled()) {
        me.kernelLayer.disable();
        mostraCrimesAgrupador();
        
      } else {
    	limpaCrimesAgrupador();
        me.kernelLayer.enable();
        
      }
    } else {
      limpaCrimesAgrupador();	
      me.kernelLayer = new KernelLayer(map);
      me.kernelLayer.enable();
    }
    me.toggleButton_(kernelDiv.firstChild, me.kernelLayer.getEnabled());
  });
  me.toggleButton_(kernelDiv.firstChild, false);
  me.assignButtonEvent_(map);
  
 
  container.appendChild(kernelDiv);
 
  //container.appendChild(earDiv);

  map.getContainer().appendChild(container);

  GEvent.trigger(map, "maptypechanged");
  return container;
}

/*
 * Creates simple buttons with text nodes. 
 * @param {String} text Text to display in button
 * @return {DOM Object} The div for the button.
 */
KernelMapTypeControl.prototype.createButton_ = function(text) {
  var buttonDiv = document.createElement("div");
  this.setButtonStyle_(buttonDiv);
  buttonDiv.style.cssFloat = "left";
  buttonDiv.style.styleFloat = "left";
  var textDiv = document.createElement("div");
  textDiv.appendChild(document.createTextNode(text));
  textDiv.style.width = "8em";
  buttonDiv.appendChild(textDiv);
  return buttonDiv;
}

/*
 * Assigns events to MapType buttons to change maptype
 *  and toggle button styles correctly for all buttons
 *  when button is clicked.
 *  @param {DOM Object} div Button's div to assign click to
 *  @param {GMap2} Map object to change maptype of.
 *  @param {Object} mapType GMapType to change map to when clicked
 *  @param {Array} otherDivs Array of other button divs to toggle off
 */  
KernelMapTypeControl.prototype.assignButtonEvent_ = function(map) {
  var me = this;

 
}

/*
 * Changes style of button to appear on/off depending on boolean passed in.
 * @param {DOM Object} div  Button div to change style of
 * @param {Boolean} boolCheck Used to decide to use on style or off style
 */
KernelMapTypeControl.prototype.toggleButton_ = function(div, boolCheck) {
   div.style.fontWeight = boolCheck ? "bold" : "";
   div.style.border = "1px solid white";
   var shadows = boolCheck ? ["Top", "Left"] : ["Bottom", "Right"];
   for (var j = 0; j < shadows.length; j++) {
     div.style["border" + shadows[j]] = "1px solid #b0b0b0";
  } 
}

/*
 * Required by GMaps API for controls. 
 * @return {GControlPosition} Default location for control
 */
KernelMapTypeControl.prototype.getDefaultPosition = function() {
  return new GControlPosition(G_ANCHOR_TOP_RIGHT, new GSize(273, 7));
}

/*
 * Sets the proper CSS for the given button element.
 * @param {DOM Object} button Button div to set style for
 */
KernelMapTypeControl.prototype.setButtonStyle_ = function(button) {
  button.style.color = "#000000";
  button.style.backgroundColor = "white";
  button.style.font = "small Arial";
  button.style.border = "1px solid black";
  button.style.padding = "0px";
  button.style.margin= "0px";
  button.style.textAlign = "center";
  button.style.fontSize = "12px"; 
  button.style.cursor = "pointer";
}