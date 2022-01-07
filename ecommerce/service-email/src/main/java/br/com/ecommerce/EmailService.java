package br.com.ecommerce;

import static java.lang.System.out;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import br.com.ecommerce.KafkaService;
import br.com.ecommerce.model.Email;

public class EmailService {

    public static void main(String[] args) {
        var emailService = new EmailService();
        try (var service = new KafkaService<Email>(EmailService.class.getSimpleName(),
                "ECOMMERCE_SEND_EMAIL", emailService::parse, Email.class, Map.of())) {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, Email> record) {
        try {
            out.println("__________________________________");
            out.println("Send mail");
            out.println(String.format("Order: %s :: %s :: %s :: %s", record.offset(), record.key(), record.value(), record.partition()));
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
