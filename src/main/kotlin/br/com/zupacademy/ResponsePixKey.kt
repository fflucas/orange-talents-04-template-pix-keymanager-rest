package br.com.zupacademy

import io.micronaut.core.annotation.Introspected
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Introspected
class ResponsePixKey(key: ResponseListKeys.Key) {
    val id_pix = key.idPix
    val key_type = key.keyType.name
    val key_value = key.keyValue
    val id_owner = key.idOwner
    val acc_type = key.accType.name
    val created_at = key.createdAt.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }
}