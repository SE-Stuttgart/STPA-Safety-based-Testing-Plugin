package stpaverifier.util.jobs;

import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Position;

import stpaverifier.controller.model.STPAVerifierController;
import stpaverifier.controller.preferences.SpinArgumentHandler;

/**
 * this job object runs a check on a promela model by trying to generate a verifier
 * with spin and creating annotations depending on the output
 * 
 * @author Lukas Balzer
 * @since 1.0.0
 *
 */
public class CheckPROMELAJob extends AbstractModelCheckJob {

	static final String ERROR = "error:";
	private List<PositionAttribute> positions;
	
	public CheckPROMELAJob(String name,STPAVerifierController controller) {
		super(name,controller);
	}
	
	@Override
	public int scanLine(String line, int current) {
		if(line.toLowerCase().contains(ERROR)){
			int lineNr = -1;

			if (line.contains(SPIN_VERSION_STRING)) {
				int pos = line.indexOf(SPIN_VERSION_STRING);
				getModel().setModelChecker(line.substring(pos,pos + SPIN_VERSION_STRING.length() + 6));
			}
			if(line.contains(getFilelName()+":")){
				lineNr = getDigit(line.substring(line.lastIndexOf(getFilelName())))[0].intValue() -1;
				
				try {
					Position pos = new Position(getDocument().getLineOffset(lineNr),getDocument().getLineLength(lineNr));
					int msgBeginn= line.indexOf(ERROR) + ERROR.length();
					String message = line.substring(msgBeginn);
					positions.add(new PositionAttribute(pos,message));
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return 0;
	}

	@Override
	protected void createRuntime() {
		inheritIO(getFile().getParent(), SpinArgumentHandler.getSpinArguments(getFile().getLocation().toOSString(), null), getConsole(), 0);

		inheritIO(getFile().getParent(), SpinArgumentHandler.getCompilerArguments(), getConsole(), 0);
	}
}
