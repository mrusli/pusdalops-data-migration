package mil.pusdalops.k2.webui.migrasi;

import java.util.List;

import mil.pusdalops.domain.kerugian.KerugianJenis;

public class KejadianKerugianJenisData {
	
	private List<KerugianJenis> kerugianJenisList;
	
	private String sqlKerugianJenis;

	public List<KerugianJenis> getKerugianJenisList() {
		return kerugianJenisList;
	}

	public void setKerugianJenisList(List<KerugianJenis> kerugianJenisList) {
		this.kerugianJenisList = kerugianJenisList;
	}

	public String getSqlKerugianJenis() {
		return sqlKerugianJenis;
	}

	public void setSqlKerugianJenis(String sqlKerugianJenis) {
		this.sqlKerugianJenis = sqlKerugianJenis;
	}
	
}
