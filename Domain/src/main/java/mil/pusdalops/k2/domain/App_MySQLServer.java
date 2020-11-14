package mil.pusdalops.k2.domain;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;

import mil.pusdalops.k2.domain.sql.Kronologis;
import mil.pusdalops.k2.domain.sql.Tkp;

public class App_MySQLServer {

	public static void main(String[] args) {				
		System.out.println("Hello World : connection to local mysql test schema...");
		
		ServiceRegistry localServiceReg = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
		Metadata localMd = new MetadataSources(localServiceReg).getMetadataBuilder().build();		
		SessionFactory localSF = localMd.getSessionFactoryBuilder().build();
		
		try {
			Session localSes = localSF.openSession();
			
			Tkp tkp = localSes.get(Tkp.class, 3L);
			
			System.out.println(tkp.toString());
			
			Kronologis kronologis = localSes.get(Kronologis.class, 7L);
			
			System.out.println(kronologis.toString());
			
			Criteria criteria = localSes.createCriteria(Tkp.class);
			criteria.add(Restrictions.eq("id_str", "200512271405199"));
			
			System.out.println(criteria.list());
			
		} catch (Exception e) {
			throw e;
		} finally {
			localSF.close();
		}

	}

}
