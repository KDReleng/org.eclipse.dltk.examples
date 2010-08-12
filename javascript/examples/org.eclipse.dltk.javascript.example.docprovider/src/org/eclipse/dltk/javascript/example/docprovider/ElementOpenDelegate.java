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
package org.eclipse.dltk.javascript.example.docprovider;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.javascript.typeinfo.model.Element;
import org.eclipse.dltk.ui.IOpenDelegate;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;

public class ElementOpenDelegate implements IOpenDelegate {

	@Override
	public boolean supports(Object object) {
		return object instanceof Element;
	}

	@Override
	public String getName(Object object) {
		if (object instanceof Element) {
			return ((Element) object).getName();
		}
		return null;
	}

	@Override
	public IEditorPart openInEditor(Object object, boolean activate)
			throws PartInitException, CoreException {
		MessageDialog.openInformation(null, getClass().getSimpleName()
				+ ".openInEditor", object.toString());
		return null;
	}

}
