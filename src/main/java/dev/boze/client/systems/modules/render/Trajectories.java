package dev.boze.client.systems.modules.render;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.renderer.Renderer3D;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.RGBASetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.render.trajectories.nz;
import dev.boze.client.utils.ItemEnchantmentUtils;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.RGBAColor;
import mapped.Class3062;
import mapped.Class3064;
import mapped.Class5919;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.projectile.thrown.*;
import net.minecraft.item.*;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;

public class Trajectories extends Module {
    public static final Trajectories INSTANCE = new Trajectories();
    private static final double field3804 = Math.toRadians(10.0);
    public final ColorSetting field3783 = new ColorSetting("Color", new BozeDrawColor(1694433280), "Color for hit fill");
    public final ColorSetting field3784 = new ColorSetting("Outline", new BozeDrawColor(-65536), "Color for hit outline");
    public final Class5919 field3805 = new Class5919();
    public final Class3064<Vector3d> field3806 = new Class3064<Vector3d>(Vector3d::new);
    private final RGBASetting field3778 = new RGBASetting("HeldOrigin", new RGBAColor(-21760), "Color for held origin");
    private final RGBASetting field3779 = new RGBASetting("HeldHit", new RGBAColor(-65536), "Color for held hit");
    private final RGBASetting field3780 = new RGBASetting("ThrownOrigin", new RGBAColor(-13958913), "Color for thrown origin");
    private final RGBASetting field3781 = new RGBASetting("ThrownHit", new RGBAColor(-65536), "Color for thrown hit");
    private final RGBASetting field3782 = new RGBASetting("Trail", new RGBAColor(-13958913), "Color for trails");
    private final FloatSetting field3785 = new FloatSetting("Width", 1.5F, 0.5F, 5.0F, 0.1F, "Line width", Trajectories::lambda$new$0);
    private final FloatSetting field3786 = new FloatSetting("TrailLinger", 0.0F, 0.0F, 5.0F, 0.05F, "Trail linger time in seconds");
    private final BooleanSetting field3787 = new BooleanSetting("AllPlayers", true, "Predict trajectories for all players");
    private final BooleanSetting field3788 = new BooleanSetting("Held", true, "Predict arrow trajectories");
    private final BooleanSetting field3789 = new BooleanSetting("Thrown", true, "Predict arrow trajectories");
    private final BooleanSetting field3790 = new BooleanSetting("Trails", true, "Draw trails behind projectiles");
    public final FloatSetting field3791 = new FloatSetting("Duration", 0.5F, 0.1F, 60.0F, 0.1F, "Trail duration", this.field3790);
    public final BooleanSetting field3792 = new BooleanSetting("Fade", true, "Fade trails", this.field3790);
    private final RGBASetting field3793 = new RGBASetting("TrailFade", new RGBAColor(53503), "Color for trail fade", this.field3792::getValue, this.field3790);
    private final BooleanSetting field3794 = new BooleanSetting("Arrows", true, "Predict arrow trajectories");
    private final BooleanSetting field3795 = new BooleanSetting("Pearls", true, "Predict pearl trajectories");
    private final BooleanSetting field3796 = new BooleanSetting("Tridents", true, "Predict trident trajectories");
    private final BooleanSetting field3797 = new BooleanSetting("Xp", false, "Predict xp trajectories");
    private final BooleanSetting field3798 = new BooleanSetting("Potions", false, "Predict potion trajectories");
    private final BooleanSetting field3799 = new BooleanSetting("Eggs", false, "Predict egg trajectories");
    private final BooleanSetting field3800 = new BooleanSetting("Snowball", false, "Predict snowball trajectories");
    private final BooleanSetting field3801 = new BooleanSetting("FishingRods", false, "Predict fishing rod trajectories");
    private final BooleanSetting field3802 = new BooleanSetting("Fireballs", false, "Predict fireball trajectories");
    private final BooleanSetting field3803 = new BooleanSetting("WitherSkulls", false, "Predict wither skull trajectories");
    private final List<nz> aa = new ArrayList();
    private final List<nz> ab = new ArrayList();
    public Renderer3D ac;

    public Trajectories() {
        super("Trajectories", "Predicts projectile trajectories", Category.Render);
    }

    private static boolean lambda$new$0() {
        return !MinecraftClient.IS_SYSTEM_MAC;
    }

    @EventHandler
    private void method2053(Render3DEvent var1) {
        if (MinecraftUtils.isClientActive()) {
            if (this.ac == null) {
                this.ac = new Renderer3D(false, false);
            }

            this.ac.field2166.field1594 = this.field3785.getValue();
            this.ac.method1217();
            if (this.field3790.getValue()) {
                for (nz var6 : this.ab) {
                    if (mc.world.getEntityById(var6.field3813) == null && var6.field3810.hasElapsed(this.field3786.getValue() * 1000.0F)) {
                        var6.method2061();
                    }
                }

                for (Entity var13 : mc.world.getEntities()) {
                    if (var13.age > 1 && this.method2055(var13)) {
                        this.method2058(var13, var1.field1951);
                    }
                }

                for (nz var14 : this.ab) {
                    var14.method2066(var1, this.field3782.getValue(), this.field3793.getValue());
                }
            }

            if (this.field3788.getValue()) {
                for (PlayerEntity var15 : mc.world.getPlayers()) {
                    if (this.field3787.getValue() || var15 == mc.player) {
                        this.method2059(var15, var1.field1951);

                        for (nz var8 : this.aa) {
                            var8.method2066(var1, this.field3778.getValue(), this.field3779.getValue());
                        }
                    }
                }
            }

            if (this.field3789.getValue()) {
                for (Entity var16 : mc.world.getEntities()) {
                    if (var16 instanceof ProjectileEntity && this.method2055(var16)) {
                        this.method2060(var16, var1.field1951);

                        for (nz var18 : this.aa) {
                            var18.method2066(var1, this.field3780.getValue(), this.field3781.getValue());
                        }
                    }
                }
            }

            this.ac.method1219(var1.matrix);
        }
    }

    private boolean method2054(Item var1) {
        if (var1 instanceof BowItem || var1 instanceof CrossbowItem) {
            return this.field3794.getValue();
        } else if (var1 instanceof EnderPearlItem) {
            return this.field3795.getValue();
        } else if (var1 instanceof TridentItem) {
            return this.field3796.getValue();
        } else if (var1 instanceof ExperienceBottleItem) {
            return this.field3797.getValue();
        } else if (var1 instanceof SplashPotionItem) {
            return this.field3798.getValue();
        } else if (var1 instanceof EggItem) {
            return this.field3799.getValue();
        } else if (var1 instanceof SnowballItem) {
            return this.field3800.getValue();
        } else {
            return var1 instanceof FishingRodItem ? this.field3801.getValue() : false;
        }
    }

    private boolean method2055(Entity var1) {
        if (var1 instanceof ArrowEntity) {
            return this.field3794.getValue();
        } else if (var1 instanceof EnderPearlEntity) {
            return this.field3795.getValue();
        } else if (var1 instanceof TridentEntity) {
            return this.field3796.getValue();
        } else if (var1 instanceof ExperienceBottleEntity) {
            return this.field3797.getValue();
        } else if (var1 instanceof PotionEntity) {
            return this.field3798.getValue();
        } else if (var1 instanceof EggEntity) {
            return this.field3799.getValue();
        } else if (var1 instanceof SnowballEntity) {
            return this.field3800.getValue();
        } else if (var1 instanceof FishingBobberEntity) {
            return this.field3801.getValue();
        } else if (var1 instanceof FireballEntity || var1 instanceof DragonFireballEntity) {
            return this.field3802.getValue();
        } else {
            return var1 instanceof WitherSkullEntity ? this.field3803.getValue() : false;
        }
    }

    private nz method2056() {
        for (nz var5 : this.aa) {
            if (var5.field3807.isEmpty()) {
                return var5;
            }
        }

        nz var6 = new nz(this);
        this.aa.add(var6);
        return var6;
    }

    private nz method2057() {
        for (nz var5 : this.ab) {
            if (var5.field3807.isEmpty()) {
                return var5;
            }
        }

        nz var6 = new nz(this);
        this.ab.add(var6);
        return var6;
    }

    private void method2058(Entity var1, double var2) {
        nz var7 = null;

        for (nz var9 : this.ab) {
            if (var9.field3813 == var1.getId()) {
                var7 = var9;
                break;
            }
        }

        if (var7 == null) {
            var7 = this.method2057();
            var7.field3813 = var1.getId();
        }

        Vector3d var10 = Class3062.method5990(this.field3806.method5993(), var1, var2);
        var7.method2064(var10);
        var7.field3810.reset();
    }

    private void method2059(PlayerEntity var1, double var2) {
        for (nz var8 : this.aa) {
            var8.method2061();
        }

        ItemStack var9 = var1.getMainHandStack();
        if (var9 == null) {
            var9 = var1.getOffHandStack();
        }

        if (var9 != null) {
            if (this.method2054(var9.getItem())) {
                if (this.field3805.method44(var1, var9, 0.0, var2)) {
                    this.method2056().method2062(var1 == mc.player && !FreeCam.INSTANCE.isEnabled());
                    if (var9.getItem() instanceof CrossbowItem && ItemEnchantmentUtils.getEnchantmentLevel(var9, Enchantments.MULTISHOT) > 0) {
                        if (!this.field3805.method44(var1, var9, -field3804, var2)) {
                            return;
                        }

                        this.method2056().method2062(var1 == mc.player && !FreeCam.INSTANCE.isEnabled());
                        if (!this.field3805.method44(var1, var9, field3804, var2)) {
                            return;
                        }

                        this.method2056().method2062(var1 == mc.player && !FreeCam.INSTANCE.isEnabled());
                    }
                }
            }
        }
    }

    private void method2060(Entity var1, double var2) {
        for (nz var8 : this.aa) {
            var8.method2061();
        }

        if (this.field3805.method46(var1, var2)) {
            this.method2056().method2062(false);
        }
    }
}
