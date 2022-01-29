import java.util.Arrays;
import java.util.Scanner;

public class ReverseSum {
    private static final int MIN_ARRAY_SIZE = 8;
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int[][] g = new int[1][];
        int[] c = new int[1];
        int height = 0;
        int maxWidth = 0;
        while (in.hasNextLine()) {
            Scanner lineScanner = new Scanner(in.nextLine());
            int width = 0;
            while (lineScanner.hasNextInt()) {
                width++;
                if (width >= c.length) {
                    c = Arrays.copyOf(c, c.length * 2);
                }
                c[width - 1] = lineScanner.nextInt();
            }
            if (width > maxWidth) {
                maxWidth = width;
            }

            if (height++ >= g.length) {
                g = Arrays.copyOf(g, g.length * 2);
            }
            g[height - 1] = Arrays.copyOf(c, width);
            lineScanner.close();
        }
        in.close();

        // g = Arrays.copyOf(g, height);

        int[] lineSums = new int[height];
        int[] columnSums = new int[maxWidth];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < g[i].length; j++) {
                lineSums[i] += g[i][j];
                columnSums[j] += g[i][j];
            }
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < g[i].length; j++) {
                System.out.print(columnSums[j] + lineSums[i] - g[i][j] + " ");
            }
            System.out.println();
        }
    }
}