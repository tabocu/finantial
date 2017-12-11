package sistemas.puc.com.finantialapp.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class FinantialAuthenticatorService extends Service {
    // Instance field that stores the authenticator object
    private FinantialAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new FinantialAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}