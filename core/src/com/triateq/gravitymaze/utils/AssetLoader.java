package com.triateq.gravitymaze.utils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AssetLoader {

    private static final Set<String> TEXTURE_EXTENSIONS = new HashSet<>(Arrays.asList("png", "jpg", "jpeg"));
    private static final Set<String> FONT_EXTENSIONS = new HashSet<>(Collections.singletonList("ttf"));

    private AssetLoader() {
        throw new IllegalStateException("Utility class");
    }

    public static void load(AssetManager manager) {
        setFontLoader(manager);

        List<FileHandle> files = new ArrayList<>();

        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            getFiles(Gdx.files.internal(""), files);
        } else {
            // Desktop
            getFiles(Gdx.files.internal("."), files);
        }

        for (FileHandle file : files) {
            String extension = file.extension().trim().toLowerCase();
            if (TEXTURE_EXTENSIONS.contains(extension)) {
                loadTexture(manager, file);
            } else if (FONT_EXTENSIONS.contains(extension)) {
                loadFont(manager, file);
            }
        }
        manager.finishLoading();
    }

    private static void setFontLoader(AssetManager manager) {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
    }

    private static void loadTexture(AssetManager manager, FileHandle file) {
        TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
        param.minFilter = Texture.TextureFilter.MipMapLinearLinear;
        param.magFilter = Texture.TextureFilter.Linear;
        param.genMipMaps = true;
        manager.load(file.path(), Texture.class, param);
    }

    private static void loadFont(AssetManager manager, FileHandle file) {
        FreetypeFontLoader.FreeTypeFontLoaderParameter param = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        param.fontFileName = file.path();
        param.fontParameters.size = Gdx.graphics.getWidth() / 12;
        manager.load(param.fontFileName, BitmapFont.class, param);
    }

    private static void getFiles(FileHandle fileHandle, List<FileHandle> files) {
        for (FileHandle file : fileHandle.list()) {

            // We need this weird thing for android devices support
            if (file.path().charAt(0) == '/') {
                file = Gdx.files.internal(file.path().substring(1));
            }
            // I think it's better to bring the path to the general form
            if (file.path().charAt(0) == '.') {
                file = Gdx.files.internal(file.path().substring(2));
            }

            if (file.isDirectory()) {
                getFiles(file, files);
            } else {
                files.add(file);
            }
        }
    }
}
