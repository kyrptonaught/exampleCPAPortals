package net.kyrptonaught.exampleCPAPortals;

import net.fabricmc.api.ModInitializer;
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.kyrptonaught.customportalapi.portal.PortalIgnitionSource;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;

public class ExampleCPAPortalsMod implements ModInitializer {
    public static final String MOD_ID = "examplecpaportals";
    public static Identifier CROP_FRAMETESTER = new Identifier(MOD_ID, "growncrop");
    public static PortalIgnitionSource CROPGROWIGNITE = PortalIgnitionSource.CustomSource(new Identifier(MOD_ID, "cropgrow"));

    @Override
    public void onInitialize() {
        CustomPortalApiRegistry.registerPortalFrameTester(CROP_FRAMETESTER, GrownCropFrameTester::new);
        CustomPortalBuilder.beginPortal()
                .frameBlock(Blocks.FARMLAND)
                .destDimID(new Identifier("the_nether"))
                .tintColor(46, 5, 25)
                .customFrameTester(CROP_FRAMETESTER)
                .forcedSize(3, 3)
                .customIgnitionSource(CROPGROWIGNITE)
                .registerPortal();
    }
}