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

import mil.pusdalops.domain.kerugian.KerugianJenis;
import mil.pusdalops.k2.webui.common.GFCBaseController;
import mil.pusdalops.k2.webui.migrasi.KejadianKerugianJenisData;

public class KerugianJenisListDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1207911344576261663L;

	private Window kerugianJenisListDialogWin;
	private Label kerugianJenisStatementLabel;
	private ListModelList<KerugianJenis> kerugianJenisModelList;
	private Listbox kerugianJenisListbox;
	private Button selectButton;
	
	private KejadianKerugianJenisData kejadianKerugianJenisData;
	private KerugianJenis selectedKerugianJenis;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setKejadianKerugianJenisData(
				(KejadianKerugianJenisData) arg.get("kejadianKerugianJenisData"));
	}

	public void onCreate$kerugianJenisListDialogWin(Event event) throws Exception {
		kerugianJenisStatementLabel.setValue("Jenis Kerugian: \""+
				getKejadianKerugianJenisData().getSqlKerugianJenis()+"\""+
				" tidak ditemukan di daftar Jenis Kejadian MySQL.  Pilih dari daftar atau Tambahkan ke dalam daftar.");
		
		setKerugianJenisModelList(
				new ListModelList<KerugianJenis>(
						getKejadianKerugianJenisData().getKerugianJenisList()));
		
		displayKerugianJenisListInfo();
	}
	
	private void displayKerugianJenisListInfo() {
		kerugianJenisListbox.setModel(getKerugianJenisModelList());
		kerugianJenisListbox.setItemRenderer(getKerugianJenisListitemRenderer());
	}

	private ListitemRenderer<KerugianJenis> getKerugianJenisListitemRenderer() {
		
		return new ListitemRenderer<KerugianJenis>() {
			
			@Override
			public void render(Listitem item, KerugianJenis kerugianJenis, int index) throws Exception {
				Listcell lc;
				
				// Jenis Kerugian
				lc = new Listcell(kerugianJenis.getNamaJenis());
				lc.setParent(item);
				
				item.setValue(kerugianJenis);
			}
		};
	}

	public void onClick$addButton(Event event) throws Exception {
		// create data		
		KerugianJenisWithTipeData kerugianJenisWithTipe = new KerugianJenisWithTipeData();
		kerugianJenisWithTipe.setSqlKerugianJenis(getKejadianKerugianJenisData().getSqlKerugianJenis());
		
		// pass into the dialog
		Map<String, KerugianJenisWithTipeData> arg = Collections.singletonMap("kerugianJenisWithTipe", kerugianJenisWithTipe);
		Window textEntryWinDialog = 
				(Window) Executions.createComponents("/dialog/KerugianJenisWithTipe.zul", kerugianJenisListDialogWin, arg);
		textEntryWinDialog.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				KerugianJenisWithTipeData kerugianJenisWithTipeData = 
						(KerugianJenisWithTipeData) event.getData();
				
				// create JenisKerugian
				KerugianJenis kerugianJenis = new KerugianJenis();
				kerugianJenis.setNamaJenis(kerugianJenisWithTipeData.getSqlKerugianJenis());
				kerugianJenis.setTipeKerugian(kerugianJenisWithTipeData.getTipeKerugian());
				
				// add to modellist
				getKerugianJenisModelList().add(0, kerugianJenis);
			}
		});
		textEntryWinDialog.doModal();
	}
	
	public void onSelect$kerugianJenisListbox(Event event) throws Exception {
		setSelectedKerugianJenis(
				kerugianJenisListbox.getSelectedItem().getValue());
		
		selectButton.setDisabled(false);
	}
	
	public void onClick$selectButton(Event event) throws Exception {
		if (getSelectedKerugianJenis().getId().compareTo(Long.MIN_VALUE)==0) {
			// send on_change event
			Events.sendEvent(Events.ON_CHANGE, kerugianJenisListDialogWin, getSelectedKerugianJenis());
		} else {
			// send on_ok event
			Events.sendEvent(Events.ON_OK, kerugianJenisListDialogWin, getSelectedKerugianJenis());			
		}
		
		kerugianJenisListDialogWin.detach();
	}
	
	public void onClick$cancelButton(Event event) throws Exception {
		
		kerugianJenisListDialogWin.detach();
	}

	public KejadianKerugianJenisData getKejadianKerugianJenisData() {
		return kejadianKerugianJenisData;
	}

	public void setKejadianKerugianJenisData(KejadianKerugianJenisData kejadianKerugianJenisData) {
		this.kejadianKerugianJenisData = kejadianKerugianJenisData;
	}

	public ListModelList<KerugianJenis> getKerugianJenisModelList() {
		return kerugianJenisModelList;
	}

	public void setKerugianJenisModelList(ListModelList<KerugianJenis> kerugianJenisModelList) {
		this.kerugianJenisModelList = kerugianJenisModelList;
	}

	public KerugianJenis getSelectedKerugianJenis() {
		return selectedKerugianJenis;
	}

	public void setSelectedKerugianJenis(KerugianJenis selectedKerugianJenis) {
		this.selectedKerugianJenis = selectedKerugianJenis;
	}
}
