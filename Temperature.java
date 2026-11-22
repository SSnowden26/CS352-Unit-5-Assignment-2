/**
 * @author John Cigas
 * Modified by Seth Snowden, 11/22/2026
 *
 * Stores low and high temperature data for a city/state
 */
public class Temperature {
	private String city;
	private String state;
	private int low;
	private int high;

	/**
	 * Creates a Temperature record for one city.
	 * @param city  name of the city
	 * @param state name of the state
	 * @param low   low temperature
	 * @param high  high temperature
	 */
	public Temperature(String city, String state, int low, int high) {
		this.city = city;
		this.state = state;
		this.low = low;
		this.high = high;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getLow() {
		return low;
	}

	public void setLow(int low) {
		this.low = low;
	}

	public int getHigh() {
		return high;
	}

	public void setHigh(int high) {
		this.high = high;
	}

	/**
	 * @return the positive difference between the high and low temperature
	 */
	public int Differential() {
		return Math.abs(high - low);
	}

	@Override
	public String toString() {
		return city + ", " + state + "  low: " + low + "  high: " + high
				+ "  diff: " + Differential();
	}
}
