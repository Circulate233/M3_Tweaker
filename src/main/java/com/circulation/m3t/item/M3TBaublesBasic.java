package com.circulation.m3t.item;

import com.circulation.m3t.M3Tweaker;
import com.circulation.m3t.Util.Function;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ShortMap;
import it.unimi.dsi.fastutil.objects.Object2ShortOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
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
import project.studio.manametalmod.api.IQualityItem;
import project.studio.manametalmod.api.Quality;
import project.studio.manametalmod.api.weapon.IMagicItem;
import project.studio.manametalmod.core.ManaItemType;
import project.studio.manametalmod.magic.magicItem.IMagicEffect;

import java.util.List;
import java.util.Map;

public class M3TBaublesBasic extends IMagicItem implements IQualityItem {

    private static final Map<String, M3EBaublesAttribute> map = new Object2ObjectOpenHashMap<>();
    private static final Object2ShortMap<String> metaMap = new Object2ShortOpenHashMap<>();

    public M3TBaublesBasic(@NotNull String Name) {
        super(Name);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.setCreativeTab(M3Tweaker.CreativeTab);
    }

    @Override
    public String getMODID() {
        return com.circulation.m3t.M3Tweaker.MOD_ID;
    }

    public static void registerEffect(String id, String name, String icon, String tooltip, Short type, int quality, int level, long money, List<IMagicEffect> effects) {
        Quality quality1 = quality < 20 ? Quality.values()[quality] : Quality.Unknown;
        map.put(id + getMeta(id), new M3EBaublesAttribute(name, icon, tooltip, type, quality1, level, money, effects));

    }

    @SideOnly(Side.CLIENT)
    public static abstract class AdvancedTooltips {

        public AdvancedTooltips(String name) {
            NAME = name;
        }

        public String NAME;

        public abstract String getTooltip(ItemStack item);

        public void register() {
            addAdvancedTooltips(NAME, this);
        }
    }

    private static final Map<String, List<String>> ExTooltips = new Object2ObjectOpenHashMap<>();
    private static final Map<String, List<AdvancedTooltips>> ExAdvancedTooltips = new Object2ObjectOpenHashMap<>();

    public static void addAdvancedTooltips(String name, AdvancedTooltips AT) {
        if (ExAdvancedTooltips.containsKey(name)) {
            final List<AdvancedTooltips> list = ExAdvancedTooltips.get(name);
            list.add(AT);
            ExAdvancedTooltips.put(name, list);
        } else {
            ExAdvancedTooltips.put(name, new ObjectArrayList<>(ObjectLists.singleton(AT)));
        }
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
        super.addInformation(itemStack, player, list, flag);

        if (itemStack.getItem() instanceof M3TBaublesBasic b) {
            if (!map.containsKey(b.Names + itemStack.getItemDamage())) return;
            list.add(Function.getText(map.get(b.Names + itemStack.getItemDamage()).tooltip));
        }

        if (ExTooltips.containsKey(this.Names)) {
            list.add(ExTooltips.get(this.Names));
        }

        if (ExAdvancedTooltips.containsKey(this.Names)) {
            for (AdvancedTooltips AT : ExAdvancedTooltips.get(this.Names)) {
                list.add(AT.getTooltip(itemStack));
            }
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        if (itemStack.getItem() instanceof M3TBaublesBasic b) {
            if (!map.containsKey(b.Names + itemStack.getItemDamage())) return this.Names;
            return Function.getText(map.get(b.Names + itemStack.getItemDamage()).Name);
        }
        return this.Names;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        final int lecher = metaMap.getShort(this.Names) + 1;
        for (int i = 0; i < lecher; ++i) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        this.icons = new IIcon[size];
        for (int i = 0; i < icons.length; ++i) {
            ItemStack itemStack = Function.getItemStack(map.get(this.Names + i).icon);
            if (itemStack != null && itemStack.getItem() != null) {
                this.icons[i] = itemStack.getItem().getIcon(itemStack, 0);
            } else if (map.get(this.Names + i).icon.startsWith("def:")) {
                this.icons[i] = register.registerIcon(map.get(this.Names + i).icon.substring("def:".length()));
            } else {
                this.icons[i] = Items.apple.getIcon(new ItemStack(Items.apple), 0);
            }
        }
    }

    private static Short getMeta(String Name) {
        if (metaMap.containsKey(Name)) {
            metaMap.put(Name, (short) (metaMap.getShort(Name) + 1));
            return metaMap.getShort(Name);
        } else {
            metaMap.put(Name, (short) 0);
            return 0;
        }
    }

    public ManaItemType getType(ItemStack itemStack) {
        if (itemStack.getItem() instanceof M3TBaublesBasic b) {
            if (!map.containsKey(b.Names + itemStack.getItemDamage())) return ManaItemType.valueOf(this.Names);
            return map.get(b.Names + itemStack.getItemDamage()).type;
        }
        return ManaItemType.Cloak;
    }

    public int getNeedLV(ItemStack itemStack, EntityPlayer entityPlayer) {
        if (itemStack.getItem() instanceof M3TBaublesBasic b) {
            if (!map.containsKey(b.Names + itemStack.getItemDamage())) return 1;
            return map.get(b.Names + itemStack.getItemDamage()).level;
        }
        return 1;
    }

    @Override
    public int TypeCount() {
        return 0;
    }

    @Override
    public List<IMagicEffect> getItemEffect(ItemStack itemStack) {
        if (itemStack.getItem() instanceof M3TBaublesBasic b) {
            if (!map.containsKey(b.Names + itemStack.getItemDamage())) return ObjectLists.emptyList();
            return map.get(b.Names + itemStack.getItemDamage()).effects;
        }
        return ObjectLists.emptyList();
    }

    @Override
    public void onEquipment(ItemStack itemStack, EntityLivingBase entityLivingBase) {

    }

    @Override
    public void onDisrobe(ItemStack itemStack, EntityLivingBase entityLivingBase) {

    }

    @Override
    public void onBeAttack(ItemStack itemStack, EntityPlayer entityPlayer, EntityLivingBase entityLivingBase, DamageSource damageSource) {

    }

    @Override
    public void onAttack(ItemStack itemStack, EntityPlayer entityPlayer, EntityLivingBase entityLivingBase, DamageSource damageSource) {

    }

    @Override
    public void onCrit(ItemStack itemStack, EntityPlayer entityPlayer, EntityLivingBase entityLivingBase, DamageSource damageSource) {

    }

    @Override
    public Quality getQuality(ItemStack itemStack) {
        if (itemStack.getItem() instanceof M3TBaublesBasic b) {
            if (map.containsKey(b.Names + itemStack.getItemDamage())) {
                return map.get(b.Names + itemStack.getItemDamage()).quality;
            }
        }
        return super.getQuality(itemStack);
    }

    @Override
    public long getValue(ItemStack itemStack) {
        if (itemStack.getItem() instanceof M3TBaublesBasic b) {
            if (map.containsKey(b.Names + itemStack.getItemDamage())) {
                return map.get(b.Names + itemStack.getItemDamage()).money;
            }
        }
        return 0;
    }

    private record M3EBaublesAttribute(String Name, String icon, String tooltip, ManaItemType type, Quality quality,
                                       int level, long money, List<IMagicEffect> effects) {
        private M3EBaublesAttribute(String Name, String icon, String tooltip, short type, Quality quality, int level, long money, List<IMagicEffect> effects) {
            this(Name, icon, tooltip, ManaItemType.getTypeFromID(type), quality, Math.max(level, 1), money, effects);
        }
    }

    private static final Map<String, M3TBaublesBasic> ItemMap = new Object2ReferenceOpenHashMap<>();

    public static void registerAllBaubles() {
        metaMap.keySet().forEach(Name -> {
            M3TBaublesBasic Basic = new M3TBaublesBasic(Name);
            Basic.size = metaMap.containsKey(Name) ? metaMap.getShort(Name) + 1 : 0;
            ItemMap.put(Name, Basic);
            GameRegistry.registerItem(Basic, Name);
        });
    }

    public static M3TBaublesBasic getBauble(String Name) {
        return ItemMap.get(Name);
    }

}
