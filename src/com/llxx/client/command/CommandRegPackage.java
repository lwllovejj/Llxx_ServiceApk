package com.llxx.client.command;

import org.json.JSONArray;
import org.json.JSONObject;

import com.llxx.socket.loger.Ll_Loger;
import com.llxx.socket.service.Ll_AccessibilityService;

public class CommandRegPackage extends CommandRun
{
    public static final String TAG = "CommandRegPackage";
    int type = 0;

    @Override
    public boolean runCommand(Ll_AccessibilityService accessibilityService)
    {
        {
            try
            {
                JSONObject object = new JSONObject(getMessage().getMessage());
                JSONObject params = object.getJSONObject(PARAMS);
                JSONArray array = params.optJSONArray("packages");
                String[] stirngs = new String[array.length()];
                for (int i = 0; i < stirngs.length; i++)
                {
                    stirngs[i] = array.getString(i);
                }
                accessibilityService.setPackage(stirngs);
                Ll_Loger.i(TAG, "CommandRegPackage -> set packages ");
                setRunOk(true);
                return true;
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public String action()
    {
        return "regPackage";
    }

    /**
     * @return the type
     */
    public int getType()
    {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type)
    {
        this.type = type;
    }

}
