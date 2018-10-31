import io.reactivex.Observable;
import hu.akarnokd.rxjava2.math.MathObservable;
import io.reactivex.functions.BiFunction;

import java.util.Comparator;
import java.util.TreeSet;

public class MathAndCollection {

    public static void main(String[] args){
        reduceWith();
    }

    public static void averageDouble(){
        Observable<Integer> numbers = Observable.just(1, 2, 3);
        MathObservable.averageDouble(numbers).subscribe((Double avg) -> System.out.println(avg));
    }

    public static void max(){
        final Observable<String> names = Observable.just("Kirk", "Spock", "Chekov", "Sulu");
        MathObservable.max(names, Comparator.comparingInt(String::length))
                .subscribe(System.out::println);
    }

    public static void reduce(){
        Observable.range(1, 5)
                .reduce(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        return integer*integer2;
                    }
                })
                .subscribe(System.out::println);
    }
    public static void reduceWith(){
        Observable.just(1, 2, 2, 3, 4, 4, 4, 5)
                .reduceWith(TreeSet::new, (set, x) -> {
                    set.add(x);
                    return set;
                })
                .subscribe(System.out::println);
    }


}
