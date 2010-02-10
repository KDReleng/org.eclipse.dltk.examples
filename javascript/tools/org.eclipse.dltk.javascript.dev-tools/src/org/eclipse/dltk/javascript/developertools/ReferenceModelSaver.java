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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.dltk.internal.javascript.reference.resolvers.SelfCompletingReference;
import org.eclipse.dltk.internal.javascript.typeinference.IReference;
import org.eclipse.dltk.internal.javascript.typeinference.NativeArrayReference;
import org.eclipse.dltk.internal.javascript.typeinference.NativeBooleanReference;
import org.eclipse.dltk.internal.javascript.typeinference.NativeDateReference;
import org.eclipse.dltk.internal.javascript.typeinference.NativeNumberReference;
import org.eclipse.dltk.internal.javascript.typeinference.NativeStringReference;
import org.eclipse.dltk.internal.javascript.typeinference.NativeXMLReference;
import org.eclipse.dltk.internal.javascript.typeinference.StandardSelfCompletingReference;
import org.eclipse.dltk.javascript.internal.model.references.Member;
import org.eclipse.dltk.javascript.internal.model.references.Method;
import org.eclipse.dltk.javascript.internal.model.references.Parameter;
import org.eclipse.dltk.javascript.internal.model.references.ReferenceModelFactory;
import org.eclipse.dltk.javascript.internal.model.references.Type;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

public class ReferenceModelSaver {

	public static void main(String[] args) throws IOException {
		final Map<Class<? extends StandardSelfCompletingReference>, Type> types = new HashMap<Class<? extends StandardSelfCompletingReference>, Type>();
		types.put(NativeBooleanReference.class, createType("Boolean"));
		types.put(NativeNumberReference.class, createType("Number"));
		types.put(NativeStringReference.class, createType("String"));
		types.put(NativeArrayReference.class, createType("Array"));
		types.put(NativeDateReference.class, createType("Date"));
		types.put(NativeXMLReference.class, createType("XML"));
		processType(new NativeBooleanReference("A"), types);
		processType(new NativeNumberReference("A"), types);
		processType(new NativeStringReference("A"), types);
		processType(new NativeArrayReference("A"), types);
		processType(new NativeDateReference("A"), types);
		processType(new NativeXMLReference("A"), types);
		final XMIResource resource = new XMIResourceImpl();
		List<Type> typeList = new ArrayList<Type>(types.values());
		Collections.sort(typeList, new Comparator<Type>() {
			public int compare(Type o1, Type o2) {
				return String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2
						.getName());
			}
		});
		resource.getContents().addAll(typeList);
		resource.setEncoding("UTF-8");
		resource.save(System.out, null);
	}

	private static void processType(
			StandardSelfCompletingReference typeRef,
			final Map<Class<? extends StandardSelfCompletingReference>, Type> types) {
		final Type type = types.get(typeRef.getClass());
		Assert.isNotNull(type);
		final List<IReference> children = new ArrayList<IReference>(typeRef
				.getChilds(true));
		Collections.sort(children, new Comparator<IReference>() {
			public int compare(IReference o1, IReference o2) {
				return String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2
						.getName());
			}
		});
		for (IReference reference : children) {
			if (!(reference instanceof SelfCompletingReference)) {
				System.out.println(reference.getName() + " skipped");
				continue;
			}
			SelfCompletingReference ref = (SelfCompletingReference) reference;
			final Member member;
			if (reference.isFunctionRef()) {
				Method method = ReferenceModelFactory.eINSTANCE.createMethod();
				final String[] parameterNames = ref.getParameterNames();
				if (parameterNames != null) {
					for (String paramName : parameterNames) {
						final Parameter parameter = ReferenceModelFactory.eINSTANCE
								.createParameter();
						parameter.setName(paramName);
						parameter.setOptional(!isIdentifier(paramName));
						method.getParameters().add(parameter);
					}
				}
				member = method;
			} else {
				member = ReferenceModelFactory.eINSTANCE.createProperty();
			}
			member.setName(reference.getName());
			member.setType(types.get(ref.getClass()));
			member.setDescription(ref.getProposalInfo());
			type.getMembers().add(member);
		}
	}

	/**
	 * @param name
	 * @return
	 */
	private static boolean isIdentifier(String name) {
		for (int i = 0; i < name.length(); ++i) {
			if (i == 0) {
				if (!Character.isJavaIdentifierStart(name.charAt(i))) {
					return false;
				}
			} else if (!Character.isJavaIdentifierPart(name.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	private static Type createType(String name) {
		Type type = ReferenceModelFactory.eINSTANCE.createType();
		type.setName(name);
		return type;
	}
}
