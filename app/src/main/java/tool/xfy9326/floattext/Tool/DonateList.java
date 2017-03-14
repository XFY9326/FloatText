package tool.xfy9326.floattext.Tool;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import tool.xfy9326.floattext.R;

//捐赠选择

public class DonateList {
	private Context mContext;

	public DonateList(Context ctx) {
		this.mContext = ctx;
	}

	public void show() {
		DonateDialogShow();
	}

	private void DonateDialogShow() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext)
			.setTitle(R.string.xml_about_donate)
			.setItems(R.array.donate_list, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface d, int i) {
					String[] urls = getPurchaseURL();
					Uri url = Uri.parse(urls[i].toString().trim());
					Intent intent = new Intent(Intent.ACTION_VIEW, url);
					mContext.startActivity(intent);
				}
			})
			.setNegativeButton(R.string.cancel, null);
		dialog.show();
	}

	private String[] getPurchaseURL() {
		String[] url = new String[3];
		url[0] = "https://raw.githubusercontent.com/XFY9326/FloatText/gh-pages/Datas/DONATE/AliPay.png";
		url[1] = "https://raw.githubusercontent.com/XFY9326/FloatText/gh-pages/Datas/DONATE/WeChat.png";
		url[2] = "https://raw.githubusercontent.com/XFY9326/FloatText/gh-pages/Datas/DONATE/QQ.png";
		return url;
	}
}
