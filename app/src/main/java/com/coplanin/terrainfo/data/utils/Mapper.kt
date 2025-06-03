package com.coplanin.terrainfo.data.utils

interface Mapper<in T, out R> {
    fun map(input: T): R
} 