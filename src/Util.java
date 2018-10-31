
public class Util {

    public static void logCurrentThreadName(){
        System.out.println(Thread.currentThread().getStackTrace()[3].getMethodName()+"-----"+Thread.currentThread().getName());
    }

    public static void main(String[] args){
        System.out.println(String.class.hashCode()+"----"+ new String("safda").getClass().hashCode());

    }
}
