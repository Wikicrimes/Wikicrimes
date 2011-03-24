package org.wikicrimes.util;

import java.awt.image.RenderedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DebugUtil {

	private static String dir = "/home/victor/Desktop/testes/rotas 2010.09.17/novo teste";

	public static void stringToFile(String fileNames, String str) {
		try {
			File file = new File(dir, fileNames);
			file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);
			DataOutputStream dout = new DataOutputStream(out);
			dout.writeUTF(str);
			dout.flush();
			dout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setDir(String dir) {
		DebugUtil.dir = dir;
	}
	
	public static void saveImage(String fileName, RenderedImage imagem){
		try {
			ImageIO.write(imagem, "PNG", new File(dir, fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
