package domainV2.util.validation.text;

import java.util.regex.Pattern;

public class GenericTextValidatorNumericSequence implements IGenericTextValidator {

	@Override
	public boolean validate(String input) {
		Pattern p = Pattern.compile("((\\d\\,(?=\\d))|(\\d\\-(?=\\d))|\\d)+");
		return p.matcher(input).matches();
	}

}
