/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.ui.tools.astview;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.dltk.ast.parser.IModuleDeclaration;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ISourceRange;
import org.eclipse.dltk.core.SourceParserUtil;
import org.eclipse.dltk.internal.ui.editor.EditorUtility;
import org.eclipse.dltk.internal.ui.editor.ScriptEditor;
import org.eclipse.dltk.internal.ui.text.IScriptReconcilingListener;
import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.dltk.utils.AdaptUtils;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.ViewPart;

public class ASTView extends ViewPart implements IScriptReconcilingListener {

    private ScriptEditor editor;
    private TreeViewer viewer;
    private IModuleDeclaration module;

    /**
     * The <code>PageBookView</code> implementation of this
     * <code>IWorkbenchPart</code> method creates a <code>PageBook</code>
     * control with its default page showing.
     */
    public void createPartControl(Composite parent) {
        final Tree tree = new Tree(parent, SWT.NONE);
        tree.setLayoutData(new GridData(GridData.FILL_BOTH));
        viewer = new TreeViewer(tree);
        viewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
        viewer.setUseHashlookup(true);
        viewer.setContentProvider(new BaseWorkbenchContentProvider());
        viewer.setLabelProvider(new WorkbenchLabelProvider());
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                if (event.getSelection() instanceof IStructuredSelection) {
                    final Object element = ((IStructuredSelection) event.getSelection()).getFirstElement();
                    if (element != null) {
                        ISourceRange range = AdaptUtils.getAdapter(element, ISourceRange.class);
                        if (range != null) {
                            editor.selectAndReveal(range.getOffset(), range.getLength());
                        }
                    }
                }
            }
        });
        final IEditorPart editorPart = getSite().getPage().getActiveEditor();
        if (editorPart instanceof ScriptEditor) {
            editor = (ScriptEditor) editorPart;
            connectEditor();
            parseInput();
        }
        viewer.setInput(module);
    }

    private void parseInput() {
        if (editor != null) {
            final ISourceModule inputModelElement = EditorUtility.getEditorInputModelElement(editor, false);
            this.module = SourceParserUtil.parse(inputModelElement, null);
        } else {
            module = null;
        }
    }

    private void connectEditor() {
        editor.addReconcileListener(this);
    }

    private void disconnectEditor() {
        if (editor != null) {
            editor.removeReconcileListener(this);
            editor = null;
        }
    }

    @Override
    public void init(IViewSite site) throws PartInitException {
        super.init(site);
        getSite().getPage().addPartListener(partListener);
    }

    @Override
    public void dispose() {
        getSite().getPage().removePartListener(partListener);
        disconnectEditor();
        super.dispose();
    }

    private final IPartListener2 partListener = new IPartListener2() {

        public void partActivated(IWorkbenchPartReference partRef) {
            final IWorkbenchPart part = partRef.getPart(false);
            if (part instanceof ScriptEditor) {
                ASTView.this.partActivated((ScriptEditor) part);
            }
        }

        public void partBroughtToTop(IWorkbenchPartReference partRef) {
        }

        public void partClosed(IWorkbenchPartReference partRef) {
            final IWorkbenchPart part = partRef.getPart(false);
            if (part instanceof ScriptEditor) {
                ASTView.this.partClosed((ScriptEditor) part);
            }
        }

        public void partDeactivated(IWorkbenchPartReference partRef) {
        }

        public void partOpened(IWorkbenchPartReference partRef) {
        }

        public void partHidden(IWorkbenchPartReference partRef) {
        }

        public void partVisible(IWorkbenchPartReference partRef) {
        }

        public void partInputChanged(IWorkbenchPartReference partRef) {
            final IWorkbenchPart part = partRef.getPart(false);
            if (part instanceof ScriptEditor) {
                if (part == editor) {
                    ASTView.this.updateInput();
                }
            }
        }
    };

    protected void partActivated(ScriptEditor newEditor) {
        if (newEditor != editor) {
            disconnectEditor();
            editor = newEditor;
            connectEditor();
            updateInput();
        }
    }

    protected void partClosed(ScriptEditor closedEditor) {
        if (editor == closedEditor) {
            disconnectEditor();
            editor = null;
            updateInput();
        }
    }

    protected void updateInput() {
        parseInput();
        viewer.setInput(module);
    }

    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
    }

    public void aboutToBeReconciled() {
    }

    public void reconciled(ISourceModule module, boolean forced, IProgressMonitor progressMonitor) {
        DLTKUIPlugin.getStandardDisplay().asyncExec(new Runnable() {
            public void run() {
                SafeRunner.run(new SafeRunnable() {
                    public void run() throws Exception {
                        updateInput();
                    }
                });
            }
        });
    }
}
