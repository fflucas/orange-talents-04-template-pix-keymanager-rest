package br.com.zupacademy

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class RequestRequestKeyTypeTest{
    
    @Nested
    inner class CPF{

        @Test
        fun `it should not be valid if the key is empty or null`(){
            with(RequestKeyType.CPF){
                assertFalse(isValidKey(null))
                assertFalse(isValidKey(""))
            }
        }

        @Test
        fun `it should not be valid if the key does not match the pattern`(){
            with(RequestKeyType.CPF){
                assertFalse(isValidKey("111222333"))
                assertFalse(isValidKey("111222333a4"))
            }
        }

        @Test
        fun `it should not be valid if the key is not a valid CPF`(){
            with(RequestKeyType.CPF){
                assertFalse(isValidKey("11122233344"))
            }
        }

        @Test
        fun `it should be valid if the key is a valid CPF`(){
            with(RequestKeyType.CPF){
                assertTrue(isValidKey("22754553029"))
            }
        }
    }

    @Nested
    inner class PHONENUMBER{

        @Test
        fun `it should not be valid if the key is empty or null`(){
            with(RequestKeyType.PHONE_NUMBER){
                assertFalse(isValidKey(null))
                assertFalse(isValidKey(""))
            }
        }

        @Test
        fun `it should not be valid if the key does not match the pattern`(){
            with(RequestKeyType.PHONE_NUMBER){
                assertFalse(isValidKey("64912345678"))
            }
        }

        @Test
        fun `it should be valid if the key has the right pattern`(){
            with(RequestKeyType.PHONE_NUMBER){
                assertTrue(isValidKey("+5564912345678"))
            }
        }
    }

    @Nested
    inner class EMAIL{

        @Test
        fun `it should not be valid if the key is empty or null`(){
            with(RequestKeyType.EMAIL){
                assertFalse(isValidKey(null))
                assertFalse(isValidKey(""))
            }
        }

        @Test
        fun `it should not be valid if the key does not have the right syntax`(){
            with(RequestKeyType.EMAIL){
                assertFalse(isValidKey("teste.teste"))
            }
        }

        @Test
        fun `it should be valid if the key has the right syntax`(){
            with(RequestKeyType.EMAIL){
                assertTrue(isValidKey("teste@teste.com"))
            }
        }
    }

    @Nested
    inner class RANDOM{

        @Test
        fun `it should not be valid if the key is not blank or null`(){
            with(RequestKeyType.RANDOM){
                assertFalse(isValidKey("128391823"))
            }
        }

        @Test
        fun `it should be valid if the key is blank or null`(){
            with(RequestKeyType.RANDOM){
                assertTrue(isValidKey(null))
                assertTrue(isValidKey(""))
            }
        }
    }
}