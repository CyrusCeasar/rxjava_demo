import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;
import junit.framework.TestCase;
import org.junit.Test;

public class Subject extends TestCase{

    @Test
    public static void testpublishSubject(){
        PublishSubject publishSubject = PublishSubject.create();
        publishSubject.subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                System.out.println("a"+o);
            }
        });
        publishSubject.subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                System.out.println("b"+o);
            }
        });
        for(int i = 0 ; i <3 ;i++){
            publishSubject.onNext(i);
        }
    }

    @Test
    public static void testreplaySubject(){
        ReplaySubject publishSubject = ReplaySubject.create(1);
        publishSubject.subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                System.out.println("a"+o);
            }
        });
        publishSubject.onNext(0);
        for(int i = 1 ; i <3 ;i++){
            publishSubject.onNext(i);
        }
        publishSubject.subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                System.out.println("b"+o);
            }
        });

    }
}
