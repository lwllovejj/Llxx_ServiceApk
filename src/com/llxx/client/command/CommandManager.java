package com.llxx.client.command;

import java.util.HashMap;

public class CommandManager
{
    public static final HashMap<String, Class<? extends CommandRun>> mProtocols = new HashMap<String, Class<? extends CommandRun>>();

    static
    {
        mProtocols.put("queryAccessibility", CommandQuery.class);
        mProtocols.put("regPackage", CommandRegPackage.class);
        mProtocols.put("uiSecletAction", CommandSelectAction.class);
    }
}
