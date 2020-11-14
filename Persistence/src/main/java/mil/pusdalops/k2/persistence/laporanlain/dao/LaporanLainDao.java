package mil.pusdalops.k2.persistence.laporanlain.dao;

import java.util.List;

import mil.pusdalops.domain.laporanlain.LaporanLain;

public interface LaporanLainDao {

	/**
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public LaporanLain findLaporanLainById(long id) throws Exception;
	
	/**
	 * @return
	 * @throws Exception
	 */
	public List<LaporanLain> findAllLaporanLain() throws Exception;
	
	/**
	 * @param laporanLain
	 * @return
	 * @throws Exception
	 */
	public Long save(LaporanLain laporanLain) throws Exception;
	
	/**
	 * @param laporanLain
	 * @throws Exception
	 */
	public void update(LaporanLain laporanLain) throws Exception;
	
}
