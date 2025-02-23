import json
import time

from kafka import KafkaProducer


KAFKA_BROKER = 'localhost:9092'
KAFKA_TOPIC = 'test_topic'


def counter_generator():
    c = 0
    while True:
        yield c
        c += 1


def delivery_report(err, msg):
    if err:
        print(f'Message delivery failed: {err}')
    else:
        print(f'Message delivered to {msg.topic()} [{msg.partition()}]')


def produce_events():
    producer = KafkaProducer(
        bootstrap_servers=KAFKA_BROKER,
        value_serializer=lambda v: json.dumps(v).encode('utf-8')
    )

    counter = counter_generator()

    while True:
        s = input('Enter message to send via Kafka (or q to quit): ')
        if s == 'q':
            break

        event = {'id': next(counter), 'message': s, 'timestamp': time.time()}
        producer.send(
            topic=KAFKA_TOPIC,
            value=event
        )
        producer.flush()

if __name__ == '__main__':
    produce_events()
