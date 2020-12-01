package mil.pusdalops.k2.webui.dialog;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.kotamaops.KotamaopsType;
import mil.pusdalops.k2.persistence.kotamaops.dao.KotamaopsDao;
import mil.pusdalops.k2.webui.common.GFCBaseController;

public class KotamaopsListDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6971688579701376471L;

	private KotamaopsDao kotamaopsDao;

	private Window kotamaopsListDialogWin;
	private Listbox kotamaopsListbox;
	private Button selectButton;
	private Label namaKotamaopsLabel;
	
	private String namaKotamaops;
	private List<Kotamaops> kotamaopsList;
	private ListModelList<Kotamaops> kotamaopsListModelList;
	private Kotamaops selectedKotamaops;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setNamaKotamaops(
				(String) Executions.getCurrent().getArg().get("namaKotamaops"));
	}
	
	public void onCreate$kotamaopsListDialogWin(Event event) throws Exception {
		
		namaKotamaopsLabel.setValue(getNamaKotamaops());
				
		// load
		loadKotamaops();
		
		// display
		displayKotamaopsListInfo();
	}
	
	private void loadKotamaops() throws Exception {
		setKotamaopsList(
				getKotamaopsDao().findAllKotamaopsWithOrder(true));
		// remove the PUSADALOPS
		// getKotamaopsList().remove(0);
		List<Kotamaops> kotamaopsListFiltered = new ArrayList<Kotamaops>();
		for (Kotamaops kotamaops : getKotamaopsList()) {
			if (kotamaops.getKotamaopsType().equals(KotamaopsType.PUSDALOPS)) {
				// do nothing
			} else {
				kotamaopsListFiltered.add(kotamaops);
			}
		}
		
		setKotamaopsListModelList(
				new ListModelList<Kotamaops>(kotamaopsListFiltered));
	}

	private void displayKotamaopsListInfo() {
		kotamaopsListbox.setModel(getKotamaopsListModelList());
		kotamaopsListbox.setItemRenderer(getKotamaopsListitemRenderer());
	}

	private ListitemRenderer<Kotamaops> getKotamaopsListitemRenderer() {

		return new ListitemRenderer<Kotamaops>() {
			
			BufferedImage buffImg = null;
			
			@Override
			public void render(Listitem item, Kotamaops kotamaops, int index) throws Exception {
				Listcell lc;
				
				item.setClass("expandedRow");
				
				try {
					buffImg = ImageIO.read(new File("/pusdalops/img/logo/"+kotamaops.getImagedId()));
				} catch (Exception e) {
					throw e;
				}
				
				// image logo
				lc = new Listcell();
				lc.setImageContent(buffImg);
				//lc.setImage("/img/logo/"+kotamaops.getImagedId());
				lc.setStyle("text-align: center;");
				lc.setParent(item);
				
				// Nama Kotamaops
				lc = initNamaKotamops(new Listcell(), kotamaops);
				lc.setParent(item);
				
				item.setValue(kotamaops);
			}

			private Listcell initNamaKotamops(Listcell listcell, Kotamaops kotamaops) {
				Label lb1 = new Label();
				lb1.setPre(true);
				lb1.setValue(kotamaops.getKotamaopsName()+" \n"+
						"Zona Waktu: "+kotamaops.getTimeZone().toString()+" \n"+
						"Kode Dokumen: "+kotamaops.getDocumentCode());
				lb1.setParent(listcell);
				
				return listcell;
			}
		};
	}

	/*
	 * USER NOT ALLOWED TO ADD KOTAMAOPS from here.  Must use the Back-End Application.
	 * 
	 * public void onClick$addButton(Event event) throws Exception { Map<String,
	 * String> arg = Collections.singletonMap("namaKotamaops", getNamaKotamaops());
	 * Window addKotamaopsWinDialog = (Window)
	 * Executions.createComponents("/dialog/KotamaopsAddDialog.zul",
	 * kotamaopsListDialogWin, arg);
	 * addKotamaopsWinDialog.addEventListener(Events.ON_CHANGE, new
	 * EventListener<Event>() {
	 * 
	 * @Override public void onEvent(Event event) throws Exception { KotamaopsData
	 * kotamaopsData = (KotamaopsData) event.getData();
	 * 
	 * Kotamaops kotamaops = new Kotamaops();
	 * kotamaops.setKotamaopsName(kotamaopsData.getNamaKotamaops());
	 * kotamaops.setKotamaopsDisplayName(kotamaopsData.getNamaKotamaops());
	 * kotamaops.setTimeZone(kotamaopsData.getTimezoneInd()); //
	 * kotamaops.setKotamaopsType(KotamaopsType.OTHERS);
	 * kotamaops.setDocumentCode(kotamaopsData.getDocumentCode());
	 * 
	 * getKotamaopsListModelList().add(0, kotamaops); } });
	 * 
	 * addKotamaopsWinDialog.doModal(); }
	 */
	
	public void onSelect$kotamaopsListbox(Event event) throws Exception {
		setSelectedKotamaops(
				kotamaopsListbox.getSelectedItem().getValue());
		
		selectButton.setDisabled(false);
	}
	
	public void onClick$selectButton(Event event) throws Exception {
		Events.sendEvent(Events.ON_OK, kotamaopsListDialogWin, getSelectedKotamaops());
		// if (getSelectedKotamaops().getId().compareTo(Long.MIN_VALUE)==0) {			
			  // new -- will add to list Events.sendEvent(Events.ON_CHANGE,
			  // kotamaopsListDialogWin, getSelectedKotamaops()); } else {
			 // existing -- will just select
		// }
		
		kotamaopsListDialogWin.detach();		
	}
	
	public void onClick$cancelButton(Event event) throws Exception {
		
		kotamaopsListDialogWin.detach();
	}
	
	public KotamaopsDao getKotamaopsDao() {
		return kotamaopsDao;
	}

	public void setKotamaopsDao(KotamaopsDao kotamaopsDao) {
		this.kotamaopsDao = kotamaopsDao;
	}

	public String getNamaKotamaops() {
		return namaKotamaops;
	}

	public void setNamaKotamaops(String namaKotamaops) {
		this.namaKotamaops = namaKotamaops;
	}

	public List<Kotamaops> getKotamaopsList() {
		return kotamaopsList;
	}

	public void setKotamaopsList(List<Kotamaops> kotamaopsList) {
		this.kotamaopsList = kotamaopsList;
	}

	public Kotamaops getSelectedKotamaops() {
		return selectedKotamaops;
	}

	public void setSelectedKotamaops(Kotamaops selectedKotamaops) {
		this.selectedKotamaops = selectedKotamaops;
	}

	public ListModelList<Kotamaops> getKotamaopsListModelList() {
		return kotamaopsListModelList;
	}

	public void setKotamaopsListModelList(ListModelList<Kotamaops> kotamaopsListModelList) {
		this.kotamaopsListModelList = kotamaopsListModelList;
	}
}
