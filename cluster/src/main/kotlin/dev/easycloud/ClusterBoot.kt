package dev.easycloud

import org.apache.log4j.BasicConfigurator

fun main() {
    BasicConfigurator.configure()

    val cluster = Cluster()
    cluster.load()
    cluster.run()
}