package mil.pusdalops.k2.webui.migrasi;

import mil.pusdalops.domain.settings.Settings;
import mil.pusdalops.k2.domain.sql.Kerugian;
import mil.pusdalops.k2.domain.sql.Tkp;

public class KejadianKerugianData {

	private Tkp sqlTkp;
	
	private Kerugian sqlKerugian;
	
	private Settings currentSettings;

	public Kerugian getSqlKerugian() {
		return sqlKerugian;
	}

	public void setSqlKerugian(Kerugian sqlKerugian) {
		this.sqlKerugian = sqlKerugian;
	}

	public Settings getCurrentSettings() {
		return currentSettings;
	}

	public void setCurrentSettings(Settings currentSettings) {
		this.currentSettings = currentSettings;
	}

	public Tkp getSqlTkp() {
		return sqlTkp;
	}

	public void setSqlTkp(Tkp sqlTkp) {
		this.sqlTkp = sqlTkp;
	}
	
}
