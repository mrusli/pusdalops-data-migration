package mil.pusdalops.k2.domain.sql;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import mil.pusdalops.domain.common.SchemaUtil;

@Entity
@Table(name = "sql_kerugian", schema = SchemaUtil.SCHEMA_COMMON)
public class Kerugian {

	//	  `id_str` varchar(255) DEFAULT NULL,
	private String id_str;
	
	//	  `nama_obj` varchar(255) DEFAULT NULL,
	private String nama_obj;
	
	//	  `jenis` varchar(255) DEFAULT NULL,
	private String jenis; 
	
	//	  `kondisi` varchar(255) DEFAULT NULL,
	private String kondisi;
	
	//	  `jumlah` int(11) DEFAULT NULL,
	private int jumlah; 
	
	//	  `satuan` varchar(255) DEFAULT NULL,
	private String satuan;
	
	//	  `keterangan` varchar(255) DEFAULT NULL,
	private String keterangan; 
	
	//	  `pihak` varchar(255) DEFAULT NULL,
	private String pihak; 
	
	//	  `pihak_id` varchar(255) DEFAULT NULL,
	private String pihak_id;
	
	//	  `sid` varchar(255) DEFAULT NULL,
	private String sid;
	
	//	  `ssid` varchar(255) DEFAULT NULL,
	private String ssid;
	
	//	  `id_kerugian` bigint(20) DEFAULT NULL,
	private Long id_kerugian;
	
	//	  `timestamp` datetime DEFAULT NULL,
	private Date timestamp;
	
	//	  `id` bigint(20) NOT NULL AUTO_INCREMENT,
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", nullable=false, unique=true)
	private Long id = Long.MIN_VALUE;

	//	`transferred_at` datetime DEFAULT NULL
	@Column(name = "transferred_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date transferredAt;
	
	@Override
	public String toString() {
		return "Kerugian [id_str=" + id_str + ", nama_obj=" + nama_obj + ", jenis=" + jenis + ", kondisi=" + kondisi
				+ ", jumlah=" + jumlah + ", satuan=" + satuan + ", keterangan=" + keterangan + ", pihak=" + pihak
				+ ", pihak_id=" + pihak_id + ", sid=" + sid + ", ssid=" + ssid + ", id_kerugian=" + id_kerugian
				+ ", timestamp=" + timestamp + ", id=" + id + "]";
	}

	public String getId_str() {
		return id_str;
	}

	public void setId_str(String id_str) {
		this.id_str = id_str;
	}

	public String getNama_obj() {
		return nama_obj;
	}

	public void setNama_obj(String nama_obj) {
		this.nama_obj = nama_obj;
	}

	public String getJenis() {
		return jenis;
	}

	public void setJenis(String jenis) {
		this.jenis = jenis;
	}

	public String getKondisi() {
		return kondisi;
	}

	public void setKondisi(String kondisi) {
		this.kondisi = kondisi;
	}

	public int getJumlah() {
		return jumlah;
	}

	public void setJumlah(int jumlah) {
		this.jumlah = jumlah;
	}

	public String getSatuan() {
		return satuan;
	}

	public void setSatuan(String satuan) {
		this.satuan = satuan;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public String getPihak() {
		return pihak;
	}

	public void setPihak(String pihak) {
		this.pihak = pihak;
	}

	public String getPihak_id() {
		return pihak_id;
	}

	public void setPihak_id(String pihak_id) {
		this.pihak_id = pihak_id;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public Long getId_kerugian() {
		return id_kerugian;
	}

	public void setId_kerugian(Long id_kerugian) {
		this.id_kerugian = id_kerugian;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTransferredAt() {
		return transferredAt;
	}

	public void setTransferredAt(Date transferredAt) {
		this.transferredAt = transferredAt;
	}
	
}
