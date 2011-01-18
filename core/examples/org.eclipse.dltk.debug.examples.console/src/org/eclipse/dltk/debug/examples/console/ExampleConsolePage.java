package org.eclipse.dltk.debug.examples.console;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.Page;

public class ExampleConsolePage extends Page {
	private Composite pgComp;

	private Label msgLabel;

	private String message = "";//$NON-NLS-1$

	/**
	 * Creates a new page. The message is the empty string.
	 */
	public ExampleConsolePage() {
		// do nothing
	}

	/*
	 * (non-Javadoc) Method declared on IPage.
	 */
	public void createControl(Composite parent) {
		// Message in default page of Outline should have margins
		pgComp = new Composite(parent, SWT.NULL);
		pgComp.setLayout(new FillLayout());

		msgLabel = new Label(pgComp, SWT.LEFT | SWT.TOP | SWT.WRAP);
		msgLabel.setText(message);
	}

	/*
	 * (non-Javadoc) Method declared on IPage.
	 */
	public Control getControl() {
		return pgComp;
	}

	/**
	 * Sets focus to a part in the page.
	 */
	public void setFocus() {
		// important to give focus to the composite rather than the label
		// as the composite will actually take focus (though hidden),
		// but setFocus on a Label is a no-op
		pgComp.setFocus();
	}

	/**
	 * Sets the message to the given string.
	 * 
	 * @param message
	 *            the message text
	 */
	public void setMessage(String message) {
		this.message = message;
		if (msgLabel != null) {
			msgLabel.setText(message);
		}
	}
}
