package br.com.ecommerce;

import static java.lang.System.out;

import java.util.Map;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

public class LogService {

    public static void main(String[] args) {
        var logService = new LogService();
        try (var service = new KafkaService<String>(LogService.class.getSimpleName(),
                Pattern.compile("ECOMMERCE.*"),
                logService::parse,
                String.class,
                Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()))) {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, String> record) {
        try {
            out.println("__________________________________");
            out.println(String.format("Log %s: %s :: %s :: %s :: %s", record.topic(), record.offset(), record.key(), record.value(), record.partition()));
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
