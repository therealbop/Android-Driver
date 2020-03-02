package com.karry.side_screens.wallet.add_card;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.heride.partner.R;
import com.karry.utility.AppPermissionsRunTime;
import com.karry.utility.AppTypeFace;
import com.karry.utility.Utility;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;

import com.stripe.android.view.CardInputWidget;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import dagger.android.support.DaggerAppCompatActivity;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

import static com.karry.utility.VariableConstant.CARD_BRAND;
import static com.karry.utility.VariableConstant.CARD_TOKEN;
import static com.karry.utility.VariableConstant.LAST_DIGITS;
import static com.karry.utility.VariableConstant.REQUEST_CODE_PERMISSION_MULTIPLE;


/**
 * <h>AddCardActivity</h>
 * <p> Activity to add new card to make payment </p>
 */
public class AddCardActivity extends DaggerAppCompatActivity implements AddCardActivityContract.AddCardview
{
	@BindView(R.id.rl_addCard_poweredBy)	RelativeLayout rl_addCard_poweredBy;
	@BindView(R.id.tv_addCard_scanCard) TextView tv_addCard_scanCard;
	@BindView(R.id.tv_addCard_done) TextView tv_addCard_done;
	@BindView(R.id.rl_addCard_scanCard) RelativeLayout rl_addCard_scanCard;
	@BindView(R.id.rl_addCard_done) RelativeLayout rl_addCard_done;
	@BindView(R.id.progressBar_addCard) ProgressBar progressBar_addCard;
	@BindColor(R.color.colorPrimaryDark) int colorPrimaryDark;
	@BindColor(R.color.white) int white;
	@BindColor(R.color.text_color) int text_color;
	@BindString(R.string.You_did_not_enter_valid_card) String You_did_not_enter_valid_card;
	@BindString(R.string.addcard) String addcard;

	@Inject AppTypeFace appTypeface;
	@Inject AppPermissionsRunTime permissionsRunTime;
	@Inject AddCardActivityContract.AddCardPresenter presenter;

	@BindView(R.id.toolbar)Toolbar toolBar;
	@BindView(R.id.tv_title) TextView tvAbarTitle;
	@BindDrawable(R.drawable.back_white_btn)	Drawable back_white_btn;

	private boolean scanflag = false;
	private CreditCard scanResult;
	private CardInputWidget card_input_widget_card;
	private EditText et_card_number;
	private String cardToken ;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
		setContentView(R.layout.activity_add_card);
		ButterKnife.bind(this);
		initToolBar();
		initializeViews();
	}


	/* <h2>initToolBar</h2>
    * <p> method to initialize customer toolbar </p>
    */
	@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
	private void initToolBar()
	{
		setSupportActionBar(toolBar);
		if(getSupportActionBar()!=null){
			getSupportActionBar().setDisplayShowTitleEnabled(false);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeAsUpIndicator(back_white_btn);
		}

		tvAbarTitle.setTypeface(appTypeface.getPro_narMedium());
		tvAbarTitle.setText(addcard);
	}

	/**
	 * <h2>initViews</h2>
	 * <p> method to initializes the views of this activit </p>
	 */
	private void initializeViews()
	{
		card_input_widget_card=findViewById(R.id.card_input_widget_card);
		tv_addCard_done.setTypeface(appTypeface.getPro_News());
		tv_addCard_scanCard.setTypeface(appTypeface.getPro_News());

		TextView tvPoweredBy_addCard =  findViewById(R.id.tv_addCard_poweredBy);
		tvPoweredBy_addCard.setTypeface(appTypeface.getPro_News());

		et_card_number= card_input_widget_card.findViewById(R.id.et_card_number);
		et_card_number.setTypeface(appTypeface.getPro_News());
		et_card_number.setTextSize(16);

		EditText et_expiry_date= card_input_widget_card.findViewById(R.id.et_expiry_date);
		et_expiry_date.setTypeface(appTypeface.getPro_News());
		et_expiry_date.setTextSize(16);

		EditText et_cvc_number= card_input_widget_card.findViewById(R.id.et_cvc_number);
		et_cvc_number.setTextSize(16);
		et_cvc_number.setTypeface(appTypeface.getPro_News());

		presenter.showKeyboard();
	}

	@OnEditorAction(R.id.tv_addCard_done)
	public boolean onEditEvent(TextView v, int actionId, KeyEvent event)
	{
		if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE)
		{
			presenter.validateCardDetails(card_input_widget_card.getCard());
			return true;
		}
		return false;
	}

	@OnClick({R.id.tv_addCard_done,R.id.tv_addCard_scanCard})
	public void clickEvent(View v)
	{
		presenter.hideKeyboardAndClearFocus();
		switch (v.getId())
		{
			case R.id.tv_addCard_done:
				presenter.validateCardDetails(card_input_widget_card.getCard());
				break;

			case R.id.tv_addCard_scanCard:
				if (Build.VERSION.SDK_INT >= 23) {
					ArrayList<AppPermissionsRunTime.Permission> myPermissionConstantsArrayList = new ArrayList<>();
					myPermissionConstantsArrayList.clear();
					myPermissionConstantsArrayList.add(AppPermissionsRunTime.Permission.CAMERA);

					if (permissionsRunTime.getPermission(myPermissionConstantsArrayList, this, true))
						onScanPress();
				}
				else
					onScanPress();
				break;

			default:
				break;
		}
	}

	/**
	 *<h2>onScanPress</h2>
	 * This method is used to open the scan card activity
	 */
	private void onScanPress()
	{
		Intent scanIntent = new Intent(this, CardIOActivity.class);
		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true);
		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true);
		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false);
		scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false);
		scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO,true);
		scanIntent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON,false);
		scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME,true );
		int MY_SCAN_REQUEST_CODE = 100;
		startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
	}

	/**
	 *<h2>callDoneButton</h2>
	 * <p>This method is used to create the stripe token</p>
	 */
	public void callDoneButton()
	{
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(tv_addCard_done.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

		progressBar_addCard.setVisibility(View.VISIBLE);
		rl_addCard_poweredBy.setVisibility(View.GONE);
		updateUi(false,colorPrimaryDark,text_color);
	/*	new Stripe(this, presenter.stripeKeyGetter()).
				createToken(
						card_input_widget_card.getCard(),
						new ApiResultCallback<Token>() {
							public void onSuccess(Token token) {
								// Send token to your own web service
								cardToken=token.getId();
								Utility.printLog("MyCardDetStripe"+cardToken);
								presenter.addCardService(cardToken );
							}
							public void onError(Exception error) {
								Utility.printLog("MyCardDetStripe"+error.getMessage());
								updateUi(true,colorPrimaryDark,text_color);
							}
						}
				);*/

		new Stripe(this).createToken(card_input_widget_card.getCard(), presenter.stripeKeyGetter(), new TokenCallback() {
			@Override
			public void onError(Exception error)
			{
				Utility.printLog("MyCardDet"+error.getMessage());
				updateUi(true,colorPrimaryDark,text_color);
			}
			@Override
			public void onSuccess(com.stripe.android.model.Token token)
			{
				cardToken=token.getId();
//				Utility.printLog("MyCardDet"+token.getId());
				presenter.addCardService(token.getId() );
			}
		});
	}


	@Override
	protected void onResume()
	{
		super.onResume();
		if(scanflag)
			scanCardResult();
	}

	/**
	 * <h2>scanCardResult</h2>
	 * <p> method to get values from the scanned card </p>
	 */
	private void scanCardResult()
	{
		card_input_widget_card.setCardNumber(scanResult.cardNumber);
		if(scanResult.expiryMonth>=1 && scanResult.expiryMonth<=12)
			card_input_widget_card.setExpiryDate(scanResult.expiryMonth,scanResult.expiryYear);
		card_input_widget_card.setCvcCode(scanResult.cvv);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT))
		{
			scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
			scanflag = true;
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		boolean isDenine = false;
		switch (requestCode)
		{
			case REQUEST_CODE_PERMISSION_MULTIPLE:
				for (int grantResult : grantResults) {
					if (grantResult != PackageManager.PERMISSION_GRANTED) {
						isDenine = true;
					}
				}
				if (isDenine)
				{
					Log.i("Permission","Denied ");
				}
				else
				{
					onScanPress();
				}
				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
				break;
		}
	}

	/**
	 * <h2>updateUi</h2>
	 * <p> method to update ui </p>
	 * @param b: to setList clickable
	 * @param color: to setList view background color
	 * @param color1: to setList Text color
	 */
	private void updateUi(boolean b, int color, int color1)
	{
		rl_addCard_done.setBackgroundColor(color);
		tv_addCard_done.setTextColor(color1);
		tv_addCard_done.setClickable(b);
		tv_addCard_done.setEnabled(b);

		rl_addCard_scanCard.setBackgroundColor(color);
		tv_addCard_scanCard.setTextColor(color1);
		tv_addCard_scanCard.setClickable(b);
		tv_addCard_scanCard.setEnabled(b);
	}

	@Override
	public void startMainActivity()
	{
//		Intent intent = new Intent(AddCardActivity.this, MainActivity.class);
//		startActivity(intent);
//		finish();
	}

	@Override
	public void finishActivity()
	{
		finish();
	}

	@Override
	public void onError(String errorMsg) {
		progressBar_addCard.setVisibility(View.GONE);
		rl_addCard_poweredBy.setVisibility(View.VISIBLE);
		updateUi(true,colorPrimaryDark,white);
		Toast.makeText(AddCardActivity.this, errorMsg, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onValidOfCard() {
		callDoneButton();
//		presenter.addCardService(cardToken);
	}

	@Override
	public void onInvalidOfCard() {
		Utility.showAlert(You_did_not_enter_valid_card,this);
	}

	@Override
	public void onResponse(String message,String cardToken)
	{
		progressBar_addCard.setVisibility(View.GONE);
		Intent resultIntent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString(LAST_DIGITS, et_card_number.getText().toString().trim().substring
				(et_card_number.getText().toString().trim().length() - 4));
		bundle.putString(CARD_TOKEN,cardToken);
		bundle.putString(CARD_BRAND,card_input_widget_card.getCard().getBrand());
		resultIntent.putExtras(bundle);
		setResult(RESULT_OK, resultIntent);
		this.onBackPressed();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
	}

	@Override
	public void hideSoftKeyboard() {
		View view = this.getCurrentFocus();
		if (view != null) {
			InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
		getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	@Override
	public void showSoftKeyboard() {
		getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
	}
}
