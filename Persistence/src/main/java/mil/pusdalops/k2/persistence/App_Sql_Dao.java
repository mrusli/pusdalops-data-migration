package mil.pusdalops.k2.persistence;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import mil.pusdalops.k2.domain.sql.Kerugian;
import mil.pusdalops.k2.domain.sql.Tkp;
import mil.pusdalops.k2.persistence.sql.kerugian.dao.KerugianSqlDao;
import mil.pusdalops.k2.persistence.sql.tkp.dao.TkpSqlDao;

/**
 * Hello world!
 *
 */
public class App_Sql_Dao {
	
	private static ApplicationContext ctx;
	
    public static void main( String[] args ) throws Exception {
        System.out.println( "Hello World!" );
        
		ctx = new ClassPathXmlApplicationContext("CommonContext-Sql-Dao.xml");

        TkpSqlDao tkpDao = (TkpSqlDao) ctx.getBean("tkpDao");
        
        List<Tkp> tkpList = tkpDao.findAllTkp();
        
        for (Tkp tkp : tkpList) {
			System.out.println(tkp.toString());
		}
        
        KerugianSqlDao kerugianSqlDao = (KerugianSqlDao) ctx.getBean("kerugianSqlDao");
        
        List<Kerugian> kerugianSqlList = kerugianSqlDao.findAllKerugianSql();
        
        for (Kerugian kerugian : kerugianSqlList) {
			System.out.println(kerugian.toString());
		}
    }
}
