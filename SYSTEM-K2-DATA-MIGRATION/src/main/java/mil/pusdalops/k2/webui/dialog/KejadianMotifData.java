package mil.pusdalops.k2.webui.dialog;

import java.util.List;

import mil.pusdalops.domain.kejadian.KejadianMotif;

public class KejadianMotifData {

	private List<KejadianMotif> kejadianMotifList;
	
	private String tkpKejadianMotif;

	public List<KejadianMotif> getKejadianMotifList() {
		return kejadianMotifList;
	}

	public void setKejadianMotifList(List<KejadianMotif> kejadianMotifList) {
		this.kejadianMotifList = kejadianMotifList;
	}

	public String getTkpKejadianMotif() {
		return tkpKejadianMotif;
	}

	public void setTkpKejadianMotif(String tkpKejadianMotif) {
		this.tkpKejadianMotif = tkpKejadianMotif;
	}
	
}
