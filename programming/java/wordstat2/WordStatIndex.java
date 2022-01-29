import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.Comparator;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.io.*;

public class WordStatIndex {
    private static boolean isWordElement(char c) {
        return Character.isLetter(c) || c == '\'' || Character.DASH_PUNCTUATION == Character.getType(c);
    }

    private static ArrayList<Integer> mergeLists(ArrayList<Integer> a, ArrayList<Integer> b) {
        a.addAll(b);
        return a;
    }
    public static void main(String[] args) throws IOException {
        Map<String, ArrayList<Integer>> map = new LinkedHashMap<>();
        Scanner input = new Scanner(new File(args[0]));
        int idx = 1;
        while(input.hasNext()) {
            String line = input.next();
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
                map.merge(word, new ArrayList<Integer>(List.of(idx)), WordStatIndex::mergeLists);
                idx++;
            }
        }
        input.close();
        
        PrintWriter out = new PrintWriter(
            new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream(args[1]),
                StandardCharsets.UTF_8)));
        
        // List<Map.Entry<String, Integer>> sorted = new ArrayList<>(map.entrySet());
        // sorted.sort(Comparator.comparing(Map.Entry::getValue));

        for (Map.Entry<String, ArrayList<Integer>> entry : map.entrySet()) {
            String word = entry.getKey();
            ArrayList<Integer> positions = entry.getValue();
            out.print(word + " " + positions.size());
            for (int i : positions) {
                out.print(" " + i);
            }
            out.println();
        }
        out.close();
    }
}