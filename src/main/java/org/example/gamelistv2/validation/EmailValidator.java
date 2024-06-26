package org.example.gamelistv2.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<Email, String> {

    private String[] domains;

    private static final Pattern EMAIL_REGEX_PATTERN;

    private static final String EMAIL_REGEX = ""
            + "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`"
            + "{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23"
            + "-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])"
            + "*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:"
            + "[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-"
            + "9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a"
            + "-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21"
            + "-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])"
            + "+)\\])";

    static {
        EMAIL_REGEX_PATTERN = Pattern.compile(EMAIL_REGEX);
    }

    @Override
    public void initialize(Email constraintAnnotation) {
        this.domains = constraintAnnotation.domains();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        boolean isValid = false;

        if (value != null) {
            for (String domain : domains) {
                isValid = EMAIL_REGEX_PATTERN.matcher(value).matches() && value.endsWith(domain);

                if (isValid) {
                    break;
                }
            }
        } else {
            isValid = true;
        }

        return isValid;
    }
}
