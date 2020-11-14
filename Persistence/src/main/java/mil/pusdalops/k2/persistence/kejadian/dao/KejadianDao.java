package mil.pusdalops.k2.persistence.kejadian.dao;

import java.util.Date;
import java.util.List;

import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.domain.kotamaops.Kotamaops;

public interface KejadianDao {

	/**
	 * @param id
	 * @return Kejadian
	 * @throws Exception
	 */
	public Kejadian findKejadianById(long id) throws Exception;

	/**
	 * Find all kejadian as a list, with descending (true) or 
	 * ascending (false) order
	 * 
	 * @param desc - true - descending, false - ascending
	 * @return List<Kejadian>
	 * @throws Exception
	 */
	public List<Kejadian> findAllKejadian(boolean desc) throws Exception;
	
	/**
	 * @param kejadian
	 * @return Long
	 * @throws Exception
	 */
	public Long save(Kejadian kejadian) throws Exception;
	
	/**
	 * @param kejadian
	 * @throws Exception
	 */
	public void update(Kejadian kejadian) throws Exception;

	/**
	 * Kotamaops is fetched lazy
	 * 
	 * @param id
	 * @return Kejadian
	 * @throws Exception
	 */
	public Kejadian findKejadianKotamaopsByProxy(long id) throws Exception;

	/**
	 * Kerugians is fetched lazy
	 * 
	 * @param id
	 * @return Kejadian
	 * @throws Exception
	 */
	public Kejadian findKejadianKerugiansByProxy(long id) throws Exception;

	/**
	 * Find all kejadian as a list, with descending (true) or 
	 * ascending (false) order
	 *  
	 * @param kotamaops
	 * @param desc - true - descending, false - ascending
	 * @return List<Kejadian>
	 * @throws Exception
	 */
	public List<Kejadian> findAllKejadianByKotamaops(Kotamaops kotamaops, boolean desc) throws Exception;

	/**
	 * Find new kejadian to synchronize
	 * 
	 * @param lastSynchDate 
	 * @param asDate
	 * @return List<Kejadian>
	 * @throws Exception
	 */
	public List<Kejadian> findNewKejadianByDate(Date currentDate, Date lastSynchDate) throws Exception;

	/**
	 * Find kejadian NOT synch yet -- where pusdalops_synch_datetime is null
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Kejadian> findAllKejadianNotSynch() throws Exception;

	/**
	 * Propinsi is fetched lazy
	 * 
	 * @param id
	 * @return Kejadian
	 * @throws Exception
	 */
	public Kejadian findKejadianPropinsiByProxy(long id) throws Exception;
	
	/**
	 * Kabupaten/Kotamadya is fetched lazy
	 * 
	 * @param id
	 * @return Kejadian
	 * @throws Exception
	 */
	public Kejadian findKejadianKabupatenKotamadyaByProxy(long id) throws Exception;
	
	/**
	 * Kecamatan is fetched lazy
	 * 
	 * @param id
	 * @return Kejadian
	 * @throws Exception
	 */
	public Kejadian findKejadianKecamatanByProxy(long id) throws Exception;
	
	/**
	 * Kelurahan is fetched lazy
	 * 
	 * @param id
	 * @return Kejadian
	 * @throws Exception
	 */
	public Kejadian findKejadianKelurahanByProxy(long id) throws Exception;
	
}
