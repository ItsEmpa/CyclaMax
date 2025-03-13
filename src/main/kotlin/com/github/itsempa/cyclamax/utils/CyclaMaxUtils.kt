package com.github.itsempa.cyclamax.utils

object CyclaMaxUtils {


    inline fun <T, R : Comparable<R>> Iterable<T>.minByNullableOrNull(selector: (T) -> R?): T? {
        val iterator = iterator()
        if (!iterator.hasNext()) return null
        var minElem = iterator.next()
        if (!iterator.hasNext()) return minElem
        var minValue = selector(minElem)
        do {
            val e = iterator.next()
            val v = selector(e) ?: continue
            if (minValue == null || minValue > v) {
                minElem = e
                minValue = v
            }
        } while (iterator.hasNext())
        return minElem
    }
}
