package com.microservice.account.repo;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.microservice.account.model.Account;

import reactor.core.publisher.Mono;

@Repository
public interface AccountRepo extends ReactiveMongoRepository<Account, String> {
	@Query("{ 'idclient' : ?0, 'typeaccount' : ?1 }")
	public Mono<Account> validateAccountPersonal(String idclient, String typeaccount);
}
