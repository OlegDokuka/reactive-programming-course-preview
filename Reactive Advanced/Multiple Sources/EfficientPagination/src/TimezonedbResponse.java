import java.util.List;

public class TimezonedbResponse {

	public String status;
	public String message;
	public int totalPage;
	public int currentPage;
	public List<TimezonedbResponseZone> zones;

	public TimezonedbResponse() {
	}

	public TimezonedbResponse(String status, String message, int totalPage,
			int currentPage, List<TimezonedbResponseZone> zones) {
		super();
		this.status = status;
		this.message = message;
		this.totalPage = totalPage;
		this.currentPage = currentPage;
		this.zones = zones;
	}

}


