import java.util.ArrayList;
import java.util.List;

/**
 * Represents result of validation operation.
 * Stores errors, warnings and validation status.
 */
public class ValidationResult {
    private boolean valid;
    private List<String> errors;
    private List<String> warnings;

    public ValidationResult() {
        this.valid = true;
        this.errors = new ArrayList<>();
        this.warnings = new ArrayList<>();
    }

    public void addError(String error) {
        this.valid = false;
        this.errors.add(error);
    }

    public void addWarning(String warning) {
        this.warnings.add(warning);
    }

    public boolean isValid() { return valid; }
    public List<String> getErrors() { return new ArrayList<>(errors); }
    public List<String> getWarnings() { return new ArrayList<>(warnings); }
    public boolean hasWarnings() { return !warnings.isEmpty(); }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(valid ? "VALID" : "INVALID");
        if (!errors.isEmpty()) {
            sb.append("\nErrors:");
            errors.forEach(e -> sb.append("\n  - ").append(e));
        }
        if (!warnings.isEmpty()) {
            sb.append("\nWarnings:");
            warnings.forEach(w -> sb.append("\n  - ").append(w));
        }
        return sb.toString();
    }
}
