package dev.easycloud.grpc

import dev.easycloud.Cluster
import dev.easycloud.logger
import io.grpc.Server
import io.grpc.ServerBuilder

class GrpcService {
    lateinit var server: Server

    fun run(cluster: Cluster) {
        logger.debug("debug.grpcStarting", cluster.clusterToml.network.port)

        server = ServerBuilder.forPort(cluster.clusterToml.network.port).build()

        server.start()
        logger.info("grpc.running", cluster.clusterToml.network.port)
    }

    fun shutdown() {
        server.shutdown()
        logger.info("grpc.shuttingDown")
    }
}