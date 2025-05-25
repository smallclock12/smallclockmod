package smallclockmod;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Filter(int score, Pattern pattern) {
    public Matcher getMatcher(String value) {
        return pattern.matcher(value);
    }
}
