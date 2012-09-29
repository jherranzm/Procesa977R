package net.herranzmartin.project977r.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the tbl_ConsultasSQL database table.
 * 
 */
@Entity
@Table(name="tbl_ConsultasSQL")
public class Tbl_ConsultasSQL implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int id;

    @Lob()
	@Column(nullable=false)
	private String definicion;

	@Column(nullable=false, length=255)
	private String nombre;

	//bi-directional many-to-one association to TblPestanye
	@OneToMany(mappedBy="tblConsultasSql")
	private List<TblPestanye> tblPestanyes;

    public Tbl_ConsultasSQL() {
    }

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDefinicion() {
		return this.definicion;
	}

	public void setDefinicion(String definicion) {
		this.definicion = definicion;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<TblPestanye> getTblPestanyes() {
		return this.tblPestanyes;
	}

	public void setTblPestanyes(List<TblPestanye> tblPestanyes) {
		this.tblPestanyes = tblPestanyes;
	}
	
}