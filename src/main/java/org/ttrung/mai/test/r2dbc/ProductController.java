package org.ttrung.mai.test.r2dbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("product")
@Slf4j
public class ProductController {

	@Autowired
	private ProductRepository repo;

	@GetMapping("/{id}")
	public Mono<Product> getId(@PathVariable("id") Long id) {
		log.info("Get Product " + id);
		Mono<Product> pro = repo.findById(id);
		return pro.switchIfEmpty(Mono.error(new NotFoundException("Product Id " + id + " is Not Found")));
	}

	@GetMapping("/list")
	public Flux<Product> getList() {
		log.info("Get List Products");
		Flux<Product> pros = repo.findAll();
		return pros.switchIfEmpty(Mono.error(new NotFoundException("No Data Available")));
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotFoundException.class)
	public void handleNotFoundExceptionHandler() {

	}

}
