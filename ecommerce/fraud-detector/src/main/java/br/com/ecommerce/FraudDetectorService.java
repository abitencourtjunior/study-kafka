package br.com.ecommerce;

import static java.lang.System.out;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import br.com.ecommerce.model.Order;

public class FraudDetectorService {

    public static void main(String[] args) throws InterruptedException {
        var fraudDetectorService = new FraudDetectorService();
        try (var service = new KafkaService<Order>(FraudDetectorService.class.getSimpleName(),
                "ECOMMERCE_NEW_ORDER", fraudDetectorService::parse, Order.class, Map.of())) {
            service.run();
        }

    }

    private void parse(ConsumerRecord<String, Order> record) {
        try {
            out.println("__________________________________");
            out.println("Processando pedidos");
            out.println(String.format("Order: %s :: %s :: %s :: %s", record.offset(), record.key(), record.value(), record.partition()));
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
