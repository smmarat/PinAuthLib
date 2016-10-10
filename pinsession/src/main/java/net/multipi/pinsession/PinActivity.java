package net.multipi.pinsession;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class PinActivity extends AppCompatActivity {

    private StringBuilder pin;
    private String pin0;
    public static final String EXTRA_SESSION = "extra_session";
    public static final String EXTRA_OPER = "extra_oper";
    public static final String EXTRA_ERROR = "extra_error";
    public static final String OPER_CREATE = "oper_create";
    public static final String OPER_VERIFY = "oper_verify";
    private String oper;
    private String session;
    private TextView msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pin);
        msg = (TextView) findViewById(R.id.message);
        pin = new StringBuilder();
        if (!getIntent().hasExtra(EXTRA_OPER)) {
            sendErr("operation is not defined");
            return;
        }
        oper = getIntent().getStringExtra(EXTRA_OPER);
        if (OPER_CREATE.equals(oper)) {
            msg.setText(R.string.create_code);
            if (!getIntent().hasExtra(EXTRA_SESSION)) {
                sendErr("session not found");
                return;
            }
            session = getIntent().getStringExtra(EXTRA_SESSION);
        } else if (OPER_VERIFY.equals(oper)) {
            msg.setText(R.string.type_code);
        }
    }

    private void next() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (OPER_CREATE.equals(oper)) {
            if (pin0 == null) {
                pin0 = pin.toString();
                pin = new StringBuilder();
                showPin();
                msg.setText(R.string.retype_code);
                return;
            }
            if (!pin0.equals(pin.toString())) {
                Toast.makeText(this, R.string.not_equal, Toast.LENGTH_LONG).show();
                pin0 = null;
                pin = new StringBuilder();
                showPin();
                msg.setText(R.string.create_code);
                return;
            }
            boolean b = new PinFacade(this).setupPin(pin.toString(), session);
            if (b) sendOk();
            else sendErr("can't setup pin");
        } else if (OPER_VERIFY.equals(oper)){
            try {
                session = new PinFacade(this).verifyPin(pin.toString());
                sendOk();
            } catch (Exception e) {
                e.printStackTrace();
                sendErr(e.getMessage());
            }
        }
    }

    private void sendOk() {
        setResult(RESULT_OK, new Intent().putExtra(EXTRA_SESSION, session));
        finish();
    }

    private void sendErr(String err) {
        setResult(RESULT_CANCELED, new Intent().putExtra(EXTRA_ERROR, err));
        finish();
    }

    private void showPin() {
        if (pin.length() > 0) findViewById(R.id.p1).setBackgroundResource(R.color.colorAccent);
        else findViewById(R.id.p1).setBackgroundResource(R.color.colorPrimary);
        if (pin.length() > 1) findViewById(R.id.p2).setBackgroundResource(R.color.colorAccent);
        else findViewById(R.id.p2).setBackgroundResource(R.color.colorPrimary);
        if (pin.length() > 2) findViewById(R.id.p3).setBackgroundResource(R.color.colorAccent);
        else findViewById(R.id.p3).setBackgroundResource(R.color.colorPrimary);
        if (pin.length() > 3) {
            findViewById(R.id.p4).setBackgroundResource(R.color.colorAccent);
            next();
        }
        else findViewById(R.id.p4).setBackgroundResource(R.color.colorPrimary);
    }

    public void do1(View view) {
        pin.append("1");
        showPin();
    }

    public void do2(View view) {
        pin.append("2");
        showPin();
    }

    public void do3(View view) {
        pin.append("3");
        showPin();
    }

    public void do4(View view) {
        pin.append("4");
        showPin();
    }

    public void do5(View view) {
        pin.append("5");
        showPin();
    }

    public void do6(View view) {
        pin.append("6");
        showPin();
    }

    public void do7(View view) {
        pin.append("7");
        showPin();
    }

    public void do8(View view) {
        pin.append("8");
        showPin();
    }

    public void do9(View view) {
        pin.append("9");
        showPin();
    }

    public void do0(View view) {
        pin.append("0");
        showPin();
    }

    public void doBack(View view) {
        pin.deleteCharAt(pin.length()-1);
        showPin();
    }


}
