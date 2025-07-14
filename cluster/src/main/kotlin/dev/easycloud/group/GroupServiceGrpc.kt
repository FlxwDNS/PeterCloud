package dev.easycloud.group

import GroupServiceOuterClass
import io.grpc.stub.StreamObserver

class GroupServiceGrpc: GroupServiceGrpc.GroupServiceImplBase() {

    override fun sayHello(
        request: GroupServiceOuterClass.TestRequest,
        responseObserver: StreamObserver<GroupServiceOuterClass.TestReply>
    ) {
        responseObserver.onNext(GroupServiceOuterClass.TestReply.newBuilder().setMessage("hello").build())
        responseObserver.onCompleted()
    }
}