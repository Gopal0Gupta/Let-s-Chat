package com.gopal.letschat.data

data class UserData(
    var userId: String? = "",
    var name: String? = "",
    var number: String? = "",
    var imageUrl: String? = ""
) {
    fun toMap() = mapOf(
        "userId" to userId,
        "name" to name,
        "number" to number,
        "imageUrl" to imageUrl
    )
}

data class ChatData(
    val chatId: String? = "",
    val user1:ChatUser = ChatUser(),
    val user2:ChatUser = ChatUser()
)

data class ChatUser(
    var userId: String? = "",
    var name: String? = "",
    var imageUrl: String? = "",
    var number: String? = ""
)

data class Message(
    var sendby:String?="",
    val message:String?="",
    val timestamp:String?=""
)

data class Status(
    val user:ChatUser=ChatUser(),
    val imageUrl: String?="",
    val timestamp:Long?=null
)