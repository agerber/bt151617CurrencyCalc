package edu.uchicago.cs.gerber.hw2;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity implements OnClickListener,
		OnItemSelectedListener {
	// http://www.xe.com/iso4217.php (get the currency codes)
	Button btnCalc;
	TextView txtConverted;
	EditText edtAmount;

	static final String CURRENCY_KEY = "rhs";
	static final String ERROR_KEY = "error";
	static final String URL_WIKI = "http://en.m.wikipedia.org/wiki/ISO_4217";

	SharedPreferences shp;
	// used as keys for shared prefs
	static final String FOR = "FOR";
	static final String HOM = "HOM";
	String[] strNames;
	String[] strCodes;
	String[] strFulls;
	Spinner spnFor, spnHom;
	String strForCode, strHomCode;
	
	//done after phase 1
	//CurrencyTask ctkTask;

	// TODO 2/ create PrefsManager util class

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		strFulls = getResources().getStringArray(R.array.currs);
		strCodes = new String[strFulls.length];
		strNames = new String[strFulls.length];
		assignCodes(strFulls, strCodes, strNames);

		shp = PreferenceManager.getDefaultSharedPreferences(this);

		txtConverted = (TextView) findViewById(R.id.txtConverted);
		edtAmount = (EditText) findViewById(R.id.edtAmount);
		btnCalc = (Button) findViewById(R.id.btnCalc);
		spnFor = (Spinner) findViewById(R.id.spnFor);
		spnHom = (Spinner) findViewById(R.id.spnHom);

		btnCalc.setOnClickListener(this);

		ArrayAdapter<String> araAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, strNames);
		araAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnFor.setAdapter(araAdapter);
		spnHom.setAdapter(araAdapter);
		spnFor.setBackgroundColor(Color.parseColor("#660000"));
		spnHom.setBackgroundColor(Color.parseColor("#660000"));

		spnFor.setOnItemSelectedListener(this);
		spnHom.setOnItemSelectedListener(this);

		// if it's the first time (savedInstanceState will be null) &&
		// sharedPrefs don't have values, then
		if (savedInstanceState == null
				&& (PrefsMgr.getLocaleInt(shp, FOR) == -99 || PrefsMgr.getLocaleInt(shp, HOM) == -99)) {
			spnFor.setSelection(findPositionGivenCode("cny", strCodes));
			spnHom.setSelection(findPositionGivenCode("usd", strCodes));

		} else {
			spnFor.setSelection(PrefsMgr.getLocaleInt(shp, FOR));
			spnHom.setSelection(PrefsMgr.getLocaleInt(shp, HOM));

		}

	}



	private int findPositionGivenCode(String strCode, String[] strCodes) {

		for (int nC = 0; nC < strCodes.length; nC++) {
			if (strCodes[nC].equalsIgnoreCase(strCode)) {
				return nC;
			}
		}
		return -99;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.action_bar, menu);
		return true;
	}

	// ActionBar example: you need this code to program the behavior
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.mnu_invert:
			invertCurrencies();
			break;

		case R.id.mnu_codes:

			launchBrowser(URL_WIKI);

			break;
		case R.id.mnu_exit:
			finish();
			break;

		default:
			break;
		}

		return true;
	}

	@Override
	public void onClick(View v) {

		String strAmt = edtAmount.getText().toString();
		if (strAmt.isEmpty() || strAmt == null) {
			strAmt = "1";
		}
		
		if( isOnline()){
			//ctkTask = new CurrencyTask();
			//ctkTask.execute(strAmt, strForCode, strHomCode);
		} else {
			toastNoConnectivity();
		}

	}

	private void toastNoConnectivity() {
		Toast.makeText(
				getApplicationContext(),
				"There is no network connectivity",
				Toast.LENGTH_LONG).show();
	}

	private void launchBrowser(String strUrl) {

		if( isOnline()){
			Uri uriUrl = Uri.parse(strUrl);
			Intent itn = new Intent(Intent.ACTION_VIEW, uriUrl);
			startActivity(itn);
		} else {
			
			toastNoConnectivity();
		}


	}

	private void invertCurrencies() {

		int nFor = spnFor.getSelectedItemPosition();
		int nHom = spnHom.getSelectedItemPosition();

		spnFor.setSelection(nHom);
		spnHom.setSelection(nFor);

		PrefsMgr.setLocaleInt(shp, FOR, nHom);
		PrefsMgr.setLocaleInt(shp, HOM, nFor);

	}

	private void assignCodes(String[] strFulls, String[] strCodes,
			String[] strNames) {

		int nPipe;
		for (int nC = 0; nC < strFulls.length; nC++) {
			nPipe = strFulls[nC].indexOf('|');
			strCodes[nC] = strFulls[nC].substring(0, nPipe);
			strNames[nC] = strFulls[nC].substring(nPipe + 1,
					strFulls[nC].length());
		}

	}

	

	// ################################################
	// implemented methods of OnItemSelectedListener
	// ################################################
	@Override
	public void onItemSelected(AdapterView<?> parent, View vwx, int pos, long id) {

		System.out.println();
		switch (parent.getId()) {
		case R.id.spnFor:
			strForCode = strCodes[pos];
			PrefsMgr.setLocaleInt(shp, FOR, pos);
			break;
		case R.id.spnHom:
			strHomCode = strCodes[pos];
			PrefsMgr.setLocaleInt(shp, HOM, pos);
			break;
		default:
			break;
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
	
	//you must put <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /> on manifest
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}

}

// for to home
// If you want currency converter £ to dollars, use it like this -
// http://www.google.com/ig/calculator?hl=en&q=1GBP=?USD
// {lhs: "1 British pound",rhs: "1.5126 U.S. dollars",error: "",icc: true}

// }
