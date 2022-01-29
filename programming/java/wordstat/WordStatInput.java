import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class WordStatInput {
    private static final String ENCODING="UTF-8";

    private static boolean isWordElement(char c) {
        return Character.isLetter(c) || c == '\'' || Character.DASH_PUNCTUATION == Character.getType(c);
    }

    public static void main(String[] args) throws IOException {
        ArrayList<String> words = new ArrayList<String>();
        ArrayList<Integer> counts = new ArrayList<Integer>();
        try {
            BufferedReader input = new BufferedReader(
                new InputStreamReader(
                    new FileInputStream(args[0]),
                ENCODING));
                // System.err.println(word + " " + count);
            try {
                while (true) {
                    String line = input.readLine();
                    if (line == null) {
                        break;
                    }
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
                        boolean flag = false;
                        for (int j = 0; j < words.size(); j++) {
                            if (words.get(j).equals(word)) {
                                counts.set(j, counts.get(j) + 1);
                                flag = true;
                                break;
                            }
                        }
                        if (!flag) {
                            words.add(word);
                            counts.add(1);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("IO error during file read: " + e.getMessage());
            } finally {
                input.close();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Not enough arguments passed");
            return;
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
            return;
        } catch (UnsupportedEncodingException e) {
            System.err.println(ENCODING + " encoding is not supported");
            return;
        }
        
        try {
            PrintWriter out = new PrintWriter(
                new BufferedWriter(
                    new OutputStreamWriter(
                        new FileOutputStream(args[1]),
                    ENCODING)));
            for (int i = 0; i < words.size(); i++) {
                String word = words.get(i);
                int count = counts.get(i);
                out.println(word + " " + count);
            }
            out.close();
        } catch (UnsupportedEncodingException e) {
            System.err.println(ENCODING + " encoding is not supported");
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Not enough arguments passed");
        }
    }
}