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
package org.eclipse.dltk.javascript.typeinfo.examples;

import org.eclipse.dltk.javascript.ast.FunctionStatement;
import org.eclipse.dltk.javascript.typeinfo.IModelBuilder;
import org.eclipse.dltk.javascript.typeinfo.ITypeInfoContext;
import org.eclipse.dltk.javascript.typeinfo.ITypeNames;

public class ExampleModelBuilder implements IModelBuilder, ITypeNames {

	public void processMethod(ITypeInfoContext context,
			FunctionStatement statement, IMethod method) {
		if (method.getType() == null) {
			if (method.getName().toLowerCase().contains("string"))
				method.setType(context.getType(STRING));
			else if (method.getName().toLowerCase().contains("number"))
				method.setType(context.getType(NUMBER));
		}
		if ("create".equals(method.getName())
				&& method.getParameterCount() == 0) {
			IParameter p = method.createParameter();
			p.setName("name");
			p.setType(context.getType(STRING));
			method.getParameters().add(p);
		}
	}

}
