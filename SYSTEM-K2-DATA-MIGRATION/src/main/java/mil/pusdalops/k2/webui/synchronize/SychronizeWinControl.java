package mil.pusdalops.k2.webui.synchronize;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timebox;

import mil.pusdalops.domain.gmt.TimezoneInd;
import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.domain.serial.DocumentSerialNumber;
import mil.pusdalops.domain.settings.Settings;
import mil.pusdalops.k2.persistence.kejadian.dao.KejadianDao;
import mil.pusdalops.k2.persistence.settings.dao.SettingsDao;
import mil.pusdalops.k2.webui.common.GFCBaseController;

public class SychronizeWinControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1939929009598340876L;

	private SettingsDao settingsDao;
	private KejadianDao kejadianDao;
	
	private Textbox logSynchTextbox, logTransTextbox;
	private Datebox twSynchDatebox;
	private Timebox twSynchTimebox;
	
	private Settings settings;
	private LocalDateTime currentDateTime, lastSynchDateTime;
	private List<Kejadian> kejadianList;
	private List<Kejadian> createList;
	private List<Kejadian> updateList;
	
	private final String 	NEW_LINE = "\n";
	private final long 		SETTINGS_DEFAULT_ID = 1L;
	
	public void onCreate$sychronizeWin(Event event) throws Exception {
		setSettings(getSettingsDao().findSettingsById(SETTINGS_DEFAULT_ID));
		
		// last synch init
		LocalDateTime lastSynchDateTimeVar = LocalDateTime.of(2020, 9, 1, 0, 0);
		setLastSynchDateTime(lastSynchDateTimeVar);
		
		// current settings kotamatops current settings
		TimezoneInd timezoneInd = getSettings().getSelectedKotamaops().getTimeZone();
		int timezoneIndOrdinal = timezoneInd.ordinal();

		// set current localdatetime
		setCurrentDateTime(getLocalDateTime(timezoneInd.toZoneId(timezoneIndOrdinal)));
		
		// init list
		setCreateList(new ArrayList<Kejadian>());
		setUpdateList(new ArrayList<Kejadian>());
		
		// display
		displaySynchronize();
	}
	
	private void displaySynchronize() {
		// Locale("id","ID") - Indonesia -- frm GFCBaseController
		twSynchDatebox.setLocale(getLocale());
		twSynchTimebox.setLocale(getLocale());

		twSynchDatebox.setValueInLocalDateTime(getLastSynchDateTime());
		twSynchTimebox.setValueInLocalDateTime(getLastSynchDateTime());
	}

	public void onClick$scanTableButton(Event event) throws Exception {
		// last synch
		LocalDateTime twDateboxLocalDateTime = twSynchDatebox.getValueInLocalDateTime();
		int year = twDateboxLocalDateTime.getYear();
		int month = twDateboxLocalDateTime.getMonthValue();
		int day = twDateboxLocalDateTime.getDayOfMonth();
		
		// clear date box
		logSynchTextbox.setValue("");
		
		LocalDateTime lastSynchDateTime = LocalDateTime.of(year, month, day, 0, 0);
		logSynchTextbox.setValue("Last Synch: "+lastSynchDateTime.toString()+NEW_LINE);
		
		// algorithm: ge lastSynchDate for the updated_at
		setKejadianList(
				kejadianDao.findNewKejadianByDate(asDate(getCurrentDateTime()), asDate(lastSynchDateTime)));

		String kejadianStr = "";
		
		// if updated_at == created_at --> new
		// else --> update
		for (Kejadian kejadian : kejadianList) {
			if (kejadian.getCreatedAt().compareTo(kejadian.getEditedAt())==0) {
				// new
				getCreateList().add(kejadian);
			} else {
				// update
				getUpdateList().add(kejadian);
			}
		}
		
		kejadianStr = kejadianStr + "Kejadian to Create:"+NEW_LINE;
		for (Kejadian kejadian : getCreateList()) {
			kejadianStr = kejadianStr+kejadian.toString()+NEW_LINE;
		}
		
		kejadianStr = kejadianStr + "Kejadian to Update:"+NEW_LINE;		
		for (Kejadian kejadian : getUpdateList()) {
			kejadianStr = kejadianStr+kejadian.toString()+NEW_LINE;
		}
		
		String textboxValue = logSynchTextbox.getValue();
		logSynchTextbox.setValue(textboxValue+kejadianStr);
	}

	@SuppressWarnings("resource")
	public void onClick$transTableButton(Event event) throws Exception {
		String textboxValue = "";
		
		// clear
		logTransTextbox.setValue("");
		logTransTextbox.setValue("Connecting to Cloud Database..."+NEW_LINE);
		// connect to google cloud mysql database
		ApplicationContext ctx = 
				new ClassPathXmlApplicationContext("CommonContext-Cloud-Dao.xml");
		
		textboxValue = logTransTextbox.getValue();
		logTransTextbox.setValue(textboxValue+"Accessing Cloud Database schema operations..."+NEW_LINE);
		KejadianDao kejadianDao = (KejadianDao) ctx.getBean("kejadianDao");

		textboxValue = logTransTextbox.getValue();		
		for (Kejadian kejadian : getCreateList()) {
			logTransTextbox.setValue(textboxValue+"Saving: ID: "+kejadian.getSerialNumber().getSerialComp());
			kejadianDao.save(kejadianToCloud(kejadian));

			textboxValue = logTransTextbox.getValue();
			logTransTextbox.setValue(textboxValue+"...Success!!!"+NEW_LINE);
		}
	}
	
	private Kejadian kejadianToCloud(Kejadian kejadian) {
		Kejadian cldKejadian = new Kejadian();
		cldKejadian.setCreatedAt(asDate(getCurrentDateTime()));
		cldKejadian.setEditedAt(asDate(getCurrentDateTime()));
		cldKejadian.setSerialNumber(getKejadianSerialNumber(kejadian.getSerialNumber()));
		cldKejadian.setTwPembuatanDateTime(kejadian.getTwPembuatanDateTime());
		cldKejadian.setTwPembuatanTimezone(kejadian.getTwPembuatanTimezone());
		cldKejadian.setTwKejadianDateTime(kejadian.getTwKejadianDateTime());
		cldKejadian.setTwKejadianTimezone(kejadian.getTwKejadianTimezone());
		
		return cldKejadian;
	}
	
	private DocumentSerialNumber getKejadianSerialNumber(DocumentSerialNumber serialNumber) {
		DocumentSerialNumber serialNum = new DocumentSerialNumber();
		serialNum.setCreatedAt(serialNumber.getCreatedAt());
		serialNum.setEditedAt(serialNumber.getEditedAt());
		serialNum.setDocumentCode(serialNumber.getDocumentCode());
		serialNum.setSerialDate(serialNumber.getSerialDate());
		serialNum.setSerialNo(serialNumber.getSerialNo());
		serialNum.setSerialComp(serialNumber.getSerialComp());
		
		return serialNum;
	}

	public KejadianDao getKejadianDao() {
		return kejadianDao;
	}

	public void setKejadianDao(KejadianDao kejadianDao) {
		this.kejadianDao = kejadianDao;
	}

	public LocalDateTime getCurrentDateTime() {
		return currentDateTime;
	}

	public void setCurrentDateTime(LocalDateTime currentDateTime) {
		this.currentDateTime = currentDateTime;
	}

	public SettingsDao getSettingsDao() {
		return settingsDao;
	}

	public void setSettingsDao(SettingsDao settingsDao) {
		this.settingsDao = settingsDao;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public List<Kejadian> getKejadianList() {
		return kejadianList;
	}

	public void setKejadianList(List<Kejadian> kejadianList) {
		this.kejadianList = kejadianList;
	}

	public LocalDateTime getLastSynchDateTime() {
		return lastSynchDateTime;
	}

	public void setLastSynchDateTime(LocalDateTime lastSynchDateTime) {
		this.lastSynchDateTime = lastSynchDateTime;
	}

	public List<Kejadian> getCreateList() {
		return createList;
	}

	public void setCreateList(List<Kejadian> createList) {
		this.createList = createList;
	}

	public List<Kejadian> getUpdateList() {
		return updateList;
	}

	public void setUpdateList(List<Kejadian> updateList) {
		this.updateList = updateList;
	}
	
	
}
