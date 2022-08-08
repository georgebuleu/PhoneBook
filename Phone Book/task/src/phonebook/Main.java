package phonebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;


public class Main {
    private static final String directoryPath = "C:/Users/georg/Downloads/directory.txt";
    private static final String findPath = "C:/Users/georg/Downloads/find.txt";


    private final static List<Contact> peopleContacts = new ArrayList<>();
    private final static List<String> peopleToFind = new ArrayList<>();
    private final static List<String> foundNumbers = new ArrayList<>();

    public static void main(String[] args) {

        loadPeopleContacts();  //Reading phone numbers from file
        loadPeopleToFind();   // Reading people to search from file

        // Linear Search
        long searchTime = printLinearSearch();

        // Bubble Sort + Jump search
        printBubbleAndJumpSearch(searchTime);

        //QuickSort + Binary Search
       printQuickSortAndBinarySearch();

       //print Hash table
        printHashTable();
    }

    private static void printHashTable(){
        System.out.println("\nStart searching (hash table)...");
        foundNumbers.clear();
        long counter = 0;
        long start = System.currentTimeMillis();
        Hashtable<String, String> people = new Hashtable<>();
        for (Contact person: peopleContacts) {
            people.put(person.getName(), person.getNumber());
        }
        long stop = System.currentTimeMillis();
        long creatingTime = stop - start;
        start = System.currentTimeMillis();
        for (String name:peopleToFind) {
            if(people.containsKey(name)){
                counter++;
            }
        }
        stop = System.currentTimeMillis();
        long searchingTime = stop - start;
        System.out.printf("Found %d / %d entries. Time taken: %s%n",
                counter, peopleToFind.size(), formatTime(searchingTime + creatingTime));
        System.out.printf("Creating time: %s%n",
                formatTime(creatingTime ));
        System.out.printf("Searching time: %s%n", formatTime(searchingTime));

    }
    private static void printQuickSortAndBinarySearch() {
        foundNumbers.clear();
        System.out.println("\nStart searching (quick sort + binary search)...");
        long start = System.currentTimeMillis();
        QuickSort.sort(peopleContacts, 0, peopleContacts.size() - 1);
        long stop = System.currentTimeMillis();
        long sortingTime = stop - start;
        start = System.currentTimeMillis();
        int counter;
        counter = search(new BinarySearch());
        stop = System.currentTimeMillis();
        long searchingTime = stop - start;
        long totalTime = searchingTime + sortingTime;

        System.out.printf("Found %d / %d entries. Time taken: %s%n",
                counter, peopleToFind.size(), formatTime(totalTime));
        System.out.printf("Sorting time: %s%n",
                formatTime(sortingTime));
        System.out.printf("Searching time: %s%n", formatTime(searchingTime));
    }

    private static long printLinearSearch(){
        System.out.println("Start searching (linear search)...");
        long start = System.currentTimeMillis();
        int counter = search(new LinearSearch());
        long stop = System.currentTimeMillis();
        long searchTime = stop - start;
        System.out.printf("Found %d / %d entries. Time taken: %s%n",
                counter, peopleToFind.size(), formatTime(searchTime));
        return searchTime;
    }

    private static void printBubbleAndJumpSearch(long searchTime){
        foundNumbers.clear();
        System.out.println("\nStart searching (bubble sort + jump search)...");
        long start = System.currentTimeMillis();
        long maxSortingTime = searchTime * 2;
        boolean isSorted = BubbleSort.sort(peopleContacts, maxSortingTime);
        long stop = System.currentTimeMillis();
        int counter;
        if (isSorted) {
            counter = search(new JumpSearch());
        } else {
            counter = search(new LinearSearch());
        }
        long stop2 = System.currentTimeMillis();
        long sortTime2 = stop - start;
        long searchTime2 = stop2 - stop;
        long totalTime2 = stop2 - start;
        System.out.printf("Found %d / %d entries. Time taken: %s%n",
                counter, peopleToFind.size(), formatTime(totalTime2));
        System.out.printf("Sorting time: %s%s%n",
                formatTime(sortTime2), isSorted ? "" : " - STOPPED, moved to linear search");
        System.out.printf("Searching time: %s%n", formatTime(searchTime2));
    }

    private static void loadPeopleContacts() {
        try (Scanner numbersReader = new Scanner(new File(directoryPath))) {
            while (numbersReader.hasNext()) {
                String number = "" + numbersReader.nextLong();
                String name = numbersReader.nextLine().trim();
                peopleContacts.add(new Contact(name, number));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Directory file not found.");
            System.exit(0);
        }
    }

    private static void loadPeopleToFind() {
        try (Scanner peopleReader = new Scanner(new File(findPath))) {
            while (peopleReader.hasNext()) {
                peopleToFind.add(peopleReader.nextLine().trim());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            System.exit(0);
        }
    }

    private static int search(Searchable searchAlgorithm) {
        int counter = 0;
        for (String person : peopleToFind) {
            int index = searchAlgorithm.search(peopleContacts, person);
            if (index != -1) {
                foundNumbers.add(peopleContacts.get(index).getNumber());
                counter++;
            }
        }
        return counter;
    }

    private static String formatTime(long timeGap) {
        long minutes = timeGap / (60 * 1000);
        timeGap %= (60 * 1000);
        long seconds = timeGap / 1000;
        long milliSeconds = timeGap % 1000;
        return String.format("%d min. %d sec. %d ms.", minutes, seconds, milliSeconds);
    }




    }

