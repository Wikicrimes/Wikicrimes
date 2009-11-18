package org.wikicrimes.servlet;

import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.wikicrimes.util.kernelMap.KernelMap;
import org.wikicrimes.util.kernelMap.KernelMapRenderer;

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
	public static void deletaImagens(HttpSession sessao){
		String imagePath = realImagePath(sessao);
		File dir = new File(imagePath);
		deleta(dir);
	}
	
	/**
	 * Deleta um diretório qualquer contendo imagens PNG.
	 */
	private static void deleta(final File dir){
		//obs: a deleção é agendade para alguns segundos mais tarde
		//pra corrigir um bug q acontecia ocasionalmente de a imagem ser deletada antes de ser exibida
		TimerTask task = 
			new TimerTask(){
			public void run() {
				
				File[] files = dir.listFiles();
				if(files != null)
				for(File file : files){
					if(file.getName().endsWith(".png")); //segurança, pra n deletar outras coisas 
					file.delete();
				}
				dir.delete();
				
			}
		};
		
		new Timer().schedule(task, 10000);
	}

	
	public static String criarImagem(KernelMap kernel, HttpSession sessao){
		
		KernelMapRenderer kRend = new KernelMapRenderer(kernel);
		RenderedImage imagem = (RenderedImage)kRend.pintaKernel();
		
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
