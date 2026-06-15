package com.gasai.ccapplied.core.client;

import java.util.HashMap;
import java.util.Map;


import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.style.StyleManager;

/**
 * CCApplied screen registration system, compatible with AE2
 */
public final class CCAppliedInitScreens {
    
    private static final Map<MenuType<?>, ScreenFactory<?, ?>> SCREEN_FACTORIES = new HashMap<>();
    
    private CCAppliedInitScreens() {}
    
    /**
     * Registers screen for specified menu type
     */
    public static <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void register(
            MenuType<M> type,
            ScreenFactory<M, U> factory,
            String stylePath) {
        
        SCREEN_FACTORIES.put(type, factory);
    }

    public static <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> ScreenFactory<M, U> get(
            MenuType<M> type) {
        @SuppressWarnings("unchecked")
        ScreenFactory<M, U> factory = (ScreenFactory<M, U>) SCREEN_FACTORIES.get(type);
        return factory;
    }
    
    /**
     * Screen factory for CCApplied
     */
    @FunctionalInterface
    public interface ScreenFactory<T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> {
        U create(T menu, Inventory playerInv, Component title, ScreenStyle style);
    }
}
