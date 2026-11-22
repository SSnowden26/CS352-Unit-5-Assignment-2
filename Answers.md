# Unit 5 Assignment 2 – Written Answers

## State of the project
All parts of the assignment are implemented. `Temperature` stores only a city, state, low and high temperature, plus a `Differential()` method that returns the positive difference between them. `WeatherReport` has a no-argument constructor that builds a 10-item test list, and a file constructor that reads `weather.txt` (16,631 records) into a `LinkedList<Temperature>`. `isSortedByCity()` and `isSortedByHigh()` return false on the freshly loaded data. `sortWithCollections(by)` and `sortWithMerge(by)` both sort correctly by "City" or "High", and each list passes its `isSorted` check after sorting. Everything compiles and runs with no known bugs.

## How I tested the program
I first used the no-argument constructor's small list. With only 10 items I could print it and check the city and high-temperature order by eye after each sort. Next I loaded four `WeatherReport` objects from `weather.txt` and confirmed that `isSortedByCity()` and `isSortedByHigh()` were false for all four before sorting. I sorted one by city with `Collections.sort()`, one by city with my merge sort, and did the same pair for high temperature. After each sort I called the matching `isSorted` method again to confirm it returned true. I timed each sort with `System.nanoTime()`.

## Timing data

| Sort method | Sort by | Time (ms) |
|---|---|---|
| Collections.sort() | City | 22.031 |
| Custom merge sort | City | 32.129 |
| Collections.sort() | High | 10.594 |
| Custom merge sort | High | 50.943 |

All four lists (16,631 records each) passed their `isSorted` check after sorting. `Collections.sort()` was faster in both cases: about 1.5x faster by city and about 5x faster by high temperature.

Writing a custom sort routine has a few benefits. You fully understand and control the algorithm, you can tailor it to the data structure (my merge sort splits and merges the linked lists directly), and it's a good way to learn how the algorithm actually works. The drawbacks are that it takes more time to write, it's easier to introduce bugs, and it is usually slower than the library version. `Collections.sort()` copies the list into an array and uses TimSort, which is heavily optimized and tested. My merge sort creates many small `LinkedList` objects as it recurses, and that adds overhead. For real projects the built-in sort is almost always the better choice unless there is a specific need it can't meet.

## What I learned
I learned how merge sort works on a linked list: split it in half, sort each half recursively, then merge the two sorted halves. I also learned how a `Comparator` lets the same sort code order objects by different fields.

## What I liked
I liked working with a real data file with thousands of records, because the timing comparison between my sort and the built-in one meant more with that much data.

## What was confusing / what I'd change
It was a little unclear how the four `WeatherReport` objects in `main` should be split between the sorts. I used two for sorting by city and two for sorting by high temperature.

## With another hour or two
I would run each sort several times and average the results for more reliable timing. I would also try an in-place merge sort that relinks nodes instead of creating new lists, to see how close it gets to `Collections.sort()`.
