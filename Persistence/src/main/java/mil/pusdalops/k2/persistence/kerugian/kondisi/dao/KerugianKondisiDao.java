package mil.pusdalops.k2.persistence.kerugian.kondisi.dao;

import java.util.List;

import mil.pusdalops.domain.kerugian.KerugianKondisi;

public interface KerugianKondisiDao {

	/**
	 * @param id
	 * @return KerugianKondisi
	 * @throws Exception
	 */
	public KerugianKondisi findKerugianKondisiById(long id) throws Exception;
	
	/**
	 * @return List<KerugianKondisi>
	 * @throws Exception
	 */
	public List<KerugianKondisi> findAllKerugianKondisi() throws Exception;
	
	/**
	 * @param asc
	 * @return List<KerugianKondisi>
	 * @throws Exception
	 */
	public List<KerugianKondisi> findAllKerugianKondisiByOrder(boolean asc) throws Exception;
	
	/**
	 * @param kerugianKondisi
	 * @return Long
	 * @throws Exception
	 */
	public Long save(KerugianKondisi kerugianKondisi) throws Exception;
	
	/**
	 * @param kerugianKondisi
	 * @throws Exception
	 */
	public void update(KerugianKondisi kerugianKondisi) throws Exception;

	/**
	 * Find by name
	 * 
	 * @param sqlKondisi
	 * @return KerugianKondisi
	 * @throws Exception
	 */
	public KerugianKondisi findKerugianKondisiByName(String sqlKondisi) throws Exception;


}
