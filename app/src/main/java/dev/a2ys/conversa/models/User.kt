package dev.a2ys.conversa.models

data class User(
    val userId: String = "",
    val basicInfo: BasicInfo = BasicInfo(),
    val username: String = ""
)

data class BasicInfo(
    val name: String = "",
    val dateOfBirth: String = "",
    val gender: String = ""
)