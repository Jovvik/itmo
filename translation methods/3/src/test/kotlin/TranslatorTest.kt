import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

internal class TranslatorTest {
    @Test
    fun simpleTest() {
        runTest(
            listOf(
                "int a",
                "scanf(\"%i\", a)",
                "int b",
                "scanf(\"%i\", b)",
                "printf(\"%i\", b)",
                "printf(\"%i\", a)"
            ),
            listOf(
                "a = readInt()",
                "b = readInt()",
                "print b",
                "print a"
            )
        )


        runTest(
            listOf(
                "int a",
                "int b",
                "scanf(\"%i\", a)",
                "scanf(\"%i\", b)"
            ),
            listOf("(a, b) = (readInt(), readInt())")
        )
        runTest(
            listOf(
                "int a",
                "scanf(\"%i\", a)",
                "int b",
                "b = a"
            ),
            listOf("a = readInt()", "b = a")
        )
    }

    @Test
    fun emptyRead() {
        runTest(
            listOf(
                "int _scanf_res",
                "scanf(\"%i\", _scanf_res)"
            ),
            listOf("readInt()")
        )
    }

    @Test
    fun tupleSizeMismatch() {
        assertThrows<AssertionError> {
            runTranslator("(a, b) = readInt()")
        }
        assertThrows<AssertionError> {
            runTranslator("(a, a) = (readInt(), readInt())")
        }
        assertThrows<AssertionError> {
            runTranslator("a = (readInt(), readInt())")
        }
    }

    @Test
    fun swap() {
        runTest(
            listOf(
                "int a",
                "int b",
                "scanf(\"%i\", a)",
                "scanf(\"%i\", b)",
                "int _swap_a",
                "_swap_a = b",
                "int _swap_b",
                "_swap_b = a",
                "a = _swap_a",
                "b = _swap_b",
            ),
            listOf(
                "(a, b) = (readInt(), readInt())",
                "(a, b) = (b, a)"
            )
        )
    }

    private fun runTest(c: List<String>, py: List<String>) {
        assertEquals(
            """#include "stdio.h"
#include "stdbool.h"

int main() {
${c.joinToString(";${System.lineSeparator()}    ", "    ", ";")}
}
""", runTranslator(py.joinToString(System.lineSeparator()))
        )
    }

    private fun runTranslator(s: String): String {
        val lex = PyLexer(CharStreams.fromString(s))
        val parser = PyParser(CommonTokenStream(lex))
        val translator = Translator()
        ParseTreeWalker.DEFAULT.walk(translator, parser.file())
        return translator.translate()
    }
}