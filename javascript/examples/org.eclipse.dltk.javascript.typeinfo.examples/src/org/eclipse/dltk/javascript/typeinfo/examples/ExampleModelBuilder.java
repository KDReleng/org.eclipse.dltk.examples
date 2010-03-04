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
import org.eclipse.dltk.javascript.typeinfo.ITypeNames;
import org.eclipse.dltk.javascript.typeinfo.model.TypeInfoModelLoader;

public class ExampleModelBuilder implements IModelBuilder {

	public void processMethod(FunctionStatement statement, IMethod method) {
		if (method.getType() == null) {
			if (method.getName().toLowerCase().contains("string"))
				method.setType(TypeInfoModelLoader.getInstance().getType(
						ITypeNames.STRING));
			else if (method.getName().toLowerCase().contains("number"))
				method.setType(TypeInfoModelLoader.getInstance().getType(
						ITypeNames.NUMBER));
		}
		if ("create".equals(method.getName())
				&& method.getParameterCount() == 0) {
			IParameter p = method.createParameter();
			p.setName("name");
			p.setType(TypeInfoModelLoader.getInstance().getType(
					ITypeNames.STRING));
			method.getParameters().add(p);
		}
	}

}
