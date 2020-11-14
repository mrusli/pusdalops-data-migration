package mil.pusdalops.k2.webui.dialog;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.gmt.TimezoneInd;
import mil.pusdalops.k2.webui.common.GFCBaseController;

public class KotamaopsAddDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -869729800767843398L;

	private Window kotamaopsAddDialogWin;
	private Textbox entryTextbox, docCodeTextbox;
	private Combobox timezoneCombobox;
	
	private String namaKotamaops;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setNamaKotamaops(
				(String) Executions.getCurrent().getArg().get("namaKotamaops"));
	}
	
	public void onCreate$kotamaopsAddDialogWin(Event event) throws Exception {
		entryTextbox.setValue(getNamaKotamaops());
		
		loadTimezoneCombobox();
	}
	
	private void loadTimezoneCombobox() {
		Comboitem comboitem;
		
		for (TimezoneInd timezoneInd : TimezoneInd.values()) {
			comboitem = new Comboitem();
			comboitem.setLabel(timezoneInd.toString());
			comboitem.setValue(timezoneInd);
			comboitem.setParent(timezoneCombobox);
		}
		
		timezoneCombobox.setSelectedIndex(0);
	}

	public void onClick$changeButton(Event event) throws Exception {
		KotamaopsData kotamaopsData = new KotamaopsData();
		kotamaopsData.setNamaKotamaops(entryTextbox.getValue());
		kotamaopsData.setTimezoneInd(timezoneCombobox.getSelectedItem().getValue());
		kotamaopsData.setDocumentCode(docCodeTextbox.getValue().toUpperCase());
		
		Events.sendEvent(Events.ON_CHANGE, kotamaopsAddDialogWin, kotamaopsData);
		
		kotamaopsAddDialogWin.detach();
	}
	
	public void onClick$cancelButton(Event event) throws Exception {
		
		kotamaopsAddDialogWin.detach();
	}

	public String getNamaKotamaops() {
		return namaKotamaops;
	}

	public void setNamaKotamaops(String namaKotamaops) {
		this.namaKotamaops = namaKotamaops;
	}
}
