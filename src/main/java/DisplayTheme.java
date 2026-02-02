/**
 * Defines visual display themes used by the console UI.

 * Each theme provides different border characters to control
 * how UI panels, separators, and layouts are rendered.

 * Themes:
 * CLASSIC → ASCII style borders
 * MODERN → Unicode box drawing characters
 * MINIMAL → No borders (clean output)
 */
public enum DisplayTheme {
    CLASSIC("=", "-", "|"),
    MODERN("━", "─", "│"),
    MINIMAL(" ", " ", " ");
    private final String heavyBorder;
    private final String lightBorder;
    private final String verticalBorder;
    DisplayTheme(String heavy, String light, String vertical) {
        this.heavyBorder = heavy;
        this.lightBorder = light;
        this.verticalBorder = vertical;
    }
    public String getHeavy() {
        return heavyBorder;
    }
    public String getLight() {
        return lightBorder;
    }
    public String getVertical() {
        return verticalBorder;
    }
}
