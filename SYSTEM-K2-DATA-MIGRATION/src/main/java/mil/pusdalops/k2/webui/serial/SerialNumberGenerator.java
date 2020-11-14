package mil.pusdalops.k2.webui.serial;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import mil.pusdalops.domain.serial.DocumentSerialNumber;
import mil.pusdalops.k2.persistence.serial.dao.DocumentSerialNumberDao;
import mil.pusdalops.k2.webui.common.GFCBaseController;

public class SerialNumberGenerator extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2815812830337958761L;

	private DocumentSerialNumberDao documentSerialNumberDao;

	public int getSerialNumber(String documentCode, ZoneId zoneId, LocalDateTime currentDate) throws Exception {
		int serialNum = 1;
		DocumentSerialNumber documentSerNum = null;
		
		if (documentCode==null) {
			documentSerNum =
					getDocumentSerialNumberDao().findLastDocumentSerialNumber();
		} else {
			documentSerNum =
					getDocumentSerialNumberDao().findLastDocumentSerialNumberByDocumentType(documentCode);			
		}

		if (documentSerNum!=null) {
			Date lastDate = documentSerNum.getSerialDate();

			// compare year
			int lastYearValue = asLocalDateTime(lastDate).getYear();
			int currYearValue = currentDate.getYear();
			// compare month
			int lastMonthValue = asLocalDateTime(lastDate).getMonthValue(); 
			int currMonthValue = currentDate.getMonthValue();

			if (lastYearValue==currYearValue) {

				if (lastMonthValue==currMonthValue) {

					serialNum = documentSerNum.getSerialNo()+1;
					
				}
			}
		}
		
		return serialNum;
	}
	
	public DocumentSerialNumberDao getDocumentSerialNumberDao() {
		return documentSerialNumberDao;
	}

	public void setDocumentSerialNumberDao(DocumentSerialNumberDao documentSerialNumberDao) {
		this.documentSerialNumberDao = documentSerialNumberDao;
	}

}
