package mil.pusdalops.k2.webui.migrasi;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.gmt.TimezoneInd;
import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.domain.kejadian.KejadianJenis;
import mil.pusdalops.domain.kejadian.KejadianMotif;
import mil.pusdalops.domain.kejadian.KejadianPelaku;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.kotamaops.KotamaopsType;
import mil.pusdalops.domain.wilayah.Kabupaten_Kotamadya;
import mil.pusdalops.domain.wilayah.Kecamatan;
import mil.pusdalops.domain.wilayah.Kelurahan;
import mil.pusdalops.domain.wilayah.Propinsi;
import mil.pusdalops.k2.persistence.kabupaten_kotamadya.dao.Kabupaten_KotamadyaDao;
import mil.pusdalops.k2.persistence.kecamatan.dao.KecamatanDao;
import mil.pusdalops.k2.persistence.kejadian.dao.KejadianDao;
import mil.pusdalops.k2.persistence.kejadian.jenis.dao.KejadianJenisDao;
import mil.pusdalops.k2.persistence.kejadian.motif.dao.KejadianMotifDao;
import mil.pusdalops.k2.persistence.kejadian.pelaku.dao.KejadianPelakuDao;
import mil.pusdalops.k2.persistence.kelurahan.dao.KelurahanDao;
import mil.pusdalops.k2.persistence.kotamaops.dao.KotamaopsDao;
import mil.pusdalops.k2.persistence.propinsi.dao.PropinsiDao;
import mil.pusdalops.k2.webui.common.GFCBaseController;
import mil.pusdalops.k2.webui.common.TwConversion;
import mil.pusdalops.k2.webui.dialog.DatetimeData;

public class KejadianMenonjolDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7996542572581486058L;

	private KejadianDao kejadianDao;
	private KotamaopsDao kotamaopsDao;
	private PropinsiDao propinsiDao;
	private Kabupaten_KotamadyaDao kabupaten_KotamadyaDao;
	private KecamatanDao kecamatanDao; 
	private KelurahanDao kelurahanDao; 
	private KejadianJenisDao kejadianJenisDao; 
	private KejadianMotifDao kejadianMotifDao; 
	private KejadianPelakuDao kejadianPelakuDao;
	
	private TwConversion twConversion;
	
	private Window kejadianMenonjolDialogWin;
	private Textbox kejadianIdTextbox, twBuatTahunTextbox, twBuatTanggalJamTextbox,
		twJadiTahunTextbox, twJadiTanggalJamTextbox, twJadiTimeZoneTextbox, koordGpsTextbox,
		koordPetaTextbox, bujurLintangTextbox, kampungTextbox, jalanTextbox, 
		keteranganPelakuTextbox, sasaranTextbox, kronologisTextbox;
	private Combobox twBuatTimeZoneCombobox, kotamaopsCombobox, propCombobox, kabupatenCombobox,
		kecamatanCombobox, kelurahanCombobox, jenisKejadianCombobox, motifKejadianCombobox,
		pelakuKejadianCombobox;
	private Label idLabel;
	
	private KejadianData kejadianData;
	private Kejadian kejadian;
	private LocalDateTime currentLocalDateTime;
	private Kotamaops settingsKotamaops;
	private ZoneId zoneId;
	// private Propinsi selectedPropinsi;
	// private Kabupaten_Kotamadya selectKabupatenKot;
	// private Kecamatan selectedKecamatan;
	
	private static final Logger log = Logger.getLogger(KejadianMenonjolDialogControl.class);
	private final String NO_INFO = "-Tidak Ada Info-";
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setKejadianData(
				(KejadianData) arg.get("kejadianData"));
	}

	public void onCreate$kejadianMenonjolDialogWin(Event event) throws Exception {
		log.info("Creating KejadianMenonjolDialogWinControl...");
		
		setTwConversion(new TwConversion());
		
		// settings kotamaops
		setSettingsKotamaops(getKejadianData().getSettingsKotamaops());
		
		// set the timezone for this kotamaops
		int timezoneOrdinal = getSettingsKotamaops().getTimeZone().ordinal();
		setZoneId(getSettingsKotamaops().getTimeZone().toZoneId(timezoneOrdinal));		
		
		// current datetime
		setCurrentLocalDateTime(getLocalDateTime(getZoneId()));
		
		// kejadian
		setKejadian(getKejadianData().getKejadian());
		
		// load timezone
		loadTimezone();
		
		// twPembuatan twKejadian
		displayTwPembuatanKejadianData();
		
		// kotamaops
		loadKotamaopsCombobox();
		
		// jenis kejadian
		loadRelatedKejadianCombobox();
		
		// other data
		displayKejadianData();
	}

	private void loadTimezone() {
		Comboitem comboitem;
		for (TimezoneInd timezoneInd : TimezoneInd.values()) {
			comboitem = new Comboitem();
			comboitem.setLabel(timezoneInd.toString());
			comboitem.setValue(timezoneInd);
			comboitem.setParent(twBuatTimeZoneCombobox);
		}
	}

	private void displayTwPembuatanKejadianData() {
		idLabel.setValue("#"+String.valueOf(getKejadian().getId()));
		kejadianIdTextbox.setValue(getKejadian().getSerialNumber().getSerialComp());
		// TW Pembuatan
		twBuatTahunTextbox.setValue(
			getLocalDateTimeString(
				asLocalDateTime(getKejadian().getTwPembuatanDateTime()), "YYYY"));
		twBuatTanggalJamTextbox.setValue(
			getLocalDateTimeString(
				asLocalDateTime(getKejadian().getTwPembuatanDateTime()), "MMdd")+"."+
			getLocalDateTimeString(
				asLocalDateTime(getKejadian().getTwPembuatanDateTime()), "HHmm"));
		for (Comboitem twBuatTimeZone : twBuatTimeZoneCombobox.getItems()) {
			if (getKejadian().getTwPembuatanTimezone().equals(twBuatTimeZone.getValue())) {
				twBuatTimeZoneCombobox.setSelectedItem(twBuatTimeZone);
				
				break;
			}
		}
		// TW Kejadian
		twJadiTahunTextbox.setValue(
			getLocalDateTimeString(
				asLocalDateTime(getKejadian().getTwKejadianDateTime()), "YYYY"));
		twJadiTanggalJamTextbox.setValue(
			getLocalDateTimeString(
				asLocalDateTime(getKejadian().getTwKejadianDateTime()), "MMdd")+"."+
			getLocalDateTimeString(
				asLocalDateTime(getKejadian().getTwKejadianDateTime()), "HHmm"));
		twJadiTimeZoneTextbox.setValue(getKejadian().getTwKejadianTimezone().toString());
		twJadiTimeZoneTextbox.setAttribute("twJadiTimeZoneInd", getKejadian().getTwKejadianTimezone());
	}
		
	public void onClick$twJadiRubahButton(Event event) throws Exception {
		// create the data object
		DatetimeData datetimeData = new DatetimeData();
		// set the dialog title
		datetimeData.setDialogWinTitle("Rubah TW Kejadian");
		
		// set timezoneInd
		for (TimezoneInd timezoneInd : TimezoneInd.values()) {
			if (timezoneInd.equals(getKejadian().getTwKejadianTimezone())) {
				datetimeData.setTimezoneInd(timezoneInd);
				datetimeData.setZoneId(timezoneInd.toZoneId(timezoneInd.ordinal()));
				
				break;
			}
		}
		// set localdatetime
		datetimeData.setLocalDateTime(asLocalDateTime(getKejadian().getTwKejadianDateTime()));
		log.info("Assign to DatetimeData: "+datetimeData.toString());
		
		Map<String, DatetimeData> args = Collections.singletonMap("datetimeData", datetimeData);
		Window datetimeWin = (Window) Executions.createComponents(
				"/dialog/DatetimeWinDialog.zul", kejadianMenonjolDialogWin, args);
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
	
	private void loadKotamaopsCombobox() throws Exception {
		List<Kotamaops> kotamaopsList = getKotamaopsDao().findAllKotamaopsWithOrder(true);
		
		Comboitem comboitem;
		for (Kotamaops kotamaops : kotamaopsList) {
			if (!kotamaops.getKotamaopsType().equals(KotamaopsType.PUSDALOPS)) {
				comboitem = new Comboitem();
				comboitem.setLabel(kotamaops.getKotamaopsName());
				comboitem.setValue(kotamaops);
				comboitem.setParent(kotamaopsCombobox);				
			}
		}
		
	}

	private void loadRelatedKejadianCombobox() throws Exception {
		Comboitem comboitem;
		
		// jenis kejadian
		List<KejadianJenis> kejadianJenisList = getKejadianJenisDao().findAllKejadianJenisOrderBy(true);
		for (KejadianJenis kejadianJenis : kejadianJenisList) {
			comboitem = new Comboitem();
			comboitem.setLabel(kejadianJenis.getNamaJenis());
			comboitem.setValue(kejadianJenis);
			comboitem.setParent(jenisKejadianCombobox);
		}
		
		// motif kejadian
		List<KejadianMotif> kejadianMotifList = getKejadianMotifDao().findAllKejadianMotifOrderBy(true);
		for (KejadianMotif kejadianMotif : kejadianMotifList) {
			comboitem = new Comboitem();
			comboitem.setLabel(kejadianMotif.getNamaMotif());
			comboitem.setValue(kejadianMotif);
			comboitem.setParent(motifKejadianCombobox);
		}
		
		// pelaku kejadian
		List<KejadianPelaku> kejadianPelakuList = getKejadianPelakuDao().findAllKejadianPelaku();
		for (KejadianPelaku kejadianPelaku : kejadianPelakuList) {
			comboitem = new Comboitem();
			comboitem.setLabel(kejadianPelaku.getNamaPelaku());
			comboitem.setValue(kejadianPelaku);
			comboitem.setParent(pelakuKejadianCombobox);
		}
	}

	private void displayKejadianData() throws Exception {
		// kotamaops combobox
		Kejadian kejadianKotamaopsByProxy = getKejadianDao().findKejadianKotamaopsByProxy(getKejadian().getId());

		for (Comboitem comboitem : kotamaopsCombobox.getItems()) {
			Kotamaops kotamaopsItem = comboitem.getValue();
			if (kotamaopsItem.getKotamaopsName().compareTo(kejadianKotamaopsByProxy.getKotamaops().getKotamaopsName())==0) {
				kotamaopsCombobox.setSelectedItem(comboitem);
				break;
			}
		}
		
		// propCombobox
		loadPropinsiCombobox(kotamaopsCombobox.getSelectedItem().getValue());
		Kejadian kejadianPropinsiByProxy = getKejadianDao().findKejadianPropinsiByProxy(getKejadian().getId());
		for (Comboitem comboitem : propCombobox.getItems()) {
			Propinsi propinsiItem = comboitem.getValue();
			if (propinsiItem.getNamaPropinsi().compareTo(kejadianPropinsiByProxy.getPropinsi().getNamaPropinsi())==0) {
				propCombobox.setSelectedItem(comboitem);
				break;
			}
		}
		
		// kabupatenCombobox
		loadKabupatenCombobox(propCombobox.getSelectedItem().getValue());
		Kejadian kejadianKabupatenByProxy = getKejadianDao().findKejadianKabupatenKotamadyaByProxy(getKejadian().getId());
		for (Comboitem comboitem : kabupatenCombobox.getItems()) {
			Kabupaten_Kotamadya kabupatenKotItem = comboitem.getValue();
			if (kabupatenKotItem.getNamaKabupaten().compareTo(kejadianKabupatenByProxy.getKabupatenKotamadya().getNamaKabupaten())==0) {
				kabupatenCombobox.setSelectedItem(comboitem);
				break;
			}
		}
		
		// kecamatanCombobox
		loadKecamatanCombobox(kabupatenCombobox.getSelectedItem().getValue());
		Kejadian kejadianKecamatanByProxy = getKejadianDao().findKejadianKecamatanByProxy(getKejadian().getId());
		for (Comboitem comboitem : kecamatanCombobox.getItems()) {
			Kecamatan kecamatanItem = comboitem.getValue();
			if (kecamatanItem.getNamaKecamatan().compareTo(kejadianKecamatanByProxy.getKecamatan().getNamaKecamatan())==0) {
				kecamatanCombobox.setSelectedItem(comboitem);
				break;
			}
		}
		
		// kelurahanCombobox
		loadKelurahanCombobox(kecamatanCombobox.getSelectedItem().getValue());
		Kejadian kejadianKelurahanByProxy = getKejadianDao().findKejadianKelurahanByProxy(getKejadian().getId());
		for (Comboitem comboitem : kelurahanCombobox.getItems()) {
			Kelurahan kelurahanItem = comboitem.getValue();
			if (kelurahanItem.getNamaKelurahan().compareTo(kejadianKelurahanByProxy.getKelurahan().getNamaKelurahan())==0) {
				kelurahanCombobox.setSelectedItem(comboitem);
				break;
			}
		}
		
		// jenisKejadian Combobox
		for (Comboitem comboitem : jenisKejadianCombobox.getItems()) {
			KejadianJenis kejadianJenisItem = comboitem.getValue();
			if (kejadianJenisItem.getNamaJenis().compareTo(getKejadian().getJenisKejadian().getNamaJenis())==0) {
				jenisKejadianCombobox.setSelectedItem(comboitem);
				break;
			}
		}
		
		// motifKejadian Combobox
		for (Comboitem comboitem : motifKejadianCombobox.getItems()) {
			KejadianMotif kejadianMotifItem = comboitem.getValue();
			if (kejadianMotifItem.getNamaMotif().compareTo(getKejadian().getMotifKejadian().getNamaMotif())==0) {
				motifKejadianCombobox.setSelectedItem(comboitem);
				break;
			}
		}
		
		// pelakuKejadian Combobox
		for (Comboitem comboitem : pelakuKejadianCombobox.getItems()) {
			KejadianPelaku kejadianPelakuItem = comboitem.getValue();
			if (kejadianPelakuItem.getNamaPelaku().compareTo(getKejadian().getPelakuKejadian().getNamaPelaku())==0) {
				pelakuKejadianCombobox.setSelectedItem(comboitem);
				break;
			}
		}
		
		// other data
		koordGpsTextbox.setValue(getKejadian().getKoordinatGps());
		koordPetaTextbox.setValue(getKejadian().getKoordinatPeta());
		bujurLintangTextbox.setValue(getKejadian().getBujurLintang()); 
		kampungTextbox.setValue(getKejadian().getKampung()); 
		jalanTextbox.setValue(getKejadian().getJalan());
		keteranganPelakuTextbox.setValue(getKejadian().getKeteranganPelaku());
		sasaranTextbox.setValue(getKejadian().getSasaran());
		kronologisTextbox.setValue(getKejadian().getKronologis());
	}	

	private void resetOtherComboboxes(int numComboboxes) {
		switch (numComboboxes) {
		case 4:
			// when kotamaops is selected
			propCombobox.setValue("");
			kabupatenCombobox.setValue("");
			kecamatanCombobox.setValue("");
			kelurahanCombobox.setValue("");
			
			propCombobox.getItems().clear();
			kabupatenCombobox.getItems().clear();
			kecamatanCombobox.getItems().clear();
			kelurahanCombobox.getItems().clear();
			break;
		case 3:
			// when propinsi is selected
			kabupatenCombobox.setValue("");
			kecamatanCombobox.setValue("");
			kelurahanCombobox.setValue("");

			kabupatenCombobox.getItems().clear();
			kecamatanCombobox.getItems().clear();
			kelurahanCombobox.getItems().clear();			
			break;
		case 2:
			// when kabupaten is selected
			kecamatanCombobox.setValue("");
			kelurahanCombobox.setValue("");

			kecamatanCombobox.getItems().clear();
			kelurahanCombobox.getItems().clear();			
			break;
		case 1:
			// when kecamatan is selected
			kelurahanCombobox.setValue("");
			kelurahanCombobox.getItems().clear();			
			break;

		default:
			break;
		}
	}	
	
	public void onSelect$kotamaopsCombobox(Event event) throws Exception {
		resetOtherComboboxes(4);
		
		Kotamaops selectedKotamaops = kotamaopsCombobox.getSelectedItem().getValue();

		// set timezone kejadian according to selected kotamaops
		twJadiTimeZoneTextbox.setValue(selectedKotamaops.getTimeZone().toString());
		twJadiTimeZoneTextbox.setAttribute("twJadiTimeZoneInd", selectedKotamaops.getTimeZone());

		loadPropinsiCombobox(selectedKotamaops);		
	}
	
	private void loadPropinsiCombobox(Kotamaops selKotamaops) throws Exception {
		Kotamaops kotamaopsByProxy = 
				getKotamaopsDao().findKotamaopsPropinsiByProxy(selKotamaops.getId());
		List<Propinsi> propinsiList = kotamaopsByProxy.getPropinsis();
		
		// sort
		Collections.sort(propinsiList, new Comparator<Propinsi>() {

			@Override
			public int compare(Propinsi p1, Propinsi p2) {

				return p1.getNamaPropinsi().compareTo(p2.getNamaPropinsi());
			}
		});
		
		Comboitem comboitem;
		for (Propinsi propinsi : propinsiList) {
			comboitem = new Comboitem();
			comboitem.setLabel(propinsi.getNamaPropinsi());
			comboitem.setValue(propinsi);
			comboitem.setParent(propCombobox);
		}	
	}

	public void onSelect$propCombobox(Event event) throws Exception {
		resetOtherComboboxes(3);
		
		Propinsi selectedPropinsi = propCombobox.getSelectedItem().getValue();
		
		loadKabupatenCombobox(selectedPropinsi);
	}
		
	private void loadKabupatenCombobox(Propinsi selectedPropinsi) throws Exception {
		Propinsi propinsiByProxy = 
				getPropinsiDao().findKabupatenKotamadyaByProxy(selectedPropinsi.getId());
		List<Kabupaten_Kotamadya> kabupatenKots = 
				propinsiByProxy.getKabupatenkotamadyas();
		
		// sort
		Collections.sort(kabupatenKots, new Comparator<Kabupaten_Kotamadya>() {

			@Override
			public int compare(Kabupaten_Kotamadya kabKot1, Kabupaten_Kotamadya kabKot2) {
				
				return kabKot1.getNamaKabupaten().compareTo(kabKot2.getNamaKabupaten());
			}
		});
		
		Comboitem comboitem;
		for (Kabupaten_Kotamadya kabupaten_Kotamadya : kabupatenKots) {
			comboitem = new Comboitem();
			comboitem.setLabel(kabupaten_Kotamadya.getNamaKabupaten());
			comboitem.setValue(kabupaten_Kotamadya);
			comboitem.setParent(kabupatenCombobox);
		}
		// create comboitem to add to the kabupaten
		// -- in case the kabupaten not in the list
		comboitem = new Comboitem();
		comboitem.setLabel("Tambah Kabupaten/Kot...");
		comboitem.setValue(null);
		comboitem.setParent(kabupatenCombobox);		
	}
	
	public void onSelect$kabupatenCombobox(Event event) throws Exception {
		if (kabupatenCombobox.getSelectedItem().getValue()==null) {
			// display textentry dialog
			Map<String, String> args = Collections.singletonMap("name", "Kabupaten/Kot:");
			Window textEntryWindow = (Window) Executions.createComponents(
					"/migrasi/kejadian/WilayahTextEntry.zul", kejadianMenonjolDialogWin, args);
			textEntryWindow.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// TODO Auto-generated method stub
					
				}
			});
			textEntryWindow.addEventListener(Events.ON_CANCEL, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// TODO Auto-generated method stub
					
				}
			});
			
			textEntryWindow.doModal();
			
		} else {
			resetOtherComboboxes(2);
			
			Kabupaten_Kotamadya selectedKabupatenKot = kabupatenCombobox.getSelectedItem().getValue();
			
			loadKecamatanCombobox(selectedKabupatenKot);
		}
	}
	
	private void loadKecamatanCombobox(Kabupaten_Kotamadya selKabupatenKot) throws Exception {
		Kabupaten_Kotamadya kabupatenKotByProxy = 
				getKabupaten_KotamadyaDao().findKecamatanByProxy(selKabupatenKot.getId());
		List<Kecamatan> kecamatanList = kabupatenKotByProxy.getKecamatans();
		
		// sort
		Collections.sort(kecamatanList, new Comparator<Kecamatan>() {

			@Override
			public int compare(Kecamatan kec1, Kecamatan kec2) {
				
				return kec1.getNamaKecamatan().compareTo(kec2.getNamaKecamatan());
			}
		});
		
		if (selKabupatenKot.getNamaKabupaten().compareTo(NO_INFO)==0) {
			if (kecamatanList.isEmpty()) {
				// 
			} else {

			}
		} else {
			Comboitem comboitem;
			for (Kecamatan kecamatan : kecamatanList) {
				comboitem = new Comboitem();
				comboitem.setLabel(kecamatan.getNamaKecamatan());
				comboitem.setValue(kecamatan);
				comboitem.setParent(kecamatanCombobox);
			}
		}
		
	}

	public void onSelect$kecamatanCombobox(Event event) throws Exception {
		if (kecamatanCombobox.getSelectedItem().getValue()==null) {
			// display textentry dialog
			Map<String, String> args = Collections.singletonMap("name", "Kecamatan:");
			Window textEntryWindow = (Window) Executions.createComponents(
					"/migrasi/kejadian/WilayahTextEntry.zul", kejadianMenonjolDialogWin, args);
			textEntryWindow.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// TODO Auto-generated method stub
					
				}
			});
			textEntryWindow.addEventListener(Events.ON_CANCEL, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// TODO Auto-generated method stub
					
				}
			});
			textEntryWindow.doModal();
		} else {
			resetOtherComboboxes(1);
			
			loadKelurahanCombobox(kecamatanCombobox.getSelectedItem().getValue());
		}
	}
	
	private void loadKelurahanCombobox(Kecamatan selKecamatan) throws Exception {
		Kecamatan selKecamatanByProxy = getKecamatanDao().findKelurahanByProxy(selKecamatan.getId());
		List<Kelurahan> kelurahanList = selKecamatanByProxy.getKelurahans();
		if (selKecamatan.getNamaKecamatan().compareTo(NO_INFO)==0) {
			if (kelurahanList.isEmpty()) {
				//
			} else {
				
			}
		} else {
			Comboitem comboitem;
			for (Kelurahan kelurahan : kelurahanList) {
				comboitem = new Comboitem();
				comboitem.setLabel(kelurahan.getNamaKelurahan());
				comboitem.setValue(kelurahan);
				comboitem.setParent(kelurahanCombobox);
			}
		}
	}

	public void onClick$saveButton(Event event) throws Exception {
		// send event - onChange
		Events.sendEvent(Events.ON_CHANGE, kejadianMenonjolDialogWin, getModifiedKejadian());
		
		// detach
		kejadianMenonjolDialogWin.detach();
	}
	
	private Kejadian getModifiedKejadian() throws Exception {
		// check empty fields
		checkEmptyFields();
		
		Kejadian userModKejadian = getKejadian();
		
		// timestamp
		userModKejadian.setEditedAt(asDate(getCurrentLocalDateTime()));
		// tw pembuatan
		LocalDateTime twPembuatanLocalDateTime = getTwConversion().convertTwToLocalDateTime(
				twBuatTahunTextbox.getValue(), twBuatTanggalJamTextbox.getValue());
		userModKejadian.setTwPembuatanDateTime(asDate(twPembuatanLocalDateTime));
		userModKejadian.setTwPembuatanTimezone(twBuatTimeZoneCombobox.getSelectedItem().getValue());
		// tw kejadian
		LocalDateTime twKejadianLocalDateTime = getTwConversion().convertTwToLocalDateTime(
				twJadiTahunTextbox.getValue(),twJadiTanggalJamTextbox.getValue());
		userModKejadian.setTwKejadianDateTime(asDate(twKejadianLocalDateTime));
		userModKejadian.setTwKejadianTimezone(
				(TimezoneInd) twJadiTimeZoneTextbox.getAttribute("twJadiTimeZoneInd"));
		
		userModKejadian.setKotamaops(kotamaopsCombobox.getSelectedItem().getValue());
		userModKejadian.setPropinsi(propCombobox.getSelectedItem().getValue());
		userModKejadian.setKabupatenKotamadya(kabupatenCombobox.getSelectedItem().getValue());
		userModKejadian.setKecamatan(kecamatanCombobox.getSelectedItem().getValue());
		userModKejadian.setKelurahan(kelurahanCombobox.getSelectedItem().getValue());
		
		userModKejadian.setKoordinatGps(koordGpsTextbox.getValue());
		userModKejadian.setKoordinatPeta(koordPetaTextbox.getValue());
		userModKejadian.setBujurLintang(bujurLintangTextbox.getValue());
		userModKejadian.setKampung(kampungTextbox.getValue());
		userModKejadian.setJalan(jalanTextbox.getValue());
		
		userModKejadian.setKronologis(kronologisTextbox.getValue());

		userModKejadian.setJenisKejadian(jenisKejadianCombobox.getSelectedItem().getValue());
		userModKejadian.setMotifKejadian(motifKejadianCombobox.getSelectedItem().getValue());
		userModKejadian.setPelakuKejadian(pelakuKejadianCombobox.getSelectedItem().getValue());
		userModKejadian.setKeteranganPelaku(keteranganPelakuTextbox.getValue());
		userModKejadian.setSasaran(sasaranTextbox.getValue());		
		
		return userModKejadian;
	}

	private void checkEmptyFields() throws Exception {
		if (twBuatTahunTextbox.getValue().isEmpty()) {
			throw new Exception("Tw Pembuatan Kejadian Tahun belum diisi.");
		}
		if (twBuatTanggalJamTextbox.getValue().isEmpty()) {
			throw new Exception("Tw Pembuatan Kejadian Tanggal dan Jam belum diisi.");
		}
		if (twJadiTanggalJamTextbox.getValue().isEmpty()) {
			throw new Exception("Tw Kejadian Tanggal dan Jam belum diisi.");
		}
		if (kotamaopsCombobox.getValue().isEmpty()) {
			throw new Exception("Kotamaops belum dipilih.");
		}
		if (propCombobox.getValue().isEmpty()) {
			throw new Exception("Propinsis belum dipilih.");
		}
		if (kabupatenCombobox.getValue().isEmpty()) {
			throw new Exception("Kabupaten / Kotamadya belum dipilih.");
		}
		if (kecamatanCombobox.getValue().isEmpty()) {
			throw new Exception("Kecamatan belum dipilih.");
		}
		if (kelurahanCombobox.getValue().isEmpty()) {
			throw new Exception("Kelurahan belum dipilih.");
		}
		if (jenisKejadianCombobox.getValue().isEmpty()) {
			throw new Exception("Jenis Kejadian belum dipilih.");
		}
		if (motifKejadianCombobox.getValue().isEmpty()) {
			throw new Exception("Motif Kejadian belum dipilih.");
		}
		
	}

	public void onClick$cancelButton(Event event) throws Exception {
		// send event - onCancel
		Events.sendEvent(Events.ON_CANCEL, kejadianMenonjolDialogWin, null);
		
		// detach
		kejadianMenonjolDialogWin.detach();
	}

	public KejadianData getKejadianData() {
		return kejadianData;
	}

	public void setKejadianData(KejadianData kejadianData) {
		this.kejadianData = kejadianData;
	}

	public Kejadian getKejadian() {
		return kejadian;
	}

	public void setKejadian(Kejadian kejadian) {
		this.kejadian = kejadian;
	}

	public KotamaopsDao getKotamaopsDao() {
		return kotamaopsDao;
	}

	public void setKotamaopsDao(KotamaopsDao kotamaopsDao) {
		this.kotamaopsDao = kotamaopsDao;
	}

	public PropinsiDao getPropinsiDao() {
		return propinsiDao;
	}

	public void setPropinsiDao(PropinsiDao propinsiDao) {
		this.propinsiDao = propinsiDao;
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

	public KejadianJenisDao getKejadianJenisDao() {
		return kejadianJenisDao;
	}

	public void setKejadianJenisDao(KejadianJenisDao kejadianJenisDao) {
		this.kejadianJenisDao = kejadianJenisDao;
	}

	public KejadianMotifDao getKejadianMotifDao() {
		return kejadianMotifDao;
	}

	public void setKejadianMotifDao(KejadianMotifDao kejadianMotifDao) {
		this.kejadianMotifDao = kejadianMotifDao;
	}

	public KejadianPelakuDao getKejadianPelakuDao() {
		return kejadianPelakuDao;
	}

	public void setKejadianPelakuDao(KejadianPelakuDao kejadianPelakuDao) {
		this.kejadianPelakuDao = kejadianPelakuDao;
	}

	public KejadianDao getKejadianDao() {
		return kejadianDao;
	}

	public void setKejadianDao(KejadianDao kejadianDao) {
		this.kejadianDao = kejadianDao;
	}

	public LocalDateTime getCurrentLocalDateTime() {
		return currentLocalDateTime;
	}

	public void setCurrentLocalDateTime(LocalDateTime currentLocalDateTime) {
		this.currentLocalDateTime = currentLocalDateTime;
	}

	public Kotamaops getSettingsKotamaops() {
		return settingsKotamaops;
	}

	public void setSettingsKotamaops(Kotamaops settingsKotamaops) {
		this.settingsKotamaops = settingsKotamaops;
	}

	public ZoneId getZoneId() {
		return zoneId;
	}

	public void setZoneId(ZoneId zoneId) {
		this.zoneId = zoneId;
	}

	public TwConversion getTwConversion() {
		return twConversion;
	}

	public void setTwConversion(TwConversion twConversion) {
		this.twConversion = twConversion;
	}
	
}
