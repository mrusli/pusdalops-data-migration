package mil.pusdalops.k2.webui.migrasi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.domain.kerugian.Kerugian;
import mil.pusdalops.domain.kerugian.Pihak;
import mil.pusdalops.k2.persistence.kejadian.dao.KejadianDao;
import mil.pusdalops.k2.webui.common.GFCBaseController;

public class KerugianListInfoControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4360702372762358810L;

	private KejadianDao kejadianDao;

	private Window kerugianListInfoWin;
	private Label formTitleLabel, infoResultlabel;
	private Listbox kerugianListbox;
	
	private KejadianData kejadianData;
	private Pihak paraPihak;
	private Kejadian mainKejadianByProxy;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setKejadianData((KejadianData) arg.get("kejadianData"));
	}

	public void onCreate$kerugianListInfoWin(Event event) throws Exception {
		setParaPihak(getKejadianData().getParaPihak());
		
		formTitleLabel.setValue("Kerugian PIhak "+getParaPihak().toString());
		
		mainKejadianByProxy = 
				getKejadianDao().findKejadianKerugiansByProxy(getKejadianData().getKejadian().getId());
		
		displayKerugianListInfo(getKerugiansByParaPihak(mainKejadianByProxy, getParaPihak()));
	}
	
	
	private List<Kerugian> getKerugiansByParaPihak(Kejadian kejadian, Pihak pihak) {
		
		List<Kerugian> kerugians = new ArrayList<Kerugian>();
		
		for (Kerugian kerugian : kejadian.getKerugians()) {
			if (kerugian.getParaPihak().equals(pihak)) {
				kerugians.add(kerugian);
			}
		}
		
		return kerugians;
	}

	private void displayKerugianListInfo(List<Kerugian> kerugianList) {
		kerugianListbox.setModel(
				new ListModelList<Kerugian>(kerugianList));
		kerugianListbox.setItemRenderer(getKerugianListitemRenderer());
		
	}

	private ListitemRenderer<Kerugian> getKerugianListitemRenderer() {
		
		return new ListitemRenderer<Kerugian>() {

			@Override
			public void render(Listitem item, Kerugian kerugian, int index) throws Exception {
				Listcell lc;
				
				// Nama Pers/Mat
				lc = new Listcell(kerugian.getNamaMaterial());
				lc.setParent(item);

				// Tipe Kerugian
				lc = new Listcell(kerugian.getTipeKerugian().toString());
				lc.setParent(item);
				
				// ID
				lc = new Listcell(kerugian.getLembagaTerkait().toString());
				lc.setParent(item);
				
				// Jenis
				lc = new Listcell(kerugian.getKerugianJenis().getNamaJenis());
				lc.setParent(item);
				
				// Kondisi
				lc = new Listcell(kerugian.getKerugianKondisi().getNamaKondisi());
				lc.setParent(item);
				
				// Jumlah
				lc = new Listcell(String.valueOf(kerugian.getJumlah()));
				lc.setParent(item);
				
				// Keterangan
				lc = new Listcell(kerugian.getKeterangan());
				lc.setStyle("white-space: nowrap;");
				lc.setParent(item);
				
				// edit
				lc = initEdit(new Listcell(), kerugian);
				lc.setParent(item);

				item.setValue(kerugian);				
			}

			private Listcell initEdit(Listcell listcell, Kerugian kerugian) {
				Button editButton = new Button();
				editButton.setLabel("...");
				editButton.setSclass("listinfoEditButton");
				editButton.setParent(listcell);
				editButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						KerugianData kerugianData = new KerugianData();
						kerugianData.setParaPihak(getParaPihak());
						kerugianData.setKerugian(kerugian);
						Kejadian kejadianKotamaopsByProxy = 
								getKejadianDao().findKejadianKotamaopsByProxy(
										getKejadianData().getKejadian().getId());
						kerugianData.setKotamaops(kejadianKotamaopsByProxy.getKotamaops());
						kerugianData.setKejadian(getKejadianData().getKejadian());
						
						Map<String, KerugianData> args = Collections.singletonMap("kerugianData", kerugianData);
						Window kerugianDialogWin = 
								(Window) Executions.createComponents("/migrasi/kejadian/KerugianDialog.zul", kerugianListInfoWin, args);
						kerugianDialogWin.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

							@Override
							public void onEvent(Event event) throws Exception {
								// update
								getKejadianDao().update(mainKejadianByProxy);
								
								// re-display
								displayKerugianListInfo(
										getKerugiansByParaPihak(mainKejadianByProxy, getParaPihak()));
							}
						});
						
						kerugianDialogWin.doModal();
					}
				});
				return listcell;
			}
		};
	}

	public void onAfterRender$kerugianListbox(Event event) throws Exception {
		infoResultlabel.setValue("Total: "+kerugianListbox.getItemCount()+" kerugian");
	}
	
	public void onClick$closeButton(Event event) throws Exception {
		kerugianListInfoWin.detach();
	}
	
	public KejadianDao getKejadianDao() {
		return kejadianDao;
	}

	public void setKejadianDao(KejadianDao kejadianDao) {
		this.kejadianDao = kejadianDao;
	}

	public KejadianData getKejadianData() {
		return kejadianData;
	}

	public void setKejadianData(KejadianData kejadianData) {
		this.kejadianData = kejadianData;
	}

	public Pihak getParaPihak() {
		return paraPihak;
	}

	public void setParaPihak(Pihak paraPihak) {
		this.paraPihak = paraPihak;
	}
	
}
