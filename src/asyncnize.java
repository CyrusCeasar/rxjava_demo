import hu.akarnokd.rxjava2.debug.RxJavaAssemblyTracking;
import io.reactivex.*;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.ExecutionException;

public class asyncnize {

    public static void main(String args[]) {
        last();
    }


    static void next() {
        RxJavaAssemblyTracking.enable();

        Observable<Integer> observable = Observable.range(1, 10);
        try {
            System.out.println(observable.toFuture().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

      /*  Iterator it = observable.blockingNext().iterator();

        while (it.hasNext()) {
            try {
                System.out.println(it.next() + "");
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                ((Disposable) it).dispose();
                throw ExceptionHelper.wrapOrThrow(e);
            }
        }*/
   /*     observable.blockingSubscribe(new io.reactivex.functions.Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println(integer+"");

            }
        });*/
/*
       Iterator<Integer> it = observable.blockingSubscribe();blockingNext().iterator();
       it.forEachRemaining(new Consumer<Integer>() {
           @Override
           public void accept(Integer integer) {
               System.out.println(integer+"");
           }
       });*/

    }

    static void first() {
        System.out.println(Flowable.range(1, 10).blockingLast() + "");
    }
    static void last() {
        System.out.println(Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {

                for(int i = 0; i < 10;i++){
                    Thread.sleep(1*1000);
                    emitter.onNext(i);
                }
                emitter.onComplete();
            }
        },BackpressureStrategy.BUFFER).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).blockingLast() + "");
    }
}
