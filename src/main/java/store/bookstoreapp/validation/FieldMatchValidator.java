package store.bookstoreapp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.beanutils.BeanUtils;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(final FieldMatch constraintAnnotation) {
        this.firstFieldName = constraintAnnotation.firstField();
        this.secondFieldName = constraintAnnotation.secondField();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        try {
            final Object firstValue = BeanUtils.getProperty(value, firstFieldName);
            final Object secondValue = BeanUtils.getProperty(value, secondFieldName);
            return firstValue != null && firstValue.equals(secondValue);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
