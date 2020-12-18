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
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;

@EventBusSubscriber(Dist.CLIENT)
@Mod(Dsyt.MOD_ID)
public class Dsyt {

	public static final String
		MOD_ID = "dsyt",
		MOD_NAME = "Don't stub your toe!";

	public static Dsyt INSTANCE;
	private static boolean changed = false;

	public Dsyt() {
		INSTANCE = this;

		if(FMLEnvironment.dist != Dist.CLIENT) {
			return;
		}

		FMLJavaModLoadingContext.get().getModEventBus().register(Settings.class);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Settings.config);
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent e) {
		if(e.phase == TickEvent.Phase.START && Settings.enabled && Minecraft.getInstance().player != null) {
			if(!changed) {
				if(!Minecraft.getInstance().gameSettings.autoJump &&
					(Settings.sprintKeyJump && Minecraft.getInstance().gameSettings.keyBindSprint.isKeyDown()
						|| Settings.sprintJump && Minecraft.getInstance().player.isSprinting())) {
					changed = true;
					Minecraft.getInstance().gameSettings.autoJump = true;
				}
			} else if(!(Settings.sprintKeyJump && Minecraft.getInstance().gameSettings.keyBindSprint.isKeyDown()
				|| Settings.sprintJump && Minecraft.getInstance().player.isSprinting())) {
				Minecraft.getInstance().gameSettings.autoJump = false;
				changed = false;
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class Settings {

		private static final ForgeConfigSpec config;
		private static final ForgeConfigSpec.BooleanValue _enabled;
		private static final ForgeConfigSpec.BooleanValue _sprintJump;
		private static final ForgeConfigSpec.BooleanValue _sprintKeyJump;

		public static boolean enabled = true;
		public static boolean sprintJump = true;
		public static boolean sprintKeyJump = true;

		static {
			ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

			_enabled = builder
				.comment("Whether this mod should try to perform its sole task, which it would do with utmost diligence should this be set to 'true'.")
				.define("enabled", true);
			_sprintJump = builder
				.comment("Whether auto jump should be activated while the player is sprinting.")
				.define("sprintJump", true);
			_sprintKeyJump = builder
				.comment("Whether auto jump should be activated while the player is holding down the configured sprint key, regardless of whether or not they are currently sprinting.")
				.define("sprintKeyJump", true);

			config = builder.build();
		}

		public static void update() {
			enabled = _enabled.get();
			sprintJump = _sprintJump.get();
			sprintKeyJump = _sprintKeyJump.get();
		}

		@SubscribeEvent
		public static void onConfigChange(ModConfig.ModConfigEvent e) {
			if(e.getConfig().getSpec() == Settings.config) {
				Settings.update();
			}
		}
	}
}
