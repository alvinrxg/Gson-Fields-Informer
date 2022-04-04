package eu.xiaoguang.lib.gson.fieldsinformer.typeadapters

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import eu.xiaoguang.lib.gson.fieldsinformer.InformerCallback

internal class KotlinNullFieldsTypeAdapterFactory(
    private val windowMilliseconds: Long,
    private val callback: InformerCallback
) : TypeAdapterFactory {

    override fun <T : Any?> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        return type.rawType.declaredAnnotations
            // https://stackoverflow.com/a/39806722
            .firstOrNull { it.annotationClass == Metadata::class }
            ?.let {
                KotlinNullFieldsTypeAdapter(
                    type,
                    gson.getDelegateAdapter(this, type),
                    windowMilliseconds,
                    callback
                )
            }

    }
}
