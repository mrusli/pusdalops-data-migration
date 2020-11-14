package mil.pusdalops.k2.persistence.kejadian.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.k2.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.k2.persistence.kejadian.dao.KejadianDao;

public class KejadianHibernate extends DaoHibernate implements KejadianDao {

	@Override
	public Kejadian findKejadianById(long id) throws Exception {

		return (Kejadian) super.findById(Kejadian.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Kejadian> findAllKejadian(boolean desc) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kejadian.class);
		criteria.addOrder(desc ? 
				Order.desc("twPembuatanDateTime") : 
				Order.asc("twPembuatanDateTime"));
		
		try {
			
			return criteria.list();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public Long save(Kejadian kejadian) throws Exception {
		
		return super.save(kejadian);
	}

	@Override
	public void update(Kejadian kejadian) throws Exception {

		super.update(kejadian);
	}

	@Override
	public Kejadian findKejadianKotamaopsByProxy(long id) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kejadian.class);
		Kejadian kejadian = (Kejadian) criteria.add(Restrictions.idEq(id)).uniqueResult();
		
		try {
			Hibernate.initialize(kejadian.getKotamaops());
			
			return kejadian;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public Kejadian findKejadianKerugiansByProxy(long id) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kejadian.class);
		Kejadian kejadian = (Kejadian) criteria.add(Restrictions.idEq(id)).uniqueResult();
		
		try {
			Hibernate.initialize(kejadian.getKerugians());
			
			return kejadian;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Kejadian> findAllKejadianByKotamaops(Kotamaops kotamaops, boolean desc) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kejadian.class);
		criteria.add(Restrictions.eq("kotamaops", kotamaops));
		criteria.addOrder(desc ? 
				Order.desc("twPembuatanDateTime") :
				Order.asc("twPembuatanDateTime"));
		
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
	public List<Kejadian> findNewKejadianByDate(Date currentDate, Date lastSynchDate) throws Exception {
		Session session = getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(Kejadian.class);
		// criteria.add(Restrictions.between("createdAt", lastSynchDate, currentDate));
		criteria.add(Restrictions.ge("editedAt", lastSynchDate));
		
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
	public List<Kejadian> findAllKejadianNotSynch() throws Exception {
		Session session = getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(Kejadian.class);
		criteria.add(Restrictions.isNull("pusdalopsSynchAt"));
		
		try {
			return criteria.list();
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public Kejadian findKejadianPropinsiByProxy(long id) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kejadian.class);
		Kejadian kejadian = (Kejadian) criteria.add(Restrictions.idEq(id)).uniqueResult();
		
		try {
			Hibernate.initialize(kejadian.getPropinsi());
			
			return kejadian;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public Kejadian findKejadianKabupatenKotamadyaByProxy(long id) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kejadian.class);
		Kejadian kejadian = (Kejadian) criteria.add(Restrictions.idEq(id)).uniqueResult();
		
		try {
			Hibernate.initialize(kejadian.getKabupatenKotamadya());
			
			return kejadian;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public Kejadian findKejadianKecamatanByProxy(long id) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kejadian.class);
		Kejadian kejadian = (Kejadian) criteria.add(Restrictions.idEq(id)).uniqueResult();
		
		try {
			Hibernate.initialize(kejadian.getKecamatan());
			
			return kejadian;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public Kejadian findKejadianKelurahanByProxy(long id) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kejadian.class);
		Kejadian kejadian = (Kejadian) criteria.add(Restrictions.idEq(id)).uniqueResult();
		
		try {
			Hibernate.initialize(kejadian.getKelurahan());
			
			return kejadian;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

}
