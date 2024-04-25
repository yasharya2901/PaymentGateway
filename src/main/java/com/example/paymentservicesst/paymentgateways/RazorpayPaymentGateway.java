package com.example.paymentservicesst.paymentgateways;

import com.razorpay.PaymentLink;
import org.json.JSONObject;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RazorpayPaymentGateway implements PaymentGateway {
    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    @Override
    public String generatePaymentLink(Long orderId, String email) throws RazorpayException {
        RazorpayClient razorpay = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
        JSONObject paymentLinkRequest = new JSONObject();

        //43.56 -> 43.56 * 100 => 4356
        paymentLinkRequest.put("amount",1000);
        paymentLinkRequest.put("currency","INR");

        paymentLinkRequest.put("expire_by",1714045778);
        paymentLinkRequest.put("reference_id", "15423456");
        paymentLinkRequest.put("description","Sample payment link for SST");

        JSONObject customer = new JSONObject();
        customer.put("name","+919000090000");
        customer.put("contact","Deepak Kasera");
        customer.put("email", email);
        
        paymentLinkRequest.put("customer",customer);
        
        JSONObject notify = new JSONObject();
        notify.put("sms",true);
        notify.put("email",true);
        
        paymentLinkRequest.put("notify",notify);
        paymentLinkRequest.put("reminder_enable",true);
//        JSONObject notes = new JSONObject();
//        notes.put("policy_name","");
//        paymentLinkRequest.put("notes",notes);
        paymentLinkRequest.put("callback_url","https://scaler.com/");
        paymentLinkRequest.put("callback_method","get");

        PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);
        return payment.toString();
    }
}