package axis.module.modules.render;

import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import axis.Axis;
import axis.command.Command;
import axis.event.events.DrawScreenEvent;
import axis.event.events.KeyboardEvent;
import axis.event.events.TickEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;
import axis.ui.tabgui.HexTabGui;
import axis.ui.tabgui.TabGui;
import axis.util.FontUtils;
import axis.util.Logger;
import axis.util.RenderingUtils;
import axis.value.Value;
import net.mcleaks.MCLeaks;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StringUtils;

public class HUD extends Module {

	public static int color = -1;
	public static int color1 = RenderingUtils.rainbow(30L, 1.0F, 255.0F).getRGB();
	public static int hexcolor = 0x951C2F96;
	private int v = 0x951C2F96;
	private int b = 0x951C2F96 + 100;
	private int d = 0x951C2F96 - 100;
	private int draw = 16777215;
	private boolean twi;
	public static int wirecolor = 0x001433;
	private boolean f;
	private int selected;
	private int selectedModule;
	private boolean open;
	public float width;
	public float height;
	public static Value<Boolean> arraylist = new Value("hud_arraylist", Boolean.valueOf(true));
	public static Value<Boolean> tabgui = new Value("hud_tabgui", Boolean.valueOf(true));
	public static Value<Boolean> armor = new Value("hud_armor-status", Boolean.valueOf(true));
	public static Value<Boolean> potions = new Value("hud_potioneffects", Boolean.valueOf(true));
	public static Value<String> currentMode = new Value("hud_mode", "Axis");
	private EntityPlayer ent;
	public static FontUtils test = new FontUtils("Test", Font.PLAIN, 18);
	public static FontUtils Comfortaa18 = new FontUtils("Comfortaa", Font.PLAIN, 18);
	public static FontUtils Comfortaa17 = new FontUtils("Comfortaa", Font.PLAIN, 17);
	public static FontUtils Comfortaa18a = new FontUtils("Comfortaa", Font.PLAIN, 18);
	public static FontUtils Raleway18 = new FontUtils("Raleway Thin", Font.BOLD, 18);
	public static FontUtils Raleway29 = new FontUtils("Raleway Thin", Font.BOLD, 29);
	public static FontUtils Minecraft18 = new FontUtils("Minecraft", Font.PLAIN, 18);
	public static FontUtils Raleway100 = new FontUtils("Raleway Thin", Font.BOLD, 100);
	public static FontUtils RalewayP = new FontUtils("Raleway Thin", Font.BOLD, 18);
	public static FontUtils MeiryoUI18 = new FontUtils("Meiryo UI", Font.PLAIN, 19);
	private Random random1 = new Random();

	public HUD() {
		super("HUD");
		this.setCategory(Category.RENDER);
		this.setEnabled(true);
		setTag(currentMode.getValue());
		Axis.getAxis().getCommandManager().getContents().add(new Command("hud", "<armorstatus/potioneffects/arraylist/tabgui/arraylistrectangle>", new String[0]) {
			public void run(String message) {
				if (message.split(" ")[1].equalsIgnoreCase("arraylist")) {
					HUD.arraylist.setValue(Boolean.valueOf(!((Boolean) HUD.arraylist.getValue()).booleanValue()));
					Logger.logChat("HUD will " + (((Boolean) HUD.arraylist.getValue()).booleanValue() ? "now" : "no longer") + " display the arraylist.");
				} else if (message.split(" ")[1].equalsIgnoreCase("tabgui")) {
					HUD.tabgui.setValue(Boolean.valueOf(!((Boolean) HUD.tabgui.getValue()).booleanValue()));
					Logger.logChat("HUD will " + (((Boolean) HUD.tabgui.getValue()).booleanValue() ? "now" : "no longer") + " display the tabgui.");
				} else if (message.split(" ")[1].equalsIgnoreCase("armorstatus")) {
					HUD.armor.setValue(Boolean.valueOf(!((Boolean) HUD.armor.getValue()).booleanValue()));
					Logger.logChat("HUD will " + (((Boolean) HUD.armor.getValue()).booleanValue() ? "now" : "no longer") + " display the armor status.");
				} else if (message.split(" ")[1].equalsIgnoreCase("potioneffects")) {
					HUD.potions.setValue(Boolean.valueOf(!((Boolean) HUD.potions.getValue()).booleanValue()));
					Logger.logChat("HUD will " + (((Boolean) HUD.potions.getValue()).booleanValue() ? "now" : "no longer") + " display the potion effects.");
				} else if (!message.split(" ")[1].equalsIgnoreCase("mode")) {
					Logger.logChat("Option not valid! Available options: armorstatus, potioneffects, arraylist, tabgui, arraylistrectangle, mode.");
				}

			}
		});
		Axis.getAxis().getCommandManager().getContents()
				.add(new Command("hud", "<mode>", new String[] { "hudmode", "hd" }) {
					public void run(String message) {
						if (message.split(" ")[1].equalsIgnoreCase("mode")) {
							if (message.split(" ")[2].equalsIgnoreCase("Axis")) {
								currentMode.setValue("Axis");
								TabGui.init();
								Logger.logChat("HUD Mode is " + currentMode.getValue());
							} else if (message.split(" ")[2].equalsIgnoreCase("Hex")) {
								currentMode.setValue("Hex");
								HexTabGui.init();
								Logger.logChat("HUD Mode is " + currentMode.getValue());
							} else if (message.split(" ")[2].equalsIgnoreCase("Cicada")) {
								currentMode.setValue("Cicada");
								Logger.logChat("HUD Mode is " + currentMode.getValue());
							} else {
								Logger.logChat("Option not valid! Available options: Axis, Hex, Cicada");
							}
							setTag(currentMode.getValue());
						}
					}
				});
	}

	private void onKeypress(KeyboardEvent event) {
		if (currentMode.getValue().equals("Axis")) {
			TabGui.keyPress(event.key);
		} else if (currentMode.getValue().equals("Hex")) {
			HexTabGui.keyPress(event.key);
		}
	}

	public void onTick(TickEvent event) {
		color1 = RenderingUtils.rainbow(30L, 1.0F, 255.0F).getRGB();
		if (hexcolor < d || hexcolor == d || !f) {
			if (hexcolor == d) {
				f = true;
				hexcolor = hexcolor + 5;
			}
			if (!f) {
				f = false;
				hexcolor = hexcolor - 5;
			}
		} else if (hexcolor > b || hexcolor == b || f) {
			if (hexcolor > b + 1) {
				hexcolor = hexcolor - 5;
				draw++;
				if (draw > 20) {
					f = false;
					hexcolor = hexcolor - 5;
					draw = 0;
				}
			}
			if (f) {
				f = true;
				hexcolor = hexcolor + 5;
			}
		}
	}

	public void onDraw(DrawScreenEvent event) {
		if (!Axis.getAxis().getModuleManager().getModuleByName("GhoastHUD").isEnabled()) {
			if (currentMode.getValue().equals("Axis")) {
				ScaledResolution scaledRes = new ScaledResolution(mc);
				boolean flag = mc.fontRendererObj.getUnicodeFlag();
				mc.fontRendererObj.setUnicodeFlag(false);

				TabGui.render();
				if (mc.thePlayer != null && mc.theWorld != null) {
					Comfortaa17.drawString(Axis.getAxis().getVersion(), 40.0F, 6.0F, hexcolor);
					Raleway29.drawString(Axis.getAxis().getName(), 8.0F, 0.0F, color1);
					test.drawString("fake", 0.0F, -15.0F, 0);
				}

				float y1 = (float) (scaledRes.getScaledHeight() - 23);
				float y2 = (float) (88);
				String user;
				if (MCLeaks.getMCName() == null) {
					user = mc.session.getUsername();
				} else {
					user = MCLeaks.getMCName();
				}
				float userno = user.length();
				userno = userno * 6;
				Comfortaa18.drawStringWithShadow("UserName: \u00a7f" + user, 2.0F, y1 + 3.0F, hexcolor);
				Comfortaa18.drawStringWithShadow("X: \u00a7f" + Math.round(mc.thePlayer.posX), 2.0F, y1 + 14.0F, hexcolor);
				Comfortaa18.drawStringWithShadow(" Y: \u00a7f" + Math.round(mc.thePlayer.posY), 33.0F, y1 + 14.0F, hexcolor);
				Comfortaa18.drawStringWithShadow(" Z: \u00a7f" + Math.round(mc.thePlayer.posZ), 67.0F, y1 + 14.0F, hexcolor);
				long ping = 0;
				try {
					ping = mc.getCurrentServerData().pingToServer;
				} catch (NullPointerException var9) {
					ping = 0;
				}
				int fps1;
				float width = scaledRes.getScaledWidth();
				float heighth = scaledRes.getScaledHeight();
				Comfortaa18.drawStringWithShadow("Ping: \u00a7f" + ping, width - 35, heighth - 20, hexcolor);
				Comfortaa18a.drawStringWithShadow("fake", width, heighth - 20, hexcolor);

				if (((Boolean) potions.getValue()).booleanValue()) {
					this.axisdrawPotionEffects(scaledRes);
				}

				if (((Boolean) armor.getValue()).booleanValue()) {
					this.drawArmorStatus(scaledRes);
				}

				if (((Boolean) arraylist.getValue()).booleanValue()) {
					this.drawArraylist(scaledRes);
				}

				mc.fontRendererObj.setUnicodeFlag(flag);
			} else if (currentMode.getValue().equals("Hex")) {
				ScaledResolution scaledRes = new ScaledResolution(mc);
				boolean flag = mc.fontRendererObj.getUnicodeFlag();
				mc.fontRendererObj.setUnicodeFlag(false);

				HexTabGui.render();

				GL11.glPushMatrix();
				GL11.glScaled(1.5D, 1.3D, 1.3D);
				mc.fontRendererObj.drawStringWithShadow("H", 2.0F, 1.0F, hexcolor);
				GL11.glPopMatrix();
				mc.fontRendererObj.drawStringWithShadow("ex", 12.5F, 3.5F, hexcolor);
				mc.fontRendererObj.drawStringWithShadow("", 2 + mc.fontRendererObj.getStringWidth("Hex") + 6, 4.0F, -1);
				SimpleDateFormat format = new SimpleDateFormat("h:mm a", Locale.ENGLISH);
				mc.fontRendererObj.drawStringWithShadow(" §7" + format.format(new Date()), 2 + mc.fontRendererObj.getStringWidth("Hex") + mc.fontRendererObj.getStringWidth("§9" + "") + 6, 4.0F, -1);

				float y1 = (float) (scaledRes.getScaledHeight() - 23);
				float y2 = (float) (88);
				mc.fontRendererObj.drawStringWithShadow("\u00a77X: \u00a7f" + Math.round(mc.thePlayer.posX) + "\u00a77 Y: \u00a7f" + Math.round(mc.thePlayer.posY) + "\u00a77 Z: \u00a7f" + Math.round(mc.thePlayer.posZ), 2.0F,
						GuiNewChat.getChatOpen() ? y1 : y1 + 14.0F, -1);
				long ping = 0;
				try {
					ping = mc.getCurrentServerData().pingToServer;
				} catch (NullPointerException var9) {
					ping = 0;
				}
				mc.fontRendererObj.drawStringWithShadow("\u00a77Ping: \u00a7f" + ping, 2.0F, y2, -1);

				if (((Boolean) potions.getValue()).booleanValue()) {
					this.drawPotionEffects(scaledRes);
				}

				if (((Boolean) armor.getValue()).booleanValue()) {
					this.drawArmorStatus(scaledRes);
				}

				if (((Boolean) arraylist.getValue()).booleanValue()) {
					this.hexdrawArraylist(scaledRes);
				}

				mc.fontRendererObj.setUnicodeFlag(flag);
			} else if (currentMode.getValue().equals("Cicada")) {
				ScaledResolution scaledRes = new ScaledResolution(mc);
				boolean flag = mc.fontRendererObj.getUnicodeFlag();
				mc.fontRendererObj.setUnicodeFlag(false);
				mc.fontRendererObj.drawStringWithShadow("Cicada", 2.0F, 1.0F, -2145320961);
				if (((Boolean) arraylist.getValue()).booleanValue()) {
					this.cicadadrawArraylist(scaledRes);
				}
				mc.fontRendererObj.setUnicodeFlag(flag);
			}
		}
	}

	private void cicadadrawArraylist(ScaledResolution scaledRes) {
		int y = 0;
		List mods = Axis.getAxis().getModuleManager().getContents();
		Collections.sort(mods, new Comparator() {
			public int compare(Module mod1, Module mod2) {
				return mc.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(mod1.getDisplayName())) > mc.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(mod2.getDisplayName())) ? -1
						: (mc.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(mod1.getDisplayName())) < mc.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(mod2.getDisplayName())) ? 1 : 0);
			}

			public int compare(Object var1, Object var2) {
				return this.compare((Module) var1, (Module) var2);
			}
		});

		Iterator var5 = mods.iterator();

		while (var5.hasNext()) {
			Module module = (Module) var5.next();
			if (module.isVisible() && module.isEnabled()) {
				ArrayList validmods = new ArrayList();
				validmods.add(module);
				Iterator var8 = validmods.iterator();

				while (var8.hasNext()) {
					Module mod = (Module) var8.next();
					int xf = (int) ((float) scaledRes.getScaledWidth() - mc.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(mod.getDisplayName())) - 2.0F);
					GL11.glPushMatrix();
					float width = mc.fontRendererObj.getStringWidth(mod.getDisplayName());
					float height = y + 10;
					float widthc = (float) scaledRes.getScaledWidth() - mc.fontRendererObj.getStringWidth(mod.getDisplayName()) - 2;
					mc.fontRendererObj.drawStringWithShadow(mod.getDisplayName(), 2.0F, (int) height, this.cicadaarray());
					if (this.height < height) {
						this.height = height;
					}

					if (this.width < width) {
						this.width = width;
					}

					y = (int) ((double) y + (double) mc.fontRendererObj.FONT_HEIGHT + 1.0D);
					GL11.glPopMatrix();
				}
			}
		}
	}

	private void drawArraylist(ScaledResolution scaledRes) {
		int y = 0;
		List mods = Axis.getAxis().getModuleManager().getContents();
		Collections.sort(mods, new Comparator() {
			public int compare(Module mod1, Module mod2) {
				return mc.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(mod1.getDisplayName())) > mc.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(mod2.getDisplayName())) ? -1
						: (mc.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(mod1.getDisplayName())) < mc.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(mod2.getDisplayName())) ? 1 : 0);
			}

			public int compare(Object var1, Object var2) {
				return this.compare((Module) var1, (Module) var2);
			}
		});

		Iterator var5 = mods.iterator();

		while (var5.hasNext()) {
			Module module = (Module) var5.next();
			if (module.isVisible() && module.isEnabled()) {
				ArrayList validmods = new ArrayList();
				validmods.add(module);
				Iterator var8 = validmods.iterator();

				while (var8.hasNext()) {
					Module mod = (Module) var8.next();
					int xf = (int) ((float) scaledRes.getScaledWidth() - Raleway18.getWidth(StringUtils.stripControlCodes(mod.getDisplayName())) - 2.0F);
					GL11.glPushMatrix();
					float width = mc.fontRendererObj.getStringWidth(mod.getDisplayName());
					float height = 0 + y;
					float widthc = (float) scaledRes.getScaledWidth() - mc.fontRendererObj.getStringWidth(mod.getDisplayName()) - 2;
					Raleway18.drawStringWithShadow(mod.getDisplayName(), (int) widthc, (int) height, color1);
					if (this.height < height) {
						this.height = height;
					}

					if (this.width < width) {
						this.width = width;
					}

					y = (int) ((double) y + (double) mc.fontRendererObj.FONT_HEIGHT + 1.0D);
					GL11.glPopMatrix();
				}
			}
		}
	}

	private void hexdrawArraylist(ScaledResolution scaledRes) {
		int y = 0;
		List mods = Axis.getAxis().getModuleManager().getContents();
		Collections.sort(mods, new Comparator() {
			public int compare(Module mod1, Module mod2) {
				return mc.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(mod1.getDisplayName())) > mc.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(mod2.getDisplayName())) ? -1
						: (mc.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(mod1.getDisplayName())) < mc.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(mod2.getDisplayName())) ? 1 : 0);
			}

			public int compare(Object var1, Object var2) {
				return this.compare((Module) var1, (Module) var2);
			}
		});

		Iterator var5 = mods.iterator();

		while (var5.hasNext()) {
			Module module = (Module) var5.next();
			if (module.isVisible() && module.isEnabled()) {
				ArrayList validmods = new ArrayList();
				validmods.add(module);
				Iterator var8 = validmods.iterator();

				while (var8.hasNext()) {
					Module mod = (Module) var8.next();
					int xf = (int) ((float) scaledRes.getScaledWidth() - mc.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(mod.getDisplayName())) - 2.0F);
					GL11.glPushMatrix();
					float width = mc.fontRendererObj.getStringWidth(mod.getDisplayName());
					float height = scaledRes.getScaledHeight() - 10 - y;
					float widthc = (float) scaledRes.getScaledWidth() - mc.fontRendererObj.getStringWidth(mod.getDisplayName()) - 2;
					mc.fontRendererObj.drawStringWithShadow(mod.getDisplayName(), (int) widthc, (int) height, hexcolor);
					if (this.height < height) {
						this.height = height;
					}

					if (this.width < width) {
						this.width = width;
					}

					y = (int) ((double) y + (double) mc.fontRendererObj.FONT_HEIGHT + 0.7D);
					GL11.glPopMatrix();
				}
			}
		}
	}

	private void drawPotionEffects(ScaledResolution scaledRes) {
		int y = 2;

		for (Iterator var4 = mc.thePlayer.getActivePotionEffects().iterator(); var4.hasNext(); y += mc.fontRendererObj.FONT_HEIGHT) {
			PotionEffect effect = (PotionEffect) var4.next();
			Potion potion = Potion.potionTypes[effect.getPotionID()];
			String name = I18n.format(potion.getName(), new Object[0]);
			if (effect.getAmplifier() == 1) {
				name = name + " II";
			} else if (effect.getAmplifier() == 2) {
				name = name + " III";
			} else if (effect.getAmplifier() == 3) {
				name = name + " IV";
			} else if (effect.getAmplifier() == 4) {
				name = name + " V";
			} else if (effect.getAmplifier() == 5) {
				name = name + " VI";
			} else if (effect.getAmplifier() == 6) {
				name = name + " VII";
			} else if (effect.getAmplifier() == 7) {
				name = name + " VIII";
			} else if (effect.getAmplifier() == 8) {
				name = name + " IX";
			} else if (effect.getAmplifier() == 9) {
				name = name + " X";
			} else if (effect.getAmplifier() > 10) {
				name = name + " X+";
			} else {
				name = name + " I";
			}

			name = name + "\u00a7f: \u00a77" + Potion.getDurationString(effect);
			int color = Integer.MIN_VALUE;
			if (effect.getEffectName() == "potion.weither") {
				color = -16777216;
			} else if (effect.getEffectName() == "potion.weakness") {
				color = -9868951;
			} else if (effect.getEffectName() == "potion.waterBreathing") {
				color = -16728065;
			} else if (effect.getEffectName() == "potion.saturation") {
				color = -11179217;
			} else if (effect.getEffectName() == "potion.resistance") {
				color = -5658199;
			} else if (effect.getEffectName() == "potion.regeneration") {
				color = -1146130;
			} else if (effect.getEffectName() == "potion.poison") {
				color = -14513374;
			} else if (effect.getEffectName() == "potion.nightVision") {
				color = -6737204;
			} else if (effect.getEffectName() == "potion.moveSpeed") {
				color = -7876870;
			} else if (effect.getEffectName() == "potion.moveSlowdown") {
				color = -16741493;
			} else if (effect.getEffectName() == "potion.jump") {
				color = -5374161;
			} else if (effect.getEffectName() == "potion.invisibility") {
				color = -9404272;
			} else if (effect.getEffectName() == "potion.hunger") {
				color = -16744448;
			} else if (effect.getEffectName() == "potion.heal") {
				color = -65536;
			} else if (effect.getEffectName() == "potion.harm") {
				color = -3730043;
			} else if (effect.getEffectName() == "potion.fireResistance") {
				color = -29696;
			} else if (effect.getEffectName() == "potion.healthBoost") {
				color = -40121;
			} else if (effect.getEffectName() == "potion.digSpeed") {
				color = -989556;
			} else if (effect.getEffectName() == "potion.digSlowdown") {
				color = -5658199;
			} else if (effect.getEffectName() == "potion.damageBoost") {
				color = -7667712;
			} else if (effect.getEffectName() == "potion.confusion") {
				color = -5192482;
			} else if (effect.getEffectName() == "potion.blindness") {
				color = -8355712;
			} else if (effect.getEffectName() == "potion.absorption") {
				color = -23296;
			}
			if (currentMode.getValue().equals("Hex")) {
				mc.fontRendererObj.drawStringWithShadow(name, (float) scaledRes.getScaledWidth() - mc.fontRendererObj.getStringWidth(name), y, color);
			}
		}
	}

	private void axisdrawPotionEffects(ScaledResolution scaledRes) {
		int y = scaledRes.getScaledHeight() - 32;

		for (Iterator var4 = mc.thePlayer.getActivePotionEffects().iterator(); var4.hasNext(); y -= 10) {
			PotionEffect effect = (PotionEffect) var4.next();
			Potion potion = Potion.potionTypes[effect.getPotionID()];
			String name = "";
			String name1 = "";
			if (effect.getEffectName() == "potion.weither") {
				name1 = "Wither";
			} else if (effect.getEffectName() == "potion.weakness") {
				name1 = "Weekness";
			} else if (effect.getEffectName() == "potion.waterBreathing") {
				name1 = "Water Breathing";
			} else if (effect.getEffectName() == "potion.saturation") {
				name1 = "Nausea";
			} else if (effect.getEffectName() == "potion.resistance") {
				name1 = "Resistance";
			} else if (effect.getEffectName() == "potion.regeneration") {
				name1 = "Regeneration";
			} else if (effect.getEffectName() == "potion.poison") {
				name1 = "Poison";
			} else if (effect.getEffectName() == "potion.nightVision") {
				name1 = "Night Vision";
			} else if (effect.getEffectName() == "potion.moveSpeed") {
				name1 = "Speed";
			} else if (effect.getEffectName() == "potion.moveSlowdown") {
				name1 = "Slowness";
			} else if (effect.getEffectName() == "potion.jump") {
				name1 = "Jump";
			} else if (effect.getEffectName() == "potion.invisibility") {
				name1 = "Invisibility";
			} else if (effect.getEffectName() == "potion.hunger") {
				name1 = "Hunger";
			} else if (effect.getEffectName() == "potion.heal") {
				name1 = "Heal";
			} else if (effect.getEffectName() == "potion.harm") {
				name1 = "Harm";
			} else if (effect.getEffectName() == "potion.fireResistance") {
				name1 = "Fire Resistance";
			} else if (effect.getEffectName() == "potion.healthBoost") {
				name1 = "Health Boost";
			} else if (effect.getEffectName() == "potion.digSpeed") {
				name1 = "Haste";
			} else if (effect.getEffectName() == "potion.digSlowdown") {
				name1 = "Mining Fatigue";
			} else if (effect.getEffectName() == "potion.damageBoost") {
				name1 = "Strength";
			} else if (effect.getEffectName() == "potion.confusion") {
				name1 = "Confusion";
			} else if (effect.getEffectName() == "potion.blindness") {
				name1 = "Blindness";
			} else if (effect.getEffectName() == "potion.absorption") {
				name1 = "Absorption";
			}
			if (effect.getAmplifier() == 1) {
				name = name + " II";
			} else if (effect.getAmplifier() == 2) {
				name = name + " III";
			} else if (effect.getAmplifier() == 3) {
				name = name + " IV";
			} else if (effect.getAmplifier() == 4) {
				name = name + " V";
			} else if (effect.getAmplifier() == 5) {
				name = name + " VI";
			} else if (effect.getAmplifier() == 6) {
				name = name + " VII";
			} else if (effect.getAmplifier() == 7) {
				name = name + " VIII";
			} else if (effect.getAmplifier() == 8) {
				name = name + " IX";
			} else if (effect.getAmplifier() == 9) {
				name = name + " X";
			} else if (effect.getAmplifier() > 10) {
				name = name + " X+";
			} else {
				name = name + " I";
			}
			int i = effect.getDuration() / 20;
			int j = i / 60;
			i = i % 60;
			String time = (String) (i < 10 ? j + ":0" + i : j + ":" + i);
			name = name1 + name + "\u00a7f: \u00a77" + time;
			int color = Integer.MIN_VALUE;
			if (effect.getEffectName() == "potion.weither") {
				color = -16777216;
			} else if (effect.getEffectName() == "potion.weakness") {
				color = -9868951;
			} else if (effect.getEffectName() == "potion.waterBreathing") {
				color = -16728065;
			} else if (effect.getEffectName() == "potion.saturation") {
				color = -11179217;
			} else if (effect.getEffectName() == "potion.resistance") {
				color = -5658199;
			} else if (effect.getEffectName() == "potion.regeneration") {
				color = -1146130;
			} else if (effect.getEffectName() == "potion.poison") {
				color = -14513374;
			} else if (effect.getEffectName() == "potion.nightVision") {
				color = -6737204;
			} else if (effect.getEffectName() == "potion.moveSpeed") {
				color = -7876870;
			} else if (effect.getEffectName() == "potion.moveSlowdown") {
				color = -16741493;
			} else if (effect.getEffectName() == "potion.jump") {
				color = -5374161;
			} else if (effect.getEffectName() == "potion.invisibility") {
				color = -9404272;
			} else if (effect.getEffectName() == "potion.hunger") {
				color = -16744448;
			} else if (effect.getEffectName() == "potion.heal") {
				color = -65536;
			} else if (effect.getEffectName() == "potion.harm") {
				color = -3730043;
			} else if (effect.getEffectName() == "potion.fireResistance") {
				color = -29696;
			} else if (effect.getEffectName() == "potion.healthBoost") {
				color = -40121;
			} else if (effect.getEffectName() == "potion.digSpeed") {
				color = -989556;
			} else if (effect.getEffectName() == "potion.digSlowdown") {
				color = -5658199;
			} else if (effect.getEffectName() == "potion.damageBoost") {
				color = -7667712;
			} else if (effect.getEffectName() == "potion.confusion") {
				color = -5192482;
			} else if (effect.getEffectName() == "potion.blindness") {
				color = -8355712;
			} else if (effect.getEffectName() == "potion.absorption") {
				color = -23296;
			}
			mc.fontutilt.drawStringWithShadow(name, (float) scaledRes.getScaledWidth() - mc.fontutilt.getStringWidth(name), y, color);
		}
	}

	private void drawArmorStatus(ScaledResolution scaledRes) {
		if (mc.playerController.isNotCreative()) {
			int x = 15;
			GL11.glPushMatrix();

			for (int index = 3; index >= 0; --index) {
				ItemStack stack = mc.thePlayer.inventory.armorInventory[index];
				if (stack != null) {
					mc.getRenderItem().renderItemIntoGUI(stack, scaledRes.getScaledWidth() / 2 + x - 1, scaledRes.getScaledHeight() - (mc.thePlayer.isInsideOfMaterial(Material.water) ? 65 : 55) - 2);
					mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, stack, scaledRes.getScaledWidth() / 2 + x - 1, scaledRes.getScaledHeight() - (mc.thePlayer.isInsideOfMaterial(Material.water) ? 65 : 55) - 2);
					x += 18;
				}
			}

			GlStateManager.disableCull();
			GlStateManager.enableAlpha();
			GlStateManager.disableBlend();
			GlStateManager.disableLighting();
			GlStateManager.disableCull();
			GlStateManager.clear(256);
			GL11.glPopMatrix();
		}

	}

	public void onEnabled() {
		if (currentMode.getValue().equals("Axis")) {
			TabGui.init();
		} else if (currentMode.getValue().equals("Hex")) {
			HexTabGui.init();
		}
		super.onEnabled();
	}

	public void onDisabled() {
		super.onDisabled();
	}

	public int cicadaarray() {
		int random = (random1.nextInt(1145141919) * -1) - 1;
		return random;
	}

}