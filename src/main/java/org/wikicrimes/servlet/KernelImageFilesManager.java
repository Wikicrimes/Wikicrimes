package org.wikicrimes.servlet;

import java.awt.Color;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.wikicrimes.util.kernelMap.KernelMap;
import org.wikicrimes.util.kernelMap.Suavizador;
import org.wikicrimes.util.kernelMap.renderer.CellBasedRenderer;
import org.wikicrimes.util.kernelMap.renderer.TransparentToColor;

/**
 * Criação e deleção de imagens relacionadas com Mapa de Kernel.
 * Obs: A implementação do Mapa de Kernel gera imagens com um endereço similar a 
 * "/images/KernelMap/[id-da-sessão]/". Esta classe gerencia a criação e deleção das imagens.
 * 
 * @author victor
 */
public class KernelImageFilesManager {

	private static final String PATH = "/images/KernelMap/"; 
	private static int num = 0; //numero q vai incrementando pra imagem, pra evitar cache
	
	
	/**
	 * Deleta o diretório e imagens (de Mapa de Kernel) relacionados a uma única sessão 
	 */
	public static void deletaImagem(HttpSession sessao){
		String imagePath = realImagePath(sessao);
		File dir = new File(imagePath);
		deletaArquivo(dir);
	}
	
	/**
	 * Deleta um diretório qualquer contendo imagens PNG.
	 */
	private static void deletaArquivo(final File dir){
		//obs: a deleção é agendade para alguns segundos mais tarde
		//pra corrigir um bug q acontecia ocasionalmente de a imagem ser deletada antes de ser exibida
		
		TimerTask task = 
			new TimerTask(){
			public void run() {
				
				File[] files = dir.listFiles();
				if(files != null){
					
					//encontra o arquivo com menor numero X "ImgX.png"
					File menorFile = null;
					int menor = Integer.MAX_VALUE;
					for(File file : files){
						String nome = file.getName();
						int num = Integer.valueOf( nome.substring(3, nome.length()-4) );
						if(num < menor){
							menor = num;
							menorFile = file;
						}
					}
					
					//deleta o arquivo (de menor numero)
					//if(menorFile.getName().endsWith(".png")) //segurança, pra n deletar outras coisas 
					menorFile.delete();
				}
				
				//deleta o diretório se tiver ficado vazio
				new Timer().schedule(deletaDir, 10000);
				
			}
			
			TimerTask deletaDir = 
				new TimerTask(){
				public void run() {
					//deleta só se tiver ficado vazio
					File[] files = dir.listFiles();
					if(files != null && files.length == 0)
						dir.delete();
				}
			};
		};
		
		new Timer().schedule(task, 10000);
	}

	
	public static String criarImagem(KernelMap kernel, HttpSession sessao){
		
		Suavizador kRend = new Suavizador(kernel);
		CellBasedRenderer scheme = new TransparentToColor(kernel, Color.RED);
		RenderedImage imagem = (RenderedImage)kRend.pintaKernel(scheme);
		
		String fileName = nomeImagem(sessao);
		String imagePath = realImagePath(sessao);
		try {
			new File(imagePath).mkdirs();
			ImageIO.write(imagem, "PNG", new File(imagePath, fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return relativeImagePath(sessao) + fileName;
		
	}
	
	private synchronized static String nomeImagem(HttpSession sessao){
		String nome = "Img" + num + ".png";
		num++;
		return nome;
	}
	
	private static String realImagePath(HttpSession sessao){
		String imagePath = sessao.getServletContext().getRealPath(relativeImagePath(sessao));
		return imagePath + "/";
	}
	
	private static String relativeImagePath(HttpSession sessao){
		return PATH + sessao.getId() + "/";
	}
	
}
