package mil.pusdalops.k2.persistence.kerugian.satuan.dao;

import java.util.List;

import mil.pusdalops.domain.kerugian.KerugianSatuan;

public interface KerugianSatuanDao {

	/**
	 * Find by id
	 * 
	 * @param id
	 * @return KerugianSatuan
	 * @throws Exception
	 */
	public KerugianSatuan findKerugianSatuanById(long id) throws Exception;
	
	/**
	 * Find all
	 * 
	 * @return List<KerugianSatuan>
	 * @throws Exception
	 */
	public List<KerugianSatuan> findAllKerugianSatuan() throws Exception;
	
	/**
	 * @param asc
	 * @return List<KerugianSatuan>
	 * @throws Exception
	 */
	public List<KerugianSatuan> findAllKerugianSatuanByOrder(boolean asc) throws Exception;
	
	/**
	 * Save
	 * 
	 * @param kerugianSatuan
	 * @return Long
 	 * @throws Exception
	 */
	public Long save(KerugianSatuan kerugianSatuan) throws Exception;
	
	/**
	 * Update
	 * 
	 * @param kerugianSatuan
	 * @throws Exception
	 */
	public void update(KerugianSatuan kerugianSatuan) throws Exception;

	/**
	 * Find by name
	 * 
	 * @param sqlSatuan
	 * @return KerugianSatuan
	 * @throws Exception
	 */
	public KerugianSatuan findKerugianSatuanByName(String sqlSatuan) throws Exception;

	
}
