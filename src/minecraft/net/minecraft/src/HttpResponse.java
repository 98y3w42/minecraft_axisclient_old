package net.minecraft.src;

import java.util.LinkedHashMap;
import java.util.Map;

public class HttpResponse
{
    private int status = 0;
    private String statusLine = null;
    private Map<String, String> headers = new LinkedHashMap();
    private byte[] body = null;

    public HttpResponse(int p_i52_1_, String p_i52_2_, Map p_i52_3_, byte[] p_i52_4_)
    {
        this.status = p_i52_1_;
        this.statusLine = p_i52_2_;
        this.headers = p_i52_3_;
        this.body = p_i52_4_;
    }

    public int getStatus()
    {
        return this.status;
    }

    public String getStatusLine()
    {
        return this.statusLine;
    }

    public Map getHeaders()
    {
        return this.headers;
    }

    public String getHeader(String p_getHeader_1_)
    {
        return (String)this.headers.get(p_getHeader_1_);
    }

    public byte[] getBody()
    {
        return this.body;
    }
}
