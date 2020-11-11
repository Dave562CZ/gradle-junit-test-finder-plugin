package cz.richter.david.distributedTestRunner

import org.junit.platform.engine.TestExecutionResult
import org.junit.platform.engine.discovery.DiscoverySelectors
import org.junit.platform.launcher.TestExecutionListener
import org.junit.platform.launcher.TestIdentifier
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
import org.junit.platform.launcher.core.LauncherFactory
import java.io.File
import javax.inject.Inject

class TestDiscovererTask @Inject constructor(private val testClasspath: List<File>, private val outputFile: File): Runnable {
    override fun run() {
        Thread.currentThread().contextClassLoader = javaClass.classLoader
        val toSet = testClasspath.map { it.toPath() }.toSet()
        val build = LauncherDiscoveryRequestBuilder.request()
            .selectors(DiscoverySelectors.selectClasspathRoots(toSet))
            .build()
        val launcher = LauncherFactory.create()
        val testPlan = launcher.discover(build)
        val tests = testPlan.roots.flatMap { testPlan.getDescendants(it) } + testPlan.roots
        val ids = tests.filter { it.type.isTest }.filter { it.uniqueId.contains("MyTest2") }.map { DiscoverySelectors.selectUniqueId(it.uniqueId) }
        val build1 = LauncherDiscoveryRequestBuilder.request()
            .selectors(ids)
            .build()
        launcher.execute(build1, object: TestExecutionListener {
            override fun executionFinished(testIdentifier: TestIdentifier, testExecutionResult: TestExecutionResult) {
                println("TEST FINISHED: ${testIdentifier.uniqueId}, RESULT: ${testExecutionResult.status}")
            }
        })
        outputFile.writeText(tests.joinToString("\n") { it.toString() })
    }
}