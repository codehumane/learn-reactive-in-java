package concept;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by codehumane on 12/18/16.
 */
@Slf4j
public class ReactivePubSub {

    public static void main(String[] args) {

        Publisher<Integer> publisher = new Publisher<Integer>() {

            private final Iterator<Integer> iterator = Stream.iterate(1, x -> x + 1)
                    .limit(100).map(Integer::valueOf).collect(Collectors.toList())
                    .iterator();

            private int index = 0;

            @Override
            public void subscribe(Subscriber subscriber) {
                subscriber.onSubscribe(new Subscription() {

                    @Override
                    public void request(long n) {
                        Stream.iterate(1, x -> x + 1).limit(n).forEach(x -> {
                            if (iterator.hasNext()) {
                                subscriber.onNext(iterator.next());
                            }
                            else {
                                subscriber.onComplete();
                            }
                        });
                    }

                    @Override
                    public void cancel() {
                        // TODO
                    }
                });
            }
        };

        Subscriber<Integer> subscriber = new Subscriber<Integer>() {

            private Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                log.info("Publisher subscribed. {}", subscription);
                this.subscription = subscription;
                this.subscription.request(1);
            }

            @Override
            public void onNext(Integer integer) {
                log.info("Data notification sent by the Publisher. {}", integer);
                this.subscription.request(1);
            }

            @Override
            public void onError(Throwable t) {
                log.info("Failed terminal state. {}", t);
            }

            @Override
            public void onComplete() {
                log.info("Successful terminal state.");
            }
        };

        // TODO 왜 publisher가 subscriber를 알아야 하는가?
        publisher.subscribe(subscriber);
    }
}
