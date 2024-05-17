package com.team4.demo.util.linepay.domain;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CheckoutPaymentRequestForm {

    private BigDecimal amount;

    private String currency;

    private String orderId;

    private List<ProductPackageForm> packages;

    private RedirectUrls redirectUrls;

}
