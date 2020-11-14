package mil.pusdalops.k2.webui.dialog;

import java.util.List;

import mil.pusdalops.domain.kejadian.KejadianJenis;

public class KejadianJenisData {

	private List<KejadianJenis> kejadianJenisList;
	
	private String tkpKejadianJenis;

	public List<KejadianJenis> getKejadianJenisList() {
		return kejadianJenisList;
	}

	public void setKejadianJenisList(List<KejadianJenis> kejadianJenisList) {
		this.kejadianJenisList = kejadianJenisList;
	}

	public String getTkpKejadianJenis() {
		return tkpKejadianJenis;
	}

	public void setTkpKejadianJenis(String tkpKejadianJenis) {
		this.tkpKejadianJenis = tkpKejadianJenis;
	}
	
}
