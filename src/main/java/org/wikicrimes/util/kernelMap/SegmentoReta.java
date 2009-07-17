package org.wikicrimes.util.kernelMap;

/**
 * 
 * @author Mairon
 *
 */

public class SegmentoReta {
	private int inicioRotaPixelX;
	private int inicioRotaPixelY;
	private int fimRotaPixelX;
	private int fimRotaPixelY;
	
	private String [] CodigoInicio = new String[2];
	private String [] CodigoFim = new String[2];
	
	public SegmentoReta(int inicioRotaPixelX, int inicioRotaPixelY,
			int fimRotaPixelX, int fimRotaPixelY) {
		this.inicioRotaPixelX = inicioRotaPixelX;
		this.inicioRotaPixelY = inicioRotaPixelY;
		this.fimRotaPixelX = fimRotaPixelX;
		this.fimRotaPixelY = fimRotaPixelY;
	}
	
	public void codificaPontosExtremos(int bordaCima, int bordaBaixo, int bordaEsq, int bordaDir){
		
		//Zera os cófigos
		CodigoInicio[0] = "";
		CodigoInicio[1] = "";
		CodigoFim[0] = "";
		CodigoFim[1] = "";
		
		//Estremidade Inicio
		if (inicioRotaPixelX > bordaCima){
			CodigoInicio[0] = Util.CIMA;
		}else if (inicioRotaPixelX < bordaBaixo){
			CodigoInicio[0] = Util.BAIXO;
		}
		if (inicioRotaPixelY < bordaEsq){
			CodigoInicio[1] = Util.ESQ;
		}else if(inicioRotaPixelY > bordaDir){
			CodigoInicio[1] = Util.DIR;
		}
		
		//Estremidade Fim
		if (fimRotaPixelX > bordaCima){
			CodigoFim[0] = Util.CIMA;
		}else if (fimRotaPixelX < bordaBaixo){
			CodigoFim[0] = Util.BAIXO;
		}
		if (fimRotaPixelY < bordaEsq){
			CodigoFim[1] = Util.ESQ;
		}else if(fimRotaPixelY > bordaDir){
			CodigoFim[1] = Util.DIR;
		}
			
		/*Codificar(P)
		se P está acima
		então Cp = {acima}
		senão se P está abaixo
		então Cp = {abaixo}
		
		se P está a esquerda
		então Cp = Cp UNIAO {esq}
		senão se P está a direita
		então Cp = Cp UNIAO {dir}*/
	}
	
	//Verifica caso de o pedaço de rota estar totalmente fora da janela
	public boolean isCasoTrivial(){
		if(CodigoInicio[0].equalsIgnoreCase(CodigoFim[0])){
			return true;
		}
		if(CodigoInicio[1].equalsIgnoreCase(CodigoFim[1])){
			return true;
		}
		return false;
	}
	
	public int getInicioRotaPixelX() {
		return inicioRotaPixelX;
	}
	public void setInicioRotaPixelX(int inicioRotaPixelX) {
		this.inicioRotaPixelX = inicioRotaPixelX;
	}
	public int getInicioRotaPixelY() {
		return inicioRotaPixelY;
	}
	public void setInicioRotaPixelY(int inicioRotaPixelY) {
		this.inicioRotaPixelY = inicioRotaPixelY;
	}
	public int getFimRotaPixelX() {
		return fimRotaPixelX;
	}
	public void setFimRotaPixelX(int fimRotaPixelX) {
		this.fimRotaPixelX = fimRotaPixelX;
	}
	public int getFimRotaPixelY() {
		return fimRotaPixelY;
	}
	public void setFimRotaPixelY(int fimRotaPixelY) {
		this.fimRotaPixelY = fimRotaPixelY;
	}	
}
