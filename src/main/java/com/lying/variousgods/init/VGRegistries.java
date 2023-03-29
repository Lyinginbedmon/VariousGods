package com.lying.variousgods.init;

import java.util.function.Supplier;

import com.lying.variousgods.deities.miracle.Miracle;
import com.lying.variousgods.deities.miracle.Miracles;
import com.lying.variousgods.deities.personality.ContextQuotient;
import com.lying.variousgods.deities.personality.ContextQuotients;
import com.lying.variousgods.deities.personality.Opinion;
import com.lying.variousgods.deities.personality.PersonalityTraits;
import com.lying.variousgods.reference.Reference;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public class VGRegistries
{
	public static final DeferredRegister<Miracle> MIRACLES					= DeferredRegister.create(Miracle.REGISTRY_KEY, Reference.ModInfo.MOD_ID);
	public static final Supplier<IForgeRegistry<Miracle>> MIRACLES_REGISTRY	= MIRACLES.makeRegistry(() -> (new RegistryBuilder<Miracle>()).hasTags());
	
	public static final DeferredRegister<ContextQuotient> QUOTIENTS						= DeferredRegister.create(ContextQuotient.REGISTRY_KEY, Reference.ModInfo.MOD_ID);
	public static final Supplier<IForgeRegistry<ContextQuotient>> QUOTIENTS_REGISTRY	= QUOTIENTS.makeRegistry(() -> (new RegistryBuilder<ContextQuotient>()));

	public static final DeferredRegister<Opinion> TRAITS					= DeferredRegister.create(Opinion.REGISTRY_KEY, Reference.ModInfo.MOD_ID);
	public static final Supplier<IForgeRegistry<Opinion>> TRAITS_REGISTRY	= TRAITS.makeRegistry(() -> (new RegistryBuilder<Opinion>()));
	
	public static void registerCustom(IEventBus modEventBus)
	{
		MIRACLES.register(modEventBus);
		QUOTIENTS.register(modEventBus);
		TRAITS.register(modEventBus);
		
		Miracles.init();
		ContextQuotients.init();
		PersonalityTraits.init();
	}
}
