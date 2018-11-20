import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.observables.ConnectableObservable;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class Connectable extends TestCase{



    @Test
    public static void testPublish(){
       ConnectableObservable observable =  Observable.range(1,3).publish();
       observable.subscribe(new Consumer() {
           @Override
           public void accept(Object o) throws Exception {
               System.out.println("a"+o);
           }
       });
        observable.subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                System.out.println("b"+o);
            }
        });
        observable.connect();
    }

    @Test
    public static void testReplay(){
        ConnectableObservable observable = Observable.interval(1, TimeUnit.SECONDS).replay();
        observable.subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                System.out.println("a"+o);
            }
        });
        observable.connect();
        try {
            Thread.sleep(3*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        observable.subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                System.out.println("b"+o);
            }
        });
        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public static void testRefCount(){
        ConnectableObservable observable = Observable.interval(1, TimeUnit.SECONDS).publish();
        observable.subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                System.out.println("a"+o);
            }
        });
        observable.refCount(1);
        observable.connect();
        try {
            Thread.sleep(3*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        observable.subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                System.out.println("b"+o);
            }
        });
        observable.subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                System.out.println("c"+o);
            }
        });
        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
