package com.microservice.account.repo;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.microservice.account.model.TypeClient;

import reactor.core.publisher.Mono;

@Service
public class ClientWC {
	private final WebClient webclient = WebClient.create("http://localhost:8082");
	
	public Mono<TypeClient> typeClient(String id) {
		Mono<TypeClient> clientwc = webclient
										.get()
										.uri("/client/findby/" + id)
										.retrieve()
										.bodyToMono(TypeClient.class);
		return clientwc;
	}
	
	public Mono<Boolean> existClient(String id) {
		
		Mono<Boolean> clientwc = webclient
									.get()
									.uri("/client/exist/" + id)
									.retrieve()
									.bodyToMono(Boolean.class);
		return clientwc;
	}
}
