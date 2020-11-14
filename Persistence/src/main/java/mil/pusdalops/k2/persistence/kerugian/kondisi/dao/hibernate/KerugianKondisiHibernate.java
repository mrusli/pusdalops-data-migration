package mil.pusdalops.k2.persistence.kerugian.kondisi.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import mil.pusdalops.domain.kerugian.KerugianKondisi;
import mil.pusdalops.k2.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.k2.persistence.kerugian.kondisi.dao.KerugianKondisiDao;

public class KerugianKondisiHibernate extends DaoHibernate implements KerugianKondisiDao {

	@Override
	public KerugianKondisi findKerugianKondisiById(long id) throws Exception {
		
		return (KerugianKondisi) super.findById(KerugianKondisi.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<KerugianKondisi> findAllKerugianKondisi() throws Exception {

		return super.findAll(KerugianKondisi.class);
	}

	@Override
	public Long save(KerugianKondisi kerugianKondisi) throws Exception {

		return super.save(kerugianKondisi);
	}

	@Override
	public void update(KerugianKondisi kerugianKondisi) throws Exception {

		super.update(kerugianKondisi);
	}

	@Override
	public KerugianKondisi findKerugianKondisiByName(String sqlKondisi) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(KerugianKondisi.class);
		
		try {
			
			return (KerugianKondisi) criteria.add(Restrictions.eq("namaKondisi", sqlKondisi)).uniqueResult();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}
}
