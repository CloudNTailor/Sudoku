package com.CloudNTailor.sudoku.Pref;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;
import com.CloudNTailor.sudoku.R;

public class MyGDPR {
    public static Bundle getBundleAd(Activity act) {
        // UMP handles the consent state automatically with Ad Request.
        // We can return an empty bundle as the SDK handles the 'npa' flag internally.
        return new Bundle();
    }

    public static void updateConsentStatus(final Activity act, Runnable onComplete) {
        ConsentRequestParameters params = new ConsentRequestParameters.Builder()
                .setTagForUnderAgeOfConsent(false)
                .build();

        ConsentInformation consentInformation = UserMessagingPlatform.getConsentInformation(act);
        consentInformation.requestConsentInfoUpdate(
                act,
                params,
                () -> {
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                            act,
                            loadAndShowError -> {
                                if (loadAndShowError != null) {
                                    Log.w("MyGDPR", String.format("%s: %s",
                                            loadAndShowError.getErrorCode(),
                                            loadAndShowError.getMessage()));
                                }
                                if (onComplete != null) {
                                    onComplete.run();
                                }
                            }
                    );
                },
                requestConsentError -> {
                    Log.w("MyGDPR", String.format("%s: %s",
                            requestConsentError.getErrorCode(),
                            requestConsentError.getMessage()));
                    if (onComplete != null) {
                        onComplete.run();
                    }
                });
    }
}
