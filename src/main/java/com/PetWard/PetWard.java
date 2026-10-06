package com.PetWard;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.Mod;

@Mod(
    modid = PetWard.MODID,
    name = PetWard.NAME,
    version = PetWard.VERSION,
    acceptedMinecraftVersions = "[1.7.10]",
    acceptableRemoteVersions = "*"
)
public class PetWard {
    public static final String MODID = "petward";
    public static final String NAME = "PetWard";
    public static final String VERSION = "1.0.0";

    public PetWard() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onLivingAttack(LivingAttackEvent event) {
        if (event.entityLiving.worldObj.isRemote) {
            return;
        }

        DamageSource source = event.source;
        if (source.isExplosion()) {
            return;
        }

        Entity trueSource = source.getEntity();
        if (!(trueSource instanceof EntityPlayer)) {
            return;
        }

        if (isProtectedTamedAnimal(event.entityLiving)) {
            event.setCanceled(true);
        }
    }

    private boolean isProtectedTamedAnimal(EntityLivingBase entity) {
        if (entity instanceof EntityTameable) {
            return ((EntityTameable) entity).isTamed();
        }

        return entity instanceof EntityHorse && ((EntityHorse) entity).isTame();
    }
}
