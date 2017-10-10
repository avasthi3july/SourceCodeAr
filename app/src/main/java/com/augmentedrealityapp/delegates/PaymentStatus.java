package com.augmentedrealityapp.delegates;

/**
 * Created by kavasthi on 9/20/2017.
 */

public interface PaymentStatus {
    void checkPayment(String orderId,String status, String amount, String des);
}
