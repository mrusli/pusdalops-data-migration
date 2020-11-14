package mil.pusdalops.k2.webui.dialog;

import java.util.List;

import mil.pusdalops.domain.wilayah.Kabupaten_Kotamadya;

public class KabupatenData {

	private List<Kabupaten_Kotamadya> kabupatenKotList;
	
	private String tkpKabupaten;
	
	private String kejadianPropinsiName;

	public List<Kabupaten_Kotamadya> getKabupatenKotList() {
		return kabupatenKotList;
	}

	public void setKabupatenKotList(List<Kabupaten_Kotamadya> kabupatenKotList) {
		this.kabupatenKotList = kabupatenKotList;
	}

	public String getTkpKabupaten() {
		return tkpKabupaten;
	}

	public void setTkpKabupaten(String tkpKabupaten) {
		this.tkpKabupaten = tkpKabupaten;
	}

	public String getKejadianPropinsiName() {
		return kejadianPropinsiName;
	}

	public void setKejadianPropinsiName(String kejadianPropinsiName) {
		this.kejadianPropinsiName = kejadianPropinsiName;
	}
}
