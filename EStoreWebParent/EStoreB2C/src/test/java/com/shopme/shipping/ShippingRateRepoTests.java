package com.shopme.shipping;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShippingRateRepoTests {

    @Autowired
    private ShippingRateRepository repo;

    @Test
    public void testFindByCountryAndState() {
        Country usa = new Country(234);
        String state = "New York";
        ShippingRate shippingRate = repo.findByCountryAndState(usa, state);

        assertThat(shippingRate).isNotNull();
        System.out.println(shippingRate);
    }
}