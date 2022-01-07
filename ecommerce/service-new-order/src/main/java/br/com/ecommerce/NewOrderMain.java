package br.com.ecommerce;

import java.math.BigDecimal;

import org.apache.kafka.common.Uuid;

import br.com.ecommerce.model.Email;
import br.com.ecommerce.model.Order;

public class NewOrderMain {

    public static void main(String[] args) throws Exception {
        try (var orderDispacher = new KafkaDispacher<Order>()) {
            try (var emailDispacher = new KafkaDispacher<Email>()) {
                for (var pointer = 0; pointer < 10; pointer++) {

                    var userId = Uuid.randomUuid().toString();
                    var amount = new BigDecimal(Math.random() + 5000 + 1);
                    var order = new Order("Jose", userId, amount);
                    orderDispacher.send("ECOMMERCE_NEW_ORDER", userId, order);

                    var body = "Thanks Jose!";
                    Email email = new Email(userId, body);
                    emailDispacher.send("ECOMMERCE_SEND_EMAIL", userId, email);
                }
            }
        }
    }

}
