package mil.pusdalops.k2.webui.dialog;

import java.util.List;

import mil.pusdalops.domain.wilayah.Kecamatan;

public class KecamatanData {

	private List<Kecamatan> kecamatanList;
	
	private String tkpKecamatan;
	
	private String kejadianKabupatenKotName;

	public List<Kecamatan> getKecamatanList() {
		return kecamatanList;
	}

	public void setKecamatanList(List<Kecamatan> kecamatanList) {
		this.kecamatanList = kecamatanList;
	}

	public String getTkpKecamatan() {
		return tkpKecamatan;
	}

	public void setTkpKecamatan(String tkpKecamatan) {
		this.tkpKecamatan = tkpKecamatan;
	}

	public String getKejadianKabupatenKotName() {
		return kejadianKabupatenKotName;
	}

	public void setKejadianKabupatenKotName(String kejadianKabupatenKotName) {
		this.kejadianKabupatenKotName = kejadianKabupatenKotName;
	}
	
}
