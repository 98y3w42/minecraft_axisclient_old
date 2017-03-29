package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiResourcePackAvailable;
import net.minecraft.client.gui.GuiResourcePackSelected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.ResourcePackListEntry;
import net.minecraft.client.resources.ResourcePackListEntryDefault;
import net.minecraft.client.resources.ResourcePackListEntryFound;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Sys;

public class GuiScreenResourcePacks extends GuiScreen {
   private static final Logger logger = LogManager.getLogger();
   private final GuiScreen parentScreen;
   private List availableResourcePacks;
   private List selectedResourcePacks;
   private GuiResourcePackAvailable availableResourcePacksList;
   private GuiResourcePackSelected selectedResourcePacksList;
   private boolean changed = false;

   public GuiScreenResourcePacks(GuiScreen parentScreenIn) {
      this.parentScreen = parentScreenIn;
   }

   public void initGui() {
      this.buttonList.add(new GuiOptionButton(2, width / 2 - 154, height - 48, I18n.format("resourcePack.openFolder", new Object[0])));
      this.buttonList.add(new GuiOptionButton(1, width / 2 + 4, height - 48, I18n.format("gui.done", new Object[0])));
      if(!this.changed) {
         this.availableResourcePacks = Lists.newArrayList();
         this.selectedResourcePacks = Lists.newArrayList();
         ResourcePackRepository resourcepackrepository = this.mc.getResourcePackRepository();
         resourcepackrepository.updateRepositoryEntriesAll();
         ArrayList list = Lists.newArrayList(resourcepackrepository.getRepositoryEntriesAll());
         list.removeAll(resourcepackrepository.getRepositoryEntries());
         Iterator var4 = list.iterator();

         ResourcePackRepository.Entry resourcepackrepository$entry1;
         while(var4.hasNext()) {
            resourcepackrepository$entry1 = (ResourcePackRepository.Entry)var4.next();
            this.availableResourcePacks.add(new ResourcePackListEntryFound(this, resourcepackrepository$entry1));
         }

         var4 = Lists.reverse(resourcepackrepository.getRepositoryEntries()).iterator();

         while(var4.hasNext()) {
            resourcepackrepository$entry1 = (ResourcePackRepository.Entry)var4.next();
            this.selectedResourcePacks.add(new ResourcePackListEntryFound(this, resourcepackrepository$entry1));
         }

         this.selectedResourcePacks.add(new ResourcePackListEntryDefault(this));
      }

      this.availableResourcePacksList = new GuiResourcePackAvailable(this.mc, 200, height, this.availableResourcePacks);
      this.availableResourcePacksList.setSlotXBoundsFromLeft(width / 2 - 4 - 200);
      this.availableResourcePacksList.registerScrollButtons(7, 8);
      this.selectedResourcePacksList = new GuiResourcePackSelected(this.mc, 200, height, this.selectedResourcePacks);
      this.selectedResourcePacksList.setSlotXBoundsFromLeft(width / 2 + 4);
      this.selectedResourcePacksList.registerScrollButtons(7, 8);
   }

   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      this.selectedResourcePacksList.handleMouseInput();
      this.availableResourcePacksList.handleMouseInput();
   }

   public boolean hasResourcePackEntry(ResourcePackListEntry p_146961_1_) {
      return this.selectedResourcePacks.contains(p_146961_1_);
   }

   public List getListContaining(ResourcePackListEntry p_146962_1_) {
      return this.hasResourcePackEntry(p_146962_1_)?this.selectedResourcePacks:this.availableResourcePacks;
   }

   public List getAvailableResourcePacks() {
      return this.availableResourcePacks;
   }

   public List getSelectedResourcePacks() {
      return this.selectedResourcePacks;
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if(button.enabled) {
         if(button.id == 2) {
            File list = this.mc.getResourcePackRepository().getDirResourcepacks();
            String resourcepackrepository$entry = list.getAbsolutePath();
            if(Util.getOSType() == Util.EnumOS.OSX) {
               try {
                  logger.info(resourcepackrepository$entry);
                  Runtime.getRuntime().exec(new String[]{"/usr/bin/open", resourcepackrepository$entry});
                  return;
               } catch (IOException var9) {
                  logger.error("Couldn\'t open file", var9);
               }
            } else if(Util.getOSType() == Util.EnumOS.WINDOWS) {
               String flag = String.format("cmd.exe /C start \"Open file\" \"%s\"", new Object[]{resourcepackrepository$entry});

               try {
                  Runtime.getRuntime().exec(flag);
                  return;
               } catch (IOException var8) {
                  logger.error("Couldn\'t open file", var8);
               }
            }

            boolean flag1 = false;

            try {
               Class throwable = Class.forName("java.awt.Desktop");
               Object object = throwable.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
               throwable.getMethod("browse", new Class[]{URI.class}).invoke(object, new Object[]{list.toURI()});
            } catch (Throwable var7) {
               logger.error("Couldn\'t open link", var7);
               flag1 = true;
            }

            if(flag1) {
               logger.info("Opening via system class!");
               Sys.openURL("file://" + resourcepackrepository$entry);
            }
         } else if(button.id == 1) {
            if(this.changed) {
               ArrayList list1 = Lists.newArrayList();
               Iterator flag2 = this.selectedResourcePacks.iterator();

               while(flag2.hasNext()) {
                  ResourcePackListEntry resourcepackrepository$entry1 = (ResourcePackListEntry)flag2.next();
                  if(resourcepackrepository$entry1 instanceof ResourcePackListEntryFound) {
                     list1.add(((ResourcePackListEntryFound)resourcepackrepository$entry1).func_148318_i());
                  }
               }

               Collections.reverse(list1);
               this.mc.getResourcePackRepository().setRepositories(list1);
               this.mc.gameSettings.resourcePacks.clear();
               this.mc.gameSettings.field_183018_l.clear();
               flag2 = list1.iterator();

               while(flag2.hasNext()) {
                  ResourcePackRepository.Entry resourcepackrepository$entry2 = (ResourcePackRepository.Entry)flag2.next();
                  this.mc.gameSettings.resourcePacks.add(resourcepackrepository$entry2.getResourcePackName());
                  if(resourcepackrepository$entry2.func_183027_f() != 1) {
                     this.mc.gameSettings.field_183018_l.add(resourcepackrepository$entry2.getResourcePackName());
                  }
               }

               this.mc.gameSettings.saveOptions();
               this.mc.refreshResources();
            }

            this.mc.displayGuiScreen(this.parentScreen);
         }
      }

   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      this.availableResourcePacksList.mouseClicked(mouseX, mouseY, mouseButton);
      this.selectedResourcePacksList.mouseClicked(mouseX, mouseY, mouseButton);
   }

   protected void mouseReleased(int mouseX, int mouseY, int state) {
      super.mouseReleased(mouseX, mouseY, state);
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawBackground(0);
      this.availableResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
      this.selectedResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
      this.drawCenteredString(this.fontRendererObj, I18n.format("resourcePack.title", new Object[0]), width / 2, 16, 16777215);
      this.drawCenteredString(this.fontRendererObj, I18n.format("resourcePack.folderInfo", new Object[0]), width / 2 - 77, height - 26, 8421504);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   public void markChanged() {
      this.changed = true;
   }
}