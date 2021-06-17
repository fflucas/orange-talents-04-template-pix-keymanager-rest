package br.com.zupacademy

import br.com.zupacademy.shared.grpc.KeyManagerGrpcFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ControllerListPixKeysTest{

    @field:Inject
    lateinit var clientStub: ListKeysServiceGrpc.ListKeysServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Test
    fun `it should be possible to list all client's pix keys`(){
        //cenario
        val idClient = UUID.randomUUID().toString()
        val responseGrpc = ResponseListKeys.newBuilder()
            .addAllPixKeys(responseListKeys(idClient))
            .build()

        Mockito.`when`(clientStub.listKeys(Mockito.any()))
            .thenReturn(responseGrpc)

        //acao
        val get = HttpRequest.GET<Any>("/api/v1/clientes/$idClient/pix")
        val responseRest = httpClient.toBlocking().exchange(get, List::class.java)

        //resultado
        with(responseRest){
            assertEquals(HttpStatus.OK.code, this.status.code)
            assertNotNull(this.body())
            assertEquals(2, this.body().size)
        }

    }

    private fun responseListKeys(idClient: String): List<ResponseListKeys.Key> =
        listOf(
            ResponseListKeys.Key.newBuilder()
                .setIdPix(UUID.randomUUID().toString())
                .setIdOwner(idClient)
                .setKeyType(KeyType.EMAIL)
                .setKeyValue("teste@teste.com")
                .setAccType(AccountType.CONTA_CORRENTE)
                .setCreatedAt(LocalDateTime.now().let {
                    val createAt = it.atZone(ZoneId.of("UTC")).toInstant()
                    Timestamp.newBuilder()
                        .setSeconds(createAt.epochSecond)
                        .setNanos(createAt.nano)
                        .build()
                })
            .build(),
            ResponseListKeys.Key.newBuilder()
                .setIdPix(UUID.randomUUID().toString())
                .setIdOwner(idClient)
                .setKeyType(KeyType.CPF)
                .setKeyValue("11122233345")
                .setAccType(AccountType.CONTA_CORRENTE)
                .setCreatedAt(LocalDateTime.now().let {
                    val createAt = it.atZone(ZoneId.of("UTC")).toInstant()
                    Timestamp.newBuilder()
                        .setSeconds(createAt.epochSecond)
                        .setNanos(createAt.nano)
                        .build()
                })
                .build()
        )

    @Replaces(value = ListKeysServiceGrpc.ListKeysServiceBlockingStub::class, factory = KeyManagerGrpcFactory::class)
    @Singleton
    fun stubTeste1Mock(): ListKeysServiceGrpc.ListKeysServiceBlockingStub =
        Mockito.mock(ListKeysServiceGrpc.ListKeysServiceBlockingStub::class.java)
}