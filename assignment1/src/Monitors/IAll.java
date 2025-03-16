package Monitors;

public interface IAll {
    // Generic getInstance that can return any implementation
    static <T extends IAll> T getInstance() {
        return null; // This will be overridden by implementing classes
    }
}
