package com.kreuterkeule.ShoppingCartPaypal;

import com.kreuterkeule.ShoppingCartPaypal.controller.ProductDatabaseApiController;
import com.kreuterkeule.ShoppingCartPaypal.controller.WebController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ShoppingCartPaypalApplicationTests {

	private ProductDatabaseApiController productDatabaseApiController;
	private WebController webController;

	@Autowired
	ShoppingCartPaypalApplicationTests(ProductDatabaseApiController productDatabaseApiController, WebController webController) {
		this.productDatabaseApiController = productDatabaseApiController;
		this.webController = webController;
	}

	@Test
	void contextLoads() {
		assertThat(productDatabaseApiController).isNotNull();
		assertThat(webController).isNotNull();
	}

}
