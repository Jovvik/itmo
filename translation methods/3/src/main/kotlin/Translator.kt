import org.antlr.v4.runtime.RuleContext

class Translator : PyBaseListener() {
    private val mainSb = StringBuilder()
    private val funSb = StringBuilder()
    private var inFun = false
    private val variables = mutableSetOf<String>()
    private val delayedAssign = mutableListOf<Pair<String, String>>() // left = right
    private var indentLevel = 1
    private var savedIndentLevel = mutableListOf<Int>()

    fun translate(): String {
        return buildString {
            appendLine(HEADER)
            append(funSb)
            if (funSb.isNotEmpty()) {
                appendLine()
            }
            appendLine(MAIN_HEADER)
            append(mainSb)
            append(FOOTER)
        }
    }

    private fun addLine(s: String, colon: Boolean = true) {
        val indent = "    ".repeat(indentLevel)
        (if (inFun) funSb else mainSb).appendLine("$indent$s${if (colon) ";" else ""}")
    }

    override fun enterAssign(ctx: PyParser.AssignContext?) {
        ctx ?: return
        val name = ctx.id()!!.NAME()
        val vars = mutableListOf<String>()
        if (name != null) { // not a tuple
            addVariable(name.toString())
            vars.add(name.toString())
        } else {
            val ids = ctx.id()?.id()!!
            for (id in ids) {
                addVariable(id.NAME()!!.toString())
                vars.add(id.NAME()!!.toString())
            }
        }
        val expr = ctx.expr()!!
        val varsSet = vars.toSet()
        assert(varsSet.size == vars.size) { "Same variable multiple times in tuple lhs" }
        assignExpr(ctx.id(), expr)
    }

    override fun enterIf_(ctx: PyParser.If_Context?) {
        addLine("if (${ctx?.arithmetic()?.text}) {", false)
        indentLevel++
    }

    override fun exitIf_(ctx: PyParser.If_Context?) {
        indentLevel--
        addLine("}", false)
    }

    override fun enterElse_(ctx: PyParser.Else_Context?) {
        addLine("else {", false)
        indentLevel++
    }

    override fun exitElse_(ctx: PyParser.Else_Context?) {
        indentLevel--
        addLine("}", false)
    }

    override fun enterWhile_(ctx: PyParser.While_Context?) {
        addLine("while (${ctx?.arithmetic()?.text!!}) {", false)
        indentLevel++
    }

    override fun exitWhile_(ctx: PyParser.While_Context?) {
        indentLevel--
        addLine("}", false)
    }

    override fun enterReturn_(ctx: PyParser.Return_Context?) {
        ctx ?: return
        addLine("return ${ctx.simpleExpr().text}")
    }

    override fun enterFun(ctx: PyParser.FunContext?) {
        inFun = true
        savedIndentLevel.add(indentLevel)
        indentLevel = 0
        addLine(
            "int ${ctx?.NAME(0)}(${ctx?.NAME()?.drop(1)?.joinToString(", ")}) {",
            false
        )
        indentLevel = 1
    }

    override fun exitFun(ctx: PyParser.FunContext?) {
        indentLevel = 0
        addLine("}", false)
        inFun = false
        indentLevel = savedIndentLevel.removeLast()
    }

    private fun addVariable(name: String) {
        if (name in variables) return
        addLine("int $name")
        variables.add(name)
    }

    private fun assignExpr(
        lhs: PyParser.IdContext,
        rhs: PyParser.ExprContext
    ) {
        val lhsName = lhs.NAME()
        val lhsIds = lhs.id()
        val rhsExprs = rhs.expr()
        if (lhsName != null) {
            exprContexts(lhsName.toString(), rhs)
        } else {
            assert(rhsExprs?.isNotEmpty() == true)
            for ((id, expr) in lhsIds zip rhsExprs) {
                assignExpr(id, expr)
            }
        }
    }

    private fun exprContexts(
        lhsName: String,
        rhs: PyParser.ExprContext
    ): List<PyParser.ExprContext>? {
        val rhsRead = rhs.simpleExpr()?.read()
        val rhsArithmetic = rhs.simpleExpr()?.arithmetic()
        val rhsExprs = rhs.expr()
        when {
            rhsRead != null -> addLine("scanf(\"%i\", $lhsName)")
            rhsArithmetic != null -> delayedAssign.add(lhsName to rhsArithmetic.text)
            else -> throw AssertionError()
        }
        return rhsExprs
    }

    override fun exitAssign(ctx: PyParser.AssignContext?) {
        if (delayedAssign.size == 1) {
            val (lhs, rhs) = delayedAssign.first()
            addLine("$lhs = $rhs")
        } else {
            for ((lhs, rhs) in delayedAssign) {
                addVariable(SWAP_PREFIX + lhs)
                addLine("$SWAP_PREFIX$lhs = $rhs")
            }
            for ((lhs, _) in delayedAssign) {
                addLine("$lhs = $SWAP_PREFIX$lhs")
            }
        }
        delayedAssign.clear()
    }

    override fun enterPrint(ctx: PyParser.PrintContext?) {
        addLine("printf(\"%i\", ${ctx?.simpleExpr()?.text!!})")
    }

    override fun enterRead(ctx: PyParser.ReadContext?) {
        if (!isInAssign(ctx)) {
            addVariable(SCANF_RES_NAME)
            addLine("scanf(\"%i\", $SCANF_RES_NAME)")
        }
    }

    override fun enterPost(ctx: PyParser.PostContext?) {
        addLine("do {", false)
        indentLevel++
    }

    override fun exitPost(ctx: PyParser.PostContext?) {
        indentLevel--
        addLine("} while (${ctx?.arithmetic()?.text!!})")
    }

    private fun isInAssign(ctx: RuleContext?): Boolean {
        var topLevelParent = ctx?.parent
        while (topLevelParent is PyParser.ExprContext? || topLevelParent is PyParser.SimpleExprContext) {
            topLevelParent = topLevelParent?.parent
        }
        return topLevelParent is PyParser.AssignContext?
    }

    companion object {
        private val HEADER = """
            #include "stdio.h"
            #include "stdbool.h"

        """.trimIndent()
        private val FOOTER = "}${System.lineSeparator()}"
        private const val SWAP_PREFIX = "_swap_"
        private const val SCANF_RES_NAME = "_scanf_res"
        private const val MAIN_HEADER = "int main() {"
    }
}