package com.yourgamespace.anticooldown.utils.module;

public class ModuleException extends Exception {

    public ModuleException(Throwable cause, String message) {
        super(message, cause);
    }

    public ModuleException(Throwable cause) {
        super("Invalid module.yml", cause);
    }

    public ModuleException(String message) {
        super(message);
    }

    public ModuleException() {
        super("Invalid module.yml");
    }
}
