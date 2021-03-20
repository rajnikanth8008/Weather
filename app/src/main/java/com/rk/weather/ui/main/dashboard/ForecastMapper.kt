package com.rk.weather.ui.main.dashboard

import com.rk.weather.data.model.ListItem
import com.rk.weather.utills.Mapper

class ForecastMapper constructor() : Mapper<List<ListItem>, List<ListItem>> {
    override fun mapFrom(type: List<ListItem>): List<ListItem> {
        val days = arrayListOf<String>()
        val mappedArray = arrayListOf<ListItem>()

        type.forEachIndexed { _, listItem ->
            if (days.contains(listItem.dtTxt?.substringBefore(" ")).not()) // Add day to days
                listItem.dtTxt?.substringBefore(" ")?.let { days.add(it) }
        }

        days.forEach { day ->

            // Find min and max temp values each of the day
            val array = type.filter { it.dtTxt?.substringBefore(" ").equals(day) }

            val minTemp = array.minBy { it.main?.tempMin ?: 0.0 }?.main?.tempMin
            val maxTemp = array.maxBy { it.main?.tempMax ?: 0.0 }?.main?.tempMax

            array.forEach {
                if (minTemp != null) {
                    it.main?.tempMin = minTemp
                } // Set min
                if (maxTemp != null) {
                    it.main?.tempMax = maxTemp
                } // Set max
                mappedArray.add(it) // add it to mappedArray
            }
        }

        return mappedArray
            .filter { it.dtTxt?.substringAfter(" ")?.substringBefore(":")?.toInt()!! /*>=*/ == 9 }
            .distinctBy { it.getDay() } // Eliminate same days
            .toList() // Return as list
    }
}