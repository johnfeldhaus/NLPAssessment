package com.dr.feldhaus.nlp.parse;

import com.dr.feldhaus.nlp.model.Sentence;
import com.dr.feldhaus.nlp.model.Token;
import com.dr.feldhaus.nlp.model.TokenizedOutput;

public class Parser {

	public TokenizedOutput parse(String input) {
		if(input == null) return new TokenizedOutput();
		
		TokenizedOutput output = new TokenizedOutput();
		
		Sentence currentSentence = new Sentence();
		StringBuilder currentTokenText = new StringBuilder();
		for(int i = 0; i < input.length(); i++) {
			if(Character.isWhitespace(input.charAt(i)) || isSpecialCharacter(i, input)) {
				// Finish previous token
				addTokenIfNonEmpty(currentSentence, currentTokenText);
				// Add whitespace or special character token
				currentSentence.addToken(new Token("" + input.charAt(i)));
				currentTokenText = new StringBuilder();
				if(isSentenceEnd(i, input)) {
					output.addSentence(currentSentence);
					currentSentence = new Sentence();
				}
			}
			else {
				currentTokenText.append(input.charAt(i));
			}
		}
		
		// Add leftover sentence and token in case the input ended without proper punctuation
		addTokenIfNonEmpty(currentSentence, currentTokenText);
		if(!currentSentence.isEmpty()) {
			output.addSentence(currentSentence);
		}
		
		return output;
	}

	private boolean isSentenceEnd(int i, String input) {
		return isEndMark(i, input) && !isSpecialCharacter(i+1, input) && !isEllipsisEnd(i, input);
	}

	private boolean isEndMark(int i, String input) {
		return input.charAt(i) == '.' || input.charAt(i) == '!' || input.charAt(i) == '?';
	}

	private boolean isEllipsisEnd(int index, String input) {
		if (index < 2) return false;
		if (index == input.length() - 1) return false;

		return input.charAt(index) == '.' && input.charAt(index - 1) == '.' && input.charAt(index - 2) == '.'
				&& Character.isWhitespace(input.charAt(index + 1));
	}

	private boolean isSpecialCharacter(int index, String input) {
		if (index >= input.length())
			return false;

		return !Character.isWhitespace(input.charAt(index)) && !Character.isAlphabetic(input.charAt(index))
				&& !Character.isDigit(input.charAt(index)) && !isPartOfNumber(index, input);
	}

	private boolean isPartOfNumber(int index, String input) {
		if(input.charAt(index) == '.'){
			// If character to left is not a digit or whitespace
			if(index > 0 && !Character.isDigit(input.charAt(index-1)) && !Character.isWhitespace(input.charAt(index-1)))
				return false;
			// If character to right is not a digit
			if(index == input.length()-1 || !Character.isDigit(input.charAt(index+1)))
				return false;
			
			return true;
		}
		else if(input.charAt(index) == ',') {
			// If character to left is not a digit
			if(index == 0 || !Character.isDigit(input.charAt(index-1)))
			   return false;
			// If character to right is not a digit
			if(index == input.length()-1 || !Character.isDigit(input.charAt(index+1)))
				return false;
			
			return true;
		}
		
		return false;
	}

	private void addTokenIfNonEmpty(Sentence sentence, StringBuilder tokenText) {
		if(tokenText.length() > 0) {
			sentence.addToken(new Token(tokenText.toString()));
		}
	}
}