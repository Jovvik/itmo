import Token.*
import java.io.InputStream
import java.text.ParseException

class Node(val name: String, vararg val children: Node) {
    override fun equals(other: Any?): Boolean {
        if (other !is Node) {
            return false
        }
        return name == other.name && children.contentEquals(other.children)
    }
}

class Parser {
    private lateinit var lex: LexicalAnalyzer

    private fun h(): Node {
        assertTokenAndAdvance(FUN)
        val functionName = getIdentifier()
        assertTokenAndAdvance(LPAREN)
        val parameters = p()
        assertTokenAndAdvance(RPAREN)
        val returnType = r()
        assertTokens(END)
        return Node("H", Node("fun"), functionName, Node("("), parameters, Node(")"), returnType)
    }

    private fun r(): Node {
        return when (lex.token) {
            END -> Node("R")
            COLON -> {
                val typeName = t()
                return Node("R", typeName)
            }
            else -> errTokens(END, COLON)
        }
    }

    private fun pprime(): Node {
        return when (lex.token) {
            RPAREN -> {
                Node("P'")
            }
            COMMA -> {
                lex.nextToken()
                Node("P'", Node(","), p())
            }
            else -> errTokens(RPAREN, COMMA)
        }
    }

    private fun p(): Node {
        return when (lex.token) {
            RPAREN -> {
                Node("P")
            }
            IDENTIFIER -> {
                val name = getIdentifier()
                val type = t()
                val tail = pprime()
                Node("P", name, type, tail)
            }
            else -> errTokens(RPAREN, IDENTIFIER)
        }
    }

    private fun t(): Node {
        assertTokenAndAdvance(COLON)
        val typeName = getIdentifier()
        val generic = g()
        return Node("T", Node(":"), typeName, generic)
    }

    private fun g(): Node {
        return when (lex.token) {
            LANGLE -> {
                lex.nextToken()
                val typeName = getIdentifier()
                val rest = a()
                lex.nextToken()
                Node("G", Node("<"), typeName, rest, Node(">"))
            }
            RPAREN, COMMA, END -> {
                Node("G")
            }
            else -> errTokens(LANGLE, RPAREN, COMMA, END)
        }
    }

    private fun a(): Node {
        return when (lex.token) {
            COMMA -> {
                lex.nextToken()
                Node("A", aprime())
            }
            RANGLE -> {
                Node("A")
            }
            else -> errTokens(COMMA, RANGLE)
        }
    }

    private fun aprime(): Node {
        return when (lex.token) {
            COLON -> {
                return Node("A'", getIdentifier(), a())
            }
            RANGLE -> {
                Node("A'")
            }
            else -> errTokens(IDENTIFIER, RANGLE)
        }
    }

    private fun getIdentifier(): Node {
        assertTokens(IDENTIFIER)
        return Node(lex.identifier).also { lex.nextToken() }
    }

    private fun assertTokenAndAdvance(token: Token) {
        assertTokens(token)
        lex.nextToken()
    }

    private fun assertTokens(vararg tokens: Token) {
        if (lex.token !in tokens) {
            errTokens(*tokens)
        }
    }

    fun parse(input: InputStream): Node {
        lex = LexicalAnalyzer(input)
        lex.nextToken()
        return h()
    }

    private fun errTokens(vararg tokens: Token): Nothing {
        err("Expected ${tokens.joinToString(" or ")}, found ${lex.token}")
    }

    private fun err(message: String): Nothing {
        throw ParseException(message, lex.position)
    }
}
