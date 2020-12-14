package mil.pusdalops.k2.persistence.kerugian.satuan.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import mil.pusdalops.domain.kerugian.KerugianSatuan;
import mil.pusdalops.k2.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.k2.persistence.kerugian.satuan.dao.KerugianSatuanDao;

public class KerugianSatuanHibernate extends DaoHibernate implements KerugianSatuanDao {

	@Override
	public KerugianSatuan findKerugianSatuanById(long id) throws Exception {
		
		return (KerugianSatuan) super.findById(KerugianSatuan.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<KerugianSatuan> findAllKerugianSatuan() throws Exception {

		return super.findAll(KerugianSatuan.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<KerugianSatuan> findAllKerugianSatuanByOrder(boolean asc) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(KerugianSatuan.class);
		criteria.addOrder(asc ? Order.asc("satuan") : Order.desc("satuan"));
		
		try {
			
			return criteria.list();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}		
	}
	
	@Override
	public Long save(KerugianSatuan kerugianSatuan) throws Exception {

		return super.save(kerugianSatuan);
	}

	@Override
	public void update(KerugianSatuan kerugianSatuan) throws Exception {

		super.update(kerugianSatuan);
	}

	@Override
	public KerugianSatuan findKerugianSatuanByName(String sqlSatuan) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(KerugianSatuan.class);
		
		try {
			
			return (KerugianSatuan) criteria.add(Restrictions.eq("satuan", sqlSatuan)).uniqueResult();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

}
