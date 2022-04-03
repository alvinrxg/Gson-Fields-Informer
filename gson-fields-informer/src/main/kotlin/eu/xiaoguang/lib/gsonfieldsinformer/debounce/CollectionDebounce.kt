package eu.xiaoguang.lib.gsonfieldsinformer.debounce

import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

internal class CollectionDebounce<T>(
    private val debounceMilliseconds: Long,
    private val callback: ((values: List<T>) -> Unit)
) {

    companion object {
        private const val ThreadName = "fields-informer-debounce"
    }

    private val executor = ScheduledThreadPoolExecutor(1) { r -> Thread(r, ThreadName) }

    private val publisher = Runnable {
        val list = values.toList()
        values.clear()
        if (list.isEmpty()) {
            throw IllegalStateException("queued values is empty")
        }
        callback.invoke(list)
    }

    private val values = mutableListOf<T>()

    fun next(value: T) {
        executor.queue.clear()
        values.add(value)
        executor.schedule(publisher, debounceMilliseconds, TimeUnit.MILLISECONDS)
    }

}