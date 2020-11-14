package mil.pusdalops.k2.webui.common;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class TextEntryDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1700611388281348375L;

	private Window textEntryDialogWin;
	private Label nameLabel;
	private Textbox entryTextbox;
	
	private TextEntryData textEntryData;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		// textEntryData
		setTextEntryData((TextEntryData) arg.get("textEntryData"));
	}
	
	public void onCreate$textEntryDialogWin(Event event) throws Exception {
		nameLabel.setValue(getTextEntryData().getNameLabel());
		entryTextbox.setValue(getTextEntryData().getNameTextboxValue());
	}

	public void onClick$changeButton(Event event) throws Exception {
		Events.sendEvent(Events.ON_CHANGE, textEntryDialogWin, entryTextbox.getValue());
		
		textEntryDialogWin.detach();
	}
	
	public void onClick$cancelButton(Event event) throws Exception {
		textEntryDialogWin.detach();
	}

	public TextEntryData getTextEntryData() {
		return textEntryData;
	}

	public void setTextEntryData(TextEntryData textEntryData) {
		this.textEntryData = textEntryData;
	}

}
