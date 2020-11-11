package cz.richter.david.distributedTestRunner

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author d.richter
 * @since 21.02.2019
 */
class DistributedTestRunnerPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        val distributedTestRunner = target.configurations.create("distributedTestRunner")
            .setVisible(false)
        target.repositories.mavenCentral()//todo if not defined
        distributedTestRunner.defaultDependencies {
            it.add(target.dependencies.create("org.junit.jupiter:junit-jupiter-engine:5.3.1"))
            it.add(target.dependencies.create("org.junit.platform:junit-platform-launcher:1.3.1"))
        }
    }
}

