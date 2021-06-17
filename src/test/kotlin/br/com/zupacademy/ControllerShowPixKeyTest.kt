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
import org.mockito.Mockito.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ControllerShowPixKeyTest{
    @field:Inject
    lateinit var grpcClient: ShowKeyServiceGrpc.ShowKeyServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Test
    fun `it should be possible to show details of a pix key`(){
        //cenario
        val idClient = UUID.randomUUID().toString()
        val idPix = UUID.randomUUID().toString()

        `when`(grpcClient.showKey(any()))
            .thenReturn(
                responseShowKey(idPix, idClient)
            )

        //acao
        val get = HttpRequest.GET<Any>("/api/v1/clientes/$idClient/pix/$idPix")
        val responseRest = httpClient.toBlocking().exchange(get, ResponseShowPixKey::class.java)

        //resultado
        with(responseRest){
            assertEquals(HttpStatus.OK.code, this.status.code)
            assertNotNull(this.body())
            with(this.body()!!){
                assertEquals("11122233345", this.cpf)
                assertEquals("teste@teste.com", this.key_value)
                assertEquals(KeyType.EMAIL.name, this.key_type)
                assertEquals(idPix, this.id_pix)
                assertEquals(idClient, this.id_owner)
            }
        }
    }

    private fun responseShowKey(idPix: String, idClient: String) = ResponseShowKey.newBuilder()
        .setIdPix(idPix)
        .setIdOwner(idClient)
        .setKeyType(KeyType.EMAIL)
        .setKeyValue("teste@teste.com")
        .setName("Testador do Teste Jr")
        .setCpf("11122233345")
        .setAccInfo(
            ResponseShowKey.AccountInfo.newBuilder()
                .setBank("ITAU Unibanco")
                .setAgency("0001")
                .setAccount("123456")
                .setAccType(AccountType.CONTA_CORRENTE)
                .build()
        )
        .setCreatedAt(LocalDateTime.now().let {
            val createAt = it.atZone(ZoneId.of("UTC")).toInstant()
            Timestamp.newBuilder()
                .setSeconds(createAt.epochSecond)
                .setNanos(createAt.nano)
                .build()
        })
        .build()

    @Replaces(value = ShowKeyServiceGrpc.ShowKeyServiceBlockingStub::class, factory = KeyManagerGrpcFactory::class)
    @Singleton
    fun stubTeste3Mock(): ShowKeyServiceGrpc.ShowKeyServiceBlockingStub =
        mock(ShowKeyServiceGrpc.ShowKeyServiceBlockingStub::class.java)
}