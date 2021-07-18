package com.majakkagames.gravitymaze.editorcore.utils;

import java.io.Serializable;
import java.util.function.Consumer;

public class TitleUpdater {

    private static final String DEFAULT_TITLE = "unnamed";

    private final TitleSetter titleSetter;

    private String path = DEFAULT_TITLE;
    private boolean changeIndicator;

    public TitleUpdater(TitleSetter titleSetter) {
        this.titleSetter = titleSetter;
        update(null);
    }

    public void update(String path) {
        if (path == null || path.isEmpty()) {
            this.path = DEFAULT_TITLE;
        } else {
            this.path = path;
        }
        this.titleSetter.accept(processTitle(this.path));
    }

    private String processTitle(String path) {
        return changeIndicator ? "*" + path : path;
    }

    public void showChangeIndicator(boolean show) {
        this.changeIndicator = show;
        update(path);
    }

    public interface TitleSetter extends Consumer<String>, Serializable {
    }
}
