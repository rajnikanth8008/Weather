package com.rk.weather.ui.base

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("app:setWeatherIcon")
fun setWeatherIcon(view: ImageView, iconPath: String?) {
    if (iconPath.isNullOrEmpty())
        return
    Picasso.get().cancelRequest(view)
    val newPath = iconPath.replace(iconPath, "a$iconPath")
    val imageid =
        view.context.resources.getIdentifier(newPath + "_svg", "drawable", view.context.packageName)
    val imageDrawable = view.context.resources.getDrawable(imageid, null)
    view.setImageDrawable(imageDrawable)
}


