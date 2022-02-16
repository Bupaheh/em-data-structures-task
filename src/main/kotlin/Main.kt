package emDataStructuresTask

import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import kotlin.random.Random
import kotlin.system.measureTimeMillis

const val testFileName = "writingSpeedTest"
const val MiB: Long = 1024 * 1024
const val GiB: Long = MiB * 1024

fun testWritingSpeed(filePath: String, length: Long = MiB * 100) {
    val buffer = ByteBuffer.wrap(Random.nextBytes(length.toInt()))
    val timeMillis = measureTimeMillis {
        FileOutputStream(filePath).channel.use { channel ->
            channel.write(buffer)
            channel.force(true)
        }
    }
    val fileSize = length.toDouble() / MiB
    val timeSeconds = (timeMillis.toDouble() / 1000)

    println("Size:  %.2f MiB".format(fileSize))
    println("Writing speed: %.2f MiB/s\n".format(fileSize / timeSeconds))

    File(filePath).delete()
}


fun testReadingSpeed(path: String): ByteArray {
    val data: ByteArray
    val timeMillis = measureTimeMillis {
        data = File(path).readBytes()
    }
    val fileSize = data.size.toDouble() / MiB
    val timeSeconds = (timeMillis.toDouble() / 1000)

    println("Path: $path")
    println("Size:  %.2f MiB".format(fileSize))
    println("Reading speed: %.2f MiB/s\n".format(fileSize / timeSeconds))

    return data
}

fun getRandomFile(path: String): String = File(path)
    .walk()
    .filter {
        !it.isDirectory && it.canRead() && it.length() >= MiB * 100 && it.length() < 2 * GiB
    }
    .toList()
    .random()
    .path

fun main() {
    val randomFile = getRandomFile("/var")
    val ignore = testReadingSpeed(randomFile).hashCode()
    testWritingSpeed(testFileName)
    println("Ignore: $ignore")
}