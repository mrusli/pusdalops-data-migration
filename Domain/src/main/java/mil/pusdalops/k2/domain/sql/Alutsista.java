package mil.pusdalops.k2.domain.sql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "alutsista")
public class Alutsista {
	
	// [KODE]
	@Column(name = "KODE")
	private String kode;
	// [JUMLAH]
	
	// [JENIS]
	
	// [SIAP]
	
	// [KETERANGAN]
	
	// [TDKSIAP]
	
	// [ID_ALUTSISTA]
	@Id
	@Column(name = "ID_ALUTSISTA")
	private int id;
	
	// [TIMESTAMP]

	public String getKode() {
		return kode;
	}

	public void setKode(String kode) {
		this.kode = kode;
	}
	
	
}
