package eu.xiaoguang.lib.gson.fieldsinformer

import com.google.gson.TypeAdapterFactory
import eu.xiaoguang.lib.gson.fieldsinformer.typeadapters.KotlinNullFieldsTypeAdapterFactory
import eu.xiaoguang.lib.gson.fieldsinformer.typeadapters.UnmappedFieldsTypeAdapterFactory


object InformerTypeAdapterFactories {

    private const val WINDOW_DEFAULT_MS = 5_000L


    fun unmappedFields(
        windowMilliseconds: Long = WINDOW_DEFAULT_MS,
        callback: InformerCallback
    ): TypeAdapterFactory = UnmappedFieldsTypeAdapterFactory(windowMilliseconds, callback)

    fun kotlinNullFields(
        windowMilliseconds: Long = WINDOW_DEFAULT_MS,
        callback: InformerCallback
    ): TypeAdapterFactory = KotlinNullFieldsTypeAdapterFactory(windowMilliseconds, callback)

}