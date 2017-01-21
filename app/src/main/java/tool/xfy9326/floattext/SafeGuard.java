package tool.xfy9326.floattext;

import android.content.*;
import android.content.pm.*;
import android.util.*;
import java.security.*;

import android.content.pm.Signature;

public class SafeGuard
{
    private static String SIGNATURE = "WcoDGef5LGYXLd";
    private static String PACKAGE_NAME = "tool.xfy9326.floattext";

    public static void isPackageNameAvailable (Context ctx)
    {
        String name = ctx.getPackageName();
        if (!name.equals(PACKAGE_NAME))
        {
            System.exit(0);
        }
    }

    public static void isSignatureAvailable (Context ctx)
    {
        try
        {
            PackageInfo pi = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] st = pi.signatures;
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(st[0].toByteArray());
            String currentSignature = Base64.encodeToString(md.digest(), Base64.DEFAULT);
            if (!SIGNATURE.equals(currentSignature.substring(8, currentSignature.length() - 7)))
            {
                System.exit(0);
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
            System.exit(0);
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
