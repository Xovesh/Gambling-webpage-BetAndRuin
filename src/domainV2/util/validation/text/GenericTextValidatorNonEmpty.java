package domainV2.util.validation.text;

public class GenericTextValidatorNonEmpty implements IGenericTextValidator {

	@Override
	public boolean validate(String input) {
		return !input.equals("");
	}

}
