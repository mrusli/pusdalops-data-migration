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

import mil.pusdalops.domain.wilayah.Kecamatan;
import mil.pusdalops.k2.webui.common.GFCBaseController;
import mil.pusdalops.k2.webui.common.TextEntryData;

public class KecamatanListDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2498244442656379373L;

	private Window kecamatanListDialogWin;
	private Label kecamatanStatementLabel;
	private Listbox kecamatanListbox;
	private Button selectButton;
	
	private KecamatanData kecamatanData;
	private ListModelList<Kecamatan> kecamatanListModelList;
	private Kecamatan selectedKecamatan;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setKecamatanData((KecamatanData) arg.get("kecamatanData"));
	}
	
	public void onCreate$kecamatanListDialogWin(Event event) throws Exception {
		kecamatanStatementLabel.setValue("Kecamatan: \""+
				getKecamatanData().getTkpKecamatan()+"\""+
				" tidak ditemukan dibawah Kabupaten/Kotamadya: "+
				getKecamatanData().getKejadianKabupatenKotName()+"."+
				" Pilih dari daftar Kecamatan atau Tambahkan ke dalam daftar.");
		
		// create listmodellist so that we can add kecamatan to the listbox
		setKecamatanListModelList(
				new ListModelList<Kecamatan>(getKecamatanData().getKecamatanList()));
		
		// display
		displayKecamatanListInfo();
	}
	
	private void displayKecamatanListInfo() {
		kecamatanListbox.setModel(getKecamatanListModelList());
		kecamatanListbox.setItemRenderer(getKecamatanListItemRenderer());
	}

	private ListitemRenderer<Kecamatan> getKecamatanListItemRenderer() {

		return new ListitemRenderer<Kecamatan>() {
			
			@Override
			public void render(Listitem item, Kecamatan kecamatan, int index) throws Exception {
				Listcell lc;
				
				// Nama Kecamatan
				lc = new Listcell(kecamatan.getNamaKecamatan());
				lc.setParent(item);
				
				item.setValue(kecamatan);
			}
		};
	}

	public void onClick$addButton(Event event) throws Exception {
		// create data
		TextEntryData textEntryData = new TextEntryData();
		textEntryData.setNameLabel("Kecamatan:");
		textEntryData.setNameTextboxValue(getKecamatanData().getTkpKecamatan());
		// pass into the dialog
		Map<String, TextEntryData> arg = Collections.singletonMap("textEntryData", textEntryData);
		Window textEntryWinDialog = 
				(Window) Executions.createComponents("/migrasi/kejadian/WilayahTextEntry.zul", kecamatanListDialogWin, arg);
		textEntryWinDialog.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				String namaKecamatan = (String) event.getData();
				// create a new kecamatan object
				Kecamatan kecamatan = new Kecamatan();
				kecamatan.setNamaKecamatan(namaKecamatan);
				// add to the listmodellist
				getKecamatanListModelList().add(0, kecamatan);
			}
		});
		
		textEntryWinDialog.doModal();
	}
	
	public void onSelect$kecamatanListbox(Event event) throws Exception {
		setSelectedKecamatan(kecamatanListbox.getSelectedItem().getValue());
		
		selectButton.setDisabled(false);
	}
	
	public void onClick$selectButton(Event event) throws Exception {
		if (getSelectedKecamatan().getId().compareTo(Long.MIN_VALUE)==0) {
			// send on_change Event
			Events.sendEvent(Events.ON_CHANGE, kecamatanListDialogWin, getSelectedKecamatan());			
		} else {
			Events.sendEvent(Events.ON_OK, kecamatanListDialogWin, getSelectedKecamatan());			
		}
		
		kecamatanListDialogWin.detach();
	}
	
	public void onClick$cancelButton(Event event) throws Exception {
		Events.sendEvent(Events.ON_CANCEL, kecamatanListDialogWin, null);			

		kecamatanListDialogWin.detach();
	}

	public KecamatanData getKecamatanData() {
		return kecamatanData;
	}

	public void setKecamatanData(KecamatanData kecamatanData) {
		this.kecamatanData = kecamatanData;
	}

	public ListModelList<Kecamatan> getKecamatanListModelList() {
		return kecamatanListModelList;
	}

	public void setKecamatanListModelList(ListModelList<Kecamatan> kecamatanListModelList) {
		this.kecamatanListModelList = kecamatanListModelList;
	}

	public Kecamatan getSelectedKecamatan() {
		return selectedKecamatan;
	}

	public void setSelectedKecamatan(Kecamatan selectedKecamatan) {
		this.selectedKecamatan = selectedKecamatan;
	}
}
