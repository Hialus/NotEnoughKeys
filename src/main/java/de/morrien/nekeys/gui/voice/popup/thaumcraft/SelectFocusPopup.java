package de.morrien.nekeys.gui.voice.popup.thaumcraft;

//import baubles.api.BaublesApi;
//import baubles.api.cap.IBaublesItemHandler;
//import de.morrien.nekeys.api.command.IVoiceCommand;
//import de.morrien.nekeys.api.popup.AbstractPopup;
//import de.morrien.nekeys.gui.DropDownList;
//import de.morrien.nekeys.voice.command.thaumcraft.SelectFocusVoiceCommand;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.GlStateManager;
//import net.minecraft.client.resources.I18n;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.NonNullList;
//import org.lwjgl.opengl.GL11;
//import thaumcraft.api.casters.ICaster;
//import thaumcraft.common.items.casters.ItemFocus;
//import thaumcraft.common.items.casters.ItemFocusPouch;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
///**
// * Created by Timor Morrien
// */
//public class SelectFocusPopup extends AbstractPopup {
//
//    protected DropDownList<ItemStack> fociDropDown;
//
//    public SelectFocusPopup(String name, String rule) {
//        super(name, rule);
//        init();
//    }
//
//    public SelectFocusPopup(SelectFocusVoiceCommand voiceCommand) {
//        super(voiceCommand);
//        init();
//        for (ItemStack itemStack : fociDropDown.optionsList) {
//            if (getFociHash(itemStack).equals(voiceCommand.getFociHash())) {
//                fociDropDown.selection = itemStack;
//                break;
//            }
//        }
//    }
//
//    protected void init() {
//        fociDropDown = new DropDownList<>(0, 0, 0, 18, 5);
//        fociDropDown.optionsList.addAll(getFocis());
//        fociDropDown.stringifier = ItemStack::getDisplayName;
//        fociDropDown.cellHeight = 16;
//        fociDropDown.cellRenderer = (itemStack, x, y, width, dropDownList) -> {
//            if (Minecraft.getInstance().player == null) return;
//            GL11.glPushMatrix();
//            {
//                GlStateManager.enableLighting();
//                Minecraft.getInstance().getRenderItem().renderItemAndEffectIntoGUI(itemStack, x, y - 1);
//                GlStateManager.disableLighting();
//            }
//            GL11.glPopMatrix();
//            String label = dropDownList.stringifier.toString(itemStack);
//            int color = 0XFFFFFF;
//            if (label.equals("")) {
//                label = "Unnamed Foci";
//                color = 0xA0A0A0;
//            }
//            drawString(Minecraft.getInstance().fontRenderer, label, x + 16, y + 3, color);
//        };
//        if (Minecraft.getInstance().player == null) {
//            fociDropDown.disabled = true;
//        }
//    }
//
//    @Override
//    public void draw(int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {
//        drawString(Minecraft.getInstance().fontRenderer, I18n.format("gui.nekeys.popup.thaumcraft.selectFocus"), x + 6, y + 4, fociDropDown.disabled ? 0xA0A0A0 : 0xFFFFFFFF);
//        Minecraft.getInstance().fontRenderer.drawSplitString(I18n.format("gui.nekeys.popup.thaumcraft.unavailable"), x + 10, y + 40, width - 20, 0xFFFFFFFF);
//
//        fociDropDown.x = x + 6;
//        fociDropDown.y = y + 16;
//        fociDropDown.width = width - 12;
//        fociDropDown.draw();
//    }
//
//    @Override
//    public boolean onClick(int mouseX, int mouseY) {
//        if (fociDropDown.onClick(mouseX, mouseY)) return true;
//        return super.onClick(mouseX, mouseY);
//    }
//
//    @Override
//    public void handleMouseInput() {
//        fociDropDown.handleMouseInput();
//        super.handleMouseInput();
//    }
//
//    @Override
//    public IVoiceCommand getCommand() {
//        return new SelectFocusVoiceCommand(name, rule, fociDropDown.selection == null ? "REMOVE" : getFociHash(fociDropDown.selection));
//    }
//
//    protected List<ItemStack> getFocis() {
//        if (Minecraft.getInstance().player == null) return Collections.emptyList();
//        List<ItemStack> fociList = new ArrayList<>();
//        ItemStack item;
//        Minecraft mc = Minecraft.getInstance();
//        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(mc.player);
//
//        for (int i = 0; i < baubles.getSlots(); ++i) {
//            if (!baubles.getStackInSlot(i).isEmpty() && baubles.getStackInSlot(i).getItem() instanceof ItemFocusPouch) {
//                item = baubles.getStackInSlot(i);
//                scanInventory(fociList, ((ItemFocusPouch) item.getItem()).getInventory(item));
//            }
//        }
//
//        for (int i = 0; i < 36; ++i) {
//            item = mc.player.inventory.mainInventory.get(i);
//            if (item.getItem() instanceof ItemFocus) {
//                String sh = ((ItemFocus) item.getItem()).getSortingHelper(item);
//                if (sh == null) {
//                    continue;
//                }
//                fociList.add(item);
//            }
//
//            if (item.getItem() instanceof ItemFocusPouch) {
//                scanInventory(fociList, ((ItemFocusPouch) item.getItem()).getInventory(item));
//            }
//            //noinspection Duplicates
//            if (item.getItem() instanceof ICaster) {
//                ICaster caster = (ICaster) item.getItem();
//                ItemStack fociItem = caster.getFocusStack(item);
//                if (fociItem != null)
//                    fociList.add(fociItem);
//            }
//        }
//
//        ItemStack itemOffhand = mc.player.getHeldItemOffhand();
//        //noinspection Duplicates
//        if (itemOffhand.getItem() instanceof ICaster) {
//            ICaster caster = (ICaster) itemOffhand.getItem();
//            ItemStack fociItem = caster.getFocusStack(itemOffhand);
//            if (fociItem != null) fociList.add(fociItem);
//        }
//
//
//        return fociList;
//    }
//
//    protected void scanInventory(List<ItemStack> fociList, NonNullList inv) {
//        ItemStack item;
//        for (int i = 0; i < inv.size(); ++i) {
//            item = (ItemStack) inv.get(i);
//            if (item.getItem() instanceof ItemFocus) {
//                String hash = ((ItemFocus) item.getItem()).getSortingHelper(item);
//                if (hash != null) {
//                    fociList.add(item);
//                }
//            }
//        }
//    }
//
//    protected String getFociHash(ItemStack stack) {
//        return ((ItemFocus) stack.getItem()).getSortingHelper(stack);
//    }
//}
