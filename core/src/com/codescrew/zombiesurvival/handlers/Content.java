package com.codescrew.zombiesurvival.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

/**
 * Created by raimat on 2014-09-09.
 */
public class Content {

    public static final float SFX_VOL = 0.05f;
    private HashMap<String, Texture> textures;
    private HashMap<String, Music> music;
    private HashMap<String, Sound> sounds;

    public Content() {
        textures = new HashMap<String, Texture>();
        music = new HashMap<String, Music>();
        sounds = new HashMap<String, Sound>();
    }

    /***********/
	/* Texture */
    /***********/

    public void loadTexture(String path) {
        int slashIndex = path.lastIndexOf('/');
        String key;
        if(slashIndex == -1) {
            key = path.substring(0, path.lastIndexOf('.'));
        }
        else {
            key = path.substring(slashIndex + 1, path.lastIndexOf('.'));
        }
        loadTexture(path, key);
    }
    public void loadTexture(String path, String key) {
        Texture tex = new Texture(Gdx.files.internal(path));
        textures.put(key, tex);
    }
    public Texture getTexture(String key) {
        return textures.get(key);
    }
    public void removeTexture(String key) {
        Texture tex = textures.get(key);
        if(tex != null) {
            textures.remove(key);
            tex.dispose();
        }
    }

    /*********/
	/* Music */
    /*********/

    public void loadMusic(String path) {
        int slashIndex = path.lastIndexOf('/');
        String key;
        if(slashIndex == -1) {
            key = path.substring(0, path.lastIndexOf('.'));
        }
        else {
            key = path.substring(slashIndex + 1, path.lastIndexOf('.'));
        }
        loadMusic(path, key);
    }
    public void loadMusic(String path, String key) {
        Music m = Gdx.audio.newMusic(Gdx.files.internal(path));
        music.put(key, m);
    }
    public Music getMusic(String key) {
        return music.get(key);
    }


    /*******/
	/* SFX */
    /*******/

    public void loadSound(String path) {
        int slashIndex = path.lastIndexOf('/');
        String key;
        if(slashIndex == -1) {
            key = path.substring(0, path.lastIndexOf('.'));
        }
        else {
            key = path.substring(slashIndex + 1, path.lastIndexOf('.'));
        }
        loadSound(path, key);
    }
    public void loadSound(String path, String key) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
        sounds.put(key, sound);
    }
    public Sound getSound(String key) {
        return sounds.get(key);
    }


    public void removeAll() {
        for(Object o : textures.values()) {
            Texture tex = (Texture) o;
            tex.dispose();
        }
        textures.clear();
        for(Object o : music.values()) {
            Music music = (Music) o;
            music.dispose();
        }
        music.clear();
        for(Object o : sounds.values()) {
            Sound sound = (Sound) o;
            sound.dispose();
        }
        sounds.clear();
    }

}
