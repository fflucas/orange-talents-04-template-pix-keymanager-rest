package br.com.zupacademy

import br.com.zupacademy.shared.grpc.KeyManagerGrpcFactory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ControllerRegisterPixKeyTest{

    @field:Inject
    lateinit var clientStub: RegisterKeyServiceGrpc.RegisterKeyServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun `it should be possible to register a new pix key`(){
        //cenario
        val idPix = UUID.randomUUID().toString()
        val idClient = UUID.randomUUID().toString()

        val responseGrpc = ResponseNewKey.newBuilder()
            .setPixId(idPix)
            .build()

        `when`(clientStub.registerKey(Mockito.any()))
            .thenReturn(responseGrpc)

        val requestRest = RequestNewPixKey(
            accType = RequestAccType.CONTA_CORRENTE,
            keyType = RequestKeyType.EMAIL,
            key = "teste@teste.com"
        )

        //acao
        //prepara uma requisição http do tipo post com o body
        val post = HttpRequest.POST("/api/v1/clientes/$idClient/pix", requestRest)

        val responseRest = client.toBlocking().exchange(post, RequestNewPixKey::class.java)

        //resultado
        with(responseRest){
            assertEquals(HttpStatus.CREATED.code, this.status.code)
            assertTrue(this.headers.contains("Location"))
            assertTrue(this.header("Location")!!.contains(idPix))
        }
    }

    @Replaces(value = RegisterKeyServiceGrpc.RegisterKeyServiceBlockingStub::class, factory = KeyManagerGrpcFactory::class)
    @Singleton
    fun stubTeste2Mock(): RegisterKeyServiceGrpc.RegisterKeyServiceBlockingStub =
        Mockito.mock(RegisterKeyServiceGrpc.RegisterKeyServiceBlockingStub::class.java)
}