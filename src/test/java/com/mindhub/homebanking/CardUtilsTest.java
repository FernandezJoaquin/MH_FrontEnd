package com.mindhub.homebanking;

import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class CardUtilsTest {
    @Test
    public void cardNumberIsCreated(){
        String cardNumber = CardUtils.getCardNumber();
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }

    @Test
    public void cardNumberLength(){
        String cardNumber = CardUtils.getCardNumber();
        assertThat(cardNumber, hasLength(19));
    }

    @Test
    public void cvvIsCreated(){
        String cardNumber = CardUtils.getCvv();
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }
    @Test
    public void cvvLength(){
        String cvv = CardUtils.getCvv();
        assertThat(cvv, hasLength(3));
    }
}
