package edu.cmu.hw2;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * Displays calculator and results.
 * @author Sairam Krishnan (sbkrishn)
 */
public class MainActivity extends ActionBarActivity {

	//Views (UI controls) within this Activity
	private TableLayout resultsTable;
	private EditText purchasePrice;
	private EditText downPaymentPercentage;
	private EditText mortgageTerm;
	private EditText interestRate;
	private DatePicker datePicker;
	private Button submitButton;
	private TextView monthlyPayment;
	private TextView totalPayment;
	private TextView payoffDate;
	private TextView downPaymentDollars;

	//Error message
	private final String ERROR = "Error - Please enter valid numerical inputs.";

	//Utility class to calculate monthly payments, etc.
	private MortgageCalculator calc = new MortgageCalculator();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new MainFragment()).commit();
		}
	}

	/**
	 * Initialize the views for this activity. This should be executed whenever
	 * the app is in the "onResume" state (after having started or restarted, 
	 * beginning to run).
	 */
	private void initializeViews() {
		resultsTable = (TableLayout) findViewById(R.id.results_table);
		purchasePrice = (EditText) findViewById(R.id.purchase_price);
		downPaymentPercentage = (EditText) findViewById(R.id.down_payment);
		mortgageTerm = (EditText) findViewById(R.id.mortgage_term);
		interestRate = (EditText) findViewById(R.id.interest_rate);
		datePicker = (DatePicker) findViewById(R.id.orig_date);
		submitButton = (Button) findViewById(R.id.submit_button);
		monthlyPayment = (TextView) findViewById(R.id.monthly_payment);
		totalPayment = (TextView) findViewById(R.id.total_payment);
		payoffDate = (TextView) findViewById(R.id.payoff_date);
		downPaymentDollars = (TextView) findViewById(R.id.down_payment_dollars);
	}

	/**
	 * Register an OnClick listener to the submit button, so that we
	 * know when the user has submitted the form.
	 */
	private void registerClickListener() {
		submitButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String mp = calc.getMonthlyPaymentString(purchasePrice, downPaymentPercentage,
						interestRate, mortgageTerm);
				String tp = calc.getTotalPaymentString(purchasePrice, downPaymentPercentage,
						interestRate, mortgageTerm);
				String dateResult = calc.getPayoffDate(datePicker, mortgageTerm);
				int color = Color.GREEN; //font color of results

				//Display results table.
				resultsTable.setVisibility(View.VISIBLE);

				//If there was an error, display an error message in each result
				//field, using red font color.
				if (mp == null || tp == null || dateResult == null) {
					mp = ERROR;
					tp = ERROR;
					dateResult = ERROR;
					color = Color.RED;
				}

				//Otherwise, display results using green font color.
				monthlyPayment.setText(mp);
				totalPayment.setText(tp);
				payoffDate.setText(dateResult);
				monthlyPayment.setTextColor(color);
				totalPayment.setTextColor(color);
				payoffDate.setTextColor(color);
			}			
		});
	}

	/**
	 * Show $ amount of down payment once user has finished typing.
	 */
	private void registerDownPaymentListener() {
		downPaymentPercentage.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				double price = calc.convertToDouble(purchasePrice);
				double dpp = calc.convertToDouble(downPaymentPercentage);
				String msg = "" + (price * (1-dpp/100));
				int color = Color.GRAY;

				//If price or dpp is NaN, then show nothing.
				if (Double.isNaN(price) || Double.isNaN(dpp)) {
					msg = "-";
					color = Color.RED;
				}

				//Otherwise, show the $ amount of down payment next to the
				//down payment percentage field.
				downPaymentDollars.setText(msg);
				downPaymentDollars.setTextColor(color);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		initializeViews(); //Initialize the views for this activity.
		registerClickListener(); //For submit button
		registerDownPaymentListener(); //
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Main fragment - displays calculator and accepts user input.
	 */
	public static class MainFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
}
