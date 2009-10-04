package raptor.swt;

import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class RegExDialog extends InputDialog {
	protected StyledText textToTest;
	protected StyledText regEx;

	/**
	 * InputDialog constructor
	 * 
	 * @param parent
	 *            the parent
	 * @param style
	 *            the style
	 */
	public RegExDialog(Shell parent, String title, String question) {
		// Let users override the default styles
		super(parent, title, question);
		setText(title);
		setMessage(question);
	}

	/**
	 * Creates the dialog's contents
	 * 
	 * @param shell
	 *            the dialog window
	 */
	protected void createContents(final Shell shell) {
		shell.setLayout(new GridLayout(3, false));

		Label label = new Label(shell, SWT.NONE);
		label
				.setText("Raptor uses only \\n for newlines. The java.util.regex.Pattern flags \n"
						+ "CASE_INSENSITIVE, DOTALL, and MULTILINE are enabled.\n"
						+ "Use .*word.* to search for a word.");
		// Show the message
		label = new Label(shell, SWT.NONE);
		label.setText(message);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3,
				1));

		// Display the input box
		regEx = new StyledText(shell, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER);
		// Set to 4 newlinews. This will be removed before its shown to the
		// user.
		// But it is used to force the textToTest to be four lines long.
		regEx.setText("\n\n\n\n");
		regEx.setWordWrap(true);
		regEx.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3,
				3));
		if (getInput() != null) {
			regEx.setText(getInput() + "\n\n\n\n");
		}

		// Show the message
		label = new Label(shell, SWT.NONE);
		label.setText("Enter some text to test below:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2,
				1));

		// Show the message
		final Label successLabel = new Label(shell, SWT.NONE);
		successLabel.setText("          ");
		successLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true,
				false, 1, 1));

		// Display the input box
		textToTest = new StyledText(shell, SWT.V_SCROLL | SWT.MULTI
				| SWT.BORDER);
		textToTest.setWordWrap(true);
		// Set to 4 newlinews. This will be removed before its shown to the
		// user.
		// But it is used to force the textToTest to be four lines long.
		textToTest.setText("\n\n\n\n");
		textToTest.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				3, 3));

		// Create the OK button and add a handler
		// so that pressing it will set input
		// to the entered value
		Button test = new Button(shell, SWT.PUSH);
		test.setText("Test");
		test
				.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false,
						1, 1));
		test.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				input = regEx.getText();
				String testText = textToTest.getText();
				boolean isSuccessful = false;
				System.err.println("REGEX: " + input + " testText=" + testText);

				try {
					Pattern pattern = Pattern.compile(input, Pattern.MULTILINE
							| Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
					isSuccessful = pattern.matcher(testText).matches();
				} catch (Throwable t) {
				}

				if (isSuccessful) {
					successLabel.setText("Successful");
					successLabel.setForeground(shell.getDisplay()
							.getSystemColor(SWT.COLOR_GREEN));
				} else {
					successLabel.setText("Failed");
					successLabel.setForeground(shell.getDisplay()
							.getSystemColor(SWT.COLOR_RED));
				}
			}
		});

		Button ok = new Button(shell, SWT.PUSH);
		ok.setText("OK");
		ok.setLayoutData(new GridData(SWT.END, SWT.CENTER, false, false, 1, 1));
		ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				input = regEx.getText();
				shell.close();
			}
		});

		// Create the cancel button and add a handler
		// so that pressing it will set input to null
		Button cancel = new Button(shell, SWT.PUSH);
		cancel.setText("Cancel");
		cancel.setLayoutData(new GridData(SWT.END, SWT.CENTER, false, false, 1,
				1));
		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				input = null;
				shell.close();
			}
		});

		// Set the OK button as the default, so
		// user can type input and press Enter
		// to dismiss
		// shell.setDefaultButton(test);
	}

	/**
	 * Opens the dialog and returns the input
	 * 
	 * @return String
	 */
	public String open() {
		// Create the dialog window
		Shell shell = new Shell(getParent(), getStyle());
		shell.setText(getText());
		createContents(shell);
		shell.pack();
		textToTest.setText("");
		regEx.setText(regEx.getText().trim());
		shell.open();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		// Return the entered value, or null
		return input;
	}
}