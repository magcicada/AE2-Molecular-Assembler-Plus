package com.gasai.ccapplied.core.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.minecraft.client.resources.model.ModelResourceLocation;

import com.gasai.ccapplied.client.render.ExtremeMolecularAssemblerRenderer;

@OnlyIn(Dist.CLIENT)
public class InitAdditionalModels {

    public static void init(ModelEvent.RegisterAdditional event) {
        event.register(ModelResourceLocation.standalone(ExtremeMolecularAssemblerRenderer.LIGHTS_MODEL));
    }
}
