package net.clansau.clans.fishing.enums;

public enum TopRating {

    TOP_10(0L),
    TOP_TODAY(86400000L),
    TOP_WEEK(604800000L);

    private final long duration;

    TopRating(final long duration) {
        this.duration = duration;
    }

    public final long getDuration() {
        return this.duration;
    }
}