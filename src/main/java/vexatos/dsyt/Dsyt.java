package vexatos.dsyt;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;

@EventBusSubscriber(Dist.CLIENT)
@Mod(Dsyt.MOD_ID)
public class Dsyt {

	public static final String
		MOD_ID = "dsyt",
		MOD_NAME = "Don't stub your toe!";

	private static boolean changed = false;

	public Dsyt() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Settings.config);
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
	}

	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent e) {
		if(e.phase == TickEvent.Phase.START && Settings.enabled && Minecraft.getInstance().player != null) {
			if(!changed) {
				if(!Minecraft.getInstance().gameSettings.autoJump && Minecraft.getInstance().player.isSprinting()) {
					changed = true;
					Minecraft.getInstance().gameSettings.autoJump = true;
				}
			} else if(!Minecraft.getInstance().player.isSprinting()) {
				Minecraft.getInstance().gameSettings.autoJump = false;
				changed = false;
			}
		}
	}

	@SubscribeEvent
	public static void onConfigChange(ModConfig.ModConfigEvent e) {
		if(e.getConfig().getSpec() == Settings.config) {
			Settings.update();
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class Settings {

		private static final ForgeConfigSpec config;
		private static final ForgeConfigSpec.BooleanValue _enabled;

		public static boolean enabled = true;

		static {
			ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

			_enabled = builder
				.comment("Whether this mod should try to perform its sole task, which it would do with utmost diligence if this were set to 'true'.")
				.define("enabled", true);

			config = builder.build();
		}

		public static void update() {
			enabled = _enabled.get();
		}

	}
}
