package mil.pusdalops.k2.persistence.sql.tkp.dao;

import java.util.List;

import mil.pusdalops.k2.domain.sql.Tkp;

public interface TkpSqlDao {

	/**
	 * @param id
	 * @return Tkp
	 * @throws Exception
	 */
	public Tkp findTkpById(long id) throws Exception;
	
	/**
	 * @return List<Tkp>
	 * @throws Exception
	 */
	public List<Tkp> findAllTkp() throws Exception;
	
	/**
	 * @param tkp
	 * @return Long
	 * @throws Exception
	 */
	public Long save(Tkp tkp) throws Exception;
	
	/**
	 * @param tkp
	 * @throws Exception
	 */
	public void update(Tkp tkp) throws Exception;


	/**
	 * Kejadian is declared lazy
	 * 
	 * @param id
	 * @return Tkp
	 * @throws Exception
	 */
	public Tkp findTkpKejadianByProxy(long id) throws Exception;

	public List<Tkp> findAllTkpByStatus(boolean nonMigration) throws Exception;

	public List<String> findDistinctTkpYear() throws Exception;

	public List<Tkp> findAllTkpByStatusYear(boolean nonMigration, String year) throws Exception;

	
}
