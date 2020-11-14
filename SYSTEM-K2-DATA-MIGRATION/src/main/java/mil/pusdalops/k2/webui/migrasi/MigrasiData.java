package mil.pusdalops.k2.webui.migrasi;

import mil.pusdalops.domain.settings.Settings;
import mil.pusdalops.k2.domain.sql.Tkp;

public class MigrasiData {

	private Tkp tkp;
	
	private Settings currentSettings;

	public Tkp getTkp() {
		return tkp;
	}

	public void setTkp(Tkp tkp) {
		this.tkp = tkp;
	}

	public Settings getCurrentSettings() {
		return currentSettings;
	}

	public void setCurrentSettings(Settings currentSettings) {
		this.currentSettings = currentSettings;
	}
	
}
