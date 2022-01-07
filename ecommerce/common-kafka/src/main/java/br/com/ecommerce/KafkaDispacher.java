package br.com.ecommerce;

import static java.lang.System.out;
import static java.util.Objects.nonNull;

import java.io.Closeable;
import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

class KafkaDispacher<T> implements Closeable {

    private KafkaProducer<String, T> producer;

    public KafkaDispacher() {
        this.producer = new KafkaProducer<>(properties());
    }

    public void send(String topic, String key, T value) throws Exception {
        var record = new ProducerRecord<>(topic, key, value);
        Callback callback = (data, ex) -> {
            if (nonNull(ex)) {
                ex.printStackTrace();
                return;
            }
            out.println("Solicitação enviada com sucesso: " + data.topic() + " :: " + data.partition() + " :: " + data.offset() + " :: " + data.timestamp());
        };
        producer.send(record, callback).get();

    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GsonSerializer.class.getName());
        return properties;
    }

    @Override
    public void close() {
        producer.close();
    }

}
