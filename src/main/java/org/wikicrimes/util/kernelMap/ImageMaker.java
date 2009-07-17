package org.wikicrimes.util.kernelMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 
 * @author Mairon
 *
 */

public class ImageMaker {

	public Graphics2D graphics2D;
	private BufferedImage img = null;
	
	public BufferedImage createImage(Integer width, Integer height) {
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		img.createGraphics();
		graphics2D = (Graphics2D) img.getGraphics();
		
		/*g.setColor(Color.YELLOW);
		g.fillRect(0, 0, 100, 100);

		for (int i = 1; i < 49; i++) {
			g.setColor(new Color(5 * i, 5 * i, 4 + 1 * 2 + i * 3));
			g.fillRect(2 * i, 2 * i, 3 * i, 3 * 1);
		}*/		
		
		
//		g = painelKernel.getGraphics().create();
//		frame.repaint();
//		g = frame.getGraphics();
		
//		saveImage(img, path + "\\TesteM.jpg");
		
		return img;
	}

	/**
	 * Saves a BufferedImage to the given file, pathname must not have any
	 * periods "." in it except for the one before the format, i.e.
	 * C:/images/fooimage.png
	 * 
	 * @param img
	 * @param saveFile
	 */
	public void saveImage(String ref) {
		try {
			String format = (ref.endsWith(".png")) ? "png" : "jpg";
			ImageIO.write(img, format, new File(ref));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	public static void main(String[] args) {
		JFrame frame = new JFrame("Image Maker");
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				System.exit(0);
			}
		});
		
		frame.setBounds(0, 0, 200, 200);
		JImagePanel panel = new JImagePanel(createImage(), 50, 45);
		frame.add(panel);
		frame.setVisible(true);
	}*/

}


//Interface
/*
class JImagePanel extends JPanel {
	private BufferedImage image;
	int x, y;

	public JImagePanel(BufferedImage image, int x, int y) {
		super();
		this.image = image;
		this.x = x;
		this.y = y;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, x, y, null);
	}
}

*/
