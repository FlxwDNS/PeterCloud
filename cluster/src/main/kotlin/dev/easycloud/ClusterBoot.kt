package dev.easycloud

fun main() {
    val cluster = Cluster()
    cluster.load()
    cluster.run()

    Runtime.getRuntime().addShutdownHook(Thread {
        cluster.shutdown()
    })
}