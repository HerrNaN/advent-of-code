package days

import Day
import cc.ekblad.konbini.*
import common.newline
import common.space

typealias Commands = List<Cmd>

fun Commands.asFileTree(): TreeNode {
    val root = TreeNode.Dir("/", mutableMapOf(), null)
    var currentDir: TreeNode.Dir = root

    this.forEach {
        when (it) {
            is Cmd.Cd ->
                currentDir = when (it.toDir) {
                    "/" -> root
                    ".." -> currentDir.parent!!
                    else -> currentDir.children[it.toDir] as TreeNode.Dir
                }

            is Cmd.Ls -> it.output.forEach { entry ->
                when (entry) {
                    is Entry.Dir -> currentDir.children[entry.name] =
                        TreeNode.Dir(entry.name, mutableMapOf(), currentDir)

                    is Entry.File -> currentDir.children[entry.name] = TreeNode.File(entry.name, entry.size)
                }
            }
        }
    }

    return root
}

sealed class Cmd {
    data class Ls(val output: List<Entry>) : Cmd()
    data class Cd(val toDir: String) : Cmd()
}

sealed class Entry {
    data class Dir(val name: String) : Entry()
    data class File(val name: String, val size: Int) : Entry()
}

sealed class TreeNode(val name: String) {

    class Dir(name: String, val children: MutableMap<String, TreeNode>, val parent: Dir?) : TreeNode(name)
    class File(name: String, val size: Int) : TreeNode(name)

    fun size(): Int = when (this) {
        is Dir -> this.children.values.sumOf { it.size() }
        is File -> this.size
    }

    fun nodes(): List<TreeNode> = when (this) {
        is Dir -> listOf(this).plus(
            this.children.values.toList().flatMap { it.nodes() })

        is File -> listOf(this)
    }
}

val command = parser {
    string("$")
    space()
    val cmd = oneOf(
        cd, ls
    )
    cmd
}

val cd = parser {
    string("cd")
    space()
    val toDir = regex("[^\n]*")
    Cmd.Cd(toDir)
}

val ls = parser {
    string("ls")
    newline()
    val entries = chain(entry, newline).terms
    Cmd.Ls(entries)
}

val entry: Parser<Entry> = parser {
    oneOf(
        file, directory
    )
}

val file = parser {
    val size = integer()
    space()
    val name = regex("[^\n]*")
    Entry.File(name, size.toInt())
}

val directory = parser {
    string("dir")
    space()
    val name = regex("[^\n]*")
    Entry.Dir(name)
}

class Day2207 : Day<Commands>() {
    override fun inputParser(): Parser<Commands> = parser { chain(command, newline).terms }

    override fun solve1(input: Commands) =
        input.asFileTree().nodes()
            .filterIsInstance<TreeNode.Dir>()
            .filter { it.size() <= 100_000 }
            .sumOf { it.size() }

    override fun solve2(input: Commands): Int {
        val fileTree = input.asFileTree()
        val unusedSpace = 70_000_000 - fileTree.size()
        return fileTree.nodes()
            .filterIsInstance<TreeNode.Dir>()
            .filter { unusedSpace + it.size() >= 30_000_000 }
            .map { it.size() }
            .minByOrNull { it }!!
    }
}