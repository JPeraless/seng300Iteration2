package com.autovend.software;

/*
 * Members for Iteration 1:
 * Michelle Loi (30019557)
 * James Hayward (30149513)
 * Caleb Cavilla ()
 * Amandeep Kaur (30153923)
 * Ethan Oke (30142180)
 * Winjoy Tiop (30069663)
 */


/**
 * Dummy class to simulate customer I/O
 */
public class CustomerIO {
    private Boolean thanks = false; // Status for if the customer needs to be thanked
    private Boolean ready = false; // Status if ready for new customer session

    // Customer IO returns thank you message to screen ("Thank you for shopping with us today!") or
    // empty string if they shouldn't be thanked yet
    public String thankCustomer(){
        if(thanks){
            return "Thank you for shopping with us today!";
        }
        return "";
    }

    // Sets if ready for new customer (at the start of new customer set to false, and once finished set to true)
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
