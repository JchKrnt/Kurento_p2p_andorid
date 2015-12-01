package com.sohu.utillib;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by jingbiaowang on 2015/8/17.
 */
public abstract class RoundAlertDialog extends Dialog {


	public RoundAlertDialog(Context context) {
		super(context);
	}

	private int theme;

	public RoundAlertDialog(Context context, int theme) {
		super(context, theme);
		this.theme = theme;
	}

	protected abstract View getCustomeContainerView();

	public static class Builder {

		private RoundAlertDialog alertDialog;
		private Context context;
		private float widthPercent = 0.8f;

		public Builder(float widthPercent) {
			this.widthPercent = widthPercent;
		}

		public RoundAlertDialog create(RoundAlertDialog dialog) {
			this.alertDialog = dialog;
			this.context = dialog.getContext();
//            FrameLayout layout = new FrameLayout(context);
//            layout.setBackground(context.getResources().getDrawable(R.drawable.alert_dialog_bg,
// context.getTheme()));

			View contentView = dialog.getCustomeContainerView();

			dialog.setContentView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams
					.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			dialog.setCanceledOnTouchOutside(false);

			dialog.getWindow().setBackgroundDrawableResource(R.drawable.round_dialog_bg);

			//resize dialog size.width is 80 percent of the screen width.
			int winWidth = (int) ((double) setDialogSize(dialog.getWindow().getWindowManager()).x
					* widthPercent);
			dialog.getWindow().setLayout(winWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
			dialog.getWindow().setGravity(Gravity.CENTER);

			return alertDialog;
		}

		private Point setDialogSize(WindowManager windowManager) {
			Point point = new Point();
			Display display = windowManager.getDefaultDisplay();
			DisplayUtil.getSize(display, point);

			return point;
		}
	}


}
