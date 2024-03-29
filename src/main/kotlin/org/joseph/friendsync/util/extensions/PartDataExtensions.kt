package org.joseph.friendsync.util.extensions

import io.ktor.http.content.*
import java.io.File
import java.util.*

fun PartData.FileItem.saveImage(path: String): String {
    val fileBytes = streamProvider().readBytes()
    val fileExtension = originalFileName?.takeLastWhile { it != '.' }
    val fileName = UUID.randomUUID().toString() + "." + fileExtension
    val folder = File(path)
    if (!folder.parentFile.exists()) {
        folder.parentFile.mkdirs()
    }
    folder.mkdir()
    File("$path$fileName").writeBytes(fileBytes)
    return fileName
}