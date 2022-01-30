import java.io.PrintWriter
import java.nio.file.Path
import kotlin.system.exitProcess

var nodeCount = -1

fun main() {
    // val input = readLine()
    // if (input == null) {
    //     println("No input, exiting.")
    //     exitProcess(0)
    // }
    val dotFilePath = Path.of("tree.dot")
    dotFilePath.toFile().printWriter().use { out ->
        visualize(Parser().parse("fun f(a: List<Int>, b: Double, `~o4ahi=M`: Int): Double".byteInputStream()), out)
    }
    Runtime.getRuntime().exec("dot -Tpng $dotFilePath -o tree.png")
}

private fun visualize(root: Node, out: PrintWriter) {
    out.println("digraph G {")
    dfs(root, out)
    out.println("}")
}

private fun dfs(node: Node, out: PrintWriter, from: Int? = null) {
    nodeCount++
    out.println("$nodeCount [label = \"${node.name}\"]")
    if (from != null) {
        out.println("$from -> $nodeCount")
    }
    val curNodeIndex: Int = nodeCount
    for (child in node.children) {
        dfs(child, out, curNodeIndex)
    }
}
