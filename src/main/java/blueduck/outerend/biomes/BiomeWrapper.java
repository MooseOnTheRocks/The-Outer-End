package blueduck.outerend.biomes;

import net.minecraft.entity.EntityClassification;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.*;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.MobSpawnInfoBuilder;

import java.util.HashMap;

public class BiomeWrapper {
	public int grassColor = 0;
	public int skyColor = 0;
	public int waterColor = 0;
	public int waterFogColor = 0;
	public int fogColor = 0;
	public int foliageColor = 0;
	public float mobSpawnProbability = 0.5f;
	public float downfall = 0;
	public float scale = 0;
	public float temperature = 0;
	public float depth = 0;
	public Biome.Category category = Biome.Category.NONE;
	public Biome.RainType rainType = Biome.RainType.NONE;
	public BiomeGenerationSettings generationSettings = new BiomeGenerationSettingsBuilder(BiomeGenerationSettings.DEFAULT_SETTINGS).build();
	public final String modid;
	public final String name;
	
	public HashMap<MobSpawnInfo.Spawners, EntityClassification> spawners = new HashMap<>();
	
	public BiomeWrapper(ResourceLocation registryName) {
		modid = registryName.getNamespace();
		name = registryName.getPath();
	}
	
	public BiomeWrapper(ResourceLocation registryName, int grassColor, int skyColor, int waterColor, int waterFogColor, int fogColor, int foliageColor, float mobSpawnProbability, float downfall, float scale, float temperature, float depth, Biome.Category category, Biome.RainType rainType) {
		this.grassColor = grassColor;
		this.skyColor = skyColor;
		this.waterColor = waterColor;
		this.waterFogColor = waterFogColor;
		this.fogColor = fogColor;
		this.foliageColor = foliageColor;
		this.mobSpawnProbability = mobSpawnProbability;
		this.downfall = downfall;
		this.scale = scale;
		this.temperature = temperature;
		this.depth = depth;
		this.category = category;
		this.rainType = rainType;
		modid = registryName.getNamespace();
		name = registryName.getPath();
	}
	
	public BiomeWrapper withGenerationSettings(BiomeGenerationSettingsBuilder builder) {
		this.generationSettings = builder.build();
		return this;
	}
	
	public BiomeWrapper withCategory(Biome.Category category) {
		this.category = category;
		return this;
	}
	
	public BiomeWrapper addSpawn(EntityClassification classification, MobSpawnInfo.Spawners spawners) {
		this.spawners.put(spawners,classification);
		return this;
	}
	
	private MobSpawnInfo generateMobSpawnList() {
		final MobSpawnInfo.Builder[] builder = {new MobSpawnInfoBuilder(MobSpawnInfo.EMPTY).withCreatureSpawnProbability(mobSpawnProbability)};
		spawners.forEach((spawner,classification)->{
			builder[0] = builder[0].withSpawner(classification,spawner);
		});
		return builder[0].copy();
	}
	
	public Biome build() {
		return new Biome.Builder()
				.scale(scale)
				.temperature(temperature)
				.category(category)
				.depth(depth)
				.precipitation(rainType)
				.withMobSpawnSettings(generateMobSpawnList()).setEffects(
				new BiomeAmbience.Builder()
						.setFogColor(fogColor)
						.withFoliageColor(foliageColor)
						.setWaterColor(waterColor)
						.setWaterFogColor(waterFogColor)
						.withSkyColor(skyColor)
						.withGrassColor(grassColor)
						.setMoodSound(MoodSoundAmbience.DEFAULT_CAVE)
						.build()
		)
				.downfall(downfall)
				.withGenerationSettings(generationSettings)
				.build()
				.setRegistryName(new ResourceLocation(modid, name)
		);
	}
}
