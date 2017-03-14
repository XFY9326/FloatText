package tool.xfy9326.floattext;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import java.security.MessageDigest;
import tool.xfy9326.floattext.Utils.StaticString;

//包名签名验证

public class SafeGuard {
    private static String SIGNATURE = "WcoDGef5LGYXLd";
	public static boolean SAFE_GUARD = true;

    public static boolean isPackageNameAvailable(Context ctx, boolean exit) {
		if (SAFE_GUARD) {
			String name = ctx.getPackageName();
			if (!name.equals(StaticString.DEFAULT_PACKAGE_NAME)) {
				if (exit) {
					System.exit(0);
				} else {
					return false;
				}
			}
			return true;
		}
		return false;
    }

    public static boolean isSignatureAvailable(Context ctx, boolean exit) {
		if (SAFE_GUARD) {
			try {
				PackageInfo pi = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNATURES);
				Signature[] st = pi.signatures;
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(st[0].toByteArray());
				String currentSignature = Base64.encodeToString(md.digest(), Base64.DEFAULT);
				if (!SIGNATURE.equals(currentSignature.substring(8, currentSignature.length() - 7))) {
					if (exit) {
						System.exit(0);
					} else {
						return false;
					}
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				if (exit) {
					System.exit(0);
				} else {
					return false;
				}
			}
		}
		return false;
	}
}
