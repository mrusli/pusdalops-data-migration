package mil.pusdalops.k2.persistence.kejadian.pelaku.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import mil.pusdalops.domain.kejadian.KejadianPelaku;
import mil.pusdalops.k2.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.k2.persistence.kejadian.pelaku.dao.KejadianPelakuDao;

public class KejadianPelakuHibernate extends DaoHibernate implements KejadianPelakuDao {

	@Override
	public KejadianPelaku findKejadianPelakuById(long id) throws Exception {

		return (KejadianPelaku) super.findById(KejadianPelaku.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<KejadianPelaku> findAllKejadianPelaku() throws Exception {

		return super.findAll(KejadianPelaku.class);
	}

	@Override
	public Long save(KejadianPelaku kejadianPelaku) throws Exception {
		
		return super.save(kejadianPelaku);
	}

	@Override
	public void update(KejadianPelaku kejadianPelaku) throws Exception {

		super.update(kejadianPelaku);
	}

	@Override
	public KejadianPelaku findKejadianPelakuByName(String kejadianPelakuName) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(KejadianPelaku.class);
		
		try {
			
			return (KejadianPelaku) criteria.add(Restrictions.eq("namaPelaku", kejadianPelakuName)).uniqueResult();

		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

}
