package markup;

import java.util.List;

public class Paragraph implements Block {
    private List<Block> contents;

    public Paragraph(List<Block> contents) {
        this.contents = contents;
    }

    public void toMarkdown(StringBuilder sb) {
        for (Block block : contents) {
            block.toMarkdown(sb);
        }
    }
}