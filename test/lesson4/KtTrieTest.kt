package lesson4

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

class KtTrieTest : AbstractTrieTest() {

    override fun create(): MutableSet<String> =
        KtTrie()

    @Test
    @Tag("Example")
    fun generalTest() {
        doGeneralTest()
    }

    @Test
    @Tag("7")//тесты полные
    fun iteratorTest() {
        doIteratorTest()
    }

    @Test
    @Tag("8")//тесты полные
    fun iteratorRemoveTest() {
        doIteratorRemoveTest()
    }

}