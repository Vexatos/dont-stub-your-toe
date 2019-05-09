package vexatos.dsyt;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(Side.CLIENT)
@Mod(modid = Dsyt.MOD_ID, name = Dsyt.MOD_NAME, version = "@VERSION@", clientSideOnly = true,
	useMetadata = true, acceptedMinecraftVersions = "[1.12.2,)", acceptableRemoteVersions = "*")
public class Dsyt {

	public static final String
		MOD_ID = "dsyt",
		MOD_NAME = "Don't stub your toe!";

	private static boolean changed = false;

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent e) {
		if(e.phase == TickEvent.Phase.START && ModConfig.enabled && Minecraft.getMinecraft().player != null) {
			if(!changed) {
				if(!Minecraft.getMinecraft().gameSettings.autoJump && Minecraft.getMinecraft().player.isSprinting()) {
					changed = true;
					Minecraft.getMinecraft().gameSettings.autoJump = true;
				}
			} else if(!Minecraft.getMinecraft().player.isSprinting()) {
				Minecraft.getMinecraft().gameSettings.autoJump = false;
				changed = false;
			}
		}
	}

	@SubscribeEvent
	public static void onConfigChange(OnConfigChangedEvent e) {
		if(Dsyt.MOD_ID.equals(e.getModID())) {
			ConfigManager.sync(Dsyt.MOD_ID, Config.Type.INSTANCE);
		}
	}

	@Config(modid = Dsyt.MOD_ID, name = Dsyt.MOD_NAME)
	public static class ModConfig {

		@Config.Name("Enable the mod")
		@Config.Comment("Whether this mod should try to perform its sole task, which it would do with utmost diligence if this were set to 'true'.")
		public static boolean enabled = true;

	}
}
