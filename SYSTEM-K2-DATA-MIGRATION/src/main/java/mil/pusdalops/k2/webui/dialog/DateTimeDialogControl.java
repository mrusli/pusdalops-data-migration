package mil.pusdalops.k2.webui.dialog;

import java.time.LocalDateTime;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.gmt.TimezoneInd;
import mil.pusdalops.k2.webui.common.GFCBaseController;

public class DateTimeDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4737564972060155048L;

	private Window datetimeDialogWin;
	private Datebox twDatebox;
	private Timebox twTimebox;
	private Combobox twZonaWaktuCombobox;
	
	private DatetimeData datetimeData;
	
	private static final Logger log = Logger.getLogger(DateTimeDialogControl.class);
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		// DatetimeData
		setDatetimeData(
				(DatetimeData) arg.get("datetimeData"));
	}

	public void onCreate$datetimeDialogWin(Event event) throws Exception {
		log.info("Creating DateTimeDialogControl...");
		datetimeDialogWin.setTitle(getDatetimeData().getDialogWinTitle());
		
		// set the current date and time
		setComponentLocalTimezone();
		
		// setup zona waktu combobox
		setupTimezoneCombobox();
		
		// display info
		displayDateTimeInfo();
	}
	
	private void setComponentLocalTimezone() {
		// Locale("id","ID") - Indonesia -- frm GFCBaseController
		twDatebox.setLocale(getLocale());
		twTimebox.setLocale(getLocale());

		// set component time zone
		twDatebox.setTimeZone(getDatetimeData().getZoneId().toString());
		twTimebox.setTimeZone(getDatetimeData().getZoneId().toString());		
	}

	private void setupTimezoneCombobox() {
		Comboitem comboitem;
		for (TimezoneInd timezoneInd : TimezoneInd.values()) {
			comboitem = new Comboitem();
			comboitem.setLabel(timezoneInd.toString());
			comboitem.setValue(timezoneInd);
			comboitem.setParent(twZonaWaktuCombobox);
		}
	}

	private void displayDateTimeInfo() {
		twTimebox.setValueInLocalTime(getDatetimeData().getLocalDateTime().toLocalTime());
		twDatebox.setValueInLocalDate(getDatetimeData().getLocalDateTime().toLocalDate());

		for (Comboitem comboitem : twZonaWaktuCombobox.getItems()) {
			if (comboitem.getValue().equals(getDatetimeData().getTimezoneInd())) {
				twZonaWaktuCombobox.setSelectedItem(comboitem);
				
				break;
			}
		}		
	}

	public void onClick$rubahButton(Event event) throws Exception {
		TimezoneInd selTimezoneInd = twZonaWaktuCombobox.getSelectedItem().getValue();
		int timezoneIndOrdinal = selTimezoneInd.getValue();
		
		// set changes in data		
		getDatetimeData().setTimezoneInd(selTimezoneInd);
		getDatetimeData().setZoneId(selTimezoneInd.toZoneId(timezoneIndOrdinal));
		getDatetimeData().setLocalDateTime(
				LocalDateTime.of(twDatebox.getValueInLocalDate(), twTimebox.getValueInLocalTime()));
		
		// send event
		Events.sendEvent(Events.ON_CHANGE, datetimeDialogWin, getDatetimeData());
		
		// detach
		datetimeDialogWin.detach();
	}
	
	public void onClick$cancelButton(Event event) throws Exception {
		// send event - onCancel
		Events.sendEvent(Events.ON_CANCEL, datetimeDialogWin, null);
		
		// detach
		datetimeDialogWin.detach();
	}

	public DatetimeData getDatetimeData() {
		return datetimeData;
	}

	public void setDatetimeData(DatetimeData datetimeData) {
		this.datetimeData = datetimeData;
	}
	
}
