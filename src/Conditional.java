import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import java.util.concurrent.TimeUnit;

public class Conditional {

    public static void main(String[] args) {
        range();
    }


    public static void range() {
     /*   PublishSubject ps = PublishSubject.create();

        ps.onNext(1);
        ps.subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                System.out.println(o);
            }
        });*/


     Observable observable = Observable.interval(1,TimeUnit.SECONDS);

        Observable  o = observable.publish().refCount(1).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread());

      Disposable disposable1= o.subscribe(new Consumer() {
         @Override
         public void accept(Object o) throws Exception {
             System.out.println(o);
         }
     });

        try {
            Thread.sleep(3*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Disposable disposable2=  o.subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                System.out.println(o);
            }
        });

        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*Observable o = Observable.interval(1, TimeUnit.SECONDS).observeOn(Schedulers.newThread()).replay(3);
        o.subscribe(System.out::print);
        System.out.println();
        o.subscribe(System.out::print);
        System.out.println();
        o.subscribe(System.out::print);
        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }


    public static void amb() {
        Observable observable1 = Observable.just("1", "2", "3");
        Observable observable2 = Observable.just("3", "4");
        Observable observable3 = Observable.just("5", "6");
        observable1.ambWith(observable2).ambWith(observable3).subscribe(o -> System.out.println(o + ""));

    }

    static void defaultIfEmpty() {
        Observable.empty().take(3).defaultIfEmpty("3").subscribe(o -> System.out.println(o + ""));
    }


}
