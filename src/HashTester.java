import java.io.*;
import java.util.Scanner;
import java.util.function.Function;

public class HashTester {


    public static void main(String args[]) throws IOException {
        Function<String, Integer> func1 = (String s) -> {
            return Integer.valueOf(s.charAt(0));
        };
        Hash<String, Integer> hash = new Hash<String, Integer>(func1);

        Function<String, Integer> func2 = (String s) -> {
            int sum = 0;
            for (int i = 0; i < s.length(); i++) {
                sum += Integer.valueOf(s.charAt(i));
            }
            return sum;
        };
        Hash<String, Integer> hash2 = new Hash<String, Integer>(func2);

        File file = new File("/Users/valentinratti/Desktop/Informatica/2020/EDA/Hashing/resource.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        int k = 0;

        //Pruebo la primer funcion
        while ((line = br.readLine()) != null) {
            Scanner lineScanner = new Scanner(line).useDelimiter("#");
            line = lineScanner.next();
            hash.insert(line, k);
            k++;
        }

        br = new BufferedReader(new FileReader(file));
        k=0;

        //Pruebo la segunda funcion
        while ((line = br.readLine()) != null) {
            Scanner lineScanner = new Scanner(line).useDelimiter("#");
            line = lineScanner.next();
            hash2.insert(line, k);
            k++;
        }

        System.out.println(hash.getColisions());
        System.out.println(hash2.getColisions());


    }
}