package dev.boze.client.systems.modules.hud.core;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.font.IFontRender;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.HUDModule;
import dev.boze.client.systems.modules.client.HUD;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.trackers.TargetTracker;
import mapped.Class2894;
import mapped.Class2895;
import mapped.Class5926;
import mapped.Class5929;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TextRadar extends HUDModule implements Class5929 {
    public static final TextRadar INSTANCE = new TextRadar();
    public final BooleanSetting field634 = new BooleanSetting("Custom", false, "Use custom theme settings");
    public final BooleanSetting field639 = new BooleanSetting("Shadow", false, "Text shadow", this.field634);
    public final LinkedList<Class2895> ac = new LinkedList();
    private final IntSetting field626 = new IntSetting("Range", 64, 8, 256, 1, "Range to display players");
    private final BooleanSetting field627 = new BooleanSetting("Limit", false, "Limit players to display");
    private final IntSetting field628 = new IntSetting("MaxPlayers", 10, 1, 25, 1, "Max amount of nearest players to display", this.field627);
    private final BooleanSetting field629 = new BooleanSetting("Heads", true, "Show player heads");
    private final BooleanSetting field630 = new BooleanSetting("Health", true, "Show player health");
    private final BooleanSetting field631 = new BooleanSetting("ColorCode", true, "Color codes health", this.field630);
    private final BooleanSetting field632 = new BooleanSetting("Ping", true, "Show player ping");
    private final BooleanSetting field633 = new BooleanSetting("Pops", true, "Show player pops");
    private final ColorSetting field635 = new ColorSetting(
            "Name", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Name color", this.field634
    );
    private final ColorSetting field636 = new ColorSetting(
            "Text", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Text color", this.field634
    );
    private final ColorSetting field637 = new ColorSetting(
            "Info", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Info color", this.field634
    );
    private final ColorSetting field638 = new ColorSetting(
            "Brackets", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Bracket color", this.field634
    );
    private final MinMaxSetting field640 = new MinMaxSetting("Spacing", 1.5, 0.0, 3.0, 0.1, "Spacing between lines", this.field634);
    private final BozeDrawColor field641 = new BozeDrawColor(-47032);
    private final BozeDrawColor field642 = new BozeDrawColor(-12728);
    private final BozeDrawColor field643 = new BozeDrawColor(-14169088);
    private final java.util.ArrayList<PlayerEntity> ab = new java.util.ArrayList();
    float aa = 0.0F;

    public TextRadar() {
        super("TextRadar", "Shows a list of nearby players", 40.0, 40.0);
        this.field595.setValue(0.75);
    }

    private static boolean lambda$onRender$1(PlayerEntity var0) {
        return !pt.method1561(var0);
    }

    @EventHandler
    public void method2041(MovementEvent event) {
        this.ab.clear();
        this.ab.addAll(mc.world.getPlayers().stream().filter(this::lambda$onSendMovementPackets$0).collect(Collectors.toList()));
    }

    @Override
    public void method295(DrawContext context) {
        boolean bl;
        int[] nArray = new int[]{0};
        int[] nArray2 = new int[]{1};
        boolean bl2 = this.method2010() == 1 || this.method2010() == 2;
        boolean bl3 = bl = this.method2010() == 2 || this.method2010() == 4;
        List<ArrayList<Class2894>> list = this.field627.getValue() ? this.ab.stream().filter(TextRadar::lambda$onRender$1).sorted(Comparator.comparingDouble(arg_0 -> mc.player.distanceTo((Entity) arg_0)).reversed()).limit(this.field628.getValue()).map(this::method337).toList() : this.ab.stream().map(this::method337).toList();
        this.aa = (float) list.stream().mapToDouble(this::lambda$onRender$2).max().orElse(0.0);
        this.method314(this.aa);
        list.stream().sorted(Comparator.comparingDouble(arg_0 -> this.lambda$onRender$3(bl2, arg_0))).forEach(arg_0 -> this.lambda$onRender$4(bl, nArray, nArray2, arg_0));
        this.method316(nArray[0]);
    }

    private java.util.ArrayList<Class2894> method337(PlayerEntity var1) {
        java.util.ArrayList<Class2894> var5 = new java.util.ArrayList();
        if (this.field629.getValue()) {
            PlayerListEntry var6 = mc.getNetworkHandler().getPlayerListEntry(var1.getUuid());
            if (var6 != null) {
                var5.add(new Class2895(var6));
            }
        }

        var5.add(new Class2894(var1.getNameForScoreboard(), this.field634.getValue() ? this.field635.getValue() : HUD.INSTANCE.field2383.getValue()));
        var5.add(new Class2894("- [", this.field634.getValue() ? this.field638.getValue() : HUD.INSTANCE.field2383.getValue()));
        var5.add(
                new Class2894(
                        String.format("%.1f", mc.player.distanceTo(var1)), this.field634.getValue() ? this.field636.getValue() : HUD.INSTANCE.field2383.getValue()
                )
        );
        if (this.field630.getValue()) {
            var5.add(new Class2894("|", this.field634.getValue() ? this.field638.getValue() : HUD.INSTANCE.field2383.getValue()));
            BozeDrawColor var9 = this.field634.getValue() ? this.field636.getValue() : HUD.INSTANCE.field2383.getValue();
            double var7 = var1.getHealth();
            if (this.field631.getValue()) {
                if (var7 < 5.0) {
                    var9 = (BozeDrawColor) this.field641.method196(var9.field411);
                } else if (var7 < 20.0) {
                    var9 = (BozeDrawColor) this.field642.method196(var9.field411);
                } else {
                    var9 = (BozeDrawColor) this.field643.method196(var9.field411);
                }
            }

            var5.add(new Class2894(String.format("%.1f", var7), var9));
            var5.add(new Class2894("hp", this.field634.getValue() ? this.field637.getValue() : HUD.INSTANCE.field2383.getValue()));
        }

        if (this.field632.getValue()) {
            var5.add(new Class2894("|", this.field634.getValue() ? this.field638.getValue() : HUD.INSTANCE.field2383.getValue()));
            var5.add(
                    new Class2894(
                            Integer.toString(Class5926.method100(var1)), this.field634.getValue() ? this.field636.getValue() : HUD.INSTANCE.field2383.getValue()
                    )
            );
            var5.add(new Class2894("ms", this.field634.getValue() ? this.field637.getValue() : HUD.INSTANCE.field2383.getValue()));
        }

        if (this.field633.getValue() && TargetTracker.field1359.containsKey(var1.getNameForScoreboard())) {
            var5.add(new Class2894("|", this.field634.getValue() ? this.field638.getValue() : HUD.INSTANCE.field2383.getValue()));
            var5.add(
                    new Class2894(
                            "-" + TargetTracker.field1359.get(var1.getNameForScoreboard()),
                            this.field634.getValue() ? this.field636.getValue() : HUD.INSTANCE.field2383.getValue()
                    )
            );
            var5.add(new Class2894("pops", this.field634.getValue() ? this.field637.getValue() : HUD.INSTANCE.field2383.getValue()));
        }

        var5.add(new Class2894("]", this.field634.getValue() ? this.field638.getValue() : HUD.INSTANCE.field2383.getValue()));
        return var5;
    }

    private double method338(List<Class2894> var1) {
        double var5 = -IFontRender.method499().measureTextHeight(" ", this.field634.getValue() ? this.field639.getValue() : HUD.INSTANCE.field2384.getValue());

        for (Class2894 var8 : var1) {
            var5 += var8.method5662();
        }

        return var5;
    }

    @Override
    public void method332(DrawContext context) {
        if (this.field629.getValue()) {
            while (!this.ac.isEmpty()) {
                Class2895 var5 = this.ac.poll();
                var5.method5663(context);
            }
        }
    }

    private void lambda$onRender$4(boolean var1, int[] var2, int[] var3, List<Class2894> var4) {
        double var8 = this.method338(var4);
        if (HUD.INSTANCE.field2394.getValue()) {
            HUD.INSTANCE
                    .field2397
                    .method2252(
                            this.method1391() + (var1 ? this.method313() - var8 - 4.0 : 0.0),
                            this.method305() + (double) var2[0],
                            var8 + 4.0,
                            (IFontRender.method499().method502(this.field634.getValue() ? this.field639.getValue() : HUD.INSTANCE.field2384.getValue()))
                                    + (this.field634.getValue() ? this.field640.getValue() : HUD.INSTANCE.field2385.getValue()),
                            RGBAColor.field402
                    );
        }

        double var10 = this.method305() + (double) var2[0] + 0.5;
        double var12 = var1 ? this.method313() - var8 - 2.0 : 2.0;

        for (Class2894 var15 : var4) {
            var15.method5661(this.method1391() + var12, var10);
            var12 += var15.method5662();
        }

        var2[0] = (int) (
                (double) var2[0]
                        + (IFontRender.method499().method502(this.field634.getValue() ? this.field639.getValue() : HUD.INSTANCE.field2384.getValue()))
                        + (this.field634.getValue() ? this.field640.getValue() : HUD.INSTANCE.field2385.getValue())
        );
        var3[0]++;
    }

    private double lambda$onRender$3(boolean var1, List var2) {
        return var1 ? -this.method338(var2) : this.method338(var2);
    }

    private double lambda$onRender$2(List var1) {
        return this.method338(var1);
    }

    private boolean lambda$onSendMovementPackets$0(AbstractClientPlayerEntity var1) {
        return var1 != mc.player && mc.player.distanceTo(var1) <= (float) this.field626.getValue().intValue();
    }
}
