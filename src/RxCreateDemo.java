import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Action;
import io.reactivex.plugins.RxJavaPlugins;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class RxCreateDemo {
    public static void main(String[] args){
        fromRunnable();
    }

    public static void just(){
        String greeting =  "Hello world!";

        Observable<String> observable = Observable.just(greeting);

        observable.subscribe(item -> System.out.println(item));
    }

    public static void fromIterable(){
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));

        Observable<Integer> observable = Observable.fromIterable(list);

        observable.subscribe(item -> System.out.println(item), error -> error.printStackTrace(),
                () -> System.out.println("Done"));
    }

    public static void fromArray(){
        Integer[] array = new Integer[10];
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }

        Observable<Integer> observable = Observable.fromArray(array);

        observable.subscribe(item -> System.out.println(item), error -> error.printStackTrace(),
                () -> System.out.println("Done"));
    }

    public static void fromCallable(){
        Callable<String> callable = () -> {
            Util.logCurrentThreadName();
            System.out.println("Hello World!");
            return "Hello World!";
        };

        Observable<String> observable = Observable.fromCallable(callable);

        observable.subscribe(item -> System.out.println(item), error -> error.printStackTrace(),
                () -> System.out.println("Done"));
        Util.logCurrentThreadName();
    }

    public static void fromAction(){
        Action action = () -> System.out.println("Hello World!");

        Completable completable = Completable.fromAction(action);

        completable.subscribe(() -> System.out.println("Done"), error -> error.printStackTrace());
    }

    public static void fromRunnable(){
        Runnable runnable = () -> {
            Util.logCurrentThreadName();
            System.out.println("Hello World!");
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        Completable completable = Completable.fromRunnable(runnable);

        completable.subscribe(() ->{
            Util.logCurrentThreadName();
            System.out.println("Done");}, error -> error.printStackTrace());
    }

    public static void create(){
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        ObservableOnSubscribe<String> handler = emitter -> {

            Future<Object> future = executor.schedule(() -> {
                emitter.onNext("Hello");
                emitter.onNext("World");
                emitter.onComplete();
                return null;
            }, 1, TimeUnit.SECONDS);

            emitter.setCancellable(() -> future.cancel(false));
        };

        Observable<String> observable = Observable.create(handler);

        observable.subscribe(item -> System.out.println(item), error -> error.printStackTrace(),
                () -> System.out.println("Done"));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }

    public static void fromFuture(){
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        Future<String> future = executor.schedule(() -> "Hello world!", 1, TimeUnit.SECONDS);

        Observable<String> observable = Observable.fromFuture(future);

        observable.subscribe(
                item -> System.out.println(item),
                error -> error.printStackTrace(),
                () -> System.out.println("Done"));

        executor.shutdown();
    }

    public static void defer(){
        Observable<Long> observable = Observable.defer(() -> {
            long time = System.currentTimeMillis();
            return Observable.just(time);
        });

        observable.subscribe(time -> System.out.println(time));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        observable.subscribe(time -> System.out.println(time));
    }

    public static void range(){
        String greeting = "Hello World!";

        Observable<Integer> indexes = Observable.range(0, greeting.length());

        Observable<Character> characters = indexes
                .map(index -> greeting.charAt(index));

        characters.subscribe(character -> System.out.print(character), erro -> erro.printStackTrace(),
                () -> System.out.println());
    }

    public static void interval(){
        Observable<Long> clock = Observable.interval(1, TimeUnit.SECONDS);

        clock.subscribe(time -> {
            if (time % 2 == 0) {
                System.out.println("Tick");
            } else {
                System.out.println("Tock");
            }
        });
    }
    public static void timer(){
        Observable<Long> eggTimer = Observable.timer(5, TimeUnit.MINUTES);

        eggTimer.blockingSubscribe(v -> System.out.println("Egg is ready!"));
    }

    public static void empty(){
        Observable<String> empty = Observable.empty();

        empty.subscribe(
                v -> System.out.println("This should never be printed!"),
                error -> System.out.println("Or this!"),
                () -> System.out.println("Done will be printed."));
    }

    public static void never(){
        Observable<String> never = Observable.never();

        never.subscribe(
                v -> System.out.println("This should never be printed!"),
                error -> System.out.println("Or this!"),
                () -> System.out.println("This neither!"));
    }

    public static void error(){
        Observable<String> error = Observable.error(new IOException());

        error.subscribe(
                v -> System.out.println("This should never be printed!"),
                er -> er.printStackTrace(),
                () -> System.out.println("This neither!"));
    }
    public static void errorOnResume(){
        Observable<String> observable = Observable.fromCallable(() -> {

            if (Math.random() <  0.5) {

                throw  new IOException();

            }

            throw  new IllegalArgumentException();

        });//抛出异常

        Observable<String> result = observable.onErrorResumeNext(error -> {

            if (error instanceof IllegalArgumentException) {

                return Observable.empty();

            }

            return Observable.error(error);

        });//处理异常

        for (int i =  0; i <  10; i++) {

            result.subscribe(

                    v -> System.out.println("This should never be printed!"),

                    error -> error.printStackTrace(),

                    () -> System.out.println("Done"));

        }
    }

}
