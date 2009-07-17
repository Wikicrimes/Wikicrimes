/* <![CDATA[ */
function formataData(Campo, teclapres)
{
	var tecla = teclapres.keyCode;
	var vr = new String(Campo.value);
	vr = vr.replace("/", "");
	vr = vr.replace("/", "");
	vr = vr.replace("/", "");
	tam = vr.length + 1;
	if (tecla != 8 && tecla != 8)
	{
		if (tam > 0 && tam < 2)
			Campo.value = vr.substr(0, 2) ;
		if (tam > 2 && tam < 4)
			Campo.value = vr.substr(0, 2) + '/' + vr.substr(2, 2);
		if (tam > 4 && tam < 7)
			Campo.value = vr.substr(0, 2) + '/' + vr.substr(2, 2) + '/' + vr.substr(4, 7);
	}
}

function validarData(campo) {
    var expReg = /^(([0-2]\d|[3][0-1])\/([0]\d|[1][0-2])\/[1-2][0-9]\d{2})$/;
    if ((campo.value.match(expReg)) && (campo.value!='')) {
    	return true;
    } else {
		return false;
	campo.focus();
    }
}
/* ]]> */