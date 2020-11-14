package mil.pusdalops.k2.webui.dialog;

import mil.pusdalops.domain.kerugian.TipeKerugian;

public class KerugianJenisWithTipeData {

	private String sqlKerugianJenis;

	private TipeKerugian tipeKerugian;

	public String getSqlKerugianJenis() {
		return sqlKerugianJenis;
	}

	public void setSqlKerugianJenis(String sqlKerugianJenis) {
		this.sqlKerugianJenis = sqlKerugianJenis;
	}

	public TipeKerugian getTipeKerugian() {
		return tipeKerugian;
	}

	public void setTipeKerugian(TipeKerugian tipeKerugian) {
		this.tipeKerugian = tipeKerugian;
	}
}
