import io.reactivex.*;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.schedulers.TestScheduler;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RxTransforming {

    public static void main(String[] args) {
        groupBy();
    }

    public static void map() {

        Observable.range(1, 5).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return "num:" + integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println(s);
            }
        });

    }

    public static void flatMap() {
        final List<String> items = Arrays.asList("a", "b", "c", "d", "e", "f");

        final TestScheduler scheduler = new TestScheduler();

        Observable.fromIterable(items)
                .flatMap(s -> {
                    Util.logCurrentThreadName();
                    final int delay = new Random().nextInt(10);
                    return Observable.just(s + "x")
                            .delay(delay, TimeUnit.SECONDS, scheduler);
                })
                .doOnNext(System.out::println)
                .subscribe();

        Util.logCurrentThreadName();
        scheduler.advanceTimeBy(1, TimeUnit.MINUTES);
    }

    public static void switchMap() {
        final List<String> items = Arrays.asList("a", "b", "c", "d", "e", "f");
        final TestScheduler scheduler = new TestScheduler();

        Observable.fromIterable(items)
                .switchMap(s -> {
                    Util.logCurrentThreadName();
                    final int delay = new Random().nextInt(10);
                    return Observable.just(s + "x")
                            .delay(delay, TimeUnit.SECONDS, scheduler);
//                return Observable.just(s+"x");
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println(s);
                    }
                });


        Util.logCurrentThreadName();
        scheduler.advanceTimeBy(1, TimeUnit.MINUTES);
        Flowable.fromPublisher(new Publisher<Integer>() {
            @Override
            public void subscribe(Subscriber<? super Integer> subscriber) {

            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {

            }
        });
        Flowable.range(1,500).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {

            }
        });
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {

        }).subscribe(integer -> {

        });
    }


    public static void concatMap() {
        final List<String> items = Arrays.asList("a", "b", "c", "d", "e", "f");
        final TestScheduler scheduler = new TestScheduler();

        Observable.fromIterable(items)
                .concatMap(s -> {
                    final int delay = new Random().nextInt(10);
                    return Observable.just(s + "x")
                            .delay(delay, TimeUnit.SECONDS, scheduler).doOnNext(System.out::println);
                })
                .doOnNext(System.out::println)
                .subscribe();

        scheduler.advanceTimeBy(1, TimeUnit.MINUTES);
    }


    public static void maps() {
        final List<String> items = Arrays.asList("a", "b", "c", "d", "e", "f");

        final TestScheduler scheduler1 = new TestScheduler();
        final TestScheduler scheduler2 = new TestScheduler();

        Observable.fromArray(items)
                .flatMap(s -> Observable.just(s + "x")
                        .delay(5, TimeUnit.SECONDS, scheduler1)
                        .doOnNext(str -> System.out.println(scheduler1.now(TimeUnit.MILLISECONDS) + " ")))
                .doOnNext(strings -> System.out.println("\nEND:" + scheduler1.now(TimeUnit.MILLISECONDS)))
                .subscribe();

        scheduler1.advanceTimeBy(1, TimeUnit.MINUTES);

        Observable.fromArray(items)
                .concatMap(s -> Observable.just(s + "x")
                        .delay(5, TimeUnit.SECONDS, scheduler2)
                        .doOnNext(str -> System.out.println(scheduler2.now(TimeUnit.MILLISECONDS) + " ")))
                .doOnNext(strings -> System.out.println("\nEND:" + scheduler2.now(TimeUnit.MILLISECONDS)))
                .subscribe();

        scheduler2.advanceTimeBy(1, TimeUnit.MINUTES);
    }
    public static void scan(){

        Observable.range(1,10).scan(new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) throws Exception {
                System.out.println(integer+"   "+integer2);
                return integer*integer2;
            }
        }).subscribe(System.out::println);
    }
    public static void buffer(){
        Observable.range(0,100).buffer(5).subscribe(System.out::println);
    }
    public static void groupBy(){
        Observable.range(1,100).groupBy(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return (integer%2)==0?"even":"odd";
            }
        }).subscribe(new Consumer<GroupedObservable<String, Integer>>() {
            @Override
            public void accept(GroupedObservable<String, Integer> stringIntegerGroupedObservable) throws Exception {
                stringIntegerGroupedObservable.toList().subscribe(list->System.out.println(stringIntegerGroupedObservable.getKey()+":---"+list));
            }
        });
    }

    public static void window(){
        Observable.range(1,100).window(5).subscribe(new Consumer<Observable<Integer>>() {
            @Override
            public void accept(Observable<Integer> integerObservable) throws Exception {
                integerObservable.toList().subscribe(System.out::println);
            }
        });
    }
}
