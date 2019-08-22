package com.puzzlegame.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;

public class EditorUI extends Group implements Disposable {

    private final EditorScreen editorScreen;

    private final Table root = new Table();
    private MenuBar menuBar;

    private LevelPropertiesWindow levelPropertiesWindow;
    private ObjectsWindow objectsWindow;
    private PropertiesWindow propertiesWindow;

    public EditorUI(EditorScreen editorScreen) {
        this.editorScreen = editorScreen;
        VisUI.load();

        root.align(Align.topLeft);
        this.addActor(root);

        menuBar = new MenuBar();
        root.add(menuBar.getTable()).expandX().fillX().row();
        root.add().expand().fill();
        createMenu();

        levelPropertiesWindow = new LevelPropertiesWindow(this);
        this.addActor(levelPropertiesWindow);

        objectsWindow = new ObjectsWindow();
        this.addActor(objectsWindow);

        propertiesWindow = new PropertiesWindow();
        this.addActor(propertiesWindow);
    }

    private void createMenu() {
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu runMenu = new Menu("Run");

        fileMenu.addItem(new MenuItem("New", new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                editorScreen.clearLevel();
            }
        }));
        fileMenu.addItem(new MenuItem("Open..."));
        fileMenu.addItem(new MenuItem("Save"));
        fileMenu.addItem(new MenuItem("Save As..."));
        fileMenu.addSeparator();
        fileMenu.addItem(new MenuItem("Exit", new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                Gdx.app.exit();
            }
        }));

        editMenu.addItem(new MenuItem("Turn right"));
        editMenu.addItem(new MenuItem("Turn left"));

        runMenu.addItem(new MenuItem("Run"));
        runMenu.addItem(new MenuItem("Stop"));

        menuBar.addMenu(fileMenu);
        menuBar.addMenu(editMenu);
        menuBar.addMenu(runMenu);
    }

    /**
     * @return the editor screen
     */
    public EditorScreen getEditorScreen(){
        return editorScreen;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        root.setWidth(Gdx.graphics.getWidth());
        root.setPosition(-(float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
        super.draw(batch, parentAlpha);
    }

    @Override
    public void dispose() {
        VisUI.dispose();
    }
}
