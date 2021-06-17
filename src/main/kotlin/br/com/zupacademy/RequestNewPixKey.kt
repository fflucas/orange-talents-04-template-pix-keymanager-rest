package br.com.zupacademy

import br.com.zupacademy.shared.validators.ValidPixKey
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ValidPixKey
data class RequestNewPixKey(
    @field:NotNull val accType: RequestAccType?,
    @field:Size(max=77) val key: String?,
    @field:NotNull val keyType: RequestKeyType?
) {
    fun toGrpcRequestNewKey(idClient: String): RequestNewKey{
        return RequestNewKey.newBuilder()
            .setIdOwner(idClient)
            .setKeyType(keyType?.grpcKeyType ?: KeyType.UNKNOWN_KEY_TYPE)
            .setKeyValue(key ?: "")
            .setAccType(accType?.grpcAccType ?: AccountType.UNKNOWN_ACC_TYPE)
            .build()
    }
}

enum class RequestKeyType(val grpcKeyType: KeyType){
    CPF(KeyType.CPF){
        override fun isValidKey(key: String?): Boolean {
            if(key.isNullOrBlank()) return false

            if(!key.matches("^[0-9]{11}\$".toRegex())) return false

            return CPFValidator().run {
                initialize(null)
                isValid(key, null)
            }
        }
    },
    PHONE_NUMBER(KeyType.PHONE_NUMBER){
        override fun isValidKey(key: String?): Boolean {
            if(key.isNullOrBlank()) return false

            return key.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
        }
    },
    EMAIL(KeyType.EMAIL){
        override fun isValidKey(key: String?): Boolean {
            if(key.isNullOrBlank()) return false

            return EmailValidator().run {
                initialize(null)
                isValid(key, null)
            }
        }
    },
    RANDOM(KeyType.RANDOM){
        override fun isValidKey(key: String?): Boolean {
            return key.isNullOrBlank()
        }
    };

    // abstract method to override with properly validations
    abstract fun isValidKey(key: String?): Boolean
}

enum class RequestAccType(val grpcAccType: AccountType) {
    CONTA_CORRENTE(AccountType.CONTA_CORRENTE),
    CONTA_POUPANCA(AccountType.CONTA_POUPANCA);
}
