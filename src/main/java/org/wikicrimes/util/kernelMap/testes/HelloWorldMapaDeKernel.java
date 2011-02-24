package org.wikicrimes.util.kernelMap.testes;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.wikicrimes.util.kernelMap.KernelMap;
import org.wikicrimes.util.kernelMap.renderer.KernelMapRenderer;
import org.wikicrimes.util.kernelMap.renderer.TransparentToColor;

public class HelloWorldMapaDeKernel {

	public static void main(String[] args) throws IOException {
		
		//criar bounds
		Rectangle bounds = new Rectangle(0,0,50,50);
		
		//criar lista de pontos
		List<Point> pontos = new ArrayList<Point>();
		Point p = new Point(0,1);
		pontos.add(p);
		p = new Point(1,1);
		pontos.add(p);
		p = new Point(1,1);
		pontos.add(p);	
		
		//criar objeto KernelMap (so tem a grid de doubles)
		KernelMap km = new KernelMap(bounds,pontos);
		
		//gerar a imagem
		KernelMapRenderer kmr = new TransparentToColor(km,Color.RED);
		RenderedImage image = (RenderedImage) kmr.renderImage();
		
		//salvar imagem em arquivo
		ImageIO.write(image, "png", new File("/home/victor/teste.png"));
		
	}
	
}
