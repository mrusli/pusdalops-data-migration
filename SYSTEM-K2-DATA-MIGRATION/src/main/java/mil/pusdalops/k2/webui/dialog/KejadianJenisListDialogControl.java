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

import mil.pusdalops.domain.kejadian.KejadianJenis;
import mil.pusdalops.k2.webui.common.GFCBaseController;
import mil.pusdalops.k2.webui.common.TextEntryData;

public class KejadianJenisListDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9142189458344844486L;

	private Window kejadianJenisListDialogWin;
	private Label kejadianJenisStatementLabel;
	private ListModelList<KejadianJenis> kejadianJenisModelList;
	private Listbox kejadianJenisListbox;
	private Button selectButton;
	
	private KejadianJenisData kejadianJenisData;
	private KejadianJenis selectedKejadianJenis;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setKejadianJenisData(
				(KejadianJenisData) arg.get("kejadianJenisData"));
	}

	public void onCreate$kejadianJenisListDialogWin(Event event) throws Exception {
		kejadianJenisStatementLabel.setValue("Jenis Kejadian : \""+getKejadianJenisData().getTkpKejadianJenis()+"\""+
				" tidak ditemukan di daftar Jenis Kejadian MySQL.  Pilih dari daftar atau Tambahkan ke dalam daftar.");
		
		setKejadianJenisModelList(
				new ListModelList<KejadianJenis>(getKejadianJenisData().getKejadianJenisList()));
		
		displayKejadianJenisListInfo();
	}

	private void displayKejadianJenisListInfo() {
		kejadianJenisListbox.setModel(getKejadianJenisModelList());
		kejadianJenisListbox.setItemRenderer(getKejadianJenisListitemRenderer());
	}

	private ListitemRenderer<KejadianJenis> getKejadianJenisListitemRenderer() {

		return new ListitemRenderer<KejadianJenis>() {
			
			@Override
			public void render(Listitem item, KejadianJenis kejJenis, int index) throws Exception {
				Listcell lc;
				
				// Nama Jenis Kejadian
				lc = new Listcell(kejJenis.getNamaJenis());
				lc.setParent(item);
				
				item.setValue(kejJenis);
			}
		};
	}

	public void onClick$addButton(Event event) throws Exception {
		// create data
		TextEntryData textEntryData = new TextEntryData();
		textEntryData.setNameLabel("Jenis Kejadian:");
		textEntryData.setNameTextboxValue(getKejadianJenisData().getTkpKejadianJenis());
		// pass into the dialog
		Map<String, TextEntryData> arg = Collections.singletonMap("textEntryData", textEntryData);
		Window textEntryWinDialog = 
				(Window) Executions.createComponents("/common/TextEntry.zul", kejadianJenisListDialogWin, arg);
		textEntryWinDialog.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				String namaJenisKejadian = (String) event.getData();
				// create KejadianJenis object
				KejadianJenis kejadianJenis = new KejadianJenis();
				kejadianJenis.setNamaJenis(namaJenisKejadian);
				
				// add to the modellist
				getKejadianJenisModelList().add(kejadianJenis);
			}
		});
		
		textEntryWinDialog.doModal();
	}
	
	public void onSelect$kejadianJenisListbox(Event event) throws Exception {
		setSelectedKejadianJenis(
				kejadianJenisListbox.getSelectedItem().getValue());
		
		selectButton.setDisabled(false);
	}
	
	public void onClick$selectButton(Event event) throws Exception {
		if (getSelectedKejadianJenis().getId().compareTo(Long.MIN_VALUE)==0) {
			// send on_change event
			Events.sendEvent(Events.ON_CHANGE, kejadianJenisListDialogWin, getSelectedKejadianJenis());
		} else {
			// send on_ok event
			Events.sendEvent(Events.ON_OK, kejadianJenisListDialogWin, getSelectedKejadianJenis());			
		}
		
		kejadianJenisListDialogWin.detach();
	}
	
	public void onClick$cancelButton(Event event) throws Exception {
		
		kejadianJenisListDialogWin.detach();
	}

	public KejadianJenisData getKejadianJenisData() {
		return kejadianJenisData;
	}

	public void setKejadianJenisData(KejadianJenisData kejadianJenisData) {
		this.kejadianJenisData = kejadianJenisData;
	}

	public ListModelList<KejadianJenis> getKejadianJenisModelList() {
		return kejadianJenisModelList;
	}

	public void setKejadianJenisModelList(ListModelList<KejadianJenis> kejadianJenisModelList) {
		this.kejadianJenisModelList = kejadianJenisModelList;
	}

	public KejadianJenis getSelectedKejadianJenis() {
		return selectedKejadianJenis;
	}

	public void setSelectedKejadianJenis(KejadianJenis selectedKejadianJenis) {
		this.selectedKejadianJenis = selectedKejadianJenis;
	}
	
}
