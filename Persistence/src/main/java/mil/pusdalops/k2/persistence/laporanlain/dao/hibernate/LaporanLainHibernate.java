package mil.pusdalops.k2.persistence.laporanlain.dao.hibernate;

import java.util.List;

import mil.pusdalops.domain.laporanlain.LaporanLain;
import mil.pusdalops.k2.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.k2.persistence.laporanlain.dao.LaporanLainDao;

public class LaporanLainHibernate extends DaoHibernate implements LaporanLainDao {

	@Override
	public LaporanLain findLaporanLainById(long id) throws Exception {
		
		return (LaporanLain) super.findById(LaporanLain.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LaporanLain> findAllLaporanLain() throws Exception {

		return super.findAll(LaporanLain.class);
	}

	@Override
	public Long save(LaporanLain laporanLain) throws Exception {

		return super.save(laporanLain);
	}

	@Override
	public void update(LaporanLain laporanLain) throws Exception {

		super.update(laporanLain);
	}

}
