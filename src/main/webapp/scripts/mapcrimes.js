CmdUtils.CreateCommand({
  //versão dia 25/03
  name: ["wikicrimes","mapcrimes"],
 
  icon: "http://farm4.static.flickr.com/3203/2927530008_efb05d10db_o.jpg",
 
  description: "Create a Crime Ocurrency on WebSite WIkicrimes",
 
  homepage: "http://www.wikicrimes.org/",
 
  author: { name: "Doug", email: "dougpetcomp@yahoo.com.br"},
 
  license: "GPL",
 
  arguments: [{role: 'object', nountype: noun_arb_text,label:'text'}, {role: 'instrument', nountype: noun_type_searchengine,label:'text in'}],
 
  help:"Currently only works with <a href=\"http://www.wikicrimes.org\">WikiCrimes</a>," +
  "so you'll need a WikiCrimes account to use it. Try selecting part of a web page (what " +
  "contains characteristic that may be made good use of into the crime) and then issuing" +
  "&quot;mapcrimes this&quot;. For example, try issuing &quot;mapcrimes furto na Rua Santos Dumont 1000, Fortaleza.",
 
preview: function(pblock, args) {

  var html = "Creates a Crime Ocurrency ";
  if (args.instrument.text)
    //html += "in " + args.instrument.text + " ";
  if(args.object.html)
        html+="with these contents:" + args.object.html;
  pblock.innerHTML = html;
  },
 
  findTab: function (desiredURL) {
    var window = Application.activeWindow;
    var currentLocation = String(Application.activeWindow.activeTab.document.location);
    if(currentLocation.indexOf(desiredURL) != -1)
      return Application.activeWindow.activeTab;
   
    for (var i = 0; i < window.tabs.length; i++) {
      var tab = window.tabs[i];
      var location = String(tab.document.location);
      
      if (location.indexOf(desiredURL) != -1)
        return tab;         
    }
    return null;
  },
 
  tinyGeoCode: function( location )  {
     
    var remoteUrl = "http://tinygeocoder.com/create-api.php?q=" + location;
    
    var retVal;
    var ajaxOptions = {
      url: remoteUrl,
      type: "GET",
      datatype: "string",
      async: false
    };
   
    retVal = jQuery.ajax(ajaxOptions).responseText;
    return( retVal );
  },
 
  execute: function(args,headers) {
	var host ="www.wikicrimes.org";  
    var ip = "192.168.0.6";
    var cidade =  "fortaleza"; 
    var wikicrimesIETab = this.findTab("://"+host+"/wikicrimesIE.html");
    if (!wikicrimesIETab){
      Utils.openUrlInBrowser("http://"+host+"/wikicrimesIE.html");
      displayMessage("Execute o comando novamente.");
    }
    var text = args.object.text;  // Texto selecionado = "this"
    var document = context.focusedWindow.document;  // Pagina onde selecionei o texto
    var title; // Titulo pagina onde selecionei o texto
   
    if (document.title)
      title = document.title;
    else
      title = text;
									//www.wikicrimes.org/main.html							
    var location = document.location;  //Endereco da pÃ¡gina onde selecionei o texto
    var wikicrimesTab = this.findTab("://"+host+"/main.html");
    var pageLink = "Crime encontrado na pagina "  + location;
    var myObj = this;
   
   
    if (wikicrimesTab) { 
        jQuery.ajax({
        type: "get",
        url: "http://"+host+"/ServletUbiq",
        data: "acao=1",
        dataType: "text",
        error: function(msg) {
          displayMessage(msg + "Problem");
        },
        success: function(response) {
         
            if(response==1){
        jQuery.ajax({
        type: "get",
        url: "http://"+host+"/ServletUbiq",
        data: "acao=2&texto="+text,
        dataType: "text",
        error: function(msg) {
            displayMessage("Problema:123" + msg);
        },
        success: function(response) {
         
          var teste = response;
          var local = teste.split("@");
          var localResponse = local[0];
          var localCidade = local[1];
          var acusacao = local[2];
    
         if(localResponse==2){                                       
                var address =  localCidade;                   
                var geoCode = myObj.tinyGeoCode(address);                    
                var latLng = geoCode.split(",");
                var lat = latLng[0];
                var lng = latLng[1];
     
                wikicrimesIETab.focus();
                var win = CmdUtils.getWindowInsecure();
                win.fixarTarrachaPonto(lat, lng);
                win.procurarEndereco();
           
                
                 //Seta o campo de endereco do sia
                win.setarEnderecoSIA (address);
                win.setarTexto(text+" "+ pageLink);              
                if(acusacao==1)
                    win.setarCrime('1');
                else if(acusacao==2)
                    win.setarCrime('2');
                else if(acusacao==3)
                    win.setarCrime('3');
                else if(acusacao==4)  
                    win.setarCrime('4');
                else
                    win.setarCrime('5');  
                  //(tipoLocal, data, horario, quantidade, qtdMasculino, tipoArmaUsada, tipoPapel, tipoCrime, tipoRegistro, tipoVitima, textoUbiq)     
                 //win.setarCamposUbiq(0,"21/01/2009", -1, "", "", 0, 0 , acusacao, 0, 0, text+" "+ pageLink);
                 //setarCamposUb(data, horario,tipoArmaUsada, tipoRegistro,tipoVitima,tipoLocal)   
                 win.setarCamposUb('21/01/2009','12','2','4','1','1','3');
                        
                }
            
          }
        });
      
       }
        else  {
              //          usuario nao logado
              wikicrimesTab.focus();
              displayMessage("Por favor, faca o login no sistema Wikicrimes, e execute o comando novamente.",this);
            }       
  }
});              
    }   else {  // No wikicrimes tab open?  Open a new one:
      Utils.openUrlInBrowser("http://"+host+"/main.html");
      displayMessage("Por favor, faca o login no sistema Wikicrimes, e execute o comando novamente.");
    }
  }
});
