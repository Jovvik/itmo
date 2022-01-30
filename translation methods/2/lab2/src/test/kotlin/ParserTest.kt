import kotlin.test.Test
import java.text.ParseException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class ParserTest {
    @Test
    fun `empty input`() {
        assertFailsWith<ParseException> { runParser("") }
    }

    @Test
    fun `sample function`() {
        assertEquals(
            Node(
                "H",
                Node("fun"),
                Node("f"),
                Node("("),
                Node(
                    "P", Node(
                        "a"
                    ),
                    Node("T", Node(":"), Node("Int"), Node("G")),
                    Node(
                        "P'",
                        Node(","),
                        Node(
                            "P",
                            Node("b"),
                            Node("T", Node(":"), Node("Bool"), Node("G")),
                            Node("P'")
                        )
                    )
                ),
                Node(")"),
                Node("R", Node("T", Node(":"), Node("Double"), Node("G")))
            ), runParser("fun f(a: Int, b: Bool): Double")
        )
    }

    @Test
    fun `no parameters or return type`() {
        assertEquals(
            Node(
                "H",
                Node("fun"),
                Node("f"),
                Node("("),
                Node("P"),
                Node(")"),
                Node("R")
            ), runParser("fun f()")
        )
        assertEquals(
            Node(
                "H",
                Node("fun"),
                Node("f"),
                Node("("),
                Node("P"),
                Node(")"),
                Node("R", Node("T", Node(":"), Node("Double"), Node("G")))
            ), runParser("fun f(): Double")
        )
        assertEquals(
            Node(
                "H",
                Node("fun"),
                Node("f"),
                Node("("),
                Node(
                    "P",
                    Node("a"),
                    Node("T", Node(":"), Node("Int"), Node("G")),
                    Node("P'"),
                ),
                Node(")"),
                Node("R")
            ), runParser("fun f(a: Int)")
        )
    }

    @Test
    fun `wrong fun keyword`() {
        assertFailsWith<ParseException> { runParser("fuuun f()") }
    }

    @Test
    fun `weird identifier names`() {
        assertEquals(
            Node(
                "H",
                Node("fun"),
                Node("_F1_a2"),
                Node("("),
                Node(
                    "P",
                    Node("`'g[4]VA?`"),
                    Node("T", Node(":"), Node("Int"), Node("G")),
                    Node("P'"),
                ),
                Node(")"),
                Node("R")
            ), runParser("fun _F1_a2(`'g[4]VA?`: Int)"))
        assertFailsWith<ParseException> { runParser("fun 1()") }
        assertFailsWith<ParseException> { runParser("fun f(1: Int)") }
        assertFailsWith<ParseException> { runParser("fun f(a: 1)") }
        assertFailsWith<ParseException> { runParser("fun f(a: Int): 1") }
        assertFailsWith<ParseException> { runParser("fun f(a: Int):") }
    }

    @Test
    fun `trailing comma`() {
        assertEquals(Node(
            "H",
            Node("fun"),
            Node("f"),
            Node("("),
            Node(
                "P",
                Node("a"),
                Node("T", Node(":"), Node("Int"), Node("G")),
                Node("P'", Node(","), Node("P")),
            ),
            Node(")"),
            Node("R")
        ), runParser("fun f(a: Int,)"))
        assertFailsWith<ParseException> { runParser("fun f(, a: Int)") }
    }

    private fun runParser(input: String) = Parser().parse(input.byteInputStream())
}