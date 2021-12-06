package com.microservice.account.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.account.model.Account;
import com.microservice.account.service.AccountService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private AccountService service;

	@GetMapping("/all")
	public Flux<Account> findAll() {
		return service.findAll();
	}
	
	@GetMapping("/findby/{id}")
	public Mono<Account> findById(@PathVariable("id") String id) {
		return service.findById(id);
	}
	
	@PostMapping("/create")
	public Mono<ResponseEntity<Account>> createAccount(@Validated @RequestBody Account account) {
		return service.createAccount(account)
				.map(item ->
				ResponseEntity.created(URI.create("Account".concat(item.getId())))
				.contentType(MediaType.APPLICATION_JSON)
				.body(item)
						);
	}
	
	@PutMapping("/update/{id}")
	public Mono<ResponseEntity<Account>> updateAccount(@PathVariable("id") String id, @RequestBody Account account) {
		return service.updateAccount(account, id)
				.map(item ->
				ResponseEntity.created(URI.create("Account".concat(item.getId())))
				.contentType(MediaType.APPLICATION_JSON)
				.body(item)
						);
	}
	
	@DeleteMapping("/delete/{id}")
	public Mono<ResponseEntity<Void>> deleteAccount(@PathVariable("id") String id) {
		return service.deleteAccount(id).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
				.defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
	}
	
}
