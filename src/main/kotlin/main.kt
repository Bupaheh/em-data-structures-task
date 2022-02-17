package emDataStructuresTask

import com.apurebase.arkenv.Arkenv
import com.apurebase.arkenv.util.argument
import com.apurebase.arkenv.util.parse
import java.io.File
import com.apurebase.arkenv.ArkenvException

const val testFileName = "writingSpeedTest"

object Parameters: Arkenv("hard-disk-benchmark") {
    val nmMiB: Int by argument("--size", "-s") {
        description = "The number of MiB for a writing benchmark"
        defaultValue = { 100 }
    }

    val readingBenchmarkFile: String? by argument("--file", "-f") {
        description = "File path for a reading benchmark"
        validate("Size must be < 2 GiB") {
            val file = File(it)
            file.exists() && file.length() < 2 * GiB
        }
    }

    val directory: String? by argument("--dir", "-d") {
        description = "The directory from which a random file will be selected"
        validate("Must exist") { File(it).isDirectory }
    }

    val lowerBound: Int by argument("--lower", "-l") {
        description = "The lower bound of MiB for a random file"
        validate("Must be < 2 * 1024") { it < 2 * 1024 }
        defaultValue = { 10 }
    }

    val upperBound: Int by argument("--upper", "-u") {
        description = "The upper bound of MiB for a random file. "
        validate("Must be <= 2 * 1024") { it <= 2 * 1024 }
        defaultValue = { 2 * 1024 }
    }

    val output: String? by argument("--output", "-o") {
        description = "Output file"
    }
}

fun getRandomFile(path: String?, lowerBound: Int, upperBound: Int): String? =
    path.takeIf { it != null }?.let { dirPath ->
    File(dirPath)
        .walk()
        .filter {
            !it.isDirectory && it.canRead() && it.length() >= MiB * lowerBound &&
                    it.length() < MiB * upperBound
        }
        .toList()
        .random()
        .path
}

fun startBenchmark(readingFilePath: String, writingFilePath: String,
                   writingFileLength: Long, output: String?) {
    val readingResults = readingSpeedBenchmark(readingFilePath)
    val writingResults = writingSpeedBenchmark(writingFilePath, writingFileLength)
    File(writingFilePath).delete()

    val result = "Path: ${readingResults.path}\n" +
            "Size:  %.2f MiB\n".format(readingResults.sizeMiB) +
            "Reading speed: %.2f MiB/s\n\n".format(readingResults.speedMiBs) +
            "Size:  %.2f MiB\n".format(writingResults.sizeMiB) +
            "Writing speed: %.2f MiB/s".format(writingResults.speedMiBs)

    if (output == null)
        println(result)
    else
        File(output).writeText(result)
}

fun main(args: Array <String>) {
    try {
        Parameters.parse(args)
    } catch (e: ArkenvException) {
        println(e.message)
        return
    }
    val readingBenchmarkFile = Parameters.readingBenchmarkFile ?:
        getRandomFile(Parameters.directory, Parameters.lowerBound, Parameters.upperBound)

    if (readingBenchmarkFile == null || Parameters.help) {
        println(Parameters.toString())
        return
    }

    startBenchmark(readingBenchmarkFile, testFileName,
        Parameters.nmMiB * MiB, Parameters.output)
}