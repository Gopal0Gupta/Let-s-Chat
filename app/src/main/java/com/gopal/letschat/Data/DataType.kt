package com.gopal.letschat.Data

data class userData(
    var userId: String?="",
    var name: String?="",
    var number: String?="",
    var imageUrl: String?=""
){
    fun toMap() = mapOf(
        "userId" to userId,
        "name" to name,
        "number" to number,
        "imageUrl" to imageUrl
    )
}