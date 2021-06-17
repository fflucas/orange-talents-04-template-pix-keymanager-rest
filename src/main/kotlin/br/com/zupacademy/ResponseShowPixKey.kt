package br.com.zupacademy

import java.time.LocalDateTime

data class ResponseShowPixKey(
    val id_pix: String,
    val key_type: String,
    val key_value: String,
    val id_owner: String,
    val name: String,
    val cpf: String,
    val acc_info: ResponseAccountInfo,
    val created_at: LocalDateTime,
)

class ResponseAccountInfo(
    val bank: String,
    val agency: String,
    val account: String,
    val acc_type: String,
)
