package com.shopme.checkout.paypal;

import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class PayPalApiTests {
    private static final String BASE_URL = "https://api.sandbox.paypal.com";
    private static final String GET_ORDER_API = "/v2/checkout/orders/";
    private static final String CLIENT_ID = "Acecx6577uvnqK_dL3KhS9VvNHI3fNioe0UCXeFQRT-y--4Ww0Xnvya5jpjiNPmU5UZdONnsHMRT9TG6";
    private static final String CLIENT_SECRET = "ED5UGoDefu3S15VBQsvJSnPfHdqchCdoxqz8ExrPHngCHP803MzuK0vNQAWu4zcuYR-m5DGpMwqY_hJx";

    @Test
    public void testGetOrderDetails() {
        String orderId = "6KL74007BT736281V";
        String requestURL = BASE_URL + GET_ORDER_API + orderId;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Accept-Language", "en_US");
        headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

//        ResponseEntity<String> responseString = restTemplate.exchange(
//                requestURL, HttpMethod.GET, request, String.class);
//        System.out.println(responseString);

        ResponseEntity<PayPalOrderResponse> response = restTemplate.exchange(
                requestURL, HttpMethod.GET, request, PayPalOrderResponse.class);

        PayPalOrderResponse orderResponse = response.getBody();

        System.out.println("Order ID: " + orderResponse.getId());
        System.out.println("Validated: " + orderResponse.validate(orderId));

    }
}
