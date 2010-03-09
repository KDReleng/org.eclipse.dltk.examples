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

import org.eclipse.dltk.javascript.typeinfo.ITypeInfoContext;
import org.eclipse.dltk.javascript.typeinfo.ITypeNames;
import org.eclipse.dltk.javascript.typeinfo.ITypeProvider;
import org.eclipse.dltk.javascript.typeinfo.model.Method;
import org.eclipse.dltk.javascript.typeinfo.model.Type;
import org.eclipse.dltk.javascript.typeinfo.model.TypeInfoModelFactory;
import org.eclipse.dltk.javascript.typeinfo.model.TypeKind;

public class ExampleTypeProvider implements ITypeProvider {

	public Type getType(ITypeInfoContext context, String typeName) {
		if ("ExampleType".equals(typeName)) {
			Type type = TypeInfoModelFactory.eINSTANCE.createType();
			type.setName(typeName);
			type.setKind(TypeKind.JAVA);
			Method method1 = TypeInfoModelFactory.eINSTANCE.createMethod();
			method1.setName("getDistance");
			method1.setType(context.getType(ITypeNames.NUMBER));
			type.getMembers().add(method1);
			return type;
		}
		return null;
	}

}
