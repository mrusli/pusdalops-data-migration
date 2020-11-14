package mil.pusdalops.k2.persistence.sql.kerugian.dao;

import java.util.List;

import mil.pusdalops.k2.domain.sql.Kerugian;

public interface KerugianSqlDao {

	/**
	 * @param id
	 * @return Kerugian
	 * @throws Exception
	 */
	public Kerugian findKerugianSqlById(long id) throws Exception;
	
	/**
	 * @return List<Kerugian>
	 * @throws Exception
	 */
	public List<Kerugian> findAllKerugianSql() throws Exception;
	
	/**
	 * @param kerugian
	 * @return Long
	 * @throws Exception
	 */
	public Long save(Kerugian kerugian) throws Exception;
	
	/**
	 * @param kerugian
	 * @throws Exception
	 */
	public void update(Kerugian kerugian) throws Exception;

	/**
	 * Kerugian list by IdStr
	 * 
	 * @param tkpIdStr
	 * @return List<Kerugian>
	 * @throws Exception
	 */
	public List<Kerugian> findAllKerugianSqlByIdStr(String tkpIdStr) throws Exception;
	
}
