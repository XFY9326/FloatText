package tool.xfy9326.floattext.CrashReport;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;

import tool.xfy9326.floattext.R;
import tool.xfy9326.floattext.SafeGuard;

public class CrashHandlerUI extends AppCompatActivity {
    private String Log = "Report Error";
    private String Device = "Device Unknown";
    private String mail;
    private String AppName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crashhandler_ui);
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        Log = getIntent().getStringExtra("CrashLog");
        Device = getIntent().getStringExtra("DeviceInfo");
        //String clsname = getIntent().getStringExtra("ClassName");
        AppName = getIntent().getStringExtra("AppName");
        mail = getIntent().getStringExtra("Mail");
        buttonset();
    }

    private boolean isOfficialVersion() {
        return SafeGuard.isSignatureAvailable(this, false) && SafeGuard.isPackageNameAvailable(this, false);
    }

    private void buttonset() {
        TextView logshow = (TextView) findViewById(R.id.textview_crashlog);
        logshow.setText(Log);
        Button exit = (Button) findViewById(R.id.button_nosendcrashlog);
        exit.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                exit();
            }
        });
        Button send = (Button) findViewById(R.id.button_sendcrashlog);
        send.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                sendmail();
            }
        });
    }

    private void sendmail() {
        PackageManager pm = getPackageManager();
        String MailSend = AppName + getString(R.string.crashreport_mail_main) + "\n\n" + getString(R.string.crashreport_mail_report) + ":\n" + Device + "\n\n" + Log;
        if (!isOfficialVersion()) {
            MailSend += "\n\nNot Official Release!";
            try {
                PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
                Signature[] sg = info.signatures;
                String sgdata = "";
                for (Signature sginfo : sg) {
                    sgdata += parseSignature(sginfo.toByteArray()) + "\n";
                }
                MailSend += "\n\n" + "SignatureInfo:" + "\n\n" + sgdata;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        Intent data = new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:" + mail));
        data.putExtra(Intent.EXTRA_SUBJECT, AppName + getString(R.string.crashreport_mail_title));
        data.putExtra(Intent.EXTRA_TEXT, MailSend);
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(data, PackageManager.MATCH_DEFAULT_ONLY);
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));
        if (resolveInfos.size() == 0) {
            Toast.makeText(this, getString(R.string.crashreport_no_mail_app) + mail, Toast.LENGTH_LONG).show();
        } else {
            startActivity(data);
        }
    }

    private String parseSignature(byte[] signature) {
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(signature));
            String pubkey = cert.getPublicKey().toString();
            String info = cert.getSubjectX500Principal().getName();
            if (info.contains(",")) {
                String[] infolist = info.split(",");
                info = "";
                for (String listitem : infolist) {
                    info += listitem + "\n";
                }
            }
            return "PublicKey:" + "\n" + pubkey + "\n\n" + "Information:" + "\n" + info;
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void exit() {
        this.finish();
        System.exit(0);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                exit();
                break;
        }
        return false;
    }

}
