package mil.pusdalops.k2.webui.migrasi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.gmt.TimezoneInd;
import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.domain.kejadian.KejadianJenis;
import mil.pusdalops.domain.kejadian.KejadianMotif;
import mil.pusdalops.domain.kejadian.KejadianPelaku;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.serial.DocumentSerialNumber;
import mil.pusdalops.domain.settings.Settings;
import mil.pusdalops.domain.wilayah.Kabupaten_Kotamadya;
import mil.pusdalops.domain.wilayah.Kecamatan;
import mil.pusdalops.domain.wilayah.Kelurahan;
import mil.pusdalops.domain.wilayah.Propinsi;
import mil.pusdalops.k2.domain.sql.Kronologis;
import mil.pusdalops.k2.domain.sql.Tkp;
import mil.pusdalops.k2.persistence.kabupaten_kotamadya.dao.Kabupaten_KotamadyaDao;
import mil.pusdalops.k2.persistence.kecamatan.dao.KecamatanDao;
import mil.pusdalops.k2.persistence.kejadian.jenis.dao.KejadianJenisDao;
import mil.pusdalops.k2.persistence.kejadian.motif.dao.KejadianMotifDao;
import mil.pusdalops.k2.persistence.kejadian.pelaku.dao.KejadianPelakuDao;
import mil.pusdalops.k2.persistence.kelurahan.dao.KelurahanDao;
import mil.pusdalops.k2.persistence.kotamaops.dao.KotamaopsDao;
import mil.pusdalops.k2.persistence.propinsi.dao.PropinsiDao;
import mil.pusdalops.k2.persistence.sql.kronologis.dao.KronologisSqlDao;
import mil.pusdalops.k2.webui.common.GFCBaseController;
import mil.pusdalops.k2.webui.dialog.DatetimeData;
import mil.pusdalops.k2.webui.dialog.KabupatenData;
import mil.pusdalops.k2.webui.dialog.KecamatanData;
import mil.pusdalops.k2.webui.dialog.KejadianJenisData;
import mil.pusdalops.k2.webui.dialog.KejadianMotifData;
import mil.pusdalops.k2.webui.dialog.KejadianPelakuData;
import mil.pusdalops.k2.webui.dialog.KelurahanData;

public class KejadianMigrasiDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5917814230924667814L;

	private KotamaopsDao kotamaopsDao;
	private PropinsiDao propinsiDao;
	private Kabupaten_KotamadyaDao kabupaten_KotamadyaDao;
	private KecamatanDao kecamatanDao;
	private KelurahanDao kelurahanDao;
	private KronologisSqlDao kronologisSqlDao;
	private KejadianJenisDao kejadianJenisDao;
	private KejadianMotifDao kejadianMotifDao;
	private KejadianPelakuDao kejadianPelakuDao;
	
	private Window kejadianMigrasiDialogWin;
	private Textbox kejadianIdTextbox, twBuatTahunTextbox, twBuatTanggalJamTextbox,
		twJadiTahunTextbox, twJadiTanggalJamTextbox, twJadiTimeZoneTextbox, koordGpsTextbox,
		koordPetaTextbox, bujurLintangTextbox, kampungTextbox, jalanTextbox, ketPelakuTextbox,  
		sasaranTextbox,kronologisTextbox;
	private Combobox twBuatTimeZoneCombobox, kotamaopsCombobox, propinsiCombobox, kabupatenCombobox,
		kecamatanCombobox, kelurahanCombobox, jenisKejadianCombobox, motifKejadianCombobox, 
		pelakuKejadianCombobox;
	
	private Tkp tkp;
	private Settings currentSettings;
	private MigrasiData migrasiData;
	private Kotamaops kotamaops;
	private LocalDateTime currentLocalDateTime;
	
	private LocalDateTime twPembuatan;
	private LocalDateTime twKejadian;
	private TimezoneInd twPembuatanTimezone;
	private TimezoneInd twKejadianTimezone;
	private Kotamaops kejadianKotamaops;
	private Propinsi kejadianPropinsi;
	private Kabupaten_Kotamadya kejadianKabupatenKotamadya;
	private Kecamatan kejadianKecamatan;
	private Kelurahan kejadianKelurahan;
	private KejadianJenis kejadianJenis;
	private KejadianMotif kejadianMotif;
	private KejadianPelaku kejadianPelaku;
	private Kronologis kejadianKronologis;
	
	private final Logger log = Logger.getLogger(KejadianMigrasiDialogControl.class);
	
	/**
	 *
	 */
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		// migrasiData
		setMigrasiData(
				(MigrasiData) Executions.getCurrent().getArg().get("migrasiData"));
		// set tkp
		setTkp(getMigrasiData().getTkp());
		// set settings
		setCurrentSettings(getMigrasiData().getCurrentSettings());
	}

	public void onCreate$kejadianMigrasiDialogWin(Event event) throws Exception {
		setKotamaops(
				getCurrentSettings().getSelectedKotamaops());
		
		TimezoneInd timezoneInd = getKotamaops().getTimeZone();
		int timezoneIndOrdinal = timezoneInd.ordinal();
		
		setCurrentLocalDateTime(
				getLocalDateTime(timezoneInd.toZoneId(timezoneIndOrdinal)));
		
		kejadianMigrasiDialogWin.setTitle("Migrasi TKP");
		
		setTwPembuatan(convertTkpTw(getTkp().getThn(), getTkp().getTWACT()));
		setTwPembuatanTimezone(convertTkpTimezone(getTkp().getKetwaktu()));
		setTwKejadian(convertTkpTw(getTkp().getThn(), getTkp().getTWACT()));
		setTwKejadianTimezone(convertTkpTimezone(getTkp().getKETWAKTUACT()));
		setKejadianKotamaops(convertTkpKotamaops(getTkp().getKOTAMAOPS()));
		setKejadianPropinsi(convertTkpPropinsi(getTkp().getPROPINSI()));
		setKejadianKabupatenKotamadya(convertTkpKabupatenKotamadya(getTkp().getKABKOTA()));
		setKejadianKecamatan(convertTkpKecamatan(getTkp().getKECAMATAN()));
		setKejadianKelurahan(covertTkpKelurahan(getTkp().getKELURAHAN()));
		setKejadianJenis(convertTkpKejadianJenis(getTkp().getKLASIFIKASI()));
		setKejadianMotif(convertTkpKejadianMotif(getTkp().getMOTIF()));
		setKejadianPelaku(convertTkpKejadianPelaku(getTkp().getPELAKU()));
		
		setKejadianKronologis(
				getKronologisSqlDao().findKronologisByIdStr(getTkp().getId_str()));
		
		displayTkpToKejadian();				
	}

	private LocalDateTime convertTkpTw(String thn, String tw) throws Exception {
		List<String> list = dateTimeSplit(isCharSufficient(tw));
		
		// get the monthdate and time as string values
		String monthDateStr = null;
		String timeStr = null;		
		try {
			monthDateStr = list.get(0);
			timeStr = list.get(1);			
		} catch (Exception e) {
			throw new Exception("Tw format: "+tw+" tidak sesuai.");
		}

		// split the month, day, hour and minutes
		String month = monthDateStr.substring(0, 2);
		String day = monthDateStr.substring(2, 4);		
		
		String hour = timeStr.substring(0, 2);
		String mint = timeStr.substring(2, 4);

		LocalDate localdate = constructLocalDate(thn, month, day);
		
		LocalTime localtime = constructLocalTime(hour, mint);		

		return LocalDateTime.of(localdate, localtime);
	}

	private LocalTime constructLocalTime(String hour, String mint) throws Exception {
		// construct a LocalTime object
		LocalTime localtime = null;
		try {
			localtime = LocalTime.of(Integer.valueOf(hour), Integer.valueOf(mint));			
		} catch (Exception e) {
			throw new Exception("Jam: "+
					hour+":"+
					mint+
					" tidak sesuai. ID:"+getTkp().getId()+"/"+getTkp().getId_str());			
		}
		return localtime;
	}

	private LocalDate constructLocalDate(String thn, String month, String day) throws Exception {
		// construct a LocalDate object
		LocalDate localdate = null;
		try {
			localdate = LocalDate.of(Integer.valueOf(thn), Integer.valueOf(month), Integer.valueOf(day));			
		} catch (Exception e) {
			throw new Exception("Tanggal: "+
					thn+"-"+
					month+"-"+
					day+"-"+
					" tidak sesuai. ID:"+getTkp().getId()+"/"+getTkp().getId_str());
		}
		return localdate;
	}

	private List<String> dateTimeSplit(String tw) {
		// split
		String[] datetime = tw.split("\\.");
		// convert to list
		List<String> list = Arrays.asList(datetime);

		return list;
	}

	private TimezoneInd convertTkpTimezone(String ketWaktu) {
		for (TimezoneInd timezoneInd : TimezoneInd.values()) {
			if (timezoneInd.toString().compareTo(ketWaktu)==0) {
				return timezoneInd.toTimezoneInd(timezoneInd.getValue());
			}
		}
		return null;
	}
	
	private Kotamaops convertTkpKotamaops(String kotamaopsName) throws Exception {
		Kotamaops kotamaops = getKotamaopsDao().findKotamaopsByName(kotamaopsName);
		
		if (kotamaops==null) {
			// kotamaops not found
			// System.out.println("kotamaops not found...");
			log.info("convertTkpKotamaops : Kotamaops not found...");
		}
		
		return kotamaops;
	}
	
	private Propinsi convertTkpPropinsi(String propinsiName) throws Exception {
		if (getKejadianKotamaops()==null) {
			
			return null;
		}
		Kotamaops kotamaopsByProxy = 
				getKotamaopsDao().findKotamaopsPropinsiByProxy(getKejadianKotamaops().getId());
		
		for (Propinsi propinsi : kotamaopsByProxy.getPropinsis()) {
			if (propinsi.getNamaPropinsi().compareTo(propinsiName)==0) {
				return propinsi;
			}
		}
		
		// System.out.println("Propinsis not found...");
		log.info("convertTkpPropinsi : Propinsi not found...");
		
		return null;
	}	
	
	private Kabupaten_Kotamadya convertTkpKabupatenKotamadya(String tkpKabKota) throws Exception {
		if (getKejadianPropinsi()==null) {
			
			return null;
		}
		
		Propinsi propinsiByProxy = getPropinsiDao().findKabupatenKotamadyaByProxy(getKejadianPropinsi().getId());
		
		for (Kabupaten_Kotamadya kabupaten : propinsiByProxy.getKabupatenkotamadyas()) {
			if (kabupaten.getNamaKabupaten().compareTo(tkpKabKota)==0) {
				return kabupaten;
			}
		}

		// System.out.println("Kabupaten_Kotamadya not found...");
		log.info("convertTkpKabupatenKotamadya : Kabupaten_Kotamadya not found...");
		
		return null;
	}	

	private Kecamatan convertTkpKecamatan(String tkpKecamatan) throws Exception {
		if (getKejadianKabupatenKotamadya()==null) {
			
			return null;
		}
		
		Kabupaten_Kotamadya kabupatenKotByProxy = getKabupaten_KotamadyaDao().findKecamatanByProxy(getKejadianKabupatenKotamadya().getId());
		
		for (Kecamatan kecamatan : kabupatenKotByProxy.getKecamatans()) {
			if (kecamatan.getNamaKecamatan().compareTo(tkpKecamatan)==0) {
				return kecamatan;
			}
		}
		
		// System.out.println("Kecamatan not found...");
		log.info("convertTkpKecamanta : Kecamatan not found...");
		
		return null;
	}
	
	private Kelurahan covertTkpKelurahan(String tkpKelurahan) throws Exception {
		if (getKejadianKecamatan()==null) {
			
			return null;
		}
		
		Kecamatan kecamatanByProxy = getKecamatanDao().findKelurahanByProxy(getKejadianKecamatan().getId());
		
		for (Kelurahan kelurahan : kecamatanByProxy.getKelurahans()) {
			if (kelurahan.getNamaKelurahan().compareTo(tkpKelurahan)==0) {
				return kelurahan;
			}
		}
		
		// System.out.println("Kelurahan not found...");
		log.info("convertTkpKelurahan : Kelurahan not found...");
		
		return null;
	}

	private KejadianJenis convertTkpKejadianJenis(String kejadianJenisName) throws Exception {
		KejadianJenis kejadianJenis = getKejadianJenisDao().findKejadianJenisByName(kejadianJenisName);
		
		if (kejadianJenis==null) {
			log.info("convertTkpKejadianJenis : Kejadian / Klasifikasi not found...");
		}
		
		return kejadianJenis;
	}
	
	private KejadianMotif convertTkpKejadianMotif(String kejadianMotifName) throws Exception {
		KejadianMotif kejadianMotif = getKejadianMotifDao().findKejadianMotifByName(kejadianMotifName);
		
		if (kejadianMotif==null) {
			log.info("convertTkpKejadianMotif : KejadianMotif not found...");
		}
		
		return kejadianMotif;
	}
	
	private KejadianPelaku convertTkpKejadianPelaku(String kejadianPelakuName) throws Exception {
		KejadianPelaku kejadianPelaku = getKejadianPelakuDao().findKejadianPelakuByName(kejadianPelakuName);
		
		if (kejadianPelaku==null) {
			log.info("convertTkpKejadianPelaku : KejadianPelaku not found...");
		}
		
		return kejadianPelaku;
	}	
	
	private String isCharSufficient(String tw) {
		// is tw 9 char long?
		if (tw.length()<9) {
			// append '0' in frong
			tw = '0'+tw;
		} else {
			// do nothing
		}

		return tw;
	}

	private void displayTkpToKejadian() {
		// id
		kejadianIdTextbox.setValue(getTkp().getId_str());
		// tw buat
		twBuatTahunTextbox.setValue(getTkp().getThn());
		twBuatTanggalJamTextbox.setValue(isCharSufficient(getTkp().getTw()));
		Comboitem comboitem = null;
		for (TimezoneInd timezoneInd : TimezoneInd.values()) {
			if (timezoneInd.toString().compareTo(getTkp().getKetwaktu())==0) {
				comboitem = new Comboitem();
				comboitem.setLabel(timezoneInd.toString());
				comboitem.setValue(timezoneInd);
				comboitem.setParent(twBuatTimeZoneCombobox);
				break;
			}
		}
		twBuatTimeZoneCombobox.setSelectedItem(comboitem);
		// tw kejadian
		twJadiTahunTextbox.setValue(getTkp().getTHNACT());
		twJadiTanggalJamTextbox.setValue(getTkp().getTWACT());
		for (TimezoneInd timezoneInd : TimezoneInd.values()) {
			if (timezoneInd.toString().compareTo(getTkp().getKETWAKTUACT())==0) {
				twJadiTimeZoneTextbox.setValue(timezoneInd.toString());
				twJadiTimeZoneTextbox.setAttribute("KETWAKTUACT", timezoneInd);
				break;
			}
		}
		
		// kotamaops
		if (getKejadianKotamaops()==null) {
			comboitem = new Comboitem();
			comboitem.setLabel("Pilih Kotamops...");
			comboitem.setParent(kotamaopsCombobox);
			
			kotamaopsCombobox.setValue("--tidak ditemukan--");
		} else {
			comboitem = new Comboitem();
			comboitem.setLabel(getKejadianKotamaops().getKotamaopsName());
			comboitem.setValue(getKejadianKotamaops());
			comboitem.setParent(kotamaopsCombobox);
			
			kotamaopsCombobox.setSelectedItem(comboitem);
		}
		
		// propinsi
		if (getKejadianPropinsi()==null) {
			comboitem = new Comboitem();
			comboitem.setLabel("Tambah Propinsi...");
			comboitem.setParent(propinsiCombobox);
			
			propinsiCombobox.setValue("--tidak ditemukan--");
		} else {
			comboitem = new Comboitem();
			comboitem.setLabel(getKejadianPropinsi().getNamaPropinsi());
			comboitem.setValue(getKejadianPropinsi());
			comboitem.setParent(propinsiCombobox);
			
			propinsiCombobox.setSelectedItem(comboitem);
		}
		
		// kabupaten - kotamadya
		if (getKejadianKabupatenKotamadya()==null) {
			comboitem = new Comboitem();
			comboitem.setLabel("Tambah Kabupaten/Kot...");
			comboitem.setParent(kabupatenCombobox);
			
			kabupatenCombobox.setValue("--tidak ditemukan--");
		} else {
			comboitem = new Comboitem();
			comboitem.setLabel(getKejadianKabupatenKotamadya().getNamaKabupaten());
			comboitem.setValue(getKejadianKabupatenKotamadya());
			comboitem.setParent(kabupatenCombobox);
			
			kabupatenCombobox.setSelectedItem(comboitem);
		}
		
		// kecamatan
		if (getKejadianKecamatan()==null) {
			comboitem = new Comboitem();
			comboitem.setLabel("Tambah Kecamatan...");
			comboitem.setParent(kecamatanCombobox);
			
			kecamatanCombobox.setValue("--tidak ditemukan--");
		} else {
			comboitem = new Comboitem();
			comboitem.setLabel(getKejadianKecamatan().getNamaKecamatan());
			comboitem.setValue(getKejadianKecamatan());
			comboitem.setParent(kecamatanCombobox);
			
			kecamatanCombobox.setSelectedItem(comboitem);
		}
		
		// kelurahan
		if (getKejadianKelurahan()==null) {
			comboitem = new Comboitem();
			comboitem.setLabel("Tambah Kelurahan...");
			comboitem.setParent(kelurahanCombobox);
			
			kelurahanCombobox.setValue("--tidak ditemukan---");
		} else {
			comboitem = new Comboitem();
			comboitem.setLabel(getKejadianKelurahan().getNamaKelurahan());
			comboitem.setValue(getKejadianKelurahan());
			comboitem.setParent(kelurahanCombobox);
			
			kelurahanCombobox.setSelectedItem(comboitem);
		}
		
		koordGpsTextbox.setValue(getTkp().getGPS());
		koordPetaTextbox.setValue(getTkp().getKOORDINAT());
		bujurLintangTextbox.setValue(getTkp().getBujur());
		
		kampungTextbox.setValue(getTkp().getKAMPUNG());
		jalanTextbox.setValue(getTkp().getJALAN());
		ketPelakuTextbox.setValue(getTkp().getPELAKU()+" - "+getTkp().getKETPELAKU());
		sasaranTextbox.setValue(getTkp().getSASARAN());
		
		if (getKejadianKronologis()!=null) {
			kronologisTextbox.setValue(getKejadianKronologis().getKronologis());			
		}
		
		// jenis kejadian
		if (getKejadianJenis()==null) {
			comboitem = new Comboitem();
			comboitem.setLabel("Tambah Jenis Kejadian");
			comboitem.setParent(jenisKejadianCombobox);
			
			jenisKejadianCombobox.setValue("--tidak ditemukan--");
		} else {
			comboitem = new Comboitem();
			comboitem.setLabel(getKejadianJenis().getNamaJenis());
			comboitem.setValue(getKejadianJenis());
			comboitem.setParent(jenisKejadianCombobox);
			
			jenisKejadianCombobox.setSelectedItem(comboitem);
		}
		
		// motif kejadian
		if (getKejadianMotif()==null) {
			comboitem = new Comboitem();
			comboitem.setLabel("Tambah Motif Kejadian");
			comboitem.setParent(motifKejadianCombobox);
			
			motifKejadianCombobox.setValue("--tidak ditemukan--");
		} else {
			comboitem = new Comboitem();
			comboitem.setLabel(getKejadianMotif().getNamaMotif());
			comboitem.setValue(getKejadianMotif());
			comboitem.setParent(motifKejadianCombobox);
			
			motifKejadianCombobox.setSelectedItem(comboitem);
		}
		
		// pelaku kejadian
		if (getKejadianPelaku()==null) {
			comboitem = new Comboitem();
			comboitem.setLabel("Tambah Pelaku Kejadian");
			comboitem.setParent(pelakuKejadianCombobox);
			
			pelakuKejadianCombobox.setValue("--tidak ditemukan--");
		} else {
			comboitem = new Comboitem();
			comboitem.setLabel(getKejadianPelaku().getNamaPelaku());
			comboitem.setValue(getKejadianPelaku());
			comboitem.setParent(pelakuKejadianCombobox);
			
			pelakuKejadianCombobox.setSelectedItem(comboitem);
		}
	}

	public void onClick$twJadiRubahButton(Event event) throws Exception {
		// create the data object
		DatetimeData datetimeData = new DatetimeData();
		// set the dialog title
		datetimeData.setDialogWinTitle("Rubah TW Kejadian");
		
		// set timezoneInd
		for (TimezoneInd timezoneInd : TimezoneInd.values()) {
			if (timezoneInd.toString().compareTo(getTkp().getKETWAKTUACT())==0) {
				datetimeData.setTimezoneInd(timezoneInd);
				datetimeData.setZoneId(timezoneInd.toZoneId(timezoneInd.ordinal()));
				
				break;
			}
		}
		// set localdatetime
		datetimeData.setLocalDateTime(convertTkpTw(getTkp().getTHNACT(), getTkp().getTWACT()));
		log.info("Assign to DatetimeData: "+datetimeData.toString());
		
		Map<String, DatetimeData> args = Collections.singletonMap("datetimeData", datetimeData);
		Window datetimeWin = (Window) Executions.createComponents(
				"/dialog/DatetimeWinDialog.zul", kejadianMigrasiDialogWin, args);
		datetimeWin.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				DatetimeData datetimeData = (DatetimeData) event.getData();
				log.info(datetimeData.toString());
				
				// year
				twJadiTahunTextbox.setValue(getLocalDateTimeString(datetimeData.getLocalDateTime(), "YYYY"));
				
				// MMdd.HHmm
				twJadiTanggalJamTextbox.setValue(
					getLocalDateTimeString(datetimeData.getLocalDateTime(), "MMdd")+"."+
					getLocalDateTimeString(datetimeData.getLocalDateTime(), "HHmm"));
				
				// zone waktu
				twJadiTimeZoneTextbox.setValue(datetimeData.getTimezoneInd().toString());
				// save the object in the attribute
				twJadiTimeZoneTextbox.setAttribute("twJadiTimeZoneInd", datetimeData.getTimezoneInd());
			}
		});
		
		datetimeWin.doModal();
	}
	
	public void onSelect$kotamaopsCombobox(Event event) throws Exception {
		Map<String, String> args = Collections.singletonMap("namaKotamaops", getTkp().getKOTAMAOPS());
		Window kotamaopsListWin = (Window) Executions.createComponents("/dialog/KotamaopsListDialog.zul", kejadianMigrasiDialogWin, args);
		/*
		 * KOTAMAOPS NOT ALLOWED TO ADD FROM HERE.  Add from Back-End Application.
		 * 
		 * kotamaopsListWin.addEventListener(Events.ON_CHANGE, new
		 * EventListener<Event>() {
		 * 
		 * @Override public void onEvent(Event event) throws Exception { Kotamaops
		 * selKotamaops = (Kotamaops) event.getData();
		 * 
		 * // kotamatops current settings TimezoneInd timezoneInd =
		 * getCurrentSettings().getSelectedKotamaops().getTimeZone(); int
		 * timezoneIndOrdinal = timezoneInd.ordinal(); // set dateitme create
		 * selKotamaops.setCreatedAt(asDate(getLocalDateTime(timezoneInd.toZoneId(
		 * timezoneIndOrdinal))));
		 * selKotamaops.setEditedAt(asDate(getLocalDateTime(timezoneInd.toZoneId(
		 * timezoneIndOrdinal)))); // save getKotamaopsDao().save(selKotamaops); //
		 * notif Clients.showNotification("Kotamaops berhasil ditambahkan"); // set
		 * setKejadianKotamaops(selKotamaops); // display
		 * kotamaopsCombobox.getItems().clear(); // set nama kotamaops Comboitem
		 * comboitem = new Comboitem();
		 * comboitem.setLabel(selKotamaops.getKotamaopsName());
		 * comboitem.setValue(selKotamaops); comboitem.setParent(kotamaopsCombobox);
		 * 
		 * // kotamaopsCombobox.setValue(selKotamaops.getKotamaopsName());
		 * kotamaopsCombobox.setSelectedItem(comboitem); } });
		 */
		kotamaopsListWin.addEventListener(Events.ON_OK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				Kotamaops existingKotamaops = (Kotamaops) event.getData();
				
				// clear all comboitems
				kotamaopsCombobox.getItems().clear();
				
				// set the nama kotamaops
				Comboitem comboitem = new Comboitem();
				comboitem.setLabel(existingKotamaops.getKotamaopsName());
				comboitem.setValue(existingKotamaops);
				comboitem.setParent(kotamaopsCombobox);
				
				kotamaopsCombobox.setSelectedItem(comboitem);
				
				// set kejadianKotamaops
				setKejadianKotamaops(existingKotamaops);
			}

		});
		kotamaopsListWin.doModal();
	}
		
	public void onSelect$propinsiCombobox(Event event) throws Exception {
		// in case
		if (getKejadianKotamaops()==null) {
			return;
		}
		// when user select this -- it means propinsi not found -- NO NEED TO CHECK FOR propinsi under the kejadian kotamaops
		// user MUST CHOOSE from the list of propinsi
		
		// display the list of all propinsi
		Map<String, String> arg = Collections.singletonMap("namaTkpPropinsi", getTkp().getPROPINSI());
		Window propinsiListWin = (Window) Executions.createComponents("/dialog/PropinsiListDialog.zul", kejadianMigrasiDialogWin, arg);
		propinsiListWin.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// user selects the propinsi
				Propinsi selPropinsi = (Propinsi) event.getData();
				
				Kotamaops kotamaopsByProxy = 
						getKotamaopsDao().findKotamaopsPropinsiByProxy(getKejadianKotamaops().getId());
				// check whether selPropinsi already in the list
				List<Propinsi> propinsiList = kotamaopsByProxy.getPropinsis();
				for (Propinsi propinsi : propinsiList) {
					if (propinsi.getNamaPropinsi().compareTo(selPropinsi.getNamaPropinsi())==0) {
						// selPropinsi already assigned to this kotamaops previously
						// -- just display
						log.info("Selected Propinsi already assigned to this kotamaops previously.");
						// clear all combo items
						propinsiCombobox.getItems().clear();
						// set the nama propinsi
						Comboitem comboitem = new Comboitem();
						comboitem.setLabel(selPropinsi.getNamaPropinsi());
						comboitem.setValue(selPropinsi);
						comboitem.setParent(propinsiCombobox);
						// propinsiCombobox.setValue(selPropinsi.getNamaPropinsi());
						propinsiCombobox.setSelectedItem(comboitem);						
						// set kejadian propinsi
						setKejadianPropinsi(selPropinsi);
						
						return;
					}
				}
				// assign the propinsi to the kejadian kotamaops				
				kotamaopsByProxy.getPropinsis().add(selPropinsi);
				// current settings kotamatops current settings
				TimezoneInd timezoneInd = getCurrentSettings().getSelectedKotamaops().getTimeZone();
				int timezoneIndOrdinal = timezoneInd.ordinal();
				// set edited date/time
				kotamaopsByProxy.setEditedAt(asDate(getLocalDateTime(timezoneInd.toZoneId(timezoneIndOrdinal))));				
				// update kotamaops				
				getKotamaopsDao().update(kotamaopsByProxy);
				
				Clients.showNotification("Propinsi berhasil ditambahkan untuk Kotamaops");
				
				// clear all combo items
				propinsiCombobox.getItems().clear();
				// set the nama propinsi
				Comboitem comboitem = new Comboitem();
				comboitem.setLabel(selPropinsi.getNamaPropinsi());
				comboitem.setValue(selPropinsi);
				comboitem.setParent(propinsiCombobox);
				
				// propinsiCombobox.setValue(selPropinsi.getNamaPropinsi());
				propinsiCombobox.setSelectedItem(comboitem);
				
				// set kejadian propinsi
				setKejadianPropinsi(selPropinsi);
			}
		});
		propinsiListWin.doModal();
	}
	

	
	public void onSelect$kabupatenCombobox(Event event) throws Exception {
		// in case
		if (getKejadianPropinsi()==null) {
			return;
		}
		// when user selects this, it means that the kabupaten not found in the kejadian propinsi
		// user will be given a list of kabupaten under the kejadian propinsi
		Propinsi propinsiByProxy = getPropinsiDao().findKabupatenKotamadyaByProxy(getKejadianPropinsi().getId());
		List<Kabupaten_Kotamadya> kabupatenKotList = propinsiByProxy.getKabupatenkotamadyas();
		// create the data
		KabupatenData kabupatenData = new KabupatenData();
		kabupatenData.setKabupatenKotList(kabupatenKotList);
		kabupatenData.setTkpKabupaten(getTkp().getKABKOTA());
		kabupatenData.setKejadianPropinsiName(getKejadianPropinsi().getNamaPropinsi());
		// pass into the dialog
		// user can choose from the list, or add to the list
		Map<String, KabupatenData> arg = Collections.singletonMap("kabupatenData", kabupatenData);
		Window kabupatenListWinDialog = (Window) Executions.createComponents(
				"/dialog/KabupatenListDialog.zul", kejadianMigrasiDialogWin, arg);
		kabupatenListWinDialog.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				Kabupaten_Kotamadya kabupatenKot = (Kabupaten_Kotamadya) event.getData();

				// current settings kotamatops current settings
				TimezoneInd timezoneInd = getCurrentSettings().getSelectedKotamaops().getTimeZone();
				int timezoneIndOrdinal = timezoneInd.ordinal();
				// set the timestamp for the kabupatenKot
				kabupatenKot.setCreatedAt(asDate(getLocalDateTime(timezoneInd.toZoneId(timezoneIndOrdinal))));
				kabupatenKot.setEditedAt(asDate(getLocalDateTime(timezoneInd.toZoneId(timezoneIndOrdinal))));
				// set the timestamp for the propinsi
				propinsiByProxy.setEditedAt(asDate(getLocalDateTime(timezoneInd.toZoneId(timezoneIndOrdinal))));
				// the selected kabupaten will be updated from kejadian propinsi
				propinsiByProxy.getKabupatenkotamadyas().add(kabupatenKot);
				// update
				getPropinsiDao().update(propinsiByProxy);
				
				Clients.showNotification("Kabupaten/Kotamadya berhasil ditambahkan untuk Propinsi");

				// clear all comboitems
				kabupatenCombobox.getItems().clear();
				// set the nama kabupaten
				Comboitem comboitem = new Comboitem();
				comboitem.setLabel(kabupatenKot.getNamaKabupaten());
				comboitem.setValue(kabupatenKot);
				comboitem.setParent(kabupatenCombobox);
				
				kabupatenCombobox.setSelectedItem(comboitem);
				
				// set kejadianKabupaten
				setKejadianKabupatenKotamadya(kabupatenKot);
			}
		});
		kabupatenListWinDialog.addEventListener(Events.ON_OK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				Kabupaten_Kotamadya existingKabupatenKot = (Kabupaten_Kotamadya) event.getData();
				
				// clear all comboitems
				kabupatenCombobox.getItems().clear();
				// set the nama kabupaten
				Comboitem comboitem = new Comboitem();
				comboitem.setLabel(existingKabupatenKot.getNamaKabupaten());
				comboitem.setValue(existingKabupatenKot);
				comboitem.setParent(kabupatenCombobox);
				
				kabupatenCombobox.setSelectedItem(comboitem);
				// set kejadianKabupaten
				setKejadianKabupatenKotamadya(existingKabupatenKot);
			}
		});
		kabupatenListWinDialog.addEventListener(Events.ON_CANCEL, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// clear all comboitems
				kabupatenCombobox.getItems().clear();
				
				Comboitem comboitem = new Comboitem();
				comboitem.setLabel("Tambah Kabupaten/Kot...");
				comboitem.setParent(kabupatenCombobox);
				
				kabupatenCombobox.setValue("--tidak ditemukan--");
			}
		});
		
		kabupatenListWinDialog.doModal();
	}
	
	public void onSelect$kecamatanCombobox(Event event) throws Exception {
		// in case
		if (getKejadianKabupatenKotamadya()==null) {
			return;
		}
		// when user selects this, it means that the kabupaten not found in the kejadian propinsi
		// user will be given a list of kabupaten under the kejadian kabupatenkotamadya
		Kabupaten_Kotamadya kabupatenKotByProxy = getKabupaten_KotamadyaDao().findKecamatanByProxy(getKejadianKabupatenKotamadya().getId());
		List<Kecamatan> kecamatanList = kabupatenKotByProxy.getKecamatans();
		// create the kecamatan data
		KecamatanData kecamatanData = new KecamatanData();
		kecamatanData.setKecamatanList(kecamatanList);
		kecamatanData.setTkpKecamatan(getTkp().getKECAMATAN());
		kecamatanData.setKejadianKabupatenKotName(getKejadianKabupatenKotamadya().getNamaKabupaten());
		// pass into the dialog
		// user can choose from the list, or add to the list
		Map<String, KecamatanData> arg = Collections.singletonMap("kecamatanData", kecamatanData);
		Window kecamatanListWinDialog = 
				(Window) Executions.createComponents("/dialog/KecamatanListDialog.zul", kejadianMigrasiDialogWin, arg);
		kecamatanListWinDialog.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				Kecamatan kecamatan = (Kecamatan) event.getData();
				
				// current settings kotamatops current settings
				TimezoneInd timezoneInd = getCurrentSettings().getSelectedKotamaops().getTimeZone();
				int timezoneIndOrdinal = timezoneInd.ordinal();
				// set the timestamp for the kecamatan
				kecamatan.setCreatedAt(asDate(getLocalDateTime(timezoneInd.toZoneId(timezoneIndOrdinal))));
				kecamatan.setEditedAt(asDate(getLocalDateTime(timezoneInd.toZoneId(timezoneIndOrdinal))));
				// set the timestamp for the kabupatenKotamadya
				kabupatenKotByProxy.setEditedAt(asDate(getLocalDateTime(timezoneInd.toZoneId(timezoneIndOrdinal))));
				// the selected kecamaan will be updated from kejadian kabupaten_kotamadya
				kabupatenKotByProxy.getKecamatans().add(kecamatan);
				// update
				getKabupaten_KotamadyaDao().update(kabupatenKotByProxy);
				
				Clients.showNotification("Kecamatan berhasil ditambahkan untuk Kabupaten/Kotamadya");
				
				// clear all comboitems
				kecamatanCombobox.getItems().clear();
				// set the nama kecamatan
				Comboitem comboitem = new Comboitem();
				comboitem.setLabel(kecamatan.getNamaKecamatan());
				comboitem.setValue(kecamatan);
				comboitem.setParent(kecamatanCombobox);
				
				kecamatanCombobox.setSelectedItem(comboitem);
				// set kejadianKecamatan
				setKejadianKecamatan(kecamatan);
			}
		});
		kecamatanListWinDialog.addEventListener(Events.ON_OK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				Kecamatan existingKecamatan = (Kecamatan) event.getData();

				// clear all comboitems
				kecamatanCombobox.getItems().clear();
				// set the nama kecamatan
				Comboitem comboitem = new Comboitem();
				comboitem.setLabel(existingKecamatan.getNamaKecamatan());
				comboitem.setValue(existingKecamatan);
				comboitem.setParent(kecamatanCombobox);
				
				kecamatanCombobox.setSelectedItem(comboitem);
				// set kejadianKecamatan
				setKejadianKecamatan(existingKecamatan);
			}
		});
		kecamatanListWinDialog.addEventListener(Events.ON_CANCEL, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// clear all comboitems
				kecamatanCombobox.getItems().clear();

				Comboitem comboitem = new Comboitem();
				comboitem.setLabel("Tambah Kecamatan...");
				comboitem.setParent(kecamatanCombobox);
				
				kecamatanCombobox.setValue("--tidak ditemukan--");
			}
		});
		
		kecamatanListWinDialog.doModal();
	}
	
	public void onSelect$kelurahanCombobox(Event event) throws Exception {
		// in case
		if (getKejadianKecamatan()==null) {
			return;
		}
		// when user selects this, it means that the kelurahan not found in the kejadian propinsi
		// user will be given a list of kelurahan under the kejadian kecamatan
		Kecamatan kecamatanByProxy = getKecamatanDao().findKelurahanByProxy(getKejadianKecamatan().getId());
		List<Kelurahan> kelurahanList = kecamatanByProxy.getKelurahans();
		// create the kelurahan data
		KelurahanData kelurahanData = new KelurahanData();
		kelurahanData.setKelurahanList(kelurahanList);
		kelurahanData.setTkpKelurahan(getTkp().getKELURAHAN());
		kelurahanData.setKejadianKecamatanName(getKejadianKecamatan().getNamaKecamatan());
		// pass into the dialog
		// user can choose from the list, or add to the list
		Map<String, KelurahanData> arg = Collections.singletonMap("kelurahanData", kelurahanData);
		Window kelurahanListWinDialog = 
				(Window) Executions.createComponents("/dialog/KelurahanListDialog.zul", kejadianMigrasiDialogWin, arg);
		kelurahanListWinDialog.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				Kelurahan kelurahan = (Kelurahan) event.getData();

				// current settings kotamatops current settings
				TimezoneInd timezoneInd = getCurrentSettings().getSelectedKotamaops().getTimeZone();
				int timezoneIndOrdinal = timezoneInd.ordinal();
				// set the timestamp for the kelurahan
				kelurahan.setCreatedAt(asDate(getLocalDateTime(timezoneInd.toZoneId(timezoneIndOrdinal))));
				kelurahan.setEditedAt(asDate(getLocalDateTime(timezoneInd.toZoneId(timezoneIndOrdinal))));
				// set the timestamp for the kecamatanByProxy
				kecamatanByProxy.setEditedAt(asDate(getLocalDateTime(timezoneInd.toZoneId(timezoneIndOrdinal))));
				// the selected kelurahan will be updated from kejadian kecamatan
				kecamatanByProxy.getKelurahans().add(kelurahan);
				// update
				getKecamatanDao().update(kecamatanByProxy);
				
				Clients.showNotification("Kelurahan berhasil ditambahkan untuk Kecamatan");

				// clear all comboitem
				kelurahanCombobox.getItems().clear();
				// set the value
				Comboitem comboitem = new Comboitem();
				comboitem.setLabel(kelurahan.getNamaKelurahan());
				comboitem.setValue(kelurahan);
				comboitem.setParent(kelurahanCombobox);
				
				kelurahanCombobox.setSelectedItem(comboitem);
				// set
				setKejadianKelurahan(kelurahan);
			}
		});
		kelurahanListWinDialog.addEventListener(Events.ON_OK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				Kelurahan existingKelurahan = (Kelurahan) event.getData();
				
				// clear all comboitem
				kelurahanCombobox.getItems().clear();
				// set the value
				Comboitem comboitem = new Comboitem();
				comboitem.setLabel(existingKelurahan.getNamaKelurahan());
				comboitem.setValue(existingKelurahan);
				comboitem.setParent(kelurahanCombobox);
				
				kelurahanCombobox.setSelectedItem(comboitem);
				// set
				setKejadianKelurahan(existingKelurahan);				
			}
		});
		kelurahanListWinDialog.addEventListener(Events.ON_CANCEL, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// clear all comboitem
				kelurahanCombobox.getItems().clear();
				
				Comboitem comboitem = new Comboitem();
				
				comboitem.setLabel("Tambah Kelurahan...");
				comboitem.setParent(kelurahanCombobox);
				
				kelurahanCombobox.setValue("--tidak ditemukan---");				
			}
		});
		
		kelurahanListWinDialog.doModal();
	}
	
	public void onSelect$jenisKejadianCombobox(Event event) throws Exception {
		List<KejadianJenis> kejadianJenisList = 
				getKejadianJenisDao().findAllKejadianJenis();
		KejadianJenisData kejadianJenisData = new KejadianJenisData();
		kejadianJenisData.setKejadianJenisList(kejadianJenisList);
		kejadianJenisData.setTkpKejadianJenis(getTkp().getKLASIFIKASI());
		// display a dialog list
		Map<String, KejadianJenisData> arg = Collections.singletonMap("kejadianJenisData", kejadianJenisData);
		Window kejadianJenisListWinDialog = 
				(Window) Executions.createComponents(
						"/dialog/KejadianJenisListDialog.zul", kejadianMigrasiDialogWin, arg);
		kejadianJenisListWinDialog.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				KejadianJenis kejadianJenis = (KejadianJenis) event.getData();				
				// current settings kotamatops current settings
				TimezoneInd timezoneInd = getCurrentSettings().getSelectedKotamaops().getTimeZone();
				int timezoneIndOrdinal = timezoneInd.ordinal();
				// set the date for the new object
				kejadianJenis.setCreatedAt(asDate(getLocalDateTime(timezoneInd.toZoneId(timezoneIndOrdinal))));
				kejadianJenis.setEditedAt(asDate(getLocalDateTime(timezoneInd.toZoneId(timezoneIndOrdinal))));
				// save
				getKejadianJenisDao().save(kejadianJenis);
				
				Clients.showNotification("Jenis Kejadian berhasil ditambahkan.");
				
				// clear all comboitems
				jenisKejadianCombobox.getItems().clear();
				// set the value
				Comboitem comboitem = new Comboitem();
				comboitem.setLabel(kejadianJenis.getNamaJenis());
				comboitem.setValue(kejadianJenis);
				comboitem.setParent(jenisKejadianCombobox);
				//
				jenisKejadianCombobox.setSelectedItem(comboitem);
				//
				setKejadianJenis(kejadianJenis);
			}
		});
		kejadianJenisListWinDialog.addEventListener(Events.ON_OK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				KejadianJenis existingKejadianJenis = (KejadianJenis) event.getData();
				
				// clear all the comboitems
				jenisKejadianCombobox.getItems().clear();
				// create comboitem
				Comboitem comboitem = new Comboitem();
				comboitem.setLabel(existingKejadianJenis.getNamaJenis());
				comboitem.setValue(existingKejadianJenis);
				comboitem.setParent(jenisKejadianCombobox);
				
				jenisKejadianCombobox.setSelectedItem(comboitem);
				// set
				setKejadianJenis(existingKejadianJenis);
			}
		});
		
		kejadianJenisListWinDialog.doModal();
	}
	
	public void onSelect$motifKejadianCombobox(Event event) throws Exception {
		List<KejadianMotif> kejadianMotifList = 
				getKejadianMotifDao().findAllKejadianMotif();
		KejadianMotifData kejadianMotifData = new KejadianMotifData();
		kejadianMotifData.setKejadianMotifList(kejadianMotifList);
		kejadianMotifData.setTkpKejadianMotif(getTkp().getMOTIF());
		// display a dialog list
		Map<String, KejadianMotifData> arg = Collections.singletonMap("kejadianMotifData", kejadianMotifData);
		Window kejadianMotifListWinDialog = 
				(Window) Executions.createComponents("/dialog/KejadianMotifListDialog.zul", 
						kejadianMigrasiDialogWin, arg);
		kejadianMotifListWinDialog.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				KejadianMotif kejadianMotif = (KejadianMotif) event.getData();
				// current settings kotamatops current settings
				TimezoneInd timezoneInd = getCurrentSettings().getSelectedKotamaops().getTimeZone();
				int timezoneIndOrdinal = timezoneInd.ordinal();
				// 
				kejadianMotif.setCreatedAt(asDate(getLocalDateTime(timezoneInd.toZoneId(timezoneIndOrdinal))));
				kejadianMotif.setEditedAt(asDate(getLocalDateTime(timezoneInd.toZoneId(timezoneIndOrdinal))));
				// save
				getKejadianMotifDao().save(kejadianMotif);
				
				Clients.showNotification("Motif Kejadian berhasil ditambahkan.");
				
				// clear all comboitems
				motifKejadianCombobox.getItems().clear();
				// add comboitem
				Comboitem comboitem = new Comboitem();
				comboitem.setLabel(kejadianMotif.getNamaMotif());
				comboitem.setValue(kejadianMotif);
				comboitem.setParent(motifKejadianCombobox);
				
				motifKejadianCombobox.setSelectedItem(comboitem);
				// set
				setKejadianMotif(kejadianMotif);
			}
		});
		kejadianMotifListWinDialog.addEventListener(Events.ON_OK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				KejadianMotif existingKejadianMotif = (KejadianMotif)event.getData();

				// clear
				motifKejadianCombobox.getItems().clear();
				// use comboitem
				Comboitem comboitem = new Comboitem();
				comboitem.setLabel(existingKejadianMotif.getNamaMotif());
				comboitem.setValue(existingKejadianMotif);
				comboitem.setParent(motifKejadianCombobox);
				
				motifKejadianCombobox.setSelectedItem(comboitem);
				// set
				setKejadianMotif(existingKejadianMotif);
			}
		});
		
		kejadianMotifListWinDialog.doModal();
	}

	public void onSelect$pelakuKejadianCombobox(Event event) throws Exception {
		List<KejadianPelaku> kejadianPelakuList = 
				getKejadianPelakuDao().findAllKejadianPelaku();
		KejadianPelakuData kejadianPelakuData = new KejadianPelakuData();
		kejadianPelakuData.setKejadianPelakuList(kejadianPelakuList);
		kejadianPelakuData.setTkpKejadianPelaku(getTkp().getPELAKU());
		// pass into the dialog
		Map<String, KejadianPelakuData> arg = 
				Collections.singletonMap("kejadianPelakuData", kejadianPelakuData);
		Window kejadianPelakuListWinDialog = 
				(Window) Executions.createComponents("/dialog/KejadianPelakuListDialog.zul", 
						kejadianMigrasiDialogWin, arg);
		kejadianPelakuListWinDialog.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				KejadianPelaku kejadianPelaku = (KejadianPelaku) event.getData();
				// current settings kotamatops current settings
				TimezoneInd timezoneInd = getCurrentSettings().getSelectedKotamaops().getTimeZone();
				int timezoneIndOrdinal = timezoneInd.ordinal();
				// 
				kejadianPelaku.setCreatedAt(asDate(getLocalDateTime(timezoneInd.toZoneId(timezoneIndOrdinal))));
				kejadianPelaku.setEditedAt(asDate(getLocalDateTime(timezoneInd.toZoneId(timezoneIndOrdinal))));
				// save
				getKejadianPelakuDao().save(kejadianPelaku);
				
				Clients.showNotification("Pelaku Kejadian berhasil ditambahkan.");
				
				// clear all comboitems
				pelakuKejadianCombobox.getItems().clear();
				// add comboitem
				Comboitem comboitem = new Comboitem();
				comboitem.setLabel(kejadianPelaku.getNamaPelaku());
				comboitem.setValue(kejadianPelaku);
				comboitem.setParent(pelakuKejadianCombobox);
				
				pelakuKejadianCombobox.setSelectedItem(comboitem);
				// set
				setKejadianPelaku(kejadianPelaku);
			}
		});
		kejadianPelakuListWinDialog.addEventListener(Events.ON_OK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				KejadianPelaku existingKejadianPelaku = (KejadianPelaku)event.getData();

				// clear
				pelakuKejadianCombobox.getItems().clear();
				// use comboitem
				Comboitem comboitem = new Comboitem();
				comboitem.setLabel(existingKejadianPelaku.getNamaPelaku());
				comboitem.setValue(existingKejadianPelaku);
				comboitem.setParent(pelakuKejadianCombobox);
				
				pelakuKejadianCombobox.setSelectedItem(comboitem);
				// set
				setKejadianPelaku(existingKejadianPelaku);				
			}
		});
		kejadianPelakuListWinDialog.doModal();
	}
	
	
	public void onClick$saveButton(Event event) throws Exception {
		if (kotamaopsCombobox.getSelectedItem()==null) {
			throw new Exception("Belum memilih Kotamaops.");
		}
		if (propinsiCombobox.getSelectedItem()==null) {
			throw new Exception("Belum memilih Propinsi.");
		}
		if (kabupatenCombobox.getSelectedItem()==null) {
			throw new Exception("Belum memilih Kabupaten.");
		}
		if (kecamatanCombobox.getSelectedItem()==null) {
			throw new Exception("Belum memilih Kecamatan.");
		}
		if (kelurahanCombobox.getSelectedItem()==null) {
			throw new Exception("Belum memilih Kelurahan.");
		}
		if (jenisKejadianCombobox.getSelectedItem()==null) {
			throw new Exception("Belum memilih Jenis Kejadian.");
		}
		if (motifKejadianCombobox.getSelectedItem()==null) {
			throw new Exception("Belum memilih Motif Kejadian.");
		}
		if (pelakuKejadianCombobox.getSelectedItem()==null) {
			throw new Exception("Belum memilih Pelaku Kejadian.");
		}
		// get the modified data
		Kejadian kejadian = new Kejadian();
		kejadian.setCreatedAt(asDate(getCurrentLocalDateTime()));
		kejadian.setEditedAt(asDate(getCurrentLocalDateTime()));
		kejadian.setSerialNumber(getSerialNumber(getTkp().getId_str(), 
				getTkp().getThn(), getTkp().getTw()));
		LocalDateTime twPembuatanLocalDateTime = 
				convertTkpTw(twBuatTahunTextbox.getValue(), 
						twBuatTanggalJamTextbox.getValue());
		kejadian.setTwPembuatanDateTime(asDate(twPembuatanLocalDateTime));
		kejadian.setTwPembuatanTimezone(
				twBuatTimeZoneCombobox.getSelectedItem().getValue());
		LocalDateTime twKejadianLocalDateTime =
				convertTkpTw(twJadiTahunTextbox.getValue(),
						twJadiTanggalJamTextbox.getValue());
		kejadian.setTwKejadianDateTime(asDate(twKejadianLocalDateTime));
		kejadian.setTwKejadianTimezone(
				(TimezoneInd) twJadiTimeZoneTextbox.getAttribute("KETWAKTUACT"));
		
		kejadian.setKotamaops(kotamaopsCombobox.getSelectedItem().getValue());
		kejadian.setPropinsi(propinsiCombobox.getSelectedItem().getValue());
		kejadian.setKabupatenKotamadya(kabupatenCombobox.getSelectedItem().getValue());
		kejadian.setKecamatan(kecamatanCombobox.getSelectedItem().getValue());
		kejadian.setKelurahan(kelurahanCombobox.getSelectedItem().getValue());
		
		kejadian.setKoordinatGps(koordGpsTextbox.getValue());
		kejadian.setKoordinatPeta(koordPetaTextbox.getValue());
		kejadian.setBujurLintang(bujurLintangTextbox.getValue());
		
		kejadian.setKampung(kampungTextbox.getValue());
		kejadian.setJalan(jalanTextbox.getValue());
		
		kejadian.setJenisKejadian(jenisKejadianCombobox.getSelectedItem().getValue());
		kejadian.setMotifKejadian(motifKejadianCombobox.getSelectedItem().getValue());
		kejadian.setPelakuKejadian(pelakuKejadianCombobox.getSelectedItem().getValue());
		
		kejadian.setKeteranganPelaku(ketPelakuTextbox.getValue());
		kejadian.setSasaran(sasaranTextbox.getValue());
		
		kejadian.setKronologis(kronologisTextbox.getValue());
		
		// send event
		Events.sendEvent(Events.ON_OK, kejadianMigrasiDialogWin, kejadian);
		
		// detach
		kejadianMigrasiDialogWin.detach();
	}

	private DocumentSerialNumber getSerialNumber(String id_str, String thn, String tw) throws Exception {
		DocumentSerialNumber serialNumber = new DocumentSerialNumber();
		serialNumber.setCreatedAt(asDate(getCurrentLocalDateTime()));
		serialNumber.setEditedAt(asDate(getCurrentLocalDateTime()));
		serialNumber.setSerialDate(asDate(convertTkpTw(thn, tw)));
		serialNumber.setSerialComp(id_str);
		
		return serialNumber;
	}

	public void onClick$cancelButton(Event event) throws Exception {
		
		kejadianMigrasiDialogWin.detach();
	}

	public Tkp getTkp() {
		return tkp;
	}

	public void setTkp(Tkp tkp) {
		this.tkp = tkp;
	}

	public LocalDateTime getTwPembuatan() {
		return twPembuatan;
	}

	public void setTwPembuatan(LocalDateTime twPembuatan) {
		this.twPembuatan = twPembuatan;
	}

	public LocalDateTime getTwKejadian() {
		return twKejadian;
	}

	public void setTwKejadian(LocalDateTime twKejadian) {
		this.twKejadian = twKejadian;
	}

	public TimezoneInd getTwPembuatanTimezone() {
		return twPembuatanTimezone;
	}

	public void setTwPembuatanTimezone(TimezoneInd twPembuatanTimezone) {
		this.twPembuatanTimezone = twPembuatanTimezone;
	}

	public TimezoneInd getTwKejadianTimezone() {
		return twKejadianTimezone;
	}

	public void setTwKejadianTimezone(TimezoneInd twKejadianTimezone) {
		this.twKejadianTimezone = twKejadianTimezone;
	}

	public KotamaopsDao getKotamaopsDao() {
		return kotamaopsDao;
	}

	public void setKotamaopsDao(KotamaopsDao kotamaopsDao) {
		this.kotamaopsDao = kotamaopsDao;
	}

	public Kotamaops getKejadianKotamaops() {
		return kejadianKotamaops;
	}

	public void setKejadianKotamaops(Kotamaops kejadianKotamaops) {
		this.kejadianKotamaops = kejadianKotamaops;
	}

	public Propinsi getKejadianPropinsi() {
		return kejadianPropinsi;
	}

	public void setKejadianPropinsi(Propinsi kejadianPropinsi) {
		this.kejadianPropinsi = kejadianPropinsi;
	}

	public PropinsiDao getPropinsiDao() {
		return propinsiDao;
	}

	public void setPropinsiDao(PropinsiDao propinsiDao) {
		this.propinsiDao = propinsiDao;
	}

	public Settings getCurrentSettings() {
		return currentSettings;
	}

	public void setCurrentSettings(Settings currentSettings) {
		this.currentSettings = currentSettings;
	}

	public MigrasiData getMigrasiData() {
		return migrasiData;
	}

	public void setMigrasiData(MigrasiData migrasiData) {
		this.migrasiData = migrasiData;
	}

	public Kabupaten_Kotamadya getKejadianKabupatenKotamadya() {
		return kejadianKabupatenKotamadya;
	}

	public void setKejadianKabupatenKotamadya(Kabupaten_Kotamadya kejadianKabupatenKotamadya) {
		this.kejadianKabupatenKotamadya = kejadianKabupatenKotamadya;
	}

	public Kecamatan getKejadianKecamatan() {
		return kejadianKecamatan;
	}

	public void setKejadianKecamatan(Kecamatan kejadianKecamatan) {
		this.kejadianKecamatan = kejadianKecamatan;
	}

	public Kabupaten_KotamadyaDao getKabupaten_KotamadyaDao() {
		return kabupaten_KotamadyaDao;
	}

	public void setKabupaten_KotamadyaDao(Kabupaten_KotamadyaDao kabupaten_KotamadyaDao) {
		this.kabupaten_KotamadyaDao = kabupaten_KotamadyaDao;
	}

	public KecamatanDao getKecamatanDao() {
		return kecamatanDao;
	}

	public void setKecamatanDao(KecamatanDao kecamatanDao) {
		this.kecamatanDao = kecamatanDao;
	}

	public KelurahanDao getKelurahanDao() {
		return kelurahanDao;
	}

	public void setKelurahanDao(KelurahanDao kelurahanDao) {
		this.kelurahanDao = kelurahanDao;
	}

	public Kelurahan getKejadianKelurahan() {
		return kejadianKelurahan;
	}

	public void setKejadianKelurahan(Kelurahan kejadianKelurahan) {
		this.kejadianKelurahan = kejadianKelurahan;
	}

	public KronologisSqlDao getKronologisSqlDao() {
		return kronologisSqlDao;
	}

	public void setKronologisSqlDao(KronologisSqlDao kronologisSqlDao) {
		this.kronologisSqlDao = kronologisSqlDao;
	}

	public Kronologis getKejadianKronologis() {
		return kejadianKronologis;
	}

	public void setKejadianKronologis(Kronologis kejadianKronologis) {
		this.kejadianKronologis = kejadianKronologis;
	}

	public KejadianJenis getKejadianJenis() {
		return kejadianJenis;
	}

	public void setKejadianJenis(KejadianJenis kejadianJenis) {
		this.kejadianJenis = kejadianJenis;
	}

	public KejadianJenisDao getKejadianJenisDao() {
		return kejadianJenisDao;
	}

	public void setKejadianJenisDao(KejadianJenisDao kejadianJenisDao) {
		this.kejadianJenisDao = kejadianJenisDao;
	}

	public KejadianMotif getKejadianMotif() {
		return kejadianMotif;
	}

	public void setKejadianMotif(KejadianMotif kejadianMotif) {
		this.kejadianMotif = kejadianMotif;
	}

	public KejadianMotifDao getKejadianMotifDao() {
		return kejadianMotifDao;
	}

	public void setKejadianMotifDao(KejadianMotifDao kejadianMotifDao) {
		this.kejadianMotifDao = kejadianMotifDao;
	}

	public KejadianPelaku getKejadianPelaku() {
		return kejadianPelaku;
	}

	public void setKejadianPelaku(KejadianPelaku kejadianPelaku) {
		this.kejadianPelaku = kejadianPelaku;
	}

	public KejadianPelakuDao getKejadianPelakuDao() {
		return kejadianPelakuDao;
	}

	public void setKejadianPelakuDao(KejadianPelakuDao kejadianPelakuDao) {
		this.kejadianPelakuDao = kejadianPelakuDao;
	}

	public Kotamaops getKotamaops() {
		return kotamaops;
	}

	public void setKotamaops(Kotamaops kotamaops) {
		this.kotamaops = kotamaops;
	}

	public LocalDateTime getCurrentLocalDateTime() {
		return currentLocalDateTime;
	}

	public void setCurrentLocalDateTime(LocalDateTime currentLocalDateTime) {
		this.currentLocalDateTime = currentLocalDateTime;
	}

}
