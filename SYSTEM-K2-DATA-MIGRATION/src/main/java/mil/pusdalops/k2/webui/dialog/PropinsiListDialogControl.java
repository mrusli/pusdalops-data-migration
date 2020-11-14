package mil.pusdalops.k2.webui.dialog;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.wilayah.Propinsi;
import mil.pusdalops.k2.persistence.propinsi.dao.PropinsiDao;
import mil.pusdalops.k2.webui.common.GFCBaseController;

public class PropinsiListDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8609612885341148359L;

	private PropinsiDao propinsiDao;

	private Window propinsiListDialogWin;
	private Listbox propinsiListbox;
	private Button selectButton;
	private Label namaPropinsiLabel;
	
	private List<Propinsi> propinsiList;
	private Propinsi selectedPropinsi;
	private String namaTkpPropinsi;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setNamaTkpPropinsi(
				(String) Executions.getCurrent().getArg().get("namaTkpPropinsi"));
	}	
	
	public void onCreate$propinsiListDialogWin(Event event) throws Exception {
		namaPropinsiLabel.setValue("Propinsi \""+getNamaTkpPropinsi()+"\"");
		// load propinsi list
		loadPropinsiList();
		
		// display propinsi list
		displayPropinsiList();

	}
	
	private void loadPropinsiList() throws Exception {
		setPropinsiList(
				getPropinsiDao().findAllPropinsiWithOrder(true));
	}

	private void displayPropinsiList() {
		propinsiListbox.setModel(
				new ListModelList<Propinsi>(getPropinsiList()));
		propinsiListbox.setItemRenderer(getPropinsiListitemRenderer());
		
	}

	private ListitemRenderer<Propinsi> getPropinsiListitemRenderer() {

		return new ListitemRenderer<Propinsi>() {
			
			@Override
			public void render(Listitem item, Propinsi propinsi, int index) throws Exception {
				Listcell lc;
				
				// image
				lc = new Listcell();
				lc.setParent(item);
				
				// Nama Propinsi
				lc = new Listcell(propinsi.getNamaPropinsi());
				lc.setParent(item);
				
				item.setValue(propinsi);
				
			}
		};
	}

	public void onSelect$propinsiListbox(Event event) throws Exception {
		setSelectedPropinsi(propinsiListbox.getSelectedItem().getValue());
		
		selectButton.setDisabled(false);
	}
	
	public void onClick$selectButton(Event event) throws Exception {
		// send event
		Events.sendEvent(Events.ON_CHANGE, propinsiListDialogWin, getSelectedPropinsi());
		
		propinsiListDialogWin.detach();
	}
	
	public void onClick$cancelButton(Event event) throws Exception {
		
		propinsiListDialogWin.detach();
	}
	
	public PropinsiDao getPropinsiDao() {
		return propinsiDao;
	}

	public void setPropinsiDao(PropinsiDao propinsiDao) {
		this.propinsiDao = propinsiDao;
	}

	public List<Propinsi> getPropinsiList() {
		return propinsiList;
	}

	public void setPropinsiList(List<Propinsi> propinsiList) {
		this.propinsiList = propinsiList;
	}

	public Propinsi getSelectedPropinsi() {
		return selectedPropinsi;
	}

	public void setSelectedPropinsi(Propinsi selectedPropinsi) {
		this.selectedPropinsi = selectedPropinsi;
	}

	public String getNamaTkpPropinsi() {
		return namaTkpPropinsi;
	}

	public void setNamaTkpPropinsi(String namaTkpPropinsi) {
		this.namaTkpPropinsi = namaTkpPropinsi;
	}
}
