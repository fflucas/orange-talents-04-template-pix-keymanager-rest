package br.com.zupacademy.shared.errors

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.JsonError
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class GlobalExceptionHandlerTest{

    private val genericRequest = HttpRequest.GET<Any>("/")

    @Test
    fun `it should return HTTP NOT_FOUND when StatusRuntimeException is NOT_FOUND`(){
        //cenario
        val message = "não encontrado"
        val notFoundException = StatusRuntimeException(Status.NOT_FOUND.withDescription(message))

        //acao
        val handle = GlobalExceptionHandler().handle(genericRequest, notFoundException)

        //resultado
        with(handle){
            assertEquals(HttpStatus.NOT_FOUND.code, this.status.code)
            assertNotNull(this.body())
            assertEquals(message, (this.body() as JsonError).message)
        }
    }

    @Test
    fun `it should return HTTP BAD_REQUEST when StatusRuntimeException is INVALID_ARGUMENT`(){
        //cenario
        val exception = StatusRuntimeException(Status.INVALID_ARGUMENT)

        //acao
        val handle = GlobalExceptionHandler().handle(genericRequest, exception)

        //resultado
        with(handle){
            assertEquals(HttpStatus.BAD_REQUEST.code, this.status.code)
            assertNotNull(this.body())
            assertEquals("Dados da requisição estão inválidos", (this.body() as JsonError).message)
        }
    }

    @Test
    fun `it should return HTTP UNPROCESSABLE_ENTITY when StatusRuntimeException is ALREADY_EXISTS`(){
        //cenario
        val message = "não pode ser nulo ou em branco"
        val exception = StatusRuntimeException(Status.ALREADY_EXISTS.withDescription(message))

        //acao
        val handle = GlobalExceptionHandler().handle(genericRequest, exception)

        //resultado
        with(handle){
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.code, this.status.code)
            assertNotNull(this.body())
            assertEquals(message, (this.body() as JsonError).message)
        }
    }

    @Test
    fun `it should return HTTP BAD_REQUEST when StatusRuntimeException is FAILED_PRECONDITION`(){
        //cenario
        val message = "cliente não encontrado no ERP Itau"
        val exception = StatusRuntimeException(Status.FAILED_PRECONDITION.withDescription(message))

        //acao
        val handle = GlobalExceptionHandler().handle(genericRequest, exception)

        //resultado
        with(handle){
            assertEquals(HttpStatus.BAD_REQUEST.code, this.status.code)
            assertNotNull(this.body())
            assertEquals(message, (this.body() as JsonError).message)
        }
    }

    @Test
    fun `it should return HTTP BAD_REQUEST when StatusRuntimeException is OUT_OF_RANGE`(){
        //cenario
        val message = "cliente BCB não respondeu a solicitação"
        val exception = StatusRuntimeException(Status.OUT_OF_RANGE.withDescription(message))

        //acao
        val handle = GlobalExceptionHandler().handle(genericRequest, exception)

        //resultado
        with(handle){
            assertEquals(HttpStatus.BAD_REQUEST.code, this.status.code)
            assertNotNull(this.body())
            assertEquals(message, (this.body() as JsonError).message)
        }
    }

    @Test
    fun `it should return HTTP INTERNAL_SERVER_ERROR when StatusRuntimeException is not mapped`(){
        //cenario
        val message = "erro em algo"
        val exception = StatusRuntimeException(Status.UNKNOWN.withDescription(message))

        //acao
        val handle = GlobalExceptionHandler().handle(genericRequest, exception)

        //resultado
        with(handle){
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.code, this.status.code)
            assertNotNull(this.body())
            assertEquals(
                "Nao foi possível completar a requisição devido ao erro: $message (${Status.UNKNOWN.code})",
                (this.body() as JsonError).message
            )
        }
    }
}