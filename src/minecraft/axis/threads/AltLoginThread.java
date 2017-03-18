package axis.threads;

import java.net.Proxy;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import axis.Axis;
import axis.util.Alt;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class AltLoginThread extends Thread
{
    private final Minecraft mc;
    private final String password;
    private String status;
    private final String username;

    public AltLoginThread(final String username, final String password) {
        super("Alt Login Thread");
        this.mc = Minecraft.getMinecraft();
        this.username = username;
        this.password = password;
        this.status = "§7Waiting...";
    }

    private final Session createSession(final String username, final String password) {
        final YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(username);
        auth.setPassword(password);
        try {
            auth.logIn();
            return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
        }
        catch (AuthenticationException var6) {
            return null;
        }
    }

    public String getStatus() {
        return this.status;
    }

    @Override
    public void run() {
    	String oldusername = mc.session.getUsername();
        if (this.password.equals("") && !this.username.equals("N00bPot")) {
            this.mc.session = new Session(this.username, "", "", "mojang");
            this.status = "§aLogged in. (" + this.username + " - offline name)";
        }
        else {
            this.status = "§eLogging in...";
            final Session auth = this.createSession(this.username, this.password);
            if (auth == null) {
                this.status = "§cLogin failed!";
            }
            else {
                Axis.getAxis().getAltManager().setLastAlt(new Alt(this.username, this.password));
                Axis.getAxis().getFileManager().getFileByName("lastalt").saveFile();
                this.status = "§aLogged in. (" + auth.getUsername() + ")";
                this.mc.session = auth;
            }
        }
    }

    public void setStatus(final String status) {
        this.status = status;
    }
}
