import java.io.IOException;
import java.util.Arrays;

public class ReverseMin {
    private static final int MIN_ARRAY_SIZE = 8;
    private static int min(int a, int b) {
        return a < b ? a : b;
    }
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        int[] lineMins = new int[MIN_ARRAY_SIZE];
        int[] columnMins = new int[MIN_ARRAY_SIZE];
        int[] widths = new int[MIN_ARRAY_SIZE];

        Arrays.fill(lineMins, Integer.MAX_VALUE);
        Arrays.fill(widths, Integer.MAX_VALUE);
        Arrays.fill(columnMins, Integer.MAX_VALUE);

        int height = 0;
        int maxWidth = 0;
        while (in.hasNextLine()) {
            // System.err.println("New line");
            Scanner lineScanner = new Scanner(in.nextLine());
            int width = 0;
            while (lineScanner.hasNextInt()) {
                // System.err.println("New int");
                width++;
                if (width >= columnMins.length) {
                    columnMins = Arrays.copyOf(columnMins, columnMins.length * 2);
                    Arrays.fill(columnMins, columnMins.length / 2, columnMins.length, Integer.MAX_VALUE);
                }
                int curr = lineScanner.nextInt();
                columnMins[width - 1] = Integer.min(curr, columnMins[width - 1]);
                lineMins[height] = Integer.min(curr, lineMins[height]);
            }
            if (height >= widths.length) {
                widths = Arrays.copyOf(widths, widths.length * 2);
            }
            if (width > maxWidth) {
                maxWidth = width;
            }
            widths[height] = width;
            height++;
            if (height >= lineMins.length) {
                lineMins = Arrays.copyOf(lineMins, lineMins.length * 2);
                Arrays.fill(lineMins, lineMins.length / 2, lineMins.length, Integer.MAX_VALUE);
            }
            lineScanner.close();
        }
        in.close();

        // g = Arrays.copyOf(g, height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < widths[i]; j++) {
                System.out.print(min(lineMins[i], columnMins[j]) + " ");
            }
            System.out.println();
        }
    }
}