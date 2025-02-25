'''
Script that produces Kafka events for testing the Go notification service.
'''


import json
import time

from kafka import KafkaProducer


KAFKA_BROKER = 'localhost:9093'

KAFKA_TOPIC = 'test_topic'


def counter_generator():
    '''
    Counter generator. Initialize with

    ```
    counter = counter_generator()
    ```

    Get next value of the counter with

    ```
    next(counter)
    ```
    '''

    c = 0
    while True:
        yield c
        c += 1


def produce_events():
    '''
    Continually prompts the user for messages, then sends them as Kafka events.
    '''

    # Create Kafka producer
    producer = KafkaProducer(
        bootstrap_servers=KAFKA_BROKER,
        value_serializer=lambda v: json.dumps(v).encode('utf-8')
    )

    counter = counter_generator()

    while True:
        # Get input from user
        s = input('Enter message to send via Kafka (or q to quit): ')
        if s == 'q':
            break

        # Send user's input as Kafka message
        event = {'id': next(counter), 'message': s, 'timestamp': time.time()}
        producer.send(
            topic=KAFKA_TOPIC,
            value=event
        )
        producer.flush()


if __name__ == '__main__':
    produce_events()
