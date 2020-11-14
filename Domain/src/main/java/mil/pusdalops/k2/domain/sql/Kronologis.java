package mil.pusdalops.k2.domain.sql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import mil.pusdalops.domain.common.SchemaUtil;

@Entity
@Table(name = "sql_kronologis", schema = SchemaUtil.SCHEMA_COMMON)
public class Kronologis {

	@Column(name = "id_str")
	private String id_str;
	
	@Column(name = "kronologis")
	private String kronologis;
	
	@Column(name = "sid")
	private String sid;

	@Column(name = "ssid")
	private String ssid;
	
	@Column(name = "id_kronologis")
	private String id_kronologis;
	
	@Column(name = "TimeStamp")
	private String timestamp;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", nullable=false, unique=true)
	private Long id = Long.MIN_VALUE;

	@Override
	public String toString() {
		return "Kronologis [id_str=" + id_str + ", kronologis=" + kronologis + ", sid=" + sid + ", ssid=" + ssid
				+ ", id_kronologis=" + id_kronologis + ", timestamp=" + timestamp + ", id=" + id + "]";
	}
	
	public String getId_str() {
		return id_str;
	}

	public void setId_str(String id_str) {
		this.id_str = id_str;
	}

	public String getKronologis() {
		return kronologis;
	}

	public void setKronologis(String kronologis) {
		this.kronologis = kronologis;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public String getId_kronologis() {
		return id_kronologis;
	}

	public void setId_kronologis(String id_kronologis) {
		this.id_kronologis = id_kronologis;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
