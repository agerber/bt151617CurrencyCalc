
package edu.uchicago.cs.gerber.hw2;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;


//created by Mohammed Al-Husein
public class Splash extends Activity {

	// splash display length in milliseconds
	private final int SPLASH_DISPLAY_LENGHT = 4000;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splashscreen);
	}

		
}