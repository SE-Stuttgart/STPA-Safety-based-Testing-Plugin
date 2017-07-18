package xstampp.stpatcgenerator.util.propertytester;

import java.util.UUID;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import xstampp.ui.common.ProjectManager;

public class ProjectTypePropertyTester extends PropertyTester{
	
	public static final String PROPERTY_NAMESPACE = "xstampp.stpatcgenerator.util.propertytester";
	public static final String PROPERTY_PROJECT_TYPE = "projectType";
	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		// TODO Auto-generated method stub
		System.out.println("Property Test");
		if(PROPERTY_PROJECT_TYPE.equals(property)) {
			
			return true;									
		}
		return true;
	}

}
