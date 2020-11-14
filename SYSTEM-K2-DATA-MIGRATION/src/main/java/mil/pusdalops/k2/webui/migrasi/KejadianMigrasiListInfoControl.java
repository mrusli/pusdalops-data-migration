package mil.pusdalops.k2.webui.migrasi;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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

import mil.pusdalops.domain.gmt.TimezoneInd;
import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.settings.Settings;
import mil.pusdalops.k2.domain.sql.Tkp;
import mil.pusdalops.k2.persistence.kejadian.dao.KejadianDao;
import mil.pusdalops.k2.persistence.settings.dao.SettingsDao;
import mil.pusdalops.k2.persistence.sql.tkp.dao.TkpSqlDao;
import mil.pusdalops.k2.webui.common.GFCBaseController;

public class KejadianMigrasiListInfoControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1645063041245026282L;

	private TkpSqlDao tkpSqlDao;
	private SettingsDao settingsDao;
	private KejadianDao kejadianDao;
	
	private Window kejadianMigrasiListInfoWin;
	private Label formTitleLabel, infoResultlabel;
	private Listbox tkpListbox;
	
	private Settings settings;
	private Kotamaops kotamaops;
	private LocalDateTime currentLocalDateTime;
	
	private List<Tkp> tkpList;
	
	private final long SETTINGS_DEFAULT_ID = 1L;
	private final Logger log = Logger.getLogger(KejadianMigrasiListInfoControl.class);
	
	public void onCreate$kejadianMigrasiListInfoWin(Event event) throws Exception {
		setSettings(
				getSettingsDao().findSettingsById(SETTINGS_DEFAULT_ID));

		setKotamaops(
				getSettings().getSelectedKotamaops());
		
		TimezoneInd timezoneInd = getKotamaops().getTimeZone();
		int timezoneIndOrdinal = timezoneInd.ordinal();
		
		setCurrentLocalDateTime(
				getLocalDateTime(timezoneInd.toZoneId(timezoneIndOrdinal)));
		
		formTitleLabel.setValue("Data Migrasi | Kejadian (dari SQL Server Tabel: TKP KRONOLOGIS KERUGIAN)");
		
		// load tkp sql server data
		loadTkpSQlServerData();
		
		// display tkp sql server data
		displayTkpListInfo();
	}
	
	private void loadTkpSQlServerData() throws Exception {
		setTkpList(getTkpSqlDao().findAllTkp());
	}

	private void displayTkpListInfo() {
		tkpListbox.setModel(new ListModelList<Tkp>(getTkpList()));
		tkpListbox.setItemRenderer(getTkpListitemRenderer());
	}

	private ListitemRenderer<Tkp> getTkpListitemRenderer() {

		return new ListitemRenderer<Tkp>() {
			
			@Override
			public void render(Listitem item, Tkp tkp, int index) throws Exception {
				Listcell lc;
				
				// ID - idr_str
				lc = new Listcell(tkp.getId_str());
				lc.setParent(item);
				
				// TW - thn tw ketwaktu
				lc = new Listcell(tkp.getThn()+" "+tkp.getTw()+" "+tkp.getKetwaktu());
				lc.setParent(item);
				
				// Kotamops - kotamaops
				lc = new Listcell(tkp.getKOTAMAOPS());
				lc.setParent(item);
				
				// Klasifikasi - klasifikasi
				lc = new Listcell(tkp.getKLASIFIKASI());
				lc.setParent(item);
				
				// Motif - motif
				lc = new Listcell(tkp.getMOTIF());
				lc.setParent(item);
								
				// Kerugian - button to laod from Kerugian table
				lc = initKerugian(new Listcell(), tkp);
				lc.setParent(item);
				
				// Migrasi - button to migrate
				lc = initMigrasi(new Listcell(), tkp);
				lc.setParent(item);
				
				item.setValue(tkp);
			}

			private Listcell initKerugian(Listcell listcell, Tkp tkp) {
				Button kerugianButton = new Button();
				kerugianButton.setLabel("Kerugian");
				kerugianButton.setClass("kerugianEditButton");
				kerugianButton.setParent(listcell);
				kerugianButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						if (tkp.getKejadian()==null) {
							throw new Exception("Data TKP belum dimigrasikan...");
						}
						
						MigrasiData migrasiData = new MigrasiData();
						migrasiData.setTkp(tkp);
						migrasiData.setCurrentSettings(getSettings());
						Map<String, MigrasiData> args = Collections.singletonMap("migrasiData", migrasiData);
						Window migrasiKerugianWin = (Window) Executions.createComponents(
								"/migrasi/kejadian/KejadianKerugianMigrasiListInfo.zul", kejadianMigrasiListInfoWin, args);
						
						migrasiKerugianWin.doModal();
					}


				});
				
				return listcell;
			}

			private Listcell initMigrasi(Listcell listcell, Tkp tkp) {
				Button migrasiButton = new Button();
				migrasiButton.setLabel("Migrasi");
				migrasiButton.setClass("migrasiButton");
				migrasiButton.setParent(listcell);
				migrasiButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						MigrasiData migrasiData = new MigrasiData();
						migrasiData.setTkp(tkp);
						migrasiData.setCurrentSettings(getSettings());
						Map<String, MigrasiData> args = Collections.singletonMap("migrasiData", migrasiData);
						Window migrasiWin = (Window) Executions.createComponents(
								"/migrasi/kejadian/KejadianMigrasiDialog.zul", kejadianMigrasiListInfoWin, args);
						migrasiWin.addEventListener(Events.ON_OK, new EventListener<Event>() {

							@Override
							public void onEvent(Event event) throws Exception {
								// get the object -- Kejadian
								Kejadian kejadian = (Kejadian) event.getData();
								
								tkp.setKejadian(kejadian);
								tkp.setTransfferedAt(asDate(getCurrentLocalDateTime()));
								// update tkpsql
								getTkpSqlDao().update(tkp);
								//
								log.info("Sukses update tabel SQLServer TKP.");
								// re-display
								displayTkpListInfo();
																
								/*
								 * try { // save the object getKejadianDao().save(kejadian); //
								 * log.info("Sukses migrasi ke table Kejadian MySQL."); } catch (Exception e) {
								 * log.info("Gagal migrasi ke table Kejadian MySQL."); throw new
								 * Exception("Gagal migrasi ke tabel Kejadian MySQL."); } // update tkpsql --
								 * transferred_at getTkpSqlDao().update(tkp);
								 */
							}
						});
						
						migrasiWin.doModal();
					}
				});
				// check already migrated??? - tkp transferred_at not null - disable migrasiButton
				migrasiButton.setDisabled(tkp.getTransfferedAt()!=null);
				
				return listcell;
			}

		};
	}
	
	public void onClick$tkpListbox(Event event) throws Exception {
		infoResultlabel.setValue("Total: "+tkpListbox.getItemCount()+" kejadian");
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public SettingsDao getSettingsDao() {
		return settingsDao;
	}

	public void setSettingsDao(SettingsDao settingsDao) {
		this.settingsDao = settingsDao;
	}

	public Kotamaops getKotamaops() {
		return kotamaops;
	}

	public void setKotamaops(Kotamaops kotamaops) {
		this.kotamaops = kotamaops;
	}

	public List<Tkp> getTkpList() {
		return tkpList;
	}

	public void setTkpList(List<Tkp> tkpList) {
		this.tkpList = tkpList;
	}

	public TkpSqlDao getTkpSqlDao() {
		return tkpSqlDao;
	}

	public void setTkpSqlDao(TkpSqlDao tkpSqlDao) {
		this.tkpSqlDao = tkpSqlDao;
	}

	public KejadianDao getKejadianDao() {
		return kejadianDao;
	}

	public void setKejadianDao(KejadianDao kejadianDao) {
		this.kejadianDao = kejadianDao;
	}

	public LocalDateTime getCurrentLocalDateTime() {
		return currentLocalDateTime;
	}

	public void setCurrentLocalDateTime(LocalDateTime currentLocalDateTime) {
		this.currentLocalDateTime = currentLocalDateTime;
	}
}
