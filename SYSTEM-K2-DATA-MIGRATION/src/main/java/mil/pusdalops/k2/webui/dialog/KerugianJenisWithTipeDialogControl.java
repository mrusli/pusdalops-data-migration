package mil.pusdalops.k2.webui.dialog;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.kerugian.TipeKerugian;
import mil.pusdalops.k2.webui.common.GFCBaseController;

public class KerugianJenisWithTipeDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1837048846058397372L;

	private Window kerugianJenisWithTipeDialogWin;
	private Textbox entryTextbox;
	private Combobox tipeKerugianCombobox;
	
	private KerugianJenisWithTipeData kerugianJenisWithTipeData;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		// kerugianJenisWithTipe
		setKerugianJenisWithTipeData(
				(KerugianJenisWithTipeData) arg.get("kerugianJenisWithTipe"));
	}

	public void onCreate$kerugianJenisWithTipeDialogWin(Event event) throws Exception {
		
		loadTipeKerugianCombobox();

		displayData();
	}

	private void loadTipeKerugianCombobox() {
		Comboitem comboitem;
		for (TipeKerugian tipeKerugian : TipeKerugian.values()) {
			comboitem = new Comboitem();
			comboitem.setLabel(tipeKerugian.toString());
			comboitem.setValue(tipeKerugian);
			comboitem.setParent(tipeKerugianCombobox);
		}
	}

	private void displayData() {
		entryTextbox.setValue(
				getKerugianJenisWithTipeData().getSqlKerugianJenis());

		tipeKerugianCombobox.setSelectedIndex(0);
	}	
	
	public void onClick$changeButton(Event event) throws Exception {
		KerugianJenisWithTipeData modKerugianJenisWithTipe = getKerugianJenisWithTipeData();
		modKerugianJenisWithTipe.setSqlKerugianJenis(entryTextbox.getValue());
		modKerugianJenisWithTipe.setTipeKerugian(tipeKerugianCombobox.getSelectedItem().getValue());
		
		Events.sendEvent(Events.ON_CHANGE, kerugianJenisWithTipeDialogWin, modKerugianJenisWithTipe);
		
		kerugianJenisWithTipeDialogWin.detach();
	}
	
	public void onClick$cancelButton(Event event) throws Exception {
		kerugianJenisWithTipeDialogWin.detach();
	}

	public KerugianJenisWithTipeData getKerugianJenisWithTipeData() {
		return kerugianJenisWithTipeData;
	}

	public void setKerugianJenisWithTipeData(KerugianJenisWithTipeData kerugianJenisWithTipeData) {
		this.kerugianJenisWithTipeData = kerugianJenisWithTipeData;
	}
	
}
