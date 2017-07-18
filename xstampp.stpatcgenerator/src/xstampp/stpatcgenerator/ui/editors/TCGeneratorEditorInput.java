package xstampp.stpatcgenerator.ui.editors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import messages.Messages;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import xstampp.Activator;
import xstampp.preferences.IPreferenceConstants;
import xstampp.stpatcgenerator.util.TCGeneratorPluginUtils;
import xstampp.ui.editors.STPAEditorInput;
import xstampp.ui.navigation.StepSelector;
import xstampp.util.STPAPluginUtils;

/**
 * The Standard Editor input for this Platform
 * 
 * @author Ting Luk-He
 * @see IEditorInput
 *
 */
public class TCGeneratorEditorInput implements IEditorInput {
	private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	private UUID projectId;
	private String editorId;
	private UUID id;
	private String editorName;
	private String pathHistory;
	private List<String> additionalViews;
	private boolean isActive;
	private boolean lock = false;
	
	/**
	 * The Default editorInput
	 *
	 * @author Ting Luk-He
	 *
	 * @param projectId
	 *            the id of the project which is related to this input
	 * @param editorId
	 *            {@link StepSelector#getDefaultEditorId()}
	 *
	 * @param refItem
	 *            {@link StepSelector}
	 */
	public TCGeneratorEditorInput (String editorId) {
		this.editorId = editorId;
		this.id = UUID.randomUUID();
		this.editorName = "";
	}
	
	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		// TODO Auto-generated method stub
		return "Tool Tip";
	}
	
	@Override
	public int hashCode() {
		return (int) this.id.getMostSignificantBits();
	}

     @Override
     public boolean equals(Object obj) {
             if (this == obj)
                     return true;
             if (obj == null)
                     return false;
             if (getClass() != obj.getClass())
                     return false;
             TCGeneratorEditorInput other = (TCGeneratorEditorInput) obj;
             if (id != other.id)
                     return false;
             return true;
     }
	/**
	 * 
	 * @return the id of the project which is related to this input
	 */
	public UUID getProjectID() {
		return this.projectId;
	}
	
	/**
	 * 
	 * @return the Name of the editor
	 */
	public String getEditorName() {
		return editorName;
	}
	
	/**
	 * 
	 * @param editorName
	 */
	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}
	
	/**
	 * 
	 * @return the path history
	 */
	public String getPathHistory() {
		return pathHistory;
	}
	
	/**
	 * 
	 * @param pathHistory
	 * 			the path history to set
	 */
	public void setPathHistory(String pathHistory) {
		this.pathHistory = pathHistory;
	}
	
	/**
	 * called when the editor handeled by this input is opened updates the
	 * workbench
	 *
	 */
	public void activate() {
		if (this.isActive || this.lock) {
			return;
		}
		this.lock = true;
		this.isActive = true;
		for (int i = 0; this.additionalViews != null && i < this.additionalViews.size(); i++) {
			IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.findView(this.additionalViews.get(i));
			if (!PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().isPartVisible(part)) {
				try {
					if (this.additionalViews.get(i).equals("A-CAST.view1")) {

					} else {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
								.showView(this.additionalViews.get(i));
					}
				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
				.setText(Messages.PlatformName + " -" + this.pathHistory);
		
		this.lock = false;
		if (!this.store.getBoolean(IPreferenceConstants.USE_NAVIGATION_COLORS)) {
			return;
		}
	}
	
	/**
	 * deactivate the editor
	 */
	public void deactivate() {
		if (!this.lock && isActive) {

			this.lock = true;
			this.isActive = false;
			this.lock = false;
		}
	}
	
	public void addViews(List<String> view) {
		this.additionalViews = view;
	}
	

	public void runCommand(String commandName) {
//		activate();
		
		switch (commandName) {
			case "open state flow":
				TCGeneratorPluginUtils.executeCommand("xstampp.stpatcgenerator.command.openStateFlowEditor");
				break;
			case "generate smv without stpa":
				TCGeneratorPluginUtils.executeCommand("xstampp.stpatcgenerator.command.generateSMVWithoutSTPA");
				break;
			case "generate smv with stpa":
				TCGeneratorPluginUtils.executeCommand("xstampp.stpatcgenerator.command.generateSMVWithSTPA");
				break;
			case "open state flow tree graph":
				TCGeneratorPluginUtils.executeCommand("xstampp.stpatcgenerator.command.openStateflowTreeGraph");
				break;
			case "open efsm tree graph":
				TCGeneratorPluginUtils.executeCommand("xstampp.stpatcgenerator.command.openEFSMTreeGraph");
				break;
			case "open test case result":
				TCGeneratorPluginUtils.executeCommand("xstampp.stpatcgenerator.command.openTestCaseResult");
				break;
			default:
				break;
		}
			
	}

}
