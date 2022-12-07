package days

import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals


internal class Day2207Test {

    private val testInput =
        listOf(
            Cmd.Cd("/"),
            Cmd.Ls(
                listOf(
                    Entry.Dir("a"),
                    Entry.File("b.txt", 14848514),
                    Entry.File("c.dat", 8504156),
                    Entry.Dir("d")
                )
            ),
            Cmd.Cd("a"),
            Cmd.Ls(
                listOf(
                    Entry.Dir("e"),
                    Entry.File("f", 29116),
                    Entry.File("g", 2557),
                    Entry.File("h.lst", 62596),
                )
            ),
            Cmd.Cd("e"),
            Cmd.Ls(
                listOf(
                    Entry.File("i", 584),
                )
            ),
            Cmd.Cd(".."),
            Cmd.Cd(".."),
            Cmd.Cd("d"),
            Cmd.Ls(
                listOf(
                    Entry.File("j", 4060174),
                    Entry.File("d.log", 8033020),
                    Entry.File("d.ext", 5626152),
                    Entry.File("k", 7214296),
                )
            )
        )


    @Test
    fun inputParser() {
        assertContentEquals(
            testInput,
            Day2207().parseInput(
                """
$ cd /
$ ls
dir a
14848514 b.txt
8504156 c.dat
dir d
$ cd a
$ ls
dir e
29116 f
2557 g
62596 h.lst
$ cd e
$ ls
584 i
$ cd ..
$ cd ..
$ cd d
$ ls
4060174 j
8033020 d.log
5626152 d.ext
7214296 k
        """.trimIndent()
            )
        )
    }

    @Test
    fun solve1() {
        assertEquals(95437, Day2207().solve1(testInput))
    }

    @Test
    fun solve2() {
        assertEquals(24933642, Day2207().solve2(testInput))
    }
}