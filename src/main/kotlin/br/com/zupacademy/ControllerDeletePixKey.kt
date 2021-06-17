package br.com.zupacademy

import br.com.zupacademy.shared.validators.ValidUUID
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.validation.constraints.NotBlank

@Controller("/api/v1/clientes/{idClient}")
@Validated
class ControllerDeletePixKey(
    private val grpcClient: RemoveKeyServiceGrpc.RemoveKeyServiceBlockingStub
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Delete("/pix/{idPix}")
    fun delete(
        @PathVariable @NotBlank @ValidUUID idClient: String,
        @PathVariable @NotBlank @ValidUUID idPix: String,
    ): HttpResponse<Any>{
        logger.info("Solicitação para remoção de chave pix $idPix do cliente $idClient")

        val requestGrpc = RequestDeleteKey.newBuilder()
            .setIdOwner(idClient)
            .setIdPix(idPix)
            .build()

        grpcClient.deleteKey(requestGrpc)

        logger.info("Remoção de chave pix $idPix do cliente $idClient bem sucedida")
        return HttpResponse.ok()
    }
}