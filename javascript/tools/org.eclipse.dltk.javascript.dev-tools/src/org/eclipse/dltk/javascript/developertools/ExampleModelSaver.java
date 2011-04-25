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
package org.eclipse.dltk.javascript.developertools;

import org.eclipse.core.runtime.Assert;
import org.eclipse.dltk.javascript.typeinfo.ITypeNames;
import org.eclipse.dltk.javascript.typeinfo.TypeUtil;
import org.eclipse.dltk.javascript.typeinfo.model.Constructor;
import org.eclipse.dltk.javascript.typeinfo.model.JSType;
import org.eclipse.dltk.javascript.typeinfo.model.Member;
import org.eclipse.dltk.javascript.typeinfo.model.Parameter;
import org.eclipse.dltk.javascript.typeinfo.model.ParameterKind;
import org.eclipse.dltk.javascript.typeinfo.model.Property;
import org.eclipse.dltk.javascript.typeinfo.model.Type;
import org.eclipse.dltk.javascript.typeinfo.model.TypeInfoModelFactory;
import org.eclipse.dltk.javascript.typeinfo.model.TypeInfoModelLoader;
import org.eclipse.dltk.javascript.typeinfo.model.TypeKind;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public class ExampleModelSaver implements IApplication {

	final TypeInfoModelFactory typeInfoFactory = TypeInfoModelFactory.eINSTANCE;

	public Object start(IApplicationContext context) throws Exception {
		Type stringType = TypeInfoModelLoader.getInstance().getType("String");
		Type numberType = TypeInfoModelLoader.getInstance().getType("Number");
		//
		final XMIResource resource = new XMIResourceImpl();
		final Type exampleType = typeInfoFactory.createType();
		exampleType.setName("Example");
		exampleType.setKind(TypeKind.JAVASCRIPT);
		//
		exampleType.getMembers().add(
				createProperty("id", TypeUtil.ref(numberType)));
		exampleType.getMembers()
				.add(createProperty("uid", TypeUtil.ref("UID")));
		exampleType.getMembers().add(
				createProperty("undef",
						TypeInfoModelFactory.eINSTANCE.createUndefinedType()));
		exampleType.getMembers().add(
				createProperty("name", TypeUtil.ref(stringType)));
		exampleType.getMembers().add(
				createProperty("names",
						TypeUtil.arrayOf(TypeUtil.ref(stringType))));
		//
		final Constructor constructor = typeInfoFactory.createConstructor();
		constructor.setName(exampleType.getName());
		final Parameter parameter = typeInfoFactory.createParameter();
		parameter.setName("value");
		parameter.setType(TypeUtil.ref(ITypeNames.OBJECT));
		parameter.setKind(ParameterKind.OPTIONAL);
		constructor.getParameters().add(parameter);
		exampleType.setConstructor(constructor);
		Assert.isTrue(constructor.getDeclaringType() == exampleType);
		//
		resource.getContents().add((EObject) exampleType);
		//
		resource.setEncoding("UTF-8");
		resource.save(System.out, null);
		return EXIT_OK;
	}

	private Member createProperty(String name, JSType type) {
		final Property property = typeInfoFactory.createProperty();
		property.setName(name);
		property.setType(type);
		return property;
	}

	public void stop() {
	}
}
