package mil.pusdalops.k2.webui.migrasi;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import mil.pusdalops.k2.webui.common.GFCBaseController;
import mil.pusdalops.k2.webui.common.TextEntryData;

public class WilayahTextEntryDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 484878441414013699L;

	private Window wilayahTextEntryDialogWin;
	private Label nameLabel;
	private Checkbox noInfoCheckbox;
	private Textbox entryTextbox;
		
	private TextEntryData textEntryData;
	
	private final String NO_INFO = "-Tidak ada Info-";
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		// textEntryData
		setTextEntryData((TextEntryData) arg.get("textEntryData"));
	}

	public void onCreate$wilayahTextEntryDialogWin(Event event) throws Exception {
		nameLabel.setValue(getTextEntryData().getNameLabel());
		entryTextbox.setValue(getTextEntryData().getNameTextboxValue());		
	}

	public void onCheck$noInfoCheckbox(Event event) throws Exception {
		if (noInfoCheckbox.isChecked()) {
			entryTextbox.setValue(NO_INFO);
			entryTextbox.setReadonly(true);
		} else {
			entryTextbox.setValue("");
			entryTextbox.setReadonly(false);
		}
	}
	
	public void onClick$changeButton(Event event) throws Exception {
		Events.sendEvent(Events.ON_CHANGE, wilayahTextEntryDialogWin, entryTextbox.getValue());

		wilayahTextEntryDialogWin.detach();
	}

	public void onClick$cancelButton(Event event) throws Exception {
		
		wilayahTextEntryDialogWin.detach();
	}
	
	public TextEntryData getTextEntryData() {
		return textEntryData;
	}



	public void setTextEntryData(TextEntryData textEntryData) {
		this.textEntryData = textEntryData;
	}
	
}
