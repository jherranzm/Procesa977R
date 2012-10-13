package net.herranzmartin.project977r.test;

import static org.junit.Assert.*;

import net.herranzmartin.project977r.ExtractZipFile;
import net.herranzmartin.project977r.Procesa977R;

import org.apache.log4j.Logger;
import org.junit.Test;

public class ExtractFileTest {

	final private static Logger logger = Logger.getLogger(ExtractFileTest.class); 

	@Test
	public void test() {
		String[] ficheros = {
//				"2012-01.zip", 
//				"2012-02.zip",
//				"2012-03.zip",
//				"2012-04.zip",
//				"2012-05.zip",
				"2012-06.zip"
				};
		
		ExtractZipFile ezf = new ExtractZipFile();
		for(String fName : ficheros){
			ezf.extraeFichero(fName);
		}
		
		assertEquals(ficheros.length, ezf.getListaFicherosDescomprimidos().size());

		for(String fName : ezf.getListaFicherosDescomprimidos()){
			logger.info(fName);
			Procesa977R proc = new Procesa977R(fName);
			proc.execute();
		}
		
	}

}
