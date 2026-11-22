import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * @author John Cigas
 * Put your name and date and description here.
 *
 * Stores a linked list of Temperature objects and sorts it by city or by
 * high temperature, using either Collections.sort() or a custom merge sort.
 */
public class WeatherReport {

	private LinkedList<Temperature> temps;

	/**
	 * Creates a small, unsorted list of Temperature objects for testing.
	 */
	public WeatherReport() {
		temps = new LinkedList<Temperature>();
		temps.add(new Temperature("Denver", "Colorado", 25, 48));
		temps.add(new Temperature("Austin", "Texas", 52, 74));
		temps.add(new Temperature("Seattle", "Washington", 40, 51));
		temps.add(new Temperature("Miami", "Florida", 68, 82));
		temps.add(new Temperature("Chicago", "Illinois", 22, 35));
		temps.add(new Temperature("Phoenix", "Arizona", 50, 71));
		temps.add(new Temperature("Boston", "Massachusetts", 28, 39));
		temps.add(new Temperature("Nashville", "Tennessee", 38, 55));
		temps.add(new Temperature("Anchorage", "Alaska", 5, 19));
		temps.add(new Temperature("Portland", "Oregon", 41, 52));
	}

	/**
	 * Creates a list of Temperature objects, one per data row in the file.
	 * @param fileName name of the comma-separated weather file
	 * @throws FileNotFoundException if the file can't be opened
	 */
	public WeatherReport(String fileName) throws FileNotFoundException {
		temps = new LinkedList<Temperature>();
		Scanner in = new Scanner(new File(fileName));
		if (in.hasNextLine()) {
			in.nextLine(); // skip header row
		}
		while (in.hasNextLine()) {
			String line = in.nextLine().trim();
			if (line.isEmpty()) {
				continue;
			}
			// Average,City,Code,Direction,Full,Maximum,Minimum,Month,
			// Precipitation,WindSpeed,State,Day,Year
			String[] parts = line.split(",");
			String city = parts[1];
			int high = Integer.parseInt(parts[5]);
			int low = Integer.parseInt(parts[6]);
			String state = parts[10];
			temps.add(new Temperature(city, state, low, high));
		}
		in.close();
	}

	/** @return number of Temperature objects in the list */
	public int size() {
		return temps.size();
	}

	/** @return true if the list is in ascending order by city */
	public boolean isSortedByCity() {
		return isSorted(new SortByCity());
	}

	/** @return true if the list is in ascending order by high temperature */
	public boolean isSortedByHigh() {
		return isSorted(new SortByHigh());
	}

	/*
	 * Walks the list once, checking every adjacent pair.
	 */
	private boolean isSorted(Comparator<Temperature> comp) {
		Iterator<Temperature> it = temps.iterator();
		if (!it.hasNext()) {
			return true;
		}
		Temperature prev = it.next();
		while (it.hasNext()) {
			Temperature curr = it.next();
			if (comp.compare(prev, curr) > 0) {
				return false;
			}
			prev = curr;
		}
		return true;
	}

	/*
	 * Turns "City" or "High" into the matching comparator.
	 */
	private Comparator<Temperature> comparatorFor(String by) {
		if (by.equalsIgnoreCase("City")) {
			return new SortByCity();
		} else if (by.equalsIgnoreCase("High")) {
			return new SortByHigh();
		}
		throw new IllegalArgumentException("Sort by must be \"City\" or \"High\", not \"" + by + "\"");
	}

	/**
	 * Sorts the list with Collections.sort().
	 * @param by "City" or "High"
	 */
	public void sortWithCollections(String by) {
		Collections.sort(temps, comparatorFor(by));
	}

	/**
	 * Sorts the list with a custom merge sort.
	 * @param by "City" or "High"
	 */
	public void sortWithMerge(String by) {
		temps = mergeSort(temps, comparatorFor(by));
	}

	/*
	 * Recursive merge sort: split the list in half, sort each half,
	 * then merge the two sorted halves.
	 */
	private LinkedList<Temperature> mergeSort(LinkedList<Temperature> list,
			Comparator<Temperature> comp) {
		if (list.size() <= 1) {
			return list;
		}

		LinkedList<Temperature> left = new LinkedList<Temperature>();
		LinkedList<Temperature> right = new LinkedList<Temperature>();
		int half = list.size() / 2;
		int count = 0;
		for (Temperature t : list) {
			if (count < half) {
				left.add(t);
			} else {
				right.add(t);
			}
			count++;
		}

		left = mergeSort(left, comp);
		right = mergeSort(right, comp);
		return merge(left, right, comp);
	}

	/*
	 * Merges two sorted lists into one sorted list. Taking from the left
	 * list on ties keeps the sort stable.
	 */
	private LinkedList<Temperature> merge(LinkedList<Temperature> left,
			LinkedList<Temperature> right, Comparator<Temperature> comp) {
		LinkedList<Temperature> result = new LinkedList<Temperature>();
		while (!left.isEmpty() && !right.isEmpty()) {
			if (comp.compare(left.getFirst(), right.getFirst()) <= 0) {
				result.add(left.removeFirst());
			} else {
				result.add(right.removeFirst());
			}
		}
		result.addAll(left);
		result.addAll(right);
		return result;
	}

	/** Prints every Temperature in the list. */
	public void print() {
		for (Temperature t : temps) {
			System.out.println(t);
		}
	}

	/*
	 * Times one sort and checks the result. Returns elapsed milliseconds.
	 */
	private static double timeSort(WeatherReport wr, String method, String by) {
		long start = System.nanoTime();
		if (method.equals("Collections")) {
			wr.sortWithCollections(by);
		} else {
			wr.sortWithMerge(by);
		}
		long end = System.nanoTime();
		double ms = (end - start) / 1_000_000.0;

		boolean sorted = by.equalsIgnoreCase("City") ? wr.isSortedByCity() : wr.isSortedByHigh();
		System.out.printf("  %-12s by %-5s  %10.3f ms   sorted afterward: %b%n",
				method, by, ms, sorted);
		return ms;
	}

	/**
	 * Tests the class: the small test list, then four reports built from
	 * the same file, each sorted a different way and timed.
	 */
	public static void main(String[] args) {
		System.out.println("=== Small test list (no-arg constructor) ===");
		WeatherReport small = new WeatherReport();
		System.out.println("isSortedByCity: " + small.isSortedByCity()
				+ "   isSortedByHigh: " + small.isSortedByHigh());
		small.sortWithMerge("City");
		System.out.println("After sortWithMerge(\"City\"):");
		small.print();
		System.out.println("isSortedByCity: " + small.isSortedByCity());
		small.sortWithMerge("High");
		System.out.println("After sortWithMerge(\"High\"):");
		small.print();
		System.out.println("isSortedByHigh: " + small.isSortedByHigh());
		System.out.println();

		String fileName = "weather.txt";
		try {
			WeatherReport cityCollections = new WeatherReport(fileName);
			WeatherReport cityMerge = new WeatherReport(fileName);
			WeatherReport highCollections = new WeatherReport(fileName);
			WeatherReport highMerge = new WeatherReport(fileName);

			System.out.println("=== " + fileName + ": " + cityCollections.size() + " records ===");
			System.out.println("Before sorting:");
			WeatherReport[] all = {cityCollections, cityMerge, highCollections, highMerge};
			for (int i = 0; i < all.length; i++) {
				System.out.println("  report " + (i + 1)
						+ "  isSortedByCity: " + all[i].isSortedByCity()
						+ "  isSortedByHigh: " + all[i].isSortedByHigh());
			}

			System.out.println("Sort times:");
			timeSort(cityCollections, "Collections", "City");
			timeSort(cityMerge, "Merge", "City");
			timeSort(highCollections, "Collections", "High");
			timeSort(highMerge, "Merge", "High");
		} catch (FileNotFoundException e) {
			System.out.println("Can't find file " + fileName);
		}
	}
}

/*
 * Comparator to sort by the city stored in a Temperature object
 */
class SortByCity implements Comparator<Temperature> {
    // Used for sorting in ascending order of City
    public int compare(Temperature t1, Temperature t2)
    {
        return t1.getCity().compareTo(t2.getCity());
    }
}

/*
 * Comparator to sort by the high temperature stored in a Temperature object
 */
class SortByHigh implements Comparator<Temperature> {
    // Used for sorting in ascending order of high temperature
    public int compare(Temperature t1, Temperature t2)
    {
        return t1.getHigh() - t2.getHigh();
    }
}
