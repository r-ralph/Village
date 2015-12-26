package com.example.examplemod;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityElf extends EntityTameable implements IRangedAttackMob {
    public EntityElf(World world) {
        super(world);
        this.setSize(0.6F, 1.8F);
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        //this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(4, new EntityAIArrowAttack(this, 1.0D, 20, 60, 10.0F));
        this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.tasks.addTask(6, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        //this.tasks.addTask(8, new EntityAIBeg(this, 8.0F));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, EntityOrc.class, 0, true));
        this.setTamed(false);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);

        if (this.isTamed()) {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
        } else {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
        }
    }

    public boolean isAIEnabled() {
        return true;
    }

    /**
     * Sets the active target the Task system uses for tracking
     */
    public void setAttackTarget(EntityLivingBase target) {
        super.setAttackTarget(target);
        if (target == null) {
            this.setAngry(false);
        } else if (!this.isTamed()) {
            this.setAngry(true);
        }
    }

    /**
     * main AI tick function, replaces updateEntityActionState
     */
    protected void updateAITick() {
        this.dataWatcher.updateObject(18, this.getHealth());
    }

    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(18, this.getHealth());
        this.dataWatcher.addObject(19, (byte) 0);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("Angry", this.isAngry());
        //p_70014_1_.setByte("CollarColor", (byte) this.getCollarColor());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tagCompound) {
        super.readEntityFromNBT(tagCompound);
        this.setAngry(tagCompound.getBoolean("Angry"));

        //if (p_70037_1_.hasKey("CollarColor", 99)) {
        //this.setCollarColor(p_70037_1_.getByte("CollarColor"));
        //}
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example,
     * zombies and skeletons use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate() {
        super.onLivingUpdate();

        /**
         if (!this.worldObj.isRemote && this.isShaking && !this.field_70928_h && !this.hasPath() && this.onGround) {
         this.field_70928_h = true;
         this.timeWolfIsShaking = 0.0F;
         this.prevTimeWolfIsShaking = 0.0F;
         this.worldObj.setEntityState(this, (byte) 8);
         }
         **/
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        super.onUpdate();

        if (this.func_70922_bv()) {
            this.numTicksToChaseTarget = 10;
        }
    }

    public boolean func_70922_bv() {
        return this.dataWatcher.getWatchableObjectByte(19) == 1;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damageSource, float damage) {
        if (this.isEntityInvulnerable()) {
            return false;
        } else {
            Entity entity = damageSource.getEntity();
            this.aiSit.setSitting(false);

            if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow)) {
                damage = (damage + 1.0F) / 2.0F;
            }

            return super.attackEntityFrom(damageSource, damage);
        }
    }

    public boolean attackEntityAsMob(Entity target) {
        return false;
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase entity, float range) {
        EntityArrow entityarrow = new EntityArrow(this.worldObj, this, entity, 1.6F, 2);
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
        int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());
        entityarrow.setDamage(10 + this.rand.nextGaussian() * 0.25D);

        if (i > 0)
        {
            entityarrow.setDamage(entityarrow.getDamage() + (double)i * 0.5D + 0.5D);
        }

        if (j > 0)
        {
            entityarrow.setKnockbackStrength(j);
        }

        this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.worldObj.spawnEntityInWorld(entityarrow);
    }

    public boolean interact(EntityPlayer entityPlayer) {
        ItemStack itemstack = entityPlayer.inventory.getCurrentItem();

        if (this.isTamed()) {
            // Tame済
            if (itemstack != null) {
                if (itemstack.getItem() instanceof ItemFood) {
                    // 食べ物
                    ItemFood itemfood = (ItemFood) itemstack.getItem();
                    if (itemfood.isWolfsFavoriteMeat() && this.dataWatcher.getWatchableObjectFloat(18) < 20.0F) {
                        if (!entityPlayer.capabilities.isCreativeMode) {
                            --itemstack.stackSize;
                        }
                        this.heal((float) itemfood.func_150905_g(itemstack));
                        if (itemstack.stackSize <= 0) {
                            entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, (ItemStack) null);
                        }

                        return true;
                    }
                }
                /**
                 else if (itemstack.getItem() == Items.dye) {
                 int i = BlockColored.func_150032_b(itemstack.getItemDamage());

                 if (i != this.getCollarColor()) {
                 this.setCollarColor(i);

                 if (!entityPlayer.capabilities.isCreativeMode && --itemstack.stackSize <= 0) {
                 entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, (ItemStack) null);
                 }

                 return true;
                 }
                 }
                 */
            }

            if (this.func_152114_e(entityPlayer) && !this.worldObj.isRemote && !this.isBreedingItem(itemstack)) {
                this.aiSit.setSitting(!this.isSitting());
                this.isJumping = false;
                this.setPathToEntity((PathEntity) null);
                this.setTarget((Entity) null);
                this.setAttackTarget((EntityLivingBase) null);
            }
        } else if (itemstack != null && itemstack.getItem() == Items.bone) {
            // 骨
            if (!entityPlayer.capabilities.isCreativeMode) {
                --itemstack.stackSize;
            }

            if (itemstack.stackSize <= 0) {
                entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, (ItemStack) null);
            }

            if (!this.worldObj.isRemote) {
                this.setTamed(true);
                this.setPathToEntity((PathEntity) null);
                this.setAttackTarget((EntityLivingBase) null);
                this.aiSit.setSitting(true);
                this.setHealth(20.0F);
                this.func_152115_b(entityPlayer.getUniqueID().toString());
                this.playTameEffect(true);
                this.worldObj.setEntityState(this, (byte) 7);
            }

            return true;
        }

        return super.interact(entityPlayer);
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots
     * or seeds depending on the animal type)
     */
    public boolean isBreedingItem(ItemStack itemStack) {
        return itemStack != null && (itemStack.getItem() instanceof ItemFood && ((ItemFood) itemStack.getItem()).isWolfsFavoriteMeat());
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk() {
        return 8;
    }

    @Override
    public EntityAgeable createChild(EntityAgeable p_90011_1_) {
        EntityElf elf = new EntityElf(this.worldObj);
        String s = this.func_152113_b();

        if (s != null && s.trim().length() > 0) {
            elf.func_152115_b(s);
            elf.setTamed(true);
        }

        return elf;
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn() {
        return false;
    }

    public boolean func_142018_a(EntityLivingBase ownerAttacker, EntityLivingBase owner) {
        // 攻撃を受けた時
        if (!(ownerAttacker instanceof EntityCreeper) && !(ownerAttacker instanceof EntityGhast)) {
            if (ownerAttacker instanceof EntityElf) {
                EntityElf entityelf = (EntityElf) ownerAttacker;

                if (entityelf.isTamed() && entityelf.getOwner() == owner) {
                    return false;
                }
            }

            return !(ownerAttacker instanceof EntityPlayer && owner instanceof EntityPlayer && !((EntityPlayer) owner).canAttackPlayer((EntityPlayer) ownerAttacker)) && (!(ownerAttacker instanceof EntityHorse) || !((EntityHorse) ownerAttacker).isTame());
        } else {
            return false;
        }
    }

    /**
     * Determines whether this wolf is angry or not.
     */
    public boolean isAngry() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 2) != 0;
    }

    /**
     * Sets whether this wolf is angry or not.
     */
    public void setAngry(boolean p_70916_1_) {
        byte b0 = this.dataWatcher.getWatchableObjectByte(16);

        if (p_70916_1_) {
            this.dataWatcher.updateObject(16, (byte) (b0 | 2));
        } else {
            this.dataWatcher.updateObject(16, (byte) (b0 & -3));
        }
    }


}
