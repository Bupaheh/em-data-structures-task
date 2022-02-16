package emDataStructuresTask

import java.io.File

const val testFileName = "writingSpeedTest"

fun startBenchmark(readingFilePath: String, writingFilePath: String,
                   writingFileLength: Long = MiB * 100, outputFile: File? = null) {
    val readingResults = readingSpeedBenchmark(readingFilePath)
    val writingResults = writingSpeedBenchmark(writingFilePath, writingFileLength)
    File(writingFilePath).delete()

    val result = "Path: ${readingResults.path}\n" +
            "Size:  %.2f MiB\n".format(readingResults.sizeMiB) +
            "Reading speed: %.2f MiB/s\n\n".format(readingResults.speedMiBs) +
            "Size:  %.2f MiB\n".format(writingResults.sizeMiB) +
            "Writing speed: %.2f MiB/s".format(writingResults.speedMiBs)

    if (outputFile == null)
        println(result)
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
    val randomFile = getRandomFile("/home")
    startBenchmark(randomFile, testFileName)
}