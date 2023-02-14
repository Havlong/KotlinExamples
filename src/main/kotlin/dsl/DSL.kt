package dsl

data class Node(val id: Int, val description: String)

class Graph {
    private val nodeList = mutableListOf<Node>()
    private val edges = mutableListOf<Pair<Node, Node>>()

    fun nodes(block: MutableList<Node>.() -> Unit) {
        block(nodeList)
    }

    operator fun Pair<Node, Node>.unaryPlus() {
        edges += this
    }

    infix fun Int.connectedTo(to: Int): Pair<Node, Node> = nodeList[this - 1] to nodeList[to - 1]

    override fun toString(): String {
        return buildString {
            appendLine("Вершины графа:")
            for (node in nodeList) {
                appendLine(node)
            }
            appendLine("Рёбра графа:")
            for ((l, r) in edges) {
                appendLine("${l.id} -> ${r.id}")
            }
        }
    }
}

fun buildGraph(build: Graph.() -> Unit): Graph {
    return Graph().also(build)
}

fun MutableList<Node>.node(prefix: String = "", body: () -> String): Node {
    val added = Node(size + 1, description = prefix + body())
    this += added
    return added
}

fun main() {
    val builtGraph = buildGraph {
        nodes {
            val first = node { "Вершина добавленная вручную".also(::println) }
            val second = node { "Вторая добавленная вершина".also(::println) }
            node(prefix = "Из консоли: ") {
                print("Введите описание третьей вершины: ")
                readln()
            }

            +(first to second)
        }
        +(1 connectedTo 3)
    }
    println(builtGraph)
}
