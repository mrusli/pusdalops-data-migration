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

import mil.pusdalops.domain.kerugian.KerugianKondisi;
import mil.pusdalops.k2.webui.common.GFCBaseController;
import mil.pusdalops.k2.webui.common.TextEntryData;
import mil.pusdalops.k2.webui.migrasi.KejadianKerugianKondisiData;

public class KerugianKondisiListDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1366844243151025272L;

	private Window kerugianKondisiListDialogWin;
	private Label kerugianKondisiStatementLabel;
	private ListModelList<KerugianKondisi> kerugianKondisiModelList;
	private Listbox kerugianKondisiListbox;
	private Button selectButton;
	
	private KejadianKerugianKondisiData kejadianKerugianKondisiData;
	private KerugianKondisi selectedKerugianKondisi;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setKejadianKerugianKondisiData(
				(KejadianKerugianKondisiData) arg.get("kejadianKerugianKondisiData"));
	}
	
	public void onCreate$kerugianKondisiListDialogWin(Event event) throws Exception {
		kerugianKondisiStatementLabel.setValue("Kondisi Kerugian: \""+
				getKejadianKerugianKondisiData().getSqlKerugianKondisi()+"\""+
				" tidak ditemukan di daftar Jenis Kejadian MySQL.  Pilih dari daftar atau Tambahkan ke dalam daftar.");
		
		// listmodellist
		setKerugianKondisiModelList(new ListModelList<KerugianKondisi>(
				getKejadianKerugianKondisiData().getKerugianKondisiList()));
		
		// display
		displayKerugianKondisiListInfo();
	}
	
	private void displayKerugianKondisiListInfo() {
		kerugianKondisiListbox.setModel(getKerugianKondisiModelList());
		kerugianKondisiListbox.setItemRenderer(getKerugianKondisiListitemRenderer());
	}

	private ListitemRenderer<KerugianKondisi> getKerugianKondisiListitemRenderer() {
		
		return new ListitemRenderer<KerugianKondisi>() {
			
			@Override
			public void render(Listitem item, KerugianKondisi kerugianKondisi, int index) throws Exception {
				Listcell lc;
				
				// Kondisi Kerugian
				lc = new Listcell(kerugianKondisi.getNamaKondisi());
				lc.setParent(item);
				
				item.setValue(kerugianKondisi);
			}
		};
	}

	public void onClick$addButton(Event event) throws Exception {
		// create data
		TextEntryData textEntryData = new TextEntryData();
		textEntryData.setNameLabel("Jenis Kerugian:");
		textEntryData.setNameTextboxValue(getKejadianKerugianKondisiData().getSqlKerugianKondisi());
		// pass into the dialog
		Map<String, TextEntryData> arg = Collections.singletonMap("textEntryData", textEntryData);
		Window textEntryWinDialog = 
				(Window) Executions.createComponents("/common/TextEntry.zul", kerugianKondisiListDialogWin, arg);
		textEntryWinDialog.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				String namaKondisiKerugian = (String) event.getData();
				
				// create JenisKerugian
				KerugianKondisi kerugianKondisi = new KerugianKondisi();
				kerugianKondisi.setNamaKondisi(namaKondisiKerugian);
				
				// add to modellist
				getKerugianKondisiModelList().add(0, kerugianKondisi);
			}
		});
		textEntryWinDialog.doModal();
	}
	
	public void onSelect$kerugianKondisiListbox(Event event) throws Exception {
		setSelectedKerugianKondisi(
				kerugianKondisiListbox.getSelectedItem().getValue());
		
		selectButton.setDisabled(false);
	}
	
	public void onClick$selectButton(Event event) throws Exception {
		if (getSelectedKerugianKondisi().getId().compareTo(Long.MIN_VALUE)==0) {
			// send on_change event
			Events.sendEvent(Events.ON_CHANGE, kerugianKondisiListDialogWin, getSelectedKerugianKondisi());
		} else {
			// send on_ok event
			Events.sendEvent(Events.ON_OK, kerugianKondisiListDialogWin, getSelectedKerugianKondisi());			
		}

		kerugianKondisiListDialogWin.detach();
	}
	
	public void onClick$cancelButton(Event event) throws Exception {
		kerugianKondisiListDialogWin.detach();
	}

	public KejadianKerugianKondisiData getKejadianKerugianKondisiData() {
		return kejadianKerugianKondisiData;
	}

	public void setKejadianKerugianKondisiData(KejadianKerugianKondisiData kejadianKerugianKondisiData) {
		this.kejadianKerugianKondisiData = kejadianKerugianKondisiData;
	}

	public ListModelList<KerugianKondisi> getKerugianKondisiModelList() {
		return kerugianKondisiModelList;
	}

	public void setKerugianKondisiModelList(ListModelList<KerugianKondisi> kerugianKondisiModelList) {
		this.kerugianKondisiModelList = kerugianKondisiModelList;
	}

	public KerugianKondisi getSelectedKerugianKondisi() {
		return selectedKerugianKondisi;
	}

	public void setSelectedKerugianKondisi(KerugianKondisi selectedKerugianKondisi) {
		this.selectedKerugianKondisi = selectedKerugianKondisi;
	}
	
}
