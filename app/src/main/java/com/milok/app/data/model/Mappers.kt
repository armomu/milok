package com.milok.app.data.model

import com.milok.app.data.network.PostDto
import com.milok.app.data.network.StationDto
import com.milok.app.data.network.UserDto

// ─── PostDto → Post ───────────────────────────────────────────────
fun PostDto.toDomain(): Post = Post(
    id = id ?: 0,
    userId = userId ?: 0,
    title = title ?: "",
    body = body ?: ""
)

// @JvmName 解决 JVM 类型擦除后签名冲突：
// List<PostDto> 和 List<UserDto> 擦除后字节码签名相同，需要区分 JVM 方法名
@JvmName("postListToDomain")
fun List<PostDto>.toDomain(): List<Post> = map { it.toDomain() }

// ─── UserDto → User ───────────────────────────────────────────────
fun UserDto.toDomain(): User = User(
    id = id ?: 0,
    name = name ?: "",
    email = email ?: "",
    username = username ?: ""
)

@JvmName("userListToDomain")
fun List<UserDto>.toDomain(): List<User> = map { it.toDomain() }

// ─── StationDto → Station ─────────────────────────────────────────
fun StationDto.toDomain(): Station = Station(
    id = id ?: 0,
    name = name ?: "",
    serialNumber = serialNumber ?: "",
    operator = operator ?: "",
    operatorPhone = operatorPhone ?: "",
    status = status ?: 0,
    createTime = createTime ?: "",
    updateTime = updateTime ?: ""
)

@JvmName("stationListToDomain")
fun List<StationDto>.toDomain(): List<Station> = map { it.toDomain() }
