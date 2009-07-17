// TPhoto() GMaps API extension copyright 2005-2006 Tom Mangan
// http://gmaps.tommangan.us/tphoto.html
// free for non-commercial use
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