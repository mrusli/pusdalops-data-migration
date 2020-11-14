package mil.pusdalops.k2.webui.migrasi;

import java.util.Collections;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.gmt.TimezoneInd;
import mil.pusdalops.domain.kerugian.KerugianJenis;
import mil.pusdalops.domain.kerugian.KerugianKondisi;
import mil.pusdalops.domain.kerugian.KerugianSatuan;
import mil.pusdalops.domain.kerugian.Lembaga;
import mil.pusdalops.domain.kerugian.Pihak;
import mil.pusdalops.domain.kerugian.TipeKerugian;
import mil.pusdalops.domain.settings.Settings;
import mil.pusdalops.k2.domain.sql.Kerugian;
import mil.pusdalops.k2.domain.sql.Tkp;
import mil.pusdalops.k2.persistence.kerugian.jenis.dao.KerugianJenisDao;
import mil.pusdalops.k2.persistence.kerugian.kondisi.dao.KerugianKondisiDao;
import mil.pusdalops.k2.persistence.kerugian.satuan.dao.KerugianSatuanDao;
import mil.pusdalops.k2.webui.common.GFCBaseController;

public class KejadianKerugianMigrasiDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8461579848283292163L;

	private KerugianJenisDao kerugianJenisDao;
	private KerugianKondisiDao kerugianKondisiDao;
	private KerugianSatuanDao kerugianSatuanDao;
	
	private Window kejadianKerugianMigrasiDialogWin;
	private Textbox kejadianIdTextbox, namaPersMatTextbox,
		keteranganTextbox;
	private Combobox paraPihakCombobox, kerugianJenisCombobox, 
		lembagaCombobox, kondisiCombobox, satuanCombobox, 
		tipeKerugianCombobox;
	private Intbox jumlahIntbox;
	
	private Settings currentSettings;
	private KejadianKerugianData kejadianKerugianData;
	private Tkp sqlTkp;
	private Kerugian sqlKerugian;
	private Pihak kerugianPihak;
	private KerugianJenis kerugianJenis;
	private Lembaga lembaga;
	private KerugianKondisi kerugianKondisi;
	private KerugianSatuan kerugianSatuan;
	private TipeKerugian kerugianTipe;
	private String namaMatPersonil;
	private int jumlah;
	private String keterangan;
	
	private final Logger log = Logger.getLogger(KejadianKerugianMigrasiDialogControl.class);
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setKejadianKerugianData(
				(KejadianKerugianData) arg.get("kejadianKerugianData"));
	}

	public void onCreate$kejadianKerugianMigrasiDialogWin(Event event) throws Exception {
		// tkp
		setSqlTkp(
				getKejadianKerugianData().getSqlTkp());
		// sqlKerugian
		setSqlKerugian(
				getKejadianKerugianData().getSqlKerugian());
		// currentSettings
		setCurrentSettings(
				getKejadianKerugianData().getCurrentSettings());
		
		setKerugianPihak(convertSqlPihak(getSqlKerugian().getPihak()));
		setKerugianJenis(convertSqlJenis(getSqlKerugian().getJenis()));
		setKerugianTipe(convertFromSqlJenis());
		setLembaga(convertSqlPihakId(getSqlKerugian().getPihak_id()));
		setKerugianKondisi(convertSqlKondisi(getSqlKerugian().getKondisi()));
		setKerugianSatuan(convertSqlSatuan(getSqlKerugian().getSatuan()));

		setNamaMatPersonil(getSqlKerugian().getNama_obj());
		setJumlah(getSqlKerugian().getJumlah());
		setKeterangan(getSqlKerugian().getKeterangan());
		
		displaySqlKerugianToKerugian();
	}

	private Pihak convertSqlPihak(String sqlPihak) {
		int i=0;
		for (Pihak pihak : Pihak.values()) {
			if (pihak.toString(i).compareTo(sqlPihak)==0) {
				return pihak;
			}
			i++;
		}
		
		log.info("convertSqlPihak : Pihak not found...");
		return null;
	}

	private KerugianJenis convertSqlJenis(String sqlJenis) throws Exception {
		KerugianJenis kerugianJenis = getKerugianJenisDao().findKerugianJenisByName(sqlJenis);
		
		if (kerugianJenis==null) {
			log.info("convertSQLJenis : Jenis not found...");
		}
		
		return kerugianJenis;
	}	

	private TipeKerugian convertFromSqlJenis() {
		TipeKerugian tipeKerugian = null;
		
		if (getKerugianJenis()==null) {
			log.info("convertFromSqlJenis : Tipe kerugian not found...");
		} else {
			tipeKerugian = getKerugianJenis().getTipeKerugian();
		}
		
		return tipeKerugian;
	}	
	
	private Lembaga convertSqlPihakId(String sqlPihakId) {
		for (Lembaga lembaga : Lembaga.values()) {
			if (lembaga.toString().compareTo(sqlPihakId)==0) {
				return lembaga;
			}
		}
		
		log.info("convertSqlPihakId : Pihak_ID not found...");
		return null;
	}
	
	private KerugianKondisi convertSqlKondisi(String sqlKondisi) throws Exception {
		KerugianKondisi kerugianKondisi = getKerugianKondisiDao().findKerugianKondisiByName(sqlKondisi); 
		
		if (kerugianKondisi==null) {	
			log.info("covertSqlKondisi : Kondisi not found...");
		}
		
		return kerugianKondisi;
	}

	private KerugianSatuan convertSqlSatuan(String sqlSatuan) throws Exception {
		KerugianSatuan kerugianSatuan = getKerugianSatuanDao().findKerugianSatuanByName(sqlSatuan);
		
		if (kerugianSatuan==null) {
			log.info("convertSqlSatuan : Satuan not found...");
		}
		
		return kerugianSatuan;
	}

	private void displaySqlKerugianToKerugian() {
		
		kejadianIdTextbox.setValue(getSqlKerugian().getId_str());
		
		// paraPihakCombobox
		Comboitem comboitem = null;
		if (getKerugianPihak()==null) {
			// user must choose
			for (Pihak pihak : Pihak.values()) {
				comboitem = new Comboitem();
				comboitem.setLabel(pihak.toString());
				comboitem.setValue(pihak);
				comboitem.setParent(paraPihakCombobox);
			}
			
		} else {
			comboitem = new Comboitem();
			comboitem.setLabel(getKerugianPihak().toString());
			comboitem.setValue(getKerugianPihak());
			comboitem.setParent(paraPihakCombobox);
			
			paraPihakCombobox.setSelectedItem(comboitem);
		}
		
		// namaPersMatTextbox
		namaPersMatTextbox.setValue(getNamaMatPersonil());
		
		// kerugianJenisCombobox
		if (getKerugianJenis()==null) {
			comboitem = new Comboitem();
			comboitem.setLabel("Buat Jenis Kerugian...");
			comboitem.setParent(kerugianJenisCombobox);
			
			kerugianJenisCombobox.setValue("--tidak ditemukan--");
		} else {
			comboitem = new Comboitem();
			comboitem.setLabel(getKerugianJenis().getNamaJenis());
			comboitem.setValue(getKerugianJenis());
			comboitem.setParent(kerugianJenisCombobox);
			
			kerugianJenisCombobox.setSelectedItem(comboitem);
		}
		
		// tipeKerugianCombobox -- from kerugianJenis
		if (getKerugianTipe()==null) {
			// user must choose
			for (TipeKerugian tipeKerugian : TipeKerugian.values()) {
				comboitem = new Comboitem();
				comboitem.setLabel(tipeKerugian.toString());
				comboitem.setValue(tipeKerugian);
				comboitem.setParent(tipeKerugianCombobox);
			}
			
		} else {
			comboitem = new Comboitem();
			comboitem.setLabel(getKerugianTipe().toString());
			comboitem.setValue(getKerugianTipe());
			comboitem.setParent(tipeKerugianCombobox);
			
			tipeKerugianCombobox.setSelectedItem(comboitem);
		}
		
		// lembagaCombobox
		if (getLembaga()==null) {
			// user must choose
			for (Lembaga lembaga : Lembaga.values()) {
				comboitem = new Comboitem();
				comboitem.setLabel(lembaga.toString());
				comboitem.setValue(lembaga);
				comboitem.setParent(lembagaCombobox);
			}
			
		} else {
			comboitem = new Comboitem();
			comboitem.setLabel(getLembaga().toString());
			comboitem.setValue(getLembaga());
			comboitem.setParent(lembagaCombobox);
			
			lembagaCombobox.setSelectedItem(comboitem);
		}
		
		// kondisiCombobox
		if (getKerugianKondisi()==null) {
			comboitem = new Comboitem();
			comboitem.setLabel("Buat Kondisi Kerugian...");
			comboitem.setParent(kondisiCombobox);
			
			kondisiCombobox.setValue("--tidak ditemukan--");
		} else {
			comboitem = new Comboitem();
			comboitem.setLabel(getKerugianKondisi().getNamaKondisi());
			comboitem.setValue(getKerugianKondisi());
			comboitem.setParent(kondisiCombobox);

			kondisiCombobox.setSelectedItem(comboitem);
		}
		
		// jumlahIntbox
		jumlahIntbox.setValue(getJumlah());
		
		// satuanCombobox
		if (getKerugianSatuan()==null) {
			comboitem = new Comboitem();
			comboitem.setLabel("Buat Satuan Kerugian...");
			comboitem.setParent(satuanCombobox);
			
			satuanCombobox.setValue("--tidak ditemukan--");
		} else {
			comboitem = new Comboitem();
			comboitem.setLabel(getKerugianSatuan().getSatuan());
			comboitem.setValue(getKerugianSatuan());
			comboitem.setParent(satuanCombobox);
			
			satuanCombobox.setSelectedItem(comboitem);
		}
		
		// keteranganTextbox
		keteranganTextbox.setValue(getKeterangan());
	}
		
	public void onSelect$kerugianJenisCombobox(Event event) throws Exception {
		KejadianKerugianJenisData kejadianKerugianJenisData = new KejadianKerugianJenisData();
		kejadianKerugianJenisData.setKerugianJenisList(getKerugianJenisDao().findAllKerugianJenis());
		kejadianKerugianJenisData.setSqlKerugianJenis(getSqlKerugian().getJenis());
		Map<String, KejadianKerugianJenisData> arg = Collections.singletonMap(
				"kejadianKerugianJenisData", kejadianKerugianJenisData);
		Window kerugianJenisListDialogWin = 
				(Window) Executions.createComponents(
						"/dialog/KerugianJenisListDialog.zul", 
						kejadianKerugianMigrasiDialogWin, arg);
		kerugianJenisListDialogWin.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				KerugianJenis kerugianJenis = (KerugianJenis) event.getData();
				// current settings kotamatops current settings
				TimezoneInd timezoneInd = getCurrentSettings().getSelectedKotamaops().getTimeZone();
				int timezoneIndOrdinal = timezoneInd.ordinal();
				// set datetime
				kerugianJenis.setCreatedAt(asDate(getLocalDateTime(timezoneInd.toZoneId(timezoneIndOrdinal))));
				kerugianJenis.setEditedAt(asDate(getLocalDateTime(timezoneInd.toZoneId(timezoneIndOrdinal))));
				// save
				getKerugianJenisDao().save(kerugianJenis);
				
				Clients.showNotification("Jenis Kerugian berhasil ditambahkan.");

				// clear all comboitems
				kerugianJenisCombobox.getItems().clear();
				// set the value
				Comboitem comboitem = new Comboitem();
				comboitem.setLabel(kerugianJenis.getNamaJenis());
				comboitem.setValue(kerugianJenis);
				comboitem.setParent(kerugianJenisCombobox);
				//
				kerugianJenisCombobox.setSelectedItem(comboitem);
				// set TipeKerugian combobox				
				for (Comboitem comboitemTipeKerugian : tipeKerugianCombobox.getItems()) {
					if (comboitemTipeKerugian.getValue().equals(kerugianJenis.getTipeKerugian())) {
						tipeKerugianCombobox.setSelectedItem(comboitemTipeKerugian);
					}
				}
				//
				setKerugianJenis(kerugianJenis);
				//
				setKerugianTipe(kerugianJenis.getTipeKerugian());
			}
		});
		kerugianJenisListDialogWin.addEventListener(Events.ON_OK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				KerugianJenis kerugianJenis = (KerugianJenis) event.getData();

				// clear all comboitems
				kerugianJenisCombobox.getItems().clear();
				// set the value
				Comboitem comboitem = new Comboitem();
				comboitem.setLabel(kerugianJenis.getNamaJenis());
				comboitem.setValue(kerugianJenis);
				comboitem.setParent(kerugianJenisCombobox);
				//
				kerugianJenisCombobox.setSelectedItem(comboitem);
				//
				setKerugianJenis(kerugianJenis);
			}
		});
		kerugianJenisListDialogWin.doModal();
	}
	
	public void onSelect$kondisiCombobox(Event event) throws Exception {
		KejadianKerugianKondisiData kejadianKerugianKondisiData = new KejadianKerugianKondisiData();
		kejadianKerugianKondisiData.setKerugianKondisiList(getKerugianKondisiDao().findAllKerugianKondisi());
		kejadianKerugianKondisiData.setSqlKerugianKondisi(getSqlKerugian().getKondisi());
		Map<String, KejadianKerugianKondisiData> arg = 
				Collections.singletonMap("kejadianKerugianKondisiData", kejadianKerugianKondisiData);
		Window kerugianKondisiListDialogWin = 
				(Window) Executions.createComponents("/dialog/KerugianKondisiListDialog.zul", kejadianKerugianMigrasiDialogWin, arg);
		kerugianKondisiListDialogWin.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				KerugianKondisi kerugianKondisi = (KerugianKondisi) event.getData();
				// current settings kotamatops current settings
				TimezoneInd timezoneInd = getCurrentSettings().getSelectedKotamaops().getTimeZone();
				int timezoneIndOrdinal = timezoneInd.ordinal();
				// set datetime
				kerugianKondisi.setCreatedAt(asDate(getLocalDateTime(timezoneInd.toZoneId(timezoneIndOrdinal))));
				kerugianKondisi.setEditedAt(asDate(getLocalDateTime(timezoneInd.toZoneId(timezoneIndOrdinal))));
				// save
				getKerugianKondisiDao().save(kerugianKondisi);
				
				Clients.showNotification("Kondisi Kerugian berhasil ditambahkan.");
				
				// clear all comboitems
				kondisiCombobox.getItems().clear();
				// set the value
				Comboitem comboitem = new Comboitem();
				comboitem.setLabel(kerugianKondisi.getNamaKondisi());
				comboitem.setValue(kerugianKondisi);
				comboitem.setParent(kondisiCombobox);
				// set selected
				kondisiCombobox.setSelectedItem(comboitem);
				// set
				setKerugianKondisi(kerugianKondisi);
			}
		});
		kerugianKondisiListDialogWin.addEventListener(Events.ON_OK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				KerugianKondisi kerugianKondisi = (KerugianKondisi) event.getData();

				// clear all comboitems
				kondisiCombobox.getItems().clear();
				// set the value
				Comboitem comboitem = new Comboitem();
				comboitem.setLabel(kerugianKondisi.getNamaKondisi());
				comboitem.setValue(kerugianKondisi);
				comboitem.setParent(kondisiCombobox);
				// set selected
				kondisiCombobox.setSelectedItem(comboitem);
				// set
				setKerugianKondisi(kerugianKondisi);
			}
		});
		
		
		kerugianKondisiListDialogWin.doModal();
	}
	
	public void onSelect$satuanCombobox(Event event) throws Exception {
		KejadianKerugianSatuanData kejadianKerugianSatuanData = new KejadianKerugianSatuanData();
		kejadianKerugianSatuanData.setKerugianSatuanList(getKerugianSatuanDao().findAllKerugianSatuan());
		kejadianKerugianSatuanData.setSqlKerugianSatuan(getSqlKerugian().getSatuan());
		Map<String, KejadianKerugianSatuanData> arg = 
				Collections.singletonMap("kejadianKerugianSatuanData", kejadianKerugianSatuanData);
		Window kerugianSatuanListDialogWin = 
				(Window) Executions.createComponents("/dialog/KerugianSatuanListDialog.zul", kejadianKerugianMigrasiDialogWin, arg);
		kerugianSatuanListDialogWin.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				KerugianSatuan kerugianSatuan = (KerugianSatuan) event.getData();
				// current settings kotamatops current settings
				TimezoneInd timezoneInd = getCurrentSettings().getSelectedKotamaops().getTimeZone();
				int timezoneIndOrdinal = timezoneInd.ordinal();
				// set datetime
				kerugianSatuan.setCreatedAt(asDate(getLocalDateTime(timezoneInd.toZoneId(timezoneIndOrdinal))));
				kerugianSatuan.setEditedAt(asDate(getLocalDateTime(timezoneInd.toZoneId(timezoneIndOrdinal))));
				// save
				getKerugianSatuanDao().save(kerugianSatuan);
				
				Clients.showNotification("Satuan Kerugian berhasil ditambahkan.");

				// clear all comboitems
				satuanCombobox.getItems().clear();
				// set the value
				Comboitem comboitem = new Comboitem();
				comboitem.setLabel(kerugianSatuan.getSatuan());
				comboitem.setValue(kerugianSatuan);
				comboitem.setParent(satuanCombobox);
				// set
				satuanCombobox.setSelectedItem(comboitem);
				// set
				setKerugianSatuan(kerugianSatuan);
			}
		});
		kerugianSatuanListDialogWin.addEventListener(Events.ON_OK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				KerugianSatuan kerugianSatuan = (KerugianSatuan) event.getData();
				
				// clear all comboitems
				satuanCombobox.getItems().clear();
				// set the value
				Comboitem comboitem = new Comboitem();
				comboitem.setLabel(kerugianSatuan.getSatuan());
				comboitem.setValue(kerugianSatuan);
				comboitem.setParent(satuanCombobox);
				// set
				satuanCombobox.setSelectedItem(comboitem);
				// set
				setKerugianSatuan(kerugianSatuan);				
			}
		});
		
		kerugianSatuanListDialogWin.doModal();
	}
	
	public void onClick$saveButton(Event event) throws Exception {
		mil.pusdalops.domain.kerugian.Kerugian kerugian = new mil.pusdalops.domain.kerugian.Kerugian();

		// current settings kotamatops current settings
		TimezoneInd timezoneInd = getCurrentSettings().getSelectedKotamaops().getTimeZone();
		int timezoneIndOrdinal = timezoneInd.ordinal();
		
		kerugian.setCreatedAt(asDate(getLocalDateTime(timezoneInd.toZoneId(timezoneIndOrdinal))));
		kerugian.setEditedAt(asDate(getLocalDateTime(timezoneInd.toZoneId(timezoneIndOrdinal))));
		kerugian.setNamaMaterial(namaPersMatTextbox.getValue());
		kerugian.setParaPihak(paraPihakCombobox.getSelectedItem().getValue());
		kerugian.setLembagaTerkait(lembagaCombobox.getSelectedItem().getValue());
		kerugian.setTipeKerugian(tipeKerugianCombobox.getSelectedItem().getValue());
		kerugian.setKerugianJenis(kerugianJenisCombobox.getSelectedItem().getValue());
		kerugian.setKerugianKondisi(kondisiCombobox.getSelectedItem().getValue());
		kerugian.setJumlah(jumlahIntbox.getValue());
		kerugian.setKerugianSatuan(satuanCombobox.getSelectedItem().getValue());
		kerugian.setKeterangan(keteranganTextbox.getValue());
		
		// send event
		Events.sendEvent(Events.ON_OK, kejadianKerugianMigrasiDialogWin, kerugian);
		
		// detach
		kejadianKerugianMigrasiDialogWin.detach();
	}
	
	public void onClick$cancelButton(Event event) throws Exception {
		
		kejadianKerugianMigrasiDialogWin.detach();
	}
	
	public KejadianKerugianData getKejadianKerugianData() {
		return kejadianKerugianData;
	}

	public void setKejadianKerugianData(KejadianKerugianData kejadianKerugianData) {
		this.kejadianKerugianData = kejadianKerugianData;
	}

	public Tkp getSqlTkp() {
		return sqlTkp;
	}

	public void setSqlTkp(Tkp sqlTkp) {
		this.sqlTkp = sqlTkp;
	}

	public mil.pusdalops.k2.domain.sql.Kerugian getSqlKerugian() {
		return sqlKerugian;
	}

	public void setSqlKerugian(mil.pusdalops.k2.domain.sql.Kerugian sqlKerugian) {
		this.sqlKerugian = sqlKerugian;
	}

	public Pihak getKerugianPihak() {
		return kerugianPihak;
	}

	public void setKerugianPihak(Pihak kerugianPihak) {
		this.kerugianPihak = kerugianPihak;
	}

	public KerugianJenis getKerugianJenis() {
		return kerugianJenis;
	}

	public void setKerugianJenis(KerugianJenis kerugianJenis) {
		this.kerugianJenis = kerugianJenis;
	}

	public KerugianJenisDao getKerugianJenisDao() {
		return kerugianJenisDao;
	}

	public void setKerugianJenisDao(KerugianJenisDao kerugianJenisDao) {
		this.kerugianJenisDao = kerugianJenisDao;
	}

	public Lembaga getLembaga() {
		return lembaga;
	}

	public void setLembaga(Lembaga lembaga) {
		this.lembaga = lembaga;
	}

	public KerugianKondisi getKerugianKondisi() {
		return kerugianKondisi;
	}

	public void setKerugianKondisi(KerugianKondisi kerugianKondisi) {
		this.kerugianKondisi = kerugianKondisi;
	}

	public KerugianKondisiDao getKerugianKondisiDao() {
		return kerugianKondisiDao;
	}

	public void setKerugianKondisiDao(KerugianKondisiDao kerugianKondisiDao) {
		this.kerugianKondisiDao = kerugianKondisiDao;
	}

	public KerugianSatuanDao getKerugianSatuanDao() {
		return kerugianSatuanDao;
	}

	public void setKerugianSatuanDao(KerugianSatuanDao kerugianSatuanDao) {
		this.kerugianSatuanDao = kerugianSatuanDao;
	}

	public KerugianSatuan getKerugianSatuan() {
		return kerugianSatuan;
	}

	public void setKerugianSatuan(KerugianSatuan kerugianSatuan) {
		this.kerugianSatuan = kerugianSatuan;
	}

	public String getNamaMatPersonil() {
		return namaMatPersonil;
	}

	public void setNamaMatPersonil(String namaMatPersonil) {
		this.namaMatPersonil = namaMatPersonil;
	}

	public TipeKerugian getKerugianTipe() {
		return kerugianTipe;
	}

	public void setKerugianTipe(TipeKerugian kerugianTipe) {
		this.kerugianTipe = kerugianTipe;
	}

	public int getJumlah() {
		return jumlah;
	}

	public void setJumlah(int jumlah) {
		this.jumlah = jumlah;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public Settings getCurrentSettings() {
		return currentSettings;
	}

	public void setCurrentSettings(Settings currentSettings) {
		this.currentSettings = currentSettings;
	}
}
