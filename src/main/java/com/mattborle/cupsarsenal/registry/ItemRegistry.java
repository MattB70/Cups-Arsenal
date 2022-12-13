package com.mattborle.cupsarsenal.registry;

import com.mattborle.cupsarsenal.CupsArsenal;
import com.mattborle.cupsarsenal.items.LauncherItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

// Item registration using ReferredRegister. Items are initialized here as to avoid the hassle of static referencing.
public class ItemRegistry {
    // Create a Creative Mode menu tab for this mod
    public static class CreativeTab extends CreativeModeTab {
        private CreativeTab(int index, String label) {
            super(index, label);
        }
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ROD_LAUNCHER.get()); // Use an item as the tab icon
        }
        // Register the new Creative Mod menu tab.
        public static final CreativeTab instance = new CreativeTab(CreativeModeTab.TABS.length, CupsArsenal.MOD_ID);
    }

    // Get the item register, so we can tell the game about our items
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CupsArsenal.MOD_ID);

    // Register Rod Launcher with id rod_launcher and reference ROD_LAUNCHER.
    public static final RegistryObject<Item> ROD_LAUNCHER = ITEMS.register("rod_launcher",
            () -> new LauncherItem(new Item.Properties().tab(CreativeTab.instance)));
}
