package com.microservice.account.service.impl;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservice.account.model.Account;
import com.microservice.account.model.TypeClient;
import com.microservice.account.repo.AccountRepo;
import com.microservice.account.repo.ClientWC;
import com.microservice.account.service.AccountService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	private AccountRepo repo;
	
	@Autowired
	private ClientWC clientwc;
	
	@Override
	public Flux<Account> findAll() {
		return repo.findAll();
	}
	
	@Override
	public Mono<Account> findById(String id) {
		return repo.findById(id);
	}
	
	public Mono<Boolean> validateClientPersonal(String dni, String typeaccount) {
		return repo.validateAccountPersonal(dni, typeaccount).flatMap(item -> {
			return repo.existsById(item.getId());
		}).switchIfEmpty(Mono.defer(() -> Mono.fromFuture(CompletableFuture.supplyAsync(() -> {
			return false;
		}))));
	}
	
	public Boolean validateClientEmpresarial(String typeaccount) {
		if(typeaccount.equals("AHORROS") || typeaccount.equals("PLAZO FIJO")) {
			return false;
		}else {
			return true;
		}
	}
	
	@Override
	public Mono<Account> createAccount(Account account) {
		
		Mono<Boolean> resp = clientwc.existClient(account.getIdclient().toString());
		
		Mono<TypeClient> typeclient = clientwc.typeClient(account.getIdclient().toString());
		/*switch (key) {
		case value:
			
			break;

		default:
			break;
		}*/
		return resp.flatMap(item -> {
			
			try {
				if(item) {
					return typeclient.flatMap(typecli -> {
						
						if(typecli.getTypeclient().equals("PERSONAL")) {
							
							return validateClientPersonal(account.getIdclient(), account.getTypeaccount())
									.flatMap(personal -> {
										if(!personal) {
											return repo.save(account);
										}else {
											return Mono.error(new Exception("El tipo de cliente Personal solo puede "
													+ "tener una cuenta de ahorro, corriente o plazo fijo"));
										}
									});
						}
						
						if(typecli.getTypeclient().equals("EMPRESARIAL")) {
							if(validateClientEmpresarial(account.getTypeaccount())) {
								return repo.save(account);
							}else {
								return Mono.error(new Exception("Las tipo de cuenta Empresarial solo puede tener"
										+ " cuentas Corrientes"));
							}
						}
						
						return Mono.error(new Exception("Algo salio mal"));	
							
					});
				}
				else {
					return Mono.error(new Exception("No existe el cliente"));
				}
			} catch (Exception e) {
				return Mono.error(e);
			}
			
		});
		
		
	}
	
	@Override
	public Mono<Account> updateAccount(Account account, String id) {
		return repo.findById(id).flatMap(acc -> {
			acc.setTypeaccount(account.getTypeaccount());
			return repo.save(acc);
		}).switchIfEmpty(Mono.empty());
	}
	
	@Override
	public Mono<Void> deleteAccount(String id) {
		try {
			return repo.findById(id).flatMap(acc -> {
				return repo.delete(acc);
			});
		} catch (Exception e) {
			// TODO: handle exception
			return Mono.error(e);
		}
	}
	
	public Mono<Boolean> existeCliente(String id) {
		return repo.existsById(id);
	}
	
}
