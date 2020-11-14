package mil.pusdalops.k2.persistence;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.k2.persistence.kejadian.dao.KejadianDao;

public class App_Dao {

	private static ApplicationContext ctx;

	public static void main(String[] args) throws Exception {
		System.out.println("Hello World...");
		
		ctx = new ClassPathXmlApplicationContext("CommonContext-Dao.xml");

        KejadianDao kejadianDao = (KejadianDao) ctx.getBean("kejadianDao");
        
        List<Kejadian> kejadianList = kejadianDao.findAllKejadian(true);
        
        for (Kejadian kejadian : kejadianList) {
			System.out.println(kejadian.toString());
		}

	}

}
