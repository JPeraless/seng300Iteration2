package com.autovend.software;

/**
 * Dummy class to simulate customer I/O
 */
public class CustomerIO {
    private Boolean thanks; // Status for if the customer needs to be thanked
    private Boolean ready; // Status if ready for new customer session

    // Customer IO returns thank you message to screen
    public String thankCustomer(){
        return "Thank you for shopping with us today!";
    }

    // Sets if ready for new customer (at the start of new customer set to false, and once finisehd set to true)
    public void setReady(Boolean ready) {
        this.ready = ready;
    }

    // Returns status about customer session
    public Boolean getReady() {
        return ready;
    }

    // System signals Customer I/O through thanks boolean
    public void setThanks(Boolean thanks){
        this.thanks = thanks;
    }

    // Returns if customer should be thanked
    public Boolean getThanks() {
        return thanks;
    }


}
