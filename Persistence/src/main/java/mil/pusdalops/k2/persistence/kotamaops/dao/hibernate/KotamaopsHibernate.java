package mil.pusdalops.k2.persistence.kotamaops.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.k2.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.k2.persistence.kotamaops.dao.KotamaopsDao;

public class KotamaopsHibernate extends DaoHibernate implements KotamaopsDao {

	@Override
	public Kotamaops findKotamaopsById(long id) throws Exception {

		return (Kotamaops) super.findById(Kotamaops.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Kotamaops> findAllKotamaops() throws Exception {

		return super.findAll(Kotamaops.class);
	}

	@Override
	public Long save(Kotamaops kotamaops) throws Exception {

		return super.save(kotamaops);
	}

	@Override
	public void update(Kotamaops kotamaops) throws Exception {

		super.update(kotamaops);
	}

	@Override
	public Kotamaops findKotamaopsPropinsiByProxy(long id) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kotamaops.class);
		Kotamaops kotamaops = (Kotamaops) criteria.add(Restrictions.idEq(id)).uniqueResult();
		
		try {
			Hibernate.initialize(kotamaops.getPropinsis());
			
			return kotamaops;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public Kotamaops findKotamaopsKotamaopsByProxy(long id) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kotamaops.class);
		Kotamaops kotamaops = (Kotamaops) criteria.add(Restrictions.idEq(id)).uniqueResult();
		
		try {
			Hibernate.initialize(kotamaops.getKotamaops());
			
			return kotamaops;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public void delete(Kotamaops kotamaops) throws Exception {

		super.delete(kotamaops);
	}

	@Override
	public Kotamaops findKotamaopsByName(String kotamaopsName) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kotamaops.class);
		
		try {
		
			return (Kotamaops) criteria.add(Restrictions.eq("kotamaopsName", kotamaopsName)).uniqueResult();

		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Kotamaops> findAllKotamaopsWithOrder(boolean asc) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kotamaops.class);
		criteria.addOrder(asc ? Order.asc("kotamaopsName") : Order.desc("kotamaopsName"));
		
		try {
			
			return criteria.list();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}
}
