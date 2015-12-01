package com.sohu.kurento_p2p_andorid.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sohu.kurento_p2p_andorid.R;
import com.sohu.utillib.RoundAlertDialog;

/**
 * Created by jingbiaowang on 2015/11/17.
 */
public class RegisterDialog extends RoundAlertDialog implements View.OnClickListener {

	private EditText registeret;
	private Button registerok;
	private Button registercancel;

	public interface RegisterDialogEvents {
		public void cancel();

		public void ok(String name);

	}

	private RegisterDialogEvents dialogInterface;

	protected RegisterDialog(Context context) {
		super(context);
	}

	public RegisterDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected View getCustomeContainerView() {

		View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_register_layout,
				null);
		initialize(view);
		return view;
	}


	private void initialize(View view) {

		registeret = (EditText) view.findViewById(R.id.register_et);
		registerok = (Button) view.findViewById(R.id.register_ok);
		registercancel = (Button) view.findViewById(R.id.register_cancel);

		registerok.setOnClickListener(this);
		registercancel.setOnClickListener(this);

	}

	public RegisterDialogEvents getDialogInterface() {
		return dialogInterface;
	}

	public void setDialogInterface(RegisterDialogEvents dialogInterface) {
		this.dialogInterface = dialogInterface;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

			case R.id.register_cancel: {

				if (dialogInterface != null) {
					RegisterDialog.this.dismiss();
					dialogInterface.cancel();
				}

				break;
			}

			case R.id.register_ok: {

				String name = registeret.getText().toString().trim();
				if (name != null) {
					if (dialogInterface != null) {
						RegisterDialog.this.dismiss();
						dialogInterface.ok(name);
					}
				} else {
					Toast.makeText(getContext(), getContext().getString(R.string.input_name),
							Toast.LENGTH_SHORT).show();
				}

				break;
			}
		}
	}
}
