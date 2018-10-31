import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DefaultSubscriber;

public class backpressure {

    public static void main(String[] args) {
        window();

        try {
            Thread.sleep(2000*3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
    public static void window() {
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
