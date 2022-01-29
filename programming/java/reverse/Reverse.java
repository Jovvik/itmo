import java.util.Scanner;


public class Reverse {
    static void reverse(Scanner sc) {
        if (sc.hasNextLine()) {
            String line = sc.nextLine();
            reverse(sc);
            Scanner sc2 = new Scanner(line);
            reverseElements(sc2);
            System.out.println();
        }
    }

    static void reverseElements(Scanner sc) {
        if (sc.hasNextInt()) {
            int t = sc.nextInt();
            reverseElements(sc);
            System.out.print(t + " ");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        reverse(sc);
    }
}