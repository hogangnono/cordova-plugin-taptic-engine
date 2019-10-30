package com.hogangnono.cordova.plugin.taptic;


import android.content.Context;
import android.os.Vibrator;
import android.util.Log;


import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;


public class TapticEngine extends CordovaPlugin {

    private static final String TAG = "TapticEngine";
    private static final int DEFAULT_MIL_SEC = 2;
    private Context context;
    private Vibrator vibrator;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);

        this.context = cordova.getActivity().getApplicationContext();
    }

    @Override
    public boolean execute(final String action, JSONArray args, CallbackContext callbackContext) {
        Log.d(TAG, "action : " + action);
        Log.d(TAG, "args : " + args.toString());

        int milliseconds = 0;
        try {
            milliseconds = args.getInt(0);
        } catch (JSONException e) { }
        if ("gestureSelectionStart".equals(action)) {
            return this.gestureSelectionStart(callbackContext);
        } else if ("gestureSelectionChanged".equals(action)) {

            return this.gestureSelectionChanged(callbackContext, milliseconds);
        } else if ("gestureSelectionEnd".equals(action)) {
            return this.gestureSelectionEnd(callbackContext);
        } else if ("selection".equals(action)) {
            return this.selection(callbackContext, milliseconds);
        } else if ("vibrate".equals(action)) {
            return this.vibrate(callbackContext, milliseconds);
        }

        return false;
    }

    private boolean gestureSelectionStart(CallbackContext callbackContext) {
        if (this.vibrator == null) {
            this.vibrator = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
            if (this.vibrator == null) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "vibrator not available"));
                return false;
            }
        }
        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));

        return true;
    }

    private boolean gestureSelectionChanged(CallbackContext callbackContext, int milliseconds) {
        if (this.vibrator == null) {
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "vibrator not initalized"));
            return false;
        }

        if (milliseconds == 0) {
            milliseconds = DEFAULT_MIL_SEC;
        }

        this.vibrator.vibrate(milliseconds);
        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
        return true;
    }

    private boolean gestureSelectionEnd(CallbackContext callbackContext) {
        this.vibrator = null;
        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
        return true;
    }

    private boolean selection(CallbackContext callbackContext, int milliseconds) {
        Vibrator vibrator = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator == null) {
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "vibrator not available"));
            return false;
        }

        if (milliseconds == 0) {
            milliseconds = DEFAULT_MIL_SEC;
        }
        this.vibrator.vibrate(milliseconds);
        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
        return true;
    }

    private boolean vibrate(CallbackContext callbackContext, int milliseconds) {
        Vibrator vibrator = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator == null) {
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "vibrator not available"));
            return false;
        }

        if (milliseconds == 0) {
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "milliseconds not set."));
            return false;
        }

        this.vibrator.vibrate(milliseconds);
        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
        return true;
    }


}
