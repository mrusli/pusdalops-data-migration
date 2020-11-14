package mil.pusdalops.k2.webui.migrasi;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
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
import mil.pusdalops.domain.settings.Settings;
import mil.pusdalops.k2.domain.sql.Kerugian;
import mil.pusdalops.k2.domain.sql.Tkp;
import mil.pusdalops.k2.persistence.kejadian.dao.KejadianDao;
import mil.pusdalops.k2.persistence.sql.kerugian.dao.KerugianSqlDao;
import mil.pusdalops.k2.persistence.sql.tkp.dao.TkpSqlDao;
import mil.pusdalops.k2.webui.common.GFCBaseController;

public class KejadianKerugianMigrasiListInfoWinControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2937325012378384951L;

	private KerugianSqlDao kerugianSqlDao;
	private TkpSqlDao tkpSqlDao;
	private KejadianDao kejadianDao;
	
	private Window kejadianKerugianMigrasiListInfoWin;
	private Label formTitleLabel;
	private Listbox kerugianListbox;
	
	private MigrasiData migrasiData;
	private Tkp tkp;
	private List<Kerugian> sqlKerugianList; 
	private Settings currentSettings;
	private TimezoneInd timezoneInd; 
	
	private final Logger log = Logger.getLogger(KejadianKerugianMigrasiListInfoWinControl.class);
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setMigrasiData((MigrasiData) arg.get("migrasiData"));
	}

	public void onCreate$kejadianKerugianMigrasiListInfoWin(Event event) throws Exception {
		setTkp(getMigrasiData().getTkp());
		formTitleLabel.setValue("Migrasi Kerugian | Kejadian (TKP) ID: "+
				getTkp().getId_str());
		
		setCurrentSettings(
				getMigrasiData().getCurrentSettings());
		
		// current settings kotamatops current settings
		setTimezoneInd(
				getCurrentSettings().getSelectedKotamaops().getTimeZone());
		
		
		loadSQLKerugianData();
		
		displaySQLKerugianData();
		
	}
	
	private void loadSQLKerugianData() throws Exception {
		String tkpIdStr = getTkp().getId_str();
		setSqlKerugianList(
				getKerugianSqlDao().findAllKerugianSqlByIdStr(tkpIdStr));
		
		log.info(sqlKerugianList.toString());
	}

	private void displaySQLKerugianData() {
		kerugianListbox.setModel(
				new ListModelList<Kerugian>(getSqlKerugianList()));
		kerugianListbox.setItemRenderer(getSqlKerugianListitemRenderer());
	}
	
	
	private ListitemRenderer<Kerugian> getSqlKerugianListitemRenderer() {
		
		return new ListitemRenderer<Kerugian>() {
			
			@Override
			public void render(Listitem item, Kerugian sqlKerugian, int index) throws Exception {
				Listcell lc;
				
				// Pihak
				lc = new Listcell(sqlKerugian.getPihak());
				lc.setParent(item);
				
				// Pihak_Id
				lc = new Listcell(sqlKerugian.getPihak_id());
				lc.setParent(item);
				
				// Nama Obj
				lc = new Listcell(sqlKerugian.getNama_obj());
				lc.setParent(item);
				
				// Jenis
				lc = new Listcell(sqlKerugian.getJenis());
				lc.setParent(item);
				
				// Kondisi
				lc = new Listcell(sqlKerugian.getKondisi());
				lc.setParent(item);
				
				// Jumlah
				lc = new Listcell(String.valueOf(sqlKerugian.getJumlah()));
				lc.setParent(item);
				
				// Satuan
				lc = new Listcell(sqlKerugian.getSatuan());
				lc.setParent(item);
				
				// Keterangan
				lc = new Listcell(sqlKerugian.getKeterangan());
				lc.setParent(item);

				// migrasi
				lc = initKerugianMigrasi(new Listcell(), sqlKerugian);
				lc.setParent(item);
			}

			private Listcell initKerugianMigrasi(Listcell listcell, Kerugian sqlKerugian) {
				Button kerugMigButton = new Button();
				kerugMigButton.setLabel("Migrasi");
				kerugMigButton.setSclass("listinfoEditButton");
				// if sqlKerugian.getTransferAt is not null -- disable the button
				kerugMigButton.setDisabled(sqlKerugian.getTransferredAt()!=null);
				kerugMigButton.setParent(listcell);
				kerugMigButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						KejadianKerugianData kejadianKerugianData = new KejadianKerugianData();
						kejadianKerugianData.setSqlTkp(getTkp());
						kejadianKerugianData.setSqlKerugian(sqlKerugian);
						kejadianKerugianData.setCurrentSettings(getMigrasiData().getCurrentSettings());
						Map<String,KejadianKerugianData> arg = 
								Collections.singletonMap("kejadianKerugianData", kejadianKerugianData);
						Window kejadianKerugianMigrasiDialogWin = 
								(Window) Executions.createComponents(
										"/migrasi/kejadian/KejadianKerugianMigrasiDialog.zul", 
										kejadianKerugianMigrasiListInfoWin, arg);
						
						kejadianKerugianMigrasiDialogWin.addEventListener(Events.ON_OK, new EventListener<Event>() {

							@Override
							public void onEvent(Event event) throws Exception {
								mil.pusdalops.domain.kerugian.Kerugian kerugian = 
										(mil.pusdalops.domain.kerugian.Kerugian) event.getData();

								int timezoneIndOrdinal = getTimezoneInd().ordinal();
								
								// get the Kejadian from tkp -- requires proxy
								Tkp tkpKejadianByProxy  = getTkpSqlDao().findTkpKejadianByProxy(getTkp().getId());
								Kejadian kejadian = tkpKejadianByProxy.getKejadian();
								kejadian.setEditedAt(asDate(getLocalDateTime(getTimezoneInd().toZoneId(timezoneIndOrdinal))));
								
								// get the list of Kerugian from Kejadian -- requires proxy
								Kejadian kejadinKerugiansByProxy = getKejadianDao().findKejadianKerugiansByProxy(kejadian.getId());
								List<mil.pusdalops.domain.kerugian.Kerugian> kerugianList = kejadinKerugiansByProxy.getKerugians();
												
								// add kerugian to the list of kerugian from Kejadian
								kerugianList.add(kerugian);
								
								// update Kejadian proxy
								// getTkpSqlDao().update(tkpKejadianByProxy);
								getKejadianDao().update(kejadinKerugiansByProxy);
								
								// update sqlKerugian -- set transferAt								
								sqlKerugian.setTransferredAt(asDate(getLocalDateTime(getTimezoneInd().toZoneId(timezoneIndOrdinal))));
								getKerugianSqlDao().update(sqlKerugian);
								
								// re-load
								loadSQLKerugianData();
								
								// re-display
								displaySQLKerugianData();
							}
						});
						
						kejadianKerugianMigrasiDialogWin.doModal();
					}
				});
				
				return listcell;
			}
		};
	}

	public void onClick$closeButton(Event event) throws Exception {
		kejadianKerugianMigrasiListInfoWin.detach();
	}

	public MigrasiData getMigrasiData() {
		return migrasiData;
	}

	public void setMigrasiData(MigrasiData migrasiData) {
		this.migrasiData = migrasiData;
	}

	public Tkp getTkp() {
		return tkp;
	}

	public void setTkp(Tkp tkp) {
		this.tkp = tkp;
	}

	public KerugianSqlDao getKerugianSqlDao() {
		return kerugianSqlDao;
	}

	public void setKerugianSqlDao(KerugianSqlDao kerugianSqlDao) {
		this.kerugianSqlDao = kerugianSqlDao;
	}

	public List<Kerugian> getSqlKerugianList() {
		return sqlKerugianList;
	}

	public void setSqlKerugianList(List<Kerugian> sqlKerugianList) {
		this.sqlKerugianList = sqlKerugianList;
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

	public Settings getCurrentSettings() {
		return currentSettings;
	}

	public void setCurrentSettings(Settings currentSettings) {
		this.currentSettings = currentSettings;
	}

	public TimezoneInd getTimezoneInd() {
		return timezoneInd;
	}

	public void setTimezoneInd(TimezoneInd timezoneInd) {
		this.timezoneInd = timezoneInd;
	}
	
}
