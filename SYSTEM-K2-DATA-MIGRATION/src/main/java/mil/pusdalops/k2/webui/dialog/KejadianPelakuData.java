package mil.pusdalops.k2.webui.dialog;

import java.util.List;

import mil.pusdalops.domain.kejadian.KejadianPelaku;

public class KejadianPelakuData {

	private List<KejadianPelaku> kejadianPelakuList;
	
	private String tkpKejadianPelaku;

	public List<KejadianPelaku> getKejadianPelakuList() {
		return kejadianPelakuList;
	}

	public void setKejadianPelakuList(List<KejadianPelaku> kejadianPelakuList) {
		this.kejadianPelakuList = kejadianPelakuList;
	}

	public String getTkpKejadianPelaku() {
		return tkpKejadianPelaku;
	}

	public void setTkpKejadianPelaku(String tkpKejadianPelaku) {
		this.tkpKejadianPelaku = tkpKejadianPelaku;
	}
	
}
