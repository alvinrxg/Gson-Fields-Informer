package eu.xiaoguang.lib.gsonfieldsinformer.typeadapters

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import eu.xiaoguang.lib.gsonfieldsinformer.InformerCallback

internal class UnmappedFieldsTypeAdapterFactory(
    private val windowMilliseconds: Long,
    private val callback: InformerCallback
) : TypeAdapterFactory {

    override fun <T : Any?> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        if (Map::class.java.isAssignableFrom(type.rawType)) {
            // temporary solution
            return null
        }

        val delegateAdapter = gson.getDelegateAdapter(this, type) as? TypeAdapter<T>
        return if (delegateAdapter == null || delegateAdapter is UnmappedFieldsTypeAdapter) {
            null
        } else {
            UnmappedFieldsTypeAdapter(type, delegateAdapter, windowMilliseconds, callback)
        }

    }
}