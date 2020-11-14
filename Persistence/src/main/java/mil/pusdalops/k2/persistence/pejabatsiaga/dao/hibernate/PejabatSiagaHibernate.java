package mil.pusdalops.k2.persistence.pejabatsiaga.dao.hibernate;

import java.util.List;

import mil.pusdalops.domain.siaga.PejabatSiaga;
import mil.pusdalops.k2.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.k2.persistence.pejabatsiaga.dao.PejabatSiagaDao;

public class PejabatSiagaHibernate extends DaoHibernate implements PejabatSiagaDao {

	@Override
	public PejabatSiaga findPejabatSiagaById(long id) throws Exception {
		
		return (PejabatSiaga) super.findById(PejabatSiaga.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PejabatSiaga> findAllPejabatSiaga() throws Exception {

		return super.findAll(PejabatSiaga.class);
	}

	@Override
	public Long save(PejabatSiaga pejabatSiaga) throws Exception {

		return super.save(pejabatSiaga);
	}

	@Override
	public void update(PejabatSiaga pejabatSiaga) throws Exception {

		super.update(pejabatSiaga);
	}

}
