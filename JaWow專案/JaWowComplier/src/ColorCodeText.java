import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class ColorCodeText {
	final StyleContext cont = StyleContext.getDefaultStyleContext();
	final AttributeSet attrReserved = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
			new Color(255, 2, 120));
	final AttributeSet attrType = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
			new Color(2, 255, 246));
	final AttributeSet attrOther = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
			new Color(234, 191, 112));
	final AttributeSet attrToken = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.WHITE);
	final AttributeSet attrEqu = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.green);

	public ColorCodeText() {

	}

	public DefaultStyledDocument getModifiedDocment() {
		DefaultStyledDocument doc = new DefaultStyledDocument() {
			public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
				super.insertString(offset, str, a);

				String text = getText(0, getLength());
				int before = findLastNonWordChar(text, offset);
				if (before < 0)
					before = 0;
				int after = findFirstNonWordChar(text, offset + str.length());
				int wordL = before;
				int wordR = before;

				while (wordR <= after) {
					if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
						if (text.substring(wordL, wordR)
								.matches("(\\W)*(begin|end|for|while|if|else if|else|and|or|<|>)"))
							setCharacterAttributes(wordL, wordR - wordL, attrReserved, false);
						else if (text.substring(wordL, wordR).matches("(\\W)*(int|float|double)"))
							setCharacterAttributes(wordL, wordR - wordL, attrType, false);
						else if (text.substring(wordL, wordR).matches("(\\W)*((|))"))
							setCharacterAttributes(wordL, wordR - wordL, attrToken, false);
						else if (text.substring(wordL, wordR).matches("(\\W)*(#)"))
							setCharacterAttributes(wordL, wordR - wordL, attrEqu, false);
						else
							setCharacterAttributes(wordL, wordR - wordL, attrOther, false);
						wordL = wordR;
					}
					wordR++;
				}
			}

			public void remove(int offs, int len) throws BadLocationException {
				super.remove(offs, len);

				String text = getText(0, getLength());
				int before = findLastNonWordChar(text, offs);
				if (before < 0)
					before = 0;
				int after = findFirstNonWordChar(text, offs);

				if (text.substring(before, after).matches("(\\W)*(begin|end|for|while|if|else if|else|and|or|<|>)")) {
					setCharacterAttributes(before, after - before, attrReserved, false);
				} else if (text.substring(before, after).matches("(\\W)*(int|float|double)")) {
					setCharacterAttributes(before, after - before, attrType, false);
				} else if (text.substring(before, after).matches("(\\W)*((|))")) {
					setCharacterAttributes(before, after - before, attrToken, false);
				} else if (text.substring(before, after).matches("(\\W)*(#)")) {
					setCharacterAttributes(before, after - before, attrEqu, false);
				} else {
					setCharacterAttributes(before, after - before, attrOther, false);
				}
			}
		};
		return doc;
	}

	private int findLastNonWordChar(String text, int index) {
		while (--index >= 0) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
		}
		return index;
	}

	private int findFirstNonWordChar(String text, int index) {
		while (index < text.length()) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
			index++;
		}
		return index;
	}
}
