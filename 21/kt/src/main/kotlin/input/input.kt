package input

import aoc.Day
import aoc.Year
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileNotFoundException

private const val AOC_URL = "https://adventofcode.com"
private const val COOKIE_DOMAIN = ".adventofcode.com"

fun getInput(year: Year, day: Day): Result<String> {
    return readInputFromDisk(year, day).onFailure {
        downloadInput(year, day).onSuccess {
            cacheInput(year, day, it)
        }
    }
}

fun cacheInput(year: Year, day: Day, input: String) {
    val file = File(System.getProperty("user.home") + "/.aoc/input/$year/%02d".format(day))

    println(file.absolutePath)
    if (!file.exists()) {
        file.createNewFile()
    }

    file.writeText(input)
}

fun downloadInput(year: Year, day: Day): Result<String> {
    val secret = File(System.getProperty("user.home") + "/.aoc/secret").readText().trim()
    val client = HttpClient(CIO) {
        install(HttpCookies) {
            storage = ConstantCookiesStorage(
                Cookie(
                    name = "session",
                    value = secret,
                    domain = COOKIE_DOMAIN
                )
            )
        }
    }

    val resp = runBlocking { client.get("$AOC_URL/$year/day/$day/input") }
    return when (resp.status) {
        HttpStatusCode.OK -> return Result.success(runBlocking { resp.bodyAsText().trim() })
        else -> Result.failure(Error("couldn't get input from https://adventofcode.com"))
    }
}

fun readInputFromDisk(year: Year, day: Day): Result<String> {
    return try {
        Result.success(File(System.getProperty("user.home") + "/.aoc/input/$year/%02d".format(day)).readText())
    } catch (e: FileNotFoundException) {
        Result.failure(Error("file not found"))
    } catch (e: Exception) {
        println("something went wrong")
        Result.failure(Error("file not found"))
    }
}