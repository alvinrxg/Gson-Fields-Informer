package eu.xiaoguang.lib.gsonfieldsinformer

import com.google.gson.TypeAdapterFactory
import eu.xiaoguang.lib.gsonfieldsinformer.typeadapters.KotlinNullFieldsTypeAdapterFactory
import eu.xiaoguang.lib.gsonfieldsinformer.typeadapters.UnmappedFieldsTypeAdapterFactory


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