package markup;

import java.util.List;

public class Emphasis extends TextModifier {
    public Emphasis(List<Block> contents) {
        super(contents, "*", "em");
    }
}