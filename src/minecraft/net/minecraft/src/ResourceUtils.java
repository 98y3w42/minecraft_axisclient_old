package net.minecraft.src;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Locale;

public class ResourceUtils
{
    private static ReflectorClass ForgeAbstractResourcePack = new ReflectorClass(AbstractResourcePack.class);
    private static ReflectorField ForgeAbstractResourcePack_resourcePackFile = new ReflectorField(ForgeAbstractResourcePack, "resourcePackFile");
    private static boolean directResourcePack = true;
    private static Map localeProperties = null;

    public static File getResourcePackFile(AbstractResourcePack p_getResourcePackFile_0_)
    {
        if (directResourcePack)
        {
            try
            {
                return p_getResourcePackFile_0_.resourcePackFile;
            }
            catch (IllegalAccessError illegalaccesserror)
            {
                directResourcePack = false;

                if (!ForgeAbstractResourcePack_resourcePackFile.exists())
                {
                    throw illegalaccesserror;
                }
            }
        }

        return (File)Reflector.getFieldValue(p_getResourcePackFile_0_, ForgeAbstractResourcePack_resourcePackFile);
    }

    public static Map getLocaleProperties()
    {
        if (localeProperties == null)
        {
            try
            {
                Field field = Reflector.getField(I18n.class, Locale.class);
                Object object = field.get((Object)null);
                Field field1 = Reflector.getField(Locale.class, Map.class);
                localeProperties = (Map)field1.get(object);
            }
            catch (Throwable throwable)
            {
                Config.warn("[ResourceUtils] Error getting locale properties: " + throwable.getClass().getName() + ": " + throwable.getMessage());
                localeProperties = new HashMap();
            }
        }

        return localeProperties;
    }
}
