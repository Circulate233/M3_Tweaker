package com.circulation.m3t.crt;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.Util.M3TCrtReload;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import project.studio.manametalmod.core.CareerCore;
import project.studio.manametalmod.core.ItemEffect;
import project.studio.manametalmod.entity.nbt.ManaMetalModRoot;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.*;

@ZenClass(M3TCrtAPI.CrtClass + "CareerModifier")
public class CareerModifierHandler implements M3TCrtReload {

    // 职业基础属性修改
    private static final Map<Integer, CareerStatModifier> careerModifiers = new HashMap<>();
    private static final Map<Integer, CareerStatModifier> defCareerModifiers = new HashMap<>();

    // 职业点数效果修改
    private static final Map<String, PointEffectModifier> pointModifiers = new HashMap<>();
    private static final Map<String, PointEffectModifier> defPointModifiers = new HashMap<>();

    // 条件性职业效果
    private static final Map<Integer, List<ConditionalCareerEffect>> conditionalEffects = new HashMap<>();
    private static final Map<Integer, List<ConditionalCareerEffect>> defConditionalEffects = new HashMap<>();

    // 职业等级加成
    private static final Map<Integer, List<LevelBonus>> levelBonuses = new HashMap<>();

    @Override
    public void reload() {
        careerModifiers.clear();
        pointModifiers.clear();
        conditionalEffects.clear();
        levelBonuses.clear();
    }

    @Override
    public void postReload() {
        if (defCareerModifiers.isEmpty()) {
            // 保存原始数据
        }
        // 应用修改
    }

    /**
     * 修改职业基础属性
     */
    @ZenMethod
    public static void modifyCareerBaseStats(int careerID, int lvUpHP, float physicalAtk,
                                             float magicAtk, float arrowAtk, float holyAtk,
                                             float atkMultiplier, float defMultiplier,
                                             float critDmg, float hpReply, int penetrate,
                                             float maxDamage) {
        CareerStatModifier modifier = new CareerStatModifier();
        modifier.lvUpHP = lvUpHP;
        modifier.physicalAttack = physicalAtk;
        modifier.magicAttack = magicAtk;
        modifier.arrowAttack = arrowAtk;
        modifier.holyAttack = holyAtk;
        modifier.attackMultiplier = atkMultiplier;
        modifier.defenseMultiplier = defMultiplier;
        modifier.critDamage = critDmg;
        modifier.hpReply = hpReply;
        modifier.penetrate = penetrate;
        modifier.maxDamage = maxDamage;

        careerModifiers.put(careerID, modifier);
    }

    /**
     * 修改职业点数效果
     */
    @ZenMethod
    public static void modifyPointEffect(int careerID, String pointType,
                                         int hp, float moveSpeed, int physicalAtk,
                                         int magicAtk, int arrowAtk,
                                         int defense, int avoid, int crit,
                                         int manaMax, float hpReply) {
        String key = careerID + "_" + pointType;
        PointEffectModifier modifier = new PointEffectModifier();
        modifier.hp = hp;
        modifier.moveSpeed = moveSpeed;
        modifier.physicalAttack = physicalAtk;
        modifier.magicAttack = magicAtk;
        modifier.arrowAttack = arrowAtk;
        modifier.defense = defense;
        modifier.avoid = avoid;
        modifier.crit = crit;
        modifier.manaMax = manaMax;
        modifier.hpReply = hpReply;

        pointModifiers.put(key, modifier);
    }

    /**
     * 添加条件性职业效果（装备物品时）
     */
    @ZenMethod
    public static void addItemConditionalEffect(int careerID, IItemStack requiredItem,
                                                String effectType, float value) {
        ConditionalCareerEffect effect = new ConditionalCareerEffect();
        effect.conditionType = ConditionType.ITEM_EQUIPPED;
        effect.conditionValue = MineTweakerMC.getItemStack(requiredItem);
        effect.effectType = effectType;
        effect.effectValue = value;

        conditionalEffects.computeIfAbsent(careerID, k -> new ArrayList<>()).add(effect);
    }

    /**
     * 添加条件性职业效果（维度）
     */
    @ZenMethod
    public static void addDimensionConditionalEffect(int careerID, int dimensionID,
                                                     String effectType, float value) {
        ConditionalCareerEffect effect = new ConditionalCareerEffect();
        effect.conditionType = ConditionType.IN_DIMENSION;
        effect.conditionValue = dimensionID;
        effect.effectType = effectType;
        effect.effectValue = value;

        conditionalEffects.computeIfAbsent(careerID, k -> new ArrayList<>()).add(effect);
    }

    /**
     * 添加等级加成
     */
    @ZenMethod
    public static void addLevelBonus(int careerID, int level, String bonusType, float value) {
        LevelBonus bonus = new LevelBonus();
        bonus.level = level;
        bonus.bonusType = bonusType;
        bonus.bonusValue = value;

        levelBonuses.computeIfAbsent(careerID, k -> new ArrayList<>()).add(bonus);
    }

    /**
     * 应用职业修改（由Mixin调用）
     */
    public static void applyCareerModifiers(EntityPlayer player, ManaMetalModRoot root) {
        int careerID = root.carrer.getCareerType();

        // 应用基础属性修改
        CareerStatModifier statMod = careerModifiers.get(careerID);
        if (statMod != null) {
            root.carrer.LVUPaddHP += statMod.lvUpHP;
            root.carrer.physicalAttack += statMod.physicalAttack;
            root.carrer.magicAttack += statMod.magicAttack;
            root.carrer.arrowAttack += statMod.arrowAttack;
            root.carrer.holyAttack += statMod.holyAttack;
            root.carrer.attackMultiplier += statMod.attackMultiplier;
            root.carrer.defenseMultiplier += statMod.defenseMultiplier;
            root.carrer.critDamage += statMod.critDamage;
            root.carrer.hpReply += statMod.hpReply;
            root.carrer.penetrate += statMod.penetrate;
            root.carrer.MaxDamage += statMod.maxDamage;
        }

        // 应用条件效果
        List<ConditionalCareerEffect> effects = conditionalEffects.get(careerID);
        if (effects != null) {
            for (ConditionalCareerEffect effect : effects) {
                if (checkCondition(player, effect)) {
                    applyEffect(root, effect.effectType, effect.effectValue);
                }
            }
        }

        // 应用等级加成
        List<LevelBonus> bonuses = levelBonuses.get(careerID);
        if (bonuses != null) {
            int playerLevel = root.carrer.careerLV;
            for (LevelBonus bonus : bonuses) {
                if (playerLevel >= bonus.level) {
                    applyEffect(root, bonus.bonusType, bonus.bonusValue);
                }
            }
        }
    }

    /**
     * 修改点数效果（由Mixin调用）
     */
    public static void modifyPointEffects(EntityPlayer player, ManaMetalModRoot root,
                                          CareerCore.CareerPoint type, int count) {
        int careerID = root.carrer.getCareerType();
        String key = careerID + "_" + type.name();

        PointEffectModifier modifier = pointModifiers.get(key);
        if (modifier != null && count > 0) {
            // 应用自定义点数效果
            if (modifier.hp != 0) {
                root.carrer.addExtraHP(modifier.hp * count);
                CareerCore.setModPointUseRanUUID(player, ItemEffect.HP, modifier.hp * count);  // 修正这里
            }
            if (modifier.moveSpeed != 0) {
                CareerCore.setModPointUseRanUUID(player, ItemEffect.SPEED, modifier.moveSpeed * count);  // 修正这里
            }
            if (modifier.physicalAttack != 0) {
                root.carrer.addphysicalAttack(modifier.physicalAttack * count);
            }
            if (modifier.magicAttack != 0) {
                root.carrer.addmagicAttack(modifier.magicAttack * count);
            }
            if (modifier.arrowAttack != 0) {
                root.carrer.addarrowAttack(modifier.arrowAttack * count);
            }
            if (modifier.defense != 0) {
                root.defe.addDefe(modifier.defense * count);
            }
            if (modifier.avoid != 0) {
                root.avoid.addAvoid(modifier.avoid * count);
            }
            if (modifier.crit != 0) {
                root.crit.addCrit(modifier.crit * count);
            }
            if (modifier.manaMax != 0) {
                root.mana.addMagicMax(modifier.manaMax * count);
            }
            if (modifier.hpReply != 0) {
                root.carrer.addHpReply(modifier.hpReply * count);
            }
        }
    }

    private static boolean checkCondition(EntityPlayer player, ConditionalCareerEffect effect) {
        switch (effect.conditionType) {
            case ITEM_EQUIPPED:
                ItemStack requiredItem = (ItemStack) effect.conditionValue;
                for (int i = 0; i < player.inventory.armorInventory.length; i++) {
                    ItemStack armor = player.inventory.armorInventory[i];
                    if (armor != null && armor.isItemEqual(requiredItem)) {
                        return true;
                    }
                }
                return player.getHeldItem() != null && player.getHeldItem().isItemEqual(requiredItem);

            case IN_DIMENSION:
                return player.dimension == (Integer) effect.conditionValue;

            default:
                return false;
        }
    }

    private static void applyEffect(ManaMetalModRoot root, String effectType, float value) {
        switch (effectType.toLowerCase()) {
            case "physicaldamage":
                root.carrer.physicalAttack += value;
                break;
            case "magicdamage":
                root.carrer.magicAttack += value;
                break;
            case "arrowdamage":
                root.carrer.arrowAttack += value;
                break;
            case "attackmultiplier":
                root.carrer.attackMultiplier += value;
                break;
            case "defensemultiplier":
                root.carrer.defenseMultiplier += value;
                break;
            case "critdamage":
                root.carrer.critDamage += value;
                break;
            case "avoid":
                root.avoid.addAvoid((int) value);
                break;
            case "crit":
                root.crit.addCrit((int) value);
                break;
            case "defense":
                root.defe.addDefe((int) value);
                break;
            case "penetrate":
                root.carrer.penetrate += (int) value;
                break;
            case "hpreply":
                root.carrer.hpReply += value;
                break;
        }
    }

    // 内部类定义
    private static class CareerStatModifier {
        int lvUpHP = 0;
        float physicalAttack = 0;
        float magicAttack = 0;
        float arrowAttack = 0;
        float holyAttack = 0;
        float attackMultiplier = 0;
        float defenseMultiplier = 0;
        float critDamage = 0;
        float hpReply = 0;
        int penetrate = 0;
        float maxDamage = 0;
    }

    private static class PointEffectModifier {
        int hp = 0;
        float moveSpeed = 0;
        int physicalAttack = 0;
        int magicAttack = 0;
        int arrowAttack = 0;
        int defense = 0;
        int avoid = 0;
        int crit = 0;
        int manaMax = 0;
        float hpReply = 0;
    }

    private static class ConditionalCareerEffect {
        ConditionType conditionType;
        Object conditionValue;
        String effectType;
        float effectValue;
    }

    private static class LevelBonus {
        int level;
        String bonusType;
        float bonusValue;
    }

    private enum ConditionType {
        ITEM_EQUIPPED,
        IN_DIMENSION,
        HAS_POTION,
        HEALTH_BELOW
    }
}
