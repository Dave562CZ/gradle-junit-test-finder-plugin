package cz.richter.david.distributedTestRunner

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.workers.IsolationMode
import org.gradle.workers.WorkerExecutor
import java.io.File
import javax.inject.Inject
import kotlin.properties.Delegates

open class TestDiscoverer @Inject constructor(private val workerExecutor: WorkerExecutor): DefaultTask() {

    @get:Classpath
    var testClasspath: FileCollection by Delegates.notNull()

    @get:OutputFile
    var outputFile: File by Delegates.notNull()

    @TaskAction
    fun discoverTests() {
        workerExecutor.submit(TestDiscovererTask::class.java) {
            it.isolationMode = IsolationMode.CLASSLOADER
            it.classpath = project.configurations.getAt("distributedTestRunner") + testClasspath
            it.params(testClasspath.toList(), outputFile)
        }
    }
}


