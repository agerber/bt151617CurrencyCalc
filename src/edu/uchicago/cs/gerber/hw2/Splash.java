
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

		/*
		 * New Handler to start the Menu-Activity and close this Splash-Screen
		 * after some seconds.
		 */
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				/* Create an Intent that will start the Menu-Activity. */
				Intent mainIntent = new Intent(Splash.this, Main.class);
				startActivity(mainIntent);

				/*
				 * Once Main is popped off the stack, finish this activity
				 * immediately
				 */
				Splash.this.finish();
			}
		}, SPLASH_DISPLAY_LENGHT);
	}
}