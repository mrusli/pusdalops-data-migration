package mil.pusdalops.k2.persistence.kotamaops.dao;

import java.util.List;

import mil.pusdalops.domain.kotamaops.Kotamaops;

public interface KotamaopsDao {

	/**
	 * @param id
	 * @return Kotamaops
	 * @throws Exception
	 */
	public Kotamaops findKotamaopsById(long id) throws Exception;
	
	/**
	 * @return List<Kotamaops>
	 * @throws Exception
	 */
	public List<Kotamaops> findAllKotamaops() throws Exception;
	
	/**
	 * @param kotamaops
	 * @return Long
	 * @throws Exception
	 */
	public Long save(Kotamaops kotamaops) throws Exception;
	
	/**
	 * @param kotamaops
	 * @throws Exception
	 */
	public void update(Kotamaops kotamaops) throws Exception;

	
	/**
	 * Propinsi is fetched lazy.
	 * 
	 * @param id
	 * @return Kotamaops
	 * @throws Exception
	 */
	public Kotamaops findKotamaopsPropinsiByProxy(long id) throws Exception;

	/**
	 * Kotamaops is fetched lazy
	 * 
	 * @param id
	 * @return Kotamaops
	 * @throws Exception
	 */
	public Kotamaops findKotamaopsKotamaopsByProxy(long id) throws Exception;

	/**
	 * 
	 * 
	 * @param kotamaops
	 * @throws Exception
	 */
	public void delete(Kotamaops kotamaops) throws Exception;

	/**
	 * @param kotamaopsName
	 * @return Kotamaops
	 * @throws Exception
	 */
	public Kotamaops findKotamaopsByName(String kotamaopsName) throws Exception;

	/**
	 * Find all with ascending or descdending order
	 * 
	 * @param asc - true - ascending, false - decending
	 * @return
	 * @throws Exception
	 */
	public List<Kotamaops> findAllKotamaopsWithOrder(boolean asc) throws Exception;
}
