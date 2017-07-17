package stpaverifier.util.jobs;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Position;

import stpaverifier.controller.model.STPAVerifierController;
import stpaverifier.controller.preferences.NuSMVArgumentHandler;

/**
 * this job object runs a check on a promela model by trying to generate a verifier
 * with spin and creating annotations depending on the output
 * 
 * @author Lukas Balzer
 * @since 1.0.0
 *
 */
public class CheckSMVJob extends AbstractModelCheckJob {

	static final String ERROR = "error:";
	
	public CheckSMVJob(String name,STPAVerifierController controller) {
		super(name,controller);
	}
	
	@Override
	public int scanLine(String line, int current) {
		int lineNr = -1;

		if (line.contains(NuSMV_VERSION_STRING)) {
			int pos = line.indexOf(NuSMV_VERSION_STRING);
			getModel().setModelChecker(line.substring(pos,pos + NuSMV_VERSION_STRING.length() + 6));
		}
		if(line.contains(getFilelName())){
			int indexOfName = line.indexOf(getFilelName());
			lineNr = getDigit(line.substring(line.indexOf(':',indexOfName)))[0].intValue() -1;
			
			try {
				Position pos = new Position(getDocument().getLineOffset(lineNr),getDocument().getLineLength(lineNr));
				String message = line.substring(line.indexOf(':',indexOfName));
				positions.add(new PositionAttribute(pos,message));
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		
		return 0;
	}

	@Override
	protected void createRuntime() {
		inheritIO(getFile().getParent(),
				  NuSMVArgumentHandler.getNuSMV_MODEL_Args(getFile().getLocation().toOSString()),
				  null, 0);

	}
}
