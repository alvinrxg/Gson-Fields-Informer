package abc.pkgname

import com.google.gson.GsonBuilder
import eu.xiaoguang.lib.gsonfieldsinformer.InformerTypeAdapterFactories
import org.junit.jupiter.api.Test

class KotlinNullFieldsTypeAdapterFactoryTest {

    @Test
    fun test() {
        val k = InformerTypeAdapterFactories.kotlinNullFields(10) { clazz, fields ->
            println("$clazz: $fields")
        }

        val builder = GsonBuilder()
        builder.registerTypeAdapterFactory(k)
        val gson = builder.create()

        val t = gson.fromJson(json1, DataClass::class.java)
        val t2 = gson.fromJson(json2, DataClass::class.java)

        val t3 = gson.fromJson(json1, NormalClass::class.java)
        val t4 = gson.fromJson(json2, NormalClass::class.java)


        println(t)
        println(t2)
        println("normal t3(${t3.a}, ${t3.b})")
        println("normal t4(${t4.a}, ${t4.b})")

        Thread.sleep(100)
    }

    private val json1 = """
            {
                "b": 123
            }
        """.trimIndent()

    private val json2 = """
            {
                "a": "111",
                "b": 123
            }
        """.trimIndent()

    data class DataClass(
        val a: String,
        val b: Int
    )

    class NormalClass(
        val a: String,
        val b: Int
    )
}
