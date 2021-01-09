package mil.pusdalops.k2.webui.sinkronisasi;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.domain.kejadian.KejadianJenis;
import mil.pusdalops.domain.kejadian.KejadianMotif;
import mil.pusdalops.domain.kerugian.Kerugian;
import mil.pusdalops.domain.kerugian.KerugianJenis;
import mil.pusdalops.domain.kerugian.KerugianKondisi;
import mil.pusdalops.domain.kerugian.KerugianSatuan;
import mil.pusdalops.domain.serial.DocumentSerialNumber;
import mil.pusdalops.domain.wilayah.Kabupaten_Kotamadya;
import mil.pusdalops.domain.wilayah.Kecamatan;
import mil.pusdalops.domain.wilayah.Kelurahan;
import mil.pusdalops.domain.wilayah.Propinsi;
import mil.pusdalops.k2.persistence.kabupaten_kotamadya.dao.Kabupaten_KotamadyaDao;
import mil.pusdalops.k2.persistence.kecamatan.dao.KecamatanDao;
import mil.pusdalops.k2.persistence.kejadian.dao.KejadianDao;
import mil.pusdalops.k2.persistence.kejadian.jenis.dao.KejadianJenisDao;
import mil.pusdalops.k2.persistence.kejadian.motif.dao.KejadianMotifDao;
import mil.pusdalops.k2.persistence.kelurahan.dao.KelurahanDao;
import mil.pusdalops.k2.persistence.kerugian.jenis.dao.KerugianJenisDao;
import mil.pusdalops.k2.persistence.kerugian.kondisi.dao.KerugianKondisiDao;
import mil.pusdalops.k2.persistence.kerugian.satuan.dao.KerugianSatuanDao;
import mil.pusdalops.k2.persistence.propinsi.dao.PropinsiDao;
import mil.pusdalops.k2.webui.common.GFCBaseController;

public class KejadianMenonjolSinkronisasiDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5814892135323518046L;

	private KejadianDao kejadianDao;
	private PropinsiDao propinsiDao;
	private Kabupaten_KotamadyaDao kabupaten_KotamadyaDao;
	private KecamatanDao kecamatanDao;
	private KelurahanDao kelurahanDao;
	private KejadianJenisDao kejadianJenisDao;
	private KejadianMotifDao kejadianMotifDao;
	private KerugianJenisDao kerugianJenisDao;
	private KerugianKondisiDao kerugianKondisiDao;
	private KerugianSatuanDao kerugianSatuanDao;
	
	private Window kejadianMenonjolSinkronisasiDialogWin;
	private Label sinkronisasiDialogLabel, kabupatenKotRef, kecamatanRef,
		kelurahanRef, synchronizeToLocalLabel,
		jenisKejadianRef, motifKejadianRef;
	private Textbox kejadianIdTextbox, kotamaopsTextbox, propinsiTextbox,
		kabupatenKotTextbox, kecamatanTextbox, kelurahanTextbox,
		jenisKejadianTextbox, motifKejadianTextbox;
	private Button synchronizeToLocalButton;
	private Checkbox referensiWilayahCheckbox, referensiKejadianCheckbox,
		referensiKerugianCheckbox;
	private Listbox kejadianKerugianListbox;
	
	private SinkronisasiData sinkronisasiData;
	private Kejadian cloudKejadian;
	private LocalDateTime currentLocalDateTime;
	private ApplicationContext ctx;
	
	private Map<Boolean, Propinsi> propinsiReferenceMap = new HashMap<Boolean, Propinsi>();
	private Map<Boolean, Kabupaten_Kotamadya> kabupatenKotReferenceMap = new HashMap<Boolean, Kabupaten_Kotamadya>();
	private Map<Boolean, Kecamatan> kecamatanReferenceMap = new HashMap<Boolean, Kecamatan>();
	private Map<Boolean, Kelurahan> kelurahanReferenceMap = new HashMap<Boolean, Kelurahan>();
	private Map<Boolean, KejadianJenis> kejadianJenisReferenceMap = new HashMap<Boolean, KejadianJenis>();
	private Map<Boolean, KejadianMotif> kejadianMotifReferenceMap = new HashMap<Boolean, KejadianMotif>();
	// private Map<Boolean, KejadianPelaku> kejadianPelakuReferenceMap = new HashMap<Boolean, KejadianPelaku>();
	
	private static Logger log = Logger.getLogger(KejadianMenonjolSinkronisasiDialogControl.class);
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		// sinkronisasiData
		setSinkronisasiData(
				(SinkronisasiData) arg.get("sinkronisasiData"));
	}

	public void onCreate$kejadianMenonjolSinkronisasiDialogWin(Event event) throws Exception {
		setCloudKejadian(
				getSinkronisasiData().getCloudKejadian());
		setCurrentLocalDateTime(
				getSinkronisasiData().getCurrentLocalDateTime());
		
		sinkronisasiDialogLabel.setValue("Sinkronisasi Data Kejadian dari Kotamaops");

		// init ref label
		initReferenceLabel();
		
		ctx = new ClassPathXmlApplicationContext("CommonContext-Cloud-Dao.xml");
		
		// display
		displayKejadianInfo();
		
		// check wilayah references
		checkWilayahReferences();
		
		// check kejadian references
		checkKejadianReferences();
		
	}

	private void initReferenceLabel() {
		kabupatenKotRef.setValue(""); 
		kecamatanRef.setValue("");
		kelurahanRef.setValue("");
		jenisKejadianRef.setValue(""); 
		motifKejadianRef.setValue("");
	}

	private void displayKejadianInfo() throws Exception {
		KejadianDao cloudKejadianDao = 
				(KejadianDao) ctx.getBean("kejadianDao");
		
		kejadianIdTextbox.setValue(
				getCloudKejadian().getSerialNumber().getSerialComp());
		
		// kotamaops
		Kejadian cloudKejadianKotamaopsByProxy = 
				cloudKejadianDao.findKejadianKotamaopsByProxy(getCloudKejadian().getId());
		kotamaopsTextbox.setValue(
				cloudKejadianKotamaopsByProxy.getKotamaops().getKotamaopsName());
		
		// popinsi
		Kejadian cloudKejadianPropinsiByProxy =
				cloudKejadianDao.findKejadianPropinsiByProxy(getCloudKejadian().getId());
		propinsiTextbox.setValue(cloudKejadianPropinsiByProxy.getPropinsi().getNamaPropinsi());
		
		// kabupaten / kotamadya
		Kejadian cloudKejadianKabupatenKotByProxy =
				cloudKejadianDao.findKejadianKabupatenKotamadyaByProxy(getCloudKejadian().getId());
		kabupatenKotTextbox.setValue(
				cloudKejadianKabupatenKotByProxy.getKabupatenKotamadya().getNamaKabupaten());
		
		// kecamatan
		Kejadian cloudKejadianKecamatanByProxy =
				cloudKejadianDao.findKejadianKecamatanByProxy(getCloudKejadian().getId());
		kecamatanTextbox.setValue(cloudKejadianKecamatanByProxy.getKecamatan().getNamaKecamatan());
		
		// kelurahan
		Kejadian cloudKejadianKelurahanByProxy =
				cloudKejadianDao.findKejadianKelurahanByProxy(getCloudKejadian().getId());
		kelurahanTextbox.setValue(cloudKejadianKelurahanByProxy.getKelurahan().getNamaKelurahan());
		
		// jenis kejadian
		jenisKejadianTextbox.setValue(getCloudKejadian().getJenisKejadian().getNamaJenis()); 
		
		// motif kejadian
		motifKejadianTextbox.setValue(getCloudKejadian().getMotifKejadian().getNamaMotif());
				
		// kerugian -- INCLUDING check references
		Kejadian cloudKejadianKerugianByProxy = cloudKejadianDao.findKejadianKerugiansByProxy(getCloudKejadian().getId());
		kejadianKerugianListbox.setModel(new ListModelList<Kerugian>(cloudKejadianKerugianByProxy.getKerugians()));
		kejadianKerugianListbox.setItemRenderer(getKerugianListboxListitemRenderer());
		kejadianKerugianListbox.setEmptyMessage("Tidak ada DATA Kerugian");
	}	
	
	private ListitemRenderer<Kerugian> getKerugianListboxListitemRenderer() {
		
		return new ListitemRenderer<Kerugian>() {
			
			@Override
			public void render(Listitem item, Kerugian cloudKerugian, int index) throws Exception {
				Listcell lc;
				
				// pihak
				lc = new Listcell("Pihak "+cloudKerugian.getParaPihak().toString());
				lc.setStyle("background-color: #f98a00");
				lc.setParent(item);
				
				// jenis kerugian
				lc = new Listcell(cloudKerugian.getKerugianJenis().getNamaJenis());
				lc.setValue(cloudKerugian.getKerugianJenis());
				lc.setParent(item);
				
				// status
				lc = initKerugianJenisReferenceCheck(new Listcell(), cloudKerugian.getKerugianJenis());
				lc.setParent(item);
				
				// kondisi kerugian
				lc = new Listcell(cloudKerugian.getKerugianKondisi().getNamaKondisi());
				lc.setValue(cloudKerugian.getKerugianKondisi());
				lc.setParent(item);
				
				// status
				lc = initKerugianKondisiReferenceCheck(new Listcell(), cloudKerugian.getKerugianKondisi());
				lc.setParent(item);
				
				// satuan kerugian
				lc = new Listcell(cloudKerugian.getKerugianSatuan().getSatuan());
				lc.setValue(cloudKerugian.getKerugianSatuan());
				lc.setParent(item);
				
				// status
				lc = initKerugianSatuanReferenceCheck(new Listcell(), cloudKerugian.getKerugianSatuan());
				lc.setParent(item);

				item.setValue(cloudKerugian);
				
			}

			private Listcell initKerugianJenisReferenceCheck(Listcell listcell, KerugianJenis cloudKerugianJenis) throws Exception {
				boolean kerugianJenisNameFound = false;
				List<KerugianJenis> kerugianJenisList = getKerugianJenisDao().findAllKerugianJenis();
				for (KerugianJenis kerugianJenis : kerugianJenisList) {
					if (cloudKerugianJenis.getNamaJenis().compareTo(kerugianJenis.getNamaJenis())==0) {
						kerugianJenisNameFound = true;
						listcell.setValue(kerugianJenis);
						listcell.setLabel("[OK]");
						listcell.setStyle("color:black;");
						break;
					}
				}
				if (!kerugianJenisNameFound) {
					listcell.setValue(null);
					listcell.setLabel("[TIDAK Ditemukan]");
					listcell.setStyle("color:red;");
				}
				return listcell;
			}
			
			private Listcell initKerugianKondisiReferenceCheck(Listcell listcell, KerugianKondisi cloudKerugianKondisi) throws Exception {
				boolean kerugianKondisiNameFound = false;
				List<KerugianKondisi> kerugianKondisiList = getKerugianKondisiDao().findAllKerugianKondisi();
				for (KerugianKondisi kerugianKondisi : kerugianKondisiList) {
					if (cloudKerugianKondisi.getNamaKondisi().compareTo(kerugianKondisi.getNamaKondisi())==0) {
						kerugianKondisiNameFound = true;
						listcell.setValue(kerugianKondisi);
						listcell.setLabel("[OK]");
						listcell.setStyle("color:black;");
						break;
					}
				}
				if (!kerugianKondisiNameFound) {
					listcell.setValue(null);
					listcell.setLabel("[TIDAK Ditemukan]");
					listcell.setStyle("color:red;");
				}
				return listcell;
			}

			private Listcell initKerugianSatuanReferenceCheck(Listcell listcell, KerugianSatuan cloudKerugianSatuan) throws Exception {
				boolean kerugianSatuanNameFound = false;
				List<KerugianSatuan> kerugianSatuanList = getKerugianSatuanDao().findAllKerugianSatuan();
				for (KerugianSatuan kerugianSatuan : kerugianSatuanList) {
					if (cloudKerugianSatuan.getSatuan().compareTo(kerugianSatuan.getSatuan())==0) {
						kerugianSatuanNameFound = true;
						listcell.setValue(kerugianSatuan);
						listcell.setLabel("[OK]");
						listcell.setStyle("color:black;");
						break;
					}
				}
				if (!kerugianSatuanNameFound) {
					listcell.setValue(null);
					listcell.setLabel("[TIDAK Ditemukan]");
					listcell.setStyle("color:red;");
				}
				return listcell;
			}
		};
	}

	public void onAfterRender$kejadianKerugianListbox(Event event) throws Exception {
		boolean rowEval = true;
		boolean listEval = true;
		Listcell lc;
		List<Listitem> listitem = kejadianKerugianListbox.getItems();
		for (Listitem item : listitem) {
			// jenis kerugian
			lc = (Listcell) item.getChildren().get(2);
			KerugianJenis kerugianJenis = lc.getValue();
			
			// kondisi kerugian
			lc = (Listcell) item.getChildren().get(4);
			KerugianKondisi kerugianKondisi = lc.getValue();
			
			// satuan kerugian
			lc = (Listcell) item.getChildren().get(6);
			KerugianSatuan kerugianSatuan = lc.getValue();
			
			rowEval = (kerugianJenis!=null) && (kerugianKondisi!=null) && (kerugianSatuan!=null);
			listEval = listEval && rowEval;
		}
		
		referensiKerugianCheckbox.setChecked(listEval);
		referensiKerugianCheckbox.setDisabled(listEval);
	}
	
	/*
	 * REFERENSI WILAYAH
	 */
	
	private void checkWilayahReferences() throws Exception {
		boolean referencePassed = false;
		
		KejadianDao cloudKejadianDao = 
				(KejadianDao) ctx.getBean("kejadianDao");

		Kejadian cloudKejadianPropinsiByProxy =
				cloudKejadianDao.findKejadianPropinsiByProxy(getCloudKejadian().getId());
		Propinsi cloudPropinsi = cloudKejadianPropinsiByProxy.getPropinsi();
		referencePassed = propinsiReferenceCheck(cloudPropinsi);
		
		// check referensi kabupaten/kotamadya
		Propinsi propinsi = propinsiReferenceMap.get(Boolean.TRUE);
		referencePassed = kabupatenKotReferenceCheck(propinsi);
		
		// check referensi kecamatan
		Kabupaten_Kotamadya kabupatenKot = kabupatenKotReferenceMap.get(Boolean.TRUE);
		referencePassed = kecamatanReferenceCheck(kabupatenKot);
		
		// check referenci kelurahan
		Kecamatan kecamatan = kecamatanReferenceMap.get(Boolean.TRUE);
		referencePassed = kelurahanReferenceCheck(kecamatan);
				
		referensiWilayahCheckbox.setChecked(referencePassed);
		referensiWilayahCheckbox.setDisabled(referencePassed);		
	}

	private boolean propinsiReferenceCheck(Propinsi cloudPropinsi) throws Exception {
		boolean propinsiNameFound = false;
		
		List<Propinsi> propinsiList = getPropinsiDao().findAllPropinsi();
		for (Propinsi propinsi : propinsiList) {
			if (cloudPropinsi.getNamaPropinsi().compareTo(propinsi.getNamaPropinsi())==0) {
				log.info(cloudPropinsi+" dengan Nama Propinsi: "+cloudPropinsi.getNamaPropinsi()+" Ditemukan...");
				propinsiNameFound = true;
				propinsiReferenceMap.put(Boolean.TRUE, propinsi);
				break;
			}
		}
		if (!propinsiNameFound) {
			log.info("Nama Propinsi : "+cloudPropinsi.getNamaPropinsi()+" TIDAK Ditemukan...");
			propinsiReferenceMap.put(Boolean.FALSE, null);
		}
		
		return propinsiNameFound;
	}
	
	private boolean kabupatenKotReferenceCheck(Propinsi propinsi) throws Exception {
		boolean kabupatenKotNameFound = false;
		
		Propinsi propinsiKabupatenKotByProxy = 
				getPropinsiDao().findKabupatenKotamadyaByProxy(propinsi.getId());
		List<Kabupaten_Kotamadya> kabupatenKotList = propinsiKabupatenKotByProxy.getKabupatenkotamadyas();
		for (Kabupaten_Kotamadya kabupatenKot : kabupatenKotList) {
			if (kabupatenKot.getNamaKabupaten().compareTo(kabupatenKotTextbox.getValue())==0) {
				log.info(kabupatenKot+" dengan Nama Kabupaten/Kotamadya: "+kabupatenKot.getNamaKabupaten()+" dari Cloud Ditemukan...");
				kabupatenKotReferenceMap.put(Boolean.TRUE, kabupatenKot);
				kabupatenKotNameFound = true;
				//
				kabupatenKotRef.setValue("[OK]");
				kabupatenKotRef.setStyle("color: black;");
				break;
			}
		}
		if (!kabupatenKotNameFound) {
			log.info("Nama Kabupaten/Kotamadya : "+kabupatenKotTextbox.getValue()+" dari Cloud TIDAK Ditemukan...");
			kabupatenKotRef.setValue("[TIDAK Ditemukan]");
			kabupatenKotRef.setStyle("color: red;");
			kabupatenKotReferenceMap.put(Boolean.FALSE, null);
		}
		
		return kabupatenKotNameFound;
	}

	private boolean kecamatanReferenceCheck(Kabupaten_Kotamadya kabupatenKot) throws Exception {
		boolean kecamatanNameFound = false;
		if (kabupatenKot==null) {
			// no reference to look for kecamatan
			log.info("kabupatenKot is NULL");
			kecamatanRef.setValue("[TIDAK ditemukan]");
			kecamatanRef.setStyle("color: red;");
			kecamatanReferenceMap.put(Boolean.FALSE, null);
			
			return kecamatanNameFound;
		}
		Kabupaten_Kotamadya kabupatenKotKecamatanByProxy = 
				getKabupaten_KotamadyaDao().findKecamatanByProxy(kabupatenKot.getId());
		List<Kecamatan> kecamatanList = kabupatenKotKecamatanByProxy.getKecamatans();
		for (Kecamatan kecamatan : kecamatanList) {
			if (kecamatanTextbox.getValue().compareTo(kecamatan.getNamaKecamatan())==0) {
				log.info(kecamatan+" dengan Nama Kecamatan: "+kecamatan.getNamaKecamatan()+" dari Cloud Ditemukan...");
				kecamatanReferenceMap.put(Boolean.TRUE, kecamatan);
				kecamatanNameFound = true;
				//
				kecamatanRef.setValue("[OK]");
				kecamatanRef.setStyle("color: black;");
				break;
			}
		}
		if (!kecamatanNameFound) {
			log.info("Nama Kecamatan : "+kecamatanTextbox.getValue()+" dari Cloud TIDAK Ditemukan...");
			kecamatanRef.setValue("TIDAK Ditemukan");
			kecamatanRef.setStyle("color: red;");
			kecamatanReferenceMap.put(Boolean.FALSE, null);
		}
		
		return kecamatanNameFound;
	}
	
	private boolean kelurahanReferenceCheck(Kecamatan kecamatan) throws Exception {
		boolean kelurahanNameFound = false;
		if (kecamatan==null) {
			// no reference to look for kelurahan
			log.info("kecamatan is NULL");
			kelurahanRef.setValue("[TIDAK ditemukan]");
			kelurahanRef.setStyle("color: red;");
			kelurahanReferenceMap.put(Boolean.FALSE, null);
			
			return kelurahanNameFound;
		}
		Kecamatan kecamatanKelurahanByProxy = getKecamatanDao().findKelurahanByProxy(kecamatan.getId());
		List<Kelurahan> kelurahanList = kecamatanKelurahanByProxy.getKelurahans();
		for (Kelurahan kelurahan : kelurahanList) {
			if (kelurahanTextbox.getValue().compareTo(kelurahan.getNamaKelurahan())==0) {
				log.info(kelurahan+" dengan Nama Kelurahan: "+kelurahan.getNamaKelurahan()+" dari Cloud Ditemukan...");
				kelurahanReferenceMap.put(Boolean.TRUE, kelurahan);
				kelurahanNameFound = true;
				//
				kelurahanRef.setValue("[OK]");
				kelurahanRef.setStyle("color:black;");
				break;
			}
		}
		if (!kelurahanNameFound) {
			log.info("Nama Kelurahan : "+kelurahanTextbox.getValue()+" dari Cloud TIDAK Ditemukan...");
			kelurahanRef.setValue("[TIDAK Ditemukan]");
			kelurahanRef.setStyle("color: red;");
			kelurahanReferenceMap.put(Boolean.FALSE, null);
		}
		
		return kelurahanNameFound;
	}

	public void onCheck$referensiWilayahCheckbox(Event event) throws Exception {
		log.info("Resolving Kotamaops Wilayah from Cloud to Local...");
		
		// get the local propinsi
		Propinsi propinsi = propinsiReferenceMap.get(Boolean.TRUE);
		log.info("Local Propinsi: "+propinsi.toString());
		
		// resolve kabupaten / kotamadya
		Kabupaten_Kotamadya kabupatenKot = kabupatenKotReferenceMap.get(Boolean.TRUE);
		log.info("Local Kabupaten/Kotamadya: "+kabupatenKot);
		if (kabupatenKot==null) {
			kabupatenKot = resolveKabupatenKotamadya(propinsi);
			// put into the reference map -- for saving into the local database
			kabupatenKotReferenceMap.put(Boolean.TRUE, kabupatenKot);
			// resolve
			log.info("Kabupaten/Kotamadya: [Resolved] "+kabupatenKot);
		}
		
		// resolve kecamatan
		Kecamatan kecamatan = kecamatanReferenceMap.get(Boolean.TRUE);
		log.info("Local Kecamatan: "+kecamatan);
		if (kecamatan==null) {
			kecamatan = resolveKecamatan(kabupatenKot);
			// put into the reference map -- for saving into the local database
			kecamatanReferenceMap.put(Boolean.TRUE, kecamatan);
			// resolve
			log.info("Kecamatan: [RESOLVED] "+kecamatan);
		}
		
		// resolve kelurahan
		Kelurahan kelurahan = kelurahanReferenceMap.get(Boolean.TRUE);
		log.info("Local Kelurahan: "+kelurahan);
		if (kelurahan==null) {
			kelurahan = resolveKelurahan(kecamatan);
			// put into the reference map -- for saving into the local database
			kelurahanReferenceMap.put(Boolean.TRUE, kelurahan);
			// resolve
			log.info("Kelurahan: [RESOLVED] "+kelurahan);
		}
		
		referensiWilayahCheckbox.setDisabled(true);
	}

	private Kabupaten_Kotamadya resolveKabupatenKotamadya(Propinsi propinsi) throws Exception {
		Propinsi propinsiKabupatenKotByProxy = getPropinsiDao().findKabupatenKotamadyaByProxy(propinsi.getId());
		List<Kabupaten_Kotamadya> kabupatenKotList = propinsiKabupatenKotByProxy.getKabupatenkotamadyas();
		// create a new Kabupaten_Kotamadya object -- with name from textbox
		Kabupaten_Kotamadya kabupatenKot = new Kabupaten_Kotamadya();
		kabupatenKot.setCreatedAt(asDate(getCurrentLocalDateTime()));
		kabupatenKot.setEditedAt(asDate(getCurrentLocalDateTime()));
		kabupatenKot.setNamaKabupaten(kabupatenKotTextbox.getValue());
		// add to the list
		kabupatenKotList.add(kabupatenKot);
		// update
		getPropinsiDao().update(propinsiKabupatenKotByProxy);
		// notify
		kabupatenKotRef.setValue("[OK]");
		kabupatenKotRef.setStyle("color: black;");
		
		return kabupatenKot;
	}

	private Kecamatan resolveKecamatan(Kabupaten_Kotamadya kabupatenKot) throws Exception {
		Kabupaten_Kotamadya kabupatenKotKecamatanByProxy = getKabupaten_KotamadyaDao().findKecamatanByProxy(kabupatenKot.getId());
		List<Kecamatan> kecamatanList = kabupatenKotKecamatanByProxy.getKecamatans();
		// create a new kecamatan object -- with name from textbox
		Kecamatan kecamatan = new Kecamatan();
		kecamatan.setCreatedAt(asDate(getCurrentLocalDateTime()));
		kecamatan.setEditedAt(asDate(getCurrentLocalDateTime()));
		kecamatan.setNamaKecamatan(kecamatanTextbox.getValue());
		// add to the list
		kecamatanList.add(kecamatan);
		// update
		getKabupaten_KotamadyaDao().update(kabupatenKotKecamatanByProxy);
		// notify
		kecamatanRef.setValue("[OK]");
		kecamatanRef.setStyle("color: black;");
		
		return kecamatan;
	}
	
	private Kelurahan resolveKelurahan(Kecamatan kecamatan) throws Exception {
		Kecamatan kecamatanKelurahanByProxy = getKecamatanDao().findKelurahanByProxy(kecamatan.getId());
		List<Kelurahan> kelurahanList = kecamatanKelurahanByProxy.getKelurahans();
		// create a new kelurahan object -- with name from textbox
		Kelurahan kelurahan = new Kelurahan();
		kelurahan.setCreatedAt(asDate(getCurrentLocalDateTime()));
		kelurahan.setEditedAt(asDate(getCurrentLocalDateTime()));
		kelurahan.setNamaKelurahan(kelurahanTextbox.getValue());
		// add to list
		kelurahanList.add(kelurahan);
		// update
		getKecamatanDao().update(kecamatanKelurahanByProxy);
		// notify
		kelurahanRef.setValue("[OK]");
		kelurahanRef.setStyle("color: black;");
		
		return kelurahan;
	}
	
	/*
	 * REFERENSI KEJADIAN
	 */
	
	private void checkKejadianReferences() throws Exception {

		// jenis kejadian
		boolean referenceJenisPassed = kejadianJenisReferenceCheck(); 
		
		// motif kejadian
		boolean referenceMotifPassed = kejadianMotifReferenceCheck();
		
		referensiKejadianCheckbox.setChecked(referenceJenisPassed && referenceMotifPassed);
		referensiKejadianCheckbox.setDisabled(referenceJenisPassed && referenceMotifPassed);
	}

	private boolean kejadianJenisReferenceCheck() throws Exception {
		boolean kejadianJenisFound = false;
		List<KejadianJenis> kejadianJenisList = getKejadianJenisDao().findAllKejadianJenis();
		for (KejadianJenis kejadianJenis : kejadianJenisList) {
			if (jenisKejadianTextbox.getValue().compareTo(kejadianJenis.getNamaJenis())==0) {
				log.info(kejadianJenis+" dengan nama Kejadian Jenis: "+kejadianJenis.getNamaJenis()+" dari Cloud Ditemukan...");
				kejadianJenisReferenceMap.put(Boolean.TRUE, kejadianJenis);
				kejadianJenisFound = true;
				//
				jenisKejadianRef.setValue("[OK]");
				jenisKejadianRef.setStyle("color:black;");
				break;
			}
		}
		if (!kejadianJenisFound) {
			log.info("Nama Jenis Kejadian: "+jenisKejadianTextbox.getValue()+" dari Cloud TIDAK Ditemukan...");
			jenisKejadianRef.setValue("[TIDAK Ditemukan]");
			jenisKejadianRef.setStyle("color: red;");
			kejadianJenisReferenceMap.put(Boolean.FALSE, null);
		}
		
		return kejadianJenisFound;
	}

	private boolean kejadianMotifReferenceCheck() throws Exception {
		boolean kejadianMotifFound = false;
		List<KejadianMotif> kejadianMotifList = getKejadianMotifDao().findAllKejadianMotif();
		for (KejadianMotif kejadianMotif : kejadianMotifList) {
			if (motifKejadianTextbox.getValue().compareTo(kejadianMotif.getNamaMotif())==0) {
				log.info(kejadianMotif+" dengan nama Kejadian Motif: "+kejadianMotif.getNamaMotif()+" dari Cloud Ditemukan...");
				kejadianMotifReferenceMap.put(Boolean.TRUE, kejadianMotif);
				kejadianMotifFound = true;
				//
				motifKejadianRef.setValue("[OK]");
				motifKejadianRef.setStyle("color:black;");
			}
		}
		if (!kejadianMotifFound) {
			log.info("Nama Motif Kejadian: "+motifKejadianTextbox.getValue()+" dari Cloud TIDAK Ditemukan...");
			motifKejadianRef.setValue("[TIDAK Ditemukan]");
			motifKejadianRef.setStyle("color:red;");
			kejadianMotifReferenceMap.put(Boolean.FALSE, null);
		}
		
		return kejadianMotifFound;
	}

	public void onCheck$referensiKejadianCheckbox(Event event) throws Exception {
		log.info("Resolving Kejadian Jenis dan Motif from Cloud to Local...");
		
		KejadianJenis kejadianJenis = kejadianJenisReferenceMap.get(Boolean.TRUE);
		log.info("Local Jenis Kejadian: "+kejadianJenis);
		if (kejadianJenis==null) {
			kejadianJenis = resolveKejadianJenis();
			// put into the reference map -- for saving into the local database
			kejadianJenisReferenceMap.put(Boolean.TRUE, kejadianJenis);
			// resolved
			log.info("Jenis Kejadian: [RESOLVED] "+kejadianJenis);
		}
		
		KejadianMotif kejadianMotif = kejadianMotifReferenceMap.get(Boolean.TRUE);
		log.info("Local Motif Kejadian: "+kejadianMotif);
		if (kejadianMotif==null) {
			kejadianMotif = resolveKejadianMotif();
			// put into the reference map -- for saving into the local database
			kejadianMotifReferenceMap.put(Boolean.TRUE, kejadianMotif);
			// resolved
			log.info("Motif Kejadian: [RESOLVED] "+kejadianMotif);
		}
		
		referensiKejadianCheckbox.setDisabled(true);
	}

	private KejadianJenis resolveKejadianJenis() throws Exception {
		KejadianJenis kejadianJenis = new KejadianJenis();
		kejadianJenis.setCreatedAt(asDate(getCurrentLocalDateTime()));
		kejadianJenis.setEditedAt(asDate(getCurrentLocalDateTime()));
		kejadianJenis.setNamaJenis(jenisKejadianTextbox.getValue());
		// save
		getKejadianJenisDao().save(kejadianJenis);
		// notify
		jenisKejadianRef.setValue("[OK]");
		jenisKejadianRef.setStyle("color:black;");
		
		return kejadianJenis;
	}	
	
	private KejadianMotif resolveKejadianMotif() throws Exception {
		KejadianMotif kejadianMotif = new KejadianMotif();
		kejadianMotif.setCreatedAt(asDate(getCurrentLocalDateTime()));
		kejadianMotif.setEditedAt(asDate(getCurrentLocalDateTime()));
		kejadianMotif.setNamaMotif(motifKejadianTextbox.getValue());
		// save
		getKejadianMotifDao().save(kejadianMotif);
		// notify
		motifKejadianRef.setValue("[OK]");
		motifKejadianRef.setStyle("color:black;");
		
		return kejadianMotif;
	}
	
	/*
	 * REFERENSI KERUGIAN
	 */
	
	public void onCheck$referensiKerugianCheckbox(Event event) throws Exception {
		log.info("Resolving Kejadian Kerugian from Cloud to Local...");
		
		List<Listitem> listitem = kejadianKerugianListbox.getItems();
		for (Listitem item : listitem) {
			Listcell lc;
			
			// kerugian jenis
			lc = (Listcell) item.getChildren().get(1);
			KerugianJenis cloudKerugianJenis = lc.getValue();
			
			// status
			lc = (Listcell) item.getChildren().get(2);
			log.info("Jenis Kerugian: "+lc.getValue());
			if (lc.getValue()==null) {
				// has it been resolved previously?
				KerugianJenis kerugianJenis = kerugianJenisPreviouslyResolved(cloudKerugianJenis);
				if (kerugianJenis==null) {
					// resolve to local database
					kerugianJenis = resolveKerugianJenis(cloudKerugianJenis);
				}
				lc.setValue(kerugianJenis);
				// notify
				lc.setLabel("[OK]");
				lc.setStyle("color:black;");
			}
			
			// kerugian kondisi
			lc = (Listcell) item.getChildren().get(3);
			KerugianKondisi cloudKerugianKondisi = lc.getValue();
			
			// status
			lc = (Listcell) item.getChildren().get(4);
			log.info("Kondisi Kerugian: "+lc.getValue());
			if (lc.getValue()==null) {
				// has it been resolved previously?
				KerugianKondisi kerugianKondisi = kerugianKondisiPreviouslyResolved(cloudKerugianKondisi);
				if (kerugianKondisi==null) {
					// resolve to local database
					kerugianKondisi = resolveKerugianKondisi(cloudKerugianKondisi);					
				}
				lc.setValue(kerugianKondisi);
				// notify
				lc.setLabel("[OK]");
				lc.setStyle("color:black;");
			}
			
			// kerugian satuan
			lc = (Listcell) item.getChildren().get(5);
			KerugianSatuan cloudKerugianSatuan = lc.getValue();
			
			// status
			lc = (Listcell) item.getChildren().get(6);
			log.info("Satuan Kerugian: "+lc.getValue());
			if (lc.getValue()==null) {
				// has it been resolved previously?
				KerugianSatuan kerugianSatuan = kerugianSatuanPreviouslyResolved(cloudKerugianSatuan);
				if (kerugianSatuan==null) {
					// resolve to local database
					kerugianSatuan = resolveKerugianSatuan(cloudKerugianSatuan);					
				}
				lc.setValue(kerugianSatuan);
				// notify
				lc.setLabel("[OK]");
				lc.setStyle("color:black;");
			}
		}
		
		referensiKerugianCheckbox.setDisabled(true);
	}

	private KerugianSatuan kerugianSatuanPreviouslyResolved(KerugianSatuan cloudKerugianSatuan) throws Exception {
		List<KerugianSatuan> kerugianSatuanList = getKerugianSatuanDao().findAllKerugianSatuan();
		for (KerugianSatuan kerugianSatuan : kerugianSatuanList) {
			if (cloudKerugianSatuan.getSatuan().compareTo(kerugianSatuan.getSatuan())==0) {
				return kerugianSatuan;
			}
		}
		
		return null;
	}

	private KerugianKondisi kerugianKondisiPreviouslyResolved(KerugianKondisi cloudKerugianKondisi) throws Exception {
		List<KerugianKondisi> kerugianKondisiList = getKerugianKondisiDao().findAllKerugianKondisi();
		for (KerugianKondisi kerugianKondisi : kerugianKondisiList) {
			if (cloudKerugianKondisi.getNamaKondisi().compareTo(kerugianKondisi.getNamaKondisi())==0) {
				return kerugianKondisi;
			}
		}
		
		return null;
	}

	private KerugianJenis kerugianJenisPreviouslyResolved(KerugianJenis cloudKerugianJenis) throws Exception {
		List<KerugianJenis> kerugianJenisList = getKerugianJenisDao().findAllKerugianJenis();
		for (KerugianJenis kerugianJenis : kerugianJenisList) {
			if (cloudKerugianJenis.getNamaJenis().compareTo(kerugianJenis.getNamaJenis())==0) {
				return kerugianJenis;
			}
		}
		
		return null;
	}

	private KerugianJenis resolveKerugianJenis(KerugianJenis cloudKerugianJenis) throws Exception {
		KerugianJenis kerugianJenis = new KerugianJenis();
		kerugianJenis.setCreatedAt(asDate(getCurrentLocalDateTime()));
		kerugianJenis.setEditedAt(asDate(getCurrentLocalDateTime()));
		kerugianJenis.setNamaJenis(cloudKerugianJenis.getNamaJenis());
		kerugianJenis.setTipeKerugian(cloudKerugianJenis.getTipeKerugian());
		// save
		getKerugianJenisDao().save(kerugianJenis);
		
		return kerugianJenis;
	}	
	
	private KerugianKondisi resolveKerugianKondisi(KerugianKondisi cloudKerugianKondisi) throws Exception {
		KerugianKondisi kerugianKondisi = new KerugianKondisi();
		kerugianKondisi.setCreatedAt(asDate(getCurrentLocalDateTime()));
		kerugianKondisi.setEditedAt(asDate(getCurrentLocalDateTime()));
		kerugianKondisi.setNamaKondisi(cloudKerugianKondisi.getNamaKondisi());
		// save
		getKerugianKondisiDao().save(kerugianKondisi);
		
		return kerugianKondisi;
	}
	
	private KerugianSatuan resolveKerugianSatuan(KerugianSatuan cloudKerugianSatuan) throws Exception {
		KerugianSatuan kerugianSatuan = new KerugianSatuan();
		kerugianSatuan.setCreatedAt(asDate(getCurrentLocalDateTime()));
		kerugianSatuan.setEditedAt(asDate(getCurrentLocalDateTime()));
		kerugianSatuan.setSatuan(cloudKerugianSatuan.getSatuan());
		// save
		getKerugianSatuanDao().save(kerugianSatuan);
		
		return kerugianSatuan;
	}
	
	public void onClick$synchronizeToLocalButton(Event event) throws Exception {
		if (!referensiWilayahCheckbox.isChecked()) {
			throw new Exception("Referensi Wilayah BELUM ada resolusi.");
		}
		if (!referensiKejadianCheckbox.isChecked()) {
			throw new Exception("Referensi Kejadian BELUM ada resolusi");
		}
		if (!referensiKerugianCheckbox.isChecked()) {
			throw new Exception("Referensi Kerugian BELUM ada resolusi");
		}
		try {
			getKejadianDao().save(cloudKejadianToLocal(getCloudKejadian()));
			//
			synchronizeToLocalLabel.setValue("Sinkronisasi Kotamaops BERHASIL.");
		} catch (Exception e) {
			throw new Exception("Sinkronisasi dari Kotamaops TIDAK Berhasil.");
		}
		
		// if success - update cloud kejadian pusdalopsSynchAt -- set to currentdatetime
		Kejadian cloudKejadian = getCloudKejadian();
		cloudKejadian.setPusdalopsSynchAt(asDate(getCurrentLocalDateTime()));
		
		KejadianDao cloudKejadianDao = (KejadianDao) ctx.getBean("kejadianDao");
		// update
		cloudKejadianDao.update(cloudKejadian);
		
		synchronizeToLocalButton.setDisabled(true);
	}
	
	private Kejadian cloudKejadianToLocal(Kejadian cloudKejadian) {
		Kejadian kejadian = new Kejadian();
		kejadian.setCreatedAt(asDate(getCurrentLocalDateTime()));
		kejadian.setEditedAt(asDate(getCurrentLocalDateTime()));
		kejadian.setSerialNumber(getKejadianSerialNumber(cloudKejadian.getSerialNumber()));
		
		kejadian.setTwPembuatanDateTime(cloudKejadian.getTwPembuatanDateTime());
		kejadian.setTwPembuatanTimezone(cloudKejadian.getTwPembuatanTimezone());
		kejadian.setTwKejadianDateTime(cloudKejadian.getTwKejadianDateTime());
		kejadian.setTwKejadianTimezone(cloudKejadian.getTwKejadianTimezone());
		
		kejadian.setKotamaops(cloudKejadian.getKotamaops());
		kejadian.setPropinsi(cloudKejadian.getPropinsi());
		kejadian.setKabupatenKotamadya(kabupatenKotReferenceMap.get(Boolean.TRUE));
		kejadian.setKecamatan(kecamatanReferenceMap.get(Boolean.TRUE));
		kejadian.setKelurahan(kelurahanReferenceMap.get(Boolean.TRUE));
		
		kejadian.setKoordinatGps(cloudKejadian.getKoordinatGps());
		kejadian.setKoordinatPeta(cloudKejadian.getKoordinatPeta());
		kejadian.setBujurLintang(cloudKejadian.getBujurLintang());
		kejadian.setKampung(cloudKejadian.getKampung());
		kejadian.setJalan(cloudKejadian.getJalan());
		kejadian.setKronologis(cloudKejadian.getKronologis());
		
		kejadian.setJenisKejadian(kejadianJenisReferenceMap.get(Boolean.TRUE));
		kejadian.setMotifKejadian(kejadianMotifReferenceMap.get(Boolean.TRUE));
		
		kejadian.setPelakuKejadian(cloudKejadian.getPelakuKejadian());
		kejadian.setKeteranganPelaku(cloudKejadian.getKeteranganPelaku());
		
		kejadian.setSasaran(cloudKejadian.getSasaran());
		
		kejadian.setKerugians(getKerugians(new ArrayList<Kerugian>()));
		
		return kejadian;
	}

	private List<Kerugian> getKerugians(List<Kerugian> kerugianList) {
		List<Listitem> listitem = kejadianKerugianListbox.getItems();
		for (Listitem item : listitem) {
			Listcell lc;
			// kerugian
			Kerugian cloudKerugian = item.getValue();
			// kerugian jenis
			lc = (Listcell) item.getChildren().get(2);
			KerugianJenis kerugianJenis = lc.getValue();
			// kerugian kondisi
			lc = (Listcell) item.getChildren().get(4);
			KerugianKondisi kerugianKondisi = lc.getValue();
			// kerugian satuan
			lc = (Listcell) item.getChildren().get(6);
			KerugianSatuan kerugianSatuan = lc.getValue();
			
			// kerugian
			Kerugian kerugian = new Kerugian();
			kerugian.setCreatedAt(asDate(getCurrentLocalDateTime()));
			kerugian.setEditedAt(asDate(getCurrentLocalDateTime()));
			kerugian.setNamaMaterial(cloudKerugian.getNamaMaterial());
			kerugian.setParaPihak(cloudKerugian.getParaPihak());
			kerugian.setLembagaTerkait(cloudKerugian.getLembagaTerkait());
			kerugian.setTipeKerugian(cloudKerugian.getTipeKerugian());
			kerugian.setKerugianJenis(kerugianJenis);
			kerugian.setKerugianKondisi(kerugianKondisi);
			kerugian.setJumlah(cloudKerugian.getJumlah());
			kerugian.setKerugianSatuan(kerugianSatuan);
			kerugian.setKeterangan(cloudKerugian.getKeterangan());
			
			kerugianList.add(kerugian);
		}
		
		return kerugianList;
	}

	private DocumentSerialNumber getKejadianSerialNumber(DocumentSerialNumber serialNumber) {
		DocumentSerialNumber serialNum = new DocumentSerialNumber();
		serialNum.setCreatedAt(asDate(getCurrentLocalDateTime()));
		serialNum.setEditedAt(asDate(getCurrentLocalDateTime()));
		serialNum.setDocumentCode(serialNumber.getDocumentCode());
		serialNum.setSerialDate(serialNumber.getSerialDate());
		serialNum.setSerialNo(serialNumber.getSerialNo());
		serialNum.setSerialComp(serialNumber.getSerialComp());		
		
		return serialNum;
	}

	public void onClick$closeButton(Event event) throws Exception {
		Events.sendEvent(Events.ON_CLOSE, kejadianMenonjolSinkronisasiDialogWin, event);
		
		kejadianMenonjolSinkronisasiDialogWin.detach();
	}

	public SinkronisasiData getSinkronisasiData() {
		return sinkronisasiData;
	}

	public void setSinkronisasiData(SinkronisasiData sinkronisasiData) {
		this.sinkronisasiData = sinkronisasiData;
	}

	public Kejadian getCloudKejadian() {
		return cloudKejadian;
	}

	public void setCloudKejadian(Kejadian cloudKejadian) {
		this.cloudKejadian = cloudKejadian;
	}

	public LocalDateTime getCurrentLocalDateTime() {
		return currentLocalDateTime;
	}

	public void setCurrentLocalDateTime(LocalDateTime currentLocalDateTime) {
		this.currentLocalDateTime = currentLocalDateTime;
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

	public KejadianDao getKejadianDao() {
		return kejadianDao;
	}

	public void setKejadianDao(KejadianDao kejadianDao) {
		this.kejadianDao = kejadianDao;
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

	public KerugianJenisDao getKerugianJenisDao() {
		return kerugianJenisDao;
	}

	public void setKerugianJenisDao(KerugianJenisDao kerugianJenisDao) {
		this.kerugianJenisDao = kerugianJenisDao;
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
}
