package com.lying.variousgods.entities;

import java.util.Optional;
import java.util.UUID;

import com.lying.variousgods.entities.ai.EntityAIGuardFollowOwner;
import com.lying.variousgods.entities.ai.EntityAIGuardOwnerHurtByTarget;
import com.lying.variousgods.entities.ai.EntityAIGuardOwnerHurtTarget;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MoveThroughVillageGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EntityGuardZombie extends Zombie implements IGuardMob
{
	protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(EntityGuardZombie.class, EntityDataSerializers.OPTIONAL_UUID);
	public AnimationState unearthAnimationState = new AnimationState();
	
	public EntityGuardZombie(EntityType<? extends EntityGuardZombie> p_34271_, Level p_34272_)
	{
		super(p_34271_, p_34272_);
	}
	
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(DATA_OWNERUUID_ID, Optional.empty());
	}
	
	protected void addBehaviourGoals()
	{
		this.goalSelector.addGoal(2, new ZombieAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(6, new MoveThroughVillageGoal(this, 1.0D, true, 4, this::canBreakDoors));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(ZombifiedPiglin.class));
		
		this.goalSelector.addGoal(6, new EntityAIGuardFollowOwner<>(this, 1.0D, 10.0F, 2.0F, false));
		this.targetSelector.addGoal(1, new EntityAIGuardOwnerHurtByTarget<>(this));
		this.targetSelector.addGoal(2, new EntityAIGuardOwnerHurtTarget<>(this));
	}
	
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		if(getOwnerUUID() != null)
			compound.putUUID("Owner", getOwnerUUID());
	}
	
	public void readAdditionalSaveData(CompoundTag compound)
	{
		super.readAdditionalSaveData(compound);
		if(compound.hasUUID("Owner"))
			setOwnerUUID(compound.getUUID("Owner"));
	}
	
	public boolean isSunSensitive() { return false; }
	public boolean convertsInWater() { return false; }
	public boolean isPreventingPlayerRest(Player player) { return false; }
	
	public boolean canAttack(LivingEntity entityIn) { return IGuardMob.isOwnerOrFriend(this, entityIn) ? false : super.canAttack(entityIn); }
	
	public UUID getOwnerUUID() { return this.entityData.get(DATA_OWNERUUID_ID).orElse(null); }
	
	public void setOwnerUUID(UUID idIn) { this.entityData.set(DATA_OWNERUUID_ID, Optional.ofNullable(idIn)); }
}
