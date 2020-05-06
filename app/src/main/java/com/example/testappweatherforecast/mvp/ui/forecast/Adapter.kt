package com.example.testappweatherforecast.mvp.ui.forecast

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testappweatherforecast.R
import com.example.testappweatherforecast.mvp.entity.ForecastDB
import java.util.*

class Adapter(private val weatherList: List<Pair<ForecastDB, Int>>, con: Context) :
    RecyclerView.Adapter<Adapter.BaseViewHolder>(){

    private val context = con


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when(viewType){
            R.layout.day_of_week_item ->{
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.day_of_week_item, parent, false)
                DayOfWeekViewHolder(view)}
            R.layout.forecast_item ->{
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.forecast_item, parent, false)
                ForecastViewHolder(view)}
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.day_of_week_item, parent, false)
                TodayViewHolder(view)}
        }

    }

    override fun getItemCount(): Int = (weatherList.size)

    override fun getItemViewType(position: Int): Int {
        when {
            weatherList[position].second == 2 -> return R.layout.day_of_week_item
            weatherList[position].second == 3 -> return R.layout.forecast_item
            else -> return 1
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when(holder){
            is TodayViewHolder -> weatherList[position].first.let { holder.bind(it) }
            is ForecastViewHolder -> weatherList[position].first.let { holder.bind(it) }
            is DayOfWeekViewHolder -> weatherList[position].first.let { holder.bind(it) }
        }
    }

    fun getDrawableForIcon(icon: String): Drawable? {
        context.packageName

        return context.getDrawable(
            context.resources
                .getIdentifier("pic$icon",  "drawable", context.packageName))
    }

    inner class ForecastViewHolder(itemView: View): BaseViewHolder(itemView){
        private val image: ImageView = itemView.findViewById(R.id.imageForecastItem)
        private val time: TextView = itemView.findViewById(R.id.timeForecastItemTextView)
        private val weather : TextView = itemView.findViewById(R.id.weatherForecastItemTextView)
        private val temperature : TextView = itemView.findViewById(R.id.temperatureForecastItemTextView)

        override fun bind(item: ForecastDB){
            image.setImageDrawable(getDrawableForIcon(item.icon))
            time.text = item.dtTxt.takeLast(8).take(5)
            weather.text = item.main
            temperature.text = (item.temp.toInt()-273).toString()
        }
    }

    inner class DayOfWeekViewHolder(itemView: View): BaseViewHolder(itemView){
        private val weekDay: TextView = itemView.findViewById(R.id.dayOfWeekTextView)

        @SuppressLint("SetTextI18n")
        override fun bind(item: ForecastDB){

            val c = Calendar.getInstance()
            c.set(item.dtTxt.take(2).toInt(),
                item.dtTxt.take(7).takeLast(2).toInt(),
                item.dtTxt.take(10).takeLast(2).toInt())

            when (c.get(Calendar.DAY_OF_MONTH)) {
                Calendar.SUNDAY -> weekDay.text = "Sunday"
                Calendar.MONDAY -> weekDay.text = "Monday"
                Calendar.TUESDAY -> weekDay.text = "Tuesday"
                Calendar.WEDNESDAY -> weekDay.text = "Wednesday"
                Calendar.THURSDAY -> weekDay.text = "Thursday"
                Calendar.FRIDAY -> weekDay.text = "Friday"
                Calendar.SATURDAY -> weekDay.text = "Saturday"
            }

        }
    }

    inner class TodayViewHolder(itemView: View): BaseViewHolder(itemView) {
        private val weekDay: TextView = itemView.findViewById(R.id.dayOfWeekTextView)

        @SuppressLint("SetTextI18n")
        override fun bind(item: ForecastDB) {
            weekDay.text = "Today"
        }
    }

    abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: ForecastDB)
    }
}