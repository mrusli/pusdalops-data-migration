package mil.pusdalops.k2.webui.sinkronisasi;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
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
import mil.pusdalops.k2.persistence.kejadian.dao.KejadianDao;
import mil.pusdalops.k2.persistence.settings.dao.SettingsDao;
import mil.pusdalops.k2.webui.common.GFCBaseController;

public class KejadianMenonjolSinkronisasiListInfoControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1860393662202122974L;

	private SettingsDao settingsDao;
	
	private Window kejadianMenonjolSinkronisasiListInfoWin;
	private Label formTitleLabel, infoResultlabel;
	private Listbox kejadianCloudListbox;
	
	private ApplicationContext ctx;
	
	private Settings settings;
	private Kotamaops kotamaops;
	private LocalDateTime currentLocalDateTime;
	
	private final long SETTINGS_DEFAULT_ID = 1L;
	
	public void onCreate$kejadianMenonjolSinkronisasiListInfoWin(Event event) throws Exception {
		setSettings(
				getSettingsDao().findSettingsById(SETTINGS_DEFAULT_ID));
		setKotamaops(
				getSettings().getSelectedKotamaops());
		
		// set current datetime from kotamaops settings
		TimezoneInd timezoneInd = getKotamaops().getTimeZone();
		int timezoneIndOrdinal = timezoneInd.ordinal();
		// set current datetime
		setCurrentLocalDateTime(getLocalDateTime(timezoneInd.toZoneId(timezoneIndOrdinal)));
		
		formTitleLabel.setValue("Sinkronisasi | Kejadian Menonjol - Kotamaops : "+
				getKotamaops().getKotamaopsName());
		
		// accessing cloud database
		ctx = new ClassPathXmlApplicationContext("CommonContext-Cloud-Dao.xml");
		
		displayCloudKejadianListInfo(ctx);
	}

	private void displayCloudKejadianListInfo(ApplicationContext ctx) throws Exception {
		KejadianDao cloudKejadianDao = (KejadianDao) ctx.getBean("kejadianDao");
		// pusdalopsSynchAt -- is null
		List<Kejadian> cloudKejadianList = cloudKejadianDao.findAllKejadianNotSynch();
		kejadianCloudListbox.setModel(
				new ListModelList<Kejadian>(cloudKejadianList));
		kejadianCloudListbox.setItemRenderer(getKejadianCloudListitemRenderer());
	}

	private ListitemRenderer<Kejadian> getKejadianCloudListitemRenderer() {

		return new ListitemRenderer<Kejadian>() {
			
			@Override
			public void render(Listitem item, Kejadian cloudKejadian, int index) throws Exception {
				Listcell lc;
				
				// Kotamops
				lc = initKotamaops(new Listcell(), cloudKejadian);
				lc.setParent(item);

				// ID
				lc = new Listcell(cloudKejadian.getSerialNumber().getSerialComp());
				lc.setParent(item);
				
				// TW
				lc = new Listcell(cloudKejadian.getTwPembuatanDateTime().toString() + " " +
						cloudKejadian.getTwPembuatanTimezone().toString());
				lc.setParent(item);
				
				// Jenis Kejadian
				lc = new Listcell();
				lc.setParent(item);
				
				// Motif
				lc = new Listcell();
				lc.setParent(item);
				
				// view detail kejadian 
				lc = initViewKejadian(new Listcell(), cloudKejadian);
				lc.setParent(item);
				
				// view detail kerugian -- view kerugian by drop-down box: Pihak-Kita Pihak-Musuh Lain2
				lc = initViewKerugian(new Listcell(), cloudKejadian);
				lc.setParent(item);
				
				// sinkronisasi dari cloud ke database lokal di Pusdalops !!!
				lc = initSynchronizeFromCloud(new Listcell(), cloudKejadian);
				lc.setParent(item);
			}

			private Listcell initKotamaops(Listcell listcell, Kejadian cloudKejadian) throws Exception {
				KejadianDao cloudKejadianDao = (KejadianDao) ctx.getBean("kejadianDao");
				Kejadian cloudKejadianKotamaopsByProxy =
						cloudKejadianDao.findKejadianKotamaopsByProxy(cloudKejadian.getId());

				Label namaKotamaopsLabel = new Label();
				namaKotamaopsLabel.setValue(cloudKejadianKotamaopsByProxy.getKotamaops().getKotamaopsName());
				namaKotamaopsLabel.setParent(listcell);
				
				return listcell;
			}

			private Listcell initViewKejadian(Listcell listcell, Kejadian cloudKejadian) {
				Button viewKejadianButton = new Button();
				viewKejadianButton.setLabel("Detail Kejadian");
				viewKejadianButton.setClass("listinfoEditButton");
				viewKejadianButton.setParent(listcell);
				viewKejadianButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						
					}
				});
				
				return listcell;
			}

			private Listcell initViewKerugian(Listcell listcell, Kejadian cloudKejadian) {
				Button viewKerugianButton = new Button();
				viewKerugianButton.setLabel("Detail Kerugian");
				viewKerugianButton.setClass("listinfoEditButton");
				viewKerugianButton.setParent(listcell);
				viewKerugianButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						
					}
				});

				return listcell;
			}

			private Listcell initSynchronizeFromCloud(Listcell listcell, Kejadian cloudKejadian) {
				Button synchFromCloudButton = new Button();
				synchFromCloudButton.setLabel("Sinkronisasi");
				synchFromCloudButton.setClass("listinfoEditButton");
				synchFromCloudButton.setParent(listcell);
				synchFromCloudButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						SinkronisasiData sinkronisasiData = new SinkronisasiData();
						sinkronisasiData.setCloudKejadian(cloudKejadian);
						sinkronisasiData.setCurrentLocalDateTime(getCurrentLocalDateTime());
						
						Map<String, SinkronisasiData> arg = Collections.singletonMap("sinkronisasiData", sinkronisasiData);
						Window synchronizeFromCloudDialogWin = 
								(Window) Executions.createComponents(
										"/sinkronisasi/KejadianMenonjolSinkronisasiDialog.zul", kejadianMenonjolSinkronisasiListInfoWin, arg);
						synchronizeFromCloudDialogWin.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

							@Override
							public void onEvent(Event event) throws Exception {
								// re-display
								displayCloudKejadianListInfo(ctx);
							}
						});
						
						synchronizeFromCloudDialogWin.doModal();
					}
				});

				return listcell;
			}

		};
	}

	public void onAfterRender$kejadianCloudListbox(Event event) throws Exception {
		infoResultlabel.setValue("Total: "+kejadianCloudListbox.getItemCount()+" kejadian dari Kotamaops");
	}
	
	public void onClick$refreshButton(Event event) throws Exception {
		displayCloudKejadianListInfo(ctx);
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

	public Kotamaops getKotamaops() {
		return kotamaops;
	}

	public void setKotamaops(Kotamaops kotamaops) {
		this.kotamaops = kotamaops;
	}

	public LocalDateTime getCurrentLocalDateTime() {
		return currentLocalDateTime;
	}

	public void setCurrentLocalDateTime(LocalDateTime currentLocalDateTime) {
		this.currentLocalDateTime = currentLocalDateTime;
	}
}
