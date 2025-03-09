package com.circulation.m3t.item;

import com.circulation.m3t.M3Tweaker;
import com.circulation.m3t.Util.Function;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import project.studio.manametalmod.api.IQualityItem;
import project.studio.manametalmod.api.Quality;
import project.studio.manametalmod.api.weapon.IMagicItem;
import project.studio.manametalmod.core.ManaItemType;
import project.studio.manametalmod.magic.magicItem.IMagicEffect;

import java.util.*;

public class M3EBaublesBasic extends IMagicItem implements IQualityItem {

    private static Map<String,M3EBaublesAttribute> map = new HashMap<>();
    private static Map<String,Short> metaMap = new HashMap<>();

    public M3EBaublesBasic(String Name) {
        super(Name);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.setCreativeTab(M3Tweaker.CreativeTab);
    }

    @Override
    public String getMODID() {
        return com.circulation.m3t.M3Tweaker.MOD_ID;
    }

    public static void registerEffect(String id,String name,String icon,String tooltip, Short type,int quality, int level,long money,List<IMagicEffect> effects) {
        Quality quality1 = quality < 20 ? Quality.values()[quality] : Quality.Unknown;
        map.put(id + getMeta(id),new M3EBaublesAttribute(name, icon,tooltip, type,quality1, level,money, effects));

    }

    @SideOnly(Side.CLIENT)
    public static abstract class AdvancedTooltips{

        public AdvancedTooltips(String name){NAME = name;}
        public String NAME;

        public abstract String getTooltip(ItemStack item);
        public void register(){
            addAdvancedTooltips(NAME,this);
        }
    }

    private static Map<String,List<String>> ExTooltips = new HashMap<>();
    private static Map<String,List<AdvancedTooltips>> ExAdvancedTooltips = new HashMap<>();

    public static void addAdvancedTooltips(String name,AdvancedTooltips AT) {
        if (ExAdvancedTooltips.containsKey(name)){
            final List<AdvancedTooltips> list = ExAdvancedTooltips.get(name);
            list.add(AT);
            ExAdvancedTooltips.put(name,list);
        } else {
            ExAdvancedTooltips.put(name,new ArrayList<>(Collections.singletonList(AT)));
        }
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
        super.addInformation(itemStack, player, list, flag);

        if (itemStack.getItem() instanceof M3EBaublesBasic) {
            M3EBaublesBasic b = (M3EBaublesBasic) itemStack.getItem();
            if (!map.containsKey(b.Names + itemStack.getItemDamage())) return;
            list.add(Function.getText(map.get(b.Names + itemStack.getItemDamage()).tooltip));
        }

        if (ExTooltips.containsKey(this.Names)){
            list.add(ExTooltips.get(this.Names));
        }

        if (ExAdvancedTooltips.containsKey(this.Names)){
            for (AdvancedTooltips AT : ExAdvancedTooltips.get(this.Names)){
                list.add(AT.getTooltip(itemStack));
            }
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        if (itemStack.getItem() instanceof M3EBaublesBasic){
            M3EBaublesBasic b = (M3EBaublesBasic) itemStack.getItem();
            if (!map.containsKey(b.Names + itemStack.getItemDamage())) return this.Names;
            return Function.getText(map.get(b.Names + itemStack.getItemDamage()).Name);
        }
        return this.Names;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
//        if (!metaMap.containsKey(this.Names)) {
//            list.add(Items.apple.getIcon(new ItemStack(Items.apple),0));
//            return;
//        }
        final int lecher = metaMap.get(this.Names) + 1;
        for(int i = 0; i < lecher; ++i) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        this.icons = new IIcon[size];
        for(int i = 0; i < icons.length; ++i) {
            ItemStack itemStack = Function.getItemStack(map.get(this.Names + i).icon);
            M3Tweaker.logger.info(map.get(this.Names + i).icon);
            if (itemStack != null && itemStack.getItem() != null) {
                this.icons[i] = itemStack.getItem().getIcon(itemStack,0);
            } else if (map.get(this.Names + i).icon.startsWith("def:")) {
                this.icons[i] = register.registerIcon(map.get(this.Names + i).icon.substring("def:".length()));
            } else {
                this.icons[i] = Items.apple.getIcon(new ItemStack(Items.apple),0);
            }
        }
    }

    private static Short getMeta(String Name) {
        if (metaMap.containsKey(Name)) {
            metaMap.put(Name, (short) (metaMap.get(Name) + 1));
            return metaMap.get(Name);
        } else {
            metaMap.put(Name, (short) 0);
            return 0;
        }
    }

    public ManaItemType getType(ItemStack itemStack) {
        if (itemStack.getItem() instanceof M3EBaublesBasic){
            M3EBaublesBasic b = (M3EBaublesBasic) itemStack.getItem();
            if (!map.containsKey(b.Names + itemStack.getItemDamage())) return ManaItemType.valueOf(this.Names);
            return map.get(b.Names + itemStack.getItemDamage()).type;
        }
        return ManaItemType.Cloak;
    }

    public int getNeedLV(ItemStack itemStack, EntityPlayer entityPlayer) {
        if (itemStack.getItem() instanceof M3EBaublesBasic){
            M3EBaublesBasic b = (M3EBaublesBasic) itemStack.getItem();
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
        if (itemStack.getItem() instanceof M3EBaublesBasic){
            M3EBaublesBasic b = (M3EBaublesBasic) itemStack.getItem();
            if (!map.containsKey(b.Names + itemStack.getItemDamage())) return Collections.emptyList();
            return map.get(b.Names + itemStack.getItemDamage()).effects;
        }
        return Collections.emptyList();
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
    public Quality getQuality(ItemStack itemStack){
        M3EBaublesBasic b = (M3EBaublesBasic) itemStack.getItem();
        if (!map.containsKey(b.Names + itemStack.getItemDamage())) return super.getQuality(itemStack);
        return map.get(b.Names + itemStack.getItemDamage()).quality;
    }

    @Override
    public long getValue(ItemStack itemStack) {
        M3EBaublesBasic b = (M3EBaublesBasic) itemStack.getItem();
        if (!map.containsKey(b.Names + itemStack.getItemDamage())) return 0;
        return map.get(b.Names + itemStack.getItemDamage()).money;
    }

    private static class M3EBaublesAttribute {
        private final String Name;
        private final String icon;
        private final String tooltip;
        private final ManaItemType type;
        private final int level;
        private final long money;
        private final Quality quality;
        private final List<IMagicEffect> effects;

        private M3EBaublesAttribute(String name,String icon,String tooltip, Short type,Quality quality, int level,long money,List<IMagicEffect> effects) {
            this.Name = name;
            this.icon = icon;
            this.tooltip = tooltip;
            this.type = ManaItemType.getTypeFromID(type);
            this.level = Math.max(level,1);
            this.money = money;
            this.effects = effects;
            this.quality = quality;
        }

    }

    private static Map<String,M3EBaublesBasic> ItemMap = new HashMap<>();

    public static void registerAllBaubles(){
        Set<String> set = new HashSet<>(metaMap.keySet());
//        for (ManaItemType value : ManaItemType.values()) {
//            String Name = value.name();
//            set.remove(Name);
//            M3EBaublesBasic Basic = new M3EBaublesBasic(Name);
//            Basic.size = metaMap.containsKey(Name) ? metaMap.get(Name) + 1 : 0;
//            ItemMap.put(Name, Basic);
//            GameRegistry.registerItem(Basic, Name);
//        }
        set.forEach(Name -> {
            M3EBaublesBasic Basic = new M3EBaublesBasic(Name);
            Basic.size = metaMap.containsKey(Name) ? metaMap.get(Name) + 1 : 0;
            ItemMap.put(Name, Basic);
            GameRegistry.registerItem(Basic, Name);
        });
    }

    public static M3EBaublesBasic getBauble(String Name){
        return ItemMap.get(Name);
    }

}
