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
		final Property id = typeInfoFactory.createProperty();
		id.setName("id");
		id.setType(numberType);
		exampleType.getMembers().add(id);
		//
		final Property name = typeInfoFactory.createProperty();
		name.setName("name");
		name.setType(stringType);
		exampleType.getMembers().add(name);
		//
		resource.getContents().add((EObject) exampleType);
		resource.setEncoding("UTF-8");
		resource.save(System.out, null);
		return EXIT_OK;
	}

	public void stop() {
	}
}
