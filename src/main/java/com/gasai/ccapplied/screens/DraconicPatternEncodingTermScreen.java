package com.gasai.ccapplied.screens;

import appeng.client.gui.me.common.MEStorageScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.VerticalButtonBar;
import appeng.client.Point;
import com.gasai.ccapplied.menus.DraconicPatternEncodingTermMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.Map;
import java.util.Locale;

public class DraconicPatternEncodingTermScreen extends MEStorageScreen<DraconicPatternEncodingTermMenu> {
    private static Field compositeWidgetsField;
    private static Field toolbarPositionField;
    private boolean toolbarGuardApplied = false;

    public DraconicPatternEncodingTermScreen(DraconicPatternEncodingTermMenu menu, Inventory inv, Component title, ScreenStyle style) {
        super(menu, inv, title, style);
        widgets.add("draconicEncodePattern", new DraconicEncodeButton(menu));
        var clear = new DraconicClearButton(menu);
        clear.setHalfSize(true);
        widgets.add("draconicClearPattern", clear);
    }

    @Override
    protected void updateBeforeRender() {
        ensureVerticalToolbarPosition();
        setTextContent("draconic_tier_label", Component.literal("Tier: " + menu.getTierText()).withStyle(getTierColor()));
        setTextContent("draconic_energy_label", Component.literal("Energy Cost: " + NumberFormat.getIntegerInstance(Locale.US).format(menu.getEnergyCost()) + " OP"));
        super.updateBeforeRender();
    }

    private ChatFormatting getTierColor() {
        return switch (menu.getTierOrdinal()) {
            case 2 -> ChatFormatting.RED;
            case 1 -> ChatFormatting.GOLD;
            case 0 -> ChatFormatting.LIGHT_PURPLE;
            default -> ChatFormatting.GRAY;
        };
    }

    @SuppressWarnings("unchecked")
    private void ensureVerticalToolbarPosition() {
        if (toolbarGuardApplied) {
            return;
        }
        try {
            if (compositeWidgetsField == null) {
                compositeWidgetsField = appeng.client.gui.WidgetContainer.class.getDeclaredField("compositeWidgets");
                compositeWidgetsField.setAccessible(true);
            }
            var compositeWidgets = (Map<String, Object>) compositeWidgetsField.get(this.widgets);
            var toolbar = compositeWidgets.get("verticalToolbar");
            if (toolbar instanceof VerticalButtonBar verticalToolbar) {
                if (toolbarPositionField == null) {
                    toolbarPositionField = VerticalButtonBar.class.getDeclaredField("position");
                    toolbarPositionField.setAccessible(true);
                }
                var position = toolbarPositionField.get(verticalToolbar);
                if (position == null) {
                    verticalToolbar.setPosition(new Point(-2, 6));
                }
            }
            toolbarGuardApplied = true;
        } catch (Exception ignored) {
        }
    }

    private static class DraconicEncodeButton extends appeng.client.gui.widgets.IconButton {
        private final DraconicPatternEncodingTermMenu menu;
        public DraconicEncodeButton(DraconicPatternEncodingTermMenu menu) {
            super(btn -> {
                if (menu.canEncode()) menu.encode();
            });
            this.menu = menu;
            this.setMessage(Component.translatable("gui.ccapplied.draconic_encode_pattern"));
        }
        @Override
        protected appeng.client.gui.Icon getIcon() {
            return appeng.client.gui.Icon.WHITE_ARROW_DOWN;
        }
    }

    private static class DraconicClearButton extends appeng.client.gui.widgets.IconButton {
        public DraconicClearButton(DraconicPatternEncodingTermMenu menu) {
            super(btn -> menu.clearAll());
            this.setMessage(Component.translatable("gui.ccapplied.draconic_clear_pattern"));
        }
        @Override
        protected appeng.client.gui.Icon getIcon() {
            return appeng.client.gui.Icon.CLEAR;
        }
    }
}
