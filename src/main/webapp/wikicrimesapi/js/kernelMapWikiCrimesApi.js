/* <![CDATA[ */

var photoWikiCrimes = null;
var flagHabilitaDesabilitaMapaDeKernelWikiCrimes = false;
function habilitaDesabilitaMapaDeKernelWikiCrimes(){
	var div = document.getElementById('botaoMapaKernel');	
	flagHabilitaDesabilitaMapaDeKernelWikiCrimes = !flagHabilitaDesabilitaMapaDeKernelWikiCrimes;
	if(flagHabilitaDesabilitaMapaDeKernelWikiCrimes){
		GVetorMarcadores = [];
		GClusterer.clearMarkers();
		podeCarregarCrimes = false;
		mapaDeKernelWikiCrimes();		
		div.innerHTML = "<div onclick='habilitaDesabilitaMapaDeKernelWikiCrimes();' class='botaoAtivado'> "+mensagens['titulo.kernel.map']+" </div>";	
							
		
	}else{
		if(photoWikiCrimes!=null)
			mapWikiCrimes.removeTPhoto(photoWikiCrimes);
		for (k in crimesAtuais) {
			GVetorMarcadores.push(crimesAtuais[k]);
		}
		podeCarregarCrimes = true;
		GClusterer = new MarkerClusterer(mapWikiCrimes, GVetorMarcadores);
		div.innerHTML = "<div onclick='habilitaDesabilitaMapaDeKernelWikiCrimes();' class='botao'> <font style='color:white'>"+mensagens['titulo.kernel.map']+"</font> </div>";	
	}
}

function mapaDeKernelWikiCrimes(){
	var b = mapWikiCrimes.getBounds();
	var north = b.getNorthEast().lat();
	var south = b.getSouthWest().lat();
	var east = b.getNorthEast().lng();
	var west = b.getSouthWest().lng();
	
	getTamMapClienteWikiCrimes();
	
	//Passa os limites para pixel
	var northPixel = mapWikiCrimes.getCurrentMapType().getProjection().fromLatLngToPixel(b.getNorthEast(), mapWikiCrimes.getZoom()).y;
	var southPixel = mapWikiCrimes.getCurrentMapType().getProjection().fromLatLngToPixel(b.getSouthWest(), mapWikiCrimes.getZoom()).y;
	var eastPixel = mapWikiCrimes.getCurrentMapType().getProjection().fromLatLngToPixel(b.getNorthEast(), mapWikiCrimes.getZoom()).x;
	var westPixel = mapWikiCrimes.getCurrentMapType().getProjection().fromLatLngToPixel(b.getSouthWest(), mapWikiCrimes.getZoom()).x;
	var idUsuarioMapaKernel = '';//retornaIdUsuario();  
	var emailUsuarioMapaKernel = '';//retornaEmailUsuario();
	var url = urlWikiCrimes + 'ServletWikiCrimesApi?acao=kernelMap&emailUsuarioMapaKernel=' + emailUsuarioMapaKernel + '&idUsuarioMapaKernel=' + idUsuarioMapaKernel + '&northPixel=' + northPixel + '&southPixel=' + southPixel + '&eastPixel=' + eastPixel + '&westPixel=' + westPixel + '&width='+ widthMapClienteWikiCrimes + '&height='+ heightMapClienteWikiCrimes +'&pontoXY=';
	var qtdCrimes = 0;
	for (k in crimesAtuais){
		qtdCrimes++;
	}
	var qtdCriEnv = 200;
	var cont = 0;
	for (k in crimesAtuais){
		cont++;
		var point = crimesAtuais[k].getPoint();
		var pixel = mapWikiCrimes.getCurrentMapType().getProjection().fromLatLngToPixel(point, mapWikiCrimes.getZoom());
		url+= pixel.y + "," + pixel.x + "a";
		if(cont % qtdCriEnv == 0 && qtdCrimes>qtdCriEnv){
			if(cont == qtdCriEnv){
				url+='&statusReq=Pri';
			}else{
				url+='&statusReq=SegOuMais';
			}
			url+='&jsoncallback=?';
			//document.write(url);
			executaRequisicaoKernelMapWikiCrimes(url);
			url = urlWikiCrimes + 'ServletWikiCrimesApi?acao=kernelMap&pontoXY=';
		}
	}
	
	url = url.substring(0,url.length-1)
	if(qtdCrimes<=qtdCriEnv)
		url+='&statusReq=PriUlt';
	else
		url+='&statusReq=Ult';
	url+='&jsoncallback=?';
	//document.write(url);
	executaRequisicaoKernelMapWikiCrimes(url);

}

function executaRequisicaoKernelMapWikiCrimes(url){
	variavelGlobalJQuery.getJSON(url, false, function(data){
		if(data.numRandomico!=null && data.numRandomico!= undefined){
			var numRandomico = data.numRandomico;
			var idImage = data.idImage;
			
			//Converte pixel para latlng. Lembrete: GPoint(lat, lng)
			var sw1 = mapWikiCrimes.getCurrentMapType().getProjection().fromPixelToLatLng(new GPoint(data.topLeftX,data.topLeftY), mapWikiCrimes.getZoom(), true);
			var sw2 = mapWikiCrimes.getCurrentMapType().getProjection().fromPixelToLatLng(new GPoint(data.bottomRightX,data.bottomRightY), mapWikiCrimes.getZoom(), true);
			
			//Recupara os latlong da resposta da requisicao						
	
		
			var lon1 = sw1.lng();
			var lat1 = sw1.lat();
			
			//Recupara os latlong da resposta da requisicao						
	
		
			var lon2 = sw2.lng();
			var lat2 = sw2.lat();
			
			photoWikiCrimes = new TPhoto();
			photoWikiCrimes.id = 'addphoto';
			//photo.id = id;
			
			photoWikiCrimes.src = urlWikiCrimes + 'images/KernelMap/'+ numRandomico + '/Img' + idImage + '.png';
			
			//photo.src = './images/KernelMap/' + idUsuarioMapaKernel +'@'+ emailUsuarioMapaKernel + '/Img' + idImage + '.png';
			//photo.src = '${contextPath}/Img2.png';			
			//photo.src = '${pageContext.request.contextPath}/Img2.png';
			//photo.src = 'images/TesteM.png';
			photoWikiCrimes.percentOpacity = 50;
			photoWikiCrimes.anchorTopLeft = new GLatLng(lat1,lon1);
			photoWikiCrimes.anchorBottomRight = new GLatLng(lat2,lon2);
	
			mapWikiCrimes.addTPhoto(photoWikiCrimes);
	  		
	  		//document.getElementById("loadingKernelMap").style.visibility='hidden';
	  		
			//Manda confirma��o para apagar a imagem no servidor
			url = urlWikiCrimes + 'ServletWikiCrimesApi?acao=kernelMap&imagem=' + numRandomico;
			url+='&jsoncallback=?';
			executaRequisicaoKernelMapWikiCrimes(url);
		}else{
			//alert('remove');
		}
	});
}



function TPhoto(){}

TPhoto.prototype.initialize=function(a){
	//alert('initialize');
	this.parentMap=a;
	var b=document.createElement('img');
	b.style.display='none';
	b.setAttribute('id',this.id);
	b.setAttribute('src',this.src);
	b.style.position='absolute';
	b.style.zIndex=1;
	this.mapTray=a.getPane(G_MAP_MAP_PANE);
	this.mapTray.appendChild(b);
	this.setPosition(a);
	b.style.display='block';
	if(this.percentOpacity){this.setOpacity(this.percentOpacity);}
	GEvent.bind(a,"zoomend",this,function(){this.setPosition(a)});
	GEvent.bind(a,"moveend",this,function(){this.setPosition(a)});
}

TPhoto.prototype.setPosition=function(a){
	//alert('setPosition');
	var d=this.parentMap.fromLatLngToDivPixel(this.anchorTopLeft);
	var e=this.parentMap.fromLatLngToDivPixel(this.anchorBottomRight);
	var x=document.getElementById(this.id);
	
	//alert('x: ' + x);
	if(x != null){
		x.style.top=d.y+'px';
		x.style.left=d.x+'px';
		x.style.width=e.x-d.x+'px';
		x.style.height=e.y-d.y+'px';
	}
	
}

TPhoto.prototype.setOpacity=function(b){
	//alert('setOpacity');
	if(b<0){b=0;}  if(b>=100){b=100;}
	var c=b/100;
	var d=document.getElementById(this.id);
	if(typeof(d.style.filter)=='string'){d.style.filter='alpha(opacity:'+b+')';}
	if(typeof(d.style.KHTMLOpacity)=='string'){d.style.KHTMLOpacity=c;}
	if(typeof(d.style.MozOpacity)=='string'){d.style.MozOpacity=c;}
	if(typeof(d.style.opacity)=='string'){d.style.opacity=c;}
}

GMap2.prototype.addTPhoto=function(a){
	//alert('addTPhoto');
	a.initialize(this);
}

GMap2.prototype.removeTPhoto=function(a){
	//alert('removeTPhoto');
	try{
	var b=document.getElementById(a.id);
		if(b != null){
			this.getPane(G_MAP_MAP_PANE).removeChild(b);
			delete(b);
		}
	}catch(e){}
	
}

/* ]]> */