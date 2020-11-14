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

import mil.pusdalops.domain.kejadian.KejadianPelaku;
import mil.pusdalops.k2.webui.common.GFCBaseController;
import mil.pusdalops.k2.webui.common.TextEntryData;

public class KejadianPelakuListDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 200520835097011324L;

	private Window kejadianPelakuListDialogWin;
	private Label kejadianPelakuStatementLabel;
	private ListModelList<KejadianPelaku> kejadianPelakuListModel;
	private Listbox kejadianPelakuListbox;
	private Button selectButton;
	
	private KejadianPelakuData kejadianPelakuData;
	private KejadianPelaku selectedKejadianPelaku;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setKejadianPelakuData(
				(KejadianPelakuData) arg.get("kejadianPelakuData"));
	}

	public void onCreate$kejadianPelakuListDialogWin(Event event) throws Exception {
		kejadianPelakuStatementLabel.setValue("Pelaku Kejadian : \""+
				getKejadianPelakuData().getTkpKejadianPelaku()+"\""+
				" tidak ditemukan di daftar Motif Kejadian MySQL."+
				" Pilih dari daftar atau Tambahkan ke dalam daftar.");
		
		setKejadianPelakuListModel(
				new ListModelList<KejadianPelaku>(
						getKejadianPelakuData().getKejadianPelakuList()));
		
		displayKejadianPelakuListInfo();
	}

	private void displayKejadianPelakuListInfo() {
		kejadianPelakuListbox.setModel(getKejadianPelakuListModel());
		kejadianPelakuListbox.setItemRenderer(getKejadianPelakuListitemRenderer());
	}

	private ListitemRenderer<KejadianPelaku> getKejadianPelakuListitemRenderer() {
		
		return new ListitemRenderer<KejadianPelaku>() {
			
			@Override
			public void render(Listitem item, KejadianPelaku kejadianPelaku, int index) throws Exception {
				Listcell lc;
				
				// Pelaku Kejadian
				lc = new Listcell(kejadianPelaku.getNamaPelaku());
				lc.setParent(item);
				
				item.setValue(kejadianPelaku);
			}
		};
	}
	
	public void onClick$addButton(Event event) throws Exception {
		// create data
		TextEntryData textEntryData = new TextEntryData();
		textEntryData.setNameLabel("Motif Kejadian:");
		textEntryData.setNameTextboxValue(getKejadianPelakuData().getTkpKejadianPelaku());
		// pass into the dialog
		Map<String, TextEntryData> arg = Collections.singletonMap("textEntryData", textEntryData);
		Window textEntryWinDialog = 
				(Window) Executions.createComponents("/common/TextEntry.zul", kejadianPelakuListDialogWin, arg);
		textEntryWinDialog.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				String namaPelakuKejadian = (String) event.getData();
				// create KejadianPelaku object
				KejadianPelaku kejadianPelaku = new KejadianPelaku();
				kejadianPelaku.setNamaPelaku(namaPelakuKejadian);
				
				// add to the modellist
				getKejadianPelakuListModel().add(kejadianPelaku);
			}
		});
		
		textEntryWinDialog.doModal();
		
	}

	public void onSelect$kejadianPelakuListbox(Event event) throws Exception {
		setSelectedKejadianPelaku(
				kejadianPelakuListbox.getSelectedItem().getValue());
		
		selectButton.setDisabled(false);
	}
	
	public void onClick$selectButton(Event event) throws Exception {
		if (getSelectedKejadianPelaku().getId().compareTo(Long.MIN_VALUE)==0) {
			// send ON_CHANGE
			Events.sendEvent(Events.ON_CHANGE, kejadianPelakuListDialogWin, 
					getSelectedKejadianPelaku());
		} else {
			// send ON_OK
			Events.sendEvent(Events.ON_OK, kejadianPelakuListDialogWin, 
					getSelectedKejadianPelaku());
		}
		
		kejadianPelakuListDialogWin.detach();
	}
	
	public void onClick$cancelButton(Event event) throws Exception {
		
		kejadianPelakuListDialogWin.detach();
	}
	
	public KejadianPelakuData getKejadianPelakuData() {
		return kejadianPelakuData;
	}

	public void setKejadianPelakuData(KejadianPelakuData kejadianPelakuData) {
		this.kejadianPelakuData = kejadianPelakuData;
	}

	public ListModelList<KejadianPelaku> getKejadianPelakuListModel() {
		return kejadianPelakuListModel;
	}

	public void setKejadianPelakuListModel(ListModelList<KejadianPelaku> kejadianPelakuListModel) {
		this.kejadianPelakuListModel = kejadianPelakuListModel;
	}

	public KejadianPelaku getSelectedKejadianPelaku() {
		return selectedKejadianPelaku;
	}

	public void setSelectedKejadianPelaku(KejadianPelaku selectedKejadianPelaku) {
		this.selectedKejadianPelaku = selectedKejadianPelaku;
	}
}
