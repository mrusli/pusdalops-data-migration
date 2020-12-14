package mil.pusdalops.k2.persistence.kejadian.jenis.dao;

import java.util.List;

import mil.pusdalops.domain.kejadian.KejadianJenis;

public interface KejadianJenisDao {

	/**
	 * @param id
	 * @return KejadianJenis
	 * @throws Exception
	 */
	public KejadianJenis findKejadianJenisById(long id) throws Exception;

	/**
	 * @param asc
	 * @return List<KejadianJenis>
	 * @throws Exception
	 */
	public List<KejadianJenis> findAllKejadianJenisOrderBy(boolean asc) throws Exception;
	
	/**
	 * @return List<KejadianJenis>
	 * @throws Exception
	 */
	public List<KejadianJenis> findAllKejadianJenis() throws Exception;
	
	/**
	 * @param kejadianJenis
	 * @return Long
	 * @throws Exception
	 */
	public Long save(KejadianJenis kejadianJenis) throws Exception;
	
	/**
	 * @param kejadianJenis
	 * @throws Exception
	 */
	public void update(KejadianJenis kejadianJenis) throws Exception;

	/**
	 * Findy kejadianJenisName from the mysql table
	 * 
	 * @param kejadianJenisName
	 * @return KejadianJenis
	 * @throws Exception
	 */
	public KejadianJenis findKejadianJenisByName(String kejadianJenisName) throws Exception;

	
}
