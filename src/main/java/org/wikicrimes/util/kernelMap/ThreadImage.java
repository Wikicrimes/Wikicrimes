package org.wikicrimes.util.kernelMap;

import java.io.File;

/**
 * 
 * @author Mairon
 *
 */

public class ThreadImage extends Thread{
	
	private String caminhoImagem;
	
	public ThreadImage(String caminhoImagem) {
		this.caminhoImagem = caminhoImagem;
	}
	
	// This method is called when the thread runs
    public void run() {
		try {
			//System.out.println("Apagando arquivo...");
			Thread.sleep(100);
			String path = new String(caminhoImagem);
			File file = new File(path);
			deleteDir(file);	
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
	public boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
    
        // The directory is now empty so delete it
        return dir.delete();
    }	

}
