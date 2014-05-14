package edu.cmu.hw2;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.widget.DatePicker;
import android.widget.EditText;

/**
 * Calculate monthly payment, total payment, and payment date.
 * @author Sairam Krishnan (sbkrishn)
 */
public class MortgageCalculator {
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd-yyyy");
	private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
	
	/** Default, no-args constructor */
	public MortgageCalculator() {}

	/**
	 * @param v - User's floating-point input in view
	 * @return the double value contained within the view, NaN for invalid input
	 */
	public double convertToDouble(EditText v) {
		try {
			return Double.parseDouble(v.getText().toString());
		} catch (NumberFormatException e) {
			return Double.NaN;
		}
	}
	
	/**
	 * @param v - User's integral input in view
	 * @return the integral value contained within the view, -1 for invalid input
	 * (since all values should be positive in this app)
	 */
	public int convertToInt(EditText v) {
		try {
			return Integer.parseInt(v.getText().toString());
		} catch (NumberFormatException e) {
			return -1;
		}
	}
	
	/**
	 * @param purchasePrice - Total amount spent originally
	 * @param downPaymentPercentage - Amount paid off immediately, as % of total purchasePrice
	 * @param annualRatePercent - Annual interest rate, in %
	 * @param termInYears - Term of mortgage in years
	 * @return the monthly mortgage payment for the given conditions
	 */
	public double getMonthlyPayment(EditText purchasePrice, EditText downPaymentPercentage, 
			EditText annualRatePercent, EditText termInYears) {
		double price = convertToDouble(purchasePrice);
		double downPayment = price * convertToDouble(downPaymentPercentage)/100;
		double p = price - downPayment;
		double r = convertToDouble(annualRatePercent);
		int n = convertToInt(termInYears);
		int termInMonths = n * 12;
		
		//For invalid inputs, return NaN.
		if (Double.isNaN(price) || Double.isNaN(r)  || n == -1) {
			return Double.NaN;
		}
		r = r / 1200; //1200 b/c 12 months/yr and r is initially a %
		return (p*r) / (1-Math.pow(1+r, -termInMonths));
	}
	
	/**
	 * @param purchasePrice - Total amount spent originally
	 * @param downPaymentPercentage - Amount paid off immediately, as % of total purchasePrice
	 * @param annualRatePercent - Annual interest rate, in %
	 * @param termInYears - Term of mortgage in years
	 * @return the monthly mortgage payment for the given conditions as formatted String
	 */
	public String getMonthlyPaymentString(EditText purchasePrice, EditText downPaymentPercentage, 
			EditText annualRatePercent, EditText termInYears) {
		double mp = getMonthlyPayment(purchasePrice, downPaymentPercentage,
				annualRatePercent, termInYears);
		if (Double.isNaN(mp))
			return null;
		return currencyFormat.format(mp);
	}
	
	/**
	 * @param purchasePrice - Total amount spent originally
	 * @param downPaymentPercentage - Amount paid off immediately, as % of total purchasePrice
	 * @param annualRatePercent - Annual interest rate, in %
	 * @param termInYears - Term of mortgage in years
	 * @return the total mortgage payments for the given conditions
	 */
	public double getTotalPayment(EditText purchasePrice, EditText downPaymentPercentage, 
			EditText annualRatePercent, EditText termInYears) {
		double monthlyPayment = getMonthlyPayment(purchasePrice, downPaymentPercentage, 
				annualRatePercent,termInYears);
		return monthlyPayment * 12 * convertToInt(termInYears);
	}
	
	/**
	 * @param purchasePrice - Total amount spent originally
	 * @param downPaymentPercentage - Amount paid off immediately, as % of total purchasePrice
	 * @param annualRatePercent - Annual interest rate, in %
	 * @param termInYears - Term of mortgage in years
	 * @return the total mortgage payments for the given conditions (in String form)
	 */
	public String getTotalPaymentString(EditText purchasePrice, EditText downPaymentPercentage, 
			EditText annualRatePercent, EditText termInYears) {
		double tp = getTotalPayment(purchasePrice, downPaymentPercentage, 
				annualRatePercent,termInYears);
		if (Double.isNaN(tp))
			return null;
		return currencyFormat.format(tp);
	}
	
	/**
	 * @param picker - UI tool that user uses to select date on which repayment begins
	 * @param termInYears - Mortgage term in years
	 * @return the date on which the loan will be completely repaid
	 */
	public String getPayoffDate(DatePicker picker, EditText termInYears) {
		int term = convertToInt(termInYears);
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, term + picker.getYear());
		c.set(Calendar.MONTH, picker.getMonth());
		c.set(Calendar.DAY_OF_MONTH, picker.getDayOfMonth());
		return dateFormat.format(c.getTime());
	}
}