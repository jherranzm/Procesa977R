/**
 * 
 */
package net.herranzmartin.project977r;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.log4j.Logger;

public class Procesa977R {
	


	final private static String SEPARADOR_CAMPO = ";";
	final private static String PREFIJO_FICHERO_TEXTO = "tbl_";
	final private static String EXTENSION_FICHERO = ".txt";

	final private static Logger logger = Logger.getLogger(Procesa977R.class); 

	//Campos especiales
	final private static String OCURRENCIAS = "OCURRENCIAS";
	final private static String CODIGO_REGISTRO = "CODIGO_REGISTRO";
	final private static String NUMERO_CAMPOS = "NUMERO_CAMPOS";
	final private static String NUMERO_BLOQUES = "NUMERO_BLOQUES";
	final private static String OCURRENCIAS2 = "OCURRENCIAS2";
	final private static String NOMBRE_ARCHIVO_PC = "NOMBRE_ARCHIVO_PC";
	final private static String FECHA_DE_EMISION = "FECHA_DE_EMISION";
	final private static String TEXTO = "TEXTO";
	final private static String LONGITUD_TEXTOS = "LONGITUD_TEXTOS";
	final private static String CODIGO_INTERNO = "CODIGO_INTERNO";
	final private static String CODIGO_EXTERNO = "CODIGO_EXTERNO";

	//Campos de Registros Especiales
	final private String[] zonaA = { "CODIGO", "NUMERO_SECUENCIAL", "CODIGO_CLIENTE",
			"AGRUPACION_FACTURABLE", "AGRUPACION_DETALLE", "TIPO_SERVICIO",
			"MULTICONEXION", "NUMERO_COMERCIAL_1", "NUMERO_COMERCIAL_2",
			"LONGITUD_REGISTRO", CODIGO_REGISTRO, NUMERO_BLOQUES, NUMERO_CAMPOS };
	final private int[] longZonaA = { 6, 8, 8, 12, 12, 12, 20, 20, 20, 4, 6, 3, 3 };

	final private String[] nombreCampos000000 = { "CODIGO",
			"NUMERO_SECUENCIAL", "CODIGO_CLIENTE", "AGRUPACION_FACTURABLE",
			"AGRUPACION_DETALLE", "TIPO_SERVICIO", "MULTICONEXION",
			"NUMERO_COMERCIAL_1", "NUMERO_COMERCIAL_2", "LONGITUD_REGISTRO",
			"TIPO_DE_FACTURACION", "CIF_SUPRACLIENTE", "NOMBRE_SUPRACLIENTE",
			"IDENTIFICACION_TIPO_ACUERDO", "AGF_MONOSERVICIO",
			"DUPLICADO_REGULAR", FECHA_DE_EMISION, "NOMBRE_ENTIDAD_EMISORA",
			"TIPO_DE_SOPORTE", "RECEPTOR_DEL_SOPORTE", "DOMICILIO",
			"LOCALIDAD", "CODIGO_POSTAL", "PROVINCIA", NOMBRE_ARCHIVO_PC,
			"TIPO_DE_MONEDA", "TIPO_DE_CAMBIO", "FECHA_VERSION",
			"VERSION_DE_977", "1A_LINEA_DE_ETIQUETA", "2A_LINEA_DE_ETIQUETA",
			"IDENTIFICADOR_UNICO", "NUM_REGISTROS_TOTALES", "TIPO_DE_IMPUESTO",
			"LONGITUD_ARCHIVO" };
	final private int[] longCampos000000 = { 6, 8, 8, 12, 12, 12, 20, 20, 20,
			4, 40, 18, 70, 30, 20, 5, 8, 70, 2, 65, 40, 40, 7, 40, 12, 5, 7, 8,
			5, 56, 30, 8, 8, 1, 10 };

	// Mapa donde estará la estructura de datos del registro o
	// cómo leer cada registro
	private Map<String, ArrayList<Campo>> estructuraRegistros;
	private Map<String, ArrayList<Campo>> estructuraRegistrosExtendida;

	private Map<String, File> filesOut = new HashMap<String, File>();
	private Map<String, BufferedWriter> bwOut = new HashMap<String, BufferedWriter>();

	private Map<String, String> formatos = new TreeMap<String, String>();

	
	private String fechaFactura = "";
	private String nombreFichero = "";
	private String fichero = "";
	private long claveTbl000000 = 0;

	private Map<String, String> tablasAuxiliares;
	private Map<String, ArrayList<String>> camposTabla;
	
	
	private EntityManagerFactory emf;

	public Procesa977R(String fichero) {
		super();
		this.fichero = fichero;
	}

	private void inicializaRegistros000000() {

		ArrayList<Campo> listaCampos = new ArrayList<Campo>();
		for (int j = 0; j < nombreCampos000000.length; j++) {
			Campo campo = new Campo();
			campo.setNombre( nombreCampos000000[j] );
			campo.setLongitud( longCampos000000[j] );
			listaCampos.add(campo);
		}
		estructuraRegistros.put(Constantes977R.REG_000000, listaCampos);
		estructuraRegistrosExtendida.put(Constantes977R.REG_000000, listaCampos);
	}

	private void inicializaRegistrosZonaA() {

		ArrayList<Campo> listaCampos = new ArrayList<Campo>();
		for (int j = 0; j < zonaA.length; j++) {
			Campo campo = new Campo();
			campo.setNombre( zonaA[j] );
			campo.setLongitud( longZonaA[j] );
			listaCampos.add(campo);
		}
		estructuraRegistros.put(Constantes977R.ZONA_A, listaCampos);
		//estructuraRegistrosExtendida.put(ZONA_A, listaCampos);

	}

	private void procesaRegistro000000(String strLine) {
		logger.info("Procesando registro " + Constantes977R.REG_000000 + "...");

		ArrayList<Campo> listaCampos = estructuraRegistros.get(Constantes977R.REG_000000);
		
		int pos = 0;

		StringBuilder ret = new StringBuilder();
		for (Campo campo : listaCampos) {
			String chunk = strLine.substring(pos, pos + campo.getLongitud());

			ret.append(chunk).append(SEPARADOR_CAMPO);
			pos += campo.getLongitud();

			if (NOMBRE_ARCHIVO_PC.equals(campo.getNombre())) {
				nombreFichero = chunk;
			} else if (FECHA_DE_EMISION.equals(campo.getNombre())) {
				fechaFactura = chunk;
			}
			
		}
		String ret2 = nombreFichero + SEPARADOR_CAMPO + fechaFactura + SEPARADOR_CAMPO;
		
		recuperaLosCamposDeLaTabla000000();
		saveToFile(Constantes977R.REG_000000, ret2.toString() + ret.toString());
		loadDataInFile(Constantes977R.REG_000000);
	}

	private void procesaRegistroGenerico(final String codigo,
			final String strLine) {
//		logger.info("Procesando registro " + codigo + "...");

		int pos = 0, 
				numRepeticionesBloque2 = 0, 
				numRepeticionesBloque3 = 0;
		ArrayList<Campo> listaCampos = estructuraRegistros.get(codigo);

		StringBuilder ret = new StringBuilder();
		
			ret.append(claveTbl000000).append(SEPARADOR_CAMPO);
			ret.append(Constantes977R.COMILLAS).append(nombreFichero).append(Constantes977R.COMILLAS).append(SEPARADOR_CAMPO);
			ret.append(Constantes977R.COMILLAS).append(fechaFactura).append(Constantes977R.COMILLAS).append(SEPARADOR_CAMPO);
		
		// Tratamiento del Bloque 1
		for (Campo campo : listaCampos) {
			// Sólo tratamos los campos del bloque 1
			if (campo.getNumeroBloque() == 1) {
				String original = strLine.substring(pos, pos + campo.getLongitud()).trim();

				
				String formateado = ModificadorFormatos.formatea(campo, original);

				ret.append(formateado).append(SEPARADOR_CAMPO);
				pos += campo.getLongitud();

				// Campos especiales
				if (OCURRENCIAS2.equals(campo.getNombre())) {
					numRepeticionesBloque2 = Integer.valueOf(original);
				}
				
				// Tiene el campo info adicional en tablas auxiliares?
				if(campo.getTablaAuxiliar() == null || campo.getTablaAuxiliar().equals("")){
					// No hay datos tabla auxiliar para este campo...
				}else{
					String clave = campo.getTablaAuxiliar() + SEPARADOR_CAMPO + original + SEPARADOR_CAMPO;
					String v = tablasAuxiliares.get(clave);
					if(v != null){
						ret.append(Constantes977R.COMILLAS).append(v).append(Constantes977R.COMILLAS).append(SEPARADOR_CAMPO);
					}else{
						ret.append(Constantes977R.COMILLAS).append("").append(Constantes977R.COMILLAS).append(SEPARADOR_CAMPO);
					}// 
				}// Fin tablas auxiliares
			}// .. el campo es del bloque 1
		}// Fin Bloque 1

		/**
		 * Nota: sólo hay un tipo de regitro que tenga bloque 3, y que es el 300000
		 */
		
		// Tratamiento del bloque2
		for (int k = 0; k < numRepeticionesBloque2; k++) {
			StringBuilder ret2 = new StringBuilder();
			for (Campo campo : listaCampos) {
				String original = "";
				if (campo.getNumeroBloque() > 1) {

					// Hay veces que, al final de línea, el campo es más corto de lo esperado
					if (pos + campo.getLongitud() < strLine.length()) {
						original = strLine.substring(pos, pos + campo.getLongitud()).trim();
					} else {
						original = strLine.substring(pos).trim();
					}

					
					String formateado = ModificadorFormatos.formatea(campo, original);

					ret2.append(formateado).append(SEPARADOR_CAMPO);
					pos += campo.getLongitud();
					
					// tiene tabla auxiliar??
					if(campo.getTablaAuxiliar().equals("")){
					}else{
						String clave = campo.getTablaAuxiliar() + SEPARADOR_CAMPO + original + SEPARADOR_CAMPO;
						String v = tablasAuxiliares.get(clave);
						if(v != null){
							ret2.append(Constantes977R.COMILLAS).append(v).append(Constantes977R.COMILLAS).append(SEPARADOR_CAMPO);
						}else{
							ret2.append(Constantes977R.COMILLAS).append("").append(Constantes977R.COMILLAS).append(SEPARADOR_CAMPO);
						}
					}
				}// if
			}// for

			String ret3 = ret.toString() + ret2.toString();
			saveToFile(codigo, ret3);
		}// for
	}

	/**
	 * @param codigo
	 * @param linea
	 */
	public void saveToFile(final String codigo, final String linea) {
		try {
			BufferedWriter br = getBROut(codigo);
			br.write(linea);
			br.newLine();
			br.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param strLine
	 */
	private void procesaRegistro901000(String strLine) {

		int pos = 0, numRepeticionesBloque2 = 0, longitudTextos = 0;
		ArrayList<Campo> listaCampos = estructuraRegistros.get(Constantes977R.REG_901000);

		StringBuilder ret = new StringBuilder();
		
			ret.append(Constantes977R.COMILLAS).append(nombreFichero).append(Constantes977R.COMILLAS).append(SEPARADOR_CAMPO);
			ret.append(Constantes977R.COMILLAS).append(fechaFactura).append(Constantes977R.COMILLAS).append(SEPARADOR_CAMPO);

		for (Campo campo : listaCampos) {
			if (campo.getNumeroBloque() == 1) {
				String chunk = strLine.substring(pos, pos + campo.getLongitud());
				ret.append(chunk).append(SEPARADOR_CAMPO);
				pos += campo.getLongitud();

				// Campos especiales
				if (LONGITUD_TEXTOS.equals(campo.getNombre())) {
					longitudTextos = Integer.valueOf(chunk);
				}else if (OCURRENCIAS2.equals(campo.getNombre())) {
					numRepeticionesBloque2 = Integer.valueOf(chunk);
				}
			}
		}

		for (int k = 0; k < numRepeticionesBloque2; k++) {
			StringBuilder ret2 = new StringBuilder();
			String clave = "", valor = "";
			for (Campo campo : listaCampos) {
				String chunk = "";
				if (campo.getNumeroBloque() != 1) {
					if (TEXTO.equals(campo.getNombre())) {
						campo.setLongitud( longitudTextos );
					}
					if (pos + campo.getLongitud() < strLine.length()) {
						chunk = strLine.substring(pos, pos + campo.getLongitud()).trim();
					} else {
						chunk = strLine.substring(pos).trim();
					}
					ret2.append(chunk).append(SEPARADOR_CAMPO);
					pos += campo.getLongitud();
					
					if(CODIGO_EXTERNO.equals(campo.getNombre()) || CODIGO_INTERNO.equals(campo.getNombre())){
						clave += chunk + SEPARADOR_CAMPO;
					}else if(TEXTO.equals(campo.getNombre())){
						valor = chunk;
					}
					
					
				}// if
			}// for
			
			tablasAuxiliares.put(clave, valor);

			String ret3 = ret.toString() + ret2.toString();
			saveToFile(Constantes977R.REG_901000, ret3);
		}// for
	}

	/**
	 * 
	 * @param strLine
	 */
	private void procesaRegistro903000(String strLine) {

		ArrayList<Campo> listaCamposZonaA = estructuraRegistros.get(Constantes977R.ZONA_A);
		int pos = 0, numeroBloques = 0, numeroCampos = 0, numeroBloque = 0, numeroBloquePadre = 0;
		String codigoRegistro = "";

		for (Campo campo : listaCamposZonaA) {
			String valorCampo = strLine.substring(pos, pos + campo.getLongitud());
			logger.info(campo.getNombre() + ":" + pos + ":" + campo.getLongitud() + ":" + valorCampo);
			pos += campo.getLongitud();
			if (NUMERO_BLOQUES.equals(campo.getNombre())) {
				numeroBloques = Integer.valueOf(valorCampo);
			} else if (NUMERO_CAMPOS.equals(campo.getNombre())) {
				numeroCampos = Integer.valueOf(valorCampo);
			} else if (CODIGO_REGISTRO.equals(campo.getNombre())) {
				codigoRegistro = valorCampo;
			}
		}
		logger.info(NUMERO_BLOQUES + ":" + numeroBloques);
		logger.info(NUMERO_CAMPOS + ":" + numeroCampos);

		ArrayList<Campo> listaCamposRegistro = new ArrayList<Campo>();
		ArrayList<Campo> listaCamposRegistroExtendida = new ArrayList<Campo>();
		listaCamposRegistro.add(new Campo("NOMBRE_FICHERO"));
		listaCamposRegistro.add(new Campo("FECHA_FACTURA"));
		listaCamposRegistroExtendida.add(new Campo("NOMBRE_FICHERO"));
		listaCamposRegistroExtendida.add(new Campo("FECHA_FACTURA"));

		for (int b = 0; b < numeroBloques; b++) {
			numeroBloque = Integer.valueOf(strLine.substring(pos, pos + 1));
			pos++;
			numeroBloquePadre = Integer.valueOf(strLine.substring(pos, pos + 1));
			pos++;
			logger.info("");
			logger.info("Número Bloque:" + numeroBloque);
			logger.info("Número Bloque Padre:" + numeroBloquePadre);

			int numOcurrencias = 0;
			for (int c = 0; c < numeroCampos; c++) {
				String strCampo = strLine.substring(pos, pos + 52);
				
				if (strCampo.substring(0, 1).equals(" ")) {
				} else {
					Campo campo = Campo.getCampo(numeroBloque,
							numeroBloquePadre, strCampo);

					formatos.put(campo.getTipo() + ":" + campo.getFormato(),
							campo.getTipo() + ":" + campo.getFormato());

					if (OCURRENCIAS.equals(campo.getNombre())) {
						numOcurrencias++;
						campo.setNombre(OCURRENCIAS + numOcurrencias);
					}
					if (campo.getLongitud() > 0) {
						listaCamposRegistro.add(campo);
						listaCamposRegistroExtendida.add(campo);
						logger.info(campo.toString());
						if (!campo.getTablaAuxiliar().equals("")) {
							Campo campoExt = new Campo();
							campoExt.setNombre( "DESC_" + campo.getNombre() );
							listaCamposRegistroExtendida.add(campoExt);
							logger.info("---" + campoExt.toString());
						}
					}

				}
				pos += 52;
			}
		}

		estructuraRegistros.put(codigoRegistro, listaCamposRegistro);
		estructuraRegistrosExtendida.put(codigoRegistro, listaCamposRegistroExtendida);

	}

	/**
	 * 
	 * Retorna un Buffer de escritura según el nombre pasado por parámetro El
	 * método busca el fichero en la HashTable, si lo encuentra devuelve el
	 * Objeto sino crea uno nuevo
	 * 
	 * 
	 * @param codigoRegistro
	 * @return
	 */
	public BufferedWriter getBROut(String codigoRegistro) {

		File fileOut = null;
		BufferedWriter out = null;
		OutputStreamWriter osw = null;
		FileOutputStream fos = null;

		try {
			if (filesOut.get(codigoRegistro) == null) {
				// creamos un nuevo fOut
				fileOut = new File(PREFIJO_FICHERO_TEXTO + codigoRegistro + EXTENSION_FICHERO);
				fos = new FileOutputStream(fileOut);
				osw = new OutputStreamWriter(fos, Constantes977R.CODIFICACION_FICHERO_DESTINO);
				out = new BufferedWriter(osw);
				
				ArrayList<Campo> listaCampos = estructuraRegistrosExtendida.get(codigoRegistro);
				StringBuilder sb = new StringBuilder();
				for(Campo campo : listaCampos){
					sb.append(Constantes977R.COMILLAS).append(campo.getNombre()).append(Constantes977R.COMILLAS).append(SEPARADOR_CAMPO);
				}
				out.write(sb.toString());
				out.newLine();
				
				filesOut.put(codigoRegistro, fileOut);
				bwOut.put(codigoRegistro, out);
			} else {
				out = bwOut.get(codigoRegistro);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return out;
	}

	public void execute() {

		init();
		TreeSet<String> tree;
		
		try {

			recuperaEstructura();
			tree = new TreeSet<String>(estructuraRegistrosExtendida.keySet());
			recuperaLosCamposDeLasTablas(tree);

			recuperaTablasAuxiliares();

			procesaFichero();

//			for (String formato : formatos.keySet()) {
//				logger.info(formato);
//			}
			
			
			
			for(String str : tree){
				logger.info(str);
				//logger.info(getCamposTabla("tbl_"+ str));
				if (!Constantes977R.REG_000000.equals(str) && !Constantes977R.REG_901000.equals(str) && !Constantes977R.REG_903000.equals(str)) loadDataInFile(str);
			}
			
			

			logger.info("Fin!");
		} catch (Exception e) {
			System.err.println("Error:" + e.toString());
		}


	}

	/**
	 * @param tree
	 */
	private void recuperaLosCamposDeLasTablas(TreeSet<String> tree) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		for(String elRegistro : tree){
			logger.info("Inicio recogida de los campos de la tabla tbl_" + elRegistro);
			logger.info(elRegistro);
			//logger.info(getCamposTabla("tbl_"+ str));
			ArrayList<String> listaCampos = null;
			if (!Constantes977R.REG_903000.equals(elRegistro)) {
				//Iniciamos la transacción
				
				listaCampos = recuperaLosCamposDeLaTabla(em, elRegistro);
			}
			camposTabla.put(elRegistro, listaCampos);
		}
		// Cerramos la transacción...
		em.getTransaction().commit();
		em.close();
	}

	/**
	 * @param tree
	 */
	private void recuperaLosCamposDeLaTabla000000() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
			logger.info("Inicio recogida de los campos de la tabla tbl_" + Constantes977R.REG_000000);
			logger.info(Constantes977R.REG_000000);
			ArrayList<String> listaCampos = recuperaLosCamposDeLaTabla(em, Constantes977R.REG_000000);
			camposTabla.put(Constantes977R.REG_000000, listaCampos);
		// Cerramos la transacción...
		em.getTransaction().commit();
		em.close();
	}

	/**
	 * @param em
	 * @param laTabla
	 * @return
	 */
	private ArrayList<String> recuperaLosCamposDeLaTabla(
			EntityManager em,
			String laTabla) {
		ArrayList<String> listaCampos;
		listaCampos = new ArrayList<String>();
		
		String laConsultaSQL = "DESCRIBE tbl_" + laTabla;
		
		Query query = em.createNativeQuery(laConsultaSQL);
		
		@SuppressWarnings("unchecked")
		List<Object[]> list = query.getResultList();
		
		logger.info("Recuperados " + list.size() + " registros...");
		for(Object[] obj : list.subList(1, list.size()- 1)){
			listaCampos.add((String)obj[0]);
		}
		listaCampos.add((String)list.get(list.size()-1)[0]);

		logger.info("Fin recogida de los campos de la tabla tbl_" + laTabla);
		return listaCampos;
	}

	/**
	 * @param proc
	 */
	private void init() {
		
		emf = Persistence.createEntityManagerFactory("977R");

		estructuraRegistros = new HashMap<String, ArrayList<Campo>>();
		estructuraRegistrosExtendida = new HashMap<String, ArrayList<Campo>>();
		tablasAuxiliares = new HashMap<String, String>();
		camposTabla = new HashMap<String, ArrayList<String>>();
		
		inicializaRegistros000000();
		inicializaRegistrosZonaA();
	}

	/**
	 * @param textFile
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private void procesaFichero()
			throws FileNotFoundException, UnsupportedEncodingException,
			IOException {
		String strLine;
		FileInputStream fis3 = new FileInputStream(new File(fichero));
		InputStreamReader isr3 = new InputStreamReader(fis3, Constantes977R.CODIFICACION_FICHERO_ORIGEN);
		BufferedReader br3 = new BufferedReader(isr3);

		while ((strLine = br3.readLine()) != null && (strLine.length() > 6)) {
			String codigo = strLine.substring(0, 6);
			if (Constantes977R.REG_901000.equals(codigo)
					|| Constantes977R.REG_903000.equals(codigo)
					|| Constantes977R.REG_000000.equals(codigo)) {
			} else {
				// logger.info(strLine);
				procesaRegistroGenerico(codigo, strLine);
			}
		}
	}

	/**
	 * Recupera los datos de las tablas auxiliares.
	 * 
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private void recuperaTablasAuxiliares()
			throws FileNotFoundException, 
			UnsupportedEncodingException,
			IOException {
		FileInputStream fis2 = new FileInputStream(new File(fichero));
		InputStreamReader isr2 = new InputStreamReader(fis2, Constantes977R.CODIFICACION_FICHERO_ORIGEN);
		BufferedReader br2 = new BufferedReader(isr2);

		String strLine;
		while ((strLine = br2.readLine()) != null && (strLine.length() > 6)) {
			String codigo = strLine.substring(0, 6);
			if (Constantes977R.REG_901000.equals(codigo)) {
				procesaRegistro901000(strLine);
			}
		}
	}

	/**
	 * Recupera la estructura de los registros
	 * 
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private void recuperaEstructura()
			throws FileNotFoundException, 
			UnsupportedEncodingException,
			IOException {
		
		FileInputStream fis = new FileInputStream(new File(fichero));
		InputStreamReader isr = new InputStreamReader(fis, Constantes977R.CODIFICACION_FICHERO_ORIGEN);
		BufferedReader br = new BufferedReader(isr);

		String strLine;
		while ((strLine = br.readLine()) != null && (strLine.length() > 6)) {
			String codigo = strLine.substring(0, 6);
			if (Constantes977R.REG_000000.equals(codigo)) {
				procesaRegistro000000(strLine);
			} else if (Constantes977R.REG_903000.equals(codigo)) {
				procesaRegistro903000(strLine);
			}
		}
	}

	/**
	 * @return the fichero
	 */
	public String getFichero() {
		return fichero;
	}
	
	/**
	 * @param fichero the fichero to set
	 */
	public void setFichero(String fichero) {
		this.fichero = fichero;
	}
	

	/**
	 * Recupera los campos de la tabla pasada por parámetro
	 * de la matriz "camposTabla" y retorna una cadena con los campos
	 * separados por comas
	 * 
	 * 
	 * @param tabla
	 * @return cadena separada por comas de los campos de la tabla pasada por parámetro
	 */
	private String getCamposTabla(String tabla){
		StringBuilder sb = new StringBuilder();
		List<String> list = camposTabla.get(tabla);
		
		for(String campo : list.subList(0, list.size()- 1)){
			sb.append(campo).append(", ");
		}
		sb.append(list.get(list.size()-1));

		return sb.toString();
	}
	
	
	/**
	 * 
	 * Carga los registros "codigo" en la tabla correspondiente.
	 * 
	 * En el caso de registros 000000, que son registros padre, si están
	 * los elimina previamente, y posteriormente los carga
	 * 
	 * Tiene conexión con la BD
	 * 
	 * @param codigo
	 */
	private void loadDataInFile(final String codigo){

		try {
			EntityManager em = emf.createEntityManager();
			Query query;
			int numRegs;
			
			logger.info("Inicio de carga de  los registros " + codigo);
			//Iniciamos la transacción
			em.getTransaction().begin();
			
			if (Constantes977R.REG_000000.equals(codigo)) {
				//	sql = "DELETE FROM 977r.tbl_000000 WHERE (FICHERO = '"+fichero+"' AND FECHA_FACTURA='"+fecha_factura+"')"
				query = em
						.createNativeQuery("DELETE FROM 977r.tbl_000000 WHERE (FICHERO = '"
								+ nombreFichero
								+ "' AND FECHA_FACTURA='"
								+ fechaFactura + "')");
				numRegs = query.executeUpdate();
				logger.info("Eliminados " + numRegs + " registros");
			}
			query = em.createNativeQuery(createLoadDataInfileQuery(codigo));
			numRegs = query.executeUpdate();
			logger.info("Incorporados " + numRegs + " registros");
			
			
			if (Constantes977R.REG_000000.equals(codigo)) {
				query = em
						.createNativeQuery("SELECT t.id FROM 977r.tbl_000000 t WHERE (t.FICHERO = '"
								+ nombreFichero
								+ "' AND t.FECHA_FACTURA='"
								+ fechaFactura + "')");
				Integer l = (Integer) query.getSingleResult();
				claveTbl000000 = l.longValue();
			}
			// Cerramos la transacción...
			em.getTransaction().commit();
			logger.info("Fin de carga de  los registros " + codigo);
			em.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * @param tabla
	 * @return
	 */
	private String createLoadDataInfileQuery(String tabla){
		String sql = "LOAD DATA LOCAL INFILE '" + PREFIJO_FICHERO_TEXTO + tabla + EXTENSION_FICHERO +"' ";
		sql += "INTO TABLE 977R.tbl_" + tabla + " " ;
		sql += "FIELDS TERMINATED BY ';' OPTIONALLY ENCLOSED BY '\"' " ;
		sql += "IGNORE 1 LINES " ;
		sql += "(" + getCamposTabla(tabla) + ")";
		logger.info(sql);
		return sql;
		
	}
	

}
