package org.eclipse.dltk.debug.examples.console;

import org.eclipse.dltk.debug.ui.display.IEvaluateConsole;
import org.eclipse.dltk.debug.ui.display.IEvaluateConsoleFactory;
import org.eclipse.jface.resource.ImageDescriptor;

public class ExampleConsoleFactory implements IEvaluateConsoleFactory {

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public IEvaluateConsole create() {
		return new ExampleConsole();
	}

	@Override
	public String getLabel() {
		return "ExampleConsole";
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

}
