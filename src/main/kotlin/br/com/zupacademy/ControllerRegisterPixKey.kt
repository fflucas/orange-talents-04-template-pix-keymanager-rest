package br.com.zupacademy

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.validation.Valid

@Validated
@Controller(value = "/api/v1/clientes/{idClient}")
class ControllerRegisterPixKey(
    private val registerKeyServiceGrpc: RegisterKeyServiceGrpc.RegisterKeyServiceBlockingStub
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Post(value = "/pix")
    fun create(
        @PathVariable idClient: String,
        @Valid @Body request: RequestNewPixKey
    ): HttpResponse<Any>{
        logger.info("Solicitação de nova chave para o cliente $idClient")

        val grpcResponse = with(request.toGrpcRequestNewKey(idClient)){
            registerKeyServiceGrpc.registerKey(this)
        }

        return HttpResponse.created(
            HttpResponse.uri("/api/v1/clientes/$idClient/pix/${grpcResponse.pixId}")
        )
    }
}