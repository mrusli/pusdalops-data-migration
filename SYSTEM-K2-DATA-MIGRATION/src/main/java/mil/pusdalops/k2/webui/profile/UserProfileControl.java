package mil.pusdalops.k2.webui.profile;

import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import mil.pusdalops.domain.authorization.User;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.k2.persistence.user.dao.UserDao;
import mil.pusdalops.k2.webui.common.GFCBaseController;
import mil.pusdalops.k2.webui.security.UserSecurityDetails;

public class UserProfileControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8764655247030539932L;

	private UserDao userDao;

	private Label formTitleLabel;
	private Textbox kotamaopsTextbox, userNameTextbox;
	private Image kotamaopsLogoImage;
	
	private UserSecurityDetails userSecurityDetails;	
	private Kotamaops kotamaops;
	private String namaLoginUser, namaKotamaops;
	
	public void onCreate$userProfileWin(Event event) throws Exception {
		setUserSecurityDetails(
				(UserSecurityDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		
		User loginUser = getUserSecurityDetails().getLoginUser();
		User loginUserByProxy = getUserDao().findUserByProxy(loginUser.getId());
		
		setNamaLoginUser(
				loginUser.getUserName());
		setKotamaops(
				loginUserByProxy.getKotamaops());
		setNamaKotamaops(
				getKotamaops().getKotamaopsName());

		formTitleLabel.setValue("Profil | "+getNamaLoginUser()+"["+getNamaKotamaops()+"]");
		
		// display info
		displayUserProfileInfo();
	}
	
	private void displayUserProfileInfo() {
		kotamaopsLogoImage.setSrc("/img/logo/"+getKotamaops().getImageId01());
		kotamaopsTextbox.setValue(getNamaKotamaops());
		userNameTextbox.setValue(getNamaLoginUser());		
	}

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

	public String getNamaLoginUser() {
		return namaLoginUser;
	}

	public void setNamaLoginUser(String namaLoginUser) {
		this.namaLoginUser = namaLoginUser;
	}

	public String getNamaKotamaops() {
		return namaKotamaops;
	}

	public void setNamaKotamaops(String namaKotamaops) {
		this.namaKotamaops = namaKotamaops;
	}

	public Kotamaops getKotamaops() {
		return kotamaops;
	}

	public void setKotamaops(Kotamaops kotamaops) {
		this.kotamaops = kotamaops;
	}
	
}
