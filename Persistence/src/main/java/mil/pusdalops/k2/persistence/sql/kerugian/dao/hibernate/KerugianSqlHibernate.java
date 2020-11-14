package mil.pusdalops.k2.persistence.sql.kerugian.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import mil.pusdalops.k2.domain.sql.Kerugian;
import mil.pusdalops.k2.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.k2.persistence.sql.kerugian.dao.KerugianSqlDao;

public class KerugianSqlHibernate extends DaoHibernate implements KerugianSqlDao {

	@Override
	public Kerugian findKerugianSqlById(long id) throws Exception {

		return (Kerugian) super.findById(Kerugian.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Kerugian> findAllKerugianSql() throws Exception {

		return super.findAll(Kerugian.class);
	}

	@Override
	public Long save(Kerugian kerugian) throws Exception {

		return super.save(kerugian);
	}

	@Override
	public void update(Kerugian kerugian) throws Exception {

		super.update(kerugian);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Kerugian> findAllKerugianSqlByIdStr(String tkpIdStr) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kerugian.class);
		criteria.add(Restrictions.eq("id_str", tkpIdStr));
		criteria.addOrder(Order.asc("pihak"));
		
		try {
			
			return criteria.list();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

}
