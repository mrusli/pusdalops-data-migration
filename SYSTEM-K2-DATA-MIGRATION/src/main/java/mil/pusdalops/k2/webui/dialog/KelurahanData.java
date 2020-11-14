package mil.pusdalops.k2.webui.dialog;

import java.util.List;

import mil.pusdalops.domain.wilayah.Kelurahan;

public class KelurahanData {

	private List<Kelurahan> kelurahanList;
	
	private String tkpKelurahan;
	
	private String kejadianKecamatanName;

	public List<Kelurahan> getKelurahanList() {
		return kelurahanList;
	}

	public void setKelurahanList(List<Kelurahan> kelurahanList) {
		this.kelurahanList = kelurahanList;
	}

	public String getTkpKelurahan() {
		return tkpKelurahan;
	}

	public void setTkpKelurahan(String tkpKelurahan) {
		this.tkpKelurahan = tkpKelurahan;
	}

	public String getKejadianKecamatanName() {
		return kejadianKecamatanName;
	}

	public void setKejadianKecamatanName(String kejadianKecamatanName) {
		this.kejadianKecamatanName = kejadianKecamatanName;
	}
	
}
