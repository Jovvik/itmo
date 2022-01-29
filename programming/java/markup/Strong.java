package markup;

import java.util.List;

public class Strong extends TextModifier {
    public Strong(List<Block> contents) {
        super(contents, "__", "strong");
    }
}