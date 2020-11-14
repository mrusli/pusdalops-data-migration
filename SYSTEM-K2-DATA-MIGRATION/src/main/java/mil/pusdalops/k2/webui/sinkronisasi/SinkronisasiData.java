package mil.pusdalops.k2.webui.sinkronisasi;

import java.time.LocalDateTime;

import mil.pusdalops.domain.kejadian.Kejadian;

public class SinkronisasiData {

	private Kejadian cloudKejadian;
	
	private LocalDateTime currentLocalDateTime;

	public Kejadian getCloudKejadian() {
		return cloudKejadian;
	}

	public void setCloudKejadian(Kejadian cloudKejadian) {
		this.cloudKejadian = cloudKejadian;
	}

	public LocalDateTime getCurrentLocalDateTime() {
		return currentLocalDateTime;
	}

	public void setCurrentLocalDateTime(LocalDateTime currentLocalDateTime) {
		this.currentLocalDateTime = currentLocalDateTime;
	}
	
}
