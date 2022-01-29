package md2html;

import java.io.*;

public class Md2Html {
    public static void main(String[] args) throws IOException, FileFormattingException {
        if (args.length < 2) {
            throw new IllegalArgumentException("Two parameters are required");
        }
        Md2HtmlConverter converter = new Md2HtmlConverter();
        converter.convert(args[0], args[1]);
    }
}