package mil.pusdalops.k2.webui.migrasi;

import java.util.List;

import mil.pusdalops.domain.kerugian.KerugianKondisi;

public class KejadianKerugianKondisiData {
	
	private List<KerugianKondisi> kerugianKondisiList;
	
	private String sqlKerugianKondisi;

	public List<KerugianKondisi> getKerugianKondisiList() {
		return kerugianKondisiList;
	}

	public void setKerugianKondisiList(List<KerugianKondisi> kerugianKondisiList) {
		this.kerugianKondisiList = kerugianKondisiList;
	}

	public String getSqlKerugianKondisi() {
		return sqlKerugianKondisi;
	}

	public void setSqlKerugianKondisi(String sqlKerugianKondisi) {
		this.sqlKerugianKondisi = sqlKerugianKondisi;
	}

}
