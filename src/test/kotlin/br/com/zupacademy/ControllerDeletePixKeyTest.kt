package br.com.zupacademy

import br.com.zupacademy.shared.grpc.KeyManagerGrpcFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ControllerDeletePixKeyTest{

    @field:Inject
    lateinit var clientStub: RemoveKeyServiceGrpc.RemoveKeyServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Test
    fun `it should be possible to delete a pix key`(){
        //cenario
        val idClient = UUID.randomUUID().toString()
        val idPix = UUID.randomUUID().toString()

        `when`(clientStub.deleteKey(Mockito.any()))
            .thenReturn(ResponseDeleteKey.newBuilder()
                .setMessage("Chave removida com sucesso")
                .build()
            )

        //acao
        val delete = HttpRequest.DELETE<Any>("/api/v1/clientes/$idClient/pix/$idPix")
        val responseRest = httpClient.toBlocking().exchange(delete, Any::class.java)

        //resultado
        with(responseRest){
            assertEquals(HttpStatus.OK.code, this.status.code)
        }
    }

    // substitui o stub apenas do registerKey
    @Replaces(value = RemoveKeyServiceGrpc.RemoveKeyServiceBlockingStub::class, factory = KeyManagerGrpcFactory::class)
    @Singleton
    fun stubTeste0Mock(): RemoveKeyServiceGrpc.RemoveKeyServiceBlockingStub =
        Mockito.mock(RemoveKeyServiceGrpc.RemoveKeyServiceBlockingStub::class.java)
}