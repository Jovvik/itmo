public class Sum{
    public static void main(String[] args) {
        int res = 0;
        for (int i = 0; i < args.length; i++) {
            int numStart = 0;
            for (int j = 0; j < args[i].length(); j++) {
                if (Character.isWhitespace(args[i].charAt(j))) {
                    if (j - numStart > 0) {
                        res += Integer.parseInt(args[i].substring(numStart, j));
                    }
                    numStart = j + 1;
                }
            }
            if (numStart < args[i].length()) {
                res += Integer.parseInt(args[i].substring(numStart));
            }
        }
        System.out.println(res);
    }
}