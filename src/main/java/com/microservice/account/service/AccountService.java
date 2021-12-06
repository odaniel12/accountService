package com.microservice.account.service;

import com.microservice.account.model.Account;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {
	
	public Flux<Account> findAll();
	public Mono<Account> findById(String id);
	public Mono<Account> createAccount(Account account);
	public Mono<Account> updateAccount(Account account, String id);
	public Mono<Void> deleteAccount(String id);
	
}
