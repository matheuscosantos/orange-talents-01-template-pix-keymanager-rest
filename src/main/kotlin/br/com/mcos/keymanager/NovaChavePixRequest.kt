package br.com.mcos.keymanager

import br.com.caelum.stella.validation.CPFValidator
import br.com.mcos.RegistraChavePixRequest
import br.com.mcos.TipoDeChave
import br.com.mcos.TipoDeConta

import io.micronaut.core.annotation.Introspected
import io.micronaut.validation.validator.constraints.EmailValidator
import java.util.*

@Introspected
class NovaChavePixRequest(
    val tipoDeConta: TipoDeContaRequest?,
    val chave: String?,
    val tipoDeChave: TipoDeChaveRequest?
) {
    fun paraModeloGrpc(clienteId: UUID): RegistraChavePixRequest {
        return RegistraChavePixRequest.newBuilder()
            .setClienteId(clienteId.toString())
            .setTipoDeConta(tipoDeConta?.atributoGrpc ?: TipoDeConta.UNKNOWN_TIPO_CONTA)
            .setTipoDeChave(tipoDeChave?.atributoGrpc ?: TipoDeChave.UNKNOWN_TIPO_CHAVE)
            .setChave(chave ?: "")
            .build()
    }
}

enum class TipoDeContaRequest(val atributoGrpc: TipoDeConta) {
    CONTA_CORRENTE(TipoDeConta.CONTA_CORRENTE),
    CONTA_POUPANCA(TipoDeConta.CONTA_POUPANCA)
}

enum class TipoDeChaveRequest(val atributoGrpc: TipoDeChave) {

    CPF(TipoDeChave.CPF) {
        override fun valida(chave: String?): Boolean {
            if (chave.isNullOrBlank()) {
                return false
            }

            return CPFValidator(false)
                .invalidMessagesFor(chave)
                .isEmpty()
        }
    },

    CELULAR(TipoDeChave.CELULAR) {
        override fun valida(chave: String?): Boolean {
            if (chave.isNullOrBlank()) {
                return false
            }
            return chave.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
        }
    },

    EMAIL(TipoDeChave.EMAIL) {
        override fun valida(chave: String?): Boolean {
            if (chave.isNullOrBlank()) {
                return false
            }
            return EmailValidator().run {
                initialize(null)
                isValid(chave, null)
            }
        }

    },

    ALEATORIA(TipoDeChave.ALEATORIA) {
        override fun valida(chave: String?) = chave.isNullOrBlank() // não deve se preenchida
    };

    abstract fun valida(chave: String?): Boolean
}