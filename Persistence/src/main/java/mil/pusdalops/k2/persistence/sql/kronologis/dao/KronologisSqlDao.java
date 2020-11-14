package mil.pusdalops.k2.persistence.sql.kronologis.dao;

import java.util.List;

import mil.pusdalops.k2.domain.sql.Kronologis;

public interface KronologisSqlDao {

	/**
	 * @param id
	 * @return Kronologis
	 * @throws Exception
	 */
	public Kronologis findKronologisById(long id) throws Exception;
	
	/**
	 * @return List<Kronologis>
	 * @throws Exception
	 */
	public List<Kronologis> findAllKronologis() throws Exception;
	
	/**
	 * @param kronologis
	 * @return Long
	 * @throws Exception
	 */
	public Long save(Kronologis kronologis) throws Exception;
	
	/**
	 * @param kronologis
	 * @throws Exception
	 */
	public void update(Kronologis kronologis) throws Exception;

	/**
	 * @param idStr
	 * @throws Exception
	 */
	public Kronologis findKronologisByIdStr(String idStr) throws Exception;
}
