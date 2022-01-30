import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker
import java.nio.file.Path

fun translate(inPath: Path/*, outPath: Path*/): String {
    val lex = PyLexer(CharStreams.fromPath(inPath))
    val parser = PyParser(CommonTokenStream(lex))
    val translator = Translator()
    ParseTreeWalker.DEFAULT.walk(translator, parser.file())
    return translator.translate()
}

fun main(args: Array<String>) {
    println(translate(Path.of("build", "resources", "main", "example.py")))
}