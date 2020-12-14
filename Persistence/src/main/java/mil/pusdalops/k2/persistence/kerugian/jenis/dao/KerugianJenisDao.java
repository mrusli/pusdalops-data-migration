package mil.pusdalops.k2.persistence.kerugian.jenis.dao;

import java.util.List;

import mil.pusdalops.domain.kerugian.KerugianJenis;

public interface KerugianJenisDao {

	/**
	 * @param id
	 * @return KerugianJenis
	 * @throws Exception
	 */
	public KerugianJenis findKerugianJenisById(long id) throws Exception;
	
	/**
	 * @return List<KerugianJenis>
	 * @throws Exception
	 */
	public List<KerugianJenis> findAllKerugianJenis() throws Exception;
	
	/**
	 * @param asc
	 * @return List<KerugianJenis>
	 * @throws Exception
	 */
	public List<KerugianJenis> findAllKerugianJenisByOrder(boolean asc) throws Exception;
	
	/**
	 * @param kerugianJenis
	 * @return Long
	 * @throws Exception
	 */
	public Long save(KerugianJenis kerugianJenis) throws Exception;
	
	/**
	 * @param kerugianJenis
	 * @throws Exception
	 */
	public void update(KerugianJenis kerugianJenis) throws Exception;

	/**
	 * Find by name
	 * 
	 * @param sqlJenis
	 * @return KerugianJenis
	 * @throws Exception
	 */
	public KerugianJenis findKerugianJenisByName(String sqlJenis) throws Exception;

}
