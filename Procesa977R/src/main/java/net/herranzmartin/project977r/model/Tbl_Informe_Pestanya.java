package net.herranzmartin.project977r.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tbl_Informe_Pestanya database table.
 * 
 */
@Entity
@Table(name="tbl_Informe_Pestanya")
public class Tbl_Informe_Pestanya implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(nullable=false)
	private int orden;

	@Column(name="pestanya_id", nullable=false)
	private int pestanyaId;

	//bi-directional many-to-one association to Tbl_InformesXL
    @ManyToOne
	@JoinColumn(name="informe_id", nullable=false)
	private Tbl_InformesXL tblInformesXl;

    public Tbl_Informe_Pestanya() {
    }

	public int getOrden() {
		return this.orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public int getPestanyaId() {
		return this.pestanyaId;
	}

	public void setPestanyaId(int pestanyaId) {
		this.pestanyaId = pestanyaId;
	}

	public Tbl_InformesXL getTblInformesXl() {
		return this.tblInformesXl;
	}

	public void setTblInformesXl(Tbl_InformesXL tblInformesXl) {
		this.tblInformesXl = tblInformesXl;
	}
	
}