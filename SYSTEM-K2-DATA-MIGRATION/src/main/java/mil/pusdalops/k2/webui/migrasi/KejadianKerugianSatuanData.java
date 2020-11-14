package mil.pusdalops.k2.webui.migrasi;

import java.util.List;

import mil.pusdalops.domain.kerugian.KerugianSatuan;

public class KejadianKerugianSatuanData {

	private List<KerugianSatuan> kerugianSatuanList;
	
	private String sqlKerugianSatuan;

	public List<KerugianSatuan> getKerugianSatuanList() {
		return kerugianSatuanList;
	}

	public void setKerugianSatuanList(List<KerugianSatuan> kerugianSatuanList) {
		this.kerugianSatuanList = kerugianSatuanList;
	}

	public String getSqlKerugianSatuan() {
		return sqlKerugianSatuan;
	}

	public void setSqlKerugianSatuan(String sqlKerugianSatuan) {
		this.sqlKerugianSatuan = sqlKerugianSatuan;
	}
	
}
