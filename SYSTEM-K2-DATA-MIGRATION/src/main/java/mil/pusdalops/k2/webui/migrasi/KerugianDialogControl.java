package mil.pusdalops.k2.webui.migrasi;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.gmt.TimezoneInd;
import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.domain.kerugian.Kerugian;
import mil.pusdalops.domain.kerugian.KerugianJenis;
import mil.pusdalops.domain.kerugian.KerugianKondisi;
import mil.pusdalops.domain.kerugian.KerugianSatuan;
import mil.pusdalops.domain.kerugian.Lembaga;
import mil.pusdalops.domain.kerugian.Pihak;
import mil.pusdalops.domain.kerugian.TipeKerugian;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.k2.persistence.kerugian.jenis.dao.KerugianJenisDao;
import mil.pusdalops.k2.persistence.kerugian.kondisi.dao.KerugianKondisiDao;
import mil.pusdalops.k2.persistence.kerugian.satuan.dao.KerugianSatuanDao;
import mil.pusdalops.k2.webui.common.GFCBaseController;

public class KerugianDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5277189559464771281L;

	private KerugianJenisDao kerugianJenisDao;
	private KerugianKondisiDao kerugianKondisiDao;
	private KerugianSatuanDao kerugianSatuanDao;

	private Window kerugianDialogWin;
	private Combobox tipeKerugianCombobox, lembagaCombobox, kerugianJenisCombobox,
		kondisiCombobox, satuanCombobox;
	private Textbox kejadianIdTextbox, namaPersMatTextbox, keteranganTextbox; 
	private Intbox jumlahIntbox;
	
	private KerugianData kerugianData;
	private Pihak paraPihak;
	private Kerugian kerugian;
	private Kejadian kejadian;
	private Kotamaops kotamaops; 
	private LocalDateTime currentDateTime;
	private TimezoneInd kotamaopsTimezoneInd;
	
	private static final Logger log = Logger.getLogger(KerugianDialogControl.class);
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		setKerugianData(
				(KerugianData) arg.get("kerugianData"));
	}

	public void onCreate$kerugianDialogWin(Event event) throws Exception {
		setKejadian(getKerugianData().getKejadian());
		setKerugian(getKerugianData().getKerugian());
		setKotamaops(getKerugianData().getKotamaops());
		
		setKotamaopsTimezoneInd(
				getKotamaops().getTimeZone());
		int ordinalVal = kotamaopsTimezoneInd.getValue();
		setCurrentDateTime(
				getLocalDateTime(getKotamaops().getTimeZone().toZoneId(ordinalVal)));
		setParaPihak(
				getKerugianData().getParaPihak());
		kerugianDialogWin.setTitle("Merubah Kerugian Pihak: "+
				getParaPihak());

		loadTipeKerugian();
		
		loadLembagaCombobox();
		
		loadKerugianJenisCombobox();
		
		loadKondisiCombobox();
		
		loadSatuanCombobox();
		
		displayData();
		
		log.info("Kerugian ID: "+getKerugian().getId());

	}

	private void loadTipeKerugian() {
		Comboitem comboitem;
		// int i=0;
		for (TipeKerugian tipeKerugian : TipeKerugian.values()) {
			comboitem = new Comboitem();
			comboitem.setLabel(tipeKerugian.toString());
			comboitem.setValue(tipeKerugian);
			comboitem.setParent(tipeKerugianCombobox);
			
			// i++;
		}
	}

	private void loadLembagaCombobox() {
		Comboitem comboitem;
		// int i=0;
		for (Lembaga lembaga : Lembaga.values()) {
			comboitem = new Comboitem();
			comboitem.setLabel(lembaga.toString());
			comboitem.setValue(lembaga);
			comboitem.setParent(lembagaCombobox);
			
			// i++;
		}
	}

	private void loadKerugianJenisCombobox() throws Exception {
		List<KerugianJenis> kerugianJenisList = getKerugianJenisDao().findAllKerugianJenisByOrder(true);
		Comboitem comboitem;
		for (KerugianJenis kerugianJenis : kerugianJenisList) {
			comboitem = new Comboitem();
			comboitem.setLabel(kerugianJenis.getNamaJenis());
			comboitem.setValue(kerugianJenis);
			comboitem.setParent(kerugianJenisCombobox);
		}
	}

	private void loadKondisiCombobox() throws Exception {
		List<KerugianKondisi> kerugianKondisiList = getKerugianKondisiDao().findAllKerugianKondisiByOrder(true);
		Comboitem comboitem;
		for (KerugianKondisi kerugianKondisi : kerugianKondisiList) {
			comboitem = new Comboitem();
			comboitem.setLabel(kerugianKondisi.getNamaKondisi());
			comboitem.setValue(kerugianKondisi);
			comboitem.setParent(kondisiCombobox);
		}
	}

	private void loadSatuanCombobox() throws Exception {
		List<KerugianSatuan> kerugianSatuanList = getKerugianSatuanDao().findAllKerugianSatuanByOrder(true);
		Comboitem comboitem;
		for (KerugianSatuan kerugianSatuan : kerugianSatuanList) {
			comboitem = new Comboitem();
			comboitem.setLabel(kerugianSatuan.getSatuan());
			comboitem.setValue(kerugianSatuan);
			comboitem.setParent(satuanCombobox);
		}
	}

	private void displayData() {
		kejadianIdTextbox.setValue(getKejadian().getSerialNumber().getSerialComp());
		namaPersMatTextbox.setValue(getKerugian().getNamaMaterial());
		for (Comboitem comboitem : tipeKerugianCombobox.getItems()) {
			if (comboitem.getValue().equals(getKerugian().getTipeKerugian())) {
				tipeKerugianCombobox.setSelectedItem(comboitem);
				break;
			}
		}
		for (Comboitem comboitem : lembagaCombobox.getItems()) {
			if (comboitem.getValue().equals(getKerugian().getLembagaTerkait())) {
				lembagaCombobox.setSelectedItem(comboitem);
			}
		}
		for (Comboitem comboitem : kerugianJenisCombobox.getItems()) {
			if (comboitem.getLabel().compareTo(getKerugian().getKerugianJenis().getNamaJenis())==0) {
				kerugianJenisCombobox.setSelectedItem(comboitem);
			}
		}
		for (Comboitem comboitem : kondisiCombobox.getItems()) {
			if (comboitem.getLabel().compareTo(getKerugian().getKerugianKondisi().getNamaKondisi())==0) {
				kondisiCombobox.setSelectedItem(comboitem);
			}
		}
		jumlahIntbox.setValue(getKerugian().getJumlah());
		for (Comboitem comboitem : satuanCombobox.getItems()) {
			if (comboitem.getLabel().compareTo(getKerugian().getKerugianSatuan().getSatuan())==0) {
				satuanCombobox.setSelectedItem(comboitem);
			}
		}
		keteranganTextbox.setValue(getKerugian().getKeterangan());
	}

	public void onClick$saveButton(Event event) throws Exception {
		// send event - onChange
		Events.sendEvent(Events.ON_CHANGE, kerugianDialogWin, getUserModKerugian());
		
		// detach
		kerugianDialogWin.detach();
	}
	
	private Object getUserModKerugian() {
		Kerugian userModKerugian = getKerugian();
		
		userModKerugian.setEditedAt(asDate(getCurrentDateTime()));
		userModKerugian.setNamaMaterial(namaPersMatTextbox.getValue());
		userModKerugian.setParaPihak(getParaPihak());
		userModKerugian.setTipeKerugian(tipeKerugianCombobox.getSelectedItem().getValue());
		userModKerugian.setLembagaTerkait(lembagaCombobox.getSelectedItem().getValue());
		userModKerugian.setKerugianJenis(kerugianJenisCombobox.getSelectedItem().getValue());
		userModKerugian.setKerugianKondisi(kondisiCombobox.getSelectedItem().getValue());
		userModKerugian.setJumlah(jumlahIntbox.getValue());
		userModKerugian.setKerugianSatuan(satuanCombobox.getSelectedItem().getValue());
		userModKerugian.setKeterangan(keteranganTextbox.getValue());		
		
		return userModKerugian;
	}

	public void onClick$cancelButton(Event event) throws Exception {
		// send event - onCancel
		
		// detach
		kerugianDialogWin.detach();
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

	public KerugianData getKerugianData() {
		return kerugianData;
	}

	public void setKerugianData(KerugianData kerugianData) {
		this.kerugianData = kerugianData;
	}

	public Pihak getParaPihak() {
		return paraPihak;
	}

	public void setParaPihak(Pihak paraPihak) {
		this.paraPihak = paraPihak;
	}

	public Kerugian getKerugian() {
		return kerugian;
	}

	public void setKerugian(Kerugian kerugian) {
		this.kerugian = kerugian;
	}

	public Kejadian getKejadian() {
		return kejadian;
	}

	public void setKejadian(Kejadian kejadian) {
		this.kejadian = kejadian;
	}

	public Kotamaops getKotamaops() {
		return kotamaops;
	}

	public void setKotamaops(Kotamaops kotamaops) {
		this.kotamaops = kotamaops;
	}

	public LocalDateTime getCurrentDateTime() {
		return currentDateTime;
	}

	public void setCurrentDateTime(LocalDateTime currentDateTime) {
		this.currentDateTime = currentDateTime;
	}

	public TimezoneInd getKotamaopsTimezoneInd() {
		return kotamaopsTimezoneInd;
	}

	public void setKotamaopsTimezoneInd(TimezoneInd kotamaopsTimezoneInd) {
		this.kotamaopsTimezoneInd = kotamaopsTimezoneInd;
	}
	
}
