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

import mil.pusdalops.domain.kejadian.KejadianMotif;
import mil.pusdalops.k2.webui.common.GFCBaseController;
import mil.pusdalops.k2.webui.common.TextEntryData;

public class KejadianMotifListDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -585037722943205200L;

	private Window kejadianMotifListDialogWin;
	private Label kejadianMotifStatementLabel;
	private ListModelList<KejadianMotif> kejadianMotifListModel;
	private Listbox kejadianMotifListbox;
	private Button selectButton;
	
	private KejadianMotifData kejadianMotifData;
	private KejadianMotif selectedKejadianMotif;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setKejadianMotifData(
				(KejadianMotifData) arg.get("kejadianMotifData"));
	}

	public void onCreate$kejadianMotifListDialogWin(Event event) throws Exception {
		kejadianMotifStatementLabel.setValue("Jenis Kejadian : \""+
				getKejadianMotifData().getTkpKejadianMotif()+"\""+
				" tidak ditemukan di daftar Motif Kejadian MySQL."+
				" Pilih dari daftar atau Tambahkan ke dalam daftar.");
		
		setKejadianMotifListModel(
				new ListModelList<KejadianMotif>(
						getKejadianMotifData().getKejadianMotifList()));
		
		displayKejadianMotifListInfo();
	}
	
	private void displayKejadianMotifListInfo() {
		kejadianMotifListbox.setModel(getKejadianMotifListModel());
		kejadianMotifListbox.setItemRenderer(getKejadianMotifListitemRenderer());
	}

	private ListitemRenderer<KejadianMotif> getKejadianMotifListitemRenderer() {
		
		return new ListitemRenderer<KejadianMotif>() {
			
			@Override
			public void render(Listitem item, KejadianMotif kejadianMotif, int index) throws Exception {
				Listcell lc;
				
				// Motif Kejadian 
				lc = new Listcell(kejadianMotif.getNamaMotif());
				lc.setParent(item);
				
				item.setValue(kejadianMotif);
			}
		};
	}

	public void onClick$addButton(Event event) throws Exception {
		// create data
		TextEntryData textEntryData = new TextEntryData();
		textEntryData.setNameLabel("Motif Kejadian:");
		textEntryData.setNameTextboxValue(getKejadianMotifData().getTkpKejadianMotif());
		// pass into the dialog
		Map<String, TextEntryData> arg = Collections.singletonMap("textEntryData", textEntryData);
		Window textEntryWinDialog = 
				(Window) Executions.createComponents("/common/TextEntry.zul", kejadianMotifListDialogWin, arg);
		textEntryWinDialog.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				String namaMotifKejadian = (String) event.getData();
				// create KejadianMotif object
				KejadianMotif kejadianMotif = new KejadianMotif();
				kejadianMotif.setNamaMotif(namaMotifKejadian);
				
				// add to the modellist
				getKejadianMotifListModel().add(kejadianMotif);
			}
		});
		
		textEntryWinDialog.doModal();
	}
	
	public void onSelect$kejadianMotifListbox(Event event) throws Exception {
		setSelectedKejadianMotif(
				kejadianMotifListbox.getSelectedItem().getValue());
		
		selectButton.setDisabled(false);
	}
	
	public void onClick$selectButton(Event event) throws Exception {
		if (getSelectedKejadianMotif().getId().compareTo(Long.MIN_VALUE)==0) {
			// send on_change event
			Events.sendEvent(Events.ON_CHANGE, kejadianMotifListDialogWin, getSelectedKejadianMotif());
		} else {
			// display
			Events.sendEvent(Events.ON_OK, kejadianMotifListDialogWin, getSelectedKejadianMotif());			
		}
	}
	
	public void onClick$cancelButton(Event event) throws Exception {
		
		kejadianMotifListDialogWin.detach();
	}

	public KejadianMotifData getKejadianMotifData() {
		return kejadianMotifData;
	}

	public void setKejadianMotifData(KejadianMotifData kejadianMotifData) {
		this.kejadianMotifData = kejadianMotifData;
	}

	public ListModelList<KejadianMotif> getKejadianMotifListModel() {
		return kejadianMotifListModel;
	}

	public void setKejadianMotifListModel(ListModelList<KejadianMotif> kejadianMotifListModel) {
		this.kejadianMotifListModel = kejadianMotifListModel;
	}

	public KejadianMotif getSelectedKejadianMotif() {
		return selectedKejadianMotif;
	}

	public void setSelectedKejadianMotif(KejadianMotif selectedKejadianMotif) {
		this.selectedKejadianMotif = selectedKejadianMotif;
	}
}
