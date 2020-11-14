package mil.pusdalops.k2.webui.main;

import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Include;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menuitem;

import mil.pusdalops.domain.authorization.User;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.kotamaops.KotamaopsType;
import mil.pusdalops.k2.persistence.user.dao.UserDao;
import mil.pusdalops.k2.webui.common.GFCBaseController;
import mil.pusdalops.k2.webui.security.UserSecurityDetails;

public class MainControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4328477574734336973L;

	private UserDao userDao;

	private Menuitem userNameMenuitem;
	private Menu dataMigrasiMenu;
	private Include mainInclude;
	
	private UserSecurityDetails userSecurityDetails;

	public void onCreate$mainDiv(Event event) throws Exception {
		setUserSecurityDetails(
				(UserSecurityDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		
		User loginUser = getUserSecurityDetails().getLoginUser();
		User loginUserByProxy = getUserDao().findUserByProxy(loginUser.getId());
		
		Kotamaops kotamaops = loginUserByProxy.getKotamaops();	
		String username = getUserSecurityDetails().getLoginUser().getUserName();
		
		// check whether kotamaops PUSAT
		dataMigrasiMenu.setVisible(
				kotamaops.getKotamaopsType().compareTo(KotamaopsType.PUSDALOPS)==0);
		
		// set userNameMenuitem with the username and kotamaops
		userNameMenuitem.setLabel(username + " ["+kotamaops.getKotamaopsName()+"]");
	}

	/*
	 * Home Menu
	 */
	
	public void onClick$homeMenuitem(Event event) throws Exception {

		mainInclude.setSrc(null);
	}
	
	/*
	 * Data-Migrasi Menu
	 */
	
	public void onClick$migrasiKejadianMenuitem(Event event) throws Exception {
		
		mainInclude.setSrc("/migrasi/kejadian/KejadianMigrasiListInfo.zul");		
	}
	
	public void onClick$migrasiLaporanLainMenuitem(Event event) throws Exception {
		
		mainInclude.setSrc("/migrasi/laporan/LaporanMigrasiListInfo.zul");
	}

	public void onClick$migrasiPejabatSiagaMenuitem(Event event) throws Exception {
		
		mainInclude.setSrc("/migrasi/siaga/SiagaMigrasiListInfo.zul");
	}
	
	public void onClick$migrasiGelarOperasiMenuitem(Event event) throws Exception {

		mainInclude.setSrc("/migrasi/operasi/OperasiMigrasiListInfo.zul");
	}

	public void onClick$migrasiDataPejabatMenuitem(Event event) throws Exception {
		
		mainInclude.setSrc("/migrasi/pejabat/PejabatMigrasiListInfo.zul");
	}

	/*
	 * Sinkronisasi Menu
	 */
	
	public void onClick$sinkronisasiKejadianMenonjolMenuitem(Event event) throws Exception {
		
		mainInclude.setSrc("/sinkronisasi/KejadianMenonjolSinkronisasiListInfo.zul");
	}
	
	
	/*
	 * Profil Menu
	 */
	
	public void onClick$userNameMenuitem(Event event) throws Exception {
		
		mainInclude.setSrc("/profile/UserProfile.zul");
	}
	
	/*
	 * Get / Set
	 */
	
	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public UserSecurityDetails getUserSecurityDetails() {
		return userSecurityDetails;
	}

	public void setUserSecurityDetails(UserSecurityDetails userSecurityDetails) {
		this.userSecurityDetails = userSecurityDetails;
	}
}
