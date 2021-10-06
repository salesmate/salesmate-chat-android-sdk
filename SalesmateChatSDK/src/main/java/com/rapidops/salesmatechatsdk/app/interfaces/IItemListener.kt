package com.rapidops.salesmatechatsdk.app.interfaces

interface IItemListener<T> {
    fun onItemClick(position: Int, item: T)
}