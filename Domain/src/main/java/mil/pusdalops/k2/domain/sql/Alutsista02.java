package mil.pusdalops.k2.domain.sql;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "alutsista2")
public class Alutsista02 {
	// [ID]
	@Column(name = "ID")
	private String kode;
	
	//	      ,[THN]
	@Column(name = "THN")
	private String thn;
	
	//	      ,[TW]
	//	      ,[KET_WAKTU]
	//	      ,[KOTAMAOPS]
	//	      ,[SIAP]
	//	      ,[TIDAK_SIAP]
	//	      ,[JUMLAH]
	//	      ,[NAMA]
	//	      ,[JENIS]
	//	      ,[KETERANGAN]
	
	//	      ,[ID_ALUTSISTA2]
	@Id
	@Column(name = "ID_ALUTSISTA2")
	private int id;
	
	//	      ,[TIMESTAMP]
	@Column(name = "TIMESTAMP")
	private Timestamp timeStamp;

	public String getKode() {
		return kode;
	}

	public void setKode(String kode) {
		this.kode = kode;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getThn() {
		return thn;
	}

	public void setThn(String thn) {
		this.thn = thn;
	}
	
	//	      ,[SiapTerbatas]
	//	      ,[NamaUnsur]
	//	      ,[TopP]
	//	      ,[Nyata]
	//	      ,[Ran]
	//	      ,[Alkom]
	//	      ,[Jat]
	//	      ,[Hampa]
	//	      ,[Karet]
	//	      ,[Batalyon]
}
