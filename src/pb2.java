import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class List {
    private final int[] numere;
    List() {
        var line_scanner = new Scanner(System.in);
        var scan = new Scanner(line_scanner.nextLine());
        ArrayList<Integer> temp = new ArrayList<Integer>();
        while (true) {
            if (!scan.hasNextInt()) break;
            temp.add(scan.nextInt());
        }
        if(temp.size()<5){
            System.out.println("Expected at least 5 numbers");
            System.exit(-1);
        }
        numere = temp.stream().mapToInt(x -> x).toArray();
    }
    int get_first_and_sort(){
        int first = numere[0];
        for(int i:numere){
            System.out.print(i+" ");
        }
        System.out.println();
        Arrays.sort(numere);
        for(int i:numere){
            System.out.print(i+" ");
        }
        System.out.println();
        return first;
    }
    int get_position(int element){
        return Arrays.binarySearch(numere,element);
    }
}


public class pb2 {
    public static void main(String[] args) {
        var list=new List();
        var first=list.get_first_and_sort();
        var pos=list.get_position(first);
        System.out.println(pos);
    }
}
