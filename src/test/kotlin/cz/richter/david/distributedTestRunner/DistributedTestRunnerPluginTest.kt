package cz.richter.david.distributedTestRunner

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files

/**
 * @author d.richter
 * @since 21.02.2019
 */
class DistributedTestRunnerPluginTest {
    private lateinit var tempDir: File
    private lateinit var buildFile: File
    private lateinit var settingsFile: File
    private lateinit var runner: GradleRunner

    @BeforeEach
    internal fun setUp() {
        tempDir = Files.createTempDirectory("gradleTest").toFile()
        buildFile = Files.createFile(tempDir.toPath().resolve("build.gradle.kts")).toFile()
        settingsFile = Files.createFile(tempDir.toPath().resolve("settings.gradle.kts")).toFile()
        settingsFile.writeText("rootProject.name = \"test\"")
        runner = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(tempDir)
    }

    @Test
    fun name() {
        buildFile.writeText("""
            import cz.richter.david.distributedTestRunner.TestDiscoverer
            plugins {
                id("cz.richter.david.gradle.plugin.distributed-test-runner")
            }
            tasks {
                val discover by creating(TestDiscoverer::class) {
                    testClasspath = files("build/classes/kotlin/test")
                    outputFile = File("build/tests.txt")
                }
            }
        """.trimIndent())
        val build = runner.withArguments("discover", "--stacktrace")
            .build()
        println(build.output)
        Assertions.assertEquals(build.task(":discover")!!.outcome, TaskOutcome.SUCCESS)
    }

    @AfterEach
    fun tearDown() {
        tempDir.deleteRecursively()
    }
}