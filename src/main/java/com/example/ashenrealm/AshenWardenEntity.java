package com.example.ashenrealm;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

/**
 * Jefe del Reino Cenizo.
 * - Fase 1: melee + onda de choque cada pocos segundos.
 * - Fase 2 (< 50 % vida): se enfurece, la onda es más rápida e invoca esqueletos wither.
 */
public class AshenWardenEntity extends HostileEntity {
    private final ServerBossBar bossBar = new ServerBossBar(
            Text.translatable("entity.ashenrealm.ashen_warden"), BossBar.Color.PURPLE, BossBar.Style.NOTCHED_10);

    private int slamCooldown = 100;
    private int summonCooldown = 0;
    private boolean enraged = false;

    public AshenWardenEntity(EntityType<? extends HostileEntity> type, World world) {
        super(type, world);
        this.experiencePoints = 500;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.MAX_HEALTH, 300.0)
                .add(EntityAttributes.ATTACK_DAMAGE, 14.0)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.28)
                .add(EntityAttributes.FOLLOW_RANGE, 40.0)
                .add(EntityAttributes.KNOCKBACK_RESISTANCE, 1.0)
                .add(EntityAttributes.ARMOR, 8.0)
                .add(EntityAttributes.SCALE, 1.7);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 16.0f));
        this.goalSelector.add(4, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    protected void mobTick(ServerWorld world) {
        super.mobTick(world);
        bossBar.setPercent(getHealth() / getMaxHealth());

        LivingEntity target = getTarget();
        if (target == null) return;

        if (--slamCooldown <= 0 && squaredDistanceTo(target) < 100.0) {
            slam(world);
            slamCooldown = enraged ? 80 : 140;
        }

        if (getHealth() < getMaxHealth() / 2.0f) {
            if (!enraged) {
                enraged = true;
                world.playSound(null, getBlockPos(), SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.HOSTILE, 1.5f, 0.8f);
                summonMinions(world);
                summonCooldown = 400;
            } else if (--summonCooldown <= 0) {
                summonMinions(world);
                summonCooldown = 400;
            }
        }
    }

    private void slam(ServerWorld world) {
        world.playSound(null, getBlockPos(), SoundEvents.ENTITY_RAVAGER_ROAR, SoundCategory.HOSTILE, 2.0f, 0.6f);
        world.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, getX(), getY() + 0.2, getZ(), 120, 3.5, 0.1, 3.5, 0.05);
        world.spawnParticles(ParticleTypes.LARGE_SMOKE, getX(), getY() + 0.2, getZ(), 40, 3.5, 0.2, 3.5, 0.02);

        Box area = getBoundingBox().expand(7.0, 2.0, 7.0);
        for (PlayerEntity p : world.getEntitiesByClass(PlayerEntity.class, area, e -> e.isAlive() && !e.isSpectator())) {
            p.damage(world, world.getDamageSources().mobAttack(this), 10.0f);
            p.takeKnockback(1.4, getX() - p.getX(), getZ() - p.getZ());
            p.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 1), this);
        }
    }

    private void summonMinions(ServerWorld world) {
        int nearby = world.getEntitiesByClass(WitherSkeletonEntity.class, getBoundingBox().expand(24), e -> true).size();
        if (nearby >= 6) return;
        for (int i = 0; i < 2; i++) {
            WitherSkeletonEntity minion = EntityType.WITHER_SKELETON.create(world, SpawnReason.MOB_SUMMONED);
            if (minion == null) continue;
            minion.refreshPositionAndAngles(getX() + random.nextInt(7) - 3, getY(), getZ() + random.nextInt(7) - 3,
                    random.nextFloat() * 360f, 0f);
            minion.setTarget(getTarget());
            world.spawnEntity(minion);
        }
    }

    @Override
    public boolean canHaveStatusEffect(StatusEffectInstance effect) {
        return !effect.getEffectType().equals(StatusEffects.WITHER) && super.canHaveStatusEffect(effect);
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false;
    }

    // ---- Barra de jefe ----
    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        bossBar.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        bossBar.removePlayer(player);
    }

    // ---- Sonidos ----
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_WITHER_SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_WITHER_SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WITHER_SKELETON_DEATH;
    }
}
