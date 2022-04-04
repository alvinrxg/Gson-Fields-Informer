package eu.xiaoguang.lib.gson.fieldsinformer.typeadapters

import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import eu.xiaoguang.lib.gson.fieldsinformer.InformerCallback
import eu.xiaoguang.lib.gson.fieldsinformer.debounce.CollectionDebounce
import java.io.Reader

internal class UnmappedFieldsTypeAdapter<T>(
    type: TypeToken<T>,
    private val delegate: TypeAdapter<T>,
    debounceMilliseconds: Long,
    private val callback: InformerCallback
) : TypeAdapter<T>() {

    companion object {
        /**
         * @see com.google.gson.internal.bind.JsonTreeReader.UNREADABLE_READER
         */
        private val UNREADABLE_READER = object : Reader() {
            override fun close(): Unit = throw AssertionError()
            override fun read(cbuf: CharArray?, off: Int, len: Int): Int = throw AssertionError()
        }

    }

    private val delegatedType: String = type.toString()

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
        return if (reader is UnmappedFieldsJsonReader) {
            delegate.read(reader)
        } else {
            delegate.read(UnmappedFieldsJsonReader(reader, collectionDebounced))
        }
    }

    private class UnmappedFieldsJsonReader(val delegate: JsonReader, val listener: CollectionDebounce<String>) :
        JsonReader(UNREADABLE_READER) {

        // --> ReflectiveTypeAdapterFactory
        override fun skipValue() {
            listener.next(delegate.path)
            delegate.skipValue()
        }

        override fun close() = delegate.close()

        override fun beginArray() = delegate.beginArray()

        override fun endArray() = delegate.endArray()

        override fun beginObject() = delegate.beginObject()

        override fun endObject() = delegate.endObject()

        override fun hasNext(): Boolean = delegate.hasNext()

        override fun peek(): JsonToken = delegate.peek()

        override fun nextName(): String = delegate.nextName()

        override fun nextString(): String = delegate.nextString()

        override fun nextBoolean(): Boolean = delegate.nextBoolean()

        override fun nextNull() = delegate.nextNull()

        override fun nextDouble(): Double = delegate.nextDouble()

        override fun nextLong(): Long = delegate.nextLong()

        override fun nextInt(): Int = delegate.nextInt()

        override fun getPath(): String = delegate.path
    }

}