package net.multipi.pinsession;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Smmarat on 10.10.16.
 */

public class PinFacade {

    private SharedPreferences pref;

    private static final String PREF_SESSION = "pref_session";

    public PinFacade(Context ctx) {
        pref = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public boolean isSessionExists() {
        return pref.getString(PREF_SESSION, null) != null;
    }

    public boolean setupPin(String pin, String session) {
        try {
            String encoded = Crypter.encrypt(session, pin);
            pref.edit().putString(PREF_SESSION, encoded).apply();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String verifyPin(String pin) throws Exception {
        String encoded = pref.getString(PREF_SESSION, null);
        if (encoded == null) {
            throw new Exception("session not found");
        }
        return Crypter.decrypt(encoded, pin);
    }
}
