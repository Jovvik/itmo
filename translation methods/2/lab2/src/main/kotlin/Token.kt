enum class Token(private val humanReadableName: String) {
    FUN("keyword fun"),
    IDENTIFIER("identifier"),
    COLON("colon"),
    LPAREN("left parenthesis"),
    RPAREN("right parenthesis"),
    LANGLE("left angled parenthesis"),
    RANGLE("right angled parenthesis"),
    COMMA("comma"),
    END("end of input");

    override fun toString() = humanReadableName
}
