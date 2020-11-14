package mil.pusdalops.k2.domain;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import mil.pusdalops.k2.domain.sql.Alutsista02;

public class App_SQLServer_Table {

	public static void main(String[] args) throws Exception {
		System.out.println("Hello World... Alutsista!!!");

		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
		
		Metadata metadata = new MetadataSources(serviceRegistry).getMetadataBuilder().build();
		
		SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
		
		try {
			Session session = sessionFactory.openSession();
			
			Alutsista02 alutsista02 = session.get(Alutsista02.class, 2);
			
			System.out.println(alutsista02.getKode()+" | "+alutsista02.getThn());
			
		} catch (Exception e) {
			throw e;
		} finally {
			sessionFactory.close();
		}
	}

}
