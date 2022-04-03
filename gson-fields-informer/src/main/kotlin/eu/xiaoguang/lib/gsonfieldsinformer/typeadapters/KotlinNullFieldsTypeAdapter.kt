package eu.xiaoguang.lib.gsonfieldsinformer.typeadapters

import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import eu.xiaoguang.lib.gsonfieldsinformer.InformerCallback
import eu.xiaoguang.lib.gsonfieldsinformer.debounce.CollectionDebounce
import kotlin.reflect.KProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMembers

class KotlinNullFieldsTypeAdapter<T : Any?>(
    type: TypeToken<T>,
    private val delegate: TypeAdapter<T>,
    debounceMilliseconds: Long,
    callback: InformerCallback
) : TypeAdapter<T>() {

    private val delegatedType: String = type.toString()

    private val notNullFields = type.rawType.kotlin.declaredMembers
        .filterIsInstance<KProperty<*>>()
        .filter { it.visibility != KVisibility.PRIVATE }
        .filter { !it.returnType.isMarkedNullable }

    private val debouncedCallback: (List<String>) -> Unit = { values: List<String> ->
        values
            .map { it.replace("[\\d+]".toRegex(), "[x]") }
            .distinct()
            .sorted()
            .takeIf { it.isNotEmpty() }
            ?.run { callback.invoke(delegatedType, this) }
    }

    private val collectionDebounced: CollectionDebounce<String> =
        CollectionDebounce(debounceMilliseconds, debouncedCallback)


    override fun write(writer: JsonWriter, value: T?) {
        delegate.write(writer, value)
    }

    override fun read(reader: JsonReader): T? {
        val t: T? = delegate.read(reader)
        if (t != null) {
            checkNullFields(t)
        }
        return t
    }

    private fun checkNullFields(t: T) {
        notNullFields.forEach {
            if (it.call(t) == null) {
                collectionDebounced.next(it.name)
            }
        }
    }
}