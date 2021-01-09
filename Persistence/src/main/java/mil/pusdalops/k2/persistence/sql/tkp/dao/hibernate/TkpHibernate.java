package mil.pusdalops.k2.persistence.sql.tkp.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import mil.pusdalops.k2.domain.sql.Tkp;
import mil.pusdalops.k2.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.k2.persistence.sql.tkp.dao.TkpSqlDao;

public class TkpHibernate extends DaoHibernate implements TkpSqlDao {

	@Override
	public Tkp findTkpById(long id) throws Exception {

		return (Tkp) super.findById(Tkp.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Tkp> findAllTkp() throws Exception {

		return super.findAll(Tkp.class);
	}

	@Override
	public Long save(Tkp tkp) throws Exception {

		return super.save(tkp);
	}

	@Override
	public void update(Tkp tkp) throws Exception {

		super.update(tkp);
	}

	@Override
	public Tkp findTkpKejadianByProxy(long id) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Tkp.class);
		Tkp tkp = (Tkp) criteria.add(Restrictions.idEq(id)).uniqueResult();
		
		try {
			Hibernate.initialize(tkp.getKejadian());
			
			return tkp;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Tkp> findAllTkpByStatus(boolean nonMigration) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Tkp.class);
		if (nonMigration) {
			criteria.add(Restrictions.isNull("transfferedAt"));			
		} else {
			criteria.add(Restrictions.isNotNull("transfferedAt"));			
		}
		
		try {
			
			return criteria.list();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> findDistinctTkpYear() throws Exception {
		Session session = getSessionFactory().openSession();

		Criteria criteria = session.createCriteria(Tkp.class);
		criteria.setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("thn"), "thn")));
		criteria.setResultTransformer(Transformers.aliasToBean(Tkp.class));
		
		List<String> distinctYearList = new ArrayList<String>();
		
		List<Tkp> tkpList = criteria.list();
		for (Tkp tkp : tkpList) {
			distinctYearList.add(tkp.getThn());
		}
		
		try {
			
			return distinctYearList;
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Tkp> findAllTkpByStatusYear(boolean nonMigration, String year) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Tkp.class);
		if (nonMigration) {
			criteria.add(Restrictions.isNull("transfferedAt"));			
		} else {
			criteria.add(Restrictions.isNotNull("transfferedAt"));			
		}
		criteria.add(Restrictions.eq("thn", year));
		
		try {
			
			return criteria.list();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
			
	}
	
}
