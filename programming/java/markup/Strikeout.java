package markup;

import java.util.List;

public class Strikeout extends TextModifier {
    public Strikeout(List<Block> contents) {
        super(contents, "~", "s");
    }
}