package dev.easycloud.grpc

import dev.easycloud.configuration
import dev.easycloud.logger
import io.grpc.Server
import io.grpc.ServerBuilder

class GrpcService {
    lateinit var server: Server

    fun run() {
        logger.debug("debug.grpcStarting", configuration.network.port)

        server = ServerBuilder.forPort(configuration.network.port).build()

        server.start()
        logger.info("grpc.running", configuration.network.port)
    }

    fun shutdown() {
        server.shutdown()
        logger.info("grpc.shuttingDown")
    }
}