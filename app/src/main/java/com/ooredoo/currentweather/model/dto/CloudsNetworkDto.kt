package com.location.openweathermap.model.dto

import com.google.gson.annotations.SerializedName

data class CloudsNetworkDto(
    @SerializedName("all")
    var all: Int
)