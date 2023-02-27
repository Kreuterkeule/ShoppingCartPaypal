package com.kreuterkeule.ShoppingCartPaypal.config;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ApplicationConfig {

    // Load PayPal values from application.properties
    @Value("${paypal.client.id}")
    private String _clientId;

    @Value("${paypal.client.secret}")
    private String _clientSecret;

    @Value("${paypal.mode}")
    private String _mode;

    @Bean
    public Map<String, String> paypalSdkConfig() {

        Map<String, String> configMap = new HashMap<>();
        configMap.put("mode", _mode);

        return configMap;

    }

    @Bean
    public OAuthTokenCredential oAuthTokenCredential() {

        return new OAuthTokenCredential(_clientId, _clientSecret, paypalSdkConfig());

    }

    @Bean
    public APIContext apiContext() throws PayPalRESTException {

        APIContext context = new APIContext(oAuthTokenCredential().getAccessToken());
        context.setConfigurationMap(paypalSdkConfig());

        return context;

    }
}
