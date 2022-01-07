package br.com.ecommerce;

import static java.lang.System.out;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.Uuid;
import org.apache.kafka.common.serialization.StringDeserializer;

class KafkaService<T> implements Closeable {

    private final KafkaConsumer<String, T> consumer;
    private final ConsumerFunction<T> parse;
    private final Class<T> type;

    KafkaService(String groupId, String topic, ConsumerFunction parse, Class<T> type, Map<String, String> overrideProperties) {
        this(parse, groupId, type, overrideProperties);
        this.consumer.subscribe(Collections.singletonList(topic));
    }

    KafkaService(String groupId, Pattern topic, ConsumerFunction parse, Class<T> type, Map<String, String> overrideProperties) {
        this(parse, groupId, type, overrideProperties);
        this.consumer.subscribe(topic);
    }

    private KafkaService(ConsumerFunction parse, String groupId, Class<T> type, Map<String, String> overrideProperties) {
        this.type = type;
        this.parse = parse;
        this.consumer = new KafkaConsumer<>(properties(type, groupId, overrideProperties));
    }

    void run() {
        while (true) {
            var records = consumer.poll(Duration.ofMillis(100));
            if (!records.isEmpty()) {
                out.println("Found " + records.count() + " records!");
                for (var record : records) {
                    parse.consume(record);
                }
            }
        }
    }

    private Properties properties(Class<T> type, String groupId, Map<String, String> overrideProperties) {
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:29092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializar.class.getName());
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, Uuid.randomUuid().toString());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(GsonDeserializar.TYPE_CONFIG, type.getName());
        properties.putAll(overrideProperties);
        return properties;
    }

    @Override
    public void close() {
        consumer.close();
    }

}
