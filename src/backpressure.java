import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DefaultSubscriber;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class backpressure {

    public static void main(String[] args) {
        request();
    }

    public static void buffer() {
        PublishProcessor<Integer> source = PublishProcessor.create();
        source.buffer(10)
                .observeOn(Schedulers.computation(), false, 50)
                .subscribe(list -> {
                    Thread.sleep(30);
                    System.out.println(list);
                }, Throwable::printStackTrace);

        for (int i = 0; i < 101; i++) {
            source.onNext(i);
        }
    }

    public static void request(){
        Flowable.range(1,1000).subscribe(new CusSubscribtion<>());
    }

    static class CusSubscribtion<T> implements  Subscriber<T>{
        Subscription msb;

        @Override
        public void onSubscribe(Subscription subscription) {
            msb = subscription;
            msb.request(1);
        }

        @Override
        public void onNext(T t) {
            System.out.println(t);
            msb.request(1);
        }

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void onComplete() {

        }
    }
    public static void window() {
        Flowable.just("1").subscribe(new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription subscription) {

            }

            @Override
            public void onNext(String s) {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
        PublishProcessor<Integer> source = PublishProcessor.create();
        source
                .window(120)
                .observeOn(Schedulers.computation(), false, 119)
                .subscribe(new DefaultSubscriber<Flowable<Integer>>() {
                    @Override
                    public void onNext(Flowable<Integer> integerFlowable) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        integerFlowable.subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                System.out.println(integer+"");
                            }
                        });
                        request(5);
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        for (int i = 0; i < 2000; i++) {
            source.onNext(i);
        }
    }

}
