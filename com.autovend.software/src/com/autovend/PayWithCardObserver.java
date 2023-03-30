package com.autovend;

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.CardReader;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.CardReaderObserver;

public class PayWithCardObserver implements CardReaderObserver {

    @Override
    public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {

    }

    @Override
    public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {

    }

    @Override
    public void reactToCardInsertedEvent(CardReader reader) {

    }

    @Override
    public void reactToCardRemovedEvent(CardReader reader) {

    }

    @Override
    public void reactToCardTappedEvent(CardReader reader) {

    }

    @Override
    public void reactToCardSwipedEvent(CardReader reader) {

    }

    @Override
    public void reactToCardDataReadEvent(CardReader reader, Card.CardData data) {

    }


    public void reactToCompletePaymentEvent(PayWithCard payWithCard) {

    }
}