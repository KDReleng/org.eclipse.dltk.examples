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

import java.io.Reader;

import org.eclipse.dltk.core.IDocumentableElement;
import org.eclipse.dltk.core.IMember;
import org.eclipse.dltk.javascript.typeinfo.model.Element;
import org.eclipse.dltk.ui.documentation.IDocumentationResponse;
import org.eclipse.dltk.ui.documentation.IScriptDocumentationProvider;
import org.eclipse.dltk.ui.documentation.IScriptDocumentationProviderExtension2;
import org.eclipse.dltk.ui.documentation.TextDocumentationResponse;

public class ExampleElementDocumentationProvider implements
		IScriptDocumentationProvider, IScriptDocumentationProviderExtension2 {

	@Override
	public Reader getInfo(IMember element, boolean lookIntoParents,
			boolean lookIntoExternal) {
		return null;
	}

	@Override
	public Reader getInfo(String content) {
		return null;
	}

	@Override
	public IDocumentationResponse getDocumentationFor(IDocumentableElement element) {
		if (element instanceof ElementWrapper) {
			final Element jsElement = ((ElementWrapper) element).element;
			if (jsElement.getDescription() != null
					&& jsElement.getDescription().length() != 0) {
				return new TextDocumentationResponse(element, jsElement
						.getDescription());
			}
		}
		return null;
	}

}
