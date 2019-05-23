package domainV2.util.validation;

public interface IValidator<T> {
	public boolean validate(T input);
}
