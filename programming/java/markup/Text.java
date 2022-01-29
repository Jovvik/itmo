package markup;

public class Text implements Block {
    private String contents;

    public Text(String contents) {
        this.contents = contents;
    }

    public void toMarkdown(StringBuilder sb) {
        sb.append(contents);
    }
}