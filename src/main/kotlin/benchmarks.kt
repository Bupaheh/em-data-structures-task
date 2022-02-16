package emDataStructuresTask

import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import kotlin.random.Random
import kotlin.system.measureTimeMillis

const val MiB: Long = 1024 * 1024
const val GiB: Long = MiB * 1024

class BenchmarkResults(val path: String, val sizeMiB: Double, val speedMiBs: Double)

fun writingSpeedBenchmark(filePath: String, length: Long): BenchmarkResults {
    val buffer = ByteBuffer.wrap(Random.nextBytes(length.toInt()))
    val timeMillis = measureTimeMillis {
        FileOutputStream(filePath).channel.use { channel ->
            channel.write(buffer)
            channel.force(true)
        }
    }
    val fileSize = length.toDouble() / MiB
    val timeSeconds = (timeMillis.toDouble() / 1000)
    val speed = fileSize / timeSeconds

    return BenchmarkResults(filePath, fileSize, speed)
}

fun readingSpeedBenchmark(filePath: String): BenchmarkResults {
    val data: ByteArray
    val timeMillis = measureTimeMillis {
        data = File(filePath).readBytes()
    }
    val fileSize = data.size.toDouble() / MiB
    val timeSeconds = (timeMillis.toDouble() / 1000)
    val speed = fileSize / timeSeconds

    // to ensure that the file is read
    println("Ignore: ${data.hashCode()}\n")

    return BenchmarkResults(filePath, fileSize, speed)
}

