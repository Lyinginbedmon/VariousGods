package com.lying.variousgods.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

public class VGDataGenerators
{
	public static void onGatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		generator.addProvider(event.includeServer(), new DeityProvider(generator, existingFileHelper));
		generator.addProvider(event.includeServer(), new VGMiracleTags(generator, existingFileHelper));
		generator.addProvider(event.includeServer(), new VGBlockTags(generator, existingFileHelper));
		generator.addProvider(event.includeServer(), new VGItemTags(generator, existingFileHelper));
		generator.addProvider(event.includeServer(), new VGEntityTags(generator, existingFileHelper));
		generator.addProvider(event.includeServer(), new VGRecipeProvider(generator));
	}
}
