package br.com.zupacademy.shared.grpc

import br.com.zupacademy.*
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class KeyManagerGrpcFactory(
    @GrpcChannel("keyManager") val channel: ManagedChannel
) {
    @Singleton
    fun registerKey(): RegisterKeyServiceGrpc.RegisterKeyServiceBlockingStub = RegisterKeyServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun removeKey(): RemoveKeyServiceGrpc.RemoveKeyServiceBlockingStub = RemoveKeyServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun showKey(): ShowKeyServiceGrpc.ShowKeyServiceBlockingStub = ShowKeyServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun listKey(): ListKeysServiceGrpc.ListKeysServiceBlockingStub = ListKeysServiceGrpc.newBlockingStub(channel)
}