package mil.pusdalops.k2.persistence.gelaroperasi.dao.hibernate;

import java.util.List;

import mil.pusdalops.domain.gelarops.GelarOperasi;
import mil.pusdalops.k2.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.k2.persistence.gelaroperasi.dao.GelarOperasiDao;

public class GelarOperasiHibernate extends DaoHibernate implements GelarOperasiDao {

	@Override
	public GelarOperasi findGelarOperasiById(long id) throws Exception {

		return (GelarOperasi) super.findById(GelarOperasi.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GelarOperasi> findAllGelarOperasi() throws Exception {

		return super.findAll(GelarOperasi.class);
	}

	@Override
	public Long save(GelarOperasi gelarOperasi) throws Exception {

		return super.save(gelarOperasi);
	}

	@Override
	public void update(GelarOperasi gelarOperasi) throws Exception {

		super.update(gelarOperasi);
	}

}
