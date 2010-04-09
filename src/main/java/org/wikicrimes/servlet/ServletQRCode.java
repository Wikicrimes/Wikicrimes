package org.wikicrimes.servlet;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;


/**
 * Gera um QRCode através da API do GoogleChart
 * Comunica-se com a Google Charts(http://code.google.com/apis/charttools/).
 * @author André
 */

public class ServletQRCode extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// Texto contido no QRCode
		String string = request.getParameter("string"); 
		
		// Se a imagem deve ser ampliada
		double tamanho;
		try {
			String tamanhoString = request.getParameter("tamanho");
			if(tamanhoString != null && tamanhoString.length() > 0)
				tamanho = Double.parseDouble(tamanhoString);
			else
				tamanho = 1;
		} catch (Exception e) {
			tamanho = 1; // Caso nenhum numero válido seja passado
		}
		
		
		String url = constroiUrl(string);
		
		URL urlImagem = new URL(url);
		Image imagemMapa = requisitarImagem(urlImagem, tamanho);
		
		if(imagemMapa == null) {
			String acceptLanguage = request.getHeader("Accept-Language");
			PrintWriter out = response.getWriter();
			out.println("<html><head></head><body>");
			if(acceptLanguage.equalsIgnoreCase("pt-br")) {
				out.println("Erro ao tentar gerar a imagem. Por favor, tente novamente em alguns segundos.");
				out.println("<a href='javascript:history.go(0)'>Tentar novamente.</a>");
			} else {
				out.println("Error generating the QR Code. Please, try again in a few seconds.");
				out.println("<a href='javascript:history.go(0)'>Try again.</a>");
			}
			out.println("</body></html>");
		} else {
			response.setContentType("image/png");
			OutputStream out = response.getOutputStream();
			ImageIO.write(toBufferedImage(imagemMapa), "PNG", out);
		}
	}
	
	/**
	 * @param centro - Ponto central do mapa
	 * @return URL preparada para requisitar imagem do Google Chart
	 */
	private String constroiUrl(String string){
		
		String urlMapaLimpo = 
			"http://chart.apis.google.com/chart?" +
			"cht=qr&" +				// Tipo de Char gerado(QRCode)
			"chl="+string+"&" + 	// Texto contido no QRCode
			"choe=UTF-8&" + 		// Formato do texto
			"chs="+ 547 +"x"+ 547 +"&" +		// Tamanho da imagem
			"chld=L|1"; 			// Nivel de redundancia dos dados | Moldura em branco
		
		return urlMapaLimpo;
	}
	
	
	
	/**
	 * Método que retorna uma imagem recebida pelo Google Charts.
	 * @param url - URL de request para o Google Charts
	 * @param tamanho - Fator de escala da imagem(para aumentar ou diminuir a imagem)
	 * @return Imagem com o recebida
	 */
	private Image requisitarImagem(URL url, double tamanho){
		BufferedImage imagem = null;
		HttpURLConnection con = null;
		try {
		    //Acessa a url
		    con = (HttpURLConnection)url.openConnection();
		    con.connect();
		    
		    //Recebe a resposta
		    ImageInputStream input = new MemoryCacheImageInputStream(con.getInputStream()); 
		    imagem = ImageIO.read(input);

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			con.disconnect();
		}
		return imagem.getScaledInstance(
				(int)(imagem.getWidth() * tamanho), 
				(int)(imagem.getHeight() * tamanho), 
				Image.SCALE_DEFAULT);
	}
	
	public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {return (BufferedImage)image;}
    
        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();
    
        // Determine if the image has transparent pixels
        boolean hasAlpha = hasAlpha(image);
    
        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha == true) {transparency = Transparency.BITMASK;}
    
            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
        } 
        catch (HeadlessException e) {
        	
        } //No screen
    
        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha == true) {type = BufferedImage.TYPE_INT_ARGB;}
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }
    
        // Copy image to buffered image
        Graphics g = bimage.createGraphics();
    
        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();
    
        return bimage;
    }

      public static boolean hasAlpha(Image image) {
             // If buffered image, the color model is readily available
             if (image instanceof BufferedImage) {return ((BufferedImage)image).getColorModel().hasAlpha();}
         
             // Use a pixel grabber to retrieve the image's color model;
             // grabbing a single pixel is usually sufficient
             PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
             try {pg.grabPixels();} catch (InterruptedException e) {}
         
             // Get the image's color model
             return pg.getColorModel().hasAlpha();
         }
}
	