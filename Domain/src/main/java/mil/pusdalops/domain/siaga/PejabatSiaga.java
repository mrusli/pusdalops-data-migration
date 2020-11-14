package mil.pusdalops.domain.siaga;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import mil.pusdalops.domain.common.IdBasedObject;
import mil.pusdalops.domain.common.SchemaUtil;
import mil.pusdalops.domain.gmt.TimezoneInd;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.serial.DocumentSerialNumber;

@Entity
@Table(name = "pejabat_siaga", schema = SchemaUtil.SCHEMA_COMMON)
public class PejabatSiaga extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7794222280992873940L;

	//  `serial_number_id_fk` bigint(20) DEFAULT NULL,
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "serial_number_id_fk")	
	private DocumentSerialNumber serialNumber;
	
	//  `tw_siaga_awal` timestamp NULL DEFAULT NULL,
	@Column(name = "tw_siaga_awal")
	@Temporal(TemporalType.TIMESTAMP)
	private Date twSiagaAwal;
	
	//  `tahun_awal` char(4) DEFAULT NULL,
	@Column(name = "tahun_awal")
	private String tahunAwal;
	
	//  `tw_awal` char(9) DEFAULT NULL,
	@Column(name = "tw_awal")
	private String twAwal;
	
	//  `tw_siaga_akhir` timestamp NULL DEFAULT NULL,
	@Column(name = "tw_siaga_akhir")
	private Date twSiagaAkhir;
	
	//  `tahun_akhir` char(4) DEFAULT NULL,
	@Column(name = "tahun_akhir")
	private String tahunAkhir;
	
	//  `tw_akhir` char(9) DEFAULT NULL,
	@Column(name = "tw_akhir")
	private String twAkhir;
	
	//  `tw_timezone` int(11) DEFAULT NULL,
	@Column(name = "tw_timezone")
	@Enumerated(EnumType.ORDINAL)
	private TimezoneInd twTimezone;
	
	//  `kotamaops_id_fk` bigint(20) DEFAULT NULL,
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kotamaops_id_fk")
	private Kotamaops kotamaops;
	
	//  `nama` varchar(255) DEFAULT NULL,
	@Column(name = "nama")
	private String nama;
	
	//  `pangkat` varchar(255) DEFAULT NULL,
	@Column(name = "pangkat")
	private String pangkat;
	
	//  `jabatan` varchar(255) DEFAULT NULL,
	@Column(name = "jabatan")
	private String jabatan;
	
	//  `nrp` varchar(255) DEFAULT NULL,
	@Column(name = "nrp")
	private String nrp;
	
	/**
	 * @return the serialNumber
	 */
	public DocumentSerialNumber getSerialNumber() {
		return serialNumber;
	}

	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(DocumentSerialNumber serialNumber) {
		this.serialNumber = serialNumber;
	}

	/**
	 * @return the twSiagaAwal
	 */
	public Date getTwSiagaAwal() {
		return twSiagaAwal;
	}

	/**
	 * @param twSiagaAwal the twSiagaAwal to set
	 */
	public void setTwSiagaAwal(Date twSiagaAwal) {
		this.twSiagaAwal = twSiagaAwal;
	}

	/**
	 * @return the tahunAwal
	 */
	public String getTahunAwal() {
		return tahunAwal;
	}

	/**
	 * @param tahunAwal the tahunAwal to set
	 */
	public void setTahunAwal(String tahunAwal) {
		this.tahunAwal = tahunAwal;
	}

	/**
	 * @return the twAwal
	 */
	public String getTwAwal() {
		return twAwal;
	}

	/**
	 * @param twAwal the twAwal to set
	 */
	public void setTwAwal(String twAwal) {
		this.twAwal = twAwal;
	}

	/**
	 * @return the twSiagaAkhir
	 */
	public Date getTwSiagaAkhir() {
		return twSiagaAkhir;
	}

	/**
	 * @param twSiagaAkhir the twSiagaAkhir to set
	 */
	public void setTwSiagaAkhir(Date twSiagaAkhir) {
		this.twSiagaAkhir = twSiagaAkhir;
	}

	/**
	 * @return the tahunAkhir
	 */
	public String getTahunAkhir() {
		return tahunAkhir;
	}

	/**
	 * @param tahunAkhir the tahunAkhir to set
	 */
	public void setTahunAkhir(String tahunAkhir) {
		this.tahunAkhir = tahunAkhir;
	}

	/**
	 * @return the twAkhir
	 */
	public String getTwAkhir() {
		return twAkhir;
	}

	/**
	 * @param twAkhir the twAkhir to set
	 */
	public void setTwAkhir(String twAkhir) {
		this.twAkhir = twAkhir;
	}

	/**
	 * @return the kotamaops
	 */
	public Kotamaops getKotamaops() {
		return kotamaops;
	}

	/**
	 * @param kotamaops the kotamaops to set
	 */
	public void setKotamaops(Kotamaops kotamaops) {
		this.kotamaops = kotamaops;
	}

	/**
	 * @return the nama
	 */
	public String getNama() {
		return nama;
	}

	/**
	 * @param nama the nama to set
	 */
	public void setNama(String nama) {
		this.nama = nama;
	}

	/**
	 * @return the pangkat
	 */
	public String getPangkat() {
		return pangkat;
	}

	/**
	 * @param pangkat the pangkat to set
	 */
	public void setPangkat(String pangkat) {
		this.pangkat = pangkat;
	}

	/**
	 * @return the jabatan
	 */
	public String getJabatan() {
		return jabatan;
	}

	/**
	 * @param jabatan the jabatan to set
	 */
	public void setJabatan(String jabatan) {
		this.jabatan = jabatan;
	}

	/**
	 * @return the nrp
	 */
	public String getNrp() {
		return nrp;
	}

	/**
	 * @param nrp the nrp to set
	 */
	public void setNrp(String nrp) {
		this.nrp = nrp;
	}

	public TimezoneInd getTwTimezone() {
		return twTimezone;
	}

	public void setTwTimezone(TimezoneInd twTimezone) {
		this.twTimezone = twTimezone;
	}
}
