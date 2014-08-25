package org.yellowbinary.server.core.annotation;

public class Dependency {

    private final String name;
    private final boolean minimum;
    private final int major;
    private final int minor;
    private final int patch;

    public Dependency(String name, int major, int minor) {
        this(name, true, major, minor);
    }

    public Dependency(String name, boolean minimum, int major, int minor) {
        this(name, minimum, major, minor, 0);
    }

    public Dependency(String name, boolean minimum, int major, int minor, int patch) {
        this.name = name;
        this.minimum = minimum;
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public String getName() {
        return name;
    }

    public boolean isMinimum() {
        return minimum;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    public String toString() {
        return "Module " + name + " (" + version() + ")";
    }

    public String version() {
        return (minimum ? ">=" : "<") + major + (minor != -1 ? "." + minor : "") + (patch != -1 ? "." + patch : "");
    }
}
