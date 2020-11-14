package mil.pusdalops.k2.webui.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import mil.pusdalops.domain.authorization.User;
import mil.pusdalops.domain.authorization.UserRole;
import mil.pusdalops.k2.persistence.user.dao.UserDao;
import mil.pusdalops.k2.webui.common.GFCBaseController;

public class UserSecurityControl extends GFCBaseController implements UserDetailsService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4768959621388211535L;

	private UserDao userDao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User dbUser = getUserDao().findUserByUsername(username);

		UserSecurityDetails userDetails = null;
		if (dbUser != null) {
			
			userDetails = getNewUserDetails(dbUser);
			
		} else {
			throw new UsernameNotFoundException("USER TIDAK DITEMUKAN");
		}

		return userDetails;
	}

	private UserSecurityDetails getNewUserDetails(User dbUser) {
		// equiv to '123'
		// User dbUser = new User("james", "$2a$10$ueHJqjoiHpmyWoBhex.GouF1/HqqMzMqAHJsGcEMv1DNSj6bJNSMG");
		
		// User dbUser = //getUserDao().findUserByUsername(userName);
		//		getUserDao().findUserByUsername(username);
			
		UserSecurityDetails userSecurityDetails =
			// new UserSecurityDetails(dbUser.getUser_name(), dbUser.getUser_password(), getGrantedAuthorities(dbUser), dbUser);
			new UserSecurityDetails(dbUser.getUserName(), dbUser.getUserPassword(), getGrantedAuthorities(dbUser), dbUser);
		
		return userSecurityDetails;

	}

	private Collection<GrantedAuthority> getGrantedAuthorities(User dbUser) {
		List<GrantedAuthority> rolesGrantedAuthorityList = new ArrayList<GrantedAuthority>();
		
		for (UserRole userRole : dbUser.getUserRoles()) {
			rolesGrantedAuthorityList.add(new SimpleGrantedAuthority(userRole.getRole_name()));			
		}
		
		return rolesGrantedAuthorityList;

	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}
