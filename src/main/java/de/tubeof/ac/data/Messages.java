package de.tubeof.ac.data;

import de.tubeof.ac.enums.MessageType;

public class Messages {

    public Messages() {}

    private String startupPrefix = "§7[§3AntiCooldownLogger§7] ";
    private String prefix;

    private String switchWorldDisabled;
    private String switchWorldEnabled;

    private String loginDisabled;
    private String loginEnabled;

    private String settingAddDisabledWorld;
    private String settingsRemoveDisabledWorld;

    private String errorWorldAlradyListed;
    private String errorWorldNotListed;
    private String playerNotOnline;
    private String noPermissions;

    public void setTextMessages(MessageType messageType, String message) {
        //Prefix
        if(messageType == MessageType.PREFIX) prefix = message;

        //Switch World
        if(messageType == MessageType.SWITCH_WORLD_DISABLED) switchWorldDisabled = message;
        if(messageType == MessageType.SWITCH_WORLD_ENABLED) switchWorldEnabled = message;

        //Login
        if(messageType == MessageType.LOGIN_DISABLED) loginDisabled = message;
        if(messageType == MessageType.LOGIN_ENABLED) loginEnabled = message;

        //Settings
        if(messageType == MessageType.SETTING_ADD_DISABLED_WORLD) settingAddDisabledWorld = message;
        if(messageType == MessageType.SETTING_REMOVE_DISABLED_WORLD) settingsRemoveDisabledWorld = message;

        //Error
        if(messageType == MessageType.ERROR_WORLD_ALRADY_LISTED) errorWorldAlradyListed = message;
        if(messageType == MessageType.ERROR_WORLD_NOT_LISTED) errorWorldNotListed = message;
        if(messageType == MessageType.ERROR_PLAYER_NOT_ONLINE) playerNotOnline = message;
        if(messageType == MessageType.ERROR_NO_PERMISSIONS) noPermissions = message;
    }

    public String getTextMessage(MessageType messageType) {
        //Prefixes
        if(messageType == MessageType.STARTUP_PREFIX) return startupPrefix;
        if(messageType == MessageType.PREFIX) return prefix;

        //Switch World
        if(messageType == MessageType.SWITCH_WORLD_DISABLED) return switchWorldDisabled;
        if(messageType == MessageType.SWITCH_WORLD_ENABLED) return switchWorldEnabled;

        //Login
        if(messageType == MessageType.LOGIN_DISABLED) return loginDisabled;
        if(messageType == MessageType.LOGIN_ENABLED) return loginEnabled;

        //Settings
        if(messageType == MessageType.SETTING_ADD_DISABLED_WORLD) return settingAddDisabledWorld;
        if(messageType == MessageType.SETTING_REMOVE_DISABLED_WORLD) return settingsRemoveDisabledWorld;

        //Error
        if(messageType == MessageType.ERROR_WORLD_ALRADY_LISTED) return errorWorldAlradyListed;
        if(messageType == MessageType.ERROR_WORLD_NOT_LISTED) return errorWorldNotListed;
        if(messageType == MessageType.ERROR_NO_PERMISSIONS) return noPermissions;
        return null;
    }
}
