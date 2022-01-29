public class SumLong{
    public static void main(String[] args) {
        long res = 0;
        for (int i = 0; i < args.length; i++) {
            int numStart = 0;
            for (int j = 0; j < args[i].length(); j++) {
                if (Character.isWhitespace(args[i].charAt(j))) {
                    if (j - numStart > 0) {
                        res += Long.parseLong(args[i].substring(numStart, j));
                    }
                    numStart = j + 1;
                }
            }
            if (numStart < args[i].length()) {
                res += Long.parseLong(args[i].substring(numStart));
            }
        }
        System.out.println(res);
    }
}