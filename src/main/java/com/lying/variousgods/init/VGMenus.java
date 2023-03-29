package com.lying.variousgods.init;

import com.lying.variousgods.client.gui.menu.MenuAltar;
import com.lying.variousgods.client.gui.menu.MenuAltarDeity;
import com.lying.variousgods.client.gui.menu.MenuAltarStart;
import com.lying.variousgods.reference.Reference;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class VGMenus
{
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Reference.ModInfo.MOD_ID);
    
    public static final RegistryObject<MenuType<MenuAltar>> ALTAR_MENU = MENUS.register("altar", () -> new MenuType<MenuAltar>(MenuAltarStart::new));
    public static final RegistryObject<MenuType<MenuAltar>> DEITY_MENU = MENUS.register("altar_deity", () -> new MenuType<MenuAltar>(MenuAltarDeity::new));
    
	public static MenuType<?> register(String nameIn, MenuType<?> menuIn)
	{
		MENUS.register(Reference.ModInfo.MOD_ID+"."+nameIn, () -> menuIn);
		return menuIn;
	}
}
