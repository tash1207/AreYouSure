package co.tashawych.areyousure.models;


public abstract interface SMSListener {
    public abstract void reportIncomingSMS(SMS incomingMessage);
    public abstract void reportOutgoingSMS(SMS outgoingMessage);
}
