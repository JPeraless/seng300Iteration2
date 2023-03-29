import com.autovend.devices.SelfCheckoutStation;

public interface  PrintReceiptObserver {
	    /**
	     * Announces that the Customer Session is complete.
	     * 
	     * @param station
	     *            The device whose session is complete.
	     */
	    void sessionComplete(System station);

	    /**
	     * Announces that the machine requires Maintanence.
	     * 
	     * @param station
	     *            The device who requires Maintances.
	     */
	    void requiresMaintance(SelfCheckoutStation station, String message);

}
