package gpe.services;

import core.ApiCore;
import core.services.json.JsonResponse;
import core.services.json.JsonService;
import gpe.bean.Parcourt;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

public class ListParcourt extends JsonService {

    @Override
    protected Mono<JsonResponse> handle(HttpServerRequest request, HttpServerResponse response) {

        return Mono.just(
                new JsonResponse(200,
                        Parcourt.getParcourts(ApiCore.INSTANCE.getMongo()).collectList()
                )
        );

    }

}
