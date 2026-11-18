package com.circulation.m3t.item;

import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;

import org.jetbrains.annotations.NotNull;

import com.circulation.m3t.M3Tweaker;
import com.circulation.m3t.Util.Function;
import com.github.bsideup.jabel.Desugar;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import project.studio.manametalmod.api.IQualityItem;
import project.studio.manametalmod.api.Quality;
import project.studio.manametalmod.api.weapon.IMagicItem;
import project.studio.manametalmod.core.ManaItemType;
import project.studio.manametalmod.magic.magicItem.IMagicEffect;

public class M3TBaublesBasic extends IMagicItem implements IQualityItem {

    private static final Map<String, ObjectList<M3EBaublesAttribute>> DEFINITIONS = new Object2ObjectOpenHashMap<>();
    private static final Quality[] QUALITIES = Quality.values();
    private final ObjectList<M3EBaublesAttribute> variants;

    public M3TBaublesBasic(@NotNull String Name) {
        super(Name);
        variants = DEFINITIONS.getOrDefault(Name, ObjectLists.emptyList());
        this.size = variants.size();
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.setCreativeTab(M3Tweaker.CreativeTab);
    }

    @Override
    public String getMODID() {
        return com.circulation.m3t.M3Tweaker.MOD_ID;
    }

    public static void registerEffect(String id, String name, String icon, String tooltip, Short type, int quality,
        int level, long money, List<IMagicEffect> effects) {
        Quality grade = quality >= 0 && quality < Math.min(20, QUALITIES.length) ? QUALITIES[quality] : Quality.Unknown;
        DEFINITIONS.computeIfAbsent(id, ignored -> new ObjectArrayList<>(1))
            .add(new M3EBaublesAttribute(name, icon, tooltip, type, grade, level, money, effects));
    }

    private static M3EBaublesAttribute getVariant(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof M3TBaublesBasic bauble) {
            int meta = stack.getItemDamage();
            if (meta >= 0 && meta < bauble.variants.size()) {
                return bauble.variants.get(meta);
            }
        }
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
        super.addInformation(itemStack, player, list, flag);

        M3EBaublesAttribute variant = getVariant(itemStack);
        if (variant != null) {
            list.add(Function.getText(variant.tooltip));
        }

    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        M3EBaublesAttribute variant = getVariant(itemStack);
        return variant != null ? Function.getText(variant.Name) : this.Names;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        for (int i = 0; i < size; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        this.icons = new IIcon[size];
        for (int i = 0; i < icons.length; ++i) {
            String icon = variants.get(i).icon;
            if (icon.startsWith("def:")) {
                this.icons[i] = register.registerIcon(icon.substring("def:".length()));
                continue;
            }
            ItemStack itemStack = Function.getItemStack(icon);
            if (itemStack != null && itemStack.getItem() != null) {
                this.icons[i] = itemStack.getItem()
                    .getIcon(itemStack, 0);
            } else {
                M3Tweaker.logger.warn("Unknown bauble icon '{}' for {} with metadata {}", icon, this.Names, i);
                this.icons[i] = Items.apple.getIcon(new ItemStack(Items.apple), 0);
            }
        }
    }

    public ManaItemType getType(ItemStack itemStack) {
        M3EBaublesAttribute variant = getVariant(itemStack);
        return variant != null ? variant.type : ManaItemType.Cloak;
    }

    public int getNeedLV(ItemStack itemStack, EntityPlayer entityPlayer) {
        M3EBaublesAttribute variant = getVariant(itemStack);
        return variant != null ? variant.level : 1;
    }

    @Override
    public int TypeCount() {
        return 0;
    }

    @Override
    public List<IMagicEffect> getItemEffect(ItemStack itemStack) {
        M3EBaublesAttribute variant = getVariant(itemStack);
        return variant != null ? variant.effects : ObjectLists.emptyList();
    }

    @Override
    public void onEquipment(ItemStack itemStack, EntityLivingBase entityLivingBase) {

    }

    @Override
    public void onDisrobe(ItemStack itemStack, EntityLivingBase entityLivingBase) {

    }

    @Override
    public void onBeAttack(ItemStack itemStack, EntityPlayer entityPlayer, EntityLivingBase entityLivingBase,
        DamageSource damageSource) {

    }

    @Override
    public void onAttack(ItemStack itemStack, EntityPlayer entityPlayer, EntityLivingBase entityLivingBase,
        DamageSource damageSource) {

    }

    @Override
    public void onCrit(ItemStack itemStack, EntityPlayer entityPlayer, EntityLivingBase entityLivingBase,
        DamageSource damageSource) {

    }

    @Override
    public Quality getQuality(ItemStack itemStack) {
        M3EBaublesAttribute variant = getVariant(itemStack);
        return variant != null ? variant.quality : super.getQuality(itemStack);
    }

    @Override
    public long getValue(ItemStack itemStack) {
        M3EBaublesAttribute variant = getVariant(itemStack);
        return variant != null ? variant.money : 0;
    }

    @Desugar
    private record M3EBaublesAttribute(String Name, String icon, String tooltip, ManaItemType type, Quality quality,
        int level, long money, List<IMagicEffect> effects) {

        private M3EBaublesAttribute(String Name, String icon, String tooltip, short type, Quality quality, int level,
            long money, List<IMagicEffect> effects) {
            this(Name, icon, tooltip, ManaItemType.getTypeFromID(type), quality, Math.max(level, 1), money, effects);
        }
    }

    private static final Map<String, M3TBaublesBasic> ItemMap = new Object2ReferenceOpenHashMap<>();

    public static void registerAllBaubles() {
        DEFINITIONS.keySet()
            .forEach(Name -> {
                M3TBaublesBasic Basic = new M3TBaublesBasic(Name);
                ItemMap.put(Name, Basic);
                GameRegistry.registerItem(Basic, Name);
            });
    }

    public static M3TBaublesBasic getBauble(String Name) {
        return ItemMap.get(Name);
    }

}
