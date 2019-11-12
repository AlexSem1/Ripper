package quoters;

import quoters.Quoter;

public class T1000 extends quoters.TerminatorQuoter implements Quoter {


    @Override
    public void sayQuote() {
        System.out.println("Я жидкий");
    }
}
