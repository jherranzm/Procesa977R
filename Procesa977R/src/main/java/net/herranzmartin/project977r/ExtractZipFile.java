package net.herranzmartin.project977r;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

public class ExtractZipFile {

	final private static Logger logger = Logger.getLogger(ExtractZipFile.class);
	
	private List<String> listaFicherosDescomprimidos = new ArrayList<String>();

	/**
	 * @param fName
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void extraeFichero(String fName) {
		byte[] buf = new byte[1024];
		try {
			ZipInputStream zinstream = new ZipInputStream( new FileInputStream(fName));
			ZipEntry zentry = zinstream.getNextEntry();
			logger.info("Name of current Zip Entry : " + zentry + "\n");
			while (zentry != null) {
				String entryName = zentry.getName();
				logger.info("Name of  Zip Entry : " + entryName);
				
				listaFicherosDescomprimidos.add(entryName);
				
				FileOutputStream outstream = new FileOutputStream(entryName);
				int n;

				while ((n = zinstream.read(buf, 0, 1024)) > -1) {
					outstream.write(buf, 0, n);

				}
				logger.info("Successfully Extracted File Name : " + entryName);
				outstream.close();

				zinstream.closeEntry();
				zentry = zinstream.getNextEntry();
			}
			zinstream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	// Getters & Setters
	

	/**
	 * @return the listaFicherosDescomprimidos
	 */
	public List<String> getListaFicherosDescomprimidos() {
		return listaFicherosDescomprimidos;
	}

	/**
	 * @param listaFicherosDescomprimidos the listaFicherosDescomprimidos to set
	 */
	public void setListaFicherosDescomprimidos(
			List<String> listaFicherosDescomprimidos) {
		this.listaFicherosDescomprimidos = listaFicherosDescomprimidos;
	}


}
