function mostrarBalao(leftRight, top, tipoDiv){
	var divBalao = document.getElementById(tipoDiv);	
	divBalao.style.visibility = "visible";
	divBalao.style.top = top;
	if(tipoDiv == "divBalaoTutorEsquerda")
		divBalao.style.left = leftRight;
	else
		divBalao.style.right = leftRight;
}

function criaBalaoSetaCima(conteudoTexto, conteudoAcoesRelacionadas, tipoDiv){
	fecharBalao();
	var htmlBalao = '<div id="topoBalaoSetaCima"><a style="color: blue; text-decoration: none; background-color: transparent" onclick="fecharBalao();">X</a></div>'+	
			'<div align="justify" id="meioBalaoSetaCima">'+conteudoTexto+conteudoAcoesRelacionadas+'</div>'+
			'<div id="baseBalaoSetaCima"></div>';	
	document.getElementById(tipoDiv).innerHTML = htmlBalao;
}

function criaBalaoSetaBaixo(conteudoTexto, conteudoAcoesRelacionadas, tipoDiv){
	fecharBalao();
	var htmlBalao = '<div id="topoBalaoSetaBaixo"><a style="color: blue; text-decoration: none; background-color: transparent" onclick="fecharBalao();">X</a></div>'+	
			'<div align="justify" id="meioBalaoSetaBaixo">'+conteudoTexto+conteudoAcoesRelacionadas+'</div>'+
			'<div id="baseBalaoSetaBaixo"></div>';	
	document.getElementById(tipoDiv).innerHTML = htmlBalao;
}