package abc.pkgname

import com.google.gson.GsonBuilder
import eu.xiaoguang.lib.gson.fieldsinformer.InformerTypeAdapterFactories
import org.junit.jupiter.api.Test


class UnmappedFieldsTest {

    @Test
    fun test() {
        // Test worker
        println("original thread: " + Thread.currentThread().name)

        val f = InformerTypeAdapterFactories.unmappedFields(10) { clazz, fields ->
            // pool-2-thread-1
            println("callback thread: ${Thread.currentThread().name}  timestamp: ${System.nanoTime()}  $clazz")
            println("$clazz: $fields")
        }

        val builder = GsonBuilder()
        builder.registerTypeAdapterFactory(f)
        val gson = builder.create()

        gson.fromJson(json1, SampleA::class.java)
        gson.fromJson(json2, SampleA::class.java)
        gson.fromJson(json3, SampleA::class.java)

        gson.fromJson(json1, SampleB::class.java)
        gson.fromJson(json2, SampleB::class.java)
        gson.fromJson(json3, SampleB::class.java)

        Thread.sleep(100)

    }

    data class SampleA(
        val a: String
    )

    data class SampleB(
        val b: String?
    )


    private val json1 = """
{
    "a": "123",
    "b": "456"
}
""".trimIndent()

    private val json2 = """
{
    "a": "123",
    "c": "456"
}
""".trimIndent()

    private val json3 = """
{
    "b": "456"
}
""".trimIndent()

}
