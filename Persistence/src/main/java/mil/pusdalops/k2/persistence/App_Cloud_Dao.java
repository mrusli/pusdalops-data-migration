package mil.pusdalops.k2.persistence;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.k2.persistence.kejadian.dao.KejadianDao;

public class App_Cloud_Dao {

	private static ApplicationContext ctx;

	public static void main(String[] args) throws Exception {
        System.out.println( "Connecting to cloud database..." );

		ctx = new ClassPathXmlApplicationContext("CommonContext-Cloud-Dao.xml");
		
		KejadianDao kejadianDao = (KejadianDao) ctx.getBean("kejadianDao");

		Kejadian kejadian = kejadianDao.findKejadianById(5L);
		
		System.out.println(kejadian.toString());
		
	}

}
