package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

class FileFormattingException extends Exception {
    public FileFormattingException() {}
    public FileFormattingException(String message) {
        super(message);
    }
}

public class Md2HtmlConverter {
    private final int MAX_HEADER_LEVEL = 6;
    private final boolean IGNORE_UNDEFINED_BEHAVIOR = true;

    private int pos;
    private BufferedReader reader;
    private PrintWriter writer;

    private final static Map<String, String> modifierTags = new HashMap<>(Map.of(
        "**", "strong",
        "__", "strong",
        "`", "code",
        "_", "em",
        "*", "em",
        "--", "s"
        // "++", "u"
    ));
    private Map<String, Integer> modifierStarts = new HashMap<>();
    private final static Map<Character, String> specialSymbols = Map.of(
        '<', "&lt;",
        '>', "&gt;",
        '&', "&amp;"
    );

    private String getParagraph() throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        do {
            if ((line = reader.readLine()) == null) {
                return "";
            }
        } while (line.isEmpty());
        do {
            sb.append(line);
            sb.append("\n");
        } while ((line = reader.readLine()) != null && !line.isEmpty());
        return sb.toString();
    }

    public void convert(String input, String output) throws IOException, FileFormattingException {
        try {
            reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(input), StandardCharsets.UTF_8));
            writer = new PrintWriter(output, StandardCharsets.UTF_8);
            String paragraph;
            while ((paragraph = getParagraph()).length() != 0) {
                pos = 0;
                while (pos != paragraph.length() && paragraph.charAt(pos) == '#') {
                    pos++;
                    if (pos > MAX_HEADER_LEVEL) {
                        break;
                    }
                }
                int headerLevel = pos;
                if (pos != paragraph.length() && headerLevel > 0 && headerLevel <= MAX_HEADER_LEVEL &&
                        Character.isWhitespace(paragraph.charAt(pos))) {
                    pos++;
                    String cont = parseParagraph(paragraph);
                    writer.println(wrap(cont, "h" + headerLevel));
                } else {
                    pos = 0;
                    writer.println(wrap(parseParagraph(paragraph), "p"));
                }
            }
        } finally {
            reader.close();
            writer.close();
        }
    }

    private String parseParagraph(String paragraph) throws FileFormattingException {
        StringBuilder sb = new StringBuilder();
        int linkTagStart = -1;
        while (pos < paragraph.length() - 1) {
            boolean modifierFound = parseModifier(paragraph, sb);
            if (paragraph.charAt(pos) == '[') {
                linkTagStart = sb.length();
            } else if (paragraph.charAt(pos) == ']' && linkTagStart != -1) {
                linkTagStart = parseLink(paragraph, sb, linkTagStart);
                continue;
            }
            if (!modifierFound) {
                String c = specialSymbols.get(paragraph.charAt(pos));
                if (c == null) {
                    sb.append(paragraph.charAt(pos));
                } else {
                    sb.append(c);
                }
            }
            pos++;
        }
        removeUnclosedModifiers(sb);
        modifierStarts = new HashMap<>();
        return sb.toString();
    }

    private boolean parseModifier(String paragraph, StringBuilder sb) {
        for (int len : new int[]{2, 1}) {
            if (pos + len > paragraph.length()) {
                continue;
            }
            String mod = paragraph.substring(pos, pos + len);
            String modHtml = modifierTags.get(mod);
            if (modHtml != null) {
                if (pos - 1 >= 0 && paragraph.charAt(pos - 1) == '\\') {
                    sb.delete(sb.length() - 1, sb.length());
                    continue;
                }
                if (!modifierStarts.containsKey(mod)) {
                    if (pos + len < paragraph.length()
                            && Character.isWhitespace(paragraph.charAt(pos + len))
                            && paragraph.charAt(pos + len) != '\n') {
                        sb.append(mod);
                    } else {
                        modifierStarts.put(mod, sb.length());
                        sb.append(getLeftBlock(modHtml));
                    }
                } else {
                    if (pos - 1 < paragraph.length()
                            && Character.isWhitespace(paragraph.charAt(pos - 1))
                            && paragraph.charAt(pos - 1) != '\n') {
                        sb.append(mod);
                    } else {
                        sb.append(getRightBlock(modHtml));
                        modifierStarts.remove(mod);
                    }
                }
                pos += len - 1;
                return true;
            }
        }
        return false;
    }

    private int parseLink(String paragraph, StringBuilder sb, int linkTagStart) throws FileFormattingException {
        int linkStart = pos;
        pos++;
        if (paragraph.length() <= pos || paragraph.charAt(pos) != '(') {
            linkTagStart = -1;
            pos--;
        } else {
            int linkTagEnd = pos - 1;
            while (paragraph.length() != pos && paragraph.charAt(pos) != ')') {
                pos++;
            }
            if (paragraph.length() == pos) {
                if (!IGNORE_UNDEFINED_BEHAVIOR) {
                    throw new FileFormattingException("Malformed link");
                }
                linkTagStart = -1;
                pos = linkStart;
            } else {
                sb.deleteCharAt(linkTagStart);
                sb.insert(linkTagStart, getLeftBlock("a href='" + paragraph.substring(linkTagEnd + 2, pos) + "'"));
                sb.append(getRightBlock("a"));
                pos++;
            }
        }
        return linkTagStart;
    }

    private void removeUnclosedModifiers(StringBuilder sb) throws FileFormattingException {
        List<Map.Entry<String, Integer>> unclosedModifiers = new ArrayList<>(modifierStarts.entrySet());
        if (!IGNORE_UNDEFINED_BEHAVIOR && unclosedModifiers.size() != 0) {
            throw new FileFormattingException("Unclosed modifiers");
        }
        unclosedModifiers.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        for (Map.Entry<String, Integer> unClosedModifier : unclosedModifiers) {
            String modifier = unClosedModifier.getKey();
            int posToDelete = unClosedModifier.getValue();
            sb.delete(posToDelete, posToDelete + 2 + modifierTags.get(modifier).length());
            sb.insert(posToDelete, modifier);
        }
    }

    private String wrap(String content, String modifier) {
        return getLeftBlock(modifier) + content + getRightBlock(modifier);
    }

    private static String getLeftBlock(String mod) {
        return "<" + mod + ">";
    }

    private static String getRightBlock(String mod) {
        return "</" + mod + ">";
    }
}