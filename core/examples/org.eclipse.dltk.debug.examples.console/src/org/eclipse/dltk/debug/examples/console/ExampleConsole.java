package org.eclipse.dltk.debug.examples.console;

import org.eclipse.dltk.debug.ui.display.AbstractEvaluateConsole;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.part.IPageBookViewPage;

public class ExampleConsole extends AbstractEvaluateConsole {

	public ExampleConsole() {
		super("ExampleConsole", null);
	}

	@Override
	public IPageBookViewPage createPage(IConsoleView view) {
		final ExampleConsolePage page = new ExampleConsolePage();
		page.setMessage("Hello, world");
		return page;
	}

}
