import com.autovend.devices.SelfCheckoutStation;

public interface  PrintReceiptObserver {
	    /**
	     * Announces that the Customer Session is complete.
	     * 
	     * @param station
	     *            The device whose session is complete.
	     *            tasks for iteration2: signals customer io that session is complete
	     */
	    void sessionComplete(SelfCheckoutStation station);

	    /**
	     * Announces that the machine requires Maintanence.
	     * 
	     * @param station
	     *            The device who requires Maintances.
	     *            tasks for iteration 2: signals attendent stub that printer requires maintainence
	     */
	    void requiresMaintance(SelfCheckoutStation station, String message);
	    

}
