import io.reactivex.Completable;
import io.reactivex.Observable;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Creation {

    public static void main(String[] args) {
        timer();
    }



    public static void defer(){
        Observable<Long> observable = Observable.defer(() -> {
            Util.logCurrentThreadName();
            long time = System.currentTimeMillis();
            return Observable.just(time);
        });

        observable.subscribe(time -> System.out.println(time));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Util.logCurrentThreadName();
        observable.subscribe(time -> System.out.println(time));
    }
    public static void timer(){
        Observable<Long> eggTimer = Observable.timer(5, TimeUnit.SECONDS);
        eggTimer.subscribe(v -> System.out.println("Egg is ready!"));
//        eggTimer.blockingSubscribe(v -> System.out.println("Egg is ready!"));
    }

}
