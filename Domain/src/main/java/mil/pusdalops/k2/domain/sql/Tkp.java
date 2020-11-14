package mil.pusdalops.k2.domain.sql;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import mil.pusdalops.domain.common.SchemaUtil;
import mil.pusdalops.domain.kejadian.Kejadian;

/**
 * @author mrusli
 *
 */
@Entity
@Table(name = "sql_tkp", schema = SchemaUtil.SCHEMA_COMMON)
public class Tkp {

	//	  `id_str` varchar(255) DEFAULT NULL,
	private String id_str;
	
	//	  `thn` varchar(255) DEFAULT NULL,
	private String thn;
	
	//	  `tw` varchar(255) DEFAULT NULL,
	private String tw;
	
	//	  `ketwaktu` varchar(255) DEFAULT NULL,
	private String ketwaktu;
	
	//	  `THNACT` varchar(255) DEFAULT NULL,
	private String THNACT;
	
	//	  `TWACT` varchar(255) DEFAULT NULL,
	private String TWACT;
	
	//	  `KETWAKTUACT` varchar(255) DEFAULT NULL,
	private String KETWAKTUACT;
	
	//	  `KOTAMAOPS` varchar(255) DEFAULT NULL,
	private String KOTAMAOPS;
	
	//	  `GPS` varchar(255) DEFAULT NULL,
	private String GPS; 
	
	//	  `bujur` varchar(255) DEFAULT NULL,
	private String bujur; 
	
	//	  `KV` varchar(255) DEFAULT NULL,
	private String KV;
	
	//	  `KOORDINAT` varchar(255) DEFAULT NULL,
	private String KOORDINAT;
	
	//	  `PROPINSI` varchar(255) DEFAULT NULL,
	private String PROPINSI;
	
	//	  `KABKOTA` varchar(255) DEFAULT NULL,
	private String KABKOTA; 
	
	//	  `KECAMATAN` varchar(255) DEFAULT NULL,
	private String KECAMATAN;
	
	//	  `KELURAHAN` varchar(255) DEFAULT NULL,
	private String KELURAHAN;
	
	//	  `KAMPUNG` varchar(255) DEFAULT NULL,
	private String KAMPUNG;
	
	//	  `KLASIFIKASI` varchar(255) DEFAULT NULL,
	private String KLASIFIKASI;
	
	//	  `MOTIF` varchar(255) DEFAULT NULL,
	private String MOTIF;
	
	//	  `PELAKU` varchar(255) DEFAULT NULL,
	private String PELAKU; 
	
	//	  `KETPELAKU` varchar(255) DEFAULT NULL,
	private String KETPELAKU; 
	
	//	  `SASARAN` varchar(255) DEFAULT NULL,
	private String SASARAN; 
	
	//	  `SID` varchar(255) DEFAULT NULL,
	private String SID;
	
	//	  `SSID` varchar(255) DEFAULT NULL,
	private String SSID; 
	
	//	  `JALAN` varchar(255) DEFAULT NULL,
	private String JALAN; 
	
	//	  `KOLAK` varchar(255) DEFAULT NULL,
	private String KOLAK; 
	
	//	  `PERAIRAN` varchar(255) DEFAULT NULL,
	private String PERAIRAN;
	
	//	  `id_tkp` varchar(255) DEFAULT NULL,
	private String id_tkp;
	
	//	  `TimeStamp` datetime DEFAULT NULL,
	private Date TimeStamp; 
	
	//	  `SatuanUnsur` varchar(255) DEFAULT NULL,
	private String SatuanUnsur; 
	
	//	  `Batalyon` varchar(255) DEFAULT NULL,
	private String Batalyon; 
	
	//	  `id` bigint(20) NOT NULL AUTO_INCREMENT,
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", nullable=false, unique=true)
	private Long id = Long.MIN_VALUE;

	//	`transferred_at` datetime DEFAULT NULL,
	@Column(name = "transferred_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date transfferedAt;
	
	//	`kejadian_id_fk` bigint(20) NOT NULL
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "kejadian_id_fk")
	private Kejadian kejadian;
	
	@Override
	public String toString() {
		return "Tkp [id_str=" + id_str + ", thn=" + thn + ", tw=" + tw + ", ketwaktu=" + ketwaktu + ", THNACT=" + THNACT
				+ ", TWACT=" + TWACT + ", KETWAKTUACT=" + KETWAKTUACT + ", KOTAMAOPS=" + KOTAMAOPS + ", GPS=" + GPS
				+ ", bujur=" + bujur + ", KV=" + KV + ", KOORDINAT=" + KOORDINAT + ", PROPINSI=" + PROPINSI
				+ ", KABKOTA=" + KABKOTA + ", KECAMATAN=" + KECAMATAN + ", KELURAHAN=" + KELURAHAN + ", KAMPUNG="
				+ KAMPUNG + ", KLASIFIKASI=" + KLASIFIKASI + ", MOTIF=" + MOTIF + ", PELAKU=" + PELAKU + ", KETPELAKU="
				+ KETPELAKU + ", SASARAN=" + SASARAN + ", SID=" + SID + ", SSID=" + SSID + ", JALAN=" + JALAN
				+ ", KOLAK=" + KOLAK + ", PERAIRAN=" + PERAIRAN + ", id_tkp=" + id_tkp + ", TimeStamp=" + TimeStamp
				+ ", SatuanUnsur=" + SatuanUnsur + ", Batalyon=" + Batalyon + ", id=" + id + "]";
	}

	public String getId_str() {
		return id_str;
	}

	public void setId_str(String id_str) {
		this.id_str = id_str;
	}

	public String getThn() {
		return thn;
	}

	public void setThn(String thn) {
		this.thn = thn;
	}

	public String getTw() {
		return tw;
	}

	public void setTw(String tw) {
		this.tw = tw;
	}

	public String getKetwaktu() {
		return ketwaktu;
	}

	public void setKetwaktu(String ketwaktu) {
		this.ketwaktu = ketwaktu;
	}

	public String getTHNACT() {
		return THNACT;
	}

	public void setTHNACT(String tHNACT) {
		THNACT = tHNACT;
	}

	public String getTWACT() {
		return TWACT;
	}

	public void setTWACT(String tWACT) {
		TWACT = tWACT;
	}

	public String getKETWAKTUACT() {
		return KETWAKTUACT;
	}

	public void setKETWAKTUACT(String kETWAKTUACT) {
		KETWAKTUACT = kETWAKTUACT;
	}

	public String getKOTAMAOPS() {
		return KOTAMAOPS;
	}

	public void setKOTAMAOPS(String kOTAMAOPS) {
		KOTAMAOPS = kOTAMAOPS;
	}

	public String getGPS() {
		return GPS;
	}

	public void setGPS(String gPS) {
		GPS = gPS;
	}

	public String getBujur() {
		return bujur;
	}

	public void setBujur(String bujur) {
		this.bujur = bujur;
	}

	public String getKV() {
		return KV;
	}

	public void setKV(String kV) {
		KV = kV;
	}

	public String getKOORDINAT() {
		return KOORDINAT;
	}

	public void setKOORDINAT(String kOORDINAT) {
		KOORDINAT = kOORDINAT;
	}

	public String getPROPINSI() {
		return PROPINSI;
	}

	public void setPROPINSI(String pROPINSI) {
		PROPINSI = pROPINSI;
	}

	public String getKABKOTA() {
		return KABKOTA;
	}

	public void setKABKOTA(String kABKOTA) {
		KABKOTA = kABKOTA;
	}

	public String getKECAMATAN() {
		return KECAMATAN;
	}

	public void setKECAMATAN(String kECAMATAN) {
		KECAMATAN = kECAMATAN;
	}

	public String getKELURAHAN() {
		return KELURAHAN;
	}

	public void setKELURAHAN(String kELURAHAN) {
		KELURAHAN = kELURAHAN;
	}

	public String getKAMPUNG() {
		return KAMPUNG;
	}

	public void setKAMPUNG(String kAMPUNG) {
		KAMPUNG = kAMPUNG;
	}

	public String getKLASIFIKASI() {
		return KLASIFIKASI;
	}

	public void setKLASIFIKASI(String kLASIFIKASI) {
		KLASIFIKASI = kLASIFIKASI;
	}

	public String getMOTIF() {
		return MOTIF;
	}

	public void setMOTIF(String mOTIF) {
		MOTIF = mOTIF;
	}

	public String getPELAKU() {
		return PELAKU;
	}

	public void setPELAKU(String pELAKU) {
		PELAKU = pELAKU;
	}

	public String getKETPELAKU() {
		return KETPELAKU;
	}

	public void setKETPELAKU(String kETPELAKU) {
		KETPELAKU = kETPELAKU;
	}

	public String getSASARAN() {
		return SASARAN;
	}

	public void setSASARAN(String sASARAN) {
		SASARAN = sASARAN;
	}

	public String getSID() {
		return SID;
	}

	public void setSID(String sID) {
		SID = sID;
	}

	public String getSSID() {
		return SSID;
	}

	public void setSSID(String sSID) {
		SSID = sSID;
	}

	public String getJALAN() {
		return JALAN;
	}

	public void setJALAN(String jALAN) {
		JALAN = jALAN;
	}

	public String getKOLAK() {
		return KOLAK;
	}

	public void setKOLAK(String kOLAK) {
		KOLAK = kOLAK;
	}

	public String getPERAIRAN() {
		return PERAIRAN;
	}

	public void setPERAIRAN(String pERAIRAN) {
		PERAIRAN = pERAIRAN;
	}

	public String getId_tkp() {
		return id_tkp;
	}

	public void setId_tkp(String id_tkp) {
		this.id_tkp = id_tkp;
	}

	public Date getTimeStamp() {
		return TimeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		TimeStamp = timeStamp;
	}

	public String getSatuanUnsur() {
		return SatuanUnsur;
	}

	public void setSatuanUnsur(String satuanUnsur) {
		SatuanUnsur = satuanUnsur;
	}

	public String getBatalyon() {
		return Batalyon;
	}

	public void setBatalyon(String batalyon) {
		Batalyon = batalyon;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTransfferedAt() {
		return transfferedAt;
	}

	public void setTransfferedAt(Date transfferedAt) {
		this.transfferedAt = transfferedAt;
	}

	public Kejadian getKejadian() {
		return kejadian;
	}

	public void setKejadian(Kejadian kejadian) {
		this.kejadian = kejadian;
	}
	
}
