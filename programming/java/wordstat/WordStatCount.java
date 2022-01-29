import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.Scanner;
import java.util.Comparator;

public class WordStatCount {
    private static boolean isWordElement(char c) {
        return Character.isLetter(c) || c == '\'' || Character.DASH_PUNCTUATION == Character.getType(c);
    }

    public static void main(String[] args) throws IOException {
        Map<String, Integer> container = new LinkedHashMap<String, Integer>();
        Scanner input = new Scanner(new File(args[0]), StandardCharsets.UTF_8);
        while(input.hasNext()) {
            String line = input.next();
            // String[] words = line.split("[^\\p{IsAlphabetic}\\p{Pd}\']+");
            int i = 0;
            while (i < line.length()) {
                while (i < line.length() && !isWordElement(line.charAt(i))) {
                    i++;
                }
                int numStart = i;
                while (i < line.length() && isWordElement(line.charAt(i))) {
                    i++;
                }
                if (i - numStart == 0) {
                    continue;
                }
                String word = line.substring(numStart, i).toLowerCase();
                Integer val = container.get(word);
                if (val == null) {
                    container.put(word, 1);
                } else {
                    container.put(word, val + 1);
                }
            }
        }
        input.close();
        
        PrintWriter out = new PrintWriter(
            new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream(args[1]),
                StandardCharsets.UTF_8)));
        
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(container.entrySet());
        sorted.sort(Comparator.comparing(Map.Entry::getValue));

        for (Map.Entry<String, Integer> entry : sorted) {
            String word = entry.getKey();
            int count = entry.getValue();
            out.println(word + " " + count);
        }
        out.close();
    }
}