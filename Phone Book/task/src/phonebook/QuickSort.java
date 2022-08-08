package phonebook;

import java.util.List;

public class QuickSort {
    public static void sort(List<Contact> people, int left, int right) {
        if(left < right){
            int part = partition(people,left, right);
            sort(people, left, part - 1);
            sort(people, part + 1, right );
        }

    }

    private static int partition(List<Contact> people, int left, int right){
        Contact pivot = people.get(right);

        int i = left - 1;

        for(int j = left; j <=right - 1; j++){
            if(people.get(j).getName().compareTo(pivot.getName()) < 0) {
                i++;
               swap(people, i, j);
            }
        }
        swap(people, i + 1, right);
        return i + 1;
    }
    private static void swap(List<Contact> people, int i, int j){
        Contact temp = people.get(i);
        people.set(i, people.get(j));
        people.set(j,temp);
    }

}
