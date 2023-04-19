package com.tahaproject.clothingsuggester.data.remote.fake

import com.tahaproject.clothingsuggester.data.models.response.*
import kotlin.random.Random

object FakeDataGenerator {
    fun generateWeatherData(): WeatherData {
        val city = City(
            name = "Shūrīyah",
            coordinates = Coordinates(33.3152, 44.3661),
            country = "IQ"
        )

        val forecasts = mutableListOf<Forecast>()

        for (day in 1..5) {
            for (time in 1..8) {
                val main = Main(
                    temp = Random.nextDouble(10.0, 30.0),
                    tempMin = Random.nextDouble(8.0, 15.0),
                    tempMax = Random.nextDouble(20.0, 35.0)
                )

                val weather = Weather(
                    description = "Fake Weather",
                    icon = "03d"
                )

                val dt = System.currentTimeMillis() / 1000L + 86400L * day + 10800L * time
                val dtTxt = "2023-04-${16 + day} ${(time - 1) * 3}:00:00"

                val forecast = Forecast(
                    dt = dt,
                    main = main,
                    weather = listOf(weather),
                    dtTxt = dtTxt
                )

                forecasts.add(forecast)
            }
        }

        return WeatherData(city, forecasts)
    }
}
