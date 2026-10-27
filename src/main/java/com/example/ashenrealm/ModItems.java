package com.example.ashenrealm;

import java.util.function.Function;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Rarity;

public class ModItems {

    public static final ToolMaterial ASHEN_MATERIAL = new ToolMaterial(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 3000, 9.0f, 5.0f, 20, ItemTags.NETHERITE_TOOL_MATERIALS);

    /** Llave que viaja entre el Overworld y el Reino Cenizo. */
    public static final Item ASHEN_KEY = register("ashen_key", AshenKeyItem::new,
            new Item.Settings().maxCount(1).rarity(Rarity.RARE));

    /** Núcleo que se encuentra en las ruinas; invoca al jefe en el altar. */
    public static final Item ASHEN_CORE = register("ashen_core", Item::new,
            new Item.Settings().rarity(Rarity.UNCOMMON));

    /** Ítem que suelta el jefe. */
    public static final Item ASHEN_BLADE = register("ashen_blade", AshenBladeItem::new,
            new Item.Settings().sword(ASHEN_MATERIAL, 3.0f, -2.4f).rarity(Rarity.EPIC).fireproof());

    private static Item register(String name, Function<Item.Settings, Item> factory, Item.Settings settings) {
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, AshenRealm.id(name));
        return Registry.register(Registries.ITEM, key, factory.apply(settings.registryKey(key)));
    }

    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(e -> e.add(ASHEN_BLADE));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(e -> {
            e.add(ASHEN_KEY);
            e.add(ASHEN_CORE);
        });
    }
}
