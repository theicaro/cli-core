package commands

import ICARO_HOME
import com.google.gson.GsonBuilder
import download
import picocli.CommandLine
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

@CommandLine.Command(name = "new-here", description = ["the current directory will become an Icaro project"])
class NewHere : Runnable {
    override fun run() {
        try {
            createDependenciesFile("deps.json")
            createSourceDir("src", "main.ic")
            createBinariesDir(".bin", "classes")
        } catch (e: Throwable) {
            return
        }
    }

    private fun createDependenciesFile(path: String) {
        print("insert cli version: ")
        val cliVersion = readLine()!!

        print("insert lang version: ")
        val langVersion = readLine()!!

        if (!File("$ICARO_HOME/cli/core/$cliVersion.jar").isFile)
            download("cli-core", cliVersion, "cli/core")

        if (!File("$ICARO_HOME/lang/$langVersion.jar").isFile)
            download("lang-implementation", langVersion, "lang")
        
        val depsInJson = GsonBuilder().setPrettyPrinting().create().toJson(
            mapOf(
                "cliVersion" to cliVersion,
                "langVersion" to langVersion
            )
        )

        File(path).writeText(depsInJson)
    }

    private fun createSourceDir(pathToSrcDir: String, pathToMainFile: String) {
        Files.createDirectory(Path.of(pathToSrcDir))
        File("$pathToSrcDir/$pathToMainFile").writeText("")
    }

    private fun createBinariesDir(pathToBinDir: String, pathToClassesDir: String) {
        Files.createDirectory(Path.of(pathToBinDir))
        Files.createDirectory(Path.of("$pathToBinDir/$pathToClassesDir"))
    }
}