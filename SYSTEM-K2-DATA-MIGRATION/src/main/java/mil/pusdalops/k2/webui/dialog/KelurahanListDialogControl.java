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

import mil.pusdalops.domain.wilayah.Kelurahan;
import mil.pusdalops.k2.webui.common.GFCBaseController;
import mil.pusdalops.k2.webui.common.TextEntryData;

public class KelurahanListDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4562296145676720247L;

	private Window kelurahanListDialogWin;
	private Label kelurahanStatementLabel;
	private Listbox kelurahanListbox;
	private Button selectButton;
	
	private KelurahanData kelurahanData;
	private ListModelList<Kelurahan> kelurahanListModelList;
	private Kelurahan selectedKelurahan;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		// kelurahanData
		setKelurahanData((KelurahanData) arg.get("kelurahanData"));
	}
	
	public void onCreate$kelurahanListDialogWin(Event event) throws Exception {
		kelurahanStatementLabel.setValue("Kelurahan: \""+
				getKelurahanData().getTkpKelurahan()+"\""+
				" tidak ditemukan dibawah Kecamatan: "+
				getKelurahanData().getKejadianKecamatanName()+"."+
				" Pilih dari daftar Kelurahan atau Tambahkan ke dalam daftar.");
		
		// create listmodellist so that a new kelurahan can be added to the listbox
		setKelurahanListModelList(
				new ListModelList<Kelurahan>(getKelurahanData().getKelurahanList()));
		
		// display
		displayKelurahanListInfo();
		
	}
	
	private void displayKelurahanListInfo() {
		kelurahanListbox.setModel(getKelurahanListModelList());
		kelurahanListbox.setItemRenderer(getKelurahanListitemRenderer());
	}

	private ListitemRenderer<Kelurahan> getKelurahanListitemRenderer() {

		return new ListitemRenderer<Kelurahan>() {
			
			@Override
			public void render(Listitem item, Kelurahan kelurahan, int index) throws Exception {
				Listcell lc;
				
				// Nama Kelurahan
				lc = new Listcell(kelurahan.getNamaKelurahan());
				lc.setParent(item);
				
				item.setValue(kelurahan);
			}
		};
	}

	public void onClick$addButton(Event event) throws Exception {
		// create data
		TextEntryData textEntryData = new TextEntryData();
		textEntryData.setNameLabel("Kelurahan:");
		textEntryData.setNameTextboxValue(getKelurahanData().getTkpKelurahan());
		// pass into the dialog
		Map<String, TextEntryData> arg = Collections.singletonMap("textEntryData", textEntryData);
		Window textEntryWinDialog = 
				(Window) Executions.createComponents("/migrasi/kejadian/WilayahTextEntry.zul", kelurahanListDialogWin, arg);
		textEntryWinDialog.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				String namaKelurahan = (String) event.getData();
				// create a new kelurahan object
				Kelurahan kelurahan = new Kelurahan();
				kelurahan.setNamaKelurahan(namaKelurahan);
				// add to the listmodellist
				getKelurahanListModelList().add(0, kelurahan);
			}
		});
		
		textEntryWinDialog.doModal();
	}
	
	public void onSelect$kelurahanListbox(Event event) throws Exception {
		setSelectedKelurahan(kelurahanListbox.getSelectedItem().getValue());
		
		selectButton.setDisabled(false);
	}
	
	public void onClick$selectButton(Event event) throws Exception {
		if (getSelectedKelurahan().getId().compareTo(Long.MIN_VALUE)==0) {
			// send ON_CHAGE event
			Events.sendEvent(Events.ON_CHANGE, kelurahanListDialogWin, getSelectedKelurahan());			
		} else {
			// send ON_OK event
			Events.sendEvent(Events.ON_OK, kelurahanListDialogWin, getSelectedKelurahan());
		}
		
		kelurahanListDialogWin.detach();
	}
	
	public void onClick$cancelButton(Event event) throws Exception {
		Events.sendEvent(Events.ON_CANCEL, kelurahanListDialogWin, null);			

		kelurahanListDialogWin.detach();
	}

	public KelurahanData getKelurahanData() {
		return kelurahanData;
	}

	public void setKelurahanData(KelurahanData kelurahanData) {
		this.kelurahanData = kelurahanData;
	}

	public ListModelList<Kelurahan> getKelurahanListModelList() {
		return kelurahanListModelList;
	}

	public void setKelurahanListModelList(ListModelList<Kelurahan> kelurahanListModelList) {
		this.kelurahanListModelList = kelurahanListModelList;
	}

	public Kelurahan getSelectedKelurahan() {
		return selectedKelurahan;
	}

	public void setSelectedKelurahan(Kelurahan selectedKelurahan) {
		this.selectedKelurahan = selectedKelurahan;
	}
}
