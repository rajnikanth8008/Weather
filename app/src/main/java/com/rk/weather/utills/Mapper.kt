package com.rk.weather.utills

interface Mapper<R, D> {
    fun mapFrom(type: R): D
}
