import java.util.ArrayList;
import java.util.Scanner;

public class ScannerTest {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String line = in.nextLine();
            Scanner intScanner = new Scanner(line);
            while (intScanner.hasNextInt()) {
                System.out.print(intScanner.nextInt());
            }
            System.out.println("bruh");
        }
        in.close();
    }
}