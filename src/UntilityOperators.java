import io.reactivex.*;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.Timed;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class UntilityOperators {


    public static void main(String[] args){
        try {
            timestamp();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void materiallize(){
        Observable.range(1,100).materialize().toList().subscribe(new Consumer<List<Notification<Integer>>>() {
            @Override
            public void accept(List<Notification<Integer>> notifications) throws Exception {
                System.out.println(notifications);
            }
        });
    }
    public static void timestamp() throws InterruptedException {
        Flowable.range(1,100).timestamp().parallel().subscribe(new Subscriber[]{new Subscriber() {
            @Override
            public void onSubscribe(Subscription subscription) {

            }

            @Override
            public void onNext(Object o) {
                System.out.println(o+"");
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {
                System.out.println("finished");
            }
        }});
        Thread.sleep(10*1000);
    }
    public static void cache(){
        Observable source = Observable.range(1,100).cache();
        source.take(30).subscribe(System.out::println);

    }


    public static void single(){
        Observable.range(1,100).single(5).subscribe(System.out::println, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println(throwable.getMessage());
            }
        });
    }
    public static void using(){
       Observable.using(new Callable<Integer>() {
                            @Override
                            public Integer call() throws Exception {
                                return 3;
                            }
                        }
               , new Function<Integer, ObservableSource<?>>() {
                   @Override
                   public ObservableSource<?> apply(Integer integer) throws Exception {
                       return Observable.just(3);
                   }
               }, new Consumer<Integer>() {
                   @Override
                   public void accept(Integer integer) throws Exception {
                       System.out.println(integer+"");
                   }
               }).subscribe(new Consumer<Object>() {
           @Override
           public void accept(Object o) throws Exception {
               System.out.println(o+"");
           }
       });
    }

}
