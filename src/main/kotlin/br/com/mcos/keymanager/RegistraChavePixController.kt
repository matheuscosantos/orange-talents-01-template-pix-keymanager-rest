package br.com.mcos.keymanager

import br.com.mcos.KeymanagerRegistraGrpcServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*
import javax.validation.Valid

@Validated
@Controller("/api/v1/clientes/{clienteId}")
// BlockingStub é uma classe gerado pelo protobuff análogo ao client http
// Para que o Micronaut saiba injetar é necessário criar a Factory de Keymanager
class RegistraChavePixController(private val registraChavePixClient: KeymanagerRegistraGrpcServiceGrpc.KeymanagerRegistraGrpcServiceBlockingStub) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Post("/pix")
    fun create(
        clienteId: UUID,
        @Valid @Body request: NovaChavePixRequest
    ): HttpResponse<Any> {
        LOGGER.info("[$clienteId] criando uma nova chave pix com $request")
        val grpcResponse = registraChavePixClient.registra(request.paraModeloGrpc(clienteId))

        return HttpResponse.created(location(clienteId, grpcResponse.pixId))
    }

    private fun location(clienteId: UUID, pixId: String) = HttpResponse.uri("/api/v1/clientes/$clienteId/pix/${pixId}")

}