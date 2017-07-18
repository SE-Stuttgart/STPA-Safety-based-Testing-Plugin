package xstampp.stpatcgenerator.util.propertytester;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.PlatformUI;

/**
 * This class checks if the active perspective is for STPA TCGenerator.
 * @author Ting Luk-He
 *
 */
public class PerspectivePropertyTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		/*
		 * Returns true if the actual perspective is STPA TC Generator perspective
		 */
		IPerspectiveDescriptor perspective = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getPerspective();
		String perspectiveId = perspective.getId();
		if (perspectiveId.equals("xstampp.stpatcgenerator.perspective")) {
			return true;
		}
		return false;
	}

}
