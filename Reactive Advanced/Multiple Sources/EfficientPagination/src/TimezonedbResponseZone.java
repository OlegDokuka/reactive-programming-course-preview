import java.util.Objects;

public class TimezonedbResponseZone {

	public String countryCode;

	public String countryName;

	public String regionName;

	public String cityName;

	public String zoneName;

	public String abbreviation;

	public long gmtOffset;

	public String dst;

	public long zoneStart;

	public long timestamp;

	public String formatted;

	public long zoneEnd;

	public String nextAbbreviation;

	public TimezonedbResponseZone() {
	}

	public TimezonedbResponseZone(String countryCode,
			String countryName,
			String regionName,
			String cityName,
			String zoneName,
			String abbreviation,
			long gmtOffset,
			String dst,
			long zoneStart,
			long timestamp,
			String formatted,
			long zoneEnd,
			String nextAbbreviation) {
		super();
		this.countryCode = countryCode;
		this.countryName = countryName;
		this.regionName = regionName;
		this.cityName = cityName;
		this.zoneName = zoneName;
		this.abbreviation = abbreviation;
		this.gmtOffset = gmtOffset;
		this.dst = dst;
		this.zoneStart = zoneStart;
		this.timestamp = timestamp;
		this.formatted = formatted;
		this.zoneEnd = zoneEnd;
		this.nextAbbreviation = nextAbbreviation;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		TimezonedbResponseZone zone = (TimezonedbResponseZone) o;
		return gmtOffset == zone.gmtOffset && zoneStart == zone.zoneStart && timestamp == zone.timestamp && zoneEnd == zone.zoneEnd && Objects.equals(
				countryCode,
				zone.countryCode) && Objects.equals(countryName,
				zone.countryName) && Objects.equals(regionName,
				zone.regionName) && Objects.equals(cityName,
				zone.cityName) && Objects.equals(zoneName,
				zone.zoneName) && Objects.equals(abbreviation,
				zone.abbreviation) && Objects.equals(dst, zone.dst) && Objects.equals(
				formatted,
				zone.formatted) && Objects.equals(nextAbbreviation,
				zone.nextAbbreviation);
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(countryCode);
		result = 31 * result + Objects.hashCode(countryName);
		result = 31 * result + Objects.hashCode(regionName);
		result = 31 * result + Objects.hashCode(cityName);
		result = 31 * result + Objects.hashCode(zoneName);
		result = 31 * result + Objects.hashCode(abbreviation);
		result = 31 * result + Long.hashCode(gmtOffset);
		result = 31 * result + Objects.hashCode(dst);
		result = 31 * result + Long.hashCode(zoneStart);
		result = 31 * result + Long.hashCode(timestamp);
		result = 31 * result + Objects.hashCode(formatted);
		result = 31 * result + Long.hashCode(zoneEnd);
		result = 31 * result + Objects.hashCode(nextAbbreviation);
		return result;
	}
}
