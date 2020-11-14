package mil.pusdalops.k2.persistence.kejadian.jenis.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import mil.pusdalops.domain.kejadian.KejadianJenis;
import mil.pusdalops.k2.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.k2.persistence.kejadian.jenis.dao.KejadianJenisDao;

public class KejadianJenisHibernate extends DaoHibernate implements KejadianJenisDao {

	@Override
	public KejadianJenis findKejadianJenisById(long id) throws Exception {

		return (KejadianJenis) super.findById(KejadianJenis.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<KejadianJenis> findAllKejadianJenis() throws Exception {

		return super.findAll(KejadianJenis.class);
	}

	@Override
	public Long save(KejadianJenis kejadianJenis) throws Exception {
	
		return super.save(kejadianJenis);
	}

	@Override
	public void update(KejadianJenis kejadianJenis) throws Exception {

		super.update(kejadianJenis);
	}

	@Override
	public KejadianJenis findKejadianJenisByName(String kejadianJenisName) throws Exception {
		Session session = getSessionFactory().openSession();

		Criteria criteria = session.createCriteria(KejadianJenis.class);
		
		try {
			
			return (KejadianJenis) criteria.add(Restrictions.eq("namaJenis", kejadianJenisName)).uniqueResult();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}		
	}

}
