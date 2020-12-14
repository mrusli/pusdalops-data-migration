package mil.pusdalops.k2.persistence.kejadian.motif.dao;

import java.util.List;

import mil.pusdalops.domain.kejadian.KejadianMotif;

public interface KejadianMotifDao {

	/**
	 * @param id
	 * @return KejadianMotif
	 * @throws Exception
	 */
	public KejadianMotif findKejadianMotifById(long id) throws Exception;
	
	/**
	 * @param asc
	 * @return List<KejadianMotif>
	 * @throws Exception
	 */
	public List<KejadianMotif> findAllKejadianMotifOrderBy(boolean asc) throws Exception;
	
	/**
	 * @return List<KejadianMotif>
	 * @throws Exception
	 */
	public List<KejadianMotif> findAllKejadianMotif() throws Exception;
	
	/**
	 * @param kejadianMotif
	 * @return Long
	 * @throws Exception
	 */
	public Long save(KejadianMotif kejadianMotif) throws Exception;
	
	/**
	 * @param kejadianMotif
	 * @throws Exception
	 */
	public void update(KejadianMotif kejadianMotif) throws Exception;

	/**
	 * Find kejadianMotifName in the mysql table
	 * 
	 * @param kejadianMotifName
	 * @return KejadianMotif
	 * @throws Exception
	 */
	public KejadianMotif findKejadianMotifByName(String kejadianMotifName) throws Exception;

	

}
