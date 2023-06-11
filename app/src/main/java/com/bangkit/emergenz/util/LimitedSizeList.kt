package com.bangkit.emergenz.util

class LimitedSizeList<T>(private val maxSize: Int) : ArrayList<T>() {

    override fun add(element: T): Boolean {
        if (size < maxSize) {
            return super.add(element)
        }
        return false
    }

    override fun addAll(elements: Collection<T>): Boolean {
        val remainingSize = maxSize - size
        val elementsToAdd = elements.take(remainingSize)
        return super.addAll(elementsToAdd)
    }
}