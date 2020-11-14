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

import mil.pusdalops.domain.wilayah.Kabupaten_Kotamadya;
import mil.pusdalops.k2.webui.common.GFCBaseController;
import mil.pusdalops.k2.webui.common.TextEntryData;

public class KabupatenListDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6363786989859761412L;

	private Window kabupatenListDialogWin;
	private Listbox kabupatenListbox;
	private Button selectButton;
	private Label kabupatenStatementLabel;
	
	private KabupatenData kabupatenData;
	private Kabupaten_Kotamadya selectedKabupatenKot;
	private ListModelList<Kabupaten_Kotamadya> kabupatenListModelList;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setKabupatenData(
				(KabupatenData) Executions.getCurrent().getArg().get("kabupatenData"));
	}
		
	public void onCreate$kabupatenListDialogWin(Event event) throws Exception {
		// tidak ditemukan dibawah Propinsi <label id="namaPropinsiLabel"></label>.
		// Pilih dari daftar Kabupaten atau Tambahkan ke dalam daftar.

		kabupatenStatementLabel.setValue("Kabupaten: \""+getKabupatenData().getTkpKabupaten()+"\""+
				" tidak ditemukan dibawah Propinsi: "+getKabupatenData().getKejadianPropinsiName()+"."+
				" Pilih dari daftar Kabupaten atau Tambahkan ke dalam daftar.");
		
		// create listmodellist so that we can add kabupaten to the listbox
		setKabupatenListModelList(
				new ListModelList<Kabupaten_Kotamadya>(getKabupatenData().getKabupatenKotList()));
		
		// display
		displayKabupatenListInfo();
	}
	
	private void displayKabupatenListInfo() {
		kabupatenListbox.setModel(getKabupatenListModelList());
		kabupatenListbox.setItemRenderer(getKabupatenListitemRenderer());
	}

	private ListitemRenderer<Kabupaten_Kotamadya> getKabupatenListitemRenderer() {

		return new ListitemRenderer<Kabupaten_Kotamadya>() {
			
			@Override
			public void render(Listitem item, Kabupaten_Kotamadya kabupatenKot, int index) throws Exception {
				Listcell lc;
				
				// Nama Kabupaten
				lc = new Listcell(kabupatenKot.getNamaKabupaten());
				lc.setParent(item);
				
				item.setValue(kabupatenKot);
			}
		};
	}

	public void onClick$addButton(Event event) throws Exception {
		// create data
		TextEntryData textEntryData = new TextEntryData();
		textEntryData.setNameLabel("Kabupaten:");
		textEntryData.setNameTextboxValue(getKabupatenData().getTkpKabupaten());
		// pass into the dialog
		Map<String, TextEntryData> arg = Collections.singletonMap("textEntryData", textEntryData);
		Window textEntryWinDialog = (Window) Executions.createComponents("/migrasi/kejadian/WilayahTextEntry.zul", kabupatenListDialogWin, arg);
		textEntryWinDialog.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				String namaKabupatenKot = (String) event.getData();
				// create a new kabupaten_kotamadya object
				Kabupaten_Kotamadya kabupatenKot = new Kabupaten_Kotamadya();
				kabupatenKot.setNamaKabupaten(namaKabupatenKot);
				
				// add to the listmodellist
				getKabupatenListModelList().add(0, kabupatenKot);
			}
		});
		
		textEntryWinDialog.doModal();
	}
	
	public void onSelect$kabupatenListbox(Event event) throws Exception {
		setSelectedKabupatenKot(kabupatenListbox.getSelectedItem().getValue());
		
		selectButton.setDisabled(false);
	}
	
	public void onClick$selectButton(Event event) throws Exception {
		if (getSelectedKabupatenKot().getId().compareTo(Long.MIN_VALUE)==0) {
			// send on_change event
			Events.sendEvent(Events.ON_CHANGE, kabupatenListDialogWin, getSelectedKabupatenKot());			
		} else {
			// send on_ok event
			Events.sendEvent(Events.ON_OK, kabupatenListDialogWin, getSelectedKabupatenKot());
		}
		
		kabupatenListDialogWin.detach();
	}
	
	public void onClick$cancelButton(Event event) throws Exception {

		kabupatenListDialogWin.detach();
	}

	public KabupatenData getKabupatenData() {
		return kabupatenData;
	}

	public void setKabupatenData(KabupatenData kabupatenData) {
		this.kabupatenData = kabupatenData;
	}

	public Kabupaten_Kotamadya getSelectedKabupatenKot() {
		return selectedKabupatenKot;
	}

	public void setSelectedKabupatenKot(Kabupaten_Kotamadya selectedKabupatenKot) {
		this.selectedKabupatenKot = selectedKabupatenKot;
	}

	public ListModelList<Kabupaten_Kotamadya> getKabupatenListModelList() {
		return kabupatenListModelList;
	}

	public void setKabupatenListModelList(ListModelList<Kabupaten_Kotamadya> kabupatenListModelList) {
		this.kabupatenListModelList = kabupatenListModelList;
	}


}
