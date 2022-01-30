import java.io.IOException
import java.io.InputStream
import java.text.ParseException
import kotlin.properties.Delegates

class LexicalAnalyzer(private var input: InputStream) {
    private var char by Delegates.notNull<Int>()
    var position = -1
        private set
    var identifier = ""
        private set
    lateinit var token: Token
        private set

    init {
        nextChar()
    }

    private fun nextChar() {
        position++
        try {
            char = input.read()
        } catch (e: IOException) {
            err(e.message ?: "Empty IO exception")
        }
    }

    fun nextToken() {
        while (char.toChar().isWhitespace()) {
            nextChar()
        }
        identifier = ""
        when (char) {
            '('.code -> {
                nextChar()
                token = Token.LPAREN
            }
            ')'.code -> {
                nextChar()
                token = Token.RPAREN
            }
            ','.code -> {
                nextChar()
                token = Token.COMMA
            }
            ':'.code -> {
                nextChar()
                token = Token.COLON
            }
            -1 -> {
                token = Token.END
            }
            '<'.code -> {
                nextChar()
                token = Token.LANGLE
            }
            '>'.code -> {
                nextChar()
                token = Token.RANGLE
            }
            '`'.code -> parseEscapedIdentifier()
            else -> parseIdentifierOrFun()
        }
    }

    private fun parseEscapedIdentifier() {
        val sb = StringBuilder()
        addToStringBuilder(sb)
        while (char != '`'.code) {
            if (char == '\r'.code || char == '\n'.code) {
                err("Newline while reading a backtick-escaped identifier")
            }
            if (char == -1) {
                err("End of input while reading a backtick-escaped identifier")
            }
            addToStringBuilder(sb)
        }
        addToStringBuilder(sb)
        setIdentifier(sb.toString())
    }

    private fun parseIdentifierOrFun() {
        val sb = StringBuilder()
        if (!char.toChar().isLetter() && char != '_'.code) {
            err("Expected letter or underscore, found \"${char.toChar()}\"")
        }
        addToStringBuilder(sb)
        while (char.toChar().isLetter() || char == '_'.code || char.toChar().isDigit()) {
            addToStringBuilder(sb)
        }
        sb.toString().also {
            if (it == "fun") {
                token = Token.FUN
            } else {
                setIdentifier(it)
            }
        }
    }

    private fun addToStringBuilder(sb: StringBuilder) {
        sb.append(char.toChar())
        nextChar()
    }

    private fun setIdentifier(it: String) {
        identifier = it
        token = Token.IDENTIFIER
    }

    private fun err(message: String) {
        throw ParseException(message, position)
    }
}
