package org.eclipse.dltk.ui.tools.astview;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.core.ISourceRange;
import org.eclipse.dltk.core.SourceRange;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.IWorkbenchAdapter;

public class ASTNodeAdapterFactory implements IAdapterFactory {

    private static class ASTNodeWorkbenchAdapter implements IWorkbenchAdapter {

        private final ASTNode node;

        public ASTNodeWorkbenchAdapter(ASTNode node) {
            this.node = node;
        }

        public Object[] getChildren(Object o) {
            return node.getChilds().toArray();
        }

        public ImageDescriptor getImageDescriptor(Object object) {
            return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_FILE);
        }

        public String getLabel(Object o) {
            return node.getClass().getSimpleName() + " [" + node.sourceStart() + ',' + node.sourceEnd() + ']';
        }

        public Object getParent(Object o) {
            // TODO Auto-generated method stub
            return null;
        }

    }

    public Object getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType) {
        if (adaptableObject instanceof ASTNode) {
            final ASTNode aNode = (ASTNode) adaptableObject;
            if (adapterType == IWorkbenchAdapter.class) {
                return new ASTNodeWorkbenchAdapter(aNode);
            } else if (adapterType == ISourceRange.class) {
                return new SourceRange(aNode.sourceStart(), aNode.sourceEnd() - aNode.sourceStart());
            }
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    public Class[] getAdapterList() {
        return new Class[] { IWorkbenchAdapter.class, ISourceRange.class };
    }

}
