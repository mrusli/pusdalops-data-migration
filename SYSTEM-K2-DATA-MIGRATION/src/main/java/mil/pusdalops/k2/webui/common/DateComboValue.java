package mil.pusdalops.k2.webui.common;

import java.io.Serializable;
import java.time.LocalDate;

public class DateComboValue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9056126349841361819L;

	String dbValue;
	String displayValue;
	private LocalDate dateValue;

	public DateComboValue(LocalDate dateValue) {
		super();
		this.setDateValue(dateValue);
	}	
	
	public DateComboValue(String dbValue, String displayValue) {
		super();
		this.dbValue = dbValue;
		this.displayValue = displayValue;
	}

	public DateComboValue(String dbValue, String displayValue, LocalDate dateValue) {
		super();
		this.dbValue = dbValue;
		this.displayValue = displayValue;
		this.dateValue = dateValue;
	}
	
	public String getDbValue() {
		return dbValue;
	}
	
	public String getDisplayValue() {
		return displayValue;
	}

	/**
	 * @return the dateValue
	 */
	public LocalDate getDateValue() {
		return dateValue;
	}

	/**
	 * @param dateValue the dateValue to set
	 */
	public void setDateValue(LocalDate dateValue) {
		this.dateValue = dateValue;
	}
}
