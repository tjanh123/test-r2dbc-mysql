package org.ttrung.mai.test.r2dbc;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebFluxTest(controllers = { ProductController.class })
class ProductApplicationTests {
	
	@Autowired
	WebTestClient webTestClient;
	 
	@MockBean
	private ProductRepository productRepoMock;
	
	@BeforeEach
	public void before() {
		List<Product> products = new ArrayList<>();
		products.add(new Product(1L, "Bleu Chanel", "Men", "EauToilette"));
		products.add(new Product(2L, "Dior J'adore", "Women", "EauParfum"));
		
		when(productRepoMock.findAll()).thenReturn(Flux.fromIterable(products));
		when(productRepoMock.findById(1L)).thenReturn(Mono.just(products.get(0)));
		when(productRepoMock.findById(2L)).thenReturn(Mono.just(products.get(1)));
		when(productRepoMock.findById((long) Integer.MAX_VALUE)).thenReturn(Mono.empty());
	}
	
	@Test
	public void should_return_product_when_requesting_id() {
		webTestClient
	        .get()
	        .uri("/product/1")
	        .exchange()
	        .expectStatus().isOk()
	        .expectBody()
	        	.jsonPath("$.label").isEqualTo("Bleu Chanel")
	        	.jsonPath("$.gender").isEqualTo("Men");
	}
	
	@Test
	public void should_return_list_product() {
		List<Product> expected = new ArrayList<>();
		expected.add(new Product(1L, "Bleu Chanel", "Men", "EauToilette"));
		expected.add(new Product(2L, "Dior J'adore", "Women", "EauParfum"));
		
		webTestClient
	        .get()
	        .uri("/product/list")
	        .exchange()
	        .expectStatus().isOk()
	        .expectBodyList(Product.class)
	        	.hasSize(2)
	        	.isEqualTo(expected);
	}
	
	@Test
	public void shoudld_return_not_found_when_id_is_max_int() {
		webTestClient
			.get()
			.uri("/product/" + Integer.MAX_VALUE)
			.exchange()
			.expectStatus().isNotFound()
			.expectBody()
				.isEmpty();
	}

}
