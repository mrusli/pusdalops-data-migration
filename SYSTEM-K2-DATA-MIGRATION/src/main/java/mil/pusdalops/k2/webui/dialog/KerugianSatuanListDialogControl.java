package mil.pusdalops.k2.webui.dialog;

import java.util.Collections;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.kerugian.KerugianSatuan;
import mil.pusdalops.k2.webui.common.GFCBaseController;
import mil.pusdalops.k2.webui.common.TextEntryData;
import mil.pusdalops.k2.webui.migrasi.KejadianKerugianSatuanData;

public class KerugianSatuanListDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -70339741747691729L;

	private Window kerugianSatuanListDialogWin;
	private Label kerugianSatuanStatementLabel;
	private Listbox kerugianSatuanListbox;
	private Button selectButton;
	
	private ListModelList<KerugianSatuan> kerugianSatuanModelList;
	private KejadianKerugianSatuanData kejadianKerugianSatuanData;
	private KerugianSatuan selectedKerugianSatuan;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setKejadianKerugianSatuanData(
				(KejadianKerugianSatuanData) arg.get("kejadianKerugianSatuanData"));
	}

	public void onCreate$kerugianSatuanListDialogWin(Event event) throws Exception {
		kerugianSatuanStatementLabel.setValue("Satuan Kerugian: \""+
				getKejadianKerugianSatuanData().getSqlKerugianSatuan()+"\""+
				" tidak ditemukan di daftar Jenis Kejadian MySQL.  Pilih dari daftar atau Tambahkan ke dalam daftar.");
		
		setKerugianSatuanModelList(
				new ListModelList<KerugianSatuan>(
						getKejadianKerugianSatuanData().getKerugianSatuanList()));
		
		displayKerugianSatuanListInfo();
	}
	
	private void displayKerugianSatuanListInfo() {
		kerugianSatuanListbox.setModel(getKerugianSatuanModelList());
		kerugianSatuanListbox.setItemRenderer(getKerugianSatuanListitemRenderer());
	}

	private ListitemRenderer<KerugianSatuan> getKerugianSatuanListitemRenderer() {
		
		return new ListitemRenderer<KerugianSatuan>() {
			
			@Override
			public void render(Listitem item, KerugianSatuan kerugianSatuan, int index) throws Exception {
				Listcell lc;
				
				// Satuan Kerugian
				lc = new Listcell(kerugianSatuan.getSatuan());
				lc.setParent(item);
				
				item.setValue(kerugianSatuan);
			}
		};
	}

	public void onClick$addButton(Event event) throws Exception {
		// create data
		TextEntryData textEntryData = new TextEntryData();
		textEntryData.setNameLabel("Jenis Kerugian:");
		textEntryData.setNameTextboxValue(getKejadianKerugianSatuanData().getSqlKerugianSatuan());
		// pass into the dialog
		Map<String, TextEntryData> arg = Collections.singletonMap("textEntryData", textEntryData);
		Window textEntryWinDialog = 
				(Window) Executions.createComponents("/common/TextEntry.zul", kerugianSatuanListDialogWin, arg);
		textEntryWinDialog.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				String namaSatuanKerugian = (String) event.getData();
				
				// create JenisKerugian
				KerugianSatuan kerugianSatuan = new KerugianSatuan();
				kerugianSatuan.setSatuan(namaSatuanKerugian);
				
				// add to modellist
				getKerugianSatuanModelList().add(0, kerugianSatuan);
			}
		});
		textEntryWinDialog.doModal();
	}
	
	public void onClick$kerugianSatuanListbox(Event event) throws Exception {
		setSelectedKerugianSatuan(
				kerugianSatuanListbox.getSelectedItem().getValue());
		
		selectButton.setDisabled(false);
	}
	
	public void onClick$selectButton(Event event) throws Exception {
		if (getSelectedKerugianSatuan().getId().compareTo(Long.MIN_VALUE)==0) {
			// send on_change event
			Events.sendEvent(Events.ON_CHANGE, kerugianSatuanListDialogWin, getSelectedKerugianSatuan());
		} else {
			// send on_ok event
			Events.sendEvent(Events.ON_OK, kerugianSatuanListDialogWin, getSelectedKerugianSatuan());			
		}
		
		kerugianSatuanListDialogWin.detach();
	}
	
	public void onClick$cancelButton(Event event) throws Exception {
		kerugianSatuanListDialogWin.detach();
	}

	public KejadianKerugianSatuanData getKejadianKerugianSatuanData() {
		return kejadianKerugianSatuanData;
	}

	public void setKejadianKerugianSatuanData(KejadianKerugianSatuanData kejadianKerugianSatuanData) {
		this.kejadianKerugianSatuanData = kejadianKerugianSatuanData;
	}

	public ListModelList<KerugianSatuan> getKerugianSatuanModelList() {
		return kerugianSatuanModelList;
	}

	public void setKerugianSatuanModelList(ListModelList<KerugianSatuan> kerugianSatuanModelList) {
		this.kerugianSatuanModelList = kerugianSatuanModelList;
	}

	public KerugianSatuan getSelectedKerugianSatuan() {
		return selectedKerugianSatuan;
	}

	public void setSelectedKerugianSatuan(KerugianSatuan selectedKerugianSatuan) {
		this.selectedKerugianSatuan = selectedKerugianSatuan;
	}
	
}
