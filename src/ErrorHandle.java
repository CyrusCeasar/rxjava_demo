import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

public class ErrorHandle extends TestCase{


    @Test
   public static void testdoOnError() {
        Observable.error(new IOException("Something went wrong"))
                .doOnError(error -> System.err.println("The error message is: " + error.getMessage()))
                .subscribe(
                        x -> System.out.println("onNext should never be printed!"),
                        Throwable::printStackTrace,
                        () -> System.out.println("onComplete should never be printed!"));

    }

    @Test
   public static void testonErrorComplete() {
        Completable.fromAction(() -> {
            throw new IOException();
        }).onErrorComplete(error -> {
            // Only ignore errors of type java.io.IOException.
            return error instanceof IOException;
        }).subscribe(
                () -> System.out.println("IOException was ignored"),
                error -> System.err.println("onError should not be printed!"));
    }

    @Test
    public static void testonErrorReturn() {
        Single.just("2A")
                .map(v -> Integer.parseInt(v, 10))
                .onErrorReturn(error -> {
                    if (error instanceof NumberFormatException) return 0;
                    else throw new IllegalArgumentException();
                })
                .subscribe(
                        System.out::println,
                        error -> System.err.println("onError should not be printed!"));
    }

    static void onExceptionResumeNext() {
        Observable<String> exception = Observable.<String>error(IOException::new)
                .onExceptionResumeNext(Observable.just("This value will be used to recover from the IOException"));

        Observable<String> error = Observable.<String>error(Error::new)
                .onExceptionResumeNext(Observable.just("This value will not be used"));

        Observable.concat(exception, error)
                .subscribe(
                        message -> System.out.println("onNext: " + message),
                        err -> System.err.println("onError: " + err));
    }

    static void retry() {
        Observable<Long> source = Observable.interval(0, 1, TimeUnit.SECONDS)
                .flatMap(x -> {
                    if (x >= 2) return Observable.error(new IOException("Something went wrong!"));
                    else return Observable.just(x);
                });

        source.retry((retryCount, error) -> retryCount < 3)
                .blockingSubscribe(
                        x -> System.out.println("onNext: " + x),
                        error -> System.err.println("onError: " + error.getMessage()));
    }

    static void retryUntil() {
        LongAdder errorCounter = new LongAdder();
        Observable<Long> source = Observable.interval(0, 1, TimeUnit.SECONDS)
                .flatMap(x -> {
                    if (x >= 2) return Observable.error(new IOException("Something went wrong!"));
                    else return Observable.just(x);
                })
                .doOnError((error) -> errorCounter.increment());

        source.retryUntil(() -> errorCounter.intValue() >= 3)
                .blockingSubscribe(
                        x -> System.out.println("onNext: " + x),
                        error -> System.err.println("onError: " + error.getMessage()));
    }

    static void retryWhen() {
        Observable<Long> source = Observable.interval(0, 1, TimeUnit.SECONDS)
                .flatMap(x -> {
                    if (x >= 2) return Observable.error(new IOException("Something went wrong!"));
                    else return Observable.just(x);
                });

        source.retryWhen(errors -> {
            return errors.map(error -> 1)

                    // Count the number of errors.
                    .scan(Math::addExact)

                    .doOnNext(errorCount -> System.out.println("No. of errors: " + errorCount))

                    // Limit the maximum number of retries.
                    .takeWhile(errorCount -> errorCount < 3)

                    // Signal resubscribe event after some delay.
                    .flatMapSingle(errorCount -> Single.timer(errorCount, TimeUnit.SECONDS));
        }).blockingSubscribe(
                x -> System.out.println("onNext: " + x),
                Throwable::printStackTrace,
                () -> System.out.println("onComplete"));

        // prints:
        // onNext: 0
        // onNext: 1
        // No. of errors: 1
        // onNext: 0
        // onNext: 1
        // No. of errors: 2
        // onNext: 0
        // onNext: 1
        // No. of errors: 3
        // onComplete
    }
}
