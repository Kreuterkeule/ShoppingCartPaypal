package com.kreuterkeule.ShoppingCartPaypal;

import com.kreuterkeule.ShoppingCartPaypal.controller.ApiController;
import com.kreuterkeule.ShoppingCartPaypal.controller.WebController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ShoppingCartPaypalApplicationTests {

	private ApiController apiController;
	private WebController webController;

	@Autowired
	ShoppingCartPaypalApplicationTests(ApiController apiController, WebController webController) {
		this.apiController = apiController;
		this.webController = webController;
	}

	@Test
	void contextLoads() {
		assertThat(apiController).isNotNull();
		assertThat(webController).isNotNull();
	}

}
