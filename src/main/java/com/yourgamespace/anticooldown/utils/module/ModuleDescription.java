package com.yourgamespace.anticooldown.utils.module;

import org.bukkit.plugin.InvalidDescriptionException;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class ModuleDescription {

    private final Pattern VALID_NAME = Pattern.compile("^[A-Za-z0-9 _.-]+$");
    private final ThreadLocal<Yaml> YAML = new ThreadLocal<>();

    public ModuleDescription(InputStream stream) throws InvalidDescriptionException {
        loadMap(asMap(YAML.get().load(stream)));
    }

    private String name;
    private String main;
    private String version;
    private String author;
    private String description;

    public String getName() {
        return this.name;
    }


    public String getVersion() {
        return this.version;
    }


    public String getAuthor() {
        return this.author;
    }


    public String getMain() {
        return this.main;
    }

    public String getDescription() {
        return this.description;
    }


    private void loadMap(Map<?, ?> map) throws InvalidDescriptionException {
        try {
            this.name = map.get("name").toString();
            if (!VALID_NAME.matcher(this.name).matches()) {
                throw new InvalidDescriptionException("Name '" + this.name + "' contains invalid characters");
            }

            this.name = this.name.replace(' ', '_');
        } catch (NullPointerException e) {
            throw new InvalidDescriptionException(e, "Name is not defined");
        } catch (ClassCastException e) {
            throw new InvalidDescriptionException(e, "Name is of wrong type");
        }

        try {
            this.version = map.get("version").toString();
        } catch (NullPointerException e) {
            throw new InvalidDescriptionException(e, "Version is not defined");
        } catch (ClassCastException e) {
            throw new InvalidDescriptionException(e, "Version is of wrong type");
        }

        try {
            this.main = map.get("main").toString();
        } catch (NullPointerException e) {
            throw new InvalidDescriptionException(e, "Main is not defined");
        } catch (ClassCastException e) {
            throw new InvalidDescriptionException(e, "Main is of wrong type");
        }

        if (map.get("description") != null) {
            this.description = map.get("description").toString();
        }
        if (map.get("author") != null) {
            this.author = map.get("author").toString();
        }
    }

    private Map<?, ?> asMap(Object object) throws InvalidDescriptionException {
        if (object instanceof Map) return (Map) object;
        throw new InvalidDescriptionException(object + " is not properly structured");
    }
}
