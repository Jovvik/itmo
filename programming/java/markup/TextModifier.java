package markup;

import java.util.List;

public abstract class TextModifier implements Block {
    protected List<Block> contents;
    protected final String markdownModifier;
    protected final String HtmlStartModifier;
    protected final String HtmlEndModifier;

    protected TextModifier(List<Block> contents, String markdownModifier, String HtmlModifier) {
        this.contents = contents;
        this.markdownModifier = markdownModifier;
        this.HtmlStartModifier = "<" + HtmlModifier + ">";
        this.HtmlEndModifier = "</" + HtmlModifier + ">";
    }

    public final void toMarkdown(StringBuilder sb) {
        sb.append(markdownModifier);
        for (Block block : contents) {
            block.toMarkdown(sb);
        }
        sb.append(markdownModifier);
    }

    public final void toHtml(StringBuilder sb) {
        sb.append(HtmlStartModifier);
        for (Block block : contents) {
            block.toMarkdown(sb);
        }
        sb.append(HtmlEndModifier);
    }
}