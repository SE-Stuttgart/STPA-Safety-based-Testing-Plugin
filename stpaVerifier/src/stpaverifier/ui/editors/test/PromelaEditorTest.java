package stpaverifier.ui.editors.test;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.junit.Before;
import org.junit.Test;

import stpaverifier.ui.editors.ModelScanner;
import stpaverifier.ui.editors.promela.PromelaScanner;

public class PromelaEditorTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		ModelScanner scanner = new PromelaScanner(null);
		Document doc = new Document("int _a;");
		scanner.setRange(doc, 0, doc.getLength());
		IToken token = scanner.nextToken();
		Assert.isTrue(ModelScanner.TYPE.equals(token));
		token = scanner.nextToken();
		Assert.isTrue(Token.WHITESPACE.equals(token));
		token = scanner.nextToken();
		Assert.isTrue(ModelScanner.IDENTIFIER.equals(token) ,"expected token IDENTIFIER was " + token.toString());
		
		doc = new Document("_a = 1");
		scanner.setRange(doc, 0, doc.getLength());
		token = scanner.nextToken();
		Assert.isTrue(ModelScanner.IDENTIFIER.equals(token));
		
		doc = new Document("! _a_big ->b = b+1");
		scanner.setRange(doc, 0, doc.getLength());
		
		token = scanner.nextToken();
		while(token != Token.EOF){
			Assert.isTrue(!ModelScanner.IDENTIFIER.equals(token));
			token = scanner.nextToken();
		}
		
		
		
	}

}
