package br.com.zupacademy

import br.com.zupacademy.shared.validators.ValidUUID
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.validation.constraints.NotBlank

@Controller("/api/v1/clientes/{idClient}")
@Validated
class ControllerShowPixKey(
    private val grpcClient: ShowKeyServiceGrpc.ShowKeyServiceBlockingStub
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Get("/pix/{idPix}")
    fun show(
        @PathVariable @NotBlank @ValidUUID idClient: String,
        @PathVariable @NotBlank @ValidUUID idPix: String,
    ): HttpResponse<ResponseShowPixKey> {
        logger.info("Buscando detalhes da chave pix $idPix do cliente $idClient")

        val requestGrpc = RequestShowKey.newBuilder()
            .setPixId(RequestShowKey.FilterByPixId.newBuilder()
                                    .setIdOwner(idClient)
                                    .setIdPix(idPix)
                                    .build()
            )
            .build()

        val responseGrpc: ResponseShowKey = grpcClient.showKey(requestGrpc)

        return HttpResponse.ok(responseGrpc.toDto())
    }
}

private fun ResponseShowKey.toDto(): ResponseShowPixKey {
    return ResponseShowPixKey(
        id_pix = idPix,
        key_type = keyType.name,
        key_value = keyValue,
        id_owner = idOwner,
        name = name,
        cpf = cpf,
        acc_info = with(accInfo){
            ResponseAccountInfo(
                bank = bank,
                agency = agency,
                account = account,
                acc_type = accType.name
            )
        },
        created_at = createdAt.let{
            LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
        }
    )
}

