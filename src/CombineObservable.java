import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.processors.PublishProcessor;
import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

public class CombineObservable {

    public static void main(String[] args) throws InterruptedException {
     /*   Observable.range(1,10).groupBy(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return integer%2 == 0?"odd":"even";
            }
        }).subscribe(new Consumer<GroupedObservable<String, Integer>>() {
            @Override
            public void accept(GroupedObservable<String, Integer> stringIntegerGroupedObservable) throws Exception {
                stringIntegerGroupedObservable.subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println(stringIntegerGroupedObservable.getKey()+integer);
                    }
                });
            }
        });*/
     Observable.empty().single(11).subscribe(new Consumer<Object>() {
         @Override
         public void accept(Object o) throws Exception {
             System.out.println(o+"");
         }
     });
   /*  Observable.range(1,10).window(5).subscribe(new Consumer<Observable<Integer>>() {
         @Override
         public void accept(Observable<Integer> integerObservable) throws Exception {
             integerObservable.subscribe(new Consumer<Integer>() {
                 @Override
                 public void accept(Integer integer) throws Exception {
                     System.out.println(integerObservable.hashCode()+"   "+integer);
                 }
             });
         }
     });*/
//     switchOnNext();
    }

    public static void startWith() {
        Observable<String> names = Observable.just("Spock", "McCoy");

        names.startWith("Kirk").subscribe(System.out::println);
    }

    public static void merge() {
        Observable.just(1, 2, 3)
                .mergeWith(Observable.just(4, 5, 6))
                .subscribe(item -> System.out.println(item));
    }
    public static void mergeDelayError(){
        Observable<String> observable1 = Observable.error(new IllegalArgumentException(""));
        Observable<String> observable2 = Observable.just("Four", "Five", "Six");
        Observable.mergeDelayError(observable1, observable2)
                .subscribe(item -> System.out.println(item), new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println(throwable.getMessage());
                    }
                });
    }
    public static void zip(){
        Observable<String> firstNames = Observable.just("James", "Jean-Luc", "Benjamin","Cyrus");
        Observable<String> lastNames = Observable.just("Kirk", "Picard", "Sisko");
        firstNames.zipWith(lastNames, (first, last) -> first + " " + last)
                .subscribe(item -> System.out.println(item));
    }
    public static void combineLatest(){
        Observable<Long> newsRefreshes = Observable.interval(100, TimeUnit.MILLISECONDS);
        Observable<Long> weatherRefreshes = Observable.interval(50, TimeUnit.MILLISECONDS);
        Observable.combineLatest(newsRefreshes, weatherRefreshes,
                (newsRefreshTimes, weatherRefreshTimes) ->
                        "Refreshed news " + newsRefreshTimes + " times and weather " + weatherRefreshTimes)
                .subscribe(item -> System.out.println(item));
        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void switchOnNext(){
        Observable<Observable<String>> timeIntervals =
                Observable.interval(1, TimeUnit.SECONDS)
                                .map(new Function<Long, Observable<String>>() {
                                    @Override
                                    public Observable<String> apply(Long aLong) throws Exception {
                                        return Observable.just(aLong+"");
                                    }
                                });
        Observable.switchOnNext(timeIntervals)
                .subscribe(item -> System.out.println(item));
        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
