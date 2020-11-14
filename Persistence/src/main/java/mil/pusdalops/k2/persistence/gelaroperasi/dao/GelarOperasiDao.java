package mil.pusdalops.k2.persistence.gelaroperasi.dao;

import java.util.List;

import mil.pusdalops.domain.gelarops.GelarOperasi;

public interface GelarOperasiDao {

	/**
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public GelarOperasi findGelarOperasiById(long id) throws Exception;
	
	/**
	 * @return
	 * @throws Exception
	 */
	public List<GelarOperasi> findAllGelarOperasi() throws Exception;
	
	/**
	 * @param gelarOperasi
	 * @return
	 * @throws Exception
	 */
	public Long save(GelarOperasi gelarOperasi) throws Exception;
	
	/**
	 * @param gelarOperasi
	 * @throws Exception
	 */
	public void update(GelarOperasi gelarOperasi) throws Exception;
	
}
