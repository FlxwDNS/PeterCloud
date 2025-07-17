package dev.easycloud.grpc

import dev.easycloud.Cluster
import dev.easycloud.logger
import io.grpc.Server
import io.grpc.ServerBuilder

class GrpcService {
    lateinit var server: Server

    fun run(cluster: Cluster) {
        logger.debug("debug.grpcStarting", cluster.clusterYaml.network.port)

        server = ServerBuilder.forPort(cluster.clusterYaml.network.port).build()

        server.start()
        logger.info("grpc.running", cluster.clusterYaml.network.port)
    }

    fun shutdown() {
        server.shutdown()
        logger.info("grpc.shuttingDown")
    }
}