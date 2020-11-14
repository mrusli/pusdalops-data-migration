package mil.pusdalops.k2.domain;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class App_Cloud_MySQL {

	@SuppressWarnings("unused")
	private static SessionFactory sessionAnnotationFactory;
	
	public static void main(String[] args) {
		System.out.println("Hello World : connection to cloud mysql...");

		sessionAnnotationFactory = new Configuration().configure("hibernate-cloud.cfg.xml").buildSessionFactory();
		
		
	}

}
