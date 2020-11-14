package mil.pusdalops.k2.webui.dialog;

import mil.pusdalops.domain.gmt.TimezoneInd;

public class KotamaopsData {
	
	private String namaKotamaops;
	
	private TimezoneInd timezoneInd;

	private String documentCode;
	
	public String getNamaKotamaops() {
		return namaKotamaops;
	}

	public void setNamaKotamaops(String namaKotamaops) {
		this.namaKotamaops = namaKotamaops;
	}

	public TimezoneInd getTimezoneInd() {
		return timezoneInd;
	}

	public void setTimezoneInd(TimezoneInd timezoneInd) {
		this.timezoneInd = timezoneInd;
	}

	public String getDocumentCode() {
		return documentCode;
	}

	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}
	
}
