public class SumDouble {
    public static void main(String[] args) {
        double res = 0;
        for (int i = 0; i < args.length; i++) {
            int j = 0;
            while (j < args[i].length()) {
                while (j < args[i].length() && Character.isWhitespace(args[i].charAt(j))) {
                    j++;
                }
                int numStart = j;
                while (j < args[i].length() && !Character.isWhitespace(args[i].charAt(j))) {
                    j++;
                }
                if (j - numStart > 0) {
                    res += Double.parseDouble(args[i].substring(numStart, j));
                }
            }
        }
        System.out.println(res);
    }
}