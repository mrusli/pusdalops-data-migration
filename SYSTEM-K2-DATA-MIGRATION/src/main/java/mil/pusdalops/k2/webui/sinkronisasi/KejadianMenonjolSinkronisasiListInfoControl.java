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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
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
	private KejadianDao kejadianDao;
	
	private Window kejadianMenonjolSinkronisasiListInfoWin;
	private Label formTitleLabel, infoResultlabel;
	private Listbox kejadianCloudListbox;
	private Combobox statusCombobox;
	
	private ApplicationContext ctx;
	
	private Settings settings;
	private Kotamaops kotamaops;
	private LocalDateTime currentLocalDateTime;
	private List<Kejadian> cloudKejadianList;
	
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
		
		// load status combobox
		loadStatusCombobox();
		
		// accessing cloud database
		ctx = new ClassPathXmlApplicationContext("CommonContext-Cloud-Dao.xml");
		
		loadCloudKejadianData();
		
		displayCloudKejadianListInfo();
	}

	private void loadStatusCombobox() {
		String[] statusSinkronisasi = { "Pending" , "Selesai" };
		
		Comboitem comboitem;
		for (String status : statusSinkronisasi) {
			comboitem = new Comboitem();
			comboitem.setLabel(status);
			comboitem.setValue(status);
			comboitem.setParent(statusCombobox);
		}
		
		statusCombobox.setSelectedIndex(0);
	}

	public void onSelect$statusCombobox(Event event) throws Exception {
		boolean pending = statusCombobox.getSelectedItem().getValue().equals("Pending");

		KejadianDao cloudKejadianDao = (KejadianDao) ctx.getBean("kejadianDao");
		// load data
		setCloudKejadianList(cloudKejadianDao.findAllKejadianNotSynch(pending));		
		// display
		displayCloudKejadianListInfo();
	}
	
	private void loadCloudKejadianData() throws Exception {
		KejadianDao cloudKejadianDao = (KejadianDao) ctx.getBean("kejadianDao");
		// pusdalopsSynchAt -- is null -- DEFAULT
		setCloudKejadianList(cloudKejadianDao.findAllKejadianNotSynch(true));		
	}
	
	private void displayCloudKejadianListInfo() throws Exception {
		kejadianCloudListbox.setModel(
				new ListModelList<Kejadian>(getCloudKejadianList()));
		kejadianCloudListbox.setItemRenderer(getKejadianCloudListitemRenderer());
	}

	private ListitemRenderer<Kejadian> getKejadianCloudListitemRenderer() {

		return new ListitemRenderer<Kejadian>() {
			
			@Override
			public void render(Listitem item, Kejadian cloudKejadian, int index) throws Exception {
				Listcell lc;
				
				item.setSclass("autopaging-content-kejadian-menonjol");
				
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
				lc = new Listcell(cloudKejadian.getJenisKejadian().getNamaJenis());
				lc.setStyle("white-space:nowrap;");
				lc.setParent(item);
				
				// Motif
				lc = new Listcell(cloudKejadian.getMotifKejadian().getNamaMotif());
				lc.setParent(item);
				
				// sinkronisasi dari cloud ke database lokal di Pusdalops !!!
				lc = initSynchronizeFromCloud(new Listcell(), cloudKejadian);
				lc.setParent(item);

				// Tgl Sinkronisasi
				if (cloudKejadian.getPusdalopsSynchAt()==null) {
					lc = new Listcell("");
				} else {
					lc = new Listcell(dateToStringDisplay(asLocalDate(cloudKejadian.getPusdalopsSynchAt()), "dd-MM-yyyy"));
				}
				lc.setParent(item);
				
				// view detail kejadian 
				lc = initViewKejadian(new Listcell(), cloudKejadian);
				lc.setParent(item);
				
				// view detail kerugian -- view kerugian by drop-down box: Pihak-Kita Pihak-Musuh Lain2
				lc = initViewKerugian(new Listcell(), cloudKejadian);
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

			private Listcell initSynchronizeFromCloud(Listcell listcell, Kejadian cloudKejadian) {
				Button synchFromCloudButton = new Button();
				synchFromCloudButton.setLabel("Sinkronisasi");
				synchFromCloudButton.setClass("listinfoEditButton");
				synchFromCloudButton.setParent(listcell);
				synchFromCloudButton.setDisabled(cloudKejadian.getPusdalopsSynchAt()!=null);
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
								displayCloudKejadianListInfo();
							}
						});
						
						synchronizeFromCloudDialogWin.doModal();
					}
				});

				return listcell;
			}

			private Listcell initViewKejadian(Listcell listcell, Kejadian cloudKejadian) {
				Button viewKejadianButton = new Button();
				viewKejadianButton.setLabel("Detail Kejadian");
				viewKejadianButton.setClass("listinfoEditButton");
				viewKejadianButton.setParent(listcell);
				viewKejadianButton.setDisabled(cloudKejadian.getPusdalopsSynchAt()==null);
				viewKejadianButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						// obtain the serial number
						String serialComp = cloudKejadian.getSerialNumber().getSerialComp();
						// find the kejadian object
						Kejadian kejadian = getKejadianDao().findKejadianBySerialComp(serialComp);
						
						Map<String, Kejadian> arg = Collections.singletonMap("kejadian", kejadian);
						Window kejadianDetailWin = (Window) Executions.createComponents(
								"/sinkronisasi/KejadianMenonjolDetail.zul", kejadianMenonjolSinkronisasiListInfoWin, arg);
						
						kejadianDetailWin.doModal();
					}
				});
				
				return listcell;
			}

			private Listcell initViewKerugian(Listcell listcell, Kejadian cloudKejadian) {
				Button viewKerugianButton = new Button();
				viewKerugianButton.setLabel("Detail Kerugian");
				viewKerugianButton.setClass("listinfoEditButton");
				viewKerugianButton.setParent(listcell);
				viewKerugianButton.setDisabled(cloudKejadian.getPusdalopsSynchAt()==null);
				viewKerugianButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						// obtain the serial number
						String serialComp = cloudKejadian.getSerialNumber().getSerialComp();
						// find the kejadian object
						Kejadian kejadian = getKejadianDao().findKejadianBySerialComp(serialComp);

						Map<String, Kejadian> arg = Collections.singletonMap("kejadian", kejadian);
						Window kerugianKejadianWin = (Window) Executions.createComponents(
								"/sinkronisasi/KejadianMenonjolKerugian.zul", kejadianMenonjolSinkronisasiListInfoWin, arg);
						
						kerugianKejadianWin.doModal();
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
		displayCloudKejadianListInfo();
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

	public List<Kejadian> getCloudKejadianList() {
		return cloudKejadianList;
	}

	public void setCloudKejadianList(List<Kejadian> cloudKejadianList) {
		this.cloudKejadianList = cloudKejadianList;
	}

	public KejadianDao getKejadianDao() {
		return kejadianDao;
	}

	public void setKejadianDao(KejadianDao kejadianDao) {
		this.kejadianDao = kejadianDao;
	}
}
