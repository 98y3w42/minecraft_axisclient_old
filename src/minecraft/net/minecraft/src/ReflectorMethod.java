package net.minecraft.src;

import java.lang.reflect.Method;

public class ReflectorMethod
{
    private ReflectorClass reflectorClass;
    private String targetMethodName;
    private Class[] targetMethodParameterTypes;
    private boolean checked;
    private Method targetMethod;

    public ReflectorMethod(ReflectorClass p_i77_1_, String p_i77_2_)
    {
        this(p_i77_1_, p_i77_2_, (Class[])null, false);
    }

    public ReflectorMethod(ReflectorClass p_i78_1_, String p_i78_2_, Class[] p_i78_3_)
    {
        this(p_i78_1_, p_i78_2_, p_i78_3_, false);
    }

    public ReflectorMethod(ReflectorClass p_i79_1_, String p_i79_2_, Class[] p_i79_3_, boolean p_i79_4_)
    {
        this.reflectorClass = null;
        this.targetMethodName = null;
        this.targetMethodParameterTypes = null;
        this.checked = false;
        this.targetMethod = null;
        this.reflectorClass = p_i79_1_;
        this.targetMethodName = p_i79_2_;
        this.targetMethodParameterTypes = p_i79_3_;

        if (!p_i79_4_)
        {
            Method method = this.getTargetMethod();
        }
    }

    public Method getTargetMethod()
    {
        if (this.checked)
        {
            return this.targetMethod;
        }
        else
        {
            this.checked = true;
            Class oclass = this.reflectorClass.getTargetClass();

            if (oclass == null)
            {
                return null;
            }
            else
            {
                try
                {
                    Method[] amethod = oclass.getDeclaredMethods();
                    int i = 0;
                    Method method;

                    while (true)
                    {
                        if (i >= amethod.length)
                        {
                            Config.log("(Reflector) Method not present: " + oclass.getName() + "." + this.targetMethodName);
                            return null;
                        }

                        method = amethod[i];

                        if (method.getName().equals(this.targetMethodName))
                        {
                            if (this.targetMethodParameterTypes == null)
                            {
                                break;
                            }

                            Class[] aclass = method.getParameterTypes();

                            if (Reflector.matchesTypes(this.targetMethodParameterTypes, aclass))
                            {
                                break;
                            }
                        }

                        ++i;
                    }

                    this.targetMethod = method;

                    if (!this.targetMethod.isAccessible())
                    {
                        this.targetMethod.setAccessible(true);
                    }

                    return this.targetMethod;
                }
                catch (Throwable throwable)
                {
                    throwable.printStackTrace();
                    return null;
                }
            }
        }
    }

    public boolean exists()
    {
        return this.checked ? this.targetMethod != null : this.getTargetMethod() != null;
    }

    public Class getReturnType()
    {
        Method method = this.getTargetMethod();
        return method == null ? null : method.getReturnType();
    }

    public void deactivate()
    {
        this.checked = true;
        this.targetMethod = null;
    }
}
