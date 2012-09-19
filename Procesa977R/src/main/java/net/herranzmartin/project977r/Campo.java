/**
 * 
 */
package net.herranzmartin.project977r;



public class Campo{

	private int numeroBloque      = 0;
	private int numeroBloquePadre = 0;

	private String nombre   = "";
	private String tipoCampo     = "";
	private String formato  = "";
	private int posicion = 0; //int
	private int longitud = 0; //int
	private int campoRepetido = 0; //int
	private String tablaAuxiliar = "";
	
	public static Campo getCampo(int nb, int nbp, String str){
		Campo campo = new Campo();
			campo.numeroBloque = nb;
			campo.numeroBloquePadre = nbp;
			int pos = 0;
			campo.nombre = str.substring(pos, pos+=25).trim().replace(" ", "_");
			campo.tipoCampo = str.substring(pos, pos+=1).trim();
			campo.formato = str.substring(pos, pos+=15).trim();
			campo.posicion = Integer.valueOf(str.substring(pos, pos+=4));
			campo.longitud = Integer.valueOf(str.substring(pos, pos+=3));
			campo.campoRepetido = Integer.valueOf(str.substring(pos, pos+=1));
			campo.tablaAuxiliar = str.substring(pos, pos+=3).trim();
			
		return campo;
	}
	
	
	
	public Campo() {
		super();
	}



	public Campo(String nombre) {
		super();
		this.nombre = nombre;
	}

	public Campo(String nombre, int nb, int nbp) {
		super();
		this.nombre = nombre;
		this.numeroBloque = nb;
		this.numeroBloquePadre = nbp;
	}


	public String toString(){
		return "" 
			+ this.numeroBloque + ";" 
			+ this.numeroBloquePadre + ";" 
			+ this.nombre + ";" 
			+ this.tipoCampo + ";" 
			+ this.formato + ";"
			+ this.posicion + ";"
			+ this.longitud + ";"
			+ this.campoRepetido + ";"
			+ this.tablaAuxiliar + ";"
			;
	}

	/**
	 * @return the numeroBloque
	 */
	public int getNumeroBloque() {
		return numeroBloque;
	}

	/**
	 * @param numeroBloque the numeroBloque to set
	 */
	public void setNumeroBloque(int numeroBloque) {
		this.numeroBloque = numeroBloque;
	}

	/**
	 * @return the numeroBloquePadre
	 */
	public int getNumeroBloquePadre() {
		return numeroBloquePadre;
	}

	/**
	 * @param numeroBloquePadre the numeroBloquePadre to set
	 */
	public void setNumeroBloquePadre(int numeroBloquePadre) {
		this.numeroBloquePadre = numeroBloquePadre;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the tipoCampo
	 */
	public String getTipo() {
		return tipoCampo;
	}

	/**
	 * @param tipoCampo the tipoCampo to set
	 */
	public void setTipo(String tipoCampo) {
		this.tipoCampo = tipoCampo;
	}

	/**
	 * @return the formatoCampo
	 */
	public String getFormato() {
		return formato;
	}

	/**
	 * @param formatoCampo the formatoCampo to set
	 */
	public void setFormato(String formatoCampo) {
		this.formato = formatoCampo;
	}

	/**
	 * @return the posicionCampo
	 */
	public int getPosicionCampo() {
		return posicion;
	}

	/**
	 * @param posicionCampo the posicionCampo to set
	 */
	public void setPosicionCampo(int posicionCampo) {
		this.posicion = posicionCampo;
	}

	/**
	 * @return the longitudCampo
	 */
	public int getLongitud() {
		return longitud;
	}

	/**
	 * @param longitudCampo the longitudCampo to set
	 */
	public void setLongitud(int longitudCampo) {
		this.longitud = longitudCampo;
	}

	/**
	 * @return the campoRepetido
	 */
	public int getCampoRepetido() {
		return campoRepetido;
	}

	/**
	 * @param campoRepetido the campoRepetido to set
	 */
	public void setCampoRepetido(int campoRepetido) {
		this.campoRepetido = campoRepetido;
	}

	/**
	 * @return the tablaAuxiliar
	 */
	public String getTablaAuxiliar() {
		return tablaAuxiliar;
	}

	/**
	 * @param tablaAuxiliar the tablaAuxiliar to set
	 */
	public void setTablaAuxiliar(String tablaAuxiliar) {
		this.tablaAuxiliar = tablaAuxiliar;
	}

	
}