package dreamlink.graphics.text;

import java.util.HashMap;
import java.util.Map;

import dreamlink.graphics.texture.sample.OverlayTextureSample;
import dreamlink.graphics.texture.sample.TextureSample;

public class CharacterTextureSampleLookup {

    private record CharacterTextureConfig(
        TextureSample normalTextureSample,
        TextureSample underlineTextureSample
    ) { }

    public static CharacterTextureSampleLookup instance = new CharacterTextureSampleLookup();

    private Map<Character, CharacterTextureConfig> glyphLookup;

    public CharacterTextureSampleLookup() {
        this.glyphLookup = new HashMap<>();
        this.addCharacterEntry(' ', OverlayTextureSample.symSpace, OverlayTextureSample.symSpaceUnderline);
        this.addCharacterEntry('!', OverlayTextureSample.symExclamation, OverlayTextureSample.symExclamationUnderline);
        this.addCharacterEntry('"', OverlayTextureSample.symDoubleQuotes, OverlayTextureSample.symDoubleQuotesUnderline);
        this.addCharacterEntry('#', OverlayTextureSample.symHash, OverlayTextureSample.symHashUnderline);
        this.addCharacterEntry('$', OverlayTextureSample.symDollar, OverlayTextureSample.symDollarUnderline);
        this.addCharacterEntry('%', OverlayTextureSample.symPercent, OverlayTextureSample.symPercentUnderline);
        this.addCharacterEntry('&', OverlayTextureSample.symAmpersand, OverlayTextureSample.symAmpersandUnderline);
        this.addCharacterEntry('\'', OverlayTextureSample.symSingleQuote, OverlayTextureSample.symSingleQuoteUnderline);
        this.addCharacterEntry('(', OverlayTextureSample.symOpenParenthesis, OverlayTextureSample.symOpenParenthesisUnderline);
        this.addCharacterEntry(')', OverlayTextureSample.symCloseParenthesis, OverlayTextureSample.symCloseParenthesisUnderline);
        this.addCharacterEntry('*', OverlayTextureSample.symAsterisk, OverlayTextureSample.symAsteriskUnderline);
        this.addCharacterEntry('+', OverlayTextureSample.symPlus, OverlayTextureSample.symPlusUnderline);
        this.addCharacterEntry(',', OverlayTextureSample.symComma, OverlayTextureSample.symCommaUnderline);
        this.addCharacterEntry('-', OverlayTextureSample.symMinus, OverlayTextureSample.symMinusUnderline);
        this.addCharacterEntry('.', OverlayTextureSample.symPeriod, OverlayTextureSample.symPeriodUnderline);
        this.addCharacterEntry('/', OverlayTextureSample.symSlash, OverlayTextureSample.symSlashUnderline);

        this.addCharacterEntry('0', OverlayTextureSample.digit0, OverlayTextureSample.digit0Underline);
        this.addCharacterEntry('1', OverlayTextureSample.digit1, OverlayTextureSample.digit1Underline);
        this.addCharacterEntry('2', OverlayTextureSample.digit2, OverlayTextureSample.digit2Underline);
        this.addCharacterEntry('3', OverlayTextureSample.digit3, OverlayTextureSample.digit3Underline);
        this.addCharacterEntry('4', OverlayTextureSample.digit4, OverlayTextureSample.digit4Underline);
        this.addCharacterEntry('5', OverlayTextureSample.digit5, OverlayTextureSample.digit5Underline);
        this.addCharacterEntry('6', OverlayTextureSample.digit6, OverlayTextureSample.digit6Underline);
        this.addCharacterEntry('7', OverlayTextureSample.digit7, OverlayTextureSample.digit7Underline);
        this.addCharacterEntry('8', OverlayTextureSample.digit8, OverlayTextureSample.digit8Underline);
        this.addCharacterEntry('9', OverlayTextureSample.digit9, OverlayTextureSample.digit9Underline);
        this.addCharacterEntry(':', OverlayTextureSample.symColon, OverlayTextureSample.symColonUnderline);
        this.addCharacterEntry(';', OverlayTextureSample.symSemicolon, OverlayTextureSample.symSemicolonUnderline);
        this.addCharacterEntry('<', OverlayTextureSample.symLessThan, OverlayTextureSample.symLessThanUnderline);
        this.addCharacterEntry('=', OverlayTextureSample.symEquals, OverlayTextureSample.symEqualsUnderline);
        this.addCharacterEntry('>', OverlayTextureSample.symGreaterThan, OverlayTextureSample.symGreaterThanUnderline);
        this.addCharacterEntry('?', OverlayTextureSample.symQuestion, OverlayTextureSample.symQuestionUnderline);

        this.addCharacterEntry('@', OverlayTextureSample.symAt, OverlayTextureSample.symAtUnderline);
        this.addCharacterEntry('A', OverlayTextureSample.letterUpperCaseA, OverlayTextureSample.letterUpperCaseAUnderline);
        this.addCharacterEntry('B', OverlayTextureSample.letterUpperCaseB, OverlayTextureSample.letterUpperCaseBUnderline);
        this.addCharacterEntry('C', OverlayTextureSample.letterUpperCaseC, OverlayTextureSample.letterUpperCaseCUnderline);
        this.addCharacterEntry('D', OverlayTextureSample.letterUpperCaseD, OverlayTextureSample.letterUpperCaseDUnderline);
        this.addCharacterEntry('E', OverlayTextureSample.letterUpperCaseE, OverlayTextureSample.letterUpperCaseEUnderline);
        this.addCharacterEntry('F', OverlayTextureSample.letterUpperCaseF, OverlayTextureSample.letterUpperCaseFUnderline);
        this.addCharacterEntry('G', OverlayTextureSample.letterUpperCaseG, OverlayTextureSample.letterUpperCaseGUnderline);
        this.addCharacterEntry('H', OverlayTextureSample.letterUpperCaseH, OverlayTextureSample.letterUpperCaseHUnderline);
        this.addCharacterEntry('I', OverlayTextureSample.letterUpperCaseI, OverlayTextureSample.letterUpperCaseIUnderline);
        this.addCharacterEntry('J', OverlayTextureSample.letterUpperCaseJ, OverlayTextureSample.letterUpperCaseJUnderline);
        this.addCharacterEntry('K', OverlayTextureSample.letterUpperCaseK, OverlayTextureSample.letterUpperCaseKUnderline);
        this.addCharacterEntry('L', OverlayTextureSample.letterUpperCaseL, OverlayTextureSample.letterUpperCaseLUnderline);
        this.addCharacterEntry('M', OverlayTextureSample.letterUpperCaseM, OverlayTextureSample.letterUpperCaseMUnderline);
        this.addCharacterEntry('N', OverlayTextureSample.letterUpperCaseN, OverlayTextureSample.letterUpperCaseNUnderline);
        this.addCharacterEntry('O', OverlayTextureSample.letterUpperCaseO, OverlayTextureSample.letterUpperCaseOUnderline);

        this.addCharacterEntry('P', OverlayTextureSample.letterUpperCaseP, OverlayTextureSample.letterUpperCasePUnderline);
        this.addCharacterEntry('Q', OverlayTextureSample.letterUpperCaseQ, OverlayTextureSample.letterUpperCaseQUnderline);
        this.addCharacterEntry('R', OverlayTextureSample.letterUpperCaseR, OverlayTextureSample.letterUpperCaseRUnderline);
        this.addCharacterEntry('S', OverlayTextureSample.letterUpperCaseS, OverlayTextureSample.letterUpperCaseSUnderline);
        this.addCharacterEntry('T', OverlayTextureSample.letterUpperCaseT, OverlayTextureSample.letterUpperCaseTUnderline);
        this.addCharacterEntry('U', OverlayTextureSample.letterUpperCaseU, OverlayTextureSample.letterUpperCaseUUnderline);
        this.addCharacterEntry('V', OverlayTextureSample.letterUpperCaseV, OverlayTextureSample.letterUpperCaseVUnderline);
        this.addCharacterEntry('W', OverlayTextureSample.letterUpperCaseW, OverlayTextureSample.letterUpperCaseWUnderline);
        this.addCharacterEntry('X', OverlayTextureSample.letterUpperCaseX, OverlayTextureSample.letterUpperCaseXUnderline);
        this.addCharacterEntry('Y', OverlayTextureSample.letterUpperCaseY, OverlayTextureSample.letterUpperCaseYUnderline);
        this.addCharacterEntry('Z', OverlayTextureSample.letterUpperCaseZ, OverlayTextureSample.letterUpperCaseZUnderline); 
        this.addCharacterEntry('[', OverlayTextureSample.symOpenBracket, OverlayTextureSample.symOpenBracketUnderline);
        this.addCharacterEntry('\\', OverlayTextureSample.symBackslash, OverlayTextureSample.symBackslashUnderline);
        this.addCharacterEntry(']', OverlayTextureSample.symCloseBracket, OverlayTextureSample.symCloseBracketUnderline);
        this.addCharacterEntry('^', OverlayTextureSample.symCaret, OverlayTextureSample.symCaretUnderline);
        this.addCharacterEntry('_', OverlayTextureSample.symUnderscore, OverlayTextureSample.symUnderscoreUnderline);

        this.addCharacterEntry('`', OverlayTextureSample.symBacktick, OverlayTextureSample.symBacktickUnderline);
        this.addCharacterEntry('a', OverlayTextureSample.letterLowerCaseA, OverlayTextureSample.letterLowerCaseAUnderline);
        this.addCharacterEntry('b', OverlayTextureSample.letterLowerCaseB, OverlayTextureSample.letterLowerCaseBUnderline);
        this.addCharacterEntry('c', OverlayTextureSample.letterLowerCaseC, OverlayTextureSample.letterLowerCaseCUnderline);
        this.addCharacterEntry('d', OverlayTextureSample.letterLowerCaseD, OverlayTextureSample.letterLowerCaseDUnderline);
        this.addCharacterEntry('e', OverlayTextureSample.letterLowerCaseE, OverlayTextureSample.letterLowerCaseEUnderline);
        this.addCharacterEntry('f', OverlayTextureSample.letterLowerCaseF, OverlayTextureSample.letterLowerCaseFUnderline);
        this.addCharacterEntry('g', OverlayTextureSample.letterLowerCaseG, OverlayTextureSample.letterLowerCaseGUnderline);
        this.addCharacterEntry('h', OverlayTextureSample.letterLowerCaseH, OverlayTextureSample.letterLowerCaseHUnderline);
        this.addCharacterEntry('i', OverlayTextureSample.letterLowerCaseI, OverlayTextureSample.letterLowerCaseIUnderline);
        this.addCharacterEntry('j', OverlayTextureSample.letterLowerCaseJ, OverlayTextureSample.letterLowerCaseJUnderline);
        this.addCharacterEntry('k', OverlayTextureSample.letterLowerCaseK, OverlayTextureSample.letterLowerCaseKUnderline);
        this.addCharacterEntry('l', OverlayTextureSample.letterLowerCaseL, OverlayTextureSample.letterLowerCaseLUnderline);
        this.addCharacterEntry('m', OverlayTextureSample.letterLowerCaseM, OverlayTextureSample.letterLowerCaseMUnderline);
        this.addCharacterEntry('n', OverlayTextureSample.letterLowerCaseN, OverlayTextureSample.letterLowerCaseNUnderline);
        this.addCharacterEntry('o', OverlayTextureSample.letterLowerCaseO, OverlayTextureSample.letterLowerCaseOUnderline);

        this.addCharacterEntry('p', OverlayTextureSample.letterLowerCaseP, OverlayTextureSample.letterLowerCasePUnderline);
        this.addCharacterEntry('q', OverlayTextureSample.letterLowerCaseQ, OverlayTextureSample.letterLowerCaseQUnderline);
        this.addCharacterEntry('r', OverlayTextureSample.letterLowerCaseR, OverlayTextureSample.letterLowerCaseRUnderline);
        this.addCharacterEntry('s', OverlayTextureSample.letterLowerCaseS, OverlayTextureSample.letterLowerCaseSUnderline);
        this.addCharacterEntry('t', OverlayTextureSample.letterLowerCaseT, OverlayTextureSample.letterLowerCaseTUnderline);
        this.addCharacterEntry('u', OverlayTextureSample.letterLowerCaseU, OverlayTextureSample.letterLowerCaseUUnderline);
        this.addCharacterEntry('v', OverlayTextureSample.letterLowerCaseV, OverlayTextureSample.letterLowerCaseVUnderline);
        this.addCharacterEntry('w', OverlayTextureSample.letterLowerCaseW, OverlayTextureSample.letterLowerCaseWUnderline);
        this.addCharacterEntry('x', OverlayTextureSample.letterLowerCaseX, OverlayTextureSample.letterLowerCaseXUnderline);
        this.addCharacterEntry('y', OverlayTextureSample.letterLowerCaseY, OverlayTextureSample.letterLowerCaseYUnderline);
        this.addCharacterEntry('z', OverlayTextureSample.letterLowerCaseZ, OverlayTextureSample.letterLowerCaseZUnderline);
        this.addCharacterEntry('{', OverlayTextureSample.symOpenBrace, OverlayTextureSample.symCloseBrace);
        this.addCharacterEntry('|', OverlayTextureSample.symPipe, OverlayTextureSample.symPipeUnderline);
        this.addCharacterEntry('}', OverlayTextureSample.symCloseBrace, OverlayTextureSample.symCloseBraceUnderline);
        this.addCharacterEntry('~', OverlayTextureSample.symTilde, OverlayTextureSample.symTildeUnderline);
    }

    private void addCharacterEntry(
        char character, 
        TextureSample textureSample, 
        TextureSample underlineTextureSample
    ) {
        this.glyphLookup.put(character, new CharacterTextureConfig(textureSample, underlineTextureSample));
    }

    public TextureSample getTextureSample(char character, FontDecoration fontDecoration) {
        var config = this.glyphLookup.get(character);
        if(config == null) {
            return null;
        }

        if(fontDecoration == FontDecoration.underline) {
            return config.underlineTextureSample;
        }

        return config.normalTextureSample;
    }
    
}
