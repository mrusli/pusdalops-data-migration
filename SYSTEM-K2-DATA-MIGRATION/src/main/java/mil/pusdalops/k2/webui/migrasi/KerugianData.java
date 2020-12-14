package mil.pusdalops.k2.webui.migrasi;

import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.domain.kerugian.Kerugian;
import mil.pusdalops.domain.kerugian.Pihak;
import mil.pusdalops.domain.kotamaops.Kotamaops;

public class KerugianData {

	private Pihak paraPihak;
	
	private Kerugian kerugian;
	
	private Kotamaops kotamaops;
	
	private Kejadian kejadian;

	public Pihak getParaPihak() {
		return paraPihak;
	}

	public void setParaPihak(Pihak paraPihak) {
		this.paraPihak = paraPihak;
	}

	public Kerugian getKerugian() {
		return kerugian;
	}

	public void setKerugian(Kerugian kerugian) {
		this.kerugian = kerugian;
	}

	public Kotamaops getKotamaops() {
		return kotamaops;
	}

	public void setKotamaops(Kotamaops kotamaops) {
		this.kotamaops = kotamaops;
	}

	public Kejadian getKejadian() {
		return kejadian;
	}

	public void setKejadian(Kejadian kejadian) {
		this.kejadian = kejadian;
	}
	
}
