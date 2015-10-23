package com.example.modernartui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ModernArtUIActivity extends Activity {
	private final static int numOfImageView = 5;
	private SeekBar seekBar;
	private ArrayList<ImageView> imageViewList = null;
	private DialogFragment mDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    
	    seekBar = (SeekBar) findViewById(R.id.seekBar1);
	    imageViewList = new ArrayList<ImageView>();
	    imageViewList.add((ImageView)findViewById(R.id.imageView1));

	    seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

	    	          int progress = 0;

	    	          @Override
	    	          public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
	    	              progress = progresValue;
	    	              //Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
	    	              
	    	              int s1 = Integer.valueOf(getString(R.string.startColorOfImageView1_string), 16).intValue();
	    	              int e1 = Integer.valueOf(getString(R.string.endColorOfImageView1_string), 16).intValue();
	    	              
	    	              int r1 = interpolateColor(s1, e1, (float)progress/(float)seekBar.getMax());
	    	              imageViewList.get(0).setBackgroundColor(r1);
	    	          }

	    	          @Override
	    	          public void onStartTrackingTouch(SeekBar seekBar) {
	    	              //Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
	    	          }
	    	         
	    	          @Override
	    	          public void onStopTrackingTouch(SeekBar seekBar) {
	    	              //textView.setText("Covered: " + progress + "/" + seekBar.getMax());
	    	              //Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
	    	          }
	    	       });


	}
	// Create Options Menu
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.top_menu, menu);
			return true;
		}

		// Process clicks on Options Menu items
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.more_info:
				mDialog = AlertDialogFragment.newInstance();
				// Show AlertDialogFragment
				mDialog.show(getFragmentManager(), "OpenBrowser");
				return true;
			default:
				return false;
			}
		}
		
		// Abort or complete ShutDown based on value of shouldContinue
		private void openBrowser(boolean open) {
			if (!open) {
				mDialog.dismiss();
			} else {
				Intent internetIntent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("http://www.moma.com"));
				internetIntent.setComponent(new ComponentName("com.android.browser","com.android.browser.BrowserActivity"));
				internetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(internetIntent);
			}
		}
	
	private float interpolate(float a, float b, float proportion) {
	    return (a + ((b - a) * proportion));
	  }

	  /** Returns an interpoloated color, between <code>a</code> and <code>b</code> */
	  private int interpolateColor(int a, int b, float proportion) {
	    float[] hsva = new float[3];
	    float[] hsvb = new float[3];
	    Color.colorToHSV(a, hsva);
	    Color.colorToHSV(b, hsvb);
	    for (int i = 0; i < 3; i++) {
	      hsvb[i] = interpolate(hsva[i], hsvb[i], proportion);
	    }
	    return Color.HSVToColor(hsvb);
	  }
	  
	// Class that creates the AlertDialog
		public static class AlertDialogFragment extends DialogFragment {

			public static AlertDialogFragment newInstance() {
				return new AlertDialogFragment();
			}

			// Build AlertDialog using AlertDialog.Builder
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				return new AlertDialog.Builder(getActivity())
						.setMessage("Do you really want to exit?")
						
						// User cannot dismiss dialog by hitting back button
						.setCancelable(false)
						
						// Set up No Button
						.setNegativeButton("Not Now",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										((ModernArtUIActivity) getActivity())
												.openBrowser(false);
									}
								})
								
						// Set up Yes Button
						.setPositiveButton("Visit MOMA",
								new DialogInterface.OnClickListener() {
									public void onClick(
											final DialogInterface dialog, int id) {
										((ModernArtUIActivity) getActivity())
												.openBrowser(true);
									}
								}).create();
			}
		}
}

