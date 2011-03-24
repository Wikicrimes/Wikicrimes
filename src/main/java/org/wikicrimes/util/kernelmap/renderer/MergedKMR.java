package org.wikicrimes.util.kernelmap.renderer;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.wikicrimes.util.kernelmap.KernelMap;

/**
 * Varia de transparente pra a cor passada como parametro
 */
public class MergedKMR extends KernelMapRenderer{

	private final KernelMapRenderer[] renderers;
	
	public MergedKMR(KernelMap kernel, KernelMapRenderer... renderers){
		super(kernel);
		this.renderers = renderers;
	}
	
	@Override
	public Image renderImage(){
		Image buffer = new BufferedImage(kernel.getWidth(), kernel.getHeight(), imageType);
		for(KernelMapRenderer renderer : renderers) {
			Image img = renderer.renderImage();
			Graphics g = buffer.getGraphics();
			g.drawImage(img, 0, 0, null);
		}
		return buffer;
	}
	
}
