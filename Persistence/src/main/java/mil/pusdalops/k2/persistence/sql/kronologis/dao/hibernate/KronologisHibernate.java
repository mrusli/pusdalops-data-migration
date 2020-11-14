package mil.pusdalops.k2.persistence.sql.kronologis.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import mil.pusdalops.k2.domain.sql.Kronologis;
import mil.pusdalops.k2.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.k2.persistence.sql.kronologis.dao.KronologisSqlDao;

public class KronologisHibernate extends DaoHibernate implements KronologisSqlDao {

	@Override
	public Kronologis findKronologisById(long id) throws Exception {
		
		return (Kronologis) super.findById(Kronologis.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Kronologis> findAllKronologis() throws Exception {

		return super.findAll(Kronologis.class);
	}

	@Override
	public Long save(Kronologis kronologis) throws Exception {

		return super.save(kronologis);
	}

	@Override
	public void update(Kronologis kronologis) throws Exception {

		super.update(kronologis);
	}

	@Override
	public Kronologis findKronologisByIdStr(String idStr) throws Exception {
		Session session = getSessionFactory().openSession();

		Criteria criteria = session.createCriteria(Kronologis.class);
		
		try {
			
			return (Kronologis) criteria.add(Restrictions.eq("id_str", idStr)).uniqueResult(); 
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

}
