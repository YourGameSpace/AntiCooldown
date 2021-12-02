package com.yourgamespace.anticooldown.utils.module;

import org.bukkit.plugin.InvalidDescriptionException;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;
import java.util.regex.Pattern;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class ModuleDescription {

    private final Pattern pattern = Pattern.compile("^[A-Za-z0-9_-]+$");

    private final String name;
    private final String main;
    private final String version;
    private final String author;
    private final String description;

    public ModuleDescription(InputStream stream) throws InvalidDescriptionException {
        Map<?, ?> map = new Yaml().load(stream);

        try {
            this.name = map.get("name").toString();
            if (!pattern.matcher(this.name).matches()) {
                throw new InvalidDescriptionException("Name '" + this.name + "' contains invalid characters");
            }
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

        this.description = map.get("description").toString();
        this.author = map.get("author").toString();
    }

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

}
