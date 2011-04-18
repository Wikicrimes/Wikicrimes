function openModal(url,largura,altura, titulo, a)
		{		
				jQuery(a).css('cursor','pointer');
				jQuery(a).removeAttr('href');
				var divID = 'modal';
				var larguraJanela = 0;
				var alturaJanela = 0;
				// Pegando a largura e altura da janela em multiplos browsers - ERRO DO IE
				  if( typeof( window.innerWidth ) == 'number' ) {
					//Nao-IE
					larguraJanela = window.innerWidth;
					alturaJanela = window.innerHeight;
				  } else if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
					//IE 6+ in 'standards compliant mode'
					larguraJanela = document.documentElement.clientWidth;
					alturaJanela = document.documentElement.clientHeight;
				  } else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
					//IE 4 compatible
					larguraJanela = document.body.clientWidth;
					alturaJanela = document.body.clientHeight;
				  }
				
				
				var posX = larguraJanela/2 - largura/2;
				var posY = alturaJanela/2 - altura/2;
				var posXpx = posX + "px"; 
				var posYpx = posY + "px";
				var posXbranco = posX + 12 + "px";
				var posYbranco = posY + 10 + "px";
				
				//Centralizando
				document.getElementById(divID).style.left = posXpx;
				document.getElementById(divID).style.top = posYpx;
				document.getElementById('branco').style.left = posXbranco;
				document.getElementById('branco').style.top = posYbranco;
			
				var largurapx = largura+"px";
				var alturapx = altura+"px";
				var larguraTopo = largura - 24;
				var larguraBottom = largura - 24;
				var larguraTopopx = larguraTopo + "px";
				var larguraBottompx = larguraBottom + "px";
				var alturaCorpo = altura;
				var alturaCorpopx = alturaCorpo + "px";
				var larguraTopoMeio = larguraTopo - 12;
				var larguraTopoMeiopx = larguraTopoMeio  + "px";
				var larguraBranco = larguraBottom + "px";
				var alturaBranco = alturaCorpo + 1 + "px";
				
				document.getElementById(divID).style.display = "block";
				document.getElementById('mascara').style.display = "block";
				
				
				document.getElementById('branco').style.display = "block";
				document.getElementById('branco').style.width = larguraBranco;
				document.getElementById('branco').style.height = alturaBranco;
				
				document.getElementById(divID).style.width = largurapx;
				document.getElementById(divID).style.height = alturapx;
				document.getElementById(divID).style.height = alturapx;
				document.getElementById('topo_meio').style.width = larguraTopopx;
				document.getElementById('bottom_meio').style.width = larguraTopopx;
				document.getElementById('corpo_meio').style.width = larguraTopopx;
				document.getElementById('corpo_esq').style.height = alturaCorpopx;
				document.getElementById('corpo_dir').style.height = alturaCorpopx;
				//document.getElementById('xfecha').style.marginLeft = larguraTopo;
				//document.body.style.overflow='hidden';
				
				jQuery('#modalConteudo').append("<img src='images/popUpIcon.png' style = 'float: left; float: left; margin-top:0px; margin-left: 5px; margin-right: 5px; margin-bottom: 0px;'/><h1 style = 'font-size: small; color: gray; padding-top: 5px;'>" + titulo + "</h1>");
				jQuery('#modalConteudo').append("<hr style='border: 0; border-top: 2px #A9A9A9 dotted; margin: 0px;'/>");
				jQuery('#modalConteudo').append('<iframe src=' + url + '  class="frame" width=' + (largura -33) + ' height=' + (altura - 50) + ' frameborder="0" scrolling="auto"></iframe>');
				
			}
			
			function closeModal(divID)
			{
			    document.getElementById(divID).style.display = "none";
				document.getElementById('mascara').style.display = "none";
				document.getElementById('branco').style.display = "none";
				//Limpar div de conteudo;
				jQuery('#modalConteudo').html('');
			}
			function openModalFiltro(largura,altura, titulo)
			{		
					
					var divID = 'modalFiltro';
					var larguraJanela = 0;
					var alturaJanela = 0;
					// Pegando a largura e altura da janela em multiplos browsers - ERRO DO IE
					  if( typeof( window.innerWidth ) == 'number' ) {
						//Nao-IE
						larguraJanela = window.innerWidth;
						alturaJanela = window.innerHeight;
					  } else if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
						//IE 6+ in 'standards compliant mode'
						larguraJanela = document.documentElement.clientWidth;
						alturaJanela = document.documentElement.clientHeight;
					  } else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
						//IE 4 compatible
						larguraJanela = document.body.clientWidth;
						alturaJanela = document.body.clientHeight;
					  }
					
					
					var posX = larguraJanela/2 - largura/2;
					var posY = alturaJanela/2 - altura/2;
					var posXpx = posX + "px"; 
					var posYpx = posY + "px";
					var posXbranco = posX + 12 + "px";
					var posYbranco = posY + 10 + "px";
					
					//Centralizando
					document.getElementById(divID).style.left = posXpx;
					document.getElementById(divID).style.top = posYpx;
					document.getElementById('brancoFiltro').style.left = posXbranco;
					document.getElementById('brancoFiltro').style.top = posYbranco;
				
					var largurapx = largura+"px";
					var alturapx = altura+"px";
					var larguraTopo = largura - 24;
					var larguraBottom = largura - 24;
					var larguraTopopx = larguraTopo + "px";
					var larguraBottompx = larguraBottom + "px";
					var alturaCorpo = altura - 5;
					var alturaCorpopx = alturaCorpo + "px";
					var larguraTopoMeio = larguraTopo - 12;
					var larguraTopoMeiopx = larguraTopoMeio  + "px";
					var larguraBranco = larguraBottom + "px";
					var alturaBranco = alturaCorpo + 1 + "px";
					
					document.getElementById(divID).style.display = "block";
					document.getElementById('mascaraFiltro').style.display = "block";
					document.getElementById('bottom_dirFiltro').style.display = "block";
					
					
					document.getElementById('brancoFiltro').style.display = "block";
					document.getElementById('brancoFiltro').style.width = larguraBranco;
					document.getElementById('brancoFiltro').style.height = alturaBranco;
					
					document.getElementById("divTelaFiltro").style.visibility = "visible";
					
					document.getElementById(divID).style.width = largurapx;
					document.getElementById(divID).style.height = alturapx;
					document.getElementById(divID).style.height = alturapx;
					document.getElementById('topo_meioFiltro').style.width = larguraTopopx;
					document.getElementById('bottom_meioFiltro').style.width = larguraTopopx;
					document.getElementById('corpo_meioFiltro').style.width = larguraTopopx;
					document.getElementById('corpo_esqFiltro').style.height = alturaCorpopx;
					document.getElementById('corpo_dirFiltro').style.height = alturaCorpopx;
					
					jQuery('#divTituloFiltro').append("<img src='images/popUpIcon.png' style = 'float: left; float: left; margin-top:0px; margin-left: 5px; margin-right: 5px; margin-bottom: 0px;'/><h1 style = 'font-size: small; color: gray; padding-top: 5px;'>" + titulo + "</h1>");
					jQuery('#divTituloFiltro').append("<hr style='border: 0; border-top: 2px #A9A9A9 dotted; margin: 0px;'/>");
					
				}
			function closeModalFiltro(divID)
			{
			    document.getElementById(divID).style.display = "none";
				document.getElementById('mascaraFiltro').style.display = "none";
				document.getElementById('brancoFiltro').style.display = "none";
				document.getElementById("divTelaFiltro").style.visibility = "hidden";
				jQuery('#divTituloFiltro').html('');
				selecionarComando(map,'mao');
			}
		