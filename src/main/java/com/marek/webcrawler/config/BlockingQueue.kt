package com.marek.webcrawler.config

import java.util.concurrent.locks.ReentrantLock

class BlockingQueue {

    val urlList: MutableList<PriorityList> = mutableListOf()
    private val lock = ReentrantLock()

    @Synchronized
    fun getSize() = urlList.size

    fun insert(newUrl: PriorityList) {
        for (i in 0..urlList.size) {
            if (newUrl.priority < urlList[i].priority)
                continue
            urlList.add(i, newUrl)
            break
        }
        urlList.add(newUrl)
    }

    @Synchronized
    fun enqueue(newUrl: PriorityList) {
        insert(newUrl)
    }

    @Synchronized
    fun dequeue(): PriorityList {
        while (urlList.size == 0)
            lock.lock()
        if (urlList.size > 0)
            lock.unlock()
        return urlList.removeAt(urlList.size - 1)
    }
}

data class PriorityList(val url: String = "", val priority: Double = 0.0)