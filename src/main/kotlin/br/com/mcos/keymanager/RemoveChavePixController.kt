package br.com.mcos.keymanager

import br.com.mcos.KeymanagerRemoveGrpcServiceGrpc
import br.com.mcos.RemoveChavePixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import org.slf4j.LoggerFactory
import java.util.*

@Controller("/api/v1/clientes/{clienteId}")
class RemoveChavePixController(private val removeChavePixClient: KeymanagerRemoveGrpcServiceGrpc.KeymanagerRemoveGrpcServiceBlockingStub) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Delete("/pix/{pixId}")
    fun delete(clienteId: UUID, pixId: UUID): HttpResponse<Any> {

        LOGGER.info("[$clienteId] removendo uma chave pix com $pixId")

        removeChavePixClient.remove(
            RemoveChavePixRequest.newBuilder()
                .setClienteId(clienteId.toString())
                .setPixId(pixId.toString())
                .build()
        )

        LOGGER.info("[$clienteId] removeu uma chave pix com $pixId")

        return HttpResponse.ok()
    }

}