/*******************************************************************************
 * Copyright (c) 2010 xored software, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.logconsole.example;

import org.eclipse.dltk.logconsole.ILogConsole;
import org.eclipse.dltk.logconsole.LogConsoleType;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class LogConsoleExampleView extends ViewPart {

	private Text id;
	private Text message;

	@Override
	public void createPartControl(Composite parent) {
		Composite panel = new Composite(parent, SWT.NONE);
		panel.setLayoutData(new GridData(GridData.FILL_BOTH));
		panel.setLayout(new GridLayout(5, false));
		new Label(panel, SWT.NONE).setText("Id:");
		id = new Text(panel, SWT.BORDER);
		new Label(panel, SWT.NONE).setText("Message:");
		message = new Text(panel, SWT.BORDER);
		Button print = new Button(panel, SWT.PUSH);
		print.setText("print");
		print.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ILogConsole console = DEMO_CONSOLE.getConsole(id.getText());
				console.println(message.getText());
			}
		});
		new Label(panel, SWT.NONE).setLayoutData(GridDataFactory.swtDefaults()
				.span(4, 1).create());
		Button activate = new Button(panel, SWT.PUSH);
		activate.setText("activate");
		activate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ILogConsole console = DEMO_CONSOLE.getConsole(id.getText());
				console.activate();
			}
		});
	}

	@Override
	public void setFocus() {
		message.setFocus();
	}

	private final LogConsoleType DEMO_CONSOLE = new LogConsoleType(
			LogConsoleExampleView.class.getName()) {
		@Override
		protected String getConsoleTypeName() {
			return "Example Log Console";
		}
	};

}
