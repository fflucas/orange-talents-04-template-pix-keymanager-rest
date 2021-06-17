package br.com.zupacademy

import br.com.zupacademy.shared.validators.ValidUUID
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.validation.constraints.NotBlank

@Controller("/api/v1/clientes/{idClient}")
@Validated
class ControllerListPixKeys(
    private val grpcClient: ListKeysServiceGrpc.ListKeysServiceBlockingStub
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Get("/pix")
    fun list(
        @PathVariable @NotBlank @ValidUUID idClient: String,
    ): HttpResponse<List<ResponsePixKey>> {
        logger.info("Buscando todas as chaves pix do cliente $idClient")

        val requestGrpc = RequestListKeys.newBuilder()
            .setIdOwner(idClient)
            .build()

        val responseGrpc: ResponseListKeys = grpcClient.listKeys(requestGrpc)

        return HttpResponse.ok(responseGrpc.toDto())
    }
}

private fun ResponseListKeys.toDto(): List<ResponsePixKey> {
    val response = mutableListOf<ResponsePixKey>()
    pixKeysList.forEach{
        response.add(ResponsePixKey(it))
    }
    return response
}
