package mil.pusdalops.k2.persistence.kerugian.jenis.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import mil.pusdalops.domain.kerugian.KerugianJenis;
import mil.pusdalops.k2.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.k2.persistence.kerugian.jenis.dao.KerugianJenisDao;

public class KerugianJenisHibernate extends DaoHibernate implements KerugianJenisDao {

	@Override
	public KerugianJenis findKerugianJenisById(long id) throws Exception {

		return (KerugianJenis) super.findById(KerugianJenis.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<KerugianJenis> findAllKerugianJenis() throws Exception {
		
		return super.findAll(KerugianJenis.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<KerugianJenis> findAllKerugianJenisByOrder(boolean asc) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(KerugianJenis.class);
		criteria.addOrder(asc ? Order.asc("namaJenis") : Order.desc("namaJenis"));
		
		try {
			
			return criteria.list();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}

	}
	
	@Override
	public Long save(KerugianJenis kerugianJenis) throws Exception {
		
		return super.save(kerugianJenis);
	}

	@Override
	public void update(KerugianJenis kerugianJenis) throws Exception {

		super.update(kerugianJenis);
	}

	@Override
	public KerugianJenis findKerugianJenisByName(String sqlJenis) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(KerugianJenis.class);
		
		try {
			
			return (KerugianJenis) criteria.add(Restrictions.eq("namaJenis", sqlJenis)).uniqueResult();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

}
