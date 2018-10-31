import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.functions.Consumer;

public class Conditional {

    public static void main(String[] args){
        defaultIfEmpty();
    }

    public static void amb(){
        Observable observable1 = Observable.just("1","2","3");
        Observable observable2 = Observable.just("3","4");
        Observable observable3 = Observable.just("5","6");

        observable1.ambWith(observable2).ambWith(observable3).subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                System.out.println(o+"");
            }
        });

    }
    static void defaultIfEmpty(){
        Observable.empty().take(3).defaultIfEmpty("3").subscribe(new Consumer<Object>() {

            @Override
            public void accept(Object o) throws Exception {
                System.out.println(o+"");
            }
        });

    }

}
