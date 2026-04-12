package com.milok.app.data.model

/**
 * UI 层使用的 Post 领域模型（与 DTO 解耦）
 */
data class Post(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String
)

/**
 * UI 层使用的 User 领域模型
 */
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val username: String
)

/**
 * UI 层使用的 Station 领域模型
 */
data class Station(
    val id: Int,
    val name: String,
    val serialNumber: String,
    val operator: String,
    val operatorPhone: String,
    val status: Int,
    val createTime: String,
    val updateTime: String
)
