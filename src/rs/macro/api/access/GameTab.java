package rs.macro.api.access;

import rs.macro.api.util.fx.Colors;

import java.awt.*;

public enum GameTab {
    COMBAT_OPTIONS(new Rectangle(529, 169, 26, 32)),
    STATS(new Rectangle(563, 169, 26, 32)),
    QUEST_LIST(new Rectangle(596, 169, 26, 32)),
    INVENTORY(new Rectangle(629, 169, 26, 32)),
    WORN_EQUIPMENT(new Rectangle(662, 169, 26, 32)),
    PRAYER(new Rectangle(695, 169, 26, 32)),
    MAGIC(new Rectangle(729, 169, 26, 32)),
    CLAN_CHAT(new Rectangle(529, 467, 26, 32)),
    FRIENDS_LIST(new Rectangle(563, 467, 26, 32)),
    IGNORE_LIST(new Rectangle(596, 467, 26, 32)),
    LOGOUT(new Rectangle(629, 467, 26, 32)),
    OPTIONS(new Rectangle(662, 467, 26, 32)),
    EMOTES(new Rectangle(695, 467, 26, 32)),
    MUSIC_PLAYER(new Rectangle(729, 467, 26, 32));

    /**
     * The bounds of the tab.
     */
    public final Rectangle bounds;

    /**
     * The RGB of an opened tab.
     */
    private static final int OPENED_RGB = Colors.hexToRGB("#71261D");

    GameTab(Rectangle bounds) {
        this.bounds = bounds;
    }

    /**
     * Checks whether the tab is opened or not.
     *
     * @return <t>true</t> if the tab is opened, otherwise <t>false</t>.
     */
    public boolean viewing() {
        return RuneScape.pixels().operator().builder()
                .bounds(bounds)
                .tolFilter(OPENED_RGB, 3)
                .query().count() > 0;
    }
}