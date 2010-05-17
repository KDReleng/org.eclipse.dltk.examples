package org.eclipse.dltk.devtools.handleidentitiers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IModelElementVisitor;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.ScriptModelUtil;
import org.eclipse.dltk.ui.viewsupport.ScriptUILabelProvider;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

public class HandleIdentifiersView extends ViewPart {

	Composite content;
	Combo projectNames;
	TableViewer output;

	private static class HandleIdentifierLabelProvider extends LabelProvider
			implements ITableLabelProvider, IColorProvider {

		private final ScriptUILabelProvider scriptProvider = new ScriptUILabelProvider();

		@Override
		public String getText(Object element) {
			return getColumnText(element, 0);
		}

		public Image getColumnImage(Object element, int columnIndex) {
			if (element instanceof Record) {
				final Record record = (Record) element;
				switch (columnIndex) {
				case 0:
					return scriptProvider.getImage(record.element);
				}
			}
			return super.getImage(element);
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof Record) {
				final Record record = (Record) element;
				switch (columnIndex) {
				case 0:
					return scriptProvider.getText(record.element)
							+ " ["
							+ ScriptModelUtil
									.describeElementType(record.element
											.getElementType()) + "]";
				case 1:
					return record.identifier;
				case 2:
					return record.status.toString();
				}
			}
			return super.getText(element);
		}

		public Color getBackground(Object element) {
			return null;
		}

		public Color getForeground(Object element) {
			if (element instanceof Record) {
				final Record record = (Record) element;
				if (record.status == Status.WRONG
						|| record.status == Status.EXCEPTION) {
					return Display.getDefault().getSystemColor(SWT.COLOR_RED);
				} else if (record.status != Status.OK) {
					return Display.getDefault().getSystemColor(
							SWT.COLOR_DARK_GRAY);
				}
			}
			return null;
		}

	}

	private enum Status {
		OK, EXCEPTION, WRONG, NULL
	}

	private static class Record {

		public Record(IModelElement element, String identifier, Status status) {
			this.element = element;
			this.identifier = identifier;
			this.status = status;
		}

		final IModelElement element;
		final String identifier;
		final Status status;
	}

	@Override
	public void createPartControl(Composite parent) {
		content = new Composite(parent, SWT.NONE);
		content.setLayoutData(new GridData(GridData.FILL_BOTH));
		content.setLayout(new GridLayout());
		final Composite top = new Composite(content, SWT.NONE);
		top.setLayout(new GridLayout(3, false));
		top.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		new Label(top, SWT.NONE).setText("Project");
		projectNames = new Combo(top, SWT.BORDER | SWT.READ_ONLY);
		output = new TableViewer(content);
		output.getTable().setHeaderVisible(true);
		final TableColumn nameColumn = new TableColumn(output.getTable(),
				SWT.LEFT);
		nameColumn.setText("Name");
		nameColumn.setWidth(200);
		final TableColumn idColumn = new TableColumn(output.getTable(),
				SWT.LEFT);
		idColumn.setText("Identifier");
		idColumn.setWidth(500);
		final TableColumn statusColumn = new TableColumn(output.getTable(),
				SWT.LEFT);
		statusColumn.setText("Status");
		statusColumn.setWidth(100);
		output.setContentProvider(ArrayContentProvider.getInstance());
		output.setLabelProvider(new HandleIdentifierLabelProvider());
		output.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		final List<String> names = new ArrayList<String>();
		try {
			for (IScriptProject project : DLTKCore.create(getWorkspaceRoot())
					.getScriptProjects()) {
				names.add(project.getElementName());
			}
		} catch (ModelException e) {
			e.printStackTrace();
		}
		projectNames.setItems(names.toArray(new String[names.size()]));
		Button btnDo = new Button(top, SWT.PUSH);
		btnDo.setText("Validate");
		btnDo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SafeRunnable r = new SafeRunnable() {
					public void run() throws Exception {
						IScriptProject project = DLTKCore
								.create(getWorkspaceRoot().getProject(
										projectNames.getText()));
						validateHandleIdentifiers(project);
					}
				};
				SafeRunner.run(r);
			}
		});
	}

	private void validateHandleIdentifiers(IScriptProject project)
			throws ModelException {
		final List<Record> records = new ArrayList<Record>();
		project.accept(new IModelElementVisitor() {

			public boolean visit(IModelElement element) {
				final String identifier = element.getHandleIdentifier();
				Status status;
				try {
					final IModelElement x = DLTKCore.create(identifier);
					if (x == null) {
						status = Status.NULL;
					} else if (x.equals(element)) {
						status = Status.OK;
					} else {
						status = Status.WRONG;
					}
				} catch (Exception e) {
					status = Status.EXCEPTION;
				}
				records.add(new Record(element, identifier, status));
				return true;
			}
		});
		output.setInput(records);
	}

	@Override
	public void setFocus() {
		output.getTable().setFocus();
	}

	protected static IWorkspaceRoot getWorkspaceRoot() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}

}
