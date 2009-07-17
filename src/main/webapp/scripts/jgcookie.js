var JGCookie = {
	  date: new Date(),
	  set: function(name, content, days) {
	    var expires = "";
	    if(days) {
		  this.date.setTime(this.date.getTime()+(days*24*60*60*1000));
	      expires = this.date.toGMTString() + "; ";
		}
		document.cookie = name + "=" + content + "; " + expires + "path=/";
		return true;
	  },
	  get: function( name ) {
	  	var nameE = name + "=";
		var cookies = document.cookie.split(";");
		for(var i = 0, Cookie; Cookie = cookies[i]; i++) {
		  while(Cookie.charAt(0) == " ") {
		  	Cookie = Cookie.substring(1,Cookie.length);
		  }
		  if(Cookie.indexOf(nameE) == 0) {
		  	return Cookie.substring(nameE.length,Cookie.length);
		  }
		}
		return false;
	  },
	  unset: function( name ) {
	  	this.set(name, "", -1);
	  	return true;
	  }
}