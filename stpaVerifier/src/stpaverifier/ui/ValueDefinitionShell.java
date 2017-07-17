package stpaverifier.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

/**
 * this class constructs an object which can open a shell that provides a tree view
 * of all defined values/variables in a provided stpa safety analysis data model
 * 
 * @since 1.0.0
 * @author Lukas Balzer
 *
 */
public class ValueDefinitionShell{
	public static final String ID="stpaVerifier.shell.valueDefinitions";

	private Map<String, Integer> resultMap;

	private Tree tree;

	private boolean apply;

	private Button bOk;

	/**
	 * constructs and opens a shell
	 * The shell is defined to be application modal so that the application must wait for this method to return 
	 *
	 * @param parent the parent shell on which the shell is drawn
	 * @param valuesMap a map that contains a list of value names mapped to the addressed variable name
	 * 				
	 * @return a list of define entries which can be included in a c code file
	 */
	public List<String> open(Shell parent, Map<String,List<String>> valuesMap){
		Map<String, List<String>> valuesToVariablesMap = valuesMap;
		
		if(!(valuesToVariablesMap == null || valuesToVariablesMap.isEmpty())){
			final Shell exampleShell = new Shell(parent,SWT.MIN |SWT.APPLICATION_MODAL |SWT.BORDER);
			exampleShell.setText("Please check all value definitions you want to include as '#define' directive");
			exampleShell.setLayout(new GridLayout(2, false));
			tree = new Tree(exampleShell, SWT.CHECK|SWT.FULL_SELECTION|SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
			tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		    tree.setHeaderVisible(true);
		    TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
		    column1.setText("identifier");
		    column1.setWidth(200);
		    TreeColumn column3 = new TreeColumn(tree, SWT.RIGHT);
		    column3.setText("#define");
		    column3.setWidth(200);
		    int preDefine = 0;
		    boolean greyVariable = true;
		    for (Entry<String, List<String>> variableEntry : valuesToVariablesMap.entrySet()) {
		    	TreeItem item = new TreeItem(tree, SWT.NONE);
		    	item.setText(new String[] { variableEntry.getKey(), "-" });
		    	greyVariable = true;
		    	for (String value:variableEntry.getValue()) {
		    		TreeItem subItem = new TreeItem(item, SWT.NONE);
		    		subItem.setText(new String[] { value, "" + preDefine });
		    		subItem.setChecked(checkValue(value));
		    		String numberText="-";
		    		if(!subItem.getChecked()){
						greyVariable = false;
					}else{
				    	preDefine++;
				    	numberText = String.valueOf(preDefine);
					}
		    		subItem.setText(new String[] { value, numberText });
		    	}
		    	if(greyVariable){
		    		item.setChecked(true);
		    		item.setGrayed(true);
		    		item.setExpanded(true);
		    	}
		    }
		    tree.setLinesVisible(true);
		    bOk = new Button(exampleShell, SWT.PUSH);
		    apply= false;
		    bOk.setText("Ok");
		    bOk.setEnabled(false);
		    bOk.addSelectionListener(new SelectionAdapter() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					exampleShell.close();
					apply = true;
				}
			});
		  
		    final TreeEditor editor = new TreeEditor(tree);
			//The editor must have the same size as the cell and must
			//not be any smaller than 50 pixels.
			editor.horizontalAlignment = SWT.RIGHT;
			editor.grabHorizontal = true;
			editor.minimumWidth = 50;
			
			tree.addListener(SWT.Selection,new Listener() {
				
				@Override
				public void handleEvent(Event e) {
					// Clean up any previous editor control
					Control oldEditor = editor.getEditor();
					if (oldEditor != null) oldEditor.dispose();
			
					// Identify the selected row
					if (e.item == null) return;
					TreeItem item = (TreeItem) e.item;
					if(item.getParentItem() == null){
						boolean check = item.getChecked();
						item.setGrayed(check);
						for(TreeItem child:item.getItems()){
							child.setChecked(check);
						}
						return;
					}

						item.getParentItem().setChecked(false);
						item.getParentItem().setGrayed(true);
						for(TreeItem child:item.getParentItem().getItems()){
							if(!child.getChecked()){
								item.getParentItem().setGrayed(false);
							}else{
								item.getParentItem().setChecked(true);
							}
						}
						if(!item.getChecked()){
							item.getParentItem().setGrayed(false);
							return;
						}
					
					// The control that will be the editor must be a child of the Tree
					Text newEditor = new Text(tree, SWT.RIGHT);
					newEditor.setText(item.getText(1));
					newEditor.addVerifyListener(new VerifyListener() {
						
						@Override
						public void verifyText(VerifyEvent e) {
							if(!e.text.equals("")){
								try{
									Integer.parseInt(e.text);
								}catch(NumberFormatException err){
									e.doit = false;
								}
							}
						}
					});
					newEditor.addModifyListener(new ModifyListener() {
						@Override
						public void modifyText(ModifyEvent e) {
							editor.getItem().setText(1, ((Text)e.getSource()).getText());
							resultMap = validate();
							bOk.setEnabled(resultMap != null);
						}
					});
					newEditor.addKeyListener(new KeyAdapter() {
						@Override
						public void keyReleased(KeyEvent e) {
							if(e.keyCode == SWT.CR){
								Control oldEditor = editor.getEditor();
								oldEditor.dispose();
							}
						}
					});
					newEditor.selectAll();
					newEditor.setFocus();
					editor.setEditor(newEditor, item,1);
				}
			});
			tree.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseUp(MouseEvent e) {
					resultMap = validate();
				}
			});
			resultMap = validate();
			exampleShell.pack();
			Rectangle dispBounds =parent.getBounds();
			int width = dispBounds.width/2;
			int height = dispBounds.height/3;
			exampleShell.setSize(width, height);
			int x = (dispBounds.x + (dispBounds.width/2)) - width/2;
			int y = (dispBounds.y + (dispBounds.height/2)) - height/2;
			exampleShell.setLocation(new Point(x,y));
			exampleShell.open();
			while (!exampleShell.isDisposed()) {
			    if (!Display.getDefault().readAndDispatch()) {
			    	Display.getDefault().sleep();
			    }
			}
			ArrayList<String> definitions = new ArrayList<>();
			if(apply && resultMap != null && !resultMap.isEmpty()){
				for (Entry<String, Integer> define : resultMap.entrySet()) {
					definitions.add("#define " +define.getKey() + " ("+define.getValue()+")");
				}
			}
			return definitions;
		}
		return new ArrayList<>();
	}

	/**
	 * validates the content of the tree returns a map which is null or contains 
	 * a list of integers for mapped to a variable name for each value
	 * if the tree is valid the ok Button is enabled
	 * 
	 * @return a map with lists of integers mapped to variable names, or null if any <code>#define</code>
	 * 			value cannot parsed as integer but should be used in the model
	 */
	private Map<String,Integer> validate(){
		List<Integer> defines = new ArrayList<>();
		Map<String,Integer> result = new HashMap<>();
		int value;
		
		for(TreeItem item: tree.getItems()){
			for(TreeItem subItem : item.getItems()){
				if(subItem.getChecked()){
					try{
						value = Integer.parseInt(subItem.getText(1));
						if(!defines.contains(value)){
							defines.add(value);
							result.put(subItem.getText(0),value);
						}else{
							bOk.setEnabled(false);
							return null;
						}
					}catch(NumberFormatException e){
						bOk.setEnabled(false);
						return null;
					}
				}
			}
		}
		bOk.setEnabled(true);
		return result;
	}
	
	
	private boolean checkValue(String value){
		for(int i=0;i <value.length();i++){
			if(i==0){
				if(!Character.isJavaIdentifierStart(value.charAt(i))){
					return false;
				}
			}else if(!Character.isJavaIdentifierPart(value.charAt(i))){
				return false;
			}
		}
		return true;
	}
}
