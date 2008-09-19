/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.ruby.testing.example.category;

import org.eclipse.dltk.testing.ITestCategoryEngine;
import org.eclipse.dltk.testing.ITestRunnerUI;
import org.eclipse.dltk.testing.TestCategoryDescriptor;

public class ExampleTestCategoryEngine implements ITestCategoryEngine {

	public TestCategoryDescriptor getCategory(String id, String name,
			boolean isSuite) {
		final String lowerCaseName = name.toLowerCase();
		if (lowerCaseName.indexOf("apple") >= 0) { //$NON-NLS-1$
			return getAppleCategory();
		} else if (lowerCaseName.indexOf("orange") >= 0) { //$NON-NLS-1$
			return getOrangeCategory();
		} else {
			return null;
		}
	}

	private TestCategoryDescriptor apple = null;

	/**
	 * @return
	 */
	private TestCategoryDescriptor getAppleCategory() {
		if (apple == null) {
			apple = new TestCategoryDescriptor("APPLE", "Apple"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return apple;
	}

	private TestCategoryDescriptor orange = null;

	/**
	 * @return
	 */
	private TestCategoryDescriptor getOrangeCategory() {
		if (orange == null) {
			orange = new TestCategoryDescriptor("ORANGE", "Orange"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return orange;
	}

	public TestCategoryDescriptor[] getInitialCategories() {
		/* does not use */
		return null;
	}

	public boolean initialize(ITestRunnerUI runnerUI) {
		/* operates on all launches */
		return true;
	}

}
