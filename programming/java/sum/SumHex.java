public class SumHex{

    static int strToInt(String str) {
        if (str.startsWith("0x") || str.startsWith("0X")) {
            return Integer.parseUnsignedInt(str.substring(2), 16);
        } else {
            return Integer.parseInt(str);
        }
    }

    public static void main(String[] args) {
        int res = 0;
        for (int i = 0; i < args.length; i++) {
            int numStart = 0;
            for (int j = 0; j < args[i].length(); j++) {
                if (Character.isWhitespace(args[i].charAt(j))) {
                    if (j - numStart > 0) {
                        res += strToInt(args[i].substring(numStart, j));
                    }
                    numStart = j + 1;
                }
            }
            if (numStart < args[i].length()) {
                res += strToInt(args[i].substring(numStart));
            }
        }
        System.out.println(res);
    }
}