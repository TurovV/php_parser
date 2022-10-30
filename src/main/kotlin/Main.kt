import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

fun main(args: Array<String>) {
    val projectDirAbsolutePath = Paths.get("").toAbsolutePath().toString()
    val resourcesPath = Paths.get(projectDirAbsolutePath, "/src/main/resources/php")
    val paths = Files.walk(resourcesPath)
        .filter { item -> Files.isRegularFile(item) }
        .filter { item -> item.toString().endsWith(".php") }

    val files = paths.map { element -> File(element.toUri()) }
    val resultFilePath = Paths.get(projectDirAbsolutePath, "/src/main/resources/result.txt")
    val resultFile = File(resultFilePath.toUri())
    resultFile.setWritable(true)
    val writer = resultFile.printWriter()

    writer.use { out ->
        files.forEach { file ->
            run {
                out.println("Functions from file ${file.name}")
                val parser = PHPParser(file.readText())
                parser.getAllFunctionsInfo().forEach { element ->
                    run {
                        out.println("Function name: ${element.name}")
                        out.println("Number of arguments: ${element.argCount}")
                        out.println("Arguments: ${element.args}")
                        out.println("Comment: ${element.comment}")
                        out.println()
                    }
                }
            }
            out.println()
        }
    }
}