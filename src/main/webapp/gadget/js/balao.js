function mostrarBalao(left, top){
	var divBalao = document.getElementById("divBalaoTutor");	
	divBalao.style.visibility = "visible";
	divBalao.style.top = top;
	divBalao.style.left = left;
}
function fecharBalao(){
	document.getElementById("divBalaoTutor").style.visibility = "hidden";
}

function criaBalaoSetaCima(conteudoTexto, conteudoAcoesRelacionadas){
	var htmlBalao = '<div id="topoBalaoSetaCima"><a onclick="fecharBalao();">X</a></div>'+	
			'<div id="meioBalaoSetaCima">'+conteudoTexto+conteudoAcoesRelacionadas+'</div>'+
			'<div id="baseBalaoSetaCima"></div>';	
	document.getElementById("divBalaoTutor").innerHTML = htmlBalao;
}

function criaBalaoSetaBaixo(conteudoTexto, conteudoAcoesRelacionadas){
	var htmlBalao = '<div id="topoBalaoSetaBaixo"><a onclick="fecharBalao();">X</a></div>'+	
			'<div id="meioBalaoSetaBaixo">'+conteudoTexto+conteudoAcoesRelacionadas+'</div>'+
			'<div id="baseBalaoSetaBaixo"></div>';	
	document.getElementById("divBalaoTutor").innerHTML = htmlBalao;
}